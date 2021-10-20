package hudson.plugins.view.dashboard.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import hudson.maven.MavenModuleSet;
import hudson.model.Result;
import hudson.plugins.view.dashboard.Dashboard;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.ExtractResourceSCM;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.ToolInstallations;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;

public class TestTrendChartTest {

  @Rule public JenkinsRule j = new JenkinsRule();

  @Rule public MockitoRule mockito = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);

  @Mock private Dashboard dashboard;

  // TODO: It would be nice to actually have some "history", but that would involve faking builds on
  // different days

  @Test
  public void summaryIncludesMavenJob() throws Exception {
    ToolInstallations.configureMaven35();

    MavenModuleSet project = j.jenkins.createProject(MavenModuleSet.class, "maven");
    project.setGoals("test");
    project.setScm(new ExtractResourceSCM(getClass().getResource("maven-unit-failure.zip")));
    j.assertBuildStatus(Result.UNSTABLE, project.scheduleBuild2(0).get());
    j.assertBuildStatus(Result.UNSTABLE, project.scheduleBuild2(0).get());

    when(dashboard.getJobs()).thenReturn(Collections.singletonList(project));
    TestTrendChart chart =
        new TestTrendChart("tests", 320, 240, TestTrendChart.DisplayStatus.ALL, 30, 0);
    chart = spy(chart);
    doReturn(dashboard).when(chart).getDashboard();

    Map<LocalDate, TestResultSummary> chartData = chart.collectData();
    Collection<TestResultSummary> values = chartData.values();
    assertThat(values, Matchers.hasSize(1));
    TestResultSummary today = values.iterator().next();
    assertThat(today.getFailed(), is(equalTo(2)));
    assertThat(today.getSuccess(), is(equalTo(2)));
  }
}
