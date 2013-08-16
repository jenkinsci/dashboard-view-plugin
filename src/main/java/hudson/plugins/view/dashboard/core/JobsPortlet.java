package hudson.plugins.view.dashboard.core;

import java.util.List;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.Job;
import hudson.plugins.view.dashboard.DashboardPortlet;

import org.kohsuke.stapler.DataBoundConstructor;

import hudson.plugins.view.dashboard.Messages;

/**
 * Portlet displays a grid of job names with status and links to jobs.
 * 
 * @author Peter Hayes
 */
public class JobsPortlet extends DashboardPortlet {

   private static final int MIN_COLUMNCOUNT = 3;
   
   private int columnCount;
   private boolean fillColumnFirst = false;

   @DataBoundConstructor
   public JobsPortlet(String name,
                      int columnCount,
                      boolean fillColumnFirst) {
      super(name);
      this.columnCount = columnCount;
      this.fillColumnFirst = fillColumnFirst;
   }
   
   public int getColumnCount() {
      return columnCount <= 0 ? MIN_COLUMNCOUNT : columnCount;
   }

   public int getRowCount() {
      int s = this.getDashboard().getJobs().size();
      int rowCount = s / this.getColumnCount();
      if (s % this.getColumnCount() > 0){
         rowCount += 1;
      }
      return rowCount;
   }

   public boolean getFillColumnFirst() {
      return this.fillColumnFirst;
   }

   public Job getJob(int curRow, int curColumun){
      List<Job> jobs = this.getDashboard().getJobs();
      int idx;
      // get grid coordinates from given params
      if (this.fillColumnFirst){
         idx = curRow + curColumun * this.getRowCount();
         if (idx >= jobs.size()){
            return null;
         }
      }
      else {
         idx = curColumun + curRow * this.getColumnCount();
         if (idx >= jobs.size()){
            return null;
         }
      }
     return jobs.get(idx);
   }

   @Extension
   public static class DescriptorImpl extends Descriptor<DashboardPortlet> {

      public int getDefaultCoulmnCount() {
          return MIN_COLUMNCOUNT;
      } 
      
      @Override
      public String getDisplayName() {
         return Messages.Dashboard_JobsGrid();
      }
   }
}
