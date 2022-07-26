package hudson.plugins.view.dashboard.allure;

import static hudson.plugins.view.dashboard.allure.AllureZipUtils.listEntries;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.junit.Test;

public class AllureZipUtilTest {

  private static final String ALLURE_REPORT_RESOURCE = "allure-report.zip";
  private static final String ALLURE_REPORT_DIRECTORY = "allure-report";
  private static final String ALLURE_REPORT_WIDGETS_LOCATION = "/widgets";

  /** Test of ListEntries, of class AllureZipUtils. */
  @Test
  public void testListEntriesExistingPath() {
    String pathToFile =
        Objects.requireNonNull(getClass().getResource(ALLURE_REPORT_RESOURCE)).getPath();
    String pathToElement = ALLURE_REPORT_DIRECTORY.concat(ALLURE_REPORT_WIDGETS_LOCATION);

    List<ZipEntry> entries = listEntries(initZipFile(pathToFile), pathToElement);

    assertNotNull(entries);
    assertEquals(15, entries.size());
  }

  @Test
  public void testListEntriesNotExistingPath() {
    String pathToFile =
        Objects.requireNonNull(getClass().getResource(ALLURE_REPORT_RESOURCE)).getPath();
    String pathToElement = ALLURE_REPORT_DIRECTORY.concat("/random");

    List<ZipEntry> entries = listEntries(initZipFile(pathToFile), pathToElement);

    assertNotNull(entries);
    assertEquals(0, entries.size());
  }

  @Test
  public void testListEntriesEmptyPath() {
    String pathToFile =
        Objects.requireNonNull(getClass().getResource(ALLURE_REPORT_RESOURCE)).getPath();
    String pathToElement = "";

    List<ZipEntry> entries = listEntries(initZipFile(pathToFile), pathToElement);

    assertNotNull(entries);
    assertEquals(73, entries.size());
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
