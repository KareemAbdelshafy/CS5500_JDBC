package model.factors;


import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;
import model.factors.IWeatherForecast.WeatherForecastType;
import model.factors.WeatherForecast;
import org.junit.Before;
import org.junit.Test;

public class WeatherForecastTest {

  private LocalDate jan8;
  private LocalDate jan9;
  private LocalDate jan10;
  private LocalDate jan11;

  private WeatherForecast forecast;

  @Before
  public void setup() throws IOException {
    String csv =
        "2020-01-08,Nice" + System.lineSeparator()
            + "2020-01-09,ReallyNice" + System.lineSeparator()
            + "2020-01-10,Unknown";

    forecast = new WeatherForecast(new Scanner(csv));

    jan8 = LocalDate.of(2020, 1, 8);
    jan9 = LocalDate.of(2020, 1, 9);
    jan10 = LocalDate.of(2020, 1, 10);
    jan11 = LocalDate.of(2020, 1, 11);
  }

  @Test
  public void getForecastNotInEnum() {
    assertEquals(WeatherForecastType.Unknown, forecast.getForecast(jan8));
  }

  @Test
  public void getForecastReallyNice() {
    assertEquals(WeatherForecastType.ReallyNice, forecast.getForecast(jan9));
  }

  @Test
  public void getForecastUnknown() {
    assertEquals(WeatherForecastType.Unknown, forecast.getForecast(jan10));
  }

  @Test
  public void getForecastDateMissing() {
    assertEquals(WeatherForecastType.Unknown, forecast.getForecast(jan11));
  }

}