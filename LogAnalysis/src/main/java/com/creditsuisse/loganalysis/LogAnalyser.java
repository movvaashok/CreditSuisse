package com.creditsuisse.loganalysis;

import java.util.Hashtable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.creditsuisse.loganalysis.beans.EventLogBean;
import com.creditsuisse.loganalysis.dao.DAO;
import com.creditsuisse.loganalysis.utils.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author Ashok Movva
 *
 * @brief LogAnalyser will read input config file and fetches serverlogs file
 *        path. The server logs file is expected to have an array of json
 *        objects. The Json objects are saved in hashtable with "event id" as
 *        key when json objects are inserted into hashtable, if there is any
 *        event data with same event id in hashtable, then event duration is
 *        calculated and data is save to database.
 * 
 */
public class LogAnalyser {
	static Logger log = LogManager.getLogger();

	public static void main(String[] args) {
		if (args.length == 0) {
			log.error("Please specify application config file path.");
			return;
		}
		log.info("Application config file path:- " + args[0]);
		Hashtable<String, JsonObject> eventHash = new Hashtable<String, JsonObject>();
		Utils utils = new Utils();
		boolean readConfigFileFlag = utils.readConfigFile(args[0]);
		if (!readConfigFileFlag)
			return;
		String dbString = utils.getDbString();
		String dbTableName = utils.getDbtableName();
		String inputFile = utils.getSeverLogFile();
		DAO dao = new DAO(dbString, dbTableName);
		try {
			log.info("Input file path: " + inputFile);
			JsonArray logData = utils.readInputFile(inputFile);
			if (logData != null) {
				for (JsonElement eventData : logData) {
					JsonObject currentEventData = eventData.getAsJsonObject();
					String eventId = currentEventData.get("id").getAsString();
					if (eventHash.containsKey(eventId)) {
						JsonObject previousEventData = eventHash.get(eventId);
						EventLogBean eventBean = utils.createEventBean(previousEventData, currentEventData);
						dao.saveEventData(eventBean);
						eventHash.remove(eventId);
					} else {
						eventHash.put(eventId, currentEventData);
					}
				}
			} else {
				log.info("No data found in input file. " + inputFile);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			log.atDebug().log("Releasing database connection.");
			dao.terminateConnection();
		}

	}
}
