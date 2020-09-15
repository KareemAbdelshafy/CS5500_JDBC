package model.originalModel;

import static java.time.temporal.ChronoUnit.HOURS;
import static java.time.temporal.ChronoUnit.MINUTES;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import model.factors.CustomerArrivalModelFactors;
import model.factors.IHolidayCalendar;
import model.factors.IWeatherForecast;

/**
 * The {@code CustomerArrivalModel} is a model that forecast the time a customer arrives into the
 * store. it calculates the entry time over a period of dates (a date range) and
 * incorporates differences in customer traffic due to weather and holiday.
 */
class CustomerArrivalModel implements ICustomerArrivalModel {

  private final IHolidayCalendar holidays;
  private final IWeatherForecast weatherForecast;
  private final CustomerArrivalModelFactors customerArrivalModelFactors;

  private List<LocalDateTime> arrivals;

  /**
   * Construct an {@code CustomerArrivalModel} instance.
   *
   * @param customerArrivalModelFactors {@link model.factors.CustomerArrivalModelFactors}
   * @param holidays {@link model.factors.IHolidayCalendar}
   * @param weatherForecast {@link model.factors.IWeatherForecast}
   */
  public CustomerArrivalModel(CustomerArrivalModelFactors customerArrivalModelFactors, IHolidayCalendar holidays,
      IWeatherForecast weatherForecast) {
    this.holidays = holidays;
    this.weatherForecast = weatherForecast;
    this.customerArrivalModelFactors = customerArrivalModelFactors;
  }

  @Override
  public boolean hasNext() {
    return this.arrivals.size() > 0;
  }

  @Override
  public LocalDateTime next() {
    LocalDateTime curr = this.arrivals.get(0);
    this.arrivals.remove(0);
    return curr;
  }

  @Override
  public void setDateRange(LocalDate fromDate, LocalDate toDate) {
    this.arrivals = new ArrayList<>();

    // for every day in the date range (https://www.baeldung.com/java-iterate-date-range)
    for (LocalDate thisDate = fromDate; thisDate.isBefore(toDate) || thisDate.isEqual(toDate);
        thisDate = thisDate.plusDays(1)) {

      int averageTrafficOnDay = getAverageTotalCustomersOnDay(thisDate);

      // assume the first customer walks into the store when it opens each day
      LocalDateTime previousEntryTime = thisDate.atTime(
          customerArrivalModelFactors.getStoreOpenTime());

      // for each customer that should be entering the store
      for (int c = 1; c <= averageTrafficOnDay; c++) {

        // find the time they arrive
        LocalDateTime thisCurrentArrival = makeEntryTime(averageTrafficOnDay, previousEntryTime);
        // add this arrival to the queue
        this.arrivals.add(thisCurrentArrival);
        // set as previuos arrival
        previousEntryTime = thisCurrentArrival;
      }
    }
  }

  /**
   * given a date determine the average customers in a day applying treatments for weather and
   * holidays;
   *
   * @param thisDate the date in question
   * @return the average umber of customers on that day
   */
  private int getAverageTotalCustomersOnDay(LocalDate thisDate) {

    int averageTraffic = customerArrivalModelFactors.getAverageTraffic(thisDate.getDayOfWeek());

    double holidayFactor = customerArrivalModelFactors.getHolidayFactor(
        this.holidays.getHoliday(thisDate));

    double weatherFactor = customerArrivalModelFactors.getWeatherFactor(
        this.weatherForecast.getForecast(thisDate), thisDate.getDayOfWeek());

    // TODO: we are matching traffic exactly, add some variance.
    double averageTotalCustomersOnDay = averageTraffic * holidayFactor * weatherFactor;

    return (int) Math.round(averageTotalCustomersOnDay);
  }

  /**
   * Given the average number of customers on a given day and the previous arrival, determine the
   * arrival of the next arrival.
   *
   * @param avgCustomersThisDay that average number of customers arriving in the day in question
   * @param previousArrival the time of the previous arrival
   * @return the arrival time of the next customer
   */
  private LocalDateTime makeEntryTime(int avgCustomersThisDay, LocalDateTime previousArrival) {

    double dinnerRushHours = (customerArrivalModelFactors.getDinnerStart().until(
        customerArrivalModelFactors.getDinnerEnd(), MINUTES)) / 60;

    double lunchRushHours = (customerArrivalModelFactors.getLunchStart().until(
        customerArrivalModelFactors.getLunchEnd(), MINUTES)) / 60;

    double regularHours = (customerArrivalModelFactors.getStoreOpenTime().until(
        customerArrivalModelFactors.getStoreCloseTime(), HOURS)) - dinnerRushHours - lunchRushHours;

    int secondsPerHour = 60 * 60;

    // TODO: make this more sophisticated (people dont come in uniformly)
    // the next customer arrive at a stead arrival rate based on average traffic for the day,
    // but adjusted for surges at lunch and dinner
    double secondsBeforeNext = ((regularHours +
        dinnerRushHours * customerArrivalModelFactors.getDinnerRushFactor() +
        lunchRushHours * customerArrivalModelFactors.getLunchRushFactor()) * secondsPerHour)
        / avgCustomersThisDay;

    if (isLunchRush(previousArrival)) {
      secondsBeforeNext = secondsBeforeNext / customerArrivalModelFactors.getLunchRushFactor();
    } else if (isDinnerRush(previousArrival)) {
      secondsBeforeNext = secondsBeforeNext / customerArrivalModelFactors.getDinnerRushFactor();
    }

    // add some amount of seconds to the previous customer to read arrival of new customer
    LocalDateTime retVal = previousArrival.plusSeconds((long) secondsBeforeNext);

    //////////////////////////////////////////////////////////////////////////////////////////
    // todo: we pretend everybody comes right before the store closes, we likeley can be
    //       smarter here not sure it will make a major impact.
    //////////////////////////////////////////////////////////////////////////////////////////
    if (retVal.toLocalTime().isAfter(customerArrivalModelFactors.getStoreCloseTime())) {
      retVal = retVal.with(customerArrivalModelFactors.getStoreCloseTime().minusSeconds(1));
      //retVal = retVal.withHour(storeCloseTime.getHour()-1).withMinute(59).withSecond(59);
    }

    // the store is oly open between 6 and 9pm
    assert retVal.toLocalTime().isAfter(customerArrivalModelFactors.getStoreOpenTime().minusSeconds(1)) &&
        retVal.toLocalTime().isBefore(customerArrivalModelFactors.getStoreCloseTime().plusSeconds(1)) :
        retVal.toString() + "must be between 6am and 9pm";

    return retVal;
  }

  /**
   * determine if this is the dinner time.
   *
   * @param date the time the customer enters
   * @return true if this is dinner time, false otherwise.
   */
  private boolean isDinnerRush(LocalDateTime date) {

    return (!isWeekend(date.getDayOfWeek()) &&
        isBetween(date.toLocalTime(),
            customerArrivalModelFactors.getDinnerStart(),
            customerArrivalModelFactors.getDinnerEnd()));
  }

  /**
   * determine if this is the lunch time.
   *
   * @param date the time the customer enters
   * @return true if this is lunch time, false otherwise.
   */
  private boolean isLunchRush(LocalDateTime date) {

    return (!isWeekend(date.getDayOfWeek()) &&
        isBetween(date.toLocalTime(),
            customerArrivalModelFactors.getLunchStart(),
            customerArrivalModelFactors.getLunchEnd()));
  }

  /**
   * determine if the day of week is on the weekend.
   *
   * @param arrivalDay the day of the arrival.
   * @return true if it ius weekend, false otherwise.
   */
  private static boolean isWeekend(DayOfWeek arrivalDay) {
    return arrivalDay == DayOfWeek.SATURDAY || arrivalDay == DayOfWeek.SUNDAY;
  }

  private static boolean isBetween(LocalTime time, LocalTime start, LocalTime end) {
    boolean isAfterStart = time.isAfter(start.minusSeconds(1));
    boolean isBeforeEnd = time.isBefore(end.plusSeconds(1));

    return isAfterStart && isBeforeEnd;

  }
}

