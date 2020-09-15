package console;

import controller.StoreForecastController;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Scanner;
import model.IStoreModel;
import model.factors.CustomerArrivalFactorsFromDb;
import model.factors.CustomerArrivalModelFactors;
import model.factors.CustomerMixModelFactors;
import model.factors.CustomerMixModelFactorsFromDb;
import model.factors.HolidayCalendar;
import model.factors.IHolidayCalendar;
import model.factors.IWeatherForecast;
import model.factors.Scenario;
import model.factors.TimeInStoreFactors;
import model.factors.TimeInStoreFactorsFromDb;
import model.factors.WeatherForecast;
import model.originalModel.StoreModelBuilder;
import view.DatabaseView;

public class RunScenarioFromDb {

  private static String sql = "SELECT * FROM scenariotable s where not exists "
      + "(select * from CustomerVisit v where s.scenarioId = v.scenarioId);";

  public static void selectAndRun(Connection connection) throws SQLException, IOException {
    Scanner scanner = new Scanner(System.in);
    //display all scenario descriptions
    displayScenario(connection);
    System.out.println("Please select scenarioID you want to use:");
    int scenarioID = scanner.nextInt();
    run(connection, scenarioID);
    System.out.println("finished!!!");
  }

  /**
   * display all scenarioTable
   */
  private static void displayScenario(Connection connection) throws SQLException {
    Statement stmt = connection.createStatement();
    ResultSet rs = stmt.executeQuery(sql);

    while (rs.next()) {
      int scenarioId = rs.getInt(1);
      int trafficFactorId = rs.getInt(2);
      String startDate = rs.getDate(3).toString();
      String endDate = rs.getDate(4).toString();
      String description = rs.getString(5);
      System.out.println("scenarioID: " + scenarioId + "   startDate:" + startDate +
          "   endDate" + endDate + "    description:" + description);
    }
  }

  private static void run(Connection connection, int scenarioId) throws IOException, SQLException {

    Scenario scenario = new Scenario(connection, scenarioId);
    LocalDate fromDate = scenario.getStart();
    LocalDate toDate = scenario.getEnd();
    int trafficId = scenario.getTrafficFactorId();

    CustomerArrivalModelFactors customerArrivalModelFactors = (new CustomerArrivalFactorsFromDb(
        connection, trafficId))
        .geyCustomerArrivalModelFactors();

    TimeInStoreFactors timeInStoreFactors = (new TimeInStoreFactorsFromDb(connection, trafficId))
        .getTimeInStoreFactors();

    CustomerMixModelFactors customerMixModelFactors = (new CustomerMixModelFactorsFromDb(connection,
        trafficId)).getCustomerMixModelFactors();

    IHolidayCalendar holidays = new HolidayCalendar(connection);
    IWeatherForecast forecast = new WeatherForecast(connection);

    // instantiate the different components of the model
    StoreModelBuilder builder = new StoreModelBuilder();
    IStoreModel storeModel = builder.
        setScenarioID(scenarioId).
        setWeatherForecast(forecast).
        setHolidayCalendar(holidays).
        setCustomerArrivalModelFactors(customerArrivalModelFactors).
        setTimeInStoreFactors(timeInStoreFactors).
        setCustomerMixModelFactors(customerMixModelFactors).
        build();

    // instantiate the controller
    StoreForecastController controller = new StoreForecastController(storeModel);

    // set the view
    controller.setView(new DatabaseView(connection));

    // generate the store forecast
    controller.run(fromDate, toDate);
    connection.close();
  }
}
