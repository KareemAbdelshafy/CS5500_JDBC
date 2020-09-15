package model.originalModel;

import java.time.LocalDateTime;
import model.CustomerType;
import model.factors.IWeatherForecast.WeatherForecastType;

/**
 * this interface {@code ICustomerMixModel} represents the deriving the next {@code CustomerType}
 * entering the store.
 */
public interface ICustomerMixModel {

  /**
   *
   * @param forecastType the current {@link model.factors.IWeatherForecast.WeatherForecastType}
   * @param arrivalTime the time of arrival for this customer
   * @return {@code CustomerType}
   */
  CustomerType next(WeatherForecastType forecastType, LocalDateTime arrivalTime);
}
