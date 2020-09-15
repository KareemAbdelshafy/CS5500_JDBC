package model.factors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashMap;

public class CustomerMixModelFactorsFromDb {
  static String sql = "SELECT TFItem, ValueString FROM TFValue " +
          "where TrafficFactorID = ?";

  private final Connection connection;
  private final int trafficFactorID;

  public CustomerMixModelFactorsFromDb(Connection connection, int trafficFactorID) {
    this.connection = connection;
    this.trafficFactorID = trafficFactorID;
  }

  public CustomerMixModelFactors getCustomerMixModelFactors() throws IllegalStateException {

    LocalTime seniorStartTime;
    LocalTime seniorEndTime;
    DayOfWeek seniorDayOfWeek;
    LocalTime dinnerStartTime;
    LocalTime dinnerEndTime;
    LocalTime lunchStartTime;
    LocalTime lunchEndTime;

    double lunchRushFactor;
    double dinnerRushFactor;
    double grabAndGoFactor;

    double seniorDemographicPercent;
    double percentOfTrafficThatIsSeniorDuringSeniorShopping;
    HashMap<String, String> hmap = new HashMap<String, String>();

    PreparedStatement stmt = null;
    try {
      stmt = connection.prepareStatement(sql);

      stmt.setInt(1, trafficFactorID);
      ResultSet result = stmt.executeQuery();

      while (result.next()) {
        String ValueString = result.getString("ValueString");
        String TFItem = result.getString("TFItem");
        hmap.put(TFItem,ValueString);
      }

        seniorStartTime = LocalTime.parse(hmap.get("seniorStartTime"));
        seniorEndTime = LocalTime.parse(hmap.get("seniorEndTime"));
        seniorDayOfWeek = DayOfWeek.valueOf(hmap.get("seniorDayOfWeek").toUpperCase());
        dinnerStartTime = LocalTime.parse(hmap.get("dinnerStartTime"));
        dinnerEndTime = LocalTime.parse(hmap.get("dinnerEndTime"));
        lunchStartTime = LocalTime.parse(hmap.get("lunchStartTime"));
        lunchEndTime = LocalTime.parse(hmap.get("lunchEndTime"));

        lunchRushFactor = Double.parseDouble(hmap.get("lunchRushFactor"));
        dinnerRushFactor = Double.parseDouble(hmap.get("dinnerRushFactor"));
        grabAndGoFactor = Double.parseDouble(hmap.get("grabAndGoFactor"));
        seniorDemographicPercent = Double.parseDouble(hmap.get("seniorDemographicPercent"));
        percentOfTrafficThatIsSeniorDuringSeniorShopping =
                Double.parseDouble(hmap.get("percentOfTrafficThatIsSeniorDuringSeniorShopping"));

        return new CustomerMixModelFactors(seniorStartTime,
                seniorEndTime, seniorDayOfWeek, dinnerStartTime,
                dinnerEndTime, lunchStartTime, lunchEndTime,
                lunchRushFactor, dinnerRushFactor,
                grabAndGoFactor, seniorDemographicPercent,
                percentOfTrafficThatIsSeniorDuringSeniorShopping);

    } catch (SQLException e) {
    }
    throw new IllegalStateException("unable to read traffic factors from database");
  }

}
