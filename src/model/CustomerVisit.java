package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import model.factors.IHolidayCalendar;
import model.factors.IWeatherForecast;

/**
 * This {@code CustomerVisit} is the ViewModel representing the fields required by the
 * {@link view.StoreForecastView} needs to display the results of the forecast.
 */
public class CustomerVisit {
  private final int id;
  private final LocalDateTime arrivalTime;
  private final CustomerType customerType;
  private final int timeInStore;
  private final IWeatherForecast.WeatherForecastType forecastType;
  private final IHolidayCalendar.HolidayTreatment holiday;
  private final int scenarioID;

  /**
   *
   */
  public CustomerVisit(int customerNumber, LocalDateTime timeOfEntry, CustomerType customerType,
                       int minutesInStore, IWeatherForecast.WeatherForecastType forecastType,
                       IHolidayCalendar.HolidayTreatment holiday, int scenarioID) {
    this.id = customerNumber;
    this.arrivalTime = timeOfEntry;
    this.customerType = customerType;
    this.timeInStore = minutesInStore;
    this.forecastType = forecastType;
    this.holiday = holiday;
    //new added
    this.scenarioID = scenarioID;
  }

  /**
   * read the customers unique identifier.
   * @return the customers unique identifier
   */
  public int getId() {
    return this.id;
  }

  /**
   * read the time (and date) the customer arrived in the store.
   * @return the time (and date) the customer arrived in the store.
   */
  public LocalDateTime getArrivalTime() {
    return this.arrivalTime;
  }

  /**
   * Get the number of minutes the customer spent in the store.
   * @return the number of minutes the customer spent in the store.
   */
  public int getMinutesInStore() {
    return this.timeInStore;
  }

  /**
   * read the type of customer.
   * @return the {@link model.CustomerType} of the customer.
   */
  public CustomerType getCustomerType() {
    return this.customerType;
  }

  public IHolidayCalendar.HolidayTreatment getHolidayType() {
    return holiday;
  }

  public IWeatherForecast.WeatherForecastType getWeatherType() {
    return forecastType;
  }

  public int getScenarioID() {
    return scenarioID;
  }
}
