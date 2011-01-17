package hudson.plugins.view.dashboard.test;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.Job;
import hudson.plugins.view.dashboard.DashboardPortlet;

import java.util.Collection;

import org.kohsuke.stapler.DataBoundConstructor;

import hudson.plugins.view.dashboard.Messages;

/**
 * Portlet that presents a grid of test result data with summation
 * 
 * @author Peter Hayes
 */
public class TestStatisticsPortlet extends DashboardPortlet {
	
	@DataBoundConstructor
	public TestStatisticsPortlet(String name) {
		super(name);
	}
	
	public TestResultSummary getTestResultSummary(Collection<Job> jobs) {
		return TestUtil.getTestResultSummary(jobs);
	}
	
	@Extension
    public static class DescriptorImpl extends Descriptor<DashboardPortlet> {

		@Override
		public String getDisplayName() {
			return Messages.Dashboard_TestStatisticsGrid();
		}
	}
}
