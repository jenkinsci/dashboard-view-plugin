package hudson.plugins.view.dashboard.stats;

import hudson.Extension;
import hudson.model.Computer;
import hudson.model.Descriptor;
import hudson.model.Job;
import hudson.plugins.view.dashboard.DashboardPortlet;
import hudson.plugins.view.dashboard.Messages;
import hudson.security.ACL;
import hudson.security.ACLContext;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.bind.JavaScriptMethod;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;

/** @author Lucie Votypkova */
public class StatSlaves extends DashboardPortlet {

  @DataBoundConstructor
  public StatSlaves(String name) {
    super(name);
  }

  @ExportedBean
  public static final class AgentStats {
    @Exported public final int agents;
    @Exported public int onlineAgents = 0;
    @Exported public int offlineAgents = 0;
    @Exported public int disconnectedAgents = 0;
    @Exported public final int tasksInQueue;
    @Exported public final int runningJobs;

    AgentStats(Jenkins j) {
      agents = j.getNodes().size();
      countAgents(j.getComputers());
      tasksInQueue = j.getQueue().getItems().length;
      runningJobs = countRunningJobs(j);
    }

    private void countAgents(Computer[] computers) {
      for (Computer computer : computers) {
        if (computer.isOnline()) {
          onlineAgents++;
        } else if (computer.getConnectTime() != 0) {
          offlineAgents++;
        } else {
          disconnectedAgents++;
        }
      }
      onlineAgents--;
    }

    private int countRunningJobs(Jenkins j) {
      // TODO: Might be a faster way to get this info from the computers
      int countRunningJobs = 0;
      // We don't really care about security here as all this returns is a count
      try (ACLContext ctx = ACL.as(ACL.SYSTEM)) {
        for (Job job : j.allItems(Job.class)) {
          if (job.isBuilding()) {
            countRunningJobs++;
          }
        }
      }
      return countRunningJobs;
    }
  }

  @JavaScriptMethod
  public AgentStats getStats() {
    return new AgentStats(Jenkins.get());
  }

  @Extension
  public static class DescriptorImpl extends Descriptor<DashboardPortlet> {

    @Override
    public String getDisplayName() {
      return Messages.Dashboard_AgentStatistics();
    }
  }
}
