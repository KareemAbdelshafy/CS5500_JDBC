package console;

import controller.StoreForecastController;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import model.IStoreModel;
import model.factors.CustomerArrivalFactorsFromFile;
import model.factors.CustomerArrivalModelFactors;
import model.factors.CustomerMixModelFactors;
import model.factors.CustomerMixModelFactorsFromFile;
import model.factors.HolidayCalendar;
import model.factors.IHolidayCalendar;
import model.factors.IWeatherForecast;
import model.factors.TimeInStoreFactors;
import model.factors.TimeInStoreFactorsFromFile;
import model.factors.WeatherForecast;
import model.originalModel.StoreModelBuilder;
import view.StoreForecastCsvView;

public class RunScenarioFromFile {

  public static void run(LocalDate fromDate, LocalDate toDate) throws IOException, SQLException {

    String holidaySchedulePath = getHolidaySchedulePath();
    String weatherForecastPath = getForecastPath();
    String trafficFactorsPath = getTrafficFactorsPath();


    CustomerArrivalModelFactors customerArrivalModelFactors = new
        CustomerArrivalFactorsFromFile(customerArrivalPath()).geyCustomerArrivalModelFactors();

    CustomerMixModelFactors customerMixModelFactors = (new CustomerMixModelFactorsFromFile(
        trafficFactorsPath)).getCustomerMixModelFactors();

    TimeInStoreFactors timeInStoreFactors = (new TimeInStoreFactorsFromFile(trafficFactorsPath))
        .getTimeInStoreFactors();

    IHolidayCalendar holidays = new HolidayCalendar(holidaySchedulePath);
    IWeatherForecast forecast = new WeatherForecast(weatherForecastPath);

    // instantiate the different components of the model
    StoreModelBuilder builder = new StoreModelBuilder();
    IStoreModel storeModel = builder.
        setWeatherForecast(forecast).
        setHolidayCalendar(holidays).
        setCustomerArrivalModelFactors(customerArrivalModelFactors).
        setTimeInStoreFactors(timeInStoreFactors).
        setCustomerMixModelFactors(customerMixModelFactors).
        setScenarioID(0).
        build();

    // instantiate the controller
    StoreForecastController controller = new StoreForecastController(storeModel);

    // set the view
    controller.setView(new StoreForecastCsvView("MyCSV.csv"));

    // generate the store forecast
    controller.run(fromDate, toDate);
  }

  private static String customerArrivalPath() {
    return "resources/factors.text";
  }


  /**
   * read the path to the weather file.
   *
   * @return the weather csv path
   */
  private static String getForecastPath() {
    return "resources/weather.csv";
  }

  /**
   * read the path to the holiday schedule.
   *
   * @return the holiday csv path
   */
  private static String getHolidaySchedulePath() {
    return "resources/holidays.csv";
  }

  /**
   * Get the path to the factors text file.
   *
   * @return the factors text file path
   */
  private static String getTrafficFactorsPath() {
    return "resources/factors.text";
  }

  /**
   * Get the path to the factors text file.
   *
   * @return the factors text file path
   */
  private static String getTimeInStoreFactorsPath() {
    return "resources/time-in-store-factors.text";
  }
}
