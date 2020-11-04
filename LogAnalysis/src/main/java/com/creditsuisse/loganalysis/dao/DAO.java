package com.creditsuisse.loganalysis.dao;

import com.creditsuisse.loganalysis.beans.EventLogBean;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DAO {
	static Logger log = LogManager.getLogger();
	private static Connection con = null;
	private String dbTable;

	public DAO(String dbString, String dbTable) {
		log.atDebug().log("Attempting to connec to database.");
		this.dbTable = dbTable;
		boolean tableExists = false;
		try {
			con = DriverManager.getConnection(dbString);
			if (con != null) {
				log.atDebug().log("Database connection successful.");
				ResultSet rs = con.getMetaData().getTables(null, null, "%", null);
				while (rs.next()) {
					if (rs.getString(4).equalsIgnoreCase("TABLE")) {
						if (rs.getString(3).equalsIgnoreCase(dbTable)) {
							tableExists = true;
						}
					}
				}
				if (!tableExists) {
					log.atDebug().log("Creating table in the database.");
					PreparedStatement createTable = con.prepareStatement("Create table " + dbTable
							+ "(id varchar (20),duration int,type varchar(20),host varchar(20),alert boolean)");
					createTable.execute();
					con.commit();
					log.info("Table created successfully");
				} else {
					log.info("Table already exists!");
				}

			} else {
				log.atDebug().log("Database connection failed.");
			}
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
	}

	public Connection getDBConnector() {
		return con;
	}

	public void terminateConnection() {
		try {
			con.close();
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
	}

	public boolean saveEventData(EventLogBean eventBean) {
		boolean retValue = false;
		log.atDebug().log("Saving event data to database: " + eventBean.getEvenId());
		String insertEventData = "insert into " + dbTable + " values(?,?,?,?,?)";
		try {
			PreparedStatement eventData = getDBConnector().prepareStatement(insertEventData);
			eventData.setString(1, eventBean.getEvenId());
			eventData.setInt(2, eventBean.getEventDuration());
			eventData.setString(3, eventBean.getType());
			eventData.setString(4, eventBean.getHost());
			eventData.setBoolean(5, eventBean.isAlert());
			eventData.execute();
			// manual setting true because, above execute command will return true only if result is ResultSet.
			retValue=true;
			con.commit();
			log.info("Event data saved to database..");
			log.atDebug().log("Event data saved to database.. Event Id: " + eventBean.getEvenId());
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
		return retValue;
	}

	public void getEventData() {
		String getEventData = " Select * from " + dbTable;
		try {
			PreparedStatement eventData = con.prepareStatement(getEventData);
			ResultSet res = eventData.executeQuery();
			while (res.next()) {
				System.out.println(res.getString("id") + "," + res.getInt("duration") + ", " + res.getString("type")
						+ ", " + res.getString("host") + ", " + res.getBoolean("alert"));
			}
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
	}

}
