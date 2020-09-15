package model.originalModel;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Random;
import model.CustomerType;
import model.factors.TimeInStoreFactors;

/**
 * The {@code TimeInStoreModel} is a model that forecasts how long a customer spends in the store.
 * This model is a function of {@link model.factors.IHolidayCalendar.HolidayTreatment},
 * {@link model.factors.IWeatherForecast.WeatherForecastType}, the arrival date and time of the
 * custmer, and {@link model.CustomerType}.
 */
class TimeInStoreModel {

  private final TimeInStoreFactors timeInStoreFactors;

  public TimeInStoreModel(TimeInStoreFactors timeInStoreFactors) {
    this.timeInStoreFactors = timeInStoreFactors;
  }

  /**
   * Determine how long someone spends in the store (in minutes).
   *
   * @param timeOfEntry the date/time the customer arrives
   * @param customer the {@link model.CustomerType}\
   *
   * @return the number of minutes this customer is forecvasted to be in the store.
   */
  public int next(LocalDateTime timeOfEntry, CustomerType customer) {

    DayOfWeek arrivalDay = timeOfEntry.getDayOfWeek();
    Random random = new Random();
    int shoppingDuration = 0;

    if (customer == CustomerType.Senior){


      shoppingDuration = timeInStoreFactors.getSeniorMinShoppingTime() +
              random.nextInt(timeInStoreFactors.getSeniorMaxShoppingTime() - timeInStoreFactors.getSeniorMinShoppingTime());
    } else if (customer == CustomerType.GrabAndGo) {
      return timeInStoreFactors.getGrabAndGoShoppingTime();
    } else if(isWeekend(arrivalDay)) {
      return timeInStoreFactors.getWeekendShoppingTime();
    } else if (customer == CustomerType.Lunch){
      return timeInStoreFactors.getLunchShoppingTime();
    } else if (customer == CustomerType.Dinner){
      return timeInStoreFactors.getDinnerShoppingTime();
    } else {
      shoppingDuration = getNonSeniorShoppingDuration();
    }

    return shoppingDuration;
  }

  private int getNonSeniorShoppingDuration() {
    int shoppingDuration;

    Random random = new Random();
    double averagek3 = ((double)timeInStoreFactors.getNonSeniorAvgShoppingTime()) / 3;

    // use chi squared distribution to best represent a distribution
    //    of non-seniors time in store from 6 to 75 with a mean of 25
    //    approach:
    //      sum three (gaussian)^2 distributions ~= chi squared, k=3
    shoppingDuration = (int)(Math.pow(random.nextGaussian(), 2) * averagek3
        + Math.pow(random.nextGaussian(), 2) * averagek3
        + Math.pow(random.nextGaussian(), 2) * averagek3);

    if (shoppingDuration > timeInStoreFactors.getNonSeniorMaxShoppingTime()) {
      shoppingDuration = timeInStoreFactors.getNonSeniorMaxShoppingTime();
    } else if (shoppingDuration < timeInStoreFactors.getNonSeniorMinShoppingTime()) {
      shoppingDuration = timeInStoreFactors.getNonSeniorMinShoppingTime();
    }
    return shoppingDuration;
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
