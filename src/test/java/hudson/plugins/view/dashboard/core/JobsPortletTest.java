package hudson.plugins.view.dashboard.core;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import hudson.model.FreeStyleProject;
import hudson.model.Job;
import hudson.plugins.view.dashboard.Dashboard;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.RunLoadCounter;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class JobsPortletTest {

  @Rule public JenkinsRule j = new JenkinsRule();

  @Test
  public void checkFillColumnFirstOption() throws IOException, SAXException {

    Dashboard dashboard = new Dashboard("foo");
    dashboard.setIncludeRegex(".*");
    dashboard.getLeftPortlets().add(new JobsPortlet("jobs list", 3, true));
    j.jenkins.addView(dashboard);

    // check config
    HtmlPage configPage = j.createWebClient().goTo("view/foo/configure");
    DomElement inputForm =
      configPage.getElementsByName("portlet.fillColumnFirst").stream()
        .filter(e -> e.getAttribute("type").equals("checkbox"))
        .findFirst()
      .orElseThrow(IllegalStateException::new);

    String checked = inputForm.getAttribute("checked");
    assertEquals(checked, "true");

    // check count when job count is one
    final FreeStyleProject p1 = j.createFreeStyleProject("test01");
    RunLoadCounter.prepare(p1);
    HtmlPage viewPage = j.createWebClient().goTo("view/foo");

    DomElement jobsGridTable = viewPage.getElementById("portlet-leftPortlets-0");
    assertEquals(1, jobsGridTable
      .getFirstElementChild() // tbody
      .getChildElementCount()); // tr count


    // check count when multi-jobs
    final FreeStyleProject p2 = j.createFreeStyleProject("test02");
    final FreeStyleProject p3 = j.createFreeStyleProject("test03");
    final FreeStyleProject p4 = j.createFreeStyleProject("test04");
    RunLoadCounter.prepare(p2);
    RunLoadCounter.prepare(p3);
    RunLoadCounter.prepare(p4);
    HtmlPage viewPage2 = j.createWebClient().goTo("view/foo");

    DomElement jobsGridTable2 = viewPage2.getElementById("portlet-leftPortlets-0");
    assertEquals(2, jobsGridTable2
      .getFirstElementChild() // tbody
      .getChildElementCount()); // tr count

    HtmlAnchor jobIconAnchor = (HtmlAnchor) jobsGridTable2.getFirstElementChild() // tbody
      .getFirstElementChild().getNextElementSibling() // 2'tr
      .getFirstElementChild() // td
      .getFirstElementChild(); // a icon

    assertEquals("job/test02/build?delay=0sec", jobIconAnchor.getHrefAttribute());
  }

}
