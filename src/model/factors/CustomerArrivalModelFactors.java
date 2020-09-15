package model.factors;

import static java.time.DayOfWeek.FRIDAY;
import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.THURSDAY;
import static java.time.DayOfWeek.TUESDAY;
import static java.time.DayOfWeek.WEDNESDAY;

import java.time.DayOfWeek;
import java.time.LocalTime;
import model.factors.IHolidayCalendar.HolidayTreatment;
import model.factors.IWeatherForecast.WeatherForecastType;

public class CustomerArrivalModelFactors {

  static String sql = "select * from trafficfactor \n" +
      "where TrafficFactorID = ?";

  private final LocalTime dinnerStart;
  private final LocalTime dinnerEnd;
  private final LocalTime lunchStart;
  private final LocalTime lunchEnd;
  private final double lunchRushFactor;
  private final double dinnerRushFactor;
  private final LocalTime storeOpenTime;
  private final LocalTime storeCloseTime;

  private final int mon;
  private final int tue;
  private final int wed;
  private final int thur;
  private final int fri;
  private final int sat;
  private final int sun;

  private final double holidayFactor;
  private final double dayBeforeHolidayFactor;
  private final double weekBeforeHolidayFactor;

  private final double grabAndGoFactor;


  CustomerArrivalModelFactors(LocalTime dinnerStart, LocalTime dinnerEnd,
      LocalTime lunchStart, LocalTime lunchEnd, double lunchRushFactor, double dinnerRushFactor,
      LocalTime storeOpenTime, LocalTime storeCloseTime, int mon, int tue, int wed, int thur,
      int fri, int sat, int sun, double holidayFactor, double dayBeforeHolidayFactor,
      double weekBeforeHolidayFactor, double grabAndGoFactor) {

    this.dinnerStart = dinnerStart;
    this.dinnerEnd = dinnerEnd;
    this.lunchStart = lunchStart;
    this.lunchEnd = lunchEnd;
    this.lunchRushFactor = lunchRushFactor;
    this.dinnerRushFactor = dinnerRushFactor;
    this.storeOpenTime = storeOpenTime;
    this.storeCloseTime = storeCloseTime;
    this.mon = mon;
    this.tue = tue;
    this.wed = wed;
    this.thur = thur;
    this.fri = fri;
    this.sat = sat;
    this.sun = sun;
    this.holidayFactor = holidayFactor;
    this.dayBeforeHolidayFactor = dayBeforeHolidayFactor;
    this.weekBeforeHolidayFactor = weekBeforeHolidayFactor;
    this.grabAndGoFactor = grabAndGoFactor;
  }

  public LocalTime getDinnerStart() {
    return dinnerStart;
  }

  public LocalTime getDinnerEnd() {
    return dinnerEnd;
  }

  public LocalTime getLunchStart() {
    return lunchStart;
  }

  public LocalTime getLunchEnd() {
    return lunchEnd;
  }

  public double getLunchRushFactor() {
    return lunchRushFactor;
  }

  public double getDinnerRushFactor() {
    return dinnerRushFactor;
  }

  public LocalTime getStoreOpenTime() {
    return storeOpenTime;
  }

  public LocalTime getStoreCloseTime() {
    return storeCloseTime;
  }

  public int getAverageTraffic(DayOfWeek dayOfWeek) {
    if (dayOfWeek == MONDAY) {
      return this.mon;
    } else if (dayOfWeek == TUESDAY) {
      return this.tue;
    } else if (dayOfWeek == WEDNESDAY) {
      return this.wed;
    } else if (dayOfWeek == THURSDAY) {
      return this.thur;
    } else if (dayOfWeek == FRIDAY) {
      return this.fri;
    } else if (dayOfWeek == SATURDAY) {
      return this.sat;
    } else {
      return this.sun;
    }
  }

  public double getHolidayFactor(HolidayTreatment holidayTreatment) {
    if (holidayTreatment.equals(HolidayTreatment.Holiday)) {
      return this.holidayFactor;
    } else if (holidayTreatment.equals(HolidayTreatment.WeekBeforeHoliday)) {
      return this.weekBeforeHolidayFactor;
    } else if (holidayTreatment.equals(HolidayTreatment.DayBeforeHoliday)) {
      return this.dayBeforeHolidayFactor;
    }

    return 1;
  }

  public double getWeatherFactor(WeatherForecastType weatherForecastType, DayOfWeek day) {
    if (isWeekend(day) && weatherForecastType.equals(WeatherForecastType.ReallyNice)) {
      return this.grabAndGoFactor;
    }
    return 1;
  }

  private static boolean isWeekend(DayOfWeek day) {
    return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
  }
}

