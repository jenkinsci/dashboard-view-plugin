package hudson.plugins.view.dashboard.core;

import com.google.common.collect.Lists;
import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.Job;
import hudson.plugins.view.dashboard.DashboardPortlet;
import hudson.plugins.view.dashboard.Messages;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Portlet displays a grid of job names with status and links to jobs.
 *
 * @author Peter Hayes
 */
public class JobsPortlet extends DashboardPortlet {

  private static final int MIN_COLUMN_COUNT = 3;

  private final int columnCount;
  private boolean fillColumnFirst = false;

  @DataBoundConstructor
  public JobsPortlet(String name, int columnCount, boolean fillColumnFirst) {
    super(name);
    this.columnCount = columnCount;
    this.fillColumnFirst = fillColumnFirst;
  }

  public int getColumnCount() {
    return columnCount <= 0 ? MIN_COLUMN_COUNT : columnCount;
  }

  public List<List<Job>> getJobs() {
    List<Job> jobs = this.getDashboard().getJobs();
    Collections.sort(
        jobs, (p1, p2) -> p1.getDisplayName().compareToIgnoreCase(p2.getDisplayName()));

    if (this.fillColumnFirst) {
      return transposed(jobs);
    } else {
      return Lists.partition(jobs, this.getColumnCount());
    }
  }

  private List<List<Job>> transposed(List<Job> jobs) {
    int rowCount = (jobs.size() + 1) / this.getColumnCount();
    List<List<Job>> result = new ArrayList<>(rowCount);
    for (int i = 0; i < rowCount; i++) {
      result.add(new ArrayList<>(this.getColumnCount()));
    }
    int c = 0;
    for (Job job : jobs) {
      result.get(c).add(job);
      c = (c + 1) % rowCount;
    }
    return result;
  }

  @Extension
  public static class DescriptorImpl extends Descriptor<DashboardPortlet> {

    public int getDefaultColumnCount() {
      return MIN_COLUMN_COUNT;
    }

    @Override
    public String getDisplayName() {
      return Messages.Dashboard_JobsGrid();
    }
  }
}
