package hudson.plugins.view.dashboard;

import static io.jenkins.plugins.casc.misc.Util.getJenkinsRoot;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import hudson.plugins.view.dashboard.builds.LatestBuilds;
import hudson.plugins.view.dashboard.core.HudsonStdJobsPortlet;
import hudson.plugins.view.dashboard.core.IframePortlet;
import hudson.plugins.view.dashboard.core.ImagePortlet;
import hudson.plugins.view.dashboard.core.JobsPortlet;
import hudson.plugins.view.dashboard.core.UnstableJobsPortlet;
import hudson.plugins.view.dashboard.stats.StatBuilds;
import hudson.plugins.view.dashboard.stats.StatJobs;
import hudson.plugins.view.dashboard.stats.StatSlaves;
import hudson.plugins.view.dashboard.test.TestStatisticsChart;
import hudson.plugins.view.dashboard.test.TestStatisticsPortlet;
import hudson.plugins.view.dashboard.test.TestTrendChart;
import io.jenkins.plugins.casc.ConfigurationContext;
import io.jenkins.plugins.casc.ConfiguratorRegistry;
import io.jenkins.plugins.casc.misc.ConfiguredWithCode;
import io.jenkins.plugins.casc.misc.JenkinsConfiguredWithCodeRule;
import io.jenkins.plugins.casc.misc.Util;
import io.jenkins.plugins.casc.model.CNode;
import java.util.List;
import org.junit.ClassRule;
import org.junit.Test;

public class ConfigurationAsCodeTest {

  @ClassRule
  @ConfiguredWithCode("casc.yml")
  public static JenkinsConfiguredWithCodeRule rule = new JenkinsConfiguredWithCodeRule();

  @Test
  public void testImportConfiguration() {
    Dashboard.DescriptorImpl descriptor =
        rule.jenkins.getDescriptorByType(Dashboard.DescriptorImpl.class);
    assertThat(descriptor, notNullValue());

    Dashboard dashboard = (Dashboard) rule.jenkins.getView("test");
    assertThat(dashboard.getViewName(), is("test"));

    assertThat(dashboard.isRecurse(), is(true));
    assertThat(dashboard.isUseCssStyle(), is(true));
    assertThat(dashboard.isIncludeStdJobList(), is(true));
    assertThat(dashboard.isHideJenkinsPanels(), is(true));
    assertThat(dashboard.getIncludeRegex(), is(".*"));
    assertThat(dashboard.getLeftPortletWidth(), is("50%"));
    assertThat(dashboard.getRightPortletWidth(), is("50%"));

    List<DashboardPortlet> topPortlets = dashboard.getTopPortlets();
    List<DashboardPortlet> leftPortlets = dashboard.getLeftPortlets();
    List<DashboardPortlet> rightPortlets = dashboard.getRightPortlets();
    List<DashboardPortlet> bottomPortlets = dashboard.getBottomPortlets();

    assertThat(topPortlets.size(), is(3));
    assertThat(leftPortlets.size(), is(2));
    assertThat(rightPortlets.size(), is(4));
    assertThat(bottomPortlets.size(), is(3));

    assertThat(topPortlets.get(0), instanceOf(StatSlaves.class));
    assertThat(topPortlets.get(1), instanceOf(StatBuilds.class));
    assertThat(topPortlets.get(2), instanceOf(LatestBuilds.class));

    assertThat(leftPortlets.get(0), instanceOf(IframePortlet.class));
    assertThat(leftPortlets.get(1), instanceOf(ImagePortlet.class));

    assertThat(rightPortlets.get(0), instanceOf(HudsonStdJobsPortlet.class));
    assertThat(rightPortlets.get(1), instanceOf(StatJobs.class));
    assertThat(rightPortlets.get(2), instanceOf(JobsPortlet.class));
    assertThat(rightPortlets.get(3), instanceOf(UnstableJobsPortlet.class));

    assertThat(bottomPortlets.get(0), instanceOf(TestStatisticsChart.class));
    assertThat(bottomPortlets.get(1), instanceOf(TestStatisticsPortlet.class));
    assertThat(bottomPortlets.get(2), instanceOf(TestTrendChart.class));
  }

  @Test
  public void testExportConfiguration() throws Exception {
    final ConfiguratorRegistry registry = ConfiguratorRegistry.get();
    final ConfigurationContext context = new ConfigurationContext(registry);
    CNode node = getJenkinsRoot(context).get("views").asSequence().get(0).asMapping();

    final String exported = Util.toYamlString(node);
    final String expected = Util.toStringFromYamlFile(this, "casc-export.yml");

    assertThat(exported, is(expected));
  }
}
