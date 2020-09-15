package model.originalModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;

/**
 * The {@code ICustomerArrivalModel} is a model that forecast the time a customer arrives into the
 * store. it calculates the entry time over a period of dates (a date range) and
 * incorporates differences in customer traffic due to weather and holiday.
 */
public interface ICustomerArrivalModel extends Iterator<LocalDateTime> {

  /**
   * sets the date range from which the customer traffic is modeled over.
   * @param fromDate the from date (or start date) of the forecast.
   * @param toDate the to date (or end date) of the forecast.
   */
  void setDateRange(LocalDate fromDate, LocalDate toDate);
}
