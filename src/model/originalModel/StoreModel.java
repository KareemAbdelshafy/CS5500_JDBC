package model.originalModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import model.CustomerType;
import model.CustomerVisit;
import model.IStoreModel;
import model.factors.IHolidayCalendar;
import model.factors.IWeatherForecast;

/**
 * This class {@code StoreModel} forecasts (or models) customers coming into a store over a period
 * of days.
 */
class StoreModel implements IStoreModel {

  private int customerNumber;
  private final IWeatherForecast weatherForecast;
  private final IHolidayCalendar holidayCalendar;
  private final ICustomerMixModel customerMixModel;
  private final TimeInStoreModel timeInStoreModel;
  private final ICustomerArrivalModel customerArrivalModel;
  // put startDate endDate and trafficFactor to form a model
  private final int scenarioID;

  /**
   * Constuct an instance of {@code StoreModel}.
   * @param weatherForecast {@link IWeatherForecast} the upcoming weather forecast.
   * @param holidayCalendar {@link IHolidayCalendar} the applicable holidays.
   * @param customerMixModel {@link ICustomerMixModel} the cusomter mix model
   * @param timeInStoreModel {@link TimeInStoreModel} the time in store model
   * @param customerArrivalModel {@link ICustomerArrivalModel} the cusomter arrival model
   * @param scenarioID
   */
  StoreModel(IWeatherForecast weatherForecast, IHolidayCalendar holidayCalendar,
             ICustomerMixModel customerMixModel,
             TimeInStoreModel timeInStoreModel,
             ICustomerArrivalModel customerArrivalModel, int scenarioID) {

    this.timeInStoreModel = timeInStoreModel;
    this.customerArrivalModel = customerArrivalModel;
    this.customerMixModel = customerMixModel;
    // TODO set factors according to scenarioID
    this.scenarioID = scenarioID;
    this.weatherForecast = weatherForecast;
    this.holidayCalendar = holidayCalendar;
    this.customerNumber = 0;
  }

  @Override
  public void generateForecast(LocalDate startDate, LocalDate endDate) {
    this.customerArrivalModel.setDateRange(startDate, endDate);
  }

  @Override
  public boolean hasNext() {
    return this.customerArrivalModel.hasNext();
  }

  @Override
  public CustomerVisit next() {
    // read the time they entered
    customerNumber = customerNumber + 1;
    LocalDateTime timeOfEntry = customerArrivalModel.next();
    LocalDate dateOfEntry = timeOfEntry.toLocalDate();

    // determine whether this customer entered on a time imapcted by a holiday (
    //   the holiday, the day prior to the holiday, the week prior to the holiday)
    IHolidayCalendar.HolidayTreatment holiday = holidayCalendar.getHoliday(dateOfEntry);

    // determine whether the day the customer is entering is forecasted to be nice
    IWeatherForecast.WeatherForecastType forecastType = weatherForecast.getForecast(dateOfEntry);

    // read the customer from the customer mix. its a function of day and time (e.g.
    // tuesday 10-12 is senior discount)
    CustomerType customer = customerMixModel.next(forecastType, timeOfEntry);

    // the minutes in store model represents the time a customer may be in the store. it is
    // a function of the day and time the customer entered, the customer type, whether it is
    // a holiday and whether the forecast is nice
    int minutesInStore = timeInStoreModel.next(timeOfEntry, customer);

    return new CustomerVisit(customerNumber, timeOfEntry, customer, minutesInStore, forecastType, holiday, scenarioID);
  }
}

