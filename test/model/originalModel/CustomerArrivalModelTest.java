package model.originalModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import model.CustomerType;
import model.factors.CustomerArrivalFactorsFromFile;
import model.factors.CustomerArrivalModelFactors;
import model.factors.CustomerMixModelFactors;
import model.factors.CustomerMixModelFactorsFromFile;
import model.factors.IHolidayCalendar;
import model.factors.IWeatherForecast;
import model.factors.IWeatherForecast.WeatherForecastType;
import org.junit.Before;
import org.junit.Test;

public class CustomerArrivalModelTest {

  private LocalDate saturday;
  private LocalDate sunday;
  private LocalDate monday;
  private LocalDate tuesday;
  private LocalDate wednesday;
  private LocalDate thursday;
  private LocalDate friday;

  private IHolidayCalendar nonHoliday;
  private IHolidayCalendar holiday;
  private IHolidayCalendar weekBeforeHoliday;
  private IWeatherForecast unknownForecast;
  private IWeatherForecast reallyNiceForecast;

  //private TrafficFactors trafficFactors;
  private CustomerArrivalModelFactors customerArrivalModelFactors;

  private double percentDinnerRush = 1.1;
  private double percentLunchRush = 1.15;
  private double percentSenior = .15;
  private double percentSeniorInDiscount = .4;
  private double percentGrabAndGo = 0.4;

  private LocalTime lunchStart = LocalTime.of(12,0);
  private LocalTime lunchEnd  = LocalTime.of(13,0);
  private LocalTime dinnerStart = LocalTime.of(17,0);
  private LocalTime dinnerEnd  = LocalTime.of(18,30);
  private LocalTime seniorStartTime =  LocalTime.of(10,0);
  private LocalTime seniorEndTime  = LocalTime.of(12,0);
  private DayOfWeek seniorDay = DayOfWeek.TUESDAY;


  @Before
  public void setup() throws IOException {

    customerArrivalModelFactors = (new CustomerArrivalFactorsFromFile(getTrafficFactors())).
        geyCustomerArrivalModelFactors();

    saturday = LocalDate.of(2020, 05, 23);
    sunday = LocalDate.of(2020, 05, 24);
    monday = LocalDate.of(2020, 05, 25);
    tuesday = LocalDate.of(2020, 05, 26);
    wednesday = LocalDate.of(2020, 05, 27);
    thursday = LocalDate.of(2020, 05, 28);
    friday = LocalDate.of(2020, 05, 29);

    nonHoliday = new IHolidayCalendar() {
      @Override
      public HolidayTreatment getHoliday(LocalDate dateOfEntry) {
        return HolidayTreatment.NonHoliday;
      }
    };

    holiday = new IHolidayCalendar() {
      @Override
      public HolidayTreatment getHoliday(LocalDate dateOfEntry) {
        return HolidayTreatment.Holiday;
      }
    };

    weekBeforeHoliday = new IHolidayCalendar() {
      @Override
      public HolidayTreatment getHoliday(LocalDate dateOfEntry) {
        return HolidayTreatment.WeekBeforeHoliday;
      }
    };

    unknownForecast = new IWeatherForecast() {
      @Override
      public WeatherForecastType getForecast(LocalDate date) {
        return WeatherForecastType.Unknown;
      }
    };

    reallyNiceForecast = new IWeatherForecast() {
      @Override
      public WeatherForecastType getForecast(LocalDate date) {
        return WeatherForecastType.ReallyNice;
      }
    };
  }

  /**
   * count the number of customer arrivals in any model.
   *
   * @param customerArrivals the model
   * @return the number of customer arrivals.
   */
  private int countArrivalsInModel(ICustomerArrivalModel customerArrivals) {

    int i = 0;
    while (customerArrivals.hasNext()) {
      customerArrivals.next();
      i++;
    }

    return i;
  }

  /**
   * count the number of customer arrivals in any model.
   *
   * @param customerArrivals the model
   * @return the number of customer arrivals.
   */
  private int countArrivalsInModel(ICustomerArrivalModel customerArrivals,
      WeatherForecastType forecast, CustomerType countType) throws IOException {

    CustomerMixModelFactors customerMixModelFactors =
        (new CustomerMixModelFactorsFromFile(getTrafficFactors())).getCustomerMixModelFactors();

    ICustomerMixModel mix = new CustomerMixModel(customerMixModelFactors);

    int i = 0;
    while (customerArrivals.hasNext()) {
      LocalDateTime time = customerArrivals.next();
      CustomerType thisType = mix.next(forecast, time);
      if (thisType == countType) {
        i++;
      }
    }

    return i;
  }


  @Test
  public void testSaturdayUnknownForecastNonHoliday() {
    ICustomerArrivalModel entryIntoStoreModel = new CustomerArrivalModel(customerArrivalModelFactors,
        nonHoliday, unknownForecast);

    entryIntoStoreModel.setDateRange(saturday, saturday);
    assertTrue(entryIntoStoreModel.hasNext());
    assertEquals(4000, countArrivalsInModel(entryIntoStoreModel));
  }

  @Test
  public void testSundayUnknownForecastNonHoliday() {
    ICustomerArrivalModel entryIntoStoreModel = new CustomerArrivalModel(customerArrivalModelFactors,
        nonHoliday, unknownForecast);
    entryIntoStoreModel.setDateRange(sunday, sunday);
    assertTrue(entryIntoStoreModel.hasNext());
    assertEquals(5000, countArrivalsInModel(entryIntoStoreModel));
  }

  @Test
  public void testMondayUnknownForecastNonHoliday() {
    ICustomerArrivalModel entryIntoStoreModel = new CustomerArrivalModel(customerArrivalModelFactors,
        nonHoliday, unknownForecast);
    entryIntoStoreModel.setDateRange(monday, monday);

    assertTrue(entryIntoStoreModel.hasNext());
    assertEquals(800, countArrivalsInModel(entryIntoStoreModel));
  }

  @Test
  public void testTuesdayUnknownForecastNonHoliday() {
    ICustomerArrivalModel entryIntoStoreModel = new CustomerArrivalModel(customerArrivalModelFactors,
        nonHoliday, unknownForecast);
    entryIntoStoreModel.setDateRange(tuesday, tuesday);
    assertTrue(entryIntoStoreModel.hasNext());
    assertEquals(1000, countArrivalsInModel(entryIntoStoreModel));
  }

  @Test
  public void testWednesdayUnknownForecastNonHoliday() {
    ICustomerArrivalModel entryIntoStoreModel = new CustomerArrivalModel(customerArrivalModelFactors,
        nonHoliday, unknownForecast);
    entryIntoStoreModel.setDateRange(wednesday, wednesday);
    assertTrue(entryIntoStoreModel.hasNext());
    assertEquals(1200, countArrivalsInModel(entryIntoStoreModel));
  }

  @Test
  public void testThursdayUnknownForecastNonHoliday() {
    ICustomerArrivalModel entryIntoStoreModel = new CustomerArrivalModel(customerArrivalModelFactors,
        nonHoliday, unknownForecast);
    entryIntoStoreModel.setDateRange(thursday, thursday);
    assertTrue(entryIntoStoreModel.hasNext());
    assertEquals(900, countArrivalsInModel(entryIntoStoreModel));
  }

  @Test
  public void testFridayUnknownForecastNonHoliday() {
    ICustomerArrivalModel entryIntoStoreModel = new CustomerArrivalModel(customerArrivalModelFactors,
        nonHoliday, unknownForecast);
    entryIntoStoreModel.setDateRange(friday, friday);
    assertTrue(entryIntoStoreModel.hasNext());
    assertEquals(2500, countArrivalsInModel(entryIntoStoreModel));
  }

  @Test
  public void testSaturdayThroughFridayUnknownForecastNonHoliday() {
    ICustomerArrivalModel entryIntoStoreModel = new CustomerArrivalModel(customerArrivalModelFactors,
        nonHoliday, unknownForecast);
    entryIntoStoreModel.setDateRange(saturday, friday);
    assertTrue(entryIntoStoreModel.hasNext());
    assertEquals(15400, countArrivalsInModel(entryIntoStoreModel));
  }


  //Test Holiday
  @Test
  public void testSaturdayUnknownForecastHoliday() {
    ICustomerArrivalModel entryIntoStoreModel = new CustomerArrivalModel(customerArrivalModelFactors,
        holiday, unknownForecast);
    entryIntoStoreModel.setDateRange(saturday, saturday);
    assertTrue(entryIntoStoreModel.hasNext());
    assertEquals(800, countArrivalsInModel(entryIntoStoreModel));
  }

  @Test
  public void testSundayUnknownForecastHoliday() {
    ICustomerArrivalModel entryIntoStoreModel = new CustomerArrivalModel(customerArrivalModelFactors,
        holiday, unknownForecast);
    entryIntoStoreModel.setDateRange(sunday, sunday);
    assertTrue(entryIntoStoreModel.hasNext());
    assertEquals(1000, countArrivalsInModel(entryIntoStoreModel));
  }

  @Test
  public void testMondayUnknownForecastHoliday() {
    ICustomerArrivalModel entryIntoStoreModel = new CustomerArrivalModel(customerArrivalModelFactors,
        holiday, unknownForecast);
    entryIntoStoreModel.setDateRange(monday, monday);
    assertTrue(entryIntoStoreModel.hasNext());
    assertEquals(160, countArrivalsInModel(entryIntoStoreModel));
  }

  @Test
  public void testTuesdayUnknownForecastHoliday() {
    ICustomerArrivalModel entryIntoStoreModel = new CustomerArrivalModel(customerArrivalModelFactors,
        holiday, unknownForecast);
    entryIntoStoreModel.setDateRange(tuesday, tuesday);
    assertTrue(entryIntoStoreModel.hasNext());
    assertEquals(200, countArrivalsInModel(entryIntoStoreModel));
  }

  @Test
  public void testWednesdayUnknownForecastHoliday() {
    ICustomerArrivalModel entryIntoStoreModel = new CustomerArrivalModel(customerArrivalModelFactors,
        holiday, unknownForecast);
    entryIntoStoreModel.setDateRange(wednesday, wednesday);
    assertTrue(entryIntoStoreModel.hasNext());
    assertEquals(240, countArrivalsInModel(entryIntoStoreModel));
  }

  @Test
  public void testThursdayUnknownForecastHoliday() {
    ICustomerArrivalModel entryIntoStoreModel = new CustomerArrivalModel(customerArrivalModelFactors,
        holiday, unknownForecast);
    entryIntoStoreModel.setDateRange(thursday, thursday);
    assertTrue(entryIntoStoreModel.hasNext());
    assertEquals(180, countArrivalsInModel(entryIntoStoreModel));
  }

  @Test
  public void testFridayUnknownForecastHoliday() {
    ICustomerArrivalModel entryIntoStoreModel = new CustomerArrivalModel(customerArrivalModelFactors,
        holiday, unknownForecast);
    entryIntoStoreModel.setDateRange(friday, friday);
    assertTrue(entryIntoStoreModel.hasNext());
    assertEquals(500, countArrivalsInModel(entryIntoStoreModel));
  }

  @Test
  public void testSaturdayThroughFridayUnknownForecastHoliday() {
    ICustomerArrivalModel entryIntoStoreModel = new CustomerArrivalModel(customerArrivalModelFactors,
        holiday, unknownForecast);
    entryIntoStoreModel.setDateRange(saturday, friday);
    assertTrue(entryIntoStoreModel.hasNext());
    assertEquals(3080, countArrivalsInModel(entryIntoStoreModel));
  }

  //Test WeekBeforeHoliday
  @Test
  public void testSaturdayUnknownForecastWeekBeforeHoliday() {
    ICustomerArrivalModel entryIntoStoreModel = new CustomerArrivalModel(customerArrivalModelFactors,
        weekBeforeHoliday, unknownForecast);
    entryIntoStoreModel.setDateRange(saturday, saturday);
    assertTrue(entryIntoStoreModel.hasNext());
    assertEquals(4600, countArrivalsInModel(entryIntoStoreModel));
  }

  @Test
  public void testSundayUnknownForecastWeekBeforeHoliday() {
    ICustomerArrivalModel entryIntoStoreModel = new CustomerArrivalModel(customerArrivalModelFactors,
        weekBeforeHoliday, unknownForecast);
    entryIntoStoreModel.setDateRange(sunday, sunday);
    assertTrue(entryIntoStoreModel.hasNext());
    assertEquals(5750, countArrivalsInModel(entryIntoStoreModel));
  }

  @Test
  public void testMondayUnknownForecastWeekBeforeHoliday() {
    ICustomerArrivalModel entryIntoStoreModel = new CustomerArrivalModel(customerArrivalModelFactors,
        weekBeforeHoliday, unknownForecast);
    entryIntoStoreModel.setDateRange(monday, monday);
    assertTrue(entryIntoStoreModel.hasNext());
    assertEquals(920, countArrivalsInModel(entryIntoStoreModel));
  }

  @Test
  public void testTuesdayUnknownForecastWeekBeforeHoliday() {
    ICustomerArrivalModel entryIntoStoreModel = new CustomerArrivalModel(customerArrivalModelFactors,
        weekBeforeHoliday, unknownForecast);
    entryIntoStoreModel.setDateRange(tuesday, tuesday);
    assertTrue(entryIntoStoreModel.hasNext());
    assertEquals(1150, countArrivalsInModel(entryIntoStoreModel));
  }

  @Test
  public void testWednesdayUnknownForecastWeekBeforeHoliday() {
    ICustomerArrivalModel entryIntoStoreModel = new CustomerArrivalModel(customerArrivalModelFactors,
        weekBeforeHoliday, unknownForecast);
    entryIntoStoreModel.setDateRange(wednesday, wednesday);
    assertTrue(entryIntoStoreModel.hasNext());
    assertEquals(1380, countArrivalsInModel(entryIntoStoreModel));
  }

  @Test
  public void testThursdayUnknownForecastWeekBeforeHoliday() {
    ICustomerArrivalModel entryIntoStoreModel = new CustomerArrivalModel(customerArrivalModelFactors,
        weekBeforeHoliday, unknownForecast);
    entryIntoStoreModel.setDateRange(thursday, thursday);
    assertTrue(entryIntoStoreModel.hasNext());
    assertEquals(1035, countArrivalsInModel(entryIntoStoreModel));
  }

  @Test
  public void testFridayUnknownForecastWeekBeforeHoliday() {
    ICustomerArrivalModel entryIntoStoreModel = new CustomerArrivalModel(customerArrivalModelFactors,
        weekBeforeHoliday, unknownForecast);
    entryIntoStoreModel.setDateRange(friday, friday);
    assertTrue(entryIntoStoreModel.hasNext());
    assertEquals(2875, countArrivalsInModel(entryIntoStoreModel));
  }

  @Test
  public void testSaturdayThroughFridayUnknownForecastWeekBeforeHoliday() {
    ICustomerArrivalModel entryIntoStoreModel = new CustomerArrivalModel(customerArrivalModelFactors,
        weekBeforeHoliday, unknownForecast);
    entryIntoStoreModel.setDateRange(saturday, friday);
    assertTrue(entryIntoStoreModel.hasNext());
    assertEquals(17710, countArrivalsInModel(entryIntoStoreModel));
  }


  ////////////
  // Grab and Go traffic
  ///////////
  @Test
  public void testSaturdayReallyNiceForecastNonHoliday() {
    ICustomerArrivalModel entryIntoStoreModel = new CustomerArrivalModel(customerArrivalModelFactors,
        nonHoliday, reallyNiceForecast);
    entryIntoStoreModel.setDateRange(saturday, saturday);
    assertTrue(entryIntoStoreModel.hasNext());
    assertEquals(5600, countArrivalsInModel(entryIntoStoreModel));
  }

  @Test
  public void testSaturdayReallyNiceForecastNonHolidayGrabAndGoCount() throws IOException {

    // assert that the extra 40% traffic from a typical saturday is grab and go...
    ICustomerArrivalModel entryIntoStoreModel = new CustomerArrivalModel(customerArrivalModelFactors,
        nonHoliday, reallyNiceForecast);
    entryIntoStoreModel.setDateRange(saturday, saturday);
    assertTrue(entryIntoStoreModel.hasNext());
    int grabAndGoActualCount = countArrivalsInModel(entryIntoStoreModel,
        WeatherForecastType.ReallyNice, CustomerType.GrabAndGo);

    int grabAndGoExpectedAverageCount = 1600;
    double difference = Math.abs(grabAndGoExpectedAverageCount - grabAndGoActualCount);

    assertTrue((difference / grabAndGoExpectedAverageCount) < 0.05);
  }


  @Test
  public void testSundayReallyNiceForecastNonHoliday() {
    ICustomerArrivalModel entryIntoStoreModel = new CustomerArrivalModel(customerArrivalModelFactors,
        nonHoliday, reallyNiceForecast);
    entryIntoStoreModel.setDateRange(sunday, sunday);
    assertTrue(entryIntoStoreModel.hasNext());
    assertEquals(7000, countArrivalsInModel(entryIntoStoreModel));
  }

  @Test
  public void testSaturdayReallyNiceForecastHoliday() {
    ICustomerArrivalModel entryIntoStoreModel = new CustomerArrivalModel(customerArrivalModelFactors,
        holiday, reallyNiceForecast);
    entryIntoStoreModel.setDateRange(saturday, saturday);
    assertTrue(entryIntoStoreModel.hasNext());
    assertEquals(1120, countArrivalsInModel(entryIntoStoreModel));
  }

  @Test
  public void testSundayReallyNiceForecastHoliday() {
    ICustomerArrivalModel entryIntoStoreModel = new CustomerArrivalModel(customerArrivalModelFactors,
        holiday, reallyNiceForecast);
    entryIntoStoreModel.setDateRange(sunday, sunday);
    assertTrue(entryIntoStoreModel.hasNext());
    assertEquals(1400, countArrivalsInModel(entryIntoStoreModel));
  }

  @Test
  public void testSaturdayReallyNiceForecastWeekBeforeHoliday() {
    ICustomerArrivalModel entryIntoStoreModel = new CustomerArrivalModel(customerArrivalModelFactors,
        weekBeforeHoliday, reallyNiceForecast);
    entryIntoStoreModel.setDateRange(saturday, saturday);
    assertTrue(entryIntoStoreModel.hasNext());
    assertEquals(6440, countArrivalsInModel(entryIntoStoreModel));
  }

  @Test
  public void testSundayReallyNiceForecastWeekBeforeHoliday() {
    ICustomerArrivalModel entryIntoStoreModel = new CustomerArrivalModel(customerArrivalModelFactors,
        weekBeforeHoliday, reallyNiceForecast);
    entryIntoStoreModel.setDateRange(sunday, sunday);
    assertTrue(entryIntoStoreModel.hasNext());
    assertEquals(8050, countArrivalsInModel(entryIntoStoreModel));
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
            + "SeniorDayOfWeek,Tuesday" + System.lineSeparator()
            + "DinnerStartTime,17:00" + System.lineSeparator()
            + "DinnerEndTime,18:30" + System.lineSeparator()
            + "LunchStartTime,12:00" + System.lineSeparator()
            + "LunchEndTime,13:00" + System.lineSeparator()
            + "StoreOpenTime,06:00" + System.lineSeparator()
            + "StoreCloseTime,21:00";

    return new StringReader(csv);
  }
}