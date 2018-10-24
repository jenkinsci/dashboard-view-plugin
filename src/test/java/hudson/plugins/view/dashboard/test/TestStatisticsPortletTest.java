/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package hudson.plugins.view.dashboard.test;

import static org.junit.Assert.assertEquals;

import java.text.DecimalFormat;
import java.util.Arrays;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author Pete
 */
public class TestStatisticsPortletTest {

	public TestStatisticsPortletTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	/**
	 * Test of format method, of class TestStatisticsPortlet.
	 */
	@Test
	public void testFormatLessThan1Percent() {
		TestStatisticsPortlet instance = new TestStatisticsPortlet("test",
				false, null, null, null, false);
		DecimalFormat df = new DecimalFormat("0%");
		double val = 0.003d;
		String expResult = ">0%";
		String result = instance.format(df, val);
		assertEquals(expResult, result);
	}

	/**
	 * Test of format method, of class TestStatisticsPortlet.
	 */
	@Test
	public void testAlternateFormatLessThan1Percent() {
		TestStatisticsPortlet instance = new TestStatisticsPortlet("test",
				false, null, null, null, false);
		instance.setUseAlternatePercentagesOnLimits(true);
		DecimalFormat df = new DecimalFormat("0%");
		double val = 0.003d;
		String expResult = "<1%";
		String result = instance.format(df, val);
		assertEquals(expResult, result);
	}

	/**
	 * Test of format method, of class TestStatisticsPortlet.
	 */
	@Test
	public void testFormatBetween1PercentAnd99Percent() {
		TestStatisticsPortlet instance = new TestStatisticsPortlet("test",
				false, null, null, null, false);
		DecimalFormat df = new DecimalFormat("0%");
		double val = 0.5d;
		String expResult = "50%";
		String result = instance.format(df, val);
		assertEquals(expResult, result);
	}

	/**
	 * Test of format method, of class TestStatisticsPortlet.
	 */
	@Test
	public void testFormatGreaterThan99Percent() {
		TestStatisticsPortlet instance = new TestStatisticsPortlet("test",
				false, null, null, null, false);
		DecimalFormat df = new DecimalFormat("0%");
		double val = 0.996d;
		String expResult = "<100%";
		String result = instance.format(df, val);
		assertEquals(expResult, result);
	}

	/**
	 * Test of format method, of class TestStatisticsPortlet.
	 */
	@Test
	public void testAlternateFormatGreaterThan99Percent() {
		TestStatisticsPortlet instance = new TestStatisticsPortlet("test",
				false, null, null, null, false);
		instance.setUseAlternatePercentagesOnLimits(true);
		DecimalFormat df = new DecimalFormat("0%");
		double val = 0.996d;
		String expResult = ">99%";
		String result = instance.format(df, val);
		assertEquals(expResult, result);
	}

	/**
	 * Test of format method, of class TestStatisticsPortlet.
	 */
	@Test
	public void testFormatEqualTo100Percent() {
		TestStatisticsPortlet instance = new TestStatisticsPortlet("test",
				false, null, null, null, false);
		DecimalFormat df = new DecimalFormat("0%");
		double val = 1d;
		String expResult = "100%";
		String result = instance.format(df, val);
		assertEquals(expResult, result);
	}

	/**
	 * Test of format method, of class TestStatisticsPortlet.
	 */
	@Test
	public void testFormatEqualTo0Percent() {
		TestStatisticsPortlet instance = new TestStatisticsPortlet("test",
				false, null, null, null, false);
		DecimalFormat df = new DecimalFormat("0%");
		double val = 0d;
		String expResult = "0%";
		String result = instance.format(df, val);
		assertEquals(expResult, result);
	}

	@Test
	public void testRowColor() throws Exception {
		TestStatisticsPortlet instance = new TestStatisticsPortlet("test",
				false, "green", "red", null, false);
		assertEquals("green", instance.getRowColor(new TestResult(null, 3, 0, 0)));
		assertEquals("red", instance.getRowColor(new TestResult(null, 1, 1, 0)));
		assertEquals("red", instance.getRowColor(new TestResult(null, 1, 0, 1)));
	}
	
	@Test
	public void testSummaryRowColorWithOneRow() throws Exception {
		TestStatisticsPortlet instance = new TestStatisticsPortlet("test",
				false, "green", "red", null, false);
		assertEquals("green", instance.getTotalRowColor(Arrays.asList(new TestResult(null, 3, 0, 0))));
		assertEquals("red", instance.getTotalRowColor(Arrays.asList(new TestResult(null, 1, 1, 0))));
		assertEquals("red", instance.getTotalRowColor(Arrays.asList(new TestResult(null, 1, 0, 1))));
	}
	
	@Test
	public void testSummaryRowColorWithMultipleRows() throws Exception {
		TestStatisticsPortlet instance = new TestStatisticsPortlet("test",
				false, "green", "red", null, false);
		assertEquals("green", instance.getTotalRowColor(Arrays.asList(new TestResult(null, 2, 0, 0), new TestResult(null, 2, 0, 0))));
		assertEquals("red", instance.getTotalRowColor(Arrays.asList(new TestResult(null, 1, 0, 0), new TestResult(null, 1, 1, 0))));
		assertEquals("red", instance.getTotalRowColor(Arrays.asList(new TestResult(null, 1, 0, 0), new TestResult(null, 1, 0, 1))));
	}

}