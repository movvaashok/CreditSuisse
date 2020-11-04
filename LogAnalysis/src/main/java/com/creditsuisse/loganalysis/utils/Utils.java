package com.creditsuisse.loganalysis.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.creditsuisse.loganalysis.beans.EventLogBean;
import com.google.gson.*;

public class Utils {
	static Logger log = LogManager.getLogger(Utils.class);
	private JsonObject configJson;

	public boolean readConfigFile(String configFilePath) {
		boolean retValue = false;
		log.debug("Reading application configuration file :- " + configFilePath);
		try {
			log.atDebug().log((new File(configFilePath).getCanonicalPath()));
			File file = new File(new File(configFilePath).getCanonicalPath());
			if (!configFilePath.isEmpty() && file.exists()) {
				FileReader inputFileReader = new FileReader(file.getAbsoluteFile());
				JsonParser jsonParser = new JsonParser();
				configJson = jsonParser.parse(inputFileReader).getAsJsonObject();
				retValue = true;
			} else {
				log.debug("Failed reading application configuration file :- " + configFilePath);
				if (file.exists())
					log.atDebug().log("Config file doesn't exists in the path :" + configFilePath);
				throw new FileNotFoundException("Config File doesn't exists in the path: " + configFilePath);
			}
		} catch (FileNotFoundException fe) {
			log.error(fe);
		} catch (JsonIOException e) {
			log.error(e);
		} catch (JsonSyntaxException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		}
		return retValue;
	}

	/**
	 * @param filePath log file (input) path
	 * @return jsonObject of the event logs
	 * @throws FileNotFoundException
	 */
	public JsonArray readInputFile(String filePath) {
		JsonArray retValue = null;
		log.atDebug().log("Reading server logs input file.");
		try {
			log.atDebug().log((new File(filePath).getCanonicalPath()));
			File file = new File(filePath);
			if (!filePath.isEmpty() && file.exists()) {
				FileReader inputFileReader = new FileReader(file);
				JsonParser jsonParser = new JsonParser();
				retValue = jsonParser.parse(inputFileReader).getAsJsonArray();
			} else {
				throw new FileNotFoundException("Input File doesn't exists in the path: " + filePath);
			}
		} catch (IOException e) {
			log.error(e);
		}
		return retValue;

	}

	/**
	 * @param eventOne Event information either start or finished
	 * @param eventTwo Event information either start or finished
	 * @return Event Bean with alert status (true or false) and event information
	 *         (id, type, host)
	 */
	public EventLogBean createEventBean(JsonObject eventOne, JsonObject eventTwo) {
		boolean alert = false;
		String host = "-NA-";
		String type = "-NA-";
		String eventId = eventTwo.get("id").getAsString();
		log.atDebug().log("Event id: " + eventId);
		if (eventTwo.has("type"))
			host = eventTwo.get("type").getAsString();
		if (eventTwo.has("host"))
			type = eventTwo.get("host").getAsString();
		double timestamp1 = eventOne.get("timestamp").getAsDouble();
		double timestamp2 = eventTwo.get("timestamp").getAsDouble();
		int timeTaken = calculateEventTime(timestamp1, timestamp2);
		log.atDebug().log("Time taken by event: " + timeTaken + "ms");

		if (timeTaken > 4) {
			log.info("Long event " + eventId + " ( " + timeTaken + "ms ) setting alert to TRUE.");
			alert = true;
		}
		return new EventLogBean(eventId, timeTaken, type, host, alert);
	}

	private int calculateEventTime(double timestamp1, double timestamp2) {
		return (int) (timestamp1 > timestamp2 ? timestamp1 - timestamp2 : timestamp2 - timestamp1);

	}

	public String getDbString() {
		log.atDebug().log("Reading dbstring value from app configuration file.");
		
		return configJson.get("dbstring").getAsString();
	}

	public String getDbtableName() {
		log.atDebug().log("Reading dbtable value from app configuration file.");
		return configJson.get("dbtable").getAsString();
	}

	public String getSeverLogFile() {
		log.atDebug().log("Reading severlogs file path value from app configuration file.");
		return configJson.get("severlogs").getAsString();
	}
}
