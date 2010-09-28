package hudson.plugins.view.dashboard.test;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.Job;
import hudson.model.Run;
import hudson.plugins.view.dashboard.DashboardPortlet;
import hudson.util.ColorPalette;
import hudson.util.DataSetBuilder;
import hudson.util.Graph;
import hudson.util.ShiftedCategoryAxis;
import hudson.util.StackedAreaRenderer2;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

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
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.kohsuke.stapler.DataBoundConstructor;

public class TestTrendChart extends DashboardPortlet {

  private int graphWidth = 300;
  private int graphHeight = 220;

	@DataBoundConstructor
	public TestTrendChart(String name, int graphWidth, int graphHeight) {
		super(name);
    this.graphWidth = graphWidth;
    this.graphHeight = graphHeight;
	}

  public int getGraphWidth() {
    return graphWidth <= 0 ? 300 : graphWidth;
  }

  public int getGraphHeight() {
    return graphHeight <= 0 ? 220 : graphHeight;
  }

	/**
	 * Graph of duration of tests over time.
	 */
	public Graph getSummaryGraph() {
    // TODO: ask user to provide first as a lower bound
		// find the first build execution
//		DateTime first = new DateTime();
//
//		for (Job job : getDashboard().getJobs()) {
//			Run firstRun = job.getFirstBuild();
//
//			if (firstRun != null) {
//				DateTime date = new DateTime(firstRun.getTimestamp());
//
//				if (date.isBefore(first)) {
//					first = date;
//				}
//			}
//		}
		
		// hold onto summaries
		final Map<LocalDate, TestResultSummary> summaries = new HashMap<LocalDate, TestResultSummary>();
//		LocalDate firstDay = new LocalDate(first);
		LocalDate today = new LocalDate();
		
		// for each job, for each day, add last build of the day to summary
		for (Job job : getDashboard().getJobs()) {
			Run run = job.getFirstBuild();
      if (run != null) { // execute only if job has builds
        LocalDate lastRunDay = new LocalDate(run.getTimestamp());

        while (run != null) {
          Run nextRun = run.getNextBuild();

          if (nextRun != null) {
            LocalDate runDay = new LocalDate(run.getTimestamp());
            // if next run is the next day, use this test to summarize
            if (new LocalDate(nextRun.getTimestamp()).isAfter(runDay)) {
              summarize(summaries, run, lastRunDay, runDay);
              lastRunDay = runDay.plusDays(1);
            }
          } else {
            // use this run's test result from last run to today
            summarize(summaries, run, lastRunDay, today);
          }

          run = nextRun;
        }
      }
		}
		
		return new Graph(-1, getGraphWidth(), getGraphHeight()) {

			@Override
			protected JFreeChart createGraph() {
				final JFreeChart chart = ChartFactory.createStackedAreaChart(
		            null,                   // chart title
		            "date",                   // unused
		            "count",                  // range axis label
		            buildDataSet(summaries), // data
		            PlotOrientation.VERTICAL, // orientation
		            false,                     // include legend
		            false,                     // tooltips
		            false                     // urls
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
		        ar.setSeriesPaint(0,ColorPalette.RED); // Failures.
		        ar.setSeriesPaint(1,ColorPalette.YELLOW); // Skips.
		        ar.setSeriesPaint(2,ColorPalette.BLUE); // Total.

		        // crop extra space around the graph
		        plot.setInsets(new RectangleInsets(0,0,0,5.0));
		        
				return chart;
			}

		};
	}
	
	private CategoryDataset buildDataSet(Map<LocalDate, TestResultSummary> summaries) {
        DataSetBuilder<String,LocalDate> dsb = new DataSetBuilder<String,LocalDate>();

        for (Map.Entry<LocalDate, TestResultSummary> entry : summaries.entrySet()) {
            dsb.add( entry.getValue().getFailed(), "failed", entry.getKey());
            dsb.add( entry.getValue().getSkipped(), "skipped", entry.getKey());
            dsb.add( entry.getValue().getSuccess(), "total", entry.getKey());
        }
        return dsb.build();
    }
	
	private void summarize(Map<LocalDate, TestResultSummary> summaries,
			Run run, LocalDate firstDay, LocalDate lastDay) {
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
			return "Test Trend Chart";
		}
	}
}
