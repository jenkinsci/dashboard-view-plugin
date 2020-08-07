package hudson.plugins.view.dashboard.core;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.Job;
import hudson.plugins.view.dashboard.DashboardPortlet;
import hudson.plugins.view.dashboard.Messages;
import java.util.Iterator;
import org.kohsuke.stapler.DataBoundConstructor;

public class IframePortlet extends DashboardPortlet {

  private String iframeSource;
  private String effectiveUrl;
  private final String divStyle;

  @DataBoundConstructor
  public IframePortlet(String name, String iframeSource, String divStyle) {
    super(name);
    this.setIframeSource(iframeSource);
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

  public void setIframeSource(String iframeSource) {
    this.iframeSource = iframeSource;
    this.overridePlaceholdersInUrl();
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
  }
}
