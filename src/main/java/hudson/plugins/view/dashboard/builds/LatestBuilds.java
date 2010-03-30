package hudson.plugins.view.dashboard.builds;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.Hudson;
import hudson.model.Job;
import hudson.model.Run;
import hudson.model.TopLevelItem;
import hudson.plugins.view.dashboard.DashboardPortlet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.kohsuke.stapler.DataBoundConstructor;

public class LatestBuilds extends DashboardPortlet{

	@DataBoundConstructor
	public LatestBuilds(String name) {
		super(name);
	}
	
	/**
	 * Number of latest builds which will be displayed on the screen
	 */
	private static final int N_LATEST_BUILDS = 10;
	
	/**
	 * Last <code>N_LATEST_BUILDS</code> builds
	 *
	 */
	public List<Run> getFinishedBuilds() {
		Collection<TopLevelItem> jobs = Hudson.getInstance().getItems();
		List<Run> allBuilds = new ArrayList<Run>();
		for (TopLevelItem job : jobs) {
			if (job instanceof Job) {
        if (getDashboard().HasItem(job)) {
          List<Run> builds = ((Job) job).getBuilds();
          allBuilds.addAll(builds);
        }
      }
		}
		Collections.sort(allBuilds, Run.ORDER_BY_DATE);
		List<Run> recentBuilds = new ArrayList<Run>();
		if(allBuilds.size() < N_LATEST_BUILDS)
			recentBuilds = allBuilds;
		else
			recentBuilds = allBuilds.subList(0,N_LATEST_BUILDS);
			
		return recentBuilds;
	}		

	@Extension
    public static class DescriptorImpl extends Descriptor<DashboardPortlet> {

		@Override
		public String getDisplayName() {
			return "Last " + N_LATEST_BUILDS + " builds";
		}
	}
	
}
