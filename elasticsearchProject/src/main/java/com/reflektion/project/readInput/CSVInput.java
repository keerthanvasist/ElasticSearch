package com.reflektion.project.readInput;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import au.com.bytecode.opencsv.CSVReader;


/**
 * @author keerthan
 * 
 * Reads the documents from a CSV file. 
 *
 */
public class CSVInput extends Input {
	CSVReader reader;
	private static final char SEPARATOR = ',';
	private String[] headers;
    static final Logger logger = LogManager.getLogger(CSVInput.class.getName());
	
	
	
	public CSVInput(String filename){
		try {
			 reader = new CSVReader(new FileReader(filename), SEPARATOR);
			 headers  = reader.readNext();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	
	/**
	 * This method reads one line from the CSV file and morphs it into a document(map)
	 * 
	 *@return a document in the form of a map 
	 */
	public Map<String, Object> readNextDocument() {
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			String[] document  = reader.readNext();
			if (document != null){
				for (int i = 0; i < document.length; i++){
					if (document[i] != null && document[i].length() != 0){
						map.put(headers[i], document[i]);
					}
				}
			} else {
				return null;
			}
			
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		logger.info("Document created: " + map.toString());
		return map;
	}


	public String[] getHeaders() {
		return Arrays.copyOf(headers, headers.length);
	}

	
}
