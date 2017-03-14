package com.reflektion.project.readInput;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;


/**
 * @author keert
 *
 *Abstract class that deals with inputs. It provides abstraction from reading different forms of input and it indexes documents
 *from a given source of input, and indexes into ElasticSearch using the elastic search API
 *
 */

public abstract class Input {

    static final Logger logger = LogManager.getLogger(Input.class.getName());
	private static final String CONFIG_FILE = "config.properties";

	public abstract String[] getHeaders();
	
	public abstract Map<String,Object> readNextDocument();
	
	
	/**
	 * 
	 * This method indexes the documents read from input into ElasticSearch
	 * 
	 *@param ip - IP address of the ElasticSearch instance
	 *@param port - port number of the ElasticSearch instance
	 *@param indexName - name of the index 
	 *@param type - The type the document belongs to
	 *@param opType - The type of operation (create or not)
	 */
	@SuppressWarnings("unchecked")
	public void index( String ip, int port, String indexName, String type, boolean opType){
		IndexResponse response = null;
		TransportClient transportClient = null;
		Client client = null;
		try {
			transportClient = new PreBuiltTransportClient(Settings.EMPTY);
			client = transportClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ip), port));

			logger.debug("Connected to elasticsearch instance "+ip+":"+port);
			int count  = 0;
			while (true){
				Map<String, Object> json = this.readNextDocument();
				if (json != null){
					response = client.prepareIndex(indexName, type).setCreate(opType)
							.setSource(json).get();
					logger.info( response.getId()+" "+json.toString());
				} else {
					logger.debug("End of file reached");
					break;
				}
			
				count++;
			}
			logger.debug("Success count : "+count);
			
		} catch (UnknownHostException e) {
			logger.error("Unknown host: Make sure ElasticSearch is running at specified host and port");
		} catch (Exception e) {
			logger.error("Exception: "+ e.getMessage());
			logger.error("Configure appropriate port number in "+CONFIG_FILE);
			logger.error("Exiting..");
		}

	}
}
