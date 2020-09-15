package model.originalModel;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import model.CustomerType;
import model.factors.CustomerMixModelFactors;
import model.factors.IWeatherForecast.WeatherForecastType;


/**
 * The {@code CustomerMixModel} represents the distribution of different types of
 * customers (senior and not senior). Model factors are specified on each iterations / retrieval.
 */
class CustomerMixModel implements ICustomerMixModel {

  private final LocalTime dinnerEnd;
  private final LocalTime seniorEndTime;
  private final LocalTime lunchEnd;


  private final CustomerMixModelFactors customerMixModelFactors;

  public CustomerMixModel(CustomerMixModelFactors customerMixModelFactors) {
    this.customerMixModelFactors  = customerMixModelFactors;

    // end time isn't inclusive
    LocalTime lunchEnd = customerMixModelFactors.getLunchEndTime();
    if(lunchEnd.getMinute() == 0 && lunchEnd.getSecond() == 0) {
      this.lunchEnd = lunchEnd.minusSeconds(1);
    } else {
      this.lunchEnd = lunchEnd;
    }

    // end time isn't inclusive
    LocalTime dinnerEnd = customerMixModelFactors.getDinnerEndTime();
    if(dinnerEnd.getMinute() == 0 && dinnerEnd.getSecond() == 0) {
      this.dinnerEnd = dinnerEnd.minusSeconds(1);
    } else {
      this.dinnerEnd = dinnerEnd;
    }


    // end time isn't inclusive
    LocalTime seniorEndTime = customerMixModelFactors.getSeniorEndTime();
    if(seniorEndTime.getMinute() == 0 && seniorEndTime.getSecond() == 0) {
      this.seniorEndTime = seniorEndTime.minusSeconds(1);
    } else {
      this.seniorEndTime = seniorEndTime;
    }
  }

  @Override
  public CustomerType next(WeatherForecastType weatherForecastType, LocalDateTime arrivalTime) {

    if (isGrabAndGo(arrivalTime, weatherForecastType)) {
      return CustomerType.GrabAndGo;
    }

    if (isSeniorDiscountTime(arrivalTime)) {
      if (randomBoolean(customerMixModelFactors.getSeniorDemographicPercent())) {
        return CustomerType.Senior;
      } else {
        return CustomerType.NonSenior;
      }
    }

    if (isDinnerRush(arrivalTime)) {
      if (randomBoolean(customerMixModelFactors.getDinnerRushFactor())) {
        return CustomerType.Dinner;
      }
    }

    if (isLunchRush(arrivalTime)) {
      if (randomBoolean(customerMixModelFactors.getLunchRushFactor())) {
        return CustomerType.Lunch;
      }
    }

    //the rest are regular shopper
    if (randomBoolean(customerMixModelFactors.getSeniorDemographicPercent())) {
      return CustomerType.Senior;
    }
    return CustomerType.NonSenior;
  }

  /**
   * Determines whether a customer is
   * {@link model.CustomerType#GrabAndGo}
   *
   * @param date the dateTime of arrival
   * @param forecastType the {@link model.factors.IWeatherForecast.WeatherForecastType}
   *
   * @return true if this customer should be considered GrabAndGo.
   */
  private boolean isGrabAndGo(LocalDateTime date, WeatherForecastType forecastType) {
    // in grab and go, traffic is 40% higher, e.g. 1000->1400
    //    therefore the mix of grab and go is 40% more of the orginal number
    //    or (1400-1000)/1400 == .285% of the new (higher) traffic number
    if (isWeekend(date.getDayOfWeek()) && isReallyNice(forecastType)) {

      return randomBoolean(
          customerMixModelFactors.getGrabAndGoFactor() / (1 + customerMixModelFactors.getGrabAndGoFactor()));
    }
    return false;
  }

  /**
   * deteremines whether the forecast is
   * {@link model.factors.IWeatherForecast.WeatherForecastType#ReallyNice}
   *
   * @param forecastType the {@link model.factors.IWeatherForecast.WeatherForecastType}
   * @return true if the {@link model.factors.IWeatherForecast.WeatherForecastType} is
   *         {@link model.factors.IWeatherForecast.WeatherForecastType#ReallyNice}  nice,
   *         false otherwise.
   */
  private static boolean isReallyNice(WeatherForecastType forecastType) {
    return (forecastType == WeatherForecastType.ReallyNice);
  }


  /**
   * determine if this is the senior discount time.
   *
   * @param date the time the customer enters
   * @return true if this is senior discount time, false otherwise.
   */
  private  boolean isSeniorDiscountTime(LocalDateTime date) {

    return ((date.getDayOfWeek() == customerMixModelFactors.getSeniorDayOfWeek())
            && isBetween(
                date.toLocalTime(), customerMixModelFactors.getSeniorStartTime(), seniorEndTime));
  }

  /**
   * determine if this is the dinner time.
   *
   * @param date the time the customer enters
   * @return true if this is dinner time, false otherwise.
   */
  private boolean isDinnerRush(LocalDateTime date) {

    return (!isWeekend(date.getDayOfWeek()) &&
        isBetween(date.toLocalTime(), customerMixModelFactors.getDinnerStartTime(), dinnerEnd));
  }

  /**
   * determine if this is the lunch time.
   *
   * @param date the time the customer enters
   * @return true if this is lunch time, false otherwise.
   */
  private boolean isLunchRush(LocalDateTime date) {

    return (!isWeekend(date.getDayOfWeek()) &&
        isBetween(
            date.toLocalTime(),
            customerMixModelFactors.getLunchStartTime(),
            this.lunchEnd));
  }

  private static boolean isBetween(LocalTime time, LocalTime start, LocalTime end) {
    boolean isAfterStart = time.isAfter(start.minusSeconds(1));
    boolean isBeforeEnd = time.isBefore(end.plusSeconds(1));

    return isAfterStart && isBeforeEnd;

  }


  /**
   * Generate a random boolean that is true the percentage of the time that corresponds
   * to the input parameter.
   *
   * @param probabilityTrue the probability of this returns true.
   * @return true or false.
   */
  private static boolean randomBoolean(double probabilityTrue) {
    return Math.random() >= 1.0 - probabilityTrue;
  }

  /**
   * determine if the day of week is on the weekend.
   *
   * @param arrivalDay the day of the arrival.
   * @return true if it ius weekend, false otherwise.
   */
  private static boolean isWeekend(DayOfWeek arrivalDay) {
    return arrivalDay == DayOfWeek.SATURDAY || arrivalDay == DayOfWeek.SUNDAY;
  }

}
