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
        assertEquals("portlet-success", instance.getRowClass(new TestResult(null, 3, 0, 0)));
        assertEquals("portlet-failure", instance.getRowClass(new TestResult(null, 1, 1, 0)));
        assertEquals("portlet-skipped", instance.getRowClass(new TestResult(null, 1, 0, 1)));
    }

    @Test
    void testSummaryRowColorWithOneRow() {
        TestStatisticsPortlet instance = new TestStatisticsPortlet("test", false, "green", "red", "orange", false);
        assertEquals(
                "portlet-success", instance.getTotalRowClass(Collections.singletonList(new TestResult(null, 3, 0, 0))));
        assertEquals(
                "portlet-failure", instance.getTotalRowClass(Collections.singletonList(new TestResult(null, 1, 1, 0))));
        assertEquals(
                "portlet-skipped", instance.getTotalRowClass(Collections.singletonList(new TestResult(null, 1, 0, 1))));
    }

    @Test
    void testSummaryRowColorWithMultipleRows() {
        TestStatisticsPortlet instance = new TestStatisticsPortlet("test", false, "green", "red", "orange", false);
        assertEquals(
                "portlet-success",
                instance.getTotalRowClass(Arrays.asList(new TestResult(null, 2, 0, 0), new TestResult(null, 2, 0, 0))));
        assertEquals(
                "portlet-failure",
                instance.getTotalRowClass(Arrays.asList(new TestResult(null, 1, 0, 0), new TestResult(null, 1, 1, 0))));
        assertEquals(
                "portlet-skipped",
                instance.getTotalRowClass(Arrays.asList(new TestResult(null, 1, 0, 0), new TestResult(null, 1, 0, 1))));
    }

    /** Legacy configurations stored a bare hex triplet/quad without a leading '#'. */
    @Test
    void testToCssColorPrefixesLegacyHexValue() {
        assertEquals("#71E66D", TestStatisticsPortlet.toCssColor("71E66D", "var(--success-color)"));
        assertEquals("#fff", TestStatisticsPortlet.toCssColor("fff", "var(--success-color)"));
        assertEquals("#ffffffaa", TestStatisticsPortlet.toCssColor("ffffffaa", "var(--success-color)"));
    }

    /** An unset color falls back to the theme-aware default so the portlet adapts to the active theme. */
    @Test
    void testToCssColorFallsBackToThemeDefaultWhenBlank() {
        assertEquals("var(--success-color)", TestStatisticsPortlet.toCssColor(null, "var(--success-color)"));
        assertEquals("var(--error-color)", TestStatisticsPortlet.toCssColor("", "var(--error-color)"));
        assertEquals("var(--warning-color)", TestStatisticsPortlet.toCssColor("   ", "var(--warning-color)"));
    }

    /** Anything that isn't a bare hex value, such as a Jenkins theme variable, is passed through unchanged. */
    @Test
    void testToCssColorPassesThroughCssValues() {
        assertEquals("var(--success-color)", TestStatisticsPortlet.toCssColor("var(--success-color)", "unused"));
        assertEquals("#71E66D", TestStatisticsPortlet.toCssColor("#71E66D", "unused"));
        assertEquals("green", TestStatisticsPortlet.toCssColor("green", "unused"));
    }

    @Test
    void testColorCssGettersUseThemeDefaultsWhenNotConfigured() {
        TestStatisticsPortlet instance = new TestStatisticsPortlet("test", false, null, null, null, true);
        assertEquals("var(--success-color)", instance.getSuccessColorCss());
        assertEquals("var(--error-color)", instance.getFailureColorCss());
        assertEquals("var(--warning-color)", instance.getSkippedColorCss());
    }

    @Test
    void testColorCssGettersHonorConfiguredValues() {
        TestStatisticsPortlet instance =
                new TestStatisticsPortlet("test", false, "71E66D", "var(--error-color)", "orange", true);
        assertEquals("#71E66D", instance.getSuccessColorCss());
        assertEquals("var(--error-color)", instance.getFailureColorCss());
        assertEquals("orange", instance.getSkippedColorCss());
    }
}
