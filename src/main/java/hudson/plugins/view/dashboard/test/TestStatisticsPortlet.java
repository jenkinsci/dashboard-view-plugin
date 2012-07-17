package hudson.plugins.view.dashboard.test;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.Job;
import hudson.plugins.view.dashboard.DashboardPortlet;

import java.util.Collection;

import org.kohsuke.stapler.DataBoundConstructor;

import hudson.plugins.view.dashboard.Messages;
import java.text.DecimalFormat;

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

   public String format(DecimalFormat df, double val) {
      if (val < 1d && val > .99d) {
         return "<100%";
      } 
      if (val > 0d && val < .01d) {
         return ">0%";
      } 
      return df.format(val);
   }

   @Extension
   public static class DescriptorImpl extends Descriptor<DashboardPortlet> {

      @Override
      public String getDisplayName() {
         return Messages.Dashboard_TestStatisticsGrid();
      }
   }
}
