/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hudson.plugins.view.dashboard.stats;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.Hudson;
import hudson.plugins.view.dashboard.DashboardPortlet;
import org.kohsuke.stapler.DataBoundConstructor;
import hudson.plugins.view.dashboard.Messages;
import org.kohsuke.stapler.bind.JavaScriptMethod;

/**
 * 
 * 
 * @author Lucie Votypkova
 */
public class StatCommon extends DashboardPortlet {

    @DataBoundConstructor
    public StatCommon(String name) {
        super(name);
    }

    @JavaScriptMethod
    public int getSlaves() {
        Hudson hudson = Hudson.getInstance();
        return hudson.getNodes().size();
    }

    @JavaScriptMethod
    public int getWorkingSlaves() {
        Hudson hudson = Hudson.getInstance();
        return hudson.getComputers().length;
    }

    @JavaScriptMethod
    public int getTasksInQueue() {
        Hudson hudson = Hudson.getInstance();
        return hudson.getQueue().getItems().length;
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<DashboardPortlet> {

        @Override
        public String getDisplayName() {
            return Messages.Dashboard_CommonStatistics();
        }
    }
}
