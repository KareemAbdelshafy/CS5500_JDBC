package model.factors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class TimeInStoreFactorsFromFile {
  private Map factorsMap;

  //constructor
  public TimeInStoreFactorsFromFile(String path) throws IOException {
    this(new BufferedReader(new FileReader(new File(path))));
  }

  public TimeInStoreFactorsFromFile(StringReader reader) throws IOException {
    this(new BufferedReader(reader));
  }

  public TimeInStoreFactorsFromFile(BufferedReader reader) throws IOException {

    Map<String, String> factorsMap = new HashMap<>();

    String thisLine = null;
    while ((thisLine = reader.readLine()) != null) {
      String[] line = thisLine.split(",");
      factorsMap.put(line[0], line[1]);
    }
    this.factorsMap = factorsMap;
  }

  public TimeInStoreFactors getTimeInStoreFactors() throws IllegalStateException {
    try {
      int grabAndGoShoppingTime = Integer.valueOf((String) factorsMap.get("GrabAndGoShoppingTime"));
      int lunchShoppingTime = Integer.valueOf((String) factorsMap.get("LunchShoppingTime"));
      int dinnerSoppingTime = Integer.valueOf((String) factorsMap.get("DinnerShoppingTime"));
      int weekendShoppingTime = Integer.valueOf((String) factorsMap.get("WeekendShoppingTime"));
      int seniorMinShoppingTime = Integer.valueOf((String) factorsMap.get("SeniorMinimunShoppingTime"));
      int seniorMaxShoppingTime = Integer.valueOf((String) factorsMap.get("SeniorMaxShoppingTime"));
      int nonSeniorAvgShoppingTime = Integer.valueOf((String) factorsMap.get("NonSeniorAverageShoppingTime"));
      int nonSeniorMinShoppingTime = Integer.valueOf((String) factorsMap.get("NonSeniorMinShoppingTime"));
      int nonSeniorMaxShoppingTime = Integer.valueOf((String) factorsMap.get("NonSeniorMaxShoppingTime"));

      TimeInStoreFactors timeInStoreFactors = new TimeInStoreFactors(grabAndGoShoppingTime, lunchShoppingTime,
              dinnerSoppingTime, weekendShoppingTime, seniorMinShoppingTime, seniorMaxShoppingTime,
              nonSeniorAvgShoppingTime, nonSeniorMinShoppingTime, nonSeniorMaxShoppingTime);
      return timeInStoreFactors;
    } catch (IllegalStateException e) {
      throw new IllegalStateException("unable to read traffic factors from file");
    }
  }


}



