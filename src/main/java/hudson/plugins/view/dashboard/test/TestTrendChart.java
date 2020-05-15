package hudson.plugins.view.dashboard.test;

import com.google.common.annotations.VisibleForTesting;
import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.Job;
import hudson.model.Run;
import hudson.plugins.view.dashboard.DashboardLog;
import hudson.plugins.view.dashboard.DashboardPortlet;
import hudson.plugins.view.dashboard.Messages;
import hudson.util.ColorPalette;
import hudson.util.DataSetBuilder;
import hudson.util.EnumConverter;
import hudson.util.Graph;
import hudson.util.ListBoxModel;
import hudson.util.ShiftedCategoryAxis;
import hudson.util.StackedAreaRenderer2;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.StackedAreaRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.ui.RectangleInsets;
import org.joda.time.LocalDate;
import org.joda.time.chrono.GregorianChronology;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.Stapler;

import java.awt.Color;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class TestTrendChart extends DashboardPortlet {

  public enum DisplayStatus {
    ALL("All"),
    SUCCESS("Success"),
    SKIPPED("Skipped"),
    FAILED("Failed");

    private final String description;

    DisplayStatus(String description) {
      this.description = description;
    }

    public String getDescription() {
      return description;
    }

    public String getName() {
      return name();
    }

    static {
      Stapler.CONVERT_UTILS.register(new EnumConverter(), DisplayStatus.class);
    }
  }

  private int graphWidth = 300;
  private int graphHeight = 220;
  private int dateRange = 365;
  private int dateShift = 0;
  private DisplayStatus displayStatus = DisplayStatus.ALL;

  @DataBoundConstructor
  public TestTrendChart(
    String name, int graphWidth, int graphHeight, DisplayStatus displayStatus, int dateRange, int dateShift) {
    super(name);
    this.graphWidth = graphWidth;
    this.graphHeight = graphHeight;
    this.dateRange = dateRange;
    this.dateShift = dateShift;
    this.displayStatus = displayStatus;
    DashboardLog.debug("TestTrendChart", "ctor");
  }

  public int getDateRange() {
    return dateRange;
  }

  public int getDateShift() {
    return dateShift;
  }

  public int getGraphWidth() {
    return graphWidth <= 0 ? 300 : graphWidth;
  }

  public int getGraphHeight() {
    return graphHeight <= 0 ? 220 : graphHeight;
  }

  public DisplayStatus getDisplayStatus() {
    return displayStatus;
  }

  @VisibleForTesting
  Map<LocalDate, TestResultSummary> collectData() {
    // The standard equals doesn't work because two LocalDate objects can
    // be differente even if the date is the same (different internal
    // timestamp)
    Comparator<LocalDate> localDateComparator =
        new Comparator<LocalDate>() {

          @Override
          public int compare(LocalDate d1, LocalDate d2) {
            if (d1.isEqual(d2)) {
              return 0;
            }
            if (d1.isAfter(d2)) {
              return 1;
            }
            return -1;
          }
        };

    // We need a custom comparator for LocalDate objects
    final Map<LocalDate, TestResultSummary> summaries =
        new TreeMap<LocalDate, TestResultSummary>(localDateComparator);
    LocalDate today =
        new LocalDate(
            System.currentTimeMillis() - dateShift * 6000, GregorianChronology.getInstanceUTC());

    // for each job, for each day, add last build of the day to summary
    for (Job job : getDashboard().getJobs()) {
      Run run = job.getFirstBuild();

      if (run != null) { // execute only if job has builds
        LocalDate runDay =
            new LocalDate(
                run.getTimeInMillis() - dateShift * 60000L, GregorianChronology.getInstanceUTC());
        LocalDate firstDay =
            (dateRange != 0)
                ? new LocalDate(
                        System.currentTimeMillis() - dateShift * 6000L,
                        GregorianChronology.getInstanceUTC())
                    .minusDays(dateRange)
                : runDay;

        while (run != null) {
          runDay =
              new LocalDate(
                  run.getTimeInMillis() - dateShift * 60000L, GregorianChronology.getInstanceUTC());
          Run nextRun = run.getNextBuild();

          if (nextRun != null) {
            LocalDate nextRunDay =
                new LocalDate(
                    nextRun.getTimeInMillis() - dateShift * 60000L,
                    GregorianChronology.getInstanceUTC());
            // skip run before firstDay, but keep if next build is
            // after start date
            if (!runDay.isBefore(firstDay)
                || runDay.isBefore(firstDay) && !nextRunDay.isBefore(firstDay)) {
              // if next run is not the same day, use this test to
              // summarize
              if (nextRunDay.isAfter(runDay)) {
                summarize(
                    summaries,
                    run,
                    (runDay.isBefore(firstDay) ? firstDay : runDay),
                    nextRunDay.minusDays(1));
              }
            }
          } else {
            // use this run's test result from last run to today
            summarize(summaries, run, (runDay.isBefore(firstDay) ? firstDay : runDay), today);
          }

          run = nextRun;
        }
      }
    }
    return summaries;
  }

  /** Graph of duration of tests over time. */
  public Graph getSummaryGraph() {
    final CategoryDataset data = buildDataSet(collectData());

    return new Graph(-1, getGraphWidth(), getGraphHeight()) {

      @Override
      protected JFreeChart createGraph() {
        final JFreeChart chart =
            ChartFactory.createStackedAreaChart(
                null, // chart title
                Messages.Dashboard_Date(), // unused
                Messages.Dashboard_Count(), // range axis label
                data, // data
                PlotOrientation.VERTICAL, // orientation
                false, // include legend
                false, // tooltips
                false // urls
                );

        chart.setBackgroundPaint(Color.white);

        final CategoryPlot plot = chart.getCategoryPlot();

        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlinePaint(null);
        plot.setForegroundAlpha(0.8f);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.black);

        CategoryAxis domainAxis = new ShiftedCategoryAxis(null);
        plot.setDomainAxis(domainAxis);
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
        domainAxis.setLowerMargin(0.0);
        domainAxis.setUpperMargin(0.0);
        domainAxis.setCategoryMargin(0.0);

        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        StackedAreaRenderer ar = new StackedAreaRenderer2();
        plot.setRenderer(ar);

        switch (getDisplayStatus()) {
          case SUCCESS:
            ar.setSeriesPaint(0, ColorPalette.BLUE);
            break;
          case SKIPPED:
            ar.setSeriesPaint(0, ColorPalette.YELLOW);
            break;
          case FAILED:
            ar.setSeriesPaint(0, ColorPalette.RED);
            break;
          default:
            ar.setSeriesPaint(0, ColorPalette.RED); // Failures.
            ar.setSeriesPaint(1, ColorPalette.YELLOW); // Skips.
            ar.setSeriesPaint(2, ColorPalette.BLUE); // Total.
        }

        // crop extra space around the graph
        plot.setInsets(new RectangleInsets(0, 0, 0, 5.0));

        return chart;
      }
    };
  }

  private CategoryDataset buildDataSet(Map<LocalDate, TestResultSummary> summaries) {
    DataSetBuilder<String, LocalDateLabel> dsb = new DataSetBuilder<String, LocalDateLabel>();

    for (Map.Entry<LocalDate, TestResultSummary> entry : summaries.entrySet()) {
      LocalDateLabel label = new LocalDateLabel(entry.getKey());

      switch (getDisplayStatus()) {
        case SUCCESS:
          dsb.add(entry.getValue().getSuccess(), Messages.Dashboard_Total(), label);
          break;
        case SKIPPED:
          dsb.add(entry.getValue().getSkipped(), Messages.Dashboard_Skipped(), label);
          break;
        case FAILED:
          dsb.add(entry.getValue().getFailed(), Messages.Dashboard_Failed(), label);
          break;
        default:
          dsb.add(entry.getValue().getSuccess(), Messages.Dashboard_Total(), label);
          dsb.add(entry.getValue().getFailed(), Messages.Dashboard_Failed(), label);
          dsb.add(entry.getValue().getSkipped(), Messages.Dashboard_Skipped(), label);
      }
    }
    return dsb.build();
  }

  private void summarize(
      Map<LocalDate, TestResultSummary> summaries, Run run, LocalDate firstDay, LocalDate lastDay) {
    TestResult testResult = TestUtil.getTestResult(run);

    // for every day between first day and last day inclusive
    for (LocalDate curr = firstDay; curr.compareTo(lastDay) <= 0; curr = curr.plusDays(1)) {
      if (testResult.getTests() != 0) {
        TestResultSummary trs = summaries.get(curr);
        if (trs == null) {
          trs = new TestResultSummary();
          summaries.put(curr, trs);
        }

        trs.addTestResult(testResult);
      }
    }
  }

  @Extension
  public static class DescriptorImpl extends Descriptor<DashboardPortlet> {

    @Override
    public String getDisplayName() {
      return Messages.Dashboard_TestTrendChart();
    }

    public DisplayStatus getDefaultDisplayStatus() {
      return DisplayStatus.ALL;
    }

    /**
     * Fills the jobExecutionMode drop-down menu.
     *
     * @return the jobExecutionMode items
     */
    public ListBoxModel doFillDisplayStatusItems() {
      final ListBoxModel items = new ListBoxModel();
      items.add(DisplayStatus.ALL.getDescription(), DisplayStatus.ALL.getName());
      items.add(DisplayStatus.SUCCESS.getDescription(), DisplayStatus.SUCCESS.getName());
      items.add(DisplayStatus.FAILED.getDescription(), DisplayStatus.FAILED.getName());
      items.add(DisplayStatus.SKIPPED.getDescription(), DisplayStatus.SKIPPED.getName());
      return items;
    }
  }
}
