package mongoJava;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.bson.Document;

import com.mongodb.DBObject;

import sensorMongo.MongoInteraction;

public class StorageSybaseProcess implements Runnable {
	Connection con;

	public StorageSybaseProcess() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		System.out.println("Teste a thread");
		try {
			while (true) {
				
				connectSQL();
				insertSQL();
				disconnectSQL();
				Thread.sleep(30000);
			}
		} catch (SQLException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			/*
			 * } catch (InterruptedException e) { // TODO Auto-generated catch block
			 * e.printStackTrace();
			 */
		}

	}

	public void connectSQL() throws SQLException {
		con = DriverManager.getConnection("jdbc:sqlanywhere:uid=sensor1;pwd=sid;DatabaseName=SIDDBv15");
	}

	public void insertSQL() {
		List<Document> documents = new MongoInteraction().getDocument();
		Iterator<Document> iterator = documents.iterator();
		CallableStatement stmt;
		while (iterator.hasNext()) {
			System.out.println("HasNext? "+iterator.hasNext());
			Document document = iterator.next();
			document.remove("_id");
			List list = new ArrayList(document.values());
			System.out.println("hora: " + list.get(0));
			System.out.println("temperatura: " + list.get(1));
			System.out.println("data: " + list.get(2));
			System.out.println("humidade: " + list.get(3));			
			try {
				stmt = con.prepareCall("{call dba.InsertSensor(?,?,?,?)}");
				stmt.setDate(1, processData(list.get(2).toString()));
			    stmt.setTime(2, processTime(list.get(0).toString()));
			    stmt.setString(3, list.get(1).toString());
			    stmt.setString(4, list.get(3).toString());
			    stmt.execute();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*PreparedStatement ps;
			try {
				ps = con.prepareStatement(
						"insert into dba.HumidadeTemperatura(dataMedicaoHT,horaMedicaoHT,valorMedicaoTemperatura,valorMedicaoHumidade) values('"
								+ processData(list.get(2).toString()) + "','" + processTime(list.get(0).toString())
								+ "','" + list.get(1) + "','" + list.get(3) + "');");
				ps.executeBatch();
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}
		new MongoInteraction().deleteMany();
	}

	public java.sql.Date processData(String dataMongo) {
		dataMongo = dataMongo.replaceAll("\\s+", "");
		java.util.Date myDate = new java.util.Date(dataMongo);
		java.sql.Date sqlDate = new java.sql.Date(myDate.getTime());
		return sqlDate;
	}

	public Time processTime(String timeMongo) {
		DateFormat sdf = new SimpleDateFormat("hh:mm:ss");
		Date date = null;
		try {
			date = sdf.parse(timeMongo);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Time time = new Time(date.getTime());
		return time;
	}

	public void disconnectSQL() throws SQLException {
		con.close();
	}

}
