package model.originalModel;

import java.io.IOException;
import java.io.StringReader;
import java.time.DayOfWeek;
import java.time.LocalTime;
import model.CustomerType;
import model.factors.CustomerMixModelFactors;
import model.factors.CustomerMixModelFactorsFromFile;
import model.factors.IWeatherForecast.WeatherForecastType;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class CustomerMixModelTest {

  private double percentDinnerRush = 1.1;
  private double percentLunchRush = 1.15;
  private double percentSenior = .15;
  private double percentSeniorInDiscount = .4;
  private double percentIncreaseGrabAndGo = 0.4;
  private LocalTime lunchStart = LocalTime.of(12,0);
  private LocalTime lunchEnd  = LocalTime.of(13,0);
  private LocalTime dinnerStart = LocalTime.of(17,0);
  private LocalTime dinnerEnd  = LocalTime.of(18,30);
  private LocalTime seniorStartTime =  LocalTime.of(10,0);
  private LocalTime seniorEndTime  = LocalTime.of(12,0);
  private DayOfWeek seniorDay = DayOfWeek.TUESDAY;
  private CustomerMixModelFactors customerMixModelFactors;

  @Test
  public void testCustomerMixSrHour() throws IOException {


    customerMixModelFactors =
        (new CustomerMixModelFactorsFromFile(getTrafficFactors())).getCustomerMixModelFactors();

    ICustomerMixModel mix = new CustomerMixModel(customerMixModelFactors);

    LocalDateTime tuesday11am = LocalDateTime.of(2020, 05, 26, 11, 0);

    //assertTrue(CustomerMixModel.isSeniorDis countTime(tuesday11am));

    double noOfSenior = 0;
    int iterations= 100000;
    for (int i =0; i < iterations ; i++){

      CustomerType customer= mix.next(WeatherForecastType.Unknown, tuesday11am);

      if (customer == CustomerType.Senior){
        noOfSenior = noOfSenior +1;
      }
    }
    //System.out.print((noOfSenior/ iterations));
    assertTrue( Math.abs( (noOfSenior / iterations) - customerMixModelFactors.getSeniorDemographicPercent()) < 0.05 );

  }


  @Test
  public void testCustomerMix() throws IOException {
    CustomerMixModelFactors customerMixModelFactors =
        (new CustomerMixModelFactorsFromFile(getTrafficFactors())).getCustomerMixModelFactors();

    ICustomerMixModel mix = new CustomerMixModel(customerMixModelFactors);

    LocalDateTime tuesday1pm = LocalDateTime.of(2020, 05, 26, 13, 0);
    double noOfSenior = 0;
    int iterations= 100000;

    for (int i =0; i < iterations ; i++){

      CustomerType customer= mix.next(WeatherForecastType.Unknown, tuesday1pm);

      if (customer == CustomerType.Senior){
        noOfSenior = noOfSenior +1;
      }
    }
    //System.out.print((noOfSenior/ iterations));
    assertEquals( noOfSenior/ iterations, percentSenior,0.05 );

  }

  @Test
  public void testCustomerMixGrabAndGo() throws IOException {

    CustomerMixModelFactors customerMixModelFactors =
        (new CustomerMixModelFactorsFromFile(getTrafficFactors())).getCustomerMixModelFactors();

    ICustomerMixModel mix = new CustomerMixModel(customerMixModelFactors);

    LocalDateTime saturday = LocalDateTime.of(2020, 05, 23, 13, 0);
    double grabAndGo = 0;
    int iterations= 100000;

    for (int i =0; i < iterations ; i++){

      CustomerType customer= mix.next(WeatherForecastType.ReallyNice, saturday);

      if (customer == CustomerType.GrabAndGo){
        grabAndGo = grabAndGo +1;
      }
    }

    // there is an incresae of 40% additional traffic on really nice weekend days, but only that
    //  incremental traffic is grab and go.
    double percentGrabAndGoOfTotal =
        customerMixModelFactors.getGrabAndGoFactor() / (1 + customerMixModelFactors.getGrabAndGoFactor());

    //System.out.print((noOfSenior/ iterations));
    assertEquals((grabAndGo/ iterations), percentGrabAndGoOfTotal, 0.05);

  }

  private static StringReader getTrafficFactors() throws IOException {
    String csv =
        "Monday,800" + System.lineSeparator()
            + "Tuesday,1000" + System.lineSeparator()
            + "Wednesday,1200" + System.lineSeparator()
            + "Thursday,900" + System.lineSeparator()
            + "Friday,2500" + System.lineSeparator()
            + "Saturday,4000" + System.lineSeparator()
            + "Sunday,5000" + System.lineSeparator()
            + "HolidayFactor,.2" + System.lineSeparator()
            + "DayBeforeHolidayFactor,1.4" + System.lineSeparator()
            + "WeekBeforeHolidayFactor,1.15" + System.lineSeparator()
            + "LunchRushFactor,1.15" + System.lineSeparator()
            + "DinnerRushFactor,1.1" + System.lineSeparator()
            + "PercentSeniorDemographic,.15" + System.lineSeparator()
            + "GrabAndGoWeekendFactor,1.4" + System.lineSeparator()
            + "SeniorPercentageInSeniorDiscountHour,0.4" + System.lineSeparator()
            + "GrabAndGoPercentOnGoodWeather,0.4" + System.lineSeparator()
            + "GrabAndGoShoppingTime,20" + System.lineSeparator()
            + "LunchShoppingTime,10" + System.lineSeparator()
            + "DinnerShoppingTime,20" + System.lineSeparator()
            + "WeekendShoppingTime,60" + System.lineSeparator()
            + "SeniorMinimunShoppingTime,45" + System.lineSeparator()
            + "SeniorMaxShoppingTime,60" + System.lineSeparator()
            + "NonSeniorAverageShoppingTime,25" + System.lineSeparator()
            + "NonSeniorMinShoppingTime,6" + System.lineSeparator()
            + "NonSeniorMaxShoppingTime,75" + System.lineSeparator()
            + "SeniorStartTime,10:00" + System.lineSeparator()
            + "SeniorEndTime,12:00" + System.lineSeparator()
            + "SeniorDayOfWeek,Tuesday"+ System.lineSeparator()
            + "DinnerStartTime,17:00" + System.lineSeparator()
            + "DinnerEndTime,18:30"+ System.lineSeparator()
            + "LunchStartTime,12:00" + System.lineSeparator()
            + "LunchEndTime,13:00"+ System.lineSeparator()
            + "StoreOpenTime,06:00" + System.lineSeparator()
            + "StoreCloseTime,21:00";

    return new StringReader(csv);
  }


}