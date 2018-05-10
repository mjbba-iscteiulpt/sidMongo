package mongoJava;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StorageSybaseProcess implements Runnable {
	Connection con;

	public StorageSybaseProcess() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		System.out.println("Teste a thread");
		try {
			connectSQL();
			insertSQL();
			disconnectSQL();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

	public void connectSQL() throws SQLException {
			con = DriverManager.getConnection("jdbc:sqlanywhere:uid=sensor1;pwd=sid;DatabaseName=SIDDBv15");
			}

	public void insertSQL() {
		PreparedStatement ps;
		try {
			//TESTE
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate localDate = LocalDate.now();
			
			System.out.println(dtf.format(localDate));
			ps = con.prepareStatement(
					"insert into dba.HumidadeTemperatura(dataMedicaoHT,horaMedicaoHT,valorMedicaoTemperatura,valorMedicaoHumidade) values("+dtf.format(localDate)+",'11:11:11','11','11');");
			ps.addBatch();
			ps.executeBatch();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void processData() {

	}
	
	public void disconnectSQL() throws SQLException {
		con.close();
	}

}
