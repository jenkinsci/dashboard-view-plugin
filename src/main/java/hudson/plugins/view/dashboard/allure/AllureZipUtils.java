package hudson.plugins.view.dashboard.allure;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/** eroshenkoam. 11.04.17 */
public final class AllureZipUtils {

  private AllureZipUtils() {}

  /**
   * Static method to parse zip archive and extract entities from it
   *
   * @param zip - ZipFile of allure-report archive
   * @param path - directory or file to extract from archive
   * @return List<ZipEntry> - list of found entries
   */
  public static List<ZipEntry> listEntries(ZipFile zip, String path) {
    if (zip == null || path == null) {
      return null;
    }
    final Enumeration<? extends ZipEntry> entries = zip.entries();
    final List<ZipEntry> files = new ArrayList<>();
    while (entries.hasMoreElements()) {
      final ZipEntry entry = entries.nextElement();
      if (entry.getName().startsWith(path)) {
        files.add(entry);
      }
    }
    return files;
  }
}
