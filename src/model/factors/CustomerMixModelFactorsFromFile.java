package model.factors;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CustomerMixModelFactorsFromFile {
  private Map factorsMap;

  //constructor
  public CustomerMixModelFactorsFromFile(String path) throws IOException {
    this(new BufferedReader(new FileReader(new File(path))));
  }

  public CustomerMixModelFactorsFromFile(StringReader reader) throws IOException {
    this(new BufferedReader(reader));
  }

  public CustomerMixModelFactorsFromFile(BufferedReader reader) throws IOException {

    Map<String, String> factorsMap = new HashMap<>();

    String thisLine = null;
    while ((thisLine = reader.readLine()) != null) {
      String[] line = thisLine.split(",");
      factorsMap.put(line[0], line[1]);
    }
    this.factorsMap = factorsMap;
  }



  public CustomerMixModelFactors getCustomerMixModelFactors()  throws IllegalStateException {
    try {
      LocalTime seniorStartTime = LocalTime.parse((CharSequence) factorsMap.get("SeniorStartTime"));
      LocalTime seniorEndTime = LocalTime.parse((CharSequence) factorsMap.get("SeniorEndTime"));
      DayOfWeek seniorDayOfWeek = DayOfWeek.valueOf(((String) factorsMap.get("SeniorDayOfWeek")).toUpperCase());
      LocalTime dinnerStartTime = LocalTime.parse((CharSequence) factorsMap.get("DinnerStartTime"));
      LocalTime dinnerEndTime = LocalTime.parse((CharSequence) factorsMap.get("DinnerEndTime"));
      LocalTime lunchStartTime = LocalTime.parse((CharSequence) factorsMap.get("LunchStartTime"));
      LocalTime lunchEndTime = LocalTime.parse((CharSequence) factorsMap.get("LunchEndTime"));

      double lunchRushFactor = Double.valueOf((String) factorsMap.get("LunchRushFactor"));
      double dinnerRushFactor = Double.valueOf((String) factorsMap.get("DinnerRushFactor"));
      double grabAndGoFactor = Double.valueOf((String) factorsMap.get("GrabAndGoWeekendFactor"));
      double seniorDemographicPercent = Double.valueOf((String) factorsMap.get("PercentSeniorDemographic"));
      double percentOfTrafficThatIsSeniorDuringSeniorShopping = Double.valueOf((String) factorsMap.get("SeniorPercentageInSeniorDiscountHour"));

      CustomerMixModelFactors customerMixModelFactors = new CustomerMixModelFactors(seniorStartTime, seniorEndTime, seniorDayOfWeek,
              dinnerStartTime, dinnerEndTime,lunchStartTime,lunchEndTime,lunchRushFactor, dinnerRushFactor,
              grabAndGoFactor,seniorDemographicPercent,percentOfTrafficThatIsSeniorDuringSeniorShopping);
      return customerMixModelFactors;
    } catch (IllegalStateException e) {
      throw new IllegalStateException("unable to read traffic factors from file");
    }

  }

}
