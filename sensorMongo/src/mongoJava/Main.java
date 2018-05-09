package mongoJava;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

	public static void main(String[] args) {
		try {
			Connection con = DriverManager.getConnection("jdbc:sqlanywhere:uid=dba;pwd=sid;DatabaseName=SIDDBv15");
			PreparedStatement ps = con.prepareStatement("insert into Variaveis (nomeVariavel) values('castanha');");
			ps.addBatch();
			ps.executeBatch();

			ps.close();

			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
