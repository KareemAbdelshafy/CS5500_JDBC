package model.factors;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class CustomerMixModelFactors {

  private final LocalTime seniorStartTime;
  private final LocalTime seniorEndTime;
  private final DayOfWeek seniorDayOfWeek;
  private final LocalTime dinnerStartTime;
  private final LocalTime dinnerEndTime;
  private final LocalTime lunchStartTime;
  private final LocalTime lunchEndTime;

  private final double lunchRushFactor;
  private final double dinnerRushFactor;
  private final double grabAndGoFactor;
  private final double seniorDemographicPercent;
  private final double percentOfTrafficThatIsSeniorDuringSeniorShopping;


  public CustomerMixModelFactors(LocalTime seniorStartTime, LocalTime seniorEndTime, DayOfWeek seniorDayOfWeek,
                                 LocalTime dinnerStartTime, LocalTime dinnerEndTime, LocalTime lunchStartTime,
                                 LocalTime lunchEndTime, double lunchRushFactor, double dinnerRushFactor,
                                 double grabAndGoFactor, double seniorDemographicPercent,
                                 double percentOfTrafficThatIsSeniorDuringSeniorShopping) {
    this.seniorStartTime = seniorStartTime;
    this.seniorEndTime = seniorEndTime;
    this.seniorDayOfWeek = seniorDayOfWeek;
    this.dinnerStartTime = dinnerStartTime;
    this.dinnerEndTime = dinnerEndTime;
    this.lunchStartTime = lunchStartTime;
    this.lunchEndTime = lunchEndTime;
    this.lunchRushFactor = lunchRushFactor;
    this.dinnerRushFactor = dinnerRushFactor;
    this.grabAndGoFactor = grabAndGoFactor;
    this.seniorDemographicPercent = seniorDemographicPercent;
    this.percentOfTrafficThatIsSeniorDuringSeniorShopping = percentOfTrafficThatIsSeniorDuringSeniorShopping;
  }

  public LocalTime getSeniorStartTime() {
    return seniorStartTime;
  }

  public LocalTime getSeniorEndTime() {
    return seniorEndTime;
  }

  public DayOfWeek getSeniorDayOfWeek() {
    return seniorDayOfWeek;
  }

  public LocalTime getDinnerStartTime() {
    return dinnerStartTime;
  }

  public LocalTime getDinnerEndTime() {
    return dinnerEndTime;
  }

  public LocalTime getLunchStartTime() {
    return lunchStartTime;
  }

  public LocalTime getLunchEndTime() {
    return lunchEndTime;
  }

  public double getLunchRushFactor() {
    return lunchRushFactor - 1;
  }

  public double getDinnerRushFactor() {
    return dinnerRushFactor - 1;
  }

  public double getGrabAndGoFactor() {
    return grabAndGoFactor - 1;
  }

  public double getSeniorDemographicPercent() {
    return seniorDemographicPercent;
  }

  public double getPercentOfTrafficThatIsSeniorDuringSeniorShopping() {
    return percentOfTrafficThatIsSeniorDuringSeniorShopping;
  }
}
