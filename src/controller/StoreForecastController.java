package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.CustomerVisit;
import model.factors.IHolidayCalendar;
import model.IStoreModel;
import model.factors.IWeatherForecast;
import view.StoreForecastView;


/**
 * This class {@code StoreForecastController} contains the logic for interacting with the model
 * {@link model.IStoreModel} and passing the data over to the View
 * {@link view.StoreForecastView}.
 */
public class StoreForecastController {

  private final IStoreModel storeModel;

  private StoreForecastView storeForecastView;

  /**
   * Construct an instance of StoreForecastController.
   * @param storeModel the {@link model.IStoreModel} with the rules for the store.
  */
  public StoreForecastController(IStoreModel storeModel) {
    this.storeModel = storeModel;
  }

  /**
   * sets the view.
   * @param storeForecastView {@link view.StoreForecastView}
   */
  public void setView(StoreForecastView storeForecastView) {
    this.storeForecastView = storeForecastView;
  }

  /**
   * runs a customer forecast over a date range (inclusive).
   * @param startDate the start data of the forecast.
   * @param endDate the end data of the forecast.
   * @throws IOException when there is trouble outputing data.
   * @throws IllegalStateException when run without setting the view
   *         {@link controller.StoreForecastController#setView}
   */
  public void run(LocalDate startDate, LocalDate endDate) throws IOException, IllegalStateException, SQLException {
    storeModel.generateForecast(startDate, endDate);
    List<CustomerVisit> customerRecords = new ArrayList<CustomerVisit>();

    // foreach customer that enters the store
    while (storeModel.hasNext()) {
      CustomerVisit customerVisit = storeModel.next();

      // add this record to the output list
      customerRecords.add(customerVisit);

    }

    this.storeForecastView.setViewModel(customerRecords);
  }


}
