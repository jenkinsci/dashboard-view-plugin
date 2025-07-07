package hudson.plugins.view.dashboard.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import hudson.Launcher;
import hudson.matrix.MatrixBuild;
import hudson.matrix.MatrixProject;
import hudson.matrix.TextAxis;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.tasks.junit.JUnitResultArchiver;
import java.io.IOException;
import java.util.Collections;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.TestBuilder;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

@WithJenkins
class TestSummaryForMatrixJobs {

    private JenkinsRule j;

    private MatrixProject matrixProject;

    @BeforeEach
    void setUp(JenkinsRule rule) throws Exception {
        j = rule;

        matrixProject = j.jenkins.createProject(MatrixProject.class, "top");
        matrixProject.getAxes().add(new TextAxis("param", "one", "two"));
        matrixProject.getBuildersList().add(new TestBuilder() {
            @Override
            public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener)
                    throws InterruptedException, IOException {
                build.getWorkspace()
                        .child("testresult.xml")
                        .copyFrom(TestSummaryForMatrixJobs.class.getResource(
                                "/hudson/plugins/view/dashboard/test_failure.xml"));
                return true;
            }
        });
        matrixProject.getPublishersList().add(new JUnitResultArchiver("testresult.xml"));
    }

    @Test
    void summaryIncludesMatrixJobs() throws Exception {
        MatrixBuild result = matrixProject.scheduleBuild2(0).get();
        j.assertBuildStatus(Result.UNSTABLE, result);

        TestResultSummary testSummary = TestUtil.getTestResultSummary(Collections.singleton(matrixProject), false);
        assertThat(testSummary.getFailed(), is((2)));
        assertThat(testSummary.getSuccess(), is((2)));
    }

    @AfterEach
    void cleanupMatrixJob() throws Exception {
        matrixProject.delete();
    }
}
