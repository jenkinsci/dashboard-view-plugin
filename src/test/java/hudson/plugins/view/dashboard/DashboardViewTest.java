/* SPDX-License-Identifier: MIT
 * Copyright (c) 2020, Tobias Gruetzmacher
 */
package hudson.plugins.view.dashboard;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import hudson.model.FreeStyleProject;
import java.io.IOException;
import java.util.function.Predicate;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.MockFolder;
import org.xml.sax.SAXException;

public class DashboardViewTest {

  @ClassRule public static JenkinsRule j = new JenkinsRule();

  static FreeStyleProject p1;
  static FreeStyleProject p2;

  @BeforeClass
  public static void prepareJobs() throws Exception {
    p1 = j.createFreeStyleProject("p1");
    j.assertBuildStatusSuccess(p1.scheduleBuild2(0));
    MockFolder f = j.createFolder("f1");
    p2 = f.createProject(FreeStyleProject.class, "p2");
    j.assertBuildStatusSuccess(p2.scheduleBuild2(0));

    for (int i = 0; i < 2; i++) {
      Dashboard dashboard = new Dashboard("foo" + i);
      dashboard.setIncludeRegex(".*");
      dashboard.setRecurse(true);
      dashboard.setIncludeStdJobList(true);
      if (i % 2 == 0) {
        j.jenkins.addView(dashboard);
      } else {
        f.addView(dashboard);
      }
    }
  }

  @Test
  public void verifyDisplayNameInFolders() throws IOException, SAXException, InterruptedException {
    HtmlPage page = j.createWebClient().goTo("view/foo0/");
    HtmlAnchor p1Link = findLink(page, a -> a.getHrefAttribute().equals("job/p1/"));
    assertThat(p1Link.getTextContent(), is(equalTo("p1")));
    HtmlAnchor p2Link = findLink(page, a -> a.getHrefAttribute().endsWith("/job/p2/"));
    assertThat(p2Link.getTextContent(), is(equalTo(p2.getFullDisplayName())));

    page = j.createWebClient().goTo("job/f1/view/foo1/");
    p2Link = findLink(page, a -> a.getHrefAttribute().equals("job/p2/"));
    assertThat(p2Link.getTextContent(), is(equalTo("p2")));
  }

  private static HtmlAnchor findLink(HtmlPage page, Predicate<HtmlAnchor> condition) {
    return page.getAnchors().stream()
        .filter(condition)
        .findAny()
        .orElseThrow(IllegalStateException::new);
  }
}
