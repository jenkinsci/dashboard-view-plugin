package hudson.plugins.view.dashboard.core;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.plugins.view.dashboard.DashboardPortlet;
import hudson.plugins.view.dashboard.Messages;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Portlet displays standard Jenkins job list
 *
 * @author marco.ambu
 */
public class HudsonStdJobsPortlet extends DashboardPortlet {

   @DataBoundConstructor
   public HudsonStdJobsPortlet(String name) {
      super(name);
   }

   @Extension
   public static class DescriptorImpl extends Descriptor<DashboardPortlet> {

      @Override
      public String getDisplayName() {
         return Messages.Dashboard_JenkinsJobsList();
      }
   }
}
