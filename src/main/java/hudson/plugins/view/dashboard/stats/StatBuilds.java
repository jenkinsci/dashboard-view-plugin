package hudson.plugins.view.dashboard.stats;

import hudson.Extension;
import hudson.model.BallColor;
import hudson.model.Descriptor;
import hudson.model.Job;
import hudson.model.Run;
import hudson.model.TopLevelItem;
import hudson.plugins.view.dashboard.DashboardPortlet;
import hudson.plugins.view.dashboard.Messages;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Build statistics - number of builds with given build status
 *
 * @author Vojtech Juranek
 */
public class StatBuilds extends DashboardPortlet {

  /** The most builds we will try to examine. */
  static final int MAX_BUILDS = Integer.getInteger(StatBuilds.class.getName() + ".maxBuilds", 10);

  @DataBoundConstructor
  public StatBuilds(String name) {
    super(name);
  }

  public Map<BallColor, Integer> getBuildStat(List<TopLevelItem> jobs) {
    SortedMap<BallColor, Integer> colStatBuilds = new TreeMap<>();
    for (BallColor color : BallColor.values()) {
      colStatBuilds.put(color.noAnime(), 0);
    }
    // loop over jobs
    for (TopLevelItem job : jobs) {
      if (job instanceof Job) {
        // Build statistics
        List<Run> builds = ((Job) job).getBuilds().limit(MAX_BUILDS);
        // loop over builds
        for (Run build : builds) {
          BallColor bColor = build.getIconColor();
          if (bColor != null && bColor.noAnime() != null && colStatBuilds.get(bColor) != null) {
            colStatBuilds.put(bColor.noAnime(), colStatBuilds.get(bColor) + 1);
          }
        }
      }
    }
    return colStatBuilds;
  }

  public float roundFloatDecimal(float input) {
    float rounded = (float) Math.round(input * 100f);
    rounded = rounded / 100f;
    return rounded;
  }

  @Extension
  public static class DescriptorImpl extends Descriptor<DashboardPortlet> {

    @Override
    public String getDisplayName() {
      return Messages.Dashboard_BuildStatistics();
    }
  }
}
