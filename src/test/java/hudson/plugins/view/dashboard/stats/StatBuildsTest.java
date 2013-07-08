package hudson.plugins.view.dashboard.stats;

import hudson.model.BallColor;
import hudson.model.FreeStyleProject;
import hudson.model.TopLevelItem;
import hudson.plugins.view.dashboard.RunLoadCounter;
import java.util.Collections;
import java.util.concurrent.Callable;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.jvnet.hudson.test.JenkinsRule;

public class StatBuildsTest {

    @Rule public JenkinsRule j = new JenkinsRule();

    @Test public void avoidEagerLoading() throws Exception {
        final FreeStyleProject p = j.createFreeStyleProject();
        RunLoadCounter.prepare(p);
        for (int i = 0; i < 15; i++) {
            j.assertBuildStatusSuccess(p.scheduleBuild2(0));
        }
        final StatBuilds stats = new StatBuilds("-");
        assertEquals(StatBuilds.MAX_BUILDS, RunLoadCounter.assertMaxLoads(p, StatBuilds.MAX_BUILDS + /* AbstractLazyLoadRunMap.headMap actually loads start, alas */1, new Callable<Integer>() {
            public Integer call() throws Exception {
                return stats.getBuildStat(Collections.<TopLevelItem>singletonList(p)).get(BallColor.BLUE);
            }
        }).intValue());
    }

}
