package hudson.plugins.view.dashboard.core;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.Job;
import hudson.model.Result;
import hudson.model.Run;
import hudson.plugins.view.dashboard.DashboardPortlet;
import hudson.views.JobColumn;
import hudson.views.ListViewColumn;
import hudson.views.StatusColumn;
import hudson.views.WeatherColumn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Portlet display a list of unstable (or worse) jobs in a simple
 * tabular format.
 * 
 * @author Peter Hayes
 */
public class UnstableJobsPortlet extends DashboardPortlet {
	private static final Collection<ListViewColumn> COLUMNS =
		Arrays.asList(new StatusColumn(), new WeatherColumn(), new JobColumn());

	@DataBoundConstructor
	public UnstableJobsPortlet(String name) {
		super(name);
	}
	
	/**
	 * Given a list of all jobs, return just those that are unstable or worse.
	 */
	public Collection<Job> getUnstableJobs(Collection<Job> allJobs) {
		ArrayList<Job> unstableJobs = new ArrayList<Job>();
		
		for (Job job : allJobs) {
			Run run = job.getLastCompletedBuild();
			
			if (run != null && Result.UNSTABLE.isBetterOrEqualTo(run.getResult())) {
				unstableJobs.add(job);
			}
		}
		
		return unstableJobs;
	}
	
	public Collection<ListViewColumn> getColumns() {
		return COLUMNS;
	}
	
	@Extension
  public static class DescriptorImpl extends Descriptor<DashboardPortlet> {

		@Override
		public String getDisplayName() {
			return "Unstable Jobs";
		}
	}
}
