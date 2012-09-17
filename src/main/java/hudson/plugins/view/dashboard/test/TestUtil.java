package hudson.plugins.view.dashboard.test;

import hudson.maven.reporters.SurefireAggregatedReport;
import hudson.model.Job;
import hudson.model.Run;
import hudson.model.TopLevelItem;
import hudson.tasks.junit.TestResultAction;
import hudson.tasks.test.AbstractTestResultAction;
import hudson.tasks.test.TestResultProjectAction;

import java.util.Collection;

public class TestUtil {

   /**
    * Summarize the last test results from the passed set of jobs.  If a job
    * doesn't include any tests, add a 0 summary.
    * 
    * @param jobs
    * @return
    */
   public static TestResultSummary getTestResultSummary(Collection<TopLevelItem> jobs) {
      TestResultSummary summary = new TestResultSummary();

       for (TopLevelItem item : jobs) {
           if (item instanceof Job) {
                Job job = (Job) item;
                boolean addBlank = true;
                TestResultProjectAction testResults = job.getAction(TestResultProjectAction.class);

                if (testResults != null) {
                    AbstractTestResultAction tra = testResults.getLastTestResultAction();

                    if (tra != null) {
                       addBlank = false;
                       summary.addTestResult(new TestResult(job, tra.getTotalCount(), tra.getFailCount(), tra.getSkipCount()));
                    }
                } else {
                    SurefireAggregatedReport surefireTestResults = job.getAction(SurefireAggregatedReport.class);
                    if (surefireTestResults != null) {
                       addBlank = false;
                       summary.addTestResult(new TestResult(job, surefireTestResults.getTotalCount(), surefireTestResults.getFailCount(), surefireTestResults.getSkipCount()));
                    }
                }

                if (addBlank) {
                    summary.addTestResult(new TestResult(job, 0, 0, 0));
                }
           }
      }

      return summary;
   }

   public static TestResult getTestResult(Run run) {
      TestResultAction tra = run.getAction(TestResultAction.class);
      if (tra != null) {
         return new TestResult(run.getParent(), tra.getTotalCount(), tra.getFailCount(), tra.getSkipCount());
      } 
      
      SurefireAggregatedReport surefireTestResults = run.getAction(SurefireAggregatedReport.class);
      if (surefireTestResults != null) {
         return new TestResult(run.getParent(), surefireTestResults.getTotalCount(), surefireTestResults.getFailCount(), surefireTestResults.getSkipCount());
      }
      
      return new TestResult(run.getParent(), 0, 0, 0);
   }
}
