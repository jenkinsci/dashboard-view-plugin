package hudson.plugins.view.dashboard.stats;

import hudson.Extension;
import hudson.model.Computer;
import hudson.model.Descriptor;
import hudson.model.Job;
import hudson.plugins.view.dashboard.DashboardPortlet;
import hudson.plugins.view.dashboard.Messages;
import java.util.List;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.bind.JavaScriptMethod;

/** @author Lucie Votypkova */
public class StatSlaves extends DashboardPortlet {

  @DataBoundConstructor
  public StatSlaves(String name) {
    super(name);
  }

  @JavaScriptMethod
  public int getAgents() {
    return Jenkins.getActiveInstance().getNodes().size();
  }

  @JavaScriptMethod
  public int getOnlineAgents() {
    Computer[] computers = Jenkins.getActiveInstance().getComputers();
    int countOnlineAgents = 0;
    for (Computer computer : computers) {
      if (computer.isOnline()) {
        countOnlineAgents++;
      }
    }
    return countOnlineAgents - 1;
  }

  @JavaScriptMethod
  public int getOfflineAgents() {
    Computer[] computers = Jenkins.getActiveInstance().getComputers();
    int countOfflineAgents = 0;
    for (Computer computer : computers) {
      if (computer.isOffline() && computer.getConnectTime() != 0) {
        countOfflineAgents++;
      }
    }
    return countOfflineAgents;
  }

  @JavaScriptMethod
  public int getDisconnectedAgents() {
    Computer[] computers = Jenkins.getActiveInstance().getComputers();
    int countDisconnectedAgents = 0;
    for (Computer computer : computers) {
      if (computer.getConnectTime() == 0) {
        countDisconnectedAgents++;
      }
    }
    return countDisconnectedAgents;
  }

  @JavaScriptMethod
  public int getTasksInQueue() {
    return Jenkins.getActiveInstance().getQueue().getItems().length;
  }

  @JavaScriptMethod
  public int getRunningJobs() {
    List<Job> jobs = Jenkins.getActiveInstance().getAllItems(Job.class);
    int countRunningJobs = 0;
    for (Job job : jobs) {
      if (job.isBuilding()) {
        countRunningJobs++;
      }
    }
    return countRunningJobs;
  }

  @Extension
  public static class DescriptorImpl extends Descriptor<DashboardPortlet> {

    @Override
    public String getDisplayName() {
      return Messages.Dashboard_AgentStatistics();
    }
  }
}
