package model.originalModel;

import model.factors.CustomerArrivalModelFactors;
import model.factors.CustomerMixModelFactors;
import model.factors.IHolidayCalendar;
import model.factors.IWeatherForecast;
import model.factors.TimeInStoreFactors;

/**
 * This class {@code StoreModelBuilder} is responsible for constructing the
 * {@link model.originalModel.StoreModel} by leveraging the {@link model.factors.IWeatherForecast}
 */
public class StoreModelBuilder {

  private IWeatherForecast weatherForecast;
  private IHolidayCalendar holidayCalendar;
  private TimeInStoreFactors timeInStoreFactors;
  private CustomerMixModelFactors customerMixModelFactors;
  private Integer scenarioID;
  private CustomerArrivalModelFactors customerArrivalModelFactors
      ;

  public StoreModelBuilder setScenarioID(int scenarioID) {
    this.scenarioID = scenarioID;
    return this;
  }

  public StoreModelBuilder setWeatherForecast(IWeatherForecast weatherForecast) {
    this.weatherForecast = weatherForecast;
    return this;
  }

  public StoreModelBuilder setHolidayCalendar(IHolidayCalendar holidayCalendar) {
    this.holidayCalendar = holidayCalendar;
    return this;
  }

  public StoreModelBuilder setCustomerArrivalModelFactors(CustomerArrivalModelFactors customerArrivalModelFactors) {
    this.customerArrivalModelFactors = customerArrivalModelFactors;
    return this;
  }

  public StoreModelBuilder setTimeInStoreFactors(TimeInStoreFactors timeInStoreFactors) {
    this.timeInStoreFactors = timeInStoreFactors;
    return this;
  }

  public StoreModelBuilder setCustomerMixModelFactors(CustomerMixModelFactors customerMixModelFactors) {
    this.customerMixModelFactors = customerMixModelFactors;
    return this;
  }

  public StoreModel build() throws IllegalStateException {

    if (holidayCalendar == null || weatherForecast == null) {
      throw new IllegalStateException("StoreModelBuilder requires IHolidayCalendar, "
          + "IWeatherForecast and TrafficFactors to be set");
    }

    ICustomerMixModel customerMixModel = new CustomerMixModel(customerMixModelFactors);

    TimeInStoreModel timeInStoreModel = new TimeInStoreModel(timeInStoreFactors);

    ICustomerArrivalModel customerArrivalModel = new CustomerArrivalModel(
        customerArrivalModelFactors,
        holidayCalendar,
        weatherForecast);

    //TODO: replace trafficFactor with three smaller factors
    return new StoreModel(
        weatherForecast,
        holidayCalendar,
        customerMixModel,
        timeInStoreModel,
        customerArrivalModel,
        scenarioID);
  }
}