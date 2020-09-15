package controller;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.CustomerType;
import model.CustomerVisit;
import model.IStoreModel;
import org.junit.Before;
import org.junit.Test;

import model.factors.IHolidayCalendar;
import model.factors.IWeatherForecast;
import view.StoreForecastView;

public class StoreForecastControllerTest {

  StoreForecastController controller;
  StoreForecastView view;
  IStoreModel model;

  LocalDateTime monday10am = LocalDateTime.of(2020, 06, 8, 10, 0);
  Map<Integer, CustomerVisit> customersMap = new HashMap<>();

  @Before
  public void setup() {

    // assume the model generates these three records
    /*customersMap.put(Integer.valueOf(1), new CustomerVisit(1, monday10am, CustomerType.Senior, 60,  IWeatherForecast.WeatherForecastType.Unknown, IHolidayCalendar.HolidayTreatment.NonHoliday));
    customersMap
        .put(Integer.valueOf(2), new CustomerVisit(2, monday10am, CustomerType.NonSenior, 24,  IWeatherForecast.WeatherForecastType.Unknown,IHolidayCalendar.HolidayTreatment.NonHoliday));
    customersMap
        .put(Integer.valueOf(3), new CustomerVisit(3, monday10am, CustomerType.NonSenior, 26,  IWeatherForecast.WeatherForecastType.Unknown,IHolidayCalendar.HolidayTreatment.NonHoliday));
*/
    // code the model to generate those records
    model = new IStoreModel() {

      private List<CustomerVisit> modelCustomers = new ArrayList<>();

      @Override
      public void generateForecast(LocalDate startDate, LocalDate endDate) {
        // for each record the model should generate add it to a list
        customersMap.forEach((Integer cusomterId, CustomerVisit customerVisit) -> {
          this.modelCustomers.add(customerVisit);
        });
      }

      @Override
      public boolean hasNext() {
        return this.modelCustomers.size() > 0;
      }

      @Override
      public CustomerVisit next() {
        return this.modelCustomers.remove(0);
      }
    };

    controller = new StoreForecastController(model);

  }

  @Test
  public void testStoreForecastControllerTest() {

    // to test the controller in isolation we test that everything that is hard coded in
    // the model, makes its way to the view

    // stubbed out view, that contains the tests
    view = new StoreForecastView() {
      @Override
      public void setViewModel(List<CustomerVisit> customerRecords) throws IOException {

        // for each record that gets set in the view, maker sure it is one of the records we expect
        customerRecords.forEach(customerVisit -> {
          assertEquals(
              customersMap.get(Integer.valueOf(customerVisit.getId())),
              customerVisit);
        });
      }
    };

    // set the view
    controller.setView(view);

    try {
      // run the model )(dates don't matter) to trigger the test in the view
      controller.run(null, null);
    } catch (IOException| SQLException e ) {
      assertFalse(true);

    }


  }
}