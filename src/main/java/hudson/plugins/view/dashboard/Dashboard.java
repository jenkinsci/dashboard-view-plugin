package hudson.plugins.view.dashboard;

import hudson.Extension;
import hudson.Util;
import hudson.model.AbstractProject;
import hudson.model.Descriptor;
import hudson.model.Hudson;
import hudson.model.Item;
import hudson.model.Job;
import hudson.model.TopLevelItem;
import hudson.model.View;
import hudson.model.ViewDescriptor;
import hudson.model.Descriptor.FormException;
import hudson.util.CaseInsensitiveComparator;
import hudson.util.FormValidation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.servlet.ServletException;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

/**
 * View that can be customized with portlets to show the selected jobs information
 * in various ways.
 * 
 * @author Peter Hayes
 */
public class Dashboard extends View {
	/*
	 * List of job names to be included in this view
	 */
	final SortedSet<String> jobNames = new TreeSet<String>(CaseInsensitiveComparator.INSTANCE);

    /*
     * Include regex string.
     */
    private String includeRegex;
    
    /*
     * If using regex, prevent disabled jobs from appearing in the list
     */
    private boolean excludeDisabledJobs = false;
    
    /*
     * Compiled include pattern from the includeRegex string.
     */
    private transient Pattern includePattern;
   /*
    * Show standard hudson jobs list at the top of the page
    */
    private boolean includeStdJobList = false;
    
    /*
     * The configured summarized reports for this summary view
     */
    private List<DashboardPortlet> leftPortlets = new ArrayList<DashboardPortlet>();
    private List<DashboardPortlet> rightPortlets = new ArrayList<DashboardPortlet>();
    private List<DashboardPortlet> topPortlets = new ArrayList<DashboardPortlet>();
	
    @DataBoundConstructor
    public Dashboard(String name) {
        super(name);
    }

    private Object readResolve() {
        if(includeRegex!=null)
            includePattern = Pattern.compile(includeRegex);
        return this;
    }
    
    public String getIncludeRegex() {
		return includeRegex;
	}
    
    public boolean isExcludeDisabledJobs() {
		return excludeDisabledJobs;
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
    
    public DashboardPortlet getPortlet(String name) {
    	ArrayList<DashboardPortlet> allPortlets = new ArrayList<DashboardPortlet>(leftPortlets);
    	allPortlets.addAll(rightPortlets);
        allPortlets.addAll(topPortlets);
    	for (DashboardPortlet portlet : allPortlets) {
    		if (name.equals(portlet.getName())) {
    			return portlet;
    		}
    	}
    	return null;
    }
    
    public List<Descriptor<DashboardPortlet>> getDashboardPortletDescriptors() {
    	return DashboardPortlet.all();
    }

	@Override
	public synchronized boolean contains(TopLevelItem item) {
		return jobNames.contains(item.getName());
	}

	@Override
	public Item doCreateItem(StaplerRequest req, StaplerResponse rsp)
			throws IOException, ServletException {
		Item item = Hudson.getInstance().doCreateItem(req, rsp);
        if (item != null) {
        	synchronized (this) {
        		jobNames.add(item.getName());
        	}
            owner.save();
        }
        return item;
	}
	
    /**
     * Returns a read-only view of all {@link Job}s in this view.
     *
     * <p>
     * This method returns a separate copy each time to avoid
     * concurrent modification issue.
     */
    public synchronized List<TopLevelItem> getItems() {
        SortedSet<String> names = new TreeSet<String>(jobNames);

        if (includePattern != null) {
            for (TopLevelItem item : Hudson.getInstance().getItems()) {
                String itemName = item.getName();
                if (includePattern.matcher(itemName).matches()) {
                    names.add(itemName);
                }
            }
        }

        List<TopLevelItem> items = new ArrayList<TopLevelItem>(names.size());
        for (String n : names) {
            TopLevelItem item = Hudson.getInstance().getItem(n);
            if (item != null) {
            	if (isExcludeDisabledJobs() && item instanceof AbstractProject) {
            		AbstractProject project = (AbstractProject) item;
            		
            		if (!project.isDisabled()) {
            			items.add(item);
            		}
            	} else {
                    items.add(item);
            	}
            }
        }
        
        return items;
    }
    
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
	public synchronized void onJobRenamed(Item item, String oldName, String newName) {
		if (jobNames.remove(oldName) && newName != null) {
			jobNames.add(newName);
		}
	}

	@Override
	protected synchronized void submit(StaplerRequest req) throws IOException,
			ServletException, FormException {
        req.setCharacterEncoding("UTF-8");
        JSONObject json = req.getSubmittedForm();
        
        jobNames.clear();
        for (TopLevelItem item : Hudson.getInstance().getItems()) {
            if(req.getParameter(item.getName())!=null)
                jobNames.add(item.getName());
        }

        if (req.getParameter("useincluderegex") != null) {
            includeRegex = Util.nullify(req.getParameter("includeRegex"));
            includePattern = Pattern.compile(includeRegex);
            
            String sExcludeDisabledJobs = Util.nullify(req.getParameter("excludeDisabledJobs"));
            excludeDisabledJobs = sExcludeDisabledJobs != null && "on".equals(sExcludeDisabledJobs);
        } else {
            includeRegex = null;
            includePattern = null;
            excludeDisabledJobs = false;
        }

        String sIncludeStdJobList = Util.nullify(req.getParameter("includeStdJobList"));
        includeStdJobList = sIncludeStdJobList != null && "on".equals(sIncludeStdJobList);

        leftPortlets = Descriptor.newInstancesFromHeteroList(req, json, "leftPortlet", DashboardPortlet.all());
        rightPortlets = Descriptor.newInstancesFromHeteroList(req, json, "rightPortlet", DashboardPortlet.all());
        topPortlets = Descriptor.newInstancesFromHeteroList(req, json, "topPortlet", DashboardPortlet.all());
	}
	
	@Extension
    public static final class DescriptorImpl extends ViewDescriptor {

        @Override
        public String getDisplayName() {
            return "Dashboard";
        }

        /**
         * Checks if the include regular expression is valid.
         */
        public FormValidation doCheckIncludeRegex( @QueryParameter String value ) throws IOException, ServletException, InterruptedException  {
            String v = Util.fixEmpty(value);
            if (v != null) {
                try {
                    Pattern.compile(v);
                } catch (PatternSyntaxException pse) {
                    return FormValidation.error(pse.getMessage());
                }
            }
            return FormValidation.ok();
        }
    }
}
