package hudson.plugins.view.dashboard.allure;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.junit.Test;

public class AllureUtilTest {

  private static final String ALLURE_REPORT_RESOURCE = "allure-report.zip";
  private static final String ALLURE_REPORT_DIRECTORY = "allure-report";
  private static final String ALLURE_REPORT_WIDGETS_LOCATION = "/widgets";

  /** Test of getSummary, of class AllureUtil. */
  @Test
  public void testGetSummaryExistingPath() {
    String pathToFile =
        Objects.requireNonNull(getClass().getResource(ALLURE_REPORT_RESOURCE)).getPath();

    ZipEntry summaryZipEntry =
        AllureUtil.getSummary(
            initZipFile(pathToFile),
            ALLURE_REPORT_DIRECTORY.concat(ALLURE_REPORT_WIDGETS_LOCATION));

    assertNotNull(summaryZipEntry);
    assertNotEquals(0, summaryZipEntry.getSize());
  }

  @Test
  public void testGetSummaryWrongPath() {
    String pathToFile =
        Objects.requireNonNull(getClass().getResource(ALLURE_REPORT_RESOURCE)).getPath();

    ZipEntry summaryZipEntry =
        AllureUtil.getSummary(
            initZipFile(pathToFile), "random".concat(ALLURE_REPORT_WIDGETS_LOCATION));

    assertNull(summaryZipEntry);
  }

  @Test
  public void testGetSummaryNullPath() {
    String pathToFile =
        Objects.requireNonNull(getClass().getResource(ALLURE_REPORT_RESOURCE)).getPath();

    ZipEntry summaryZipEntry = AllureUtil.getSummary(initZipFile(pathToFile), null);

    assertNull(summaryZipEntry);
  }

  @Test
  public void testGetSummaryNullZip() {
    ZipEntry summaryZipEntry =
        AllureUtil.getSummary(null, ALLURE_REPORT_DIRECTORY.concat(ALLURE_REPORT_WIDGETS_LOCATION));

    assertNull(summaryZipEntry);
  }

  @Test
  public void testGetAllureResultFromZipFileWithWidgetsSummary() {
    String pathToFile =
        Objects.requireNonNull(getClass().getResource(ALLURE_REPORT_RESOURCE)).getPath();

    AllureResult allureResult = AllureUtil.getAllureResultFromZipFile(initZipFile(pathToFile));

    assertNotNull(allureResult);
    assertEquals(8, allureResult.getTotal());
  }

  private ZipFile initZipFile(String pathToFile) {
    ZipFile archive;
    try {
      archive = new ZipFile(pathToFile);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return archive;
  }
}
