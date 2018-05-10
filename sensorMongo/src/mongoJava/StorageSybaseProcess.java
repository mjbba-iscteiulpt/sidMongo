package mongoJava;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
				//Thread.sleep(30000);
				connectSQL();
				insertSQL();
				processData();
				disconnectSQL();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		/*} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();*/
		}

	}

	public void connectSQL() throws SQLException {
		con = DriverManager.getConnection("jdbc:sqlanywhere:uid=sensor1;pwd=sid;DatabaseName=SIDDBv15");
	}

	public void insertSQL() {
		/*PreparedStatement ps;
		try {
			// TESTE

			ps = con.prepareStatement(
					"insert into dba.HumidadeTemperatura(dataMedicaoHT,horaMedicaoHT,valorMedicaoTemperatura,valorMedicaoHumidade) values('"
							+ msSqlDateFormat.format(date) + "','11:11:11','11','11');");
			ps.addBatch();
			ps.executeBatch();
			ps.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

	}

	public Date processData() {
		java.util.Date date = null;
		List<Document> auxList = new MongoInteraction().getDocument();
		
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse("2018-05-10");

			java.text.SimpleDateFormat msSqlDateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;

	}

	public void disconnectSQL() throws SQLException {
		con.close();
	}

}
