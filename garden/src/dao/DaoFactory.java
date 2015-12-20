package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import application.Common;

public class DaoFactory {
	private Connection con = null;

	public DaoFactory() {
		
	}
 
	public void makeConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://192.168.43.233/garden?user=gardenuser&password=garden@123");
            Common.LOG.add("Nawiązano połączenie z MySQL");
			Common.STATUS_BAR.dbServerStatus.set(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Connection getConnection() {
		return con;
	}
	
	public int status () {
		int wynik = 0;
		try {
			if(con.isClosed()) wynik=0; else wynik=1;
		} catch (Exception e) {
		}
		return wynik;
	}
	
	public void closeConnection () {
		try {
			con.isClosed();
			Common.LOG.add("Rozlaczono z serwerem MySQL");
			Common.STATUS_BAR.dbServerStatus.set(0);
		} catch (Exception e) {
		}
	}
	
	public boolean deleteRekord(long idRekord, String nazwaTabeli) {
		int wynik = 0;
		try {
			CallableStatement cstmt = con.prepareCall("{call deleteRekord (?, ?, ?)}");
			cstmt.setLong(1, idRekord);
			cstmt.setString(2, nazwaTabeli);
			cstmt.registerOutParameter(3, java.sql.Types.INTEGER);
			cstmt.execute();			
			wynik=cstmt.getInt(3);
			if (wynik==0) Common.LOG.add("Rekordu nie dało się usunąć !");					
		} catch (SQLException e) {
			e.printStackTrace();
			Common.LOG.add("Błąd usuwania rekordu !'");
		}		
		return wynik>0;
	}

}
