package hudson.plugins.view.dashboard.builds;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;

import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.model.Job;
import hudson.plugins.view.dashboard.Dashboard;
import java.util.Collections;
import java.util.List;
import org.htmlunit.html.HtmlAnchor;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.Issue;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.RunLoadCounter;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

@WithJenkins
class LatestBuildsTest {

    private static JenkinsRule j;

    private static FreeStyleProject p;

    @BeforeAll
    static void setUp(JenkinsRule rule) throws Exception {
        j = rule;

        p = j.createFreeStyleProject();
        for (int i = 0; i < 5; i++) {
            j.assertBuildStatusSuccess(p.scheduleBuild2(0));
        }
    }

    @Test
    void testAvoidEagerLoading() throws Exception {
        RunLoadCounter.prepare(p);

        int numbuilds = 3;
        final LatestBuilds latest = new LatestBuilds("-", numbuilds) {

            @Override
            protected List<Job> getDashboardJobs() {
                return Collections.singletonList(p);
            }
        };

        RunLoadCounter.assertMaxLoads(p, numbuilds, latest::getFinishedBuilds);
    }

    @Test
    @Issue("SECURITY-1489")
    void testTooltipIsEscaped() throws Exception {
        FreeStyleBuild lastBuild = p.getLastBuild();
        lastBuild.setDescription("<i/onmouseover=confirm(1)>test");
        Dashboard dashboard = new Dashboard("foo");
        dashboard.setIncludeRegex(".*");
        dashboard.getLeftPortlets().add(new LatestBuilds("foo", 10));
        j.jenkins.addView(dashboard);
        HtmlPage page = j.createWebClient().goTo("view/foo/");
        HtmlAnchor link = page.getAnchors().stream()
                .filter(a -> a.getHrefAttribute().endsWith("/" + lastBuild.number))
                .findAny()
                .orElseThrow(IllegalStateException::new);
        String tooltip = link.getAttribute("tooltip");
        // The default formatter just escapes all HTML
        assertThat(tooltip, not(containsString("<")));
        assertThat(tooltip, startsWith("&lt;"));
    }
}
