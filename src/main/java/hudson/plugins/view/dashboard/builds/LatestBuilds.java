package hudson.plugins.view.dashboard.builds;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.Job;
import hudson.model.Run;
import hudson.plugins.view.dashboard.DashboardPortlet;
import java.text.DateFormat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.kohsuke.stapler.DataBoundConstructor;

import hudson.plugins.view.dashboard.Messages;

public class LatestBuilds extends DashboardPortlet{

  /**
	 * Number of latest builds which will be displayed on the screen
	 */  
  private int numBuilds = 10;

	@DataBoundConstructor
	public LatestBuilds(String name, int numBuilds) {
		super(name);
    this.numBuilds = numBuilds;
	}

  public int getNumBuilds() {
    return numBuilds <= 0 ? 10 : numBuilds;
  }

  public String getTimestampSortData(Run run) {
    return String.valueOf(run.getTimeInMillis());
  }

  public String getTimestampString(Run run) {
    return DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM).format(new Date(run.getTimeInMillis()));
  }
	
	/**
	 * Last <code>N_LATEST_BUILDS</code> builds
	 *
	 */
	public List<Run> getFinishedBuilds() {
    List<Job> jobs = getDashboard().getJobs();
		List<Run> allBuilds = new ArrayList<Run>();
		for (Job job : jobs) {
      List<Run> builds = job.getBuilds();
      allBuilds.addAll(builds);
		}
		Collections.sort(allBuilds, Run.ORDER_BY_DATE);
		List<Run> recentBuilds = new ArrayList<Run>();
		if(allBuilds.size() < getNumBuilds())
			recentBuilds = allBuilds;
		else
			recentBuilds = allBuilds.subList(0,getNumBuilds());
			
		return recentBuilds;
	}

  public String getBuildColumnSortData(Run<?, ?> build) {
    return String.valueOf(build.getNumber());
  }

	@Extension
    public static class DescriptorImpl extends Descriptor<DashboardPortlet> {

		@Override
		public String getDisplayName() {
			return Messages.Dashboard_LatestBuilds();
		}

	}
	
}
