package hudson.plugins.view.dashboard.core;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.plugins.view.dashboard.DashboardPortlet;
import hudson.plugins.view.dashboard.Messages;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Portlet displays image fetched from specified URL
 *
 * @author rmihael@gmail.com
 */
public class ImagePortlet extends DashboardPortlet {

  private String url;

  @DataBoundConstructor
  public ImagePortlet(String name, String url) {
    super(name);
    this.url = url;
  }

  public String getUrl() {
    return this.url;
  }

  @Extension
  public static class DescriptorImpl extends Descriptor<DashboardPortlet> {

    @Override
    public String getDisplayName() {
      return Messages.Dashboard_Image();
    }
  }
}
