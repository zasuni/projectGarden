package modelDao;
 
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.Common;
import dao.DaoFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.EspModul;
import model.NoName;

@SuppressWarnings("unchecked")
public class EspModulDao<T extends EspModul> implements DaoInterface<T> {

	private ObservableList<T> listaEsp = FXCollections.observableArrayList();
	private DaoFactory daoFactory;
		
	private Connection con;

	public EspModulDao(DaoFactory daoFactory, boolean... getAll) {
		boolean czyPobrac = getAll.length>0 ? getAll[0] : true;		
		if (daoFactory != null) {
			this.daoFactory = daoFactory;
			con = this.daoFactory.getConnection();

			if (con != null && czyPobrac) getAll();
		}
	}

	public DaoFactory getDaoFactory() {
		return daoFactory;
	}
	

	@Override
	public T get(long id) {
		PreparedStatement pStat = null;
		ResultSet rs = null;
		T item = null;
		try {
			pStat = con.prepareStatement("SELECT * FROM espModules WHERE id = ?");
			pStat.setLong(1, id);
			rs = pStat.executeQuery();
			if (rs.next()) item = map(rs);
		} catch (SQLException e) {
		}
		return item;
	}


	@Override
	public T get(String mac) {
		PreparedStatement pStat = null;
		ResultSet rs = null;
		T item = null;
		try {
			pStat = con.prepareStatement("SELECT * FROM espModules WHERE mac = ?");
			pStat.setString(1, mac);
			rs = pStat.executeQuery();
			if (rs.next()) item = map(rs);
		} catch (SQLException e) {
			System.err.println("blad: "+e.getMessage());
		}
		return item;
	}


	@Override
	public ObservableList<T> getLista() {
		return listaEsp;
	}

 
	@Override
	public void getAll() {	
	}
	
	@Override
	public boolean zapisz(T esp) {
		
		long kodBledu = 0;
		
		try {
		} catch (Exception e) {
		}

		return kodBledu==0;		
	}

	public boolean przypiszModul(T item, NoName noName) {
		try {
			CallableStatement cstmt = con.prepareCall("{call przypiszModul(?, ?, ?, ?)}");
			cstmt.setLong(1, noName.getId());
			cstmt.setLong(2, item.getIdGroup());
			cstmt.setString(3, item.getModulName());
			cstmt.registerOutParameter(4, java.sql.Types.INTEGER);
			cstmt.execute();

			System.out.println("noName.getId(): "+noName.getId());
			System.out.println("item.getIdGroup(): "+item.getIdGroup());
			System.out.println("item.getModulName()): "+item.getModulName());
			
			item.setId(cstmt.getLong(4));

			if (item.getId()>0) {
				listaEsp.add(item); 
				Dao.daoNoName.getLista().remove(noName);
				Dao.daoEspDevice.getAll();
			} else Common.LOG.add("Błąd dodania modułu !");

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return item.getId()>0;
	}

	@Override
	public boolean zmien(T esp) {

		boolean wynik = false;
		long kodBledu = 0;
		
		try {
			wynik=kodBledu==0;
		} catch (Exception e) {
		}
		
		return wynik;
	}

	@Override
	public T map(ResultSet rs) throws SQLException {
		T item = (T) new EspModul();

		item.setId(rs.getLong("id"));
		item.setIdGroup(rs.getLong("idGroup"));
		item.setMac(rs.getString("mac"));
		item.setModulName(rs.getString("modulName"));

		return item;

	}
}
