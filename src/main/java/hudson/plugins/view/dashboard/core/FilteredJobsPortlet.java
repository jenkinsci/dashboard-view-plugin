package hudson.plugins.view.dashboard.core;

import java.util.List;

import hudson.Extension;
import hudson.matrix.MatrixConfiguration;
import hudson.matrix.MatrixProject;
import hudson.model.Descriptor;
import hudson.model.Job;
import hudson.model.Run;
import hudson.plugins.view.dashboard.DashboardPortlet;

import org.kohsuke.stapler.DataBoundConstructor;

import hudson.plugins.view.dashboard.Messages;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Pattern;

/**
 * Portlet displays a grid of job names with status and links to jobs.
 * 
 * @author Peter Hayes
 */
public class FilteredJobsPortlet extends DashboardPortlet {

   private static final int MIN_COLUMNCOUNT = 1;
   private static final String DEFAULT_FILTER_TEXT = ".*";
   private static final boolean DEFAULT_INCLUDE_LAST_BUILD = false;
   
   private final int columnCount;
   private boolean fillColumnFirst = false;   
   private final String includeRegex;
   private boolean includeLastBuild = false;
   
   transient Pattern includePattern;

   @DataBoundConstructor
   public FilteredJobsPortlet(String name, String filterText, int columnCount,
                      boolean fillColumnFirst, boolean includeLastBuild) {
      super(name);
      this.includeRegex = filterText;
      this.columnCount = columnCount;
      this.fillColumnFirst = fillColumnFirst;
      this.includeLastBuild = includeLastBuild;      
   }
   
   // Called once from jelly to compile the Pattern, if placed in the ctor, it doesnt survive a reboot.
   public void doInitFilterPattern() {
      if (includeRegex != null) {
         includePattern = Pattern.compile(includeRegex);
      }         
   }
   
   public int getColumnCount() {
      return columnCount <= 0 ? MIN_COLUMNCOUNT : columnCount;
   }
   
   public String getFilterText() {
      return includeRegex;
   }

   public int getRowCount() {
      List<Job> jobs = getJobs();
      
      int s = jobs.size();
      int rowCount = s / getColumnCount();
      if (s % getColumnCount() > 0) {
         rowCount += 1;
      }
      return rowCount;
   }

   public boolean getFillColumnFirst() {
      return fillColumnFirst;
   }
   
   public boolean getIncludeLastBuild() {                
      return includeLastBuild;
   }

   public List<Run> getMostRecentBuilds(Job job) {   
      List<Run> recentBuilds = new ArrayList<>();      
      
      // Add the last Build of the current job
      Run lb = job.getLastBuild();
      if (lb != null) {                
         recentBuilds.add(lb);
      }            
      
      // If it is a matrix job, add all last builds of active configurations
      if (job instanceof MatrixProject) {            
         Collection<MatrixConfiguration> mcs = ((MatrixProject)job).getActiveConfigurations();
         
         for (MatrixConfiguration mc : mcs) {
            Run lmb = mc.getLastBuild();
            if (lmb != null) {                
               recentBuilds.add(lmb);
            }                
         }
      }      
      return recentBuilds;
   }
   
   public String getBuildColumnSortData(Run<?, ?> build) {
      return String.valueOf(build.getNumber());
   }
   
   public String getTimestampSortData(Run run) {
      return String.valueOf(run.getTimeInMillis());
   }

   public String getTimestampString(Run run) {
      return DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM).format(new Date(run.getTimeInMillis()));
   }      
   
   private List<Job> getFilteredJobs() {
      List<Job> jobs = getDashboard().getJobs();
      
      // Remove jobs not matching on the regex pattern
      Iterator<Job> iter = jobs.iterator();

      while (iter.hasNext()) {
         String jobName = iter.next().getName();

         if (!includePattern.matcher(jobName).matches()) {
            iter.remove();   
         }              
      }
      return jobs;
   }
   
   private List<Job> getJobs() {      
      return includePattern == null ? getDashboard().getJobs() : getFilteredJobs();      
   }
   
   public Job getJob(int curRow, int curColumun){
      List<Job> jobs = getJobs();            
      
      int idx;
      // get grid coordinates from given params
      if (fillColumnFirst){
         idx = curRow + curColumun * getRowCount();
         if (idx >= jobs.size()){
            return null;
         }
      }
      else {
         idx = curColumun + curRow * getColumnCount();
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
      
      public String getDefaultFilterText() {
          return DEFAULT_FILTER_TEXT;
      }
            
      public boolean getDefaultIncludeLastBuild() {
          return DEFAULT_INCLUDE_LAST_BUILD;
      }
      
      @Override
      public String getDisplayName() {
         return Messages.Dashboard_FilteredJobsGrid();
      }            
   }
}
