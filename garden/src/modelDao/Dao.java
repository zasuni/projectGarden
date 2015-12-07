package modelDao;

import application.Common;
import model.EspDevice;
import model.EspModul;
import model.Group;
import model.NoName;

public class Dao {
	
	public final static EspDeviceDao<EspDevice> daoEspDevice = new EspDeviceDao<>(Common.daoFactory, false);
	public final static EspModulDao<EspModul> daoEspModul = new EspModulDao<EspModul>(Common.daoFactory, false);
	public final static GroupDao<Group> daoGroup = new GroupDao<Group>(Common.daoFactory, false);
	public final static NoNameDao<NoName> daoNoName = new NoNameDao<NoName>(Common.daoFactory, false);
	
}
