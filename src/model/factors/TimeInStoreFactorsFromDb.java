package model.factors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashMap;

public class TimeInStoreFactorsFromDb {
  static String sql = "SELECT TFItem, ValueString FROM TFValue " +
          "where TrafficFactorID = ?";

  private final Connection connection;
  private final int trafficFactorID;

  public TimeInStoreFactorsFromDb(Connection connection, int trafficFactorID) {
    this.connection = connection;
    this.trafficFactorID = trafficFactorID;
  }

  public TimeInStoreFactors getTimeInStoreFactors() throws IllegalStateException {



    PreparedStatement stmt = null;
    try {
      stmt = connection.prepareStatement(sql);

      stmt.setInt(1, trafficFactorID);
      ResultSet result = stmt.executeQuery();
      HashMap<String, String> hmap = new HashMap<String, String>();

      while (result.next()) {
        String ValueString = result.getString("ValueString");
        String TFItem = result.getString("TFItem");
        hmap.put(TFItem,ValueString);
      }
       int grabAndGoShoppingTime =  Integer.parseInt(hmap.get("grabAndGoShoppingTime"));

       int lunchShoppingTime = Integer.parseInt(hmap.get("lunchShoppingTime"));
      int  dinnerSoppingTime = Integer.parseInt(hmap.get("dinnerSoppingTime"));
      int  weekendShoppingTime =Integer.parseInt(hmap.get("weekendShoppingTime"));

      int  seniorMinShoppingTime = Integer.parseInt(hmap.get("seniorMinShoppingTime"));
      int  seniorMaxShoppingTime = Integer.parseInt(hmap.get("seniorMaxShoppingTime"));
      int  nonSeniorAvgShoppingTime =  Integer.parseInt(hmap.get("nonSeniorAvgShoppingTime"));
     int   nonSeniorMinShoppingTime = Integer.parseInt(hmap.get("nonSeniorMinShoppingTime"));
     int   nonSeniorMaxShoppingTime = Integer.parseInt(hmap.get("nonSeniorMaxShoppingTime"));

        return new TimeInStoreFactors( grabAndGoShoppingTime,
                lunchShoppingTime, dinnerSoppingTime, weekendShoppingTime,
                seniorMinShoppingTime, seniorMaxShoppingTime, nonSeniorAvgShoppingTime,
                nonSeniorMinShoppingTime, nonSeniorMaxShoppingTime);

    } catch (SQLException e) {
      e.printStackTrace();
      throw new IllegalStateException("unable to read traffic factors from database");
    }
  }
}
