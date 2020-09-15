package model.factors;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * This {@code WeatherForecast} represents the weather forecast, the ability to determine what
 * the forecast is for a given day as well as the ability to read a holiday calendar from either
 * a file path OR a scanner.
 */
public class WeatherForecast implements IWeatherForecast {

  /**
   * This is a private collection of weatherforecasts.
   */
  private static class WeatherForecastDetail {

    private final LocalDate forecastOn;
    private final WeatherForecastType forecastType;

    protected WeatherForecastDetail(LocalDate forecastOn,
        WeatherForecastType forecastType) {
      this.forecastOn = forecastOn;
      this.forecastType = forecastType;
    }
  }

  /**
   * This class contains the Lexeme for parsing the input.
   */
  private static class Lexeme {

    static String COMMA = ",";
    static String NEW_LINE = System.lineSeparator();
    static String DELIMITER = "[" + COMMA + NEW_LINE + "]";
    static String ANYTHING = ".*";
    // https://www.oreilly.com/library/view/regular-expressions-cookbook/9781449327453/ch04s07.html
    static String ISO_DATE_FORMAT =
        "([0-9]{4})-?(1[0-2]|0[1-9])-?(3[01]|0[1-9]|[12][0-9])";
  }

  //final need to be add back in
  private Map<LocalDate, WeatherForecastDetail> forecastList;
  private Connection connection;

  private static final String sql = "SELECT "
      + "   WeatherForeCastDate"
      + "   , WeatherDescription\n"
      + "FROM \n"
      + "   WeatherForecastTable wf inner join WeatherType wt\n"
      + "      on wf.WeatherTypeID = wt.WeatherTypeID";

  /**
   * Construct an instance provided a scanner.
   *
   * @param scanner the scanner
   * @throws IOException when the scanner cannot parse input
   */
  protected WeatherForecast(Scanner scanner) throws IOException {
    this.forecastList = parse(scanner);
  }

  public WeatherForecast(Connection connection) throws SQLException {
    this.connection = connection;
    this.forecastList = readWeatherForecastTable();
  }

  //WeatherForecastDetail
  private Map<LocalDate, WeatherForecastDetail> readWeatherForecastTable() throws SQLException {
    Map<LocalDate, WeatherForecastDetail> map = new HashMap<>();
    Statement stmt = connection.createStatement();

    // todo: we shouldn't need two queries here, we should join them
    //       also select * is an anti-pattern - list fields out
    ResultSet result = stmt.executeQuery(sql);

    while (result.next()) {
      //list.add(result.getDate(2).toLocalDate());
      LocalDate currDate = result.getDate("WeatherForeCastDate").toLocalDate();
      WeatherForecastType weatherForecastType = parseForecastType(result.getString("WeatherDescription")) ;
      map.put(currDate, new WeatherForecastDetail(currDate, weatherForecastType));
    }
    return map;
  }

  /**
   * Construct an instance provided a path to an input file.
   *
   * @param path ther path to the input file
   * @throws IOException when the file cannot be read or there is an issue parsing
   */
  public WeatherForecast(String path) throws IOException {
    this(new Scanner(new File(path)));
  }

  /**
   * Process each line in the input.
   *
   * @param scanner the scanner
   * @return a map of WeatherForecastDetail records
   * @throws IOException when there is an issue parsing
   */
  private static Map<LocalDate, WeatherForecastDetail> parse(Scanner scanner) throws IOException {
    Map<LocalDate, WeatherForecastDetail> map = new HashMap<>();

    scanner.useDelimiter(Lexeme.DELIMITER);

    //read input
    while (scanner != null && scanner.hasNextLine()) {

      // read the date
      String isoDate = scanner.next(Lexeme.ISO_DATE_FORMAT);
      LocalDate date = LocalDate.parse(isoDate);

      // read the forecast
      String forecastString = scanner.next(Lexeme.ANYTHING);
      WeatherForecastType forecastType = parseForecastType(forecastString);

      map.put(date, new WeatherForecastDetail(date, forecastType));
    }

    return map;
  }

  /**
   * parse the WeatherForecastType from an input string.  If it is not known (not in the enum) then
   * default to Unknown.  Case sensitive parse.
   *
   * @param forecastString the input forecast as a string.
   * @return WeatherForecastType
   */
  private static WeatherForecastType parseForecastType(String forecastString) {
    try {
      return WeatherForecastType.valueOf(forecastString);
    } catch (IllegalArgumentException e) {
      // this is thrown when the string doesn't match something in the enum
      // intentionally blank
    }
    // if the forecast cannot be parsed assume unknown.
    return WeatherForecastType.Unknown;
  }

  @Override
  public WeatherForecastType getForecast(LocalDate date) {
    WeatherForecastDetail detail = this.forecastList.get(date);
    if (detail != null) {
      return detail.forecastType;
    }

    // default unknown
    return WeatherForecastType.Unknown;
  }

  //for test use only, need to be deleted
  public Map<LocalDate, WeatherForecastDetail> getForecastList() {
    return forecastList;
  }
}