package hudson.plugins.view.dashboard.allure;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AllureResultTest {

  /** Test of percent method, of class AllureResult. */
  @Test
  public void testAllureResultPctTotalZero() {
    AllureResult allureResult = new AllureResult(null, 0, 0, 0, 0, 0, 0);
    assertEquals("0.0", String.valueOf(allureResult.getPct(5)));
  }

  @Test
  public void testAllureResultDivideZero() {
    AllureResult allureResult = new AllureResult(null, 10, 0, 0, 0, 0, 0);
    assertEquals("0.0", String.valueOf(allureResult.getPct(0)));
  }

  @Test
  public void testAllureResultDivideRound() {
    AllureResult allureResult = new AllureResult(null, 100, 0, 0, 0, 0, 0);
    assertEquals("0.33", String.valueOf(allureResult.getPct(33)));
  }
}
