/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package hudson.plugins.view.dashboard.core;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.plugins.view.dashboard.DashboardPortlet;

import hudson.views.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Portlet displays standard hudson job list
 *
 * @author marco.ambu
 */
public class HudsonStdJobsPortlet extends DashboardPortlet {

  private static final Collection<ListViewColumn> COLUMNS = Arrays.asList(
                      new StatusColumn(),
                      new WeatherColumn(),
                      new JobColumn(),
                      new LastSuccessColumn(),
                      new LastFailureColumn(),
                      new LastDurationColumn(),
                      new BuildButtonColumn());

  @DataBoundConstructor
  public HudsonStdJobsPortlet(String name) {
    super(name);
  }

  public Collection<ListViewColumn> getColumns() {
    return COLUMNS;
  }

  @Extension
  public static class DescriptorImpl extends Descriptor<DashboardPortlet> {

    @Override
    public String getDisplayName() {
            return "Hudson jobs list";
    }
  }
}
