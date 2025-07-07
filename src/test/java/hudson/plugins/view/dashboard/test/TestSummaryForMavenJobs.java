package hudson.plugins.view.dashboard.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import hudson.maven.MavenModuleSet;
import hudson.maven.MavenModuleSetBuild;
import hudson.model.Result;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.ExtractResourceSCM;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.ToolInstallations;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

@WithJenkins
class TestSummaryForMavenJobs {

    private JenkinsRule j;

    @BeforeEach
    void setUp(JenkinsRule rule) {
        j = rule;
    }

    @Test
    void summaryIncludesMavenJob() throws Exception {
        ToolInstallations.configureMaven35();

        MavenModuleSet project = j.jenkins.createProject(MavenModuleSet.class, "maven");
        project.setGoals("test");
        project.setScm(new ExtractResourceSCM(getClass().getResource("maven-unit-failure.zip")));
        MavenModuleSetBuild build =
                j.assertBuildStatus(Result.UNSTABLE, project.scheduleBuild2(0).get());

        TestResultSummary testSummary = TestUtil.getTestResultSummary(Collections.singleton(project), false);
        assertThat(testSummary.getFailed(), is(2));
        assertThat(testSummary.getSuccess(), is(2));
    }
}
