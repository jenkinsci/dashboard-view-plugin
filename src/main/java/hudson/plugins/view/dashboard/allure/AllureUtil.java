package hudson.plugins.view.dashboard.allure;

import static hudson.plugins.view.dashboard.allure.AllureZipUtils.listEntries;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hudson.FilePath;
import hudson.matrix.MatrixProject;
import hudson.model.Job;
import hudson.model.Run;
import hudson.model.TopLevelItem;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class AllureUtil {

  private static final String ALLURE_REPORT_DEFAULT_ZIP = "archive/allure-report.zip";
  private static final String ALLURE_REPORT_DIRECTORY = "allure-report";
  private static final List<String> BUILD_STATISTICS_KEYS =
      Arrays.asList("passed", "failed", "broken", "skipped", "unknown", "total");

  /** "Cache" if matrix plugin is installed, so exception handling is only triggered once. */
  private static boolean matrixPluginInstalled = true;

  /**
   * Summarize the last test results from the passed set of jobs. If a job doesn't include any
   * tests, add a 0 summary.
   */
  public static AllureResultSummary getAllureResultSummary(
      Collection<TopLevelItem> jobs, boolean hideZeroTestProjects) {
    AllureResultSummary summary = new AllureResultSummary();

    for (TopLevelItem item : jobs) {
      if (isMatrixJob(item)) {
        MatrixProject mp = (MatrixProject) item;

        for (Job configuration : mp.getActiveConfigurations()) {
          summarizeJob(configuration, summary, hideZeroTestProjects);
        }
      } else if (item instanceof Job) {
        Job job = (Job) item;
        summarizeJob(job, summary, hideZeroTestProjects);
      }
    }

    return summary;
  }

  private static void summarizeJob(
      Job job, AllureResultSummary summary, boolean hideZeroTestProjects) {
    AllureResult allureResult = getAllureResult(job.getLastCompletedBuild());
    if (!hideZeroTestProjects) {
      summary.addAllureResult(allureResult);
    } else if (allureResult != null && allureResult.getSummarized() > 0) {
      summary.addAllureResult(allureResult);
    }
  }

  public static AllureResult getAllureResult(Run run) {
    if (run != null) {
      final FilePath report = new FilePath(run.getRootDir()).child(ALLURE_REPORT_DEFAULT_ZIP);
      try {
        if (report.exists()) {
          try (ZipFile archive = new ZipFile(report.getRemote())) {
            AllureResult ar = getAllureResultFromZipFile(archive);
            return new AllureResult(
                run.getParent(),
                ar.getTotal(),
                ar.getPassed(),
                ar.getFailed(),
                ar.getBroken(),
                ar.getSkipped(),
                ar.getUnknown());
          }
        }
      } catch (IOException | InterruptedException ignore) {
        return null;
      }
    }
    return null;
  }

  protected static AllureResult getAllureResultFromZipFile(ZipFile archive) {
    AllureResult ar = new AllureResult(null, 0, 0, 0, 0, 0, 0);
    ZipEntry summaryZipEntry =
        Optional.ofNullable(getSummary(archive, ALLURE_REPORT_DIRECTORY.concat("/export")))
            .orElse(getSummary(archive, ALLURE_REPORT_DIRECTORY.concat("/widgets")));
    if (summaryZipEntry != null && summaryZipEntry.getSize() != 0) {
      try (InputStream is = archive.getInputStream(summaryZipEntry)) {
        final ObjectMapper mapper = new ObjectMapper();
        final JsonNode summaryJson = mapper.readTree(is);
        final JsonNode statisticJson = summaryJson.get("statistic");
        final Map<String, Integer> statisticsMap = new HashMap<>();
        for (String key : BUILD_STATISTICS_KEYS) {
          statisticsMap.put(key, statisticJson.get(key).intValue());
        }
        ar.setTotal(statisticsMap.get("total"))
            .setPassed(statisticsMap.get("passed"))
            .setFailed(statisticsMap.get("failed"))
            .setBroken(statisticsMap.get("broken"))
            .setSkipped(statisticsMap.get("skipped"))
            .setUnknown(statisticsMap.get("unknown"));
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    return ar;
  }

  protected static ZipEntry getSummary(final ZipFile archive, final String locationPath) {
    Optional<List<ZipEntry>> entries =
        Optional.ofNullable(listEntries(archive, locationPath + "/summary.json"));
    return entries.isPresent() && entries.get().size() != 0 ? entries.get().get(0) : null;
  }

  private static final boolean isMatrixJob(TopLevelItem item) {
    if (!matrixPluginInstalled) {
      return false;
    }
    try {
      return item instanceof MatrixProject;
    } catch (NoClassDefFoundError x) {
      matrixPluginInstalled = false;
    }
    return false;
  }
}
