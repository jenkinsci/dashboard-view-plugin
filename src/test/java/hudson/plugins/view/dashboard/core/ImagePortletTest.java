package hudson.plugins.view.dashboard.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import hudson.plugins.view.dashboard.Dashboard;
import java.util.Arrays;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.Issue;
import org.jvnet.hudson.test.JenkinsRule;

public class ImagePortletTest {
  @Rule public JenkinsRule j = new JenkinsRule();

  @Test
  @Issue("SECURITY-2233")
  public void imagePortletValidation() throws Exception {
    j.createFreeStyleProject("p1");

    Dashboard dashboard = new Dashboard("dash1");
    dashboard.setIncludeRegex(".*");
    j.jenkins.addView(dashboard);

    for (String invalid :
        Arrays.asList("", "<img/src/onerror=alert(\"XSS\")>", "ftp://example.com")) {
      ImagePortlet imagePortlet = new ImagePortlet("bar", invalid);
      dashboard.getBottomPortlets().clear();
      dashboard.getBottomPortlets().add(imagePortlet);
      assertThat(imagePortlet.isUrlValid(), is(false));
      HtmlPage page = j.createWebClient().goTo("view/dash1/");
      assertThat(findError(page), hasSize(1));
      assertThat(findImage(page), is(emptyIterable()));
    }

    for (String valid :
        Arrays.asList("http://example.com/img.png", "//example.com/img.png", "/some/img.png")) {
      ImagePortlet imagePortlet = new ImagePortlet("bar", valid);
      dashboard.getBottomPortlets().clear();
      dashboard.getBottomPortlets().add(imagePortlet);
      assertThat(imagePortlet.isUrlValid(), is(true));
      HtmlPage page = j.createWebClient().goTo("view/dash1/");
      assertThat(findError(page), is(emptyIterable()));
      assertThat(findImage(page), hasSize(1));
    }
  }

  private List<DomNode> findImage(HtmlPage page) {
    return page.getByXPath("//table[@id='portlet-bottomPortlets-0']//img");
  }

  private List<DomNode> findError(HtmlPage page) {
    return page.getByXPath("//div[@class='error']");
  }
}
