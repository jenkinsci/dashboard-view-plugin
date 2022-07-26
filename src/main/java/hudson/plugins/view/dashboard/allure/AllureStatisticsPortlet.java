package hudson.plugins.view.dashboard.allure;

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
 * @author Aleksey Kazarin
 */
public class AllureStatisticsPortlet extends DashboardPortlet {
  private boolean useBackgroundColors;
  private String passedColor;
  private String failedColor;
  private String brokenColor;
  private String skippedColor;
  private String unknownColor;
  private final boolean hideZeroTestProjects;
  private boolean useAlternatePercentagesOnLimits;

  @DataBoundConstructor
  public AllureStatisticsPortlet(
      String name,
      final boolean hideZeroTestProjects,
      String passedColor,
      String failedColor,
      String brokenColor,
      String skippedColor,
      String unknownColor,
      boolean useBackgroundColors) {
    super(name);
    this.passedColor = passedColor;
    this.failedColor = failedColor;
    this.brokenColor = brokenColor;
    this.skippedColor = skippedColor;
    this.unknownColor = unknownColor;
    this.useBackgroundColors = useBackgroundColors;
    this.hideZeroTestProjects = hideZeroTestProjects;
  }

  public AllureResultSummary getAllureResultSummary(Collection<TopLevelItem> jobs) {
    return AllureUtil.getAllureResultSummary(jobs, hideZeroTestProjects);
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

  public String getPassedColor() {
    return passedColor;
  }

  public String getFailedColor() {
    return failedColor;
  }

  public String getBrokenColor() {
    return brokenColor;
  }

  public String getSkippedColor() {
    return skippedColor;
  }

  public String getUnknownColor() {
    return unknownColor;
  }

  @DataBoundSetter
  public void setUseAlternatePercentagesOnLimits(boolean useAlternatePercentagesOnLimits) {
    this.useAlternatePercentagesOnLimits = useAlternatePercentagesOnLimits;
  }

  public boolean isUseAlternatePercentagesOnLimits() {
    return useAlternatePercentagesOnLimits;
  }

  public String getRowColor(AllureResult allureResult) {
    if (allureResult.failed > 0) {
      return failedColor;
    } else if (allureResult.broken > 0) {
      return brokenColor;
    } else if (allureResult.skipped > 0) {
      return skippedColor;
    } else if (allureResult.unknown > 0) {
      return unknownColor;
    } else if (allureResult.passed > 0) {
      return passedColor;
    } else {
      return null;
    }
  }

  public String getTotalRowColor(List<AllureResult> allureResults) {
    if (allureResults == null) {
      return null;
    }
    AllureResult accumulatedResults = new AllureResult(null, 0, 0, 0, 0, 0, 0);
    for (AllureResult allureResult : allureResults) {
      if (allureResult == null || allureResult.getSummarized() <= 0) {
        return null;
      }
      accumulatedResults.setPassed(accumulatedResults.getPassed() + allureResult.getPassed());
      accumulatedResults.setFailed(accumulatedResults.getFailed() + allureResult.getFailed());
      accumulatedResults.setBroken(accumulatedResults.getBroken() + allureResult.getBroken());
      accumulatedResults.setSkipped(accumulatedResults.getSkipped() + allureResult.getSkipped());
      accumulatedResults.setUnknown(accumulatedResults.getUnknown() + allureResult.getUnknown());
    }
    if (accumulatedResults.getFailed() > 0) {
      return failedColor;
    } else if (accumulatedResults.getBroken() > 0) {
      return brokenColor;
    } else if (accumulatedResults.getSkipped() > 0) {
      return skippedColor;
    } else if (accumulatedResults.getUnknown() > 0) {
      return unknownColor;
    }
    return passedColor;
  }

  public void setUseBackgroundColors(boolean useBackgroundColors) {
    this.useBackgroundColors = useBackgroundColors;
  }

  public void setPassedColor(String passedColor) {
    this.passedColor = passedColor;
  }

  public void setFailedColor(String failedColor) {
    this.failedColor = failedColor;
  }

  public void setBrokenColor(String brokenColor) {
    this.brokenColor = brokenColor;
  }

  public void setSkippedColor(String skippedColor) {
    this.skippedColor = skippedColor;
  }

  public void setUnknownColor(String unknownColor) {
    this.unknownColor = unknownColor;
  }

  @Extension
  public static class DescriptorImpl extends Descriptor<DashboardPortlet> {

    @Override
    public String getDisplayName() {
      return Messages.Dashboard_AllureStatisticsGrid();
    }
  }
}
