/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hudson.plugins.view.dashboard.stats;

import hudson.Extension;
import hudson.model.Computer;
import hudson.model.Descriptor;
import hudson.model.Hudson;
import hudson.model.Job;
import hudson.plugins.view.dashboard.DashboardPortlet;
import org.kohsuke.stapler.DataBoundConstructor;
import hudson.plugins.view.dashboard.Messages;
import java.util.List;
import org.kohsuke.stapler.bind.JavaScriptMethod;

/**
 * 
 * 
 * @author Lucie Votypkova
 */
public class StatSlaves extends DashboardPortlet {

    @DataBoundConstructor
    public StatSlaves(String name) {
        super(name);
    }

    @JavaScriptMethod
    public int getSlaves() {
        Hudson hudson = Hudson.getInstance();
        return hudson.getNodes().size();
    }

    @JavaScriptMethod
    public int getOnlineSlaves() {
        Hudson hudson = Hudson.getInstance();
        Computer[] computers = hudson.getComputers();
        int countOnlineSlaves = 0;
        for(Computer computer : computers){
            if(computer.isOnline()){
                countOnlineSlaves++;
            }
        }
        return countOnlineSlaves -1;
    }
    
    @JavaScriptMethod
    public int getOfflineSlaves() {
        Hudson hudson = Hudson.getInstance();
        Computer[] computers = hudson.getComputers();
        int countOfflineSlaves = 0;
        for(Computer computer : computers){
            if(computer.isOffline() && computer.getConnectTime()!=0){
                countOfflineSlaves++;
            }
        }
        return countOfflineSlaves;
    }
    
    @JavaScriptMethod
    public int getDisconnectedSlaves() {
        Hudson hudson = Hudson.getInstance();
        Computer[] computers = hudson.getComputers();
        int countDisconnectedSlaves = 0;
        for(Computer computer : computers){
            if(computer.getConnectTime()== 0){
                countDisconnectedSlaves++;
            }
        }
        return countDisconnectedSlaves;
    }

    @JavaScriptMethod
    public int getTasksInQueue() {
        Hudson hudson = Hudson.getInstance();
        return hudson.getQueue().getItems().length;
    }
    
    @JavaScriptMethod
    public int getRunningJobs() {
        Hudson hudson = Hudson.getInstance();
        List<Job> jobs = hudson.getAllItems(Job.class);
        int countRunningJobs = 0;
        for(Job job:jobs){
            if(job.isBuilding()){
               countRunningJobs++; 
            }
        }
        return countRunningJobs;
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<DashboardPortlet> {

        @Override
        public String getDisplayName() {
            return Messages.Dashboard_SlavesStatistics();
        }
    }
}
