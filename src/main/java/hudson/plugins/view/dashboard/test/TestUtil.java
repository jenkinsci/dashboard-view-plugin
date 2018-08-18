package hudson.plugins.view.dashboard.test;

import hudson.matrix.MatrixConfiguration;
import hudson.matrix.MatrixProject;
import hudson.model.Job;
import hudson.model.Run;
import hudson.model.TopLevelItem;
import hudson.tasks.test.AbstractTestResultAction;
import hudson.tasks.test.TestResultProjectAction;
import java.util.Collection;

public class TestUtil {

  /**
   * Summarize the last test results from the passed set of jobs. If a job doesn't include any
   * tests, add a 0 summary.
   *
   * @param jobs
   * @return
   */
  public static TestResultSummary getTestResultSummary(
      Collection<TopLevelItem> jobs, boolean hideZeroTestProjects) {
    TestResultSummary summary = new TestResultSummary();

    for (TopLevelItem item : jobs) {
      if (item instanceof MatrixProject) {
        MatrixProject mp = (MatrixProject) item;

        for (MatrixConfiguration configuration : mp.getActiveConfigurations()) {
          MatrixConfiguration job = mp.getItem(configuration.getCombination());
          summarizeJob(job, summary, hideZeroTestProjects);
        }
      } else if (item instanceof Job) {
        Job job = (Job) item;
        summarizeJob(job, summary, hideZeroTestProjects);
      }
    }

    return summary;
  }

  private static void summarizeJob(
      Job job, TestResultSummary summary, boolean hideZeroTestProjects) {
    boolean addBlank = true;
    TestResultProjectAction testResults = job.getAction(TestResultProjectAction.class);

    if (testResults != null) {
      AbstractTestResultAction tra = testResults.getLastTestResultAction();

      if (tra != null) {
        addBlank = false;
        if (tra.getTotalCount() > 0 || !hideZeroTestProjects) {
          summary.addTestResult(
              new TestResult(job, tra.getTotalCount(), tra.getFailCount(), tra.getSkipCount()));
        }
      }
    }

    if (addBlank && !hideZeroTestProjects) {
      summary.addTestResult(new TestResult(job, 0, 0, 0));
    }
  }

  public static TestResult getTestResult(Run run) {
    AbstractTestResultAction tra = run.getAction(AbstractTestResultAction.class);
    if (tra != null) {
      return new TestResult(
          run.getParent(), tra.getTotalCount(), tra.getFailCount(), tra.getSkipCount());
    }

    return new TestResult(run.getParent(), 0, 0, 0);
  }
}
