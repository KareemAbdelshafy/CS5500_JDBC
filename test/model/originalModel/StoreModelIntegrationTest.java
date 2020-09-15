package model.originalModel;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.LocalDateTime;

import model.CustomerType;
import model.CustomerVisit;
import model.IStoreModel;
import model.factors.CustomerArrivalFactorsFromFile;
import model.factors.CustomerArrivalModelFactors;
import model.factors.CustomerMixModelFactors;
import model.factors.CustomerMixModelFactorsFromFile;
import model.factors.IHolidayCalendar;
import model.factors.IWeatherForecast;
import model.factors.TimeInStoreFactors;
import model.factors.TimeInStoreFactorsFromFile;
import org.junit.Before;
import org.junit.Test;

/**
 * Integration test of StoreModel across the CustomerArrivalModel, TimeInStoreModel
 * and CustomerMixModel.
 */
public class StoreModelIntegrationTest {

  IStoreModel storeModel;
  IStoreModel storeModelGoodWeather;
  CustomerMixModelFactors customerMixModelFactors;

  @Before
  public void setUp() throws Exception {
    IWeatherForecast weatherForecastUnknown = new IWeatherForecast() {
      @Override
      public WeatherForecastType getForecast(LocalDate date) {
        return WeatherForecastType.Unknown;
      }
    };

    IWeatherForecast weatherForecastNice = new IWeatherForecast() {
      @Override
      public WeatherForecastType getForecast(LocalDate date) {
        return WeatherForecastType.ReallyNice;
      }
    };

    IHolidayCalendar holidayCalendarNonHoliday = new IHolidayCalendar() {
      @Override
      public HolidayTreatment getHoliday(LocalDate dateOfEntry) {
        return HolidayTreatment.NonHoliday;
      }
    };

    CustomerArrivalModelFactors customerArrivalModelFactors = (new CustomerArrivalFactorsFromFile(getTrafficFactors())).
        geyCustomerArrivalModelFactors();

    TimeInStoreFactors timeInStoreFactors =
        (new TimeInStoreFactorsFromFile(getTrafficFactors())).getTimeInStoreFactors();

    customerMixModelFactors =
        (new CustomerMixModelFactorsFromFile(getTrafficFactors())).getCustomerMixModelFactors();

    StoreModelBuilder builder = new StoreModelBuilder();
    storeModel = builder.
        setWeatherForecast(weatherForecastUnknown).
        setHolidayCalendar(holidayCalendarNonHoliday).
        setCustomerArrivalModelFactors(customerArrivalModelFactors).
        setTimeInStoreFactors(timeInStoreFactors).
        setCustomerMixModelFactors(customerMixModelFactors).
        setScenarioID(0).
        build();

    StoreModelBuilder builder2 = new StoreModelBuilder();
    storeModelGoodWeather = builder2.
        setWeatherForecast(weatherForecastNice).
        setHolidayCalendar(holidayCalendarNonHoliday).
        setCustomerArrivalModelFactors(customerArrivalModelFactors).
        setTimeInStoreFactors(timeInStoreFactors).
        setCustomerMixModelFactors(customerMixModelFactors).
        setScenarioID(0).
        build();
  }

  private int[] countArrivalsInModel(IStoreModel storeModel, LocalDateTime from, LocalDateTime to) {
    int grabAndGo = 0;
    int lunch = 0;
    int dinner = 0;
    int seniors = 0;
    int nonSenior = 0;
    //CustomerMixModel customerMixModel = new CustomerMixModel();

    while (storeModel.hasNext()) {
      CustomerVisit customer = storeModel.next();
      if (customer.getArrivalTime().isBefore(to) && customer.getArrivalTime().isAfter(from)
          && customer.getCustomerType().equals(CustomerType.GrabAndGo)) {
        grabAndGo++;
      } else if (customer.getArrivalTime().isBefore(to) && customer.getArrivalTime()
          .isAfter(from)
          && customer.getCustomerType().equals(CustomerType.Lunch)) {
        lunch++;
      } else if (customer.getArrivalTime().isBefore(to) && customer.getArrivalTime()
          .isAfter(from)
          && customer.getCustomerType().equals(CustomerType.Dinner)) {
        dinner++;
      } else if (customer.getArrivalTime().isBefore(to) && customer.getArrivalTime()
          .isAfter(from)
          && customer.getCustomerType().equals(CustomerType.Senior)) {
        seniors++;
      } else if (customer.getArrivalTime().isBefore(to) && customer.getArrivalTime()
          .isAfter(from)
          && customer.getCustomerType().equals(CustomerType.NonSenior)) {
        nonSenior++;
      }
    }
    int[] ansList = {grabAndGo, lunch, dinner, seniors, nonSenior};
    return ansList;
  }

  @Test
  public void testTuesdayLunchNiceWeather() {
    LocalDate tuesday = LocalDate.of(2020, 05, 26);
    storeModelGoodWeather.generateForecast(tuesday, tuesday);

    LocalDateTime start = LocalDateTime.of(2020, 05, 26, 12, 0);
    LocalDateTime end = LocalDateTime.of(2020, 05, 26, 13, 0);
    int[] ansList = countArrivalsInModel(storeModelGoodWeather, start, end);
    int allShopperAmount = ansList[0] + ansList[1] + ansList[2] + ansList[3] + ansList[4];
    System.out
        .println("GrabAndGo:" + ansList[0] + " Lunch:" + ansList[1] + "  Dinner:" + ansList[2] +
            "  Senior: " + ansList[3] + "  NonSenior:" + ansList[4] + "  All:" + allShopperAmount);

    double grabGoPercentage = (ansList[0] * 1.0) / allShopperAmount;
    double lunchPercentage = (ansList[1] * 1.0 / allShopperAmount);
    double dinnerPercentage = (ansList[2] * 1.0 / allShopperAmount);
    double seniorPercentage = (ansList[3] * 1.0 / (ansList[4] + ansList[3]));

    assertEquals(0.0, dinnerPercentage, 0.0);
    assertEquals(customerMixModelFactors.getLunchRushFactor(), lunchPercentage, 0.1);
    //test grabAndGo percentage and seniorPercentage
    assertEquals(0.0, grabGoPercentage, 0.0);
    assertEquals(customerMixModelFactors.getSeniorDemographicPercent(), seniorPercentage, 0.1);
  }

  @Test
  public void testTuesdayDinnerNiceWeather() {
    LocalDate tuesday = LocalDate.of(2020, 05, 26);
    storeModelGoodWeather.generateForecast(tuesday, tuesday);

    LocalDateTime start = LocalDateTime.of(2020, 05, 26, 17, 0);
    LocalDateTime end = LocalDateTime.of(2020, 05, 26, 18, 30);
    int[] ansList = countArrivalsInModel(storeModelGoodWeather, start, end);
    int allShopperAmount = ansList[0] + ansList[1] + ansList[2] + ansList[3] + ansList[4];
    System.out
        .println("GrabAndGo:" + ansList[0] + " Lunch:" + ansList[1] + "  Dinner:" + ansList[2] +
            "  Senior: " + ansList[3] + "  NonSenior:" + ansList[4] + "  All:" + allShopperAmount);

    double grabGoPercentage = (ansList[0] * 1.0) / allShopperAmount;
    double lunchPercentage = (ansList[1] * 1.0 / allShopperAmount);
    double dinnerPercentage = (ansList[2] * 1.0 / allShopperAmount);
    double seniorPercentage = (ansList[3] * 1.0 / (ansList[4] + ansList[3]));

    assertEquals(customerMixModelFactors.getDinnerRushFactor(), dinnerPercentage, 0.06);
    assertEquals(0.0, lunchPercentage, 0.0);
    //test grabAndGo percentage and seniorPercentage
    assertEquals(0.0, grabGoPercentage, 0.0);
    assertEquals(customerMixModelFactors.getSeniorDemographicPercent(), seniorPercentage, 0.1);
  }

  @Test
  public void testTuesdaySeniorDiscountNiceWeather() {
    LocalDate tuesday = LocalDate.of(2020, 05, 26);
    storeModelGoodWeather.generateForecast(tuesday, tuesday);

    LocalDateTime start = LocalDateTime.of(2020, 05, 26, 10, 0);
    LocalDateTime end = LocalDateTime.of(2020, 05, 26, 12, 0);
    int[] ansList = countArrivalsInModel(storeModelGoodWeather, start, end);
    int allShopperAmount = ansList[0] + ansList[1] + ansList[2] + ansList[3] + ansList[4];
    System.out
        .println("GrabAndGo:" + ansList[0] + " Lunch:" + ansList[1] + "  Dinner:" + ansList[2] +
            "  Senior: " + ansList[3] + "  NonSenior:" + ansList[4] + "  All" + allShopperAmount);

    double grabGoPercentage = (ansList[0] * 1.0) / allShopperAmount;
    double lunchPercentage = (ansList[1] * 1.0 / allShopperAmount);
    double dinnerPercentage = (ansList[2] * 1.0 / allShopperAmount);
    double seniorPercentage = (ansList[3] * 1.0 / (ansList[4] + ansList[3]));

    assertEquals(0.0, dinnerPercentage, 0.0);
    assertEquals(0.0, lunchPercentage, 0.0);
    //test grabAndGo percentage and seniorPercentage
    assertEquals(0.0, grabGoPercentage, 0.0);
    assertEquals(customerMixModelFactors.getSeniorDemographicPercent(), seniorPercentage, 0.1);
  }

  @Test
  public void testTuesdayRegularHourNiceWeather() {
    LocalDate tuesday = LocalDate.of(2020, 05, 26);
    storeModelGoodWeather.generateForecast(tuesday, tuesday);

    LocalDateTime start = LocalDateTime.of(2020, 05, 26, 6, 0);
    LocalDateTime end = LocalDateTime.of(2020, 05, 26, 7, 0);
    int[] ansList = countArrivalsInModel(storeModelGoodWeather, start, end);
    int allShopperAmount = ansList[0] + ansList[1] + ansList[2] + ansList[3] + ansList[4];
    System.out
        .println("GrabAndGo:" + ansList[0] + " Lunch:" + ansList[1] + "  Dinner:" + ansList[2] +
            "  Senior: " + ansList[3] + "  NonSenior:" + ansList[4] + "  All:" + allShopperAmount);

    double grabGoPercentage = (ansList[0] * 1.0) / allShopperAmount;
    double lunchPercentage = (ansList[1] * 1.0 / allShopperAmount);
    double dinnerPercentage = (ansList[2] * 1.0 / allShopperAmount);
    double seniorPercentage = (ansList[3] * 1.0 / (ansList[4] + ansList[3]));

    assertEquals(0.0, dinnerPercentage, 0.0);
    assertEquals(0.0, lunchPercentage, 0.0);
    //test grabAndGo percentage and seniorPercentage
    assertEquals(0.0, grabGoPercentage, 0.0);
    assertEquals(customerMixModelFactors.getSeniorDemographicPercent(), seniorPercentage, 0.1);
  }


  @Test
  public void testTuesdayLunch() {
    LocalDate tuesday = LocalDate.of(2020, 05, 26);
    storeModel.generateForecast(tuesday, tuesday);

    LocalDateTime start = LocalDateTime.of(2020, 05, 26, 12, 0);
    LocalDateTime end = LocalDateTime.of(2020, 05, 26, 13, 0);
    int[] ansList = countArrivalsInModel(storeModel, start, end);
    int allShopperAmount = ansList[0] + ansList[1] + ansList[2] + ansList[3] + ansList[4];
    System.out
        .println("GrabAndGo:" + ansList[0] + " Lunch:" + ansList[1] + "  Dinner:" + ansList[2] +
            "  Senior: " + ansList[3] + "  NonSenior:" + ansList[4] + "  All:" + allShopperAmount);

    double grabGoPercentage = (ansList[0] * 1.0) / allShopperAmount;
    double lunchPercentage = (ansList[1] * 1.0 / allShopperAmount);
    double dinnerPercentage = (ansList[2] * 1.0 / allShopperAmount);
    double seniorPercentage = (ansList[3] * 1.0 / (ansList[4] + ansList[3]));

    assertEquals(0.0, dinnerPercentage, 0.0);
    assertEquals(customerMixModelFactors.getLunchRushFactor(), lunchPercentage, 0.1);
    //test grabAndGo percentage and seniorPercentage
    assertEquals(0.0, grabGoPercentage, 0.0);
    assertEquals(customerMixModelFactors.getSeniorDemographicPercent(), seniorPercentage, 0.1);
  }

  @Test
  public void testTuesdayDinner() {
    LocalDate tuesday = LocalDate.of(2020, 05, 26);
    storeModel.generateForecast(tuesday, tuesday);

    LocalDateTime start = LocalDateTime.of(2020, 05, 26, 17, 0);
    LocalDateTime end = LocalDateTime.of(2020, 05, 26, 18, 30);
    int[] ansList = countArrivalsInModel(storeModel, start, end);
    int allShopperAmount = ansList[0] + ansList[1] + ansList[2] + ansList[3] + ansList[4];
    System.out
        .println("GrabAndGo:" + ansList[0] + " Lunch:" + ansList[1] + "  Dinner:" + ansList[2] +
            "  Senior: " + ansList[3] + "  NonSenior:" + ansList[4] + "  All:" + allShopperAmount);

    double grabGoPercentage = (ansList[0] * 1.0) / allShopperAmount;
    double lunchPercentage = (ansList[1] * 1.0 / allShopperAmount);
    double dinnerPercentage = (ansList[2] * 1.0 / allShopperAmount);
    double seniorPercentage = (ansList[3] * 1.0 / (ansList[4] + ansList[3]));

    assertEquals(customerMixModelFactors.getDinnerRushFactor(), dinnerPercentage, 0.05);
    assertEquals(0.0, lunchPercentage, 0.0);
    //test grabAndGo percentage and seniorPercentage
    assertEquals(0.0, grabGoPercentage, 0.0);
    assertEquals(customerMixModelFactors.getSeniorDemographicPercent(), seniorPercentage, 0.1);
  }

  @Test
  public void testTuesdaySeniorDiscount() {
    LocalDate tuesday = LocalDate.of(2020, 05, 26);
    storeModel.generateForecast(tuesday, tuesday);

    LocalDateTime start = LocalDateTime.of(2020, 05, 26, 10, 0);
    LocalDateTime end = LocalDateTime.of(2020, 05, 26, 12, 0);
    int[] ansList = countArrivalsInModel(storeModel, start, end);
    int allShopperAmount = ansList[0] + ansList[1] + ansList[2] + ansList[3] + ansList[4];
    System.out
        .println("GrabAndGo:" + ansList[0] + " Lunch:" + ansList[1] + "  Dinner:" + ansList[2] +
            "  Senior: " + ansList[3] + "  NonSenior:" + ansList[4] + "  All" + allShopperAmount);

    double grabGoPercentage = (ansList[0] * 1.0) / allShopperAmount;
    double lunchPercentage = (ansList[1] * 1.0 / allShopperAmount);
    double dinnerPercentage = (ansList[2] * 1.0 / allShopperAmount);
    double seniorPercentage = (ansList[3] * 1.0 / (ansList[4] + ansList[3]));

    assertEquals(0.0, dinnerPercentage, 0.0);
    assertEquals(0.0, lunchPercentage, 0.0);
    //test grabAndGo percentage and seniorPercentage
    assertEquals(0.0, grabGoPercentage, 0.0);
    assertEquals(customerMixModelFactors.getSeniorDemographicPercent(), seniorPercentage, 0.1);
  }

  @Test
  public void testTuesdayRegularHour() {
    LocalDate tuesday = LocalDate.of(2020, 05, 26);
    storeModel.generateForecast(tuesday, tuesday);

    LocalDateTime start = LocalDateTime.of(2020, 05, 26, 6, 0);
    LocalDateTime end = LocalDateTime.of(2020, 05, 26, 7, 0);
    int[] ansList = countArrivalsInModel(storeModel, start, end);
    int allShopperAmount = ansList[0] + ansList[1] + ansList[2] + ansList[3] + ansList[4];
    System.out
        .println("GrabAndGo:" + ansList[0] + " Lunch:" + ansList[1] + "  Dinner:" + ansList[2] +
            "  Senior: " + ansList[3] + "  NonSenior:" + ansList[4] + "  All:" + allShopperAmount);

    double grabGoPercentage = (ansList[0] * 1.0) / allShopperAmount;
    double lunchPercentage = (ansList[1] * 1.0 / allShopperAmount);
    double dinnerPercentage = (ansList[2] * 1.0 / allShopperAmount);
    double seniorPercentage = (ansList[3] * 1.0 / (ansList[4] + ansList[3]));

    assertEquals(0.0, dinnerPercentage, 0.0);
    assertEquals(0.0, lunchPercentage, 0.0);
    //test grabAndGo percentage and seniorPercentage
    assertEquals(0.0, grabGoPercentage, 0.0);
    assertEquals(customerMixModelFactors.getSeniorDemographicPercent(), seniorPercentage, 0.1);
  }


  @Test
  public void testSaturdayLunchWeatherUnknown() {
    LocalDate saturday = LocalDate.of(2020, 05, 30);
    storeModel.generateForecast(saturday, saturday);

    LocalDateTime start = LocalDateTime.of(2020, 05, 30, 12, 0);
    LocalDateTime end = LocalDateTime.of(2020, 05, 30, 13, 0);
    int[] ansList = countArrivalsInModel(storeModel, start, end);
    int allShopperAmount = ansList[0] + ansList[1] + ansList[2] + ansList[3] + ansList[4];
    System.out
        .println("GrabAndGo:" + ansList[0] + " Lunch:" + ansList[1] + "  Dinner:" + ansList[2] +
            "  Senior: " + ansList[3] + "  NonSenior:" + ansList[4] + "  All:" + allShopperAmount);

    double grabGoPercentage = (ansList[0] * 1.0) / allShopperAmount;
    double seniorPercentage = (ansList[3] * 1.0 / (ansList[4] + ansList[3]));
    double dinnerPercentage = (ansList[1] * 1.0 / allShopperAmount);
    double lunchPercentage = (ansList[2] * 1.0 / allShopperAmount);

    assertEquals(0.0, dinnerPercentage, 0.0);
    assertEquals(0.0, lunchPercentage, 0.0);
    //test grabAndGo percentage and seniorPercentage
    assertEquals(0.0, grabGoPercentage, 0.1);
    assertEquals(customerMixModelFactors.getSeniorDemographicPercent(), seniorPercentage, 0.1);
  }


  @Test
  public void testSaturdayDinnerWeatherUnknown() {
    LocalDate saturday = LocalDate.of(2020, 05, 30);
    storeModel.generateForecast(saturday, saturday);

    LocalDateTime start = LocalDateTime.of(2020, 05, 30, 17, 0);
    LocalDateTime end = LocalDateTime.of(2020, 05, 30, 18, 30);
    int[] ansList = countArrivalsInModel(storeModel, start, end);
    int allShopperAmount = ansList[0] + ansList[1] + ansList[2] + ansList[3] + ansList[4];
    System.out
        .println("GrabAndGo:" + ansList[0] + " Lunch:" + ansList[1] + "  Dinner:" + ansList[2] +
            "  Senior: " + ansList[3] + "  NonSenior:" + ansList[4] + "  All:" + allShopperAmount);

    double grabGoPercentage = (ansList[0] * 1.0) / allShopperAmount;
    double seniorPercentage = (ansList[3] * 1.0 / (ansList[4] + ansList[3]));
    double dinnerPercentage = (ansList[1] * 1.0 / allShopperAmount);
    double lunchPercentage = (ansList[2] * 1.0 / allShopperAmount);

    //weekend no lunch and dinner traffic
    assertEquals(0.0, dinnerPercentage, 0.0);
    assertEquals(0.0, lunchPercentage, 0.0);
    //test grabAndGo percentage and seniorPercentage
    assertEquals(0.0, grabGoPercentage, 0.1);
    assertEquals(customerMixModelFactors.getSeniorDemographicPercent(), seniorPercentage, 0.1);
  }

  @Test
  public void testSaturdayLunchNiceWeather() {
    LocalDate saturday = LocalDate.of(2020, 05, 30);
    storeModelGoodWeather.generateForecast(saturday, saturday);

    LocalDateTime start = LocalDateTime.of(2020, 05, 30, 12, 0);
    LocalDateTime end = LocalDateTime.of(2020, 05, 30, 13, 0);
    int[] ansList = countArrivalsInModel(storeModelGoodWeather, start, end);
    int allShopperAmount = ansList[0] + ansList[1] + ansList[2] + ansList[3] + ansList[4];
    System.out
        .println("GrabAndGo:" + ansList[0] + " Lunch:" + ansList[1] + "  Dinner:" + ansList[2] +
            "  Senior: " + ansList[3] + "  NonSenior:" + ansList[4] + "  All:" + allShopperAmount);

    double grabGoPercentage = (ansList[0] * 1.0) / allShopperAmount;
    double seniorPercentage = (ansList[3] * 1.0 / (ansList[4] + ansList[3]));
    double dinnerPercentage = (ansList[1] * 1.0 / allShopperAmount);
    double lunchPercentage = (ansList[2] * 1.0 / allShopperAmount);

    assertEquals(0.0, dinnerPercentage, 0.0);
    assertEquals(0.0, lunchPercentage, 0.0);
    //test grabAndGo percentage and seniorPercentage
    assertEquals(grabGoPercentage, customerMixModelFactors.getGrabAndGoFactor(), 0.1);
    assertEquals(seniorPercentage, customerMixModelFactors.getSeniorDemographicPercent(), 0.1);
  }

  @Test
  public void testSaturdayDinnerNiceWeather() {
    LocalDate saturday = LocalDate.of(2020, 05, 30);
    storeModelGoodWeather.generateForecast(saturday, saturday);

    LocalDateTime start = LocalDateTime.of(2020, 05, 30, 17, 0);
    LocalDateTime end = LocalDateTime.of(2020, 05, 30, 18, 30);
    int[] ansList = countArrivalsInModel(storeModelGoodWeather, start, end);
    int allShopperAmount = ansList[0] + ansList[1] + ansList[2] + ansList[3] + ansList[4];
    System.out
        .println("GrabAndGo:" + ansList[0] + " Lunch:" + ansList[1] + "  Dinner:" + ansList[2] +
            "  Senior: " + ansList[3] + "  NonSenior:" + ansList[4] + "  All:" + allShopperAmount);

    double grabGoPercentage = (ansList[0] * 1.0) / allShopperAmount;
    double seniorPercentage = (ansList[3] * 1.0 / (ansList[4] + ansList[3]));
    double dinnerPercentage = 0.0;
    double lunchPercentage = 0.0;

    assertEquals(0.0, dinnerPercentage, 0.0);
    assertEquals(0.0, lunchPercentage, 0.0);
    //test grabAndGo percentage and seniorPercentage
    assertEquals(grabGoPercentage, customerMixModelFactors.getGrabAndGoFactor(), 0.11);
    assertEquals(seniorPercentage, customerMixModelFactors.getSeniorDemographicPercent(), 0.1);
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
