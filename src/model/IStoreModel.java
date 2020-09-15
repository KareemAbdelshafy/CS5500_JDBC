package model;

import java.time.LocalDate;
import java.util.Iterator;

/**
 * This interface {@code IStoreModel} extends {@code Iterator<CustomerVisit>} to forecast(model)
 * and present a number of {@link model.CustomerVisit} to a store over a specfic date rnage.
 */
public interface IStoreModel extends Iterator<CustomerVisit> {

  /**
   * Forecast (or model) the customer traffic in a store over a date range.
   * @param startDate the start date
   * @param endDate the end date
   */
  void generateForecast(LocalDate startDate, LocalDate endDate);
}
