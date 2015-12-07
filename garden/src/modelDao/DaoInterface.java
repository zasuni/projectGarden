package modelDao;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.ObservableList;
import model.BaseModel;

public interface DaoInterface<T extends BaseModel> {
	
	T get(long wartosc);
	T get(String wartosc);
	void getAll();
	ObservableList<T> getLista();
    
    boolean zapisz(T ent);
    boolean zmien(T ent);
    
    T map(ResultSet rs) throws SQLException;

}
