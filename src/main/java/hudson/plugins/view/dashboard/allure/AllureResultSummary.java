package hudson.plugins.view.dashboard.allure;

import java.util.ArrayList;
import java.util.List;

public class AllureResultSummary extends AllureResult {

  private final List<AllureResult> allureResults = new ArrayList<>();

  public AllureResultSummary() {
    super(null, 0, 0, 0, 0, 0, 0);
  }

  public void addAllureResult(AllureResult allureResult) {
    if (allureResult == null) {
      allureResult = new AllureResult(null, 0, 0, 0, 0, 0, 0);
    }
    allureResults.add(allureResult);
    total += allureResult.getTotal();
    passed += allureResult.getPassed();
    failed += allureResult.getFailed();
    broken += allureResult.getBroken();
    skipped += allureResult.getSkipped();
    unknown += allureResult.getUnknown();
  }

  public List<AllureResult> getAllureResults() {
    return allureResults;
  }
}
