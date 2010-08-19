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
		if(allBuilds.size() < getNumBuilds())
			recentBuilds = allBuilds;
		else
			recentBuilds = allBuilds.subList(0,getNumBuilds());
			
		return recentBuilds;
	}		

	@Extension
    public static class DescriptorImpl extends Descriptor<DashboardPortlet> {

		@Override
		public String getDisplayName() {
			return "Latest builds";
		}

	}
	
}
