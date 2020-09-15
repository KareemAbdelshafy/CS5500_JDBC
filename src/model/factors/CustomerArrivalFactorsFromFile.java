package model.factors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class CustomerArrivalFactorsFromFile {
  private Map factorsMap;

  //constructor
  public CustomerArrivalFactorsFromFile(String path) throws IOException {
    this(new BufferedReader(new FileReader(new File(path))));
  }

  public CustomerArrivalFactorsFromFile(StringReader reader) throws IOException {
    this(new BufferedReader(reader));
  }

  public CustomerArrivalFactorsFromFile(BufferedReader reader) throws IOException {

    Map<String, String> factorsMap = new HashMap<>();

    String thisLine = null;
    while ((thisLine = reader.readLine()) != null) {
      String[] line = thisLine.split(",");
      factorsMap.put(line[0], line[1]);
    }
    this.factorsMap = factorsMap;
  }

  public CustomerArrivalModelFactors geyCustomerArrivalModelFactors() throws IllegalStateException {

    try {
      LocalTime storeOpenTime = LocalTime.parse((CharSequence) factorsMap.get("StoreOpenTime"));
      LocalTime storeCloseTime = LocalTime.parse((CharSequence) factorsMap.get("StoreCloseTime"));
      LocalTime dinnerStartTime = LocalTime.parse((CharSequence) factorsMap.get("DinnerStartTime"));
      LocalTime dinnerEndTime = LocalTime.parse((CharSequence) factorsMap.get("DinnerEndTime"));
      LocalTime lunchStartTime = LocalTime.parse((CharSequence) factorsMap.get("LunchStartTime"));
      LocalTime lunchEndTime = LocalTime.parse((CharSequence) factorsMap.get("LunchEndTime"));

      Double lunchRushFactor = Double.valueOf((String) factorsMap.get("LunchRushFactor"));
      Double dinnerRushFactor = Double.valueOf((String) factorsMap.get("DinnerRushFactor"));
      Double grabAndGoFactor = Double.valueOf((String) factorsMap.get("GrabAndGoWeekendFactor"));
      double holidayFactor = Double.valueOf((String) factorsMap.get("HolidayFactor"));
      double dayBeforeHolidayFactor = Double.valueOf(
          (String) factorsMap.get("DayBeforeHolidayFactor"));
      double weekBeforeHolidayFactor = Double.valueOf(
          (String) factorsMap.get("WeekBeforeHolidayFactor"));

      int mon = Integer.valueOf((String) factorsMap.get("Monday"));
      int tue = Integer.valueOf((String) factorsMap.get("Tuesday"));
      int wed = Integer.valueOf((String) factorsMap.get("Wednesday"));
      int thur = Integer.valueOf((String) factorsMap.get("Thursday"));
      int fri = Integer.valueOf((String) factorsMap.get("Friday"));
      int sat = Integer.valueOf((String) factorsMap.get("Saturday"));
      int sun = Integer.valueOf((String) factorsMap.get("Sunday"));

      return new CustomerArrivalModelFactors(dinnerStartTime, dinnerEndTime,
          lunchStartTime, lunchEndTime, lunchRushFactor, dinnerRushFactor, storeOpenTime, storeCloseTime,
          mon, tue, wed, thur, fri, sat, sun, holidayFactor, dayBeforeHolidayFactor,
          weekBeforeHolidayFactor, grabAndGoFactor);

    } catch (IllegalStateException e) {
      throw new IllegalStateException("unable to read traffic factors from file");
    }

  }

}
