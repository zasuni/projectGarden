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
import model.EspDevice;

@SuppressWarnings("unchecked")
public class EspDeviceDao<T extends EspDevice> implements DaoInterface<T> {

	private ObservableList<T> listaDev = FXCollections.observableArrayList();
	private DaoFactory daoFactory;
		
	private Connection con;

	public EspDeviceDao(DaoFactory daoFactory, boolean... getAll) {
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
			pStat = con.prepareStatement("SELECT * FROM espDevices WHERE id = ?");
			pStat.setLong(1, id);
			rs = pStat.executeQuery();
			if (rs.next()) item = map(rs);
		} catch (SQLException e) {
		}
		return item;
	}


	@Override
	public T get(String mac) {
		return null;
	}


	@Override
	public ObservableList<T> getLista() {
		return listaDev;
	}

 
	@Override
	public void getAll() {	
		listaDev.clear();
		PreparedStatement pStat = null;
		ResultSet rs = null;
		try {
			pStat = con.prepareStatement("SELECT * FROM espDevices ORDER BY gpioNo");
			rs = pStat.executeQuery();
			while(rs.next()){
				listaDev.add(map(rs));
			}
		} catch (SQLException e) {
		}
	}
	
	@Override
	public boolean zapisz(T item) {
		try {
			CallableStatement cstmt = con.prepareCall("{call insertEspDevice(?, ?, ?, ?, ?)}");
			cstmt.setLong(1, item.getIdEspModul());
			cstmt.setInt(2, item.getGpioNo());
			cstmt.setString(3, item.getDeviceName());
			cstmt.setInt(4, item.getGpioType());
			cstmt.registerOutParameter(5, java.sql.Types.INTEGER);
			cstmt.execute();

			item.setId(cstmt.getLong(5));

			if (item.getId()>0) listaDev.add(item); else Common.LOG.add("Błąd dodania urządzenia !");

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
		T item = (T) new EspDevice();
		item.setId(rs.getLong("id"));
		item.setIdEspModul(rs.getInt("idEspModul"));
		item.setGpioNo(rs.getInt("gpioNo")); 
		item.setGpioType(rs.getInt("gpioType")); 
		item.setDeviceName(rs.getString("deviceName"));
		item.setGpioState(rs.getInt("gpioState"));
		return item;

	}
}
