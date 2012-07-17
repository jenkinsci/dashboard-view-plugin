package hudson.plugins.view.dashboard.core;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.plugins.view.dashboard.DashboardPortlet;

import org.kohsuke.stapler.DataBoundConstructor;

import hudson.plugins.view.dashboard.Messages;

/**
 * Portlet displays a grid of job names with status and links to jobs.
 * 
 * @author Peter Hayes
 */
public class JobsPortlet extends DashboardPortlet {

   @DataBoundConstructor
   public JobsPortlet(String name) {
      super(name);
   }

   @Extension
   public static class DescriptorImpl extends Descriptor<DashboardPortlet> {

      @Override
      public String getDisplayName() {
         return Messages.Dashboard_JobsGrid();
      }
   }
}
