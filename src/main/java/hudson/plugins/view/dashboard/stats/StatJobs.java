package hudson.plugins.view.dashboard.stats;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.Hudson;
import hudson.model.Job;
import hudson.model.TopLevelItem;
import hudson.plugins.view.dashboard.DashboardPortlet;
import hudson.plugins.view.dashboard.Messages;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Job statistics - number of jobs with given health status
 *
 * @author Vojtech Juranek
 */
public class StatJobs extends DashboardPortlet {

   @DataBoundConstructor
   public StatJobs(String name) {
      super(name);
   }

   /**
    * Heath status of the builds (use enum not class for it)
    *
    * FIXME: Should be refactored to not duplicate logic from Jenkins core.
    *
    * @deprecated Should be replaced with {@link hudson.model.HealthReport}
    */
   @Deprecated
   public enum HealthStatus {

      HEALTH_OVER_80("health-80plus.gif", Messages.Dashboard_NoRecentBuildsFailed()),
      HEALTH_60_TO_79("health-60to79.gif", Messages.Dashboard_RecentBuildsFailed("20", "40")),
      HEALTH_40_TO_59("health-40to59.gif", Messages.Dashboard_RecentBuildsFailed("40", "60")),
      HEALTH_20_TO_39("health-20to39.gif", Messages.Dashboard_RecentBuildsFailed("60", "80")),
      HEALTH_0_TO_19("health-00to19.gif", Messages.Dashboard_AllRecentBuildsFailed()),
      HEALTH_UNKNOWN("empty.gif", Messages.Dashboard_UnknownStatus());
      // private HealthReport healthReport;
      private final String iconUrl;
      private final String description;

      HealthStatus(String iconUrl, String description) {
         this.iconUrl = iconUrl;
         this.description = description;
      }

      public static HealthStatus getHealthStatus(Job job) {
         int score = job.getBuildHealth().getScore();
         if (score <= 20) {
            return HEALTH_0_TO_19;
         }
         if (score <= 40) {
            return HEALTH_20_TO_39;
         }
         if (score <= 60) {
            return HEALTH_40_TO_59;
         }
         if (score <= 80) {
            return HEALTH_60_TO_79;
         }

         return job.getFirstBuild() != null ? HEALTH_OVER_80 : HEALTH_UNKNOWN;
      }

      public String getIconUrl() {
         return Hudson.RESOURCE_PATH + "/images/32x32/" + iconUrl;
      }

      public String getIconUrl(String size) {
         if (iconUrl == null) {
            return Hudson.RESOURCE_PATH + "/images/" + size + "/" + HEALTH_UNKNOWN.getIconUrl();
         }
         if (iconUrl.startsWith("/")) {
            return iconUrl.replace("/32x32/", "/" + size + "/");
         }
         return Hudson.RESOURCE_PATH + "/images/" + size + "/" + iconUrl;
      }

      public String getDescription() {
         return description;
      }
   }

   /**
    * Project statistics - number of projects with given health status
    */
   public Map<HealthStatus, Integer> getJobStat(List<TopLevelItem> jobs) {
      SortedMap<HealthStatus, Integer> colStatJobs = new TreeMap<HealthStatus, Integer>();
      for (HealthStatus status : HealthStatus.values()) {
         colStatJobs.put(status, 0);
      }
      // Job and build statistics
      for (TopLevelItem job : jobs) {
         if (job instanceof Job) {
            HealthStatus status = HealthStatus.getHealthStatus(((Job) job));
            colStatJobs.put(status, colStatJobs.get(status) + 1);
         }
      }
      return colStatJobs;
   }

   @Extension
   public static class DescriptorImpl extends Descriptor<DashboardPortlet> {

      @Override
      public String getDisplayName() {
         return Messages.Dashboard_JobStatistics();
      }
   }
}
