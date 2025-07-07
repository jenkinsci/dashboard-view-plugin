package hudson.plugins.view.dashboard.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.Test;

class TestStatisticsPortletTest {

    /** Test of format method, of class TestStatisticsPortlet. */
    @Test
    void testFormatLessThan1Percent() {
        TestStatisticsPortlet instance = new TestStatisticsPortlet("test", false, null, null, null, false);
        DecimalFormat df = new DecimalFormat("0%");
        double val = 0.003d;
        String expResult = ">0%";
        String result = instance.format(df, val);
        assertEquals(expResult, result);
    }

    /** Test of format method, of class TestStatisticsPortlet. */
    @Test
    void testAlternateFormatLessThan1Percent() {
        TestStatisticsPortlet instance = new TestStatisticsPortlet("test", false, null, null, null, false);
        instance.setUseAlternatePercentagesOnLimits(true);
        DecimalFormat df = new DecimalFormat("0%");
        double val = 0.003d;
        String expResult = "<1%";
        String result = instance.format(df, val);
        assertEquals(expResult, result);
    }

    /** Test of format method, of class TestStatisticsPortlet. */
    @Test
    void testFormatBetween1PercentAnd99Percent() {
        TestStatisticsPortlet instance = new TestStatisticsPortlet("test", false, null, null, null, false);
        DecimalFormat df = new DecimalFormat("0%");
        double val = 0.5d;
        String expResult = "50%";
        String result = instance.format(df, val);
        assertEquals(expResult, result);
    }

    /** Test of format method, of class TestStatisticsPortlet. */
    @Test
    void testFormatGreaterThan99Percent() {
        TestStatisticsPortlet instance = new TestStatisticsPortlet("test", false, null, null, null, false);
        DecimalFormat df = new DecimalFormat("0%");
        double val = 0.996d;
        String expResult = "<100%";
        String result = instance.format(df, val);
        assertEquals(expResult, result);
    }

    /** Test of format method, of class TestStatisticsPortlet. */
    @Test
    void testAlternateFormatGreaterThan99Percent() {
        TestStatisticsPortlet instance = new TestStatisticsPortlet("test", false, null, null, null, false);
        instance.setUseAlternatePercentagesOnLimits(true);
        DecimalFormat df = new DecimalFormat("0%");
        double val = 0.996d;
        String expResult = ">99%";
        String result = instance.format(df, val);
        assertEquals(expResult, result);
    }

    /** Test of format method, of class TestStatisticsPortlet. */
    @Test
    void testFormatEqualTo100Percent() {
        TestStatisticsPortlet instance = new TestStatisticsPortlet("test", false, null, null, null, false);
        DecimalFormat df = new DecimalFormat("0%");
        double val = 1d;
        String expResult = "100%";
        String result = instance.format(df, val);
        assertEquals(expResult, result);
    }

    /** Test of format method, of class TestStatisticsPortlet. */
    @Test
    void testFormatEqualTo0Percent() {
        TestStatisticsPortlet instance = new TestStatisticsPortlet("test", false, null, null, null, false);
        DecimalFormat df = new DecimalFormat("0%");
        double val = 0d;
        String expResult = "0%";
        String result = instance.format(df, val);
        assertEquals(expResult, result);
    }

    @Test
    void testRowColor() {
        TestStatisticsPortlet instance = new TestStatisticsPortlet("test", false, "green", "red", "orange", false);
        assertEquals("green", instance.getRowColor(new TestResult(null, 3, 0, 0)));
        assertEquals("red", instance.getRowColor(new TestResult(null, 1, 1, 0)));
        assertEquals("orange", instance.getRowColor(new TestResult(null, 1, 0, 1)));
    }

    @Test
    void testSummaryRowColorWithOneRow() {
        TestStatisticsPortlet instance = new TestStatisticsPortlet("test", false, "green", "red", "orange", false);
        assertEquals("green", instance.getTotalRowColor(Collections.singletonList(new TestResult(null, 3, 0, 0))));
        assertEquals("red", instance.getTotalRowColor(Collections.singletonList(new TestResult(null, 1, 1, 0))));
        assertEquals("orange", instance.getTotalRowColor(Collections.singletonList(new TestResult(null, 1, 0, 1))));
    }

    @Test
    void testSummaryRowColorWithMultipleRows() {
        TestStatisticsPortlet instance = new TestStatisticsPortlet("test", false, "green", "red", "orange", false);
        assertEquals(
                "green",
                instance.getTotalRowColor(Arrays.asList(new TestResult(null, 2, 0, 0), new TestResult(null, 2, 0, 0))));
        assertEquals(
                "red",
                instance.getTotalRowColor(Arrays.asList(new TestResult(null, 1, 0, 0), new TestResult(null, 1, 1, 0))));
        assertEquals(
                "orange",
                instance.getTotalRowColor(Arrays.asList(new TestResult(null, 1, 0, 0), new TestResult(null, 1, 0, 1))));
    }
}
