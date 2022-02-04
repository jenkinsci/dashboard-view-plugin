package hudson.plugins.view.dashboard.core;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.Job;
import hudson.plugins.view.dashboard.DashboardPortlet;
import hudson.plugins.view.dashboard.Messages;
import hudson.util.FormValidation;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

public class IframePortlet extends DashboardPortlet {

  private String iframeSource;
  private String effectiveUrl;
  private final String divStyle;

  @DataBoundConstructor
  public IframePortlet(String name, String divStyle) {
    super(name);
    this.divStyle = divStyle;
  }

  public String getIframeSource() {
    return iframeSource;
  }

  public String getEffectiveUrl() {
    return effectiveUrl;
  }

  public String getDivStyle() {
    return divStyle;
  }

  @DataBoundSetter
  public void setIframeSource(String iframeSource) {
    this.iframeSource = iframeSource;
    this.overridePlaceholdersInUrl();
  }

  public boolean isIframeSourceValid() {
    return getUrlError(iframeSource) == null;
  }

  private void overridePlaceholdersInUrl() {
    if (iframeSource != null) {
      if (getDashboard() != null) {
        effectiveUrl = iframeSource.replaceAll("\\$\\{viewName\\}", getDashboard().getViewName());
        effectiveUrl = effectiveUrl.replaceAll("\\$\\{jobsList\\}", jobsListAsString());
      } else {
        effectiveUrl = iframeSource;
      }
    } else {
      effectiveUrl = null;
    }
  }

  private String jobsListAsString() {
    StringBuilder sb = new StringBuilder();
    Iterator<Job> jobs = getDashboard().getJobs().iterator();
    if (jobs.hasNext()) {
      sb.append(jobs.next().getName());
    }
    while (jobs.hasNext()) {
      sb.append(",");
      sb.append(jobs.next().getName());
    }
    return sb.toString();
  }

  @Extension
  public static class DescriptorImpl extends Descriptor<DashboardPortlet> {

    @Override
    public String getDisplayName() {
      return Messages.Dashboard_IframePortlet();
    }

    public FormValidation doCheckIframeSource(@QueryParameter String value) {
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
      return Messages.Dashboard_UrlEmpty();
    }
    try {
      final URI allowedUrl = new URI(url);
      final String protocol = allowedUrl.getScheme();
      if (!allowedUrl.isAbsolute() || protocol.equals("http") || protocol.equals("https")) {
        return null;
      } else {
        return Messages.Dashboard_UrlHttp();
      }
    } catch (URISyntaxException e) {
      return Messages.Dashboard_UrlInvalid(e.getMessage());
    }
  }
}
