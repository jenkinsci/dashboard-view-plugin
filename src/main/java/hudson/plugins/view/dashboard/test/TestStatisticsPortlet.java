package hudson.plugins.view.dashboard.test;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.TopLevelItem;
import hudson.plugins.view.dashboard.DashboardPortlet;
import hudson.plugins.view.dashboard.Messages;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

/**
 * Portlet that presents a grid of test result data with summation
 *
 * @author Peter Hayes
 */
public class TestStatisticsPortlet extends DashboardPortlet {

    /**
     * Legacy stored colors were a bare hex triplet/quad without a leading
     * {@code #} (e.g. {@code 71E66D}). Anything else, such as a CSS color
     * keyword, an already-prefixed hex value, or a Jenkins theme variable
     * (e.g. {@code var(--success-color)}), is used verbatim so the portlet
     * can adopt the administrator's preferred theme.
     */
    private static final Pattern LEGACY_HEX_COLOR = Pattern.compile("[0-9a-fA-F]{3,4}|[0-9a-fA-F]{6}|[0-9a-fA-F]{8}");

    private boolean useBackgroundColors;
    private String skippedColor;
    private String successColor;
    private String failureColor;
    private final boolean hideZeroTestProjects;
    private boolean useAlternatePercentagesOnLimits;

    @DataBoundConstructor
    public TestStatisticsPortlet(
            String name,
            final boolean hideZeroTestProjects,
            String successColor,
            String failureColor,
            String skippedColor,
            boolean useBackgroundColors) {
        super(name);
        this.successColor = successColor;
        this.failureColor = failureColor;
        this.skippedColor = skippedColor;
        this.useBackgroundColors = useBackgroundColors;
        this.hideZeroTestProjects = hideZeroTestProjects;
    }

    public TestResultSummary getTestResultSummary(Collection<TopLevelItem> jobs) {
        return TestUtil.getTestResultSummary(jobs, hideZeroTestProjects);
    }

    public boolean getHideZeroTestProjects() {
        return this.hideZeroTestProjects;
    }

    public String format(DecimalFormat df, double val) {
        if (val < 1d && val > .99d) {
            return useAlternatePercentagesOnLimits ? ">99%" : "<100%";
        }
        if (val > 0d && val < .01d) {
            return useAlternatePercentagesOnLimits ? "<1%" : ">0%";
        }
        return df.format(val);
    }

    public boolean isUseBackgroundColors() {
        return useBackgroundColors;
    }

    public String getSuccessColor() {
        return successColor;
    }

    public String getFailureColor() {
        return failureColor;
    }

    public String getSkippedColor() {
        return skippedColor;
    }

    /**
     * The success color resolved to a value that is safe to use in a CSS
     * {@code background} declaration, falling back to the theme's success
     * color when none is configured.
     */
    public String getSuccessColorCss() {
        return toCssColor(successColor, "var(--success-color)");
    }

    /**
     * The failure color resolved to a value that is safe to use in a CSS
     * {@code background} declaration, falling back to the theme's error
     * color when none is configured.
     */
    public String getFailureColorCss() {
        return toCssColor(failureColor, "var(--error-color)");
    }

    /**
     * The skipped color resolved to a value that is safe to use in a CSS
     * {@code background} declaration, falling back to the theme's warning
     * color when none is configured. Jenkins core does not define a
     * dedicated "skipped" theme color, so this reuses the same {@code
     * var(--warning-color)} used elsewhere in Jenkins for an unstable/skipped
     * state.
     */
    public String getSkippedColorCss() {
        return toCssColor(skippedColor, "var(--warning-color)");
    }

    /**
     * Resolves a color as configured by the administrator into a value that
     * can be used directly in a CSS {@code background} declaration.
     *
     * <p>Historically this field only ever held a bare hex triplet/quad
     * (e.g. {@code 71E66D}) which the view prefixed with {@code #}. To let
     * administrators pick a color that automatically adapts to their
     * preferred Jenkins theme (light/dark/etc.), any other value - such as
     * {@code var(--success-color)}, a CSS color keyword, or an already
     * {@code #}-prefixed hex code - is passed through unchanged.
     *
     * @param color the color as configured, may be {@code null} or blank
     * @param themeDefault the theme-aware value to fall back to when {@code
     *     color} is not set
     */
    static String toCssColor(String color, String themeDefault) {
        if (color == null || color.isBlank()) {
            return themeDefault;
        }
        if (LEGACY_HEX_COLOR.matcher(color).matches()) {
            return "#" + color;
        }
        return color;
    }

    @DataBoundSetter
    public void setUseAlternatePercentagesOnLimits(boolean useAlternatePercentagesOnLimits) {
        this.useAlternatePercentagesOnLimits = useAlternatePercentagesOnLimits;
    }

    public boolean isUseAlternatePercentagesOnLimits() {
        return useAlternatePercentagesOnLimits;
    }

    public String getRowClass(TestResult testResult) {
        if (testResult.failed > 0) {
            return "portlet-failure";
        } else if (testResult.skipped > 0) {
            return "portlet-skipped";
        } else {
            return "portlet-success";
        }
    }

    public String getTotalRowClass(List<TestResult> testResults) {
        for (TestResult testResult : testResults) {
            if (testResult.failed > 0) {
                return "portlet-failure";
            } else if (testResult.skipped > 0) {
                return "portlet-skipped";
            }
        }
        return "portlet-success";
    }

    public void setUseBackgroundColors(boolean useBackgroundColors) {
        this.useBackgroundColors = useBackgroundColors;
    }

    public void setSkippedColor(String skippedColor) {
        this.skippedColor = skippedColor;
    }

    public void setSuccessColor(String successColor) {
        this.successColor = successColor;
    }

    public void setFailureColor(String failureColor) {
        this.failureColor = failureColor;
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<DashboardPortlet> {

        @Override
        public String getDisplayName() {
            return Messages.Dashboard_TestStatisticsGrid();
        }
    }
}
