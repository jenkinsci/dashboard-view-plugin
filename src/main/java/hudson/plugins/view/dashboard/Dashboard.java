package hudson.plugins.view.dashboard;

import hudson.DescriptorExtensionList;
import hudson.Extension;
import hudson.Util;
import hudson.model.Descriptor;
import hudson.model.Job;
import hudson.model.TopLevelItem;
import hudson.model.ViewDescriptor;
import hudson.model.Descriptor.FormException;
import hudson.model.ListView;
import java.io.IOException;

import java.io.UnsupportedEncodingException;
import java.util.*;

import javax.servlet.ServletException;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

/**
 * View that can be customized with portlets to show the selected jobs information
 * in various ways.
 * 
 * @author Peter Hayes
 */
public class Dashboard extends ListView {
   /*
    * Use custom CSS style provided by the user
    */

   private boolean useCssStyle = false;
   /*
    * Show standard jobs list at the top of the page
    */
   private boolean includeStdJobList = false;
   /*
    * The width of the left portlets
    */
   private String leftPortletWidth = "50%";
   /*
    * The width of the right portlets
    */
   private String rightPortletWidth = "50%";
   private List<DashboardPortlet> leftPortlets = new ArrayList<DashboardPortlet>();
   private List<DashboardPortlet> rightPortlets = new ArrayList<DashboardPortlet>();
   private List<DashboardPortlet> topPortlets = new ArrayList<DashboardPortlet>();
   private List<DashboardPortlet> bottomPortlets = new ArrayList<DashboardPortlet>();

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

   public List<DashboardPortlet> getLeftPortlets() {
      return leftPortlets;
   }

   public List<DashboardPortlet> getRightPortlets() {
      return rightPortlets;
   }

   public List<DashboardPortlet> getTopPortlets() {
      return topPortlets;
   }

   public List<DashboardPortlet> getBottomPortlets() {
      return bottomPortlets;
   }

   public String getLeftPortletWidth() {
      return leftPortletWidth;
   }

   public String getRightPortletWidth() {
      return rightPortletWidth;
   }

   public DashboardPortlet getPortlet(String name) {
      ArrayList<DashboardPortlet> allPortlets = new ArrayList<DashboardPortlet>(topPortlets);
      allPortlets.addAll(leftPortlets);
      allPortlets.addAll(rightPortlets);
      allPortlets.addAll(bottomPortlets);
      for (DashboardPortlet portlet : allPortlets) {
         if (name.equals(portlet.getId())) {
            return portlet;
         }
      }
      return null;
   }

   public DescriptorExtensionList<DashboardPortlet, Descriptor<DashboardPortlet>> getDashboardPortletDescriptors() {
      DescriptorExtensionList<DashboardPortlet, Descriptor<DashboardPortlet>> list = DashboardPortlet.all();
      //Collections.sort(list);
//    Collections.sort(list, new Comparator<Descriptor<DashboardPortlet>>() {
//      public int compare(Descriptor<DashboardPortlet> p1, Descriptor<DashboardPortlet> p2) {
//        return p1.getDisplayName().compareTo(p2.getDisplayName());
//      }
//    });
      return list;
   }

   /* Use contains */
   //@Deprecated
   public synchronized boolean HasItem(TopLevelItem item) {
      List<TopLevelItem> items = getItems();
      return items.contains(item);
//    return this.contains(item);
   }

   /* Use getItems */
   public synchronized List<Job> getJobs() {
      List<Job> jobs = new ArrayList<Job>();
      
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

      topPortlets = Descriptor.newInstancesFromHeteroList(req, json, "topPortlet", DashboardPortlet.all());
      leftPortlets = Descriptor.newInstancesFromHeteroList(req, json, "leftPortlet", DashboardPortlet.all());
//      for (DashboardPortlet p: leftPortlets)
//      {
//         DashboardLog.debug("Dashboard", p.getDisplayName() + " " + p.getId());
//      }
      rightPortlets = Descriptor.newInstancesFromHeteroList(req, json, "rightPortlet", DashboardPortlet.all());
//      for (DashboardPortlet p: rightPortlets)
//      {
//         DashboardLog.debug("Dashboard", p.getDisplayName() + " " + p.getId());
//      }
      bottomPortlets = Descriptor.newInstancesFromHeteroList(req, json, "bottomPortlet", DashboardPortlet.all());
   }

   @Override
   public void rename(String newName) throws FormException {
      super.rename(newName);
      // Bug 6689 <http://issues.jenkins-ci.org/browse/JENKINS-6689>
      // TODO: if this view is the default view configured in Jenkins, the we must keep it after renaming
   }

   @Extension
   public static final class DescriptorImpl extends ViewDescriptor {

      @Override
      public String getDisplayName() {
         return Messages.Dashboard_DisplayName();
      }
   }
}
