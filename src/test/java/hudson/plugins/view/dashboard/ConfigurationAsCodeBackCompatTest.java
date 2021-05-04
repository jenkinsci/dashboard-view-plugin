package hudson.plugins.view.dashboard;

import hudson.plugins.view.dashboard.core.ImagePortlet;
import io.jenkins.plugins.casc.ConfigurationAsCode;
import io.jenkins.plugins.casc.ConfigurationContext;
import io.jenkins.plugins.casc.ConfiguratorRegistry;
import io.jenkins.plugins.casc.ObsoleteConfigurationMonitor;
import io.jenkins.plugins.casc.misc.ConfiguredWithCode;
import io.jenkins.plugins.casc.misc.JenkinsConfiguredWithCodeRule;
import io.jenkins.plugins.casc.misc.Util;
import io.jenkins.plugins.casc.model.CNode;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.Issue;
import org.jvnet.hudson.test.JenkinsRule;

import java.util.List;

import static io.jenkins.plugins.casc.misc.Util.getJenkinsRoot;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.AllOf.allOf;

public class ConfigurationAsCodeBackCompatTest {

  @Rule
  public JenkinsConfiguredWithCodeRule j = new JenkinsConfiguredWithCodeRule();


  @Test
  @Issue("SECURITY-2233")
  @ConfiguredWithCode("casc_image_backcompat_import.yml")
  public void testOldImagePortletUrl() throws Exception {
    Dashboard.DescriptorImpl descriptor =
      j.jenkins.getDescriptorByType(Dashboard.DescriptorImpl.class);
    assertThat(descriptor, notNullValue());

    Dashboard dashboard = (Dashboard) j.jenkins.getView("test");
    assertThat(dashboard.getViewName(), is("test"));
    List<DashboardPortlet> leftPortlets = dashboard.getLeftPortlets();
    assertThat(leftPortlets.get(0), allOf(
      instanceOf(ImagePortlet.class),
      hasProperty("name", equalTo("Image")),
      hasProperty("imageUrl", equalTo("test-backcompat"))
    ));

    final List<ObsoleteConfigurationMonitor.Error> errors = ObsoleteConfigurationMonitor.get().getErrors();
    assertThat(errors, containsInAnyOrder(new ErrorMatcher(containsString("'url' is deprecated"))));

    final ConfiguratorRegistry registry = ConfiguratorRegistry.get();
    final ConfigurationContext context = new ConfigurationContext(registry);
    CNode node = getJenkinsRoot(context).get("views").asSequence().get(0).asMapping();

    final String exported = Util.toYamlString(node);
    final String expected = Util.toStringFromYamlFile(this, "casc_image_backcompat_export.yml");

    assertThat(exported, is(expected));
  }

  static class ErrorMatcher extends TypeSafeDiagnosingMatcher<ObsoleteConfigurationMonitor.Error> {
    final Matcher<String> messageMatcher;

    public ErrorMatcher(final Matcher<String> messageMatcher) {
      this.messageMatcher = messageMatcher;
    }

    @Override
    protected boolean matchesSafely(final ObsoleteConfigurationMonitor.Error item, final Description mismatchDescription) {
      return messageMatcher.matches(item.message);
    }

    @Override
    public void describeTo(final Description description) {
      description.appendDescriptionOf(messageMatcher);
    }
  }
}
