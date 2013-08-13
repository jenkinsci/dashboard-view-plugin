package hudson.plugins.view.dashboard.builds;

import hudson.model.FreeStyleProject;
import hudson.model.Job;
import hudson.model.Run;
import hudson.plugins.view.dashboard.RunLoadCounter;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.junit.Test;
import org.junit.Rule;
import org.jvnet.hudson.test.JenkinsRule;

public class LatestBuildsTest {

    @Rule
    public JenkinsRule j = new JenkinsRule();

    @Test
    public void testAvoidEagerLoading() throws Exception {
        final FreeStyleProject p = j.createFreeStyleProject();
        RunLoadCounter.prepare(p);
        for (int i = 0; i < 5; i++) {
            j.assertBuildStatusSuccess(p.scheduleBuild2(0));
        }

        int numbuilds = 3;
        final LatestBuilds latest = new LatestBuilds("-", numbuilds) {

            @Override
            protected List<Job> getDashboardJobs() {
                return Collections.singletonList((Job) p);
            }

        };

        RunLoadCounter.assertMaxLoads(p, numbuilds, new Callable<List<Run>>() {

            public List<Run> call() throws Exception {
                return latest.getFinishedBuilds();
            }
        });
    }

}