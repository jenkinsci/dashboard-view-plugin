package hudson.plugins.view.dashboard.core;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import hudson.Extension;
import hudson.model.Descriptor;
import hudson.plugins.view.dashboard.DashboardPortlet;
import hudson.plugins.view.dashboard.Messages;
import hudson.util.FormValidation;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

/**
 * Portlet displays image fetched from specified URL
 *
 * @author rmihael@gmail.com
 */
public class ImagePortlet extends DashboardPortlet {

  private String url;

  @DataBoundConstructor
  public ImagePortlet(String name) {
    super(name);
  }

  @Deprecated
  public ImagePortlet(String name, String url) {
    super(name);
    this.url = url;
  }

  public String getImageUrl() {
    return this.url;
  }

  @DataBoundSetter
  public void setImageUrl(final String url) {
    this.url = url;
  }

  @Deprecated @DataBoundSetter
  public void setUrl(final String url) {
    this.url = url;
  }

  public boolean isUrlValid() {
    return getUrlError(url) == null;
  }

  @Extension
  public static class DescriptorImpl extends Descriptor<DashboardPortlet> {

    @Override
    public String getDisplayName() {
      return Messages.Dashboard_Image();
    }

    public FormValidation doCheckImageUrl(@QueryParameter String value) {
      String error = getUrlError(value);
      if (error != null) {
        return FormValidation.error(error);
      }
      return FormValidation.ok();
    }
  }

  /**
   * Checks if the passed string is a valid HTTP or relative URL.
   *
   * @return Localized error message or null if URL is valid.
   */
  @CheckForNull
  private static final String getUrlError(String url) {
    if (StringUtils.isBlank(url)) {
      return Messages.Dashboard_ImageUrlEmpty();
    }
    try {
      final URI allowedUrl = new URI(url);
      final String protocol = allowedUrl.getScheme();
      if (!allowedUrl.isAbsolute() || protocol.equals("http") || protocol.equals("https")) {
        return null;
      } else {
        return Messages.Dashboard_ImageUrlHttp();
      }
    } catch (URISyntaxException e) {
      return Messages.Dashboard_ImageUrlInvalid(e.getMessage());
    }
  }
}
