package model.factors;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class TimeInStoreFactors {


  private final int grabAndGoShoppingTime;
  private final int lunchShoppingTime;
  private final int dinnerShoppingTime;
  private final int weekendShoppingTime;

  private final int seniorMinShoppingTime;
  private final int seniorMaxShoppingTime;
  private final int nonSeniorAvgShoppingTime;
  private final int nonSeniorMinShoppingTime;
  private final int nonSeniorMaxShoppingTime;

  public TimeInStoreFactors(int grabAndGoShoppingTime, int lunchShoppingTime, int dinnerSoppingTime,
                            int weekendShoppingTime, int seniorMinShoppingTime, int seniorMaxShoppingTime,
                            int nonSeniorAvgShoppingTime, int nonSeniorMinShoppingTime, int nonSeniorMaxShoppingTime) {
    this.grabAndGoShoppingTime = grabAndGoShoppingTime;
    this.lunchShoppingTime = lunchShoppingTime;
    this.dinnerShoppingTime = dinnerSoppingTime;
    this.weekendShoppingTime = weekendShoppingTime;
    this.seniorMinShoppingTime = seniorMinShoppingTime;
    this.seniorMaxShoppingTime = seniorMaxShoppingTime;
    this.nonSeniorAvgShoppingTime = nonSeniorAvgShoppingTime;
    this.nonSeniorMinShoppingTime = nonSeniorMinShoppingTime;
    this.nonSeniorMaxShoppingTime = nonSeniorMaxShoppingTime;
  }

  public int getGrabAndGoShoppingTime() {
    return grabAndGoShoppingTime;
  }

  public int getLunchShoppingTime() {
    return lunchShoppingTime;
  }

  public int getDinnerShoppingTime() {
    return dinnerShoppingTime;
  }

  public int getWeekendShoppingTime() {
    return weekendShoppingTime;
  }

  public int getSeniorMinShoppingTime() {
    return seniorMinShoppingTime;
  }

  public int getSeniorMaxShoppingTime() {
    return seniorMaxShoppingTime;
  }

  public int getNonSeniorAvgShoppingTime() {
    return nonSeniorAvgShoppingTime;
  }

  public int getNonSeniorMinShoppingTime() {
    return nonSeniorMinShoppingTime;
  }

  public int getNonSeniorMaxShoppingTime() {
    return nonSeniorMaxShoppingTime;
  }
}
