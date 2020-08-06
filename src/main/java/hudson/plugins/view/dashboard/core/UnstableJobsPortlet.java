package hudson.plugins.view.dashboard.core;

import hudson.Extension;
import hudson.model.*;
import hudson.plugins.view.dashboard.DashboardPortlet;
import hudson.plugins.view.dashboard.Messages;
import hudson.views.JobColumn;
import hudson.views.ListViewColumn;
import hudson.views.StatusColumn;
import hudson.views.WeatherColumn;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Iterator;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Portlet display a list of unstable (or worse) jobs in a simple tabular format.
 *
 * @author Peter Hayes
 */
public class UnstableJobsPortlet extends DashboardPortlet {

  private static final Collection<ListViewColumn> COLUMNS =
      Arrays.asList(new StatusColumn(), new WeatherColumn(), new JobColumn());
  private boolean showOnlyFailedJobs = false;
  private final boolean recurse;

  @DataBoundConstructor
  public UnstableJobsPortlet(String name, boolean showOnlyFailedJobs, boolean recurse) {
    super(name);
    this.showOnlyFailedJobs = showOnlyFailedJobs;
    this.recurse = recurse;
  }

  /** Given a list of all jobs, return just those that are unstable or worse. */
  public Collection<Job> getUnstableJobs(Collection<? extends Item> allJobs) {
    ArrayList<Job> unstableJobs = new ArrayList<>();
    Result expected = this.showOnlyFailedJobs ? Result.FAILURE : Result.UNSTABLE;

    for (Item item : allJobs) {
      if (!(item instanceof TopLevelItem)) {
        continue;
      }
      if (recurse && item instanceof ItemGroup) {
        unstableJobs.addAll(getUnstableJobs(((ItemGroup<? extends Item>) item).getItems()));
      }
      if (item instanceof Job) {
        Job job = (Job) item;
        Run run = job.getLastCompletedBuild();

        if (run != null) {
          Result result = run.getResult();
          if (result != null && expected.isBetterOrEqualTo(result)) {
            unstableJobs.add(job);
          }
        }
      }
    }
    if (recurse) {
      IdentityHashMap<Job, Object> duplicates = new IdentityHashMap<>(unstableJobs.size());
      for (Iterator<Job> iterator = unstableJobs.iterator(); iterator.hasNext(); ) {
        Job j = iterator.next();
        if (duplicates.containsKey(j)) {
          iterator.remove();
        } else {
          duplicates.put(j, null);
        }
      }
    }

    return unstableJobs;
  }

  public Collection<ListViewColumn> getColumns() {
    return COLUMNS;
  }

  public boolean isShowOnlyFailedJobs() {
    return this.showOnlyFailedJobs;
  }

  public boolean isRecurse() {
    return recurse;
  }

  @Extension
  public static class DescriptorImpl extends Descriptor<DashboardPortlet> {

    @Override
    public String getDisplayName() {
      return Messages.Dashboard_UnstableJobs();
    }
  }
}
