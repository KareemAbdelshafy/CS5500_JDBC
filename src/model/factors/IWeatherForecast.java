package model.factors;

import java.time.LocalDate;

/**
 * This {@code IWeatherForecast} represents the weather forecast.
 */
public interface IWeatherForecast {

  /**
   * read the WeatherForecastType for a given date.  If it is not found then return Unknown.
   * @param date the date
   * @return WeatherForecastType
   */
  WeatherForecastType getForecast(LocalDate date);

  public enum WeatherForecastType {ReallyNice, Unknown}
}