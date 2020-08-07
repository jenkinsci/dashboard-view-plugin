package hudson.plugins.view.dashboard.test;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.TopLevelItem;
import hudson.plugins.view.dashboard.DashboardPortlet;
import hudson.plugins.view.dashboard.Messages;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

/**
 * Portlet that presents a grid of test result data with summation
 *
 * @author Peter Hayes
 */
public class TestStatisticsPortlet extends DashboardPortlet {
  private boolean useBackgroundColors;
  private String skippedColor;
  private String successColor;
  private String failureColor;
  private final boolean hideZeroTestProjects;
  private boolean useAlternatePercentagesOnLimits;

  @DataBoundConstructor
  public TestStatisticsPortlet(
      String name,
      final boolean hideZeroTestProjects,
      String successColor,
      String failureColor,
      String skippedColor,
      boolean useBackgroundColors) {
    super(name);
    this.successColor = successColor;
    this.failureColor = failureColor;
    this.skippedColor = skippedColor;
    this.useBackgroundColors = useBackgroundColors;
    this.hideZeroTestProjects = hideZeroTestProjects;
  }

  public TestResultSummary getTestResultSummary(Collection<TopLevelItem> jobs) {
    return TestUtil.getTestResultSummary(jobs, hideZeroTestProjects);
  }

  public boolean getHideZeroTestProjects() {
    return this.hideZeroTestProjects;
  }

  public String format(DecimalFormat df, double val) {
    if (val < 1d && val > .99d) {
      return useAlternatePercentagesOnLimits ? ">99%" : "<100%";
    }
    if (val > 0d && val < .01d) {
      return useAlternatePercentagesOnLimits ? "<1%" : ">0%";
    }
    return df.format(val);
  }

  public boolean isUseBackgroundColors() {
    return useBackgroundColors;
  }

  public String getSuccessColor() {
    return successColor;
  }

  public String getFailureColor() {
    return failureColor;
  }

  public String getSkippedColor() {
    return skippedColor;
  }

  @DataBoundSetter
  public void setUseAlternatePercentagesOnLimits(boolean useAlternatePercentagesOnLimits) {
    this.useAlternatePercentagesOnLimits = useAlternatePercentagesOnLimits;
  }

  public boolean isUseAlternatePercentagesOnLimits() {
    return useAlternatePercentagesOnLimits;
  }

  public String getRowColor(TestResult testResult) {
    return testResult.success == testResult.tests ? successColor : failureColor;
  }

  public String getTotalRowColor(List<TestResult> testResults) {
    for (TestResult testResult : testResults) {
      if (testResult.success != testResult.tests) {
        return failureColor;
      }
    }
    return successColor;
  }

  public void setUseBackgroundColors(boolean useBackgroundColors) {
    this.useBackgroundColors = useBackgroundColors;
  }

  public void setSkippedColor(String skippedColor) {
    this.skippedColor = skippedColor;
  }

  public void setSuccessColor(String successColor) {
    this.successColor = successColor;
  }

  public void setFailureColor(String failureColor) {
    this.failureColor = failureColor;
  }

  @Extension
  public static class DescriptorImpl extends Descriptor<DashboardPortlet> {

    @Override
    public String getDisplayName() {
      return Messages.Dashboard_TestStatisticsGrid();
    }
  }
}
