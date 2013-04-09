package hudson.plugins.view.dashboard.core;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.Job;
import hudson.model.Result;
import hudson.model.Run;
import hudson.model.TopLevelItem;
import hudson.plugins.view.dashboard.DashboardPortlet;
import hudson.views.JobColumn;
import hudson.views.ListViewColumn;
import hudson.views.StatusColumn;
import hudson.views.WeatherColumn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.kohsuke.stapler.DataBoundConstructor;

import hudson.plugins.view.dashboard.Messages;

/**
 * Portlet display a list of unstable (or worse) jobs in a simple
 * tabular format.
 * 
 * @author Peter Hayes
 */
public class UnstableJobsPortlet extends DashboardPortlet {

   private boolean showOnlyFailedJobs = false;
   
   private static final Collection<ListViewColumn> COLUMNS =
           Arrays.asList(new StatusColumn(), new WeatherColumn(), new JobColumn());

   @DataBoundConstructor
   public UnstableJobsPortlet(String name, boolean showOnlyFailedJobs) {
      super(name);
	  this.showOnlyFailedJobs = showOnlyFailedJobs;
   }

   /**
    * Given a list of all jobs, return just those that are unstable or worse.
    */
   public Collection<Job> getUnstableJobs(Collection<TopLevelItem> allJobs) {
      ArrayList<Job> unstableJobs = new ArrayList<Job>();

       for (TopLevelItem item : allJobs) {
         if (item instanceof Job) {
             Job job = (Job) item;
             Run run = job.getLastCompletedBuild();

             if (run != null) {
                Result expected = this.showOnlyFailedJobs ? Result.FAILURE : Result.UNSTABLE;

                if (expected.isBetterOrEqualTo(run.getResult())) {
                   unstableJobs.add(job);
                }
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
	
   @Extension
   public static class DescriptorImpl extends Descriptor<DashboardPortlet> {

      @Override
      public String getDisplayName() {
         return Messages.Dashboard_UnstableJobs();
      }
   }
}
