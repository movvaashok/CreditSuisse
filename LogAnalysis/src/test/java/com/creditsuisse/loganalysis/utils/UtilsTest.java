package com.creditsuisse.loganalysis.utils;

import static org.junit.Assert.*;


import org.junit.Test;

public class UtilsTest {

	@Test
	public void testReadConfigFile() {
		Utils utility = new Utils();
		assertTrue(utility.readConfigFile("./appConfig.json"));
		assertFalse(utility.readConfigFile("./tester.json"));
	}

	@Test
	public void testReadInputFile() {
		Utils utility = new Utils();
		assertNotNull(utility.readInputFile("./src/test/resources/creditSuisse.txt"));
		assertNull(utility.readInputFile(""));

	}

}
