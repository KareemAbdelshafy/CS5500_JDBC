package model.factors;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import model.CustomerType;
import model.CustomerVisit;
import model.IStoreModel;
import model.originalModel.StoreModelBuilder;
import org.junit.Before;
import org.junit.Test;

public class AverageShoppingTimeTest {

  IStoreModel storeModel;
  List<CustomerVisit> allVisits;
  List<CustomerVisit> seniorVisit;
  List<CustomerVisit> nonSeniorVisit;
  List<CustomerVisit> lunchVisit;
  List<CustomerVisit> dinnerVisit;
  List<CustomerVisit> grabAndGoVisit;
  TimeInStoreFactors timeInStoreFactors;

  @Before
  public void setUp() throws Exception {

    LocalDate june8 = LocalDate.of(2020, 6, 8);
    LocalDate june9 = LocalDate.of(2020, 6, 9);
    LocalDate may30 = LocalDate.of(2020, 5, 30);

    String holidayCSV = "2020-01-01" + System.lineSeparator()
        + "2020-05-16" + System.lineSeparator()
        + "2020-07-04" + System.lineSeparator()
        + "2020-11-26";

    IHolidayCalendar calendar = new HolidayCalendar(new Scanner(holidayCSV));

    String weatherCSV =
        "2020-01-08,Nice" + System.lineSeparator()
            + "2020-01-09,ReallyNice" + System.lineSeparator()
            + "2020-01-10,Unknown";

    IWeatherForecast forecast = new WeatherForecast(new Scanner(weatherCSV));

    StoreModelBuilder builder = new StoreModelBuilder();

    CustomerArrivalModelFactors customerArrivalModelFactors = (new CustomerArrivalFactorsFromFile(getTrafficFactors())).
        geyCustomerArrivalModelFactors();

    timeInStoreFactors =
        (new TimeInStoreFactorsFromFile(getTrafficFactors())).getTimeInStoreFactors();

    CustomerMixModelFactors customerMixModelFactors =
        (new CustomerMixModelFactorsFromFile(getTrafficFactors())).getCustomerMixModelFactors();


    storeModel = builder.
        setHolidayCalendar(calendar).
        setWeatherForecast(forecast).
        setCustomerArrivalModelFactors(customerArrivalModelFactors).
        setTimeInStoreFactors(timeInStoreFactors).
        setCustomerMixModelFactors(customerMixModelFactors).
        setScenarioID(0).
        build();

    allVisits = new ArrayList<>();

    storeModel.generateForecast(june8, june9);
    while (storeModel.hasNext()) {
      allVisits.add(storeModel.next());
    }

    seniorVisit = allVisits.stream()
        .filter(visit -> visit.getCustomerType() == CustomerType.Senior)
        .collect(Collectors.toList());

    nonSeniorVisit = allVisits.stream()
        .filter(visit -> visit.getCustomerType() == CustomerType.NonSenior)
        .collect(Collectors.toList());

    lunchVisit = allVisits.stream()
        .filter(visit -> visit.getCustomerType() == CustomerType.Lunch)
        .collect(Collectors.toList());

    dinnerVisit = allVisits.stream()
        .filter(visit -> visit.getCustomerType() == CustomerType.Dinner)
        .collect(Collectors.toList());

    grabAndGoVisit = allVisits.stream()
        .filter(visit -> visit.getCustomerType() == CustomerType.GrabAndGo)
        .collect(Collectors.toList());
  }

  @Test
  public void seniorMinShoppingTimeTest() {
    int minTime = seniorVisit.get(0).getMinutesInStore();
    for (CustomerVisit currVisit : seniorVisit) {
      if (currVisit.getMinutesInStore() < minTime) {
        minTime = currVisit.getMinutesInStore();
      }
    }
    System.out.println(minTime);
    assertEquals(timeInStoreFactors.getSeniorMinShoppingTime(), minTime);
  }

  @Test
  public void seniorMaxShoppingTimeTest() {
    int maxTime = seniorVisit.get(0).getMinutesInStore();
    for (CustomerVisit currVisit : seniorVisit) {
      if (currVisit.getMinutesInStore() > maxTime) {
        maxTime = currVisit.getMinutesInStore();
      }
    }
    System.out.println(maxTime);
    assertEquals(timeInStoreFactors.getSeniorMaxShoppingTime(), maxTime, 1.0);
  }

  //----------------------------------------------------
  @Test
  public void nonSeniorAverageShoppingTimeTest() {
    int count = nonSeniorVisit.size();
    int totalShoppingTime = 0;
    for (CustomerVisit currVisit : nonSeniorVisit) {
      totalShoppingTime += currVisit.getMinutesInStore();
    }

    System.out.println("All visit:" + allVisits.size() + "  nonSenior:" + nonSeniorVisit.size());
    int avergeShoppingTime = totalShoppingTime / count;
    System.out.println(avergeShoppingTime);
    assertEquals(timeInStoreFactors.getNonSeniorAvgShoppingTime(), avergeShoppingTime, 2.0);
  }

  @Test
  public void lunchAverageShoppingTimeTest() {
    int count = lunchVisit.size();
    int totalShoppingTime = 0;
    for (CustomerVisit currVisit : lunchVisit) {
      totalShoppingTime += currVisit.getMinutesInStore();
    }

    int avergeShoppingTime = totalShoppingTime / count;
    System.out.println(avergeShoppingTime);
    assertEquals(timeInStoreFactors.getLunchShoppingTime(), avergeShoppingTime, 1.0);
  }

  @Test
  public void dinnerAverageShoppingTimeTest() {
    int count = dinnerVisit.size();
    int totalShoppingTime = 0;
    for (CustomerVisit currVisit : dinnerVisit) {
      totalShoppingTime += currVisit.getMinutesInStore();
    }

    int avergeShoppingTime = totalShoppingTime / count;
    System.out.println(avergeShoppingTime);
    assertEquals(timeInStoreFactors.getDinnerShoppingTime(), avergeShoppingTime, 1.0);
  }

  @Test
  public void grabAverageShoppingTimeTest() {
    int count = grabAndGoVisit.size();
    int totalShoppingTime = 0;
    for (CustomerVisit currVisit : grabAndGoVisit) {
      totalShoppingTime += currVisit.getMinutesInStore();
    }
    if (count != 0) {
      int avergeShoppingTime = totalShoppingTime / count;
      System.out.println(avergeShoppingTime);
      assertEquals(timeInStoreFactors.getGrabAndGoShoppingTime(), avergeShoppingTime, 1.0);
    }
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
