package model.factors;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Scenario {

  private static String sql = "Select TrafficFactorID, StartDate, EndDate from ScenarioTable where ScenarioID = ?";

  private final Connection connection;
  private final int scenarioID;
  private LocalDate startDate;
  private LocalDate endDate;
  private int trafficFactorId;

  public Scenario(Connection connection, int scenarioID) throws SQLException {
    this.connection = connection;
    this.scenarioID = scenarioID;
    readScenarioFromTable();
  }

  public LocalDate getStart() {
    return this.startDate;
  }

  public LocalDate getEnd() {
    return this.endDate;
  }


  private void readScenarioFromTable() throws SQLException {
    PreparedStatement stmt = connection.prepareStatement(sql);
    stmt.setInt(1, this.scenarioID);
    ResultSet result = stmt.executeQuery();

    if (result.next()) {
      this.startDate = result.getDate("StartDate" ).toLocalDate();
      this.endDate = result.getDate("EndDate" ).toLocalDate();
      this.trafficFactorId = result.getInt("trafficFactorId");
    }
  }

  public int getTrafficFactorId() {
    return this.trafficFactorId;
  }
}
