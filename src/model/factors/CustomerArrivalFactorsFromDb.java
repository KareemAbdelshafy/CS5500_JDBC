package model.factors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashMap;

public class CustomerArrivalFactorsFromDb {
 // static String sql = "SELECT grabAndGoShoppingTime, lunchShoppingTime, dinnerSoppingTime, weekendShoppingTime, seniorMinShoppingTime, seniorMaxShoppingTime, nonSeniorAvgShoppingTime, nonSeniorMinShoppingTime, nonSeniorMaxShoppingTime\n" +
  //        "FROM Trafficfactor\n" +
 //         "where TrafficFactorID = ?";

  static String sql = "SELECT TFItem, ValueString FROM TFValue " +
          "where TrafficFactorID = ?";

  private  Connection connection;
  private  int trafficFactorID;

  public CustomerArrivalFactorsFromDb(Connection connection, int trafficFactorID) {
    this.connection = connection;
    this.trafficFactorID = trafficFactorID;
  }


  public CustomerArrivalModelFactors geyCustomerArrivalModelFactors() throws IllegalStateException {
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

        LocalTime storeOpenTime = LocalTime.parse(hmap.get("storeOpenTime"));
        LocalTime storeCloseTime = LocalTime.parse(hmap.get("storeCloseTime"));
        int mon = Integer.parseInt(hmap.get("mon"));
        int tue = Integer.parseInt(hmap.get("tue"));
        int wed = Integer.parseInt(hmap.get("wed"));
        int thur = Integer.parseInt(hmap.get("thur"));
        int fri =  Integer.parseInt(hmap.get("fri"));
        int sat = Integer.parseInt(hmap.get("sat"));
        int sun = Integer.parseInt(hmap.get("sun"));
        double holidayFactor = Double.parseDouble(hmap.get("holidayFactor"));
        double dayBeforeHolidayFactor = Double.parseDouble(hmap.get("dayBeforeHolidayFactor"));
        double weekBeforeHolidayFactor = Double.parseDouble(hmap.get("weekBeforeHolidayFactor"));
        double lunchRushFactor = Double.parseDouble(hmap.get("lunchRushFactor"));
        double dinnerRushFactor = Double.parseDouble(hmap.get("dinnerRushFactor"));
        double grabAndGoFactor = Double.parseDouble(hmap.get("grabAndGoFactor"));

        LocalTime seniorStartTime = LocalTime.parse(hmap.get("seniorStartTime"));
        LocalTime seniorEndTime =  LocalTime.parse(hmap.get("seniorEndTime"));
        DayOfWeek seniorDayOfWeek = DayOfWeek.valueOf(hmap.get("seniorDayOfWeek").toUpperCase());
        LocalTime dinnerStartTime = LocalTime.parse(hmap.get("dinnerStartTime"));
        LocalTime dinnerEndTime = LocalTime.parse(hmap.get("dinnerEndTime"));
        LocalTime lunchStartTime = LocalTime.parse(hmap.get("lunchStartTime"));
        LocalTime lunchEndTime = LocalTime.parse(hmap.get("lunchEndTime"));

        return new CustomerArrivalModelFactors(dinnerStartTime, dinnerEndTime,
            lunchStartTime, lunchEndTime, lunchRushFactor, dinnerRushFactor, storeOpenTime, storeCloseTime,
            mon, tue, wed, thur, fri, sat, sun, holidayFactor, dayBeforeHolidayFactor,
            weekBeforeHolidayFactor, grabAndGoFactor);


    } catch (SQLException e) {
      e.printStackTrace();
      throw new IllegalStateException("unable to read traffic factors from database");
    }

  }
}
