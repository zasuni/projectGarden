package application;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import controllers.LogController;
import controllers.MainController;
import controllers.StatusBarController;
import dao.DaoFactory;
import socket.Server;

public class Common {

	public static int PORT_NUMBER = 5030;
	public static Server SOCKET_SERVER = null;
	public static StatusBarController STATUS_BAR = null;
	public static LogController LOG = new LogController();
	public static MainController MAIN = null;
	public final static DaoFactory daoFactory = new DaoFactory();
	public static final String KOLOR_TLA = "-fx-background-color: #e5ffe5";
	
	
	public static String now() {
		DateFormat df = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		return df.format(new Date());
	}
}
