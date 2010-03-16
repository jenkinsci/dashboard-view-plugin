/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package hudson.plugins.view.dashboard.core;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.plugins.view.dashboard.DashboardPortlet;

import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Portlet displays standard hudson job list
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
            return "Hudson jobs list";
    }
  }
}
