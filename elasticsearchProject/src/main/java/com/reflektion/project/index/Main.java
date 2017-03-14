package com.reflektion.project.index;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.reflektion.project.readInput.Input;
import com.reflektion.project.readInput.CSVInput;

public class Main {
	private static final String CONFIG_FILE = "config.properties";

	private static final String FORMAT = "format";
	private static final String FILE_NAME = "filename";
	private static final String CSV = "csv";
	
	
	private static final String IP = "IP";
	private static final String PORT = "port";
	
	
	private static final String INDEX_NAME = "indexName";
	private static final String TYPE = "type";
	private static final String OP_TYPE = "opType";
	private static final String CREATE = "create";
	private static final int PORT_NO = 9300;
	static final Logger logger = LogManager.getLogger(Main.class.getName());

	
	
	public static void main(String[] args) {
		Properties prop = new Properties();
		InputStream inputStream = null;
		
		int portno = PORT_NO;
		boolean opTypeCreate = false;
		
		try {

			inputStream = new FileInputStream(CONFIG_FILE);

			// load a properties file
			prop.load(inputStream);

			// get the property value
			
			
			String filename = prop.getProperty(FILE_NAME);
			if (filename == null){
				logger.error("Property 'filename' required in "+CONFIG_FILE);
				System.exit(0);
			}
			
			Input input = null;
			String format = prop.getProperty(FORMAT);
			if (format == null){
				logger.error("Property 'format' required in "+CONFIG_FILE);
				System.exit(0);
			} else {
				
				if (format.equals(CSV)){
					input = new CSVInput(filename);
				} else {
					logger.error("Invalid Property 'format' in "+CONFIG_FILE);
					System.exit(0);
				}
			}
			
			String ip = prop.getProperty(IP);
			if (ip == null){
				logger.error("Property 'IP' required in "+CONFIG_FILE);
				System.exit(0);
			}
			String port = prop.getProperty(PORT);
			if (port == null){
				logger.debug("Property 'port' missing in "+CONFIG_FILE);
				logger.debug("Assuming default port 9300");
			} else {
				portno = Integer.parseInt(port);
			}
			String indexName = prop.getProperty(INDEX_NAME);
			if (indexName == null){
				logger.error("Property 'index' required in "+CONFIG_FILE);
				System.exit(0);
			}
			String type = prop.getProperty(TYPE);
			if (type == null){
				logger.error("Property 'type' required in "+CONFIG_FILE);
				System.exit(0);
			}
			String opType = prop.getProperty(OP_TYPE); 
			if (opType != null && opType.equals(CREATE)){
				opTypeCreate = true;
			}

			input.index( ip, portno, indexName, type, opTypeCreate);
			
			
		} catch (Exception e){
			logger.debug(e.getMessage());
		}

		
		
		
		
	}

}
