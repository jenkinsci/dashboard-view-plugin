/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package hudson.plugins.view.dashboard.test;

import hudson.model.Job;
import java.text.DecimalFormat;
import java.util.Collection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

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
        TestStatisticsPortlet instance = new TestStatisticsPortlet("test");
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
    public void testFormatBetween1PercentAnd99Percent() {
        TestStatisticsPortlet instance = new TestStatisticsPortlet("test");
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
        TestStatisticsPortlet instance = new TestStatisticsPortlet("test");
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
    public void testFormatEqualTo100Percent() {
        TestStatisticsPortlet instance = new TestStatisticsPortlet("test");
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
        TestStatisticsPortlet instance = new TestStatisticsPortlet("test");
        DecimalFormat df = new DecimalFormat("0%");
        double val = 0d;
        String expResult = "0%";
        String result = instance.format(df, val);
        assertEquals(expResult, result);
    }

}