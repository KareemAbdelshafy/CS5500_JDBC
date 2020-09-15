package model.originalModel;


import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDateTime;
import model.CustomerType;
import model.factors.TimeInStoreFactors;
import model.factors.TimeInStoreFactorsFromFile;
import org.junit.Before;
import org.junit.Test;

public class TimeInStoreModelTest {

  TimeInStoreModel timeInStoreModel;

  LocalDateTime saturday1pm;
  LocalDateTime sunday1pm;
  LocalDateTime monday1pm;
  LocalDateTime tuesday1pm;
  LocalDateTime tuesday6pm;
  LocalDateTime wednesday1pm;
  LocalDateTime thursday1pm;
  LocalDateTime friday1pm;
  LocalDateTime tuesday10am;
  LocalDateTime tuesday12halfpm;

  TimeInStoreFactors timeInStoreFactors ;
  @Before
  public void setup() throws IOException {

    timeInStoreFactors =
        (new TimeInStoreFactorsFromFile(getTrafficFactors())).getTimeInStoreFactors();

    timeInStoreModel = new TimeInStoreModel(timeInStoreFactors);

    saturday1pm = LocalDateTime.of(2020, 05, 23, 13, 0);
    sunday1pm = LocalDateTime.of(2020, 05, 24, 13, 0);
    monday1pm = LocalDateTime.of(2020, 05, 25, 13, 0);
    tuesday1pm = LocalDateTime.of(2020, 05, 26, 13, 0);
    wednesday1pm = LocalDateTime.of(2020, 05, 27, 13, 0);
    thursday1pm = LocalDateTime.of(2020, 05, 28, 13, 0);
    friday1pm = LocalDateTime.of(2020, 05, 29, 13, 0);
    tuesday10am = LocalDateTime.of(2020, 05, 26, 10, 0);
    tuesday12halfpm = LocalDateTime.of(2020, 05, 26, 12, 30);
    tuesday6pm = LocalDateTime.of(2020, 05, 26, 6 + 12, 0);


  }

  @Test
  public void testTimeInStoreMondays() {
    testTimeInStore(monday1pm);
  }

  @Test
  public void testTimeInStoreTuesdays() {
    testTimeInStore(tuesday1pm);
  }

  @Test
  public void testTimeInStoreWednesdays() {
    testTimeInStore(wednesday1pm);

  }

  @Test
  public void testTimeInStoreThursdays() {
    testTimeInStore(thursday1pm);
  }

  @Test
  public void testTimeInStoreFridays() {
    testTimeInStore(friday1pm);
  }

  @Test
  public void testTimeInStoreSaturdays() {
    testTimeInStore(saturday1pm);
  }

  @Test
  public void testTimeInStoreSundays() {
    testTimeInStore(sunday1pm);
  }

  @Test
  public void testLunchTime() {
    assertEquals(timeInStoreFactors.getLunchShoppingTime(),
        testSpecialCustomer(tuesday12halfpm, CustomerType.Lunch));
  }


  @Test
  public void testDinnerTime() {
    assertEquals(timeInStoreFactors.getDinnerShoppingTime(),
        testSpecialCustomer(tuesday6pm, CustomerType.Dinner));
  }

  @Test
  public void testGrabAndGoTime() {
    assertEquals(timeInStoreFactors.getGrabAndGoShoppingTime(),
        testSpecialCustomer(saturday1pm, CustomerType.GrabAndGo));
    assertEquals(timeInStoreFactors.getGrabAndGoShoppingTime(),
        testSpecialCustomer(sunday1pm, CustomerType.GrabAndGo));
  }

  private static boolean between(int lower, int actual, int upper) {
    return lower <= actual && actual <= upper;
  }

  private int seniorHolidayReallyNice(LocalDateTime localDateTime) {
    return timeInStoreModel.next(
        localDateTime,
        CustomerType.Senior);
  }

  private int nonSeniorHolidayReallyNice(LocalDateTime localDateTime) {
    return timeInStoreModel.next(
        localDateTime,
        CustomerType.NonSenior);
  }

  private int seniorNonHolidayReallyNice(LocalDateTime localDateTime) {
    return timeInStoreModel.next(
        localDateTime,
        CustomerType.Senior);
  }

  private int nonSeniorNonHolidayReallyNice(LocalDateTime localDateTime) {
    return timeInStoreModel.next(
        localDateTime,
        CustomerType.NonSenior);
  }


  private int seniorHolidayUnknown(LocalDateTime localDateTime) {
    return timeInStoreModel.next(
        localDateTime,
        CustomerType.Senior);
  }

  private int nonSeniorHolidayUnknown(LocalDateTime localDateTime) {
    return timeInStoreModel.next(
        localDateTime,
        CustomerType.NonSenior);
  }

  private int seniorNonHolidayUnknown(LocalDateTime localDateTime) {
    return timeInStoreModel.next(
        localDateTime,
        CustomerType.Senior);
  }

  private int nonSeniorNonHolidayUnknown(LocalDateTime localDateTime) {
    return timeInStoreModel.next(
        localDateTime,
        CustomerType.NonSenior);
  }

  private void testTimeInStore(LocalDateTime localDateTime) {
    assertTrue(between(
        timeInStoreFactors.getNonSeniorMinShoppingTime(),
        nonSeniorNonHolidayUnknown(localDateTime),
        timeInStoreFactors.getNonSeniorMaxShoppingTime()
    ));

    assertTrue(between(
        timeInStoreFactors.getNonSeniorMinShoppingTime(),
        nonSeniorNonHolidayReallyNice(localDateTime),
        timeInStoreFactors.getNonSeniorMaxShoppingTime()
    ));

    assertTrue(between(
        timeInStoreFactors.getNonSeniorMinShoppingTime(),
        nonSeniorHolidayUnknown(localDateTime),
        timeInStoreFactors.getNonSeniorMaxShoppingTime()
    ));

    assertTrue(between(
        timeInStoreFactors.getNonSeniorMinShoppingTime(),
        nonSeniorHolidayReallyNice(localDateTime),
        timeInStoreFactors.getNonSeniorMaxShoppingTime()
    ));

    // Senior treatment
    assertTrue(between(
        timeInStoreFactors.getSeniorMinShoppingTime(),
        seniorNonHolidayUnknown(localDateTime),
        timeInStoreFactors.getSeniorMaxShoppingTime()
    ));

    assertTrue(between(
        timeInStoreFactors.getSeniorMinShoppingTime(),
        seniorNonHolidayReallyNice(localDateTime),
        timeInStoreFactors.getSeniorMaxShoppingTime()
    ));

    assertTrue(between(
        timeInStoreFactors.getSeniorMinShoppingTime(),
        seniorHolidayUnknown(localDateTime),
        timeInStoreFactors.getSeniorMaxShoppingTime()
    ));

    assertTrue(between(
        timeInStoreFactors.getSeniorMinShoppingTime(),
        seniorHolidayReallyNice(localDateTime),
        timeInStoreFactors.getSeniorMaxShoppingTime()
    ));


  }


  private int testSpecialCustomer(LocalDateTime localDateTime, CustomerType customerType) {
    int holidaynice = timeInStoreModel.next(
        localDateTime,
        customerType);

    int nonholidaynice = timeInStoreModel.next(
        localDateTime,
        customerType);

    int holidayunknown = timeInStoreModel.next(
        localDateTime,
        customerType);

    int nonholidayunknown = timeInStoreModel.next(
        localDateTime,
        customerType);

    assertTrue(holidaynice == nonholidaynice);
    assertTrue(holidayunknown == nonholidayunknown);
    assertTrue(holidaynice == nonholidayunknown);

    return holidaynice;

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