package mongoJava;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import mongoJava.ThreadSybaseProcessor;

import org.bson.Document;

import sensorMongo.MongoInteraction;
import sensorMongo.sensorMessages;

public class StorageSybaseProcess implements Runnable {
	Connection con;
	private MongoInteraction mongoInt = new MongoInteraction();

	public StorageSybaseProcess() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		System.out.println("Teste a thread");

		try {
			while (true) {
				Thread.sleep(30000);
				List<Double> humList = new ArrayList<Double>();
				List<Double> tempList = new ArrayList<Double>();
				if (!sensorMessages.auxClass.getLastHum().isEmpty()) {
					humList = sensorMessages.auxClass.getOutliers(sensorMessages.auxClass.getLastHum());
				}
				if (!sensorMessages.auxClass.getLastTemp().isEmpty())
					tempList = sensorMessages.auxClass.getOutliers(sensorMessages.auxClass.getLastTemp());
				if (!humList.isEmpty()) {
					for (Double v : humList)
						mongoInt.deleteOneDoc("humidity", v.toString());
				}
				System.out.println("Tamanho "+tempList.size());
				if (!tempList.isEmpty()) {
					for (Double v : tempList)
						mongoInt.deleteOneDoc("temperature", v.toString());
				}
				connectSQL();
				insertSQL();
				disconnectSQL();

			}
		} catch (SQLException | InterruptedException e) {
			run();
			// TODO Auto-generated catch block
			e.printStackTrace();
			/*
			 * } catch (InterruptedException e) { // TODO Auto-generated catch block
			 * e.printStackTrace();
			 */
		}

	}

	public void connectSQL() throws SQLException {
		System.out.println("Conect SQL!!!!!!!!");
		con = DriverManager.getConnection("jdbc:sqlanywhere:uid=sensor1;pwd=sid;DatabaseName=SIDDBv15");
	}

	public void insertSQL() {
		mongoInt.mongoConnect();
		List<Document> documents = mongoInt.getDocument();
		Iterator<Document> iterator = documents.iterator();
		CallableStatement stmt;
		while (iterator.hasNext()) {
			Document document = iterator.next();
			document.remove("_id");
			List list = new ArrayList(document.values());
			try {
				stmt = con.prepareCall("{call dba.InsertSensor(?,?,?,?)}");
				try {
					stmt.setDate(1, processData(list.get(2).toString()));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				stmt.setTime(2, processTime(list.get(0).toString()));
				stmt.setString(3, list.get(1).toString());
				stmt.setString(4, list.get(3).toString());
				stmt.execute();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*
			 * PreparedStatement ps; try { ps = con.prepareStatement(
			 * "insert into dba.HumidadeTemperatura(dataMedicaoHT,horaMedicaoHT,valorMedicaoTemperatura,valorMedicaoHumidade) values('"
			 * + processData(list.get(2).toString()) + "','" +
			 * processTime(list.get(0).toString()) + "','" + list.get(1) + "','" +
			 * list.get(3) + "');"); ps.executeBatch(); ps.close(); } catch (SQLException e)
			 * { // TODO Auto-generated catch block e.printStackTrace(); }
			 */
		}
		mongoInt.deleteMany();
		mongoInt.disconnectMongo();
	}

	@SuppressWarnings("deprecation")
	public java.sql.Date processData(String dataMongo) throws ParseException {
		dataMongo = dataMongo.replaceAll("\\s+", "");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date parsed = null;
		try {
	        parsed = sdf.parse(dataMongo);
	    } catch (ParseException e1) {
	        // TODO Auto-generated catch block
	        e1.printStackTrace();
	    }
	    java.sql.Date sqlDate = new java.sql.Date(parsed.getTime());
		return sqlDate;
	}

	public Time processTime(String timeMongo) {
		timeMongo = timeMongo.replaceAll("\\s+", "");
		//DateFormat sdf = new SimpleDateFormat("hh:mm:ss");
		//Date date = null;
		/*try {
			date = sdf.parse(timeMongo);
		} catch (ParseException e) {
			timeMongo+=":00";
			processTime(timeMongo);
			e.printStackTrace();
		}
		Time time = new Time(date.getTime());*/
		
		return java.sql.Time.valueOf(LocalTime.parse(timeMongo));
	}

	public void disconnectSQL() throws SQLException {
		con.close();
	}

}
