package edu.foo.tetrixmult.network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Properties;

public class Utils {
	
	public Properties getProperties() throws IOException {
		Properties prop = new Properties();
		InputStream is = getClass().getClassLoader().getResourceAsStream("conf.properties");
		if (is!=null) {
			prop.load(is);
		} else {
			throw new FileNotFoundException("Properties file not found.");
		}
		return prop;
	}
	
	public boolean testSocketPlayer(String host, int port) {
		try {
			InetAddress address = InetAddress.getByName(host);
			Socket connection = new Socket(address, port);
			
			BufferedOutputStream outputStream = new BufferedOutputStream(connection.getOutputStream());
			
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "US-ASCII");
	        outputStreamWriter.write((char)13);
	        outputStreamWriter.flush();
	        outputStream.close();
			connection.close();
		} catch (IOException e) {
			return true;
		}
    	return false;
	}
	
}
