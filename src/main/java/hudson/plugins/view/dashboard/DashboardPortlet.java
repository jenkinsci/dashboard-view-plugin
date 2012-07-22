package hudson.plugins.view.dashboard;

import hudson.DescriptorExtensionList;
import hudson.ExtensionPoint;
import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.model.Hudson;
import hudson.model.ModelObject;
import hudson.model.ParameterDefinition;
import hudson.model.TopLevelItem;

import org.kohsuke.stapler.Stapler;
import org.kohsuke.stapler.StaplerRequest;

import java.util.Comparator;
import java.util.Random;

/**
 * Report that can summarize project data across multiple projects and display
 * the resulting data.
 * 
 * @author Peter Hayes
 */
public abstract class DashboardPortlet implements ModelObject, Describable<DashboardPortlet>, ExtensionPoint {

   private static Random generator = new Random();
   private String id = null;
   private String name;

   public DashboardPortlet(String name) {
      if (this.id == null)
      {
         this.id = "dashboard_portlet_" + generator.nextInt(32000);
      }
      this.name = name;
      DashboardLog.debug("DashboardPortlet", name + " - " + id);
   }

   public String getId() {
      return id;
   }

   public String getName() {
      return name;
   }

   public Dashboard getDashboard() {
      // TODO Can the dashboard instance be a field on this class -- parent?
      StaplerRequest req = Stapler.getCurrentRequest();
      return req.findAncestorObject(Dashboard.class);
   }

   public String getDisplayName() {
      return getName();
   }

   public String getUrl() {
      return "portlet/" + getId() + '/';
   }

   /**
    * Support accessing jobs available via view through portlets
    */
   public TopLevelItem getJob(String name) {
      return getDashboard().getJob(name);
   }

   /**
    * {@inheritDoc}
    */
   public Descriptor<DashboardPortlet> getDescriptor() {
      return (Descriptor<DashboardPortlet>) Hudson.getInstance().getDescriptor(getClass());
   }

   /**
    * Returns all the registered {@link ParameterDefinition} descriptors.
    */
   public static DescriptorExtensionList<DashboardPortlet, Descriptor<DashboardPortlet>> all() {
      return Hudson.getInstance().getDescriptorList(DashboardPortlet.class);
   }

   public static Comparator getComparator() {
      return new Comparator<Dashboard>() {

         public int compare(Dashboard p1, Dashboard p2) {
            return p1.getDescription().compareTo(p2.getDescription());
         }
      };
   }
}
