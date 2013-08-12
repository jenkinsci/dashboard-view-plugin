package hudson.plugins.view.dashboard.builds;

import hudson.model.FreeStyleProject;
import hudson.model.Job;
import hudson.model.Run;
import hudson.plugins.view.dashboard.RunLoadCounter;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import org.junit.Rule;
import org.jvnet.hudson.test.JenkinsRule;
import static org.junit.Assert.assertTrue;

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

        int actual = RunLoadCounter.countLoads(p, new Runnable() {
            public void run() {
                List<Run> builds = latest.getFinishedBuilds();
            }
        });
        assertTrue(actual <= numbuilds);

    }

}