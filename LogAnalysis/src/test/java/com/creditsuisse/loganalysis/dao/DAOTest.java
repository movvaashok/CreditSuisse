package com.creditsuisse.loganalysis.dao;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Test;

import com.creditsuisse.loganalysis.beans.EventLogBean;

public class DAOTest {

	@Test
	public void testDAO() {
		String dbstring = "jdbc:hsqldb:file:testCreditSuisse";
		String dbtable = "eventLogs";
		DAO dao = new DAO(dbstring, dbtable);
		assertNotNull(dao.getDBConnector());
		dao.terminateConnection();
		try {
			dao.getDBConnector().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test(expected = NullPointerException.class)
	public void testSaveEventData() {
		String dbstring = "jdbc:hsqldb:file:testCreditSuisse";
		String dbtable = "testEventLogs";
		DAO dao = new DAO(dbstring, dbtable);
		assertTrue(dao.saveEventData(new EventLogBean("1", 5, "application_log", "testhost", true)));
		assertNull(dao.saveEventData(null));
	}

}
