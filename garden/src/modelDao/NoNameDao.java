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
import model.NoName;

@SuppressWarnings("unchecked")
public class NoNameDao<T extends NoName> implements DaoInterface<T> {

	private ObservableList<T> listaGroup = FXCollections.observableArrayList();
	private DaoFactory daoFactory;
		
	private Connection con;

	public NoNameDao(DaoFactory daoFactory, boolean... getAll) {
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
			pStat = con.prepareStatement("SELECT * FROM noNames WHERE id = ?");
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
			pStat = con.prepareStatement("SELECT * FROM noNames WHERE mac = ?");
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
		return listaGroup;
	}

 
	@Override
	public void getAll() {	
		listaGroup.clear();
		PreparedStatement pStat = null;
		ResultSet rs = null;
		try {
			pStat = con.prepareStatement("SELECT * FROM noNames ORDER BY mac");
			rs = pStat.executeQuery();
			while(rs.next()){
				listaGroup.add(map(rs));
			}
		} catch (SQLException e) {
		}
	}
	
	@Override
	public boolean zapisz(T item) {
		try {
			CallableStatement cstmt = con.prepareCall("{call insertNoName(?, ?, ?, ?)}");
			cstmt.setString(1, item.getMac());
			cstmt.setString(2, item.getGpioUsed());
			cstmt.setString(3, item.getGpioType());
			cstmt.registerOutParameter(4, java.sql.Types.INTEGER);
			cstmt.execute();

			item.setId(cstmt.getLong(4));

			if (item.getId()>0) listaGroup.add(item); else Common.LOG.add("Błąd dodania urządzenia NONAME !");

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return item.getId()>0;
	}


	@Override
	public boolean zmien(T item) {
		return false;
	}

	@Override
	public T map(ResultSet rs) throws SQLException {
		T item = (T) new NoName();
		item.setId(rs.getLong("id"));
		item.setMac(rs.getString("mac"));
		item.setGpioUsed(rs.getString("gpioUsed"));
		item.setGpioType(rs.getString("gpioType"));
		return item;
	}
}
