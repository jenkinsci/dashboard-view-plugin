package hudson.plugins.view.dashboard;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import hudson.DescriptorExtensionList;
import hudson.Extension;
import hudson.Util;
import hudson.model.Descriptor;
import hudson.model.Descriptor.FormException;
import hudson.model.Job;
import hudson.model.ListView;
import hudson.model.TopLevelItem;
import hudson.model.ViewDescriptor;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.servlet.ServletException;
import jenkins.security.stapler.StaplerDispatchable;
import net.sf.json.JSONObject;
import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.DoNotUse;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.StaplerRequest;

/**
 * View that can be customized with portlets to show the selected jobs information in various ways.
 *
 * @author Peter Hayes
 */
public class Dashboard extends ListView {

  /** Use custom CSS style provided by the user */
  private boolean useCssStyle = false;
  /** Show standard jobs list at the top of the page */
  private boolean includeStdJobList = false;
  /** Hide standard Jenkins panels (full screen view) */
  private boolean hideJenkinsPanels = false;
  /** The width of the left portlets */
  private String leftPortletWidth = "50%";
  /** The width of the right portlets */
  private String rightPortletWidth = "50%";

  private List<DashboardPortlet> leftPortlets = new ArrayList<>();
  private List<DashboardPortlet> rightPortlets = new ArrayList<>();
  private List<DashboardPortlet> topPortlets = new ArrayList<>();
  private List<DashboardPortlet> bottomPortlets = new ArrayList<>();

  @DataBoundConstructor
  public Dashboard(String name) {
    super(name);
  }

  public boolean isUseCssStyle() {
    return useCssStyle;
  }

  public boolean isIncludeStdJobList() {
    return includeStdJobList;
  }

  /** @since 2.13 */
  @DataBoundSetter
  public void setIncludeStdJobList(boolean includeStdJobList) {
    this.includeStdJobList = includeStdJobList;
  }

  public boolean isHideJenkinsPanels() {
    return hideJenkinsPanels;
  }

  @StaplerDispatchable
  public List<DashboardPortlet> getLeftPortlets() {
    return leftPortlets;
  }

  @StaplerDispatchable
  public List<DashboardPortlet> getRightPortlets() {
    return rightPortlets;
  }

  @StaplerDispatchable
  public List<DashboardPortlet> getTopPortlets() {
    return topPortlets;
  }

  @StaplerDispatchable
  public List<DashboardPortlet> getBottomPortlets() {
    return bottomPortlets;
  }

  public synchronized String getLeftPortletWidth() {
    return leftPortletWidth;
  }

  public synchronized String getRightPortletWidth() {
    return rightPortletWidth;
  }

  @DataBoundSetter
  public synchronized void setUseCssStyle(boolean useCssStyle) {
    this.useCssStyle = useCssStyle;
  }

  @DataBoundSetter
  public void setHideJenkinsPanels(boolean hideJenkinsPanels) {
    this.hideJenkinsPanels = hideJenkinsPanels;
  }

  @DataBoundSetter
  public synchronized void setLeftPortletWidth(String leftPortletWidth) {
    this.leftPortletWidth = leftPortletWidth;
  }

  @DataBoundSetter
  public synchronized void setRightPortletWidth(String rightPortletWidth) {
    this.rightPortletWidth = rightPortletWidth;
  }

  @DataBoundSetter
  public void setLeftPortlets(List<DashboardPortlet> leftPortlets) {
    this.leftPortlets = leftPortlets;
  }

  @DataBoundSetter
  public void setRightPortlets(List<DashboardPortlet> rightPortlets) {
    this.rightPortlets = rightPortlets;
  }

  @DataBoundSetter
  public void setTopPortlets(List<DashboardPortlet> topPortlets) {
    this.topPortlets = topPortlets;
  }

  @DataBoundSetter
  public void setBottomPortlets(List<DashboardPortlet> bottomPortlets) {
    this.bottomPortlets = bottomPortlets;
  }

  public String getPortletUrl(DashboardPortlet portlet) {
    int pos = topPortlets.indexOf(portlet);
    if (pos > -1) return "topPortlets/" + pos;
    pos = leftPortlets.indexOf(portlet);
    if (pos > -1) return "leftPortlets/" + pos;
    pos = rightPortlets.indexOf(portlet);
    if (pos > -1) return "rightPortlets/" + pos;
    pos = bottomPortlets.indexOf(portlet);
    if (pos > -1) return "bottomPortlets/" + pos;
    return "";
  }

  @Deprecated
  public DescriptorExtensionList<DashboardPortlet, Descriptor<DashboardPortlet>>
      getDashboardPortletDescriptors() {
    return DashboardPortlet.all();
  }

  public List<Descriptor<DashboardPortlet>> getSortedDashboardPortletDescriptors() {
    DescriptorExtensionList<DashboardPortlet, Descriptor<DashboardPortlet>> list =
        DashboardPortlet.all();
    List<Descriptor<DashboardPortlet>> descriptors = new ArrayList<>(list);

    descriptors.sort(Comparator.comparing(Descriptor::getDisplayName));

    return descriptors;
  }

  /* Use contains */
  @Deprecated
  @SuppressFBWarnings(
      value = "NM_METHOD_NAMING_CONVENTION",
      justification = "backwards compatibility, but seems unused internally.")
  @Restricted(DoNotUse.class)
  public synchronized boolean HasItem(TopLevelItem item) {
    List<TopLevelItem> items = getItems();
    return items.contains(item);
  }

  /* Use getItems */
  public synchronized List<Job> getJobs() {
    List<Job> jobs = new ArrayList<>();

    for (TopLevelItem item : getItems()) {
      if (item instanceof Job) {
        jobs.add((Job) item);
      }
    }

    return jobs;
  }

  @Override
  protected synchronized void submit(StaplerRequest req)
      throws IOException, ServletException, FormException {
    super.submit(req);

    try {
      req.setCharacterEncoding("UTF-8");
    } catch (UnsupportedEncodingException ex) {
      DashboardLog.error("Dashboard", ex.getLocalizedMessage());
    }
    JSONObject json = req.getSubmittedForm();

    String sIncludeStdJobList = Util.nullify(req.getParameter("includeStdJobList"));
    includeStdJobList = sIncludeStdJobList != null && "on".equals(sIncludeStdJobList);

    String shideJenkinsPanels = Util.nullify(req.getParameter("hideJenkinsPanels"));
    hideJenkinsPanels = shideJenkinsPanels != null && "on".equals(shideJenkinsPanels);

    String sUseCssStyle = Util.nullify(req.getParameter("useCssStyle"));
    useCssStyle = sUseCssStyle != null && "on".equals(sUseCssStyle);

    if (useCssStyle) {
      if (req.getParameter("leftPortletWidth") != null) {
        leftPortletWidth = req.getParameter("leftPortletWidth");
      }

      if (req.getParameter("rightPortletWidth") != null) {
        rightPortletWidth = req.getParameter("rightPortletWidth");
      }
    } else {
      leftPortletWidth = rightPortletWidth = "50%";
    }

    topPortlets =
        Descriptor.newInstancesFromHeteroList(req, json, "topPortlet", DashboardPortlet.all());
    leftPortlets =
        Descriptor.newInstancesFromHeteroList(req, json, "leftPortlet", DashboardPortlet.all());
    rightPortlets =
        Descriptor.newInstancesFromHeteroList(req, json, "rightPortlet", DashboardPortlet.all());
    bottomPortlets =
        Descriptor.newInstancesFromHeteroList(req, json, "bottomPortlet", DashboardPortlet.all());
  }

  @Override
  public void rename(String newName) throws FormException {
    super.rename(newName);
    // Bug 6689 <http://issues.jenkins-ci.org/browse/JENKINS-6689>
    // TODO: if this view is the default view configured in Jenkins, the we must keep it after
    // renaming
  }

  @Extension
  public static final class DescriptorImpl extends ViewDescriptor {

    @Override
    public String getDisplayName() {
      return Messages.Dashboard_DisplayName();
    }
  }
}
