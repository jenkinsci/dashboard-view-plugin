package hudson.plugins.view.dashboard.stats;

import static org.junit.jupiter.api.Assertions.assertEquals;

import hudson.model.BallColor;
import hudson.model.FreeStyleProject;
import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.RunLoadCounter;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

@WithJenkins
class StatBuildsTest {

    private JenkinsRule j;

    @BeforeEach
    void setUp(JenkinsRule rule) {
        j = rule;
    }

    @Test
    void avoidEagerLoading() throws Exception {
        final FreeStyleProject p = j.createFreeStyleProject();
        RunLoadCounter.prepare(p);
        for (int i = 0; i < 25; i++) {
            j.assertBuildStatusSuccess(p.scheduleBuild2(0));
        }
        final StatBuilds stats = new StatBuilds("-");
        assertEquals(
                StatBuilds.MAX_BUILDS,
                RunLoadCounter.assertMaxLoads(
                                p,
                                StatBuilds.MAX_BUILDS + /* margin for AbstractLazyLoadRunMap.headMap */ 2,
                                () -> stats.getBuildStat(Collections.singletonList(p))
                                        .get(BallColor.BLUE))
                        .intValue());
    }

    @Test
    void testGettingBuildStatsWithZeroBuild() throws Exception {
        final FreeStyleProject project = j.createFreeStyleProject();
        RunLoadCounter.prepare(project);
        final StatBuilds stats = new StatBuilds("-");
        final Map<BallColor, Integer> buildStats = stats.getBuildStat(Collections.singletonList(project));
        for (BallColor color : BallColor.values()) {
            assertEquals((Integer) 0, buildStats.get(color.noAnime()));
        }
    }
}
