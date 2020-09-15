package view;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import model.CustomerVisit;

public class DatabaseView implements StoreForecastView{
  private static int BatchSize = 10000;
  private final Connection connection;
  private final String query = "call InsertCustomerVisit (?, ?, ?, ?, ?, ?);" ;

  public DatabaseView(Connection connection) {
    this.connection = connection;
  }

  @Override
  public void setViewModel(List<CustomerVisit> customerRecords) throws IOException, SQLException {

    CallableStatement stmt = connection.prepareCall(query);

    for (CustomerVisit customerVisit: customerRecords) {

      LocalTime sixAm = LocalTime.of(6,0);
      LocalDateTime arrival = customerVisit.getArrivalTime();
      assert(arrival.toLocalTime().isAfter(sixAm))
          : "oops" + arrival.toString();

      //stmt.setInt(1, customerVisit.getId());
      stmt.setString(1, customerVisit.getCustomerType().toString());
      stmt.setString(2, customerVisit.getHolidayType().toString());
      stmt.setString(3, customerVisit.getWeatherType().toString());
      //change localDate to sql.Timestamp
      java.sql.Timestamp timestamp = java.sql.Timestamp.valueOf(arrival);
      stmt.setTimestamp(4, timestamp);
      stmt.setInt(5, customerVisit.getMinutesInStore());
      stmt.setInt(6, customerVisit.getScenarioID());
      stmt.addBatch();
    }

    // commit the remaining transactions
    stmt.executeBatch();

  }
}
