package hudson.plugins.view.dashboard.allure;

import hudson.model.Job;

public class AllureResult {

  private final Job job;
  protected int total;
  protected int passed;
  protected int failed;
  protected int broken;
  protected int skipped;
  protected int unknown;

  public AllureResult(
      Job job, int total, int passed, int failed, int broken, int skipped, int unknown) {
    super();
    this.job = job;
    this.total = total;
    this.passed = passed;
    this.failed = failed;
    this.broken = broken;
    this.skipped = skipped;
    this.unknown = unknown;
  }

  public Job getJob() {
    return job;
  }

  public AllureResult setTotal(int total) {
    this.total = total;
    return this;
  }

  public int getTotal() {
    return total;
  }

  public AllureResult setPassed(int passed) {
    this.passed = passed;
    return this;
  }

  public int getPassed() {
    return passed;
  }

  public double getPassedPct() {
    return getPct(passed);
  }

  public AllureResult setFailed(int failed) {
    this.failed = failed;
    return this;
  }

  public int getFailed() {
    return failed;
  }

  public double getFailedPct() {
    return getPct(failed);
  }

  public AllureResult setBroken(int broken) {
    this.broken = broken;
    return this;
  }

  public int getBroken() {
    return broken;
  }

  public double getBrokenPct() {
    return getPct(broken);
  }

  public AllureResult setSkipped(int skipped) {
    this.skipped = skipped;
    return this;
  }

  public int getSkipped() {
    return skipped;
  }

  public double getSkippedPct() {
    return getPct(skipped);
  }

  public AllureResult setUnknown(int unknown) {
    this.unknown = unknown;
    return this;
  }

  public int getUnknown() {
    return unknown;
  }

  public double getUnknownPct() {
    return getPct(unknown);
  }

  protected double getPct(int dividendValue) {
    return total != 0 ? ((double) dividendValue / total) : 0d;
  }

  protected int getSummarized() {
    return getPassed() + getFailed() + getBroken() + getSkipped() + getUnknown();
  }
}
