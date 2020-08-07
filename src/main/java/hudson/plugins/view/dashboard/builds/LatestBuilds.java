package hudson.plugins.view.dashboard.builds;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.Job;
import hudson.model.Run;
import hudson.plugins.view.dashboard.DashboardPortlet;
import java.text.DateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.kohsuke.stapler.DataBoundConstructor;

import hudson.plugins.view.dashboard.Messages;
import java.util.PriorityQueue;

public class LatestBuilds extends DashboardPortlet {

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
        List<Job> jobs = getDashboardJobs();

        PriorityQueue<Run> queue = new PriorityQueue<>(numBuilds, Run.ORDER_BY_DATE);
        for (Job job : jobs) {
            Run lb = job.getLastBuild();
            if (lb != null) {
                queue.add(lb);
            }
        }

        List<Run> recentBuilds = new ArrayList<>(numBuilds);
        Run build;
        while ((build = queue.poll()) != null) {
            recentBuilds.add(build);
            if (recentBuilds.size() == numBuilds) {
                break;
            }
            Run pb = build.getPreviousBuild();
            if (pb != null) {
                queue.add(pb);
            }
        }

        return recentBuilds;
   }

   /**
    * for unit test
    */
   protected List<Job> getDashboardJobs() {
       return getDashboard().getJobs();
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
