package hudson.plugins.view.dashboard.allure;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AllureResultSummaryTest {

  /** Test of percent method, of class AllureResultSummary. */
  @Test
  public void testAllureResultSummaryGetEmpty() {
    AllureResultSummary allureResultSummary = new AllureResultSummary();
    assertEquals(0, allureResultSummary.getAllureResults().size());
  }

  @Test
  public void testAllureResultSummaryAddAndGetSingleResult() {
    AllureResultSummary allureResultSummary = new AllureResultSummary();
    allureResultSummary.addAllureResult(new AllureResult(null, 10, 0, 0, 0, 0, 0));
    assertEquals(10, allureResultSummary.getAllureResults().get(0).getTotal());
  }

  @Test
  public void testAllureResultSummaryAddAndGetTotalCount() {
    AllureResultSummary allureResultSummary = new AllureResultSummary();
    allureResultSummary.addAllureResult(new AllureResult(null, 20, 0, 0, 0, 0, 0));
    allureResultSummary.addAllureResult(new AllureResult(null, 30, 0, 0, 0, 0, 0));
    assertEquals(50, allureResultSummary.getTotal());
  }
}
