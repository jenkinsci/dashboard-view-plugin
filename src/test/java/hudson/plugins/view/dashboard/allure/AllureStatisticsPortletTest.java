package hudson.plugins.view.dashboard.allure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import org.junit.Test;

public class AllureStatisticsPortletTest {

  /** Test of format method, of class AllureStatisticsPortlet. */
  @Test
  public void testFormatLessThan1Percent() {
    AllureStatisticsPortlet instance =
        new AllureStatisticsPortlet("test", false, null, null, null, null, null, false);
    DecimalFormat df = new DecimalFormat("0%");
    double val = 0.003d;
    String expResult = ">0%";
    String result = instance.format(df, val);
    assertEquals(expResult, result);
  }

  /** Test of format method, of class AllureStatisticsPortlet. */
  @Test
  public void testAlternateFormatLessThan1Percent() {
    AllureStatisticsPortlet instance =
        new AllureStatisticsPortlet("test", false, null, null, null, null, null, false);
    instance.setUseAlternatePercentagesOnLimits(true);
    DecimalFormat df = new DecimalFormat("0%");
    double val = 0.003d;
    String expResult = "<1%";
    String result = instance.format(df, val);
    assertEquals(expResult, result);
  }

  /** Test of format method, of class AllureStatisticsPortlet. */
  @Test
  public void testFormatBetween1PercentAnd99Percent() {
    AllureStatisticsPortlet instance =
        new AllureStatisticsPortlet("test", false, null, null, null, null, null, false);
    DecimalFormat df = new DecimalFormat("0%");
    double val = 0.5d;
    String expResult = "50%";
    String result = instance.format(df, val);
    assertEquals(expResult, result);
  }

  /** Test of format method, of class AllureStatisticsPortlet. */
  @Test
  public void testFormatGreaterThan99Percent() {
    AllureStatisticsPortlet instance =
        new AllureStatisticsPortlet("test", false, null, null, null, null, null, false);
    DecimalFormat df = new DecimalFormat("0%");
    double val = 0.996d;
    String expResult = "<100%";
    String result = instance.format(df, val);
    assertEquals(expResult, result);
  }

  /** Test of format method, of class AllureStatisticsPortlet. */
  @Test
  public void testAlternateFormatGreaterThan99Percent() {
    AllureStatisticsPortlet instance =
        new AllureStatisticsPortlet("test", false, null, null, null, null, null, false);
    instance.setUseAlternatePercentagesOnLimits(true);
    DecimalFormat df = new DecimalFormat("0%");
    double val = 0.996d;
    String expResult = ">99%";
    String result = instance.format(df, val);
    assertEquals(expResult, result);
  }

  /** Test of format method, of class AllureStatisticsPortlet. */
  @Test
  public void testFormatEqualTo100Percent() {
    AllureStatisticsPortlet instance =
        new AllureStatisticsPortlet("test", false, null, null, null, null, null, false);
    DecimalFormat df = new DecimalFormat("0%");
    double val = 1d;
    String expResult = "100%";
    String result = instance.format(df, val);
    assertEquals(expResult, result);
  }

  /** Test of format method, of class AllureStatisticsPortlet. */
  @Test
  public void testFormatEqualTo0Percent() {
    AllureStatisticsPortlet instance =
        new AllureStatisticsPortlet("test", false, null, null, null, null, null, false);
    DecimalFormat df = new DecimalFormat("0%");
    double val = 0d;
    String expResult = "0%";
    String result = instance.format(df, val);
    assertEquals(expResult, result);
  }

  @Test
  public void testRowColorOnlyTotal() {
    AllureStatisticsPortlet instance =
        new AllureStatisticsPortlet("test", false, "a", "b", "c", "d", "e", false);
    assertNull(instance.getRowColor(new AllureResult(null, 3, 0, 0, 0, 0, 0)));
  }

  @Test
  public void testRowColorFailedPriority() {
    AllureStatisticsPortlet instance =
        new AllureStatisticsPortlet(
            "test", false, "passed", "failed", "broken", "skipped", "unknown", false);
    assertEquals("failed", instance.getRowColor(new AllureResult(null, 3, 1, 1, 1, 1, 1)));
  }

  @Test
  public void testTotalRowColorWithNull() {
    AllureStatisticsPortlet instance =
        new AllureStatisticsPortlet(
            "test", false, "passed", "failed", "broken", "skipped", "unknown", false);
    assertNull(instance.getTotalRowColor(null));
  }

  @Test
  public void testSummaryRowColorWithOneRow() {
    AllureStatisticsPortlet instance =
        new AllureStatisticsPortlet(
            "test", false, "passed", "failed", "broken", "skipped", "unknown", false);
    assertNull(
        instance.getTotalRowColor(
            Collections.singletonList(new AllureResult(null, 3, 0, 0, 0, 0, 0))));
    assertEquals(
        "passed",
        instance.getTotalRowColor(
            Collections.singletonList(new AllureResult(null, 1, 1, 0, 0, 0, 0))));
    assertEquals(
        "failed",
        instance.getTotalRowColor(
            Collections.singletonList(new AllureResult(null, 1, 0, 1, 0, 0, 0))));
  }

  @Test
  public void testSummaryRowColorWithMultipleRows() {
    AllureStatisticsPortlet instance =
        new AllureStatisticsPortlet(
            "test", false, "passed", "failed", "broken", "skipped", "unknown", false);
    assertNull(
        instance.getTotalRowColor(Arrays.asList(new AllureResult(null, 2, 1, 1, 0, 0, 0), null)));
    assertEquals(
        "passed",
        instance.getTotalRowColor(
            Arrays.asList(
                new AllureResult(null, 1, 1, 0, 0, 0, 0),
                new AllureResult(null, 1, 1, 0, 0, 0, 0))));
    assertEquals(
        "skipped",
        instance.getTotalRowColor(
            Arrays.asList(
                new AllureResult(null, 1, 1, 0, 0, 0, 0),
                new AllureResult(null, 1, 0, 0, 0, 1, 1))));
  }
}
