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
import model.Group;

@SuppressWarnings("unchecked")
public class GroupDao<T extends Group> implements DaoInterface<T> {

	private ObservableList<T> listaGroup = FXCollections.observableArrayList();
	private DaoFactory daoFactory;
		
	private Connection con;

	public GroupDao(DaoFactory daoFactory, boolean... getAll) {
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
			pStat = con.prepareStatement("SELECT * FROM groups WHERE id = ?");
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
		return listaGroup;
	}

 
	@Override
	public void getAll() {	
		listaGroup.clear();
		PreparedStatement pStat = null;
		ResultSet rs = null;
		try {
			pStat = con.prepareStatement("SELECT * FROM groups ORDER BY groupName");
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
			CallableStatement cstmt = con.prepareCall("{call insertGroup(?, ?)}");
			cstmt.setString(1, item.getGroupName());
			cstmt.registerOutParameter(2, java.sql.Types.INTEGER);
			cstmt.execute();

			item.setId(cstmt.getLong(2));

			if (item.getId()>0) listaGroup.add(item); else Common.LOG.add("Błąd dodania nowej grupy !");

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return item.getId()>0;
	}


	@Override
	public boolean zmien(T item) {
		long wynik = 0;
		try {
			CallableStatement cstmt = con.prepareCall("{call updateGroup (?, ?, ?)}");
			cstmt.setLong(1, item.getId());
			cstmt.setString(2, item.getGroupName());
			cstmt.registerOutParameter(3, java.sql.Types.INTEGER);
			cstmt.execute();
			wynik = cstmt.getLong(3);
			if (wynik<=0) Common.LOG.add("Nie udało się zapisać zmian !");
		} catch (Exception e) {
		}
		
		return wynik>0;
	}

	@Override
	public T map(ResultSet rs) throws SQLException {
		T item = (T) new Group();

		item.setId(rs.getLong("id"));
		item.setGroupName(rs.getString("groupName"));
		item.setState(rs.getInt("state"));

		return item;

	}
}
