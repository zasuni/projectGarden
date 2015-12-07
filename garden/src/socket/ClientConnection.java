package socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import application.Common;
import model.EspModul;
import model.NoName;
import modelDao.Dao;

public class ClientConnection extends Thread {
	private Socket clientSocket;
	private BufferedWriter out;

	private BufferedReader in;
	private static String ipAddress;

	public ClientConnection(Socket clientSocket) {
		this.clientSocket = clientSocket;
		ipAddress = clientSocket.getRemoteSocketAddress().toString().replace("/", "");
	}

	public void stopClient() {
		try {
			clientSocket.close();
		} catch (IOException e) {
		}
		
	}
	
	public void run() {
		try {
			out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			String line;
			while((line = in.readLine()) != null){
				out.write("");
				Common.LOG.add("<Socket client> - " + ipAddress +": " + line);
				analizujDane(line);
			}
		} catch (IOException e) {
		} finally {
			stopClient();
		}
	}
	
	private void analizujDane(String msg) {
		if(msg.length()>0) {
			Character msgType = msg.charAt(0);
			System.out.println("msgType: "+msgType.toString());
			if(msgType.toString()=="0") {
			}
			if(msgType.toString().equals("0")) {
				String mac = msg.substring(1, 18);
				EspModul esp = Dao.daoEspModul.get(mac);
				if(esp!=null) {
					Common.LOG.add("Klient: "+esp.getModulName());
				} else {
					NoName noName = Dao.daoNoName.get(mac);
					if(noName==null) {
						noName = new NoName();
						noName.setMac(mac);
						noName.setGpioUsed(msg.substring(18, 34));
						noName.setGpioType(msg.substring(34, 50));
						Dao.daoNoName.zapisz(noName);
					}
				}
				
			}
			System.out.println("finito");
		} else {
			System.out.println("Ciag pusty !");
		}
	}

	
}
