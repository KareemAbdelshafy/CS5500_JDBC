package view;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import model.CustomerType;
import model.CustomerVisit;
import model.factors.IHolidayCalendar;
import model.factors.IWeatherForecast;

import org.junit.Before;
import org.junit.Test;

public class StoreForecastCsvViewTest {

  List<CustomerVisit> customerVisitList = new ArrayList<>();
  StoreForecastCsvView view;
  LocalDateTime monday10am = LocalDateTime.of(2020, 06, 8, 10, 0);
  StringWriter strWriter;

  @Before
  public void setup() throws IOException {
    strWriter = new StringWriter();
    view = new StoreForecastCsvView(new BufferedWriter(strWriter));
    customerVisitList.add(new CustomerVisit(1, monday10am, CustomerType.Senior, 60,  IWeatherForecast.WeatherForecastType.Unknown, IHolidayCalendar.HolidayTreatment.NonHoliday, 0));
    customerVisitList.add(new CustomerVisit(2, monday10am, CustomerType.NonSenior, 24,  IWeatherForecast.WeatherForecastType.Unknown,IHolidayCalendar.HolidayTreatment.NonHoliday, 0));
    customerVisitList.add(new CustomerVisit(3, monday10am, CustomerType.NonSenior, 26,  IWeatherForecast.WeatherForecastType.Unknown,IHolidayCalendar.HolidayTreatment.NonHoliday, 0));
    view.setViewModel(customerVisitList);
  }

  @Test
  public void testCSV() {

    String expected = "1,2020-06-08T10:00:00,60,Senior\n"
        + "2,2020-06-08T10:00:00,24,NonSenior\n"
        + "3,2020-06-08T10:00:00,26,NonSenior\n";

    assertEquals(expected, strWriter.toString());

  }
}