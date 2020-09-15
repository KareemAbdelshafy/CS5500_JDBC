package model.factors;

import static java.time.temporal.ChronoUnit.DAYS;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The {@code HolidayCalendar} provides a access to the HolidayTreatment of a given day as well
 * as the ability to read a holiday calendar from either a file path OR a scanner.
 */
public class HolidayCalendar<connection> implements IHolidayCalendar {
  private Connection connection = null;
  //private final String query = "call InsertCustomerVisit (?, ?, ?, ?,?);" ;

  /**
   * This class contains the Lexeme, or identifying characteristics within the input so that
   * we know how to parse it.
   */
  private static class Lexeme {

    static String NEW_LINE = System.lineSeparator();
    static String DELIMITER = NEW_LINE;
    // https://www.oreilly.com/library/view/regular-expressions-cookbook/9781449327453/ch04s07.html
    static String ISO_DATE_FORMAT =
        "([0-9]{4})-?(1[0-2]|0[1-9])-?(3[01]|0[1-9]|[12][0-9])";
  }

  private List<LocalDate> holidayList;

  /**
   * Construct an instance provided a scanner.
   *
   * @param scanner the scanner
   * @throws IOException when the scanner cannot parse input
   */
  protected HolidayCalendar(Scanner scanner) throws IOException {
    this.holidayList = parse(scanner);
  }

  /**
   * Construct an instance provided a path to an input file.
   *
   * @param path the path to the input file
   * @throws IOException when the file cannot be read or there is an issue parsing
   */
  public HolidayCalendar(String path) throws IOException {
    this(new Scanner(new File(path)));
  }

  public HolidayCalendar(Connection connection) throws SQLException {
    this.connection = connection;
    this.holidayList = ReadHolidayTable();
  }

  //*******************************************************************
  private List<LocalDate> ReadHolidayTable() throws SQLException {
    List<LocalDate> list = new ArrayList<>();

    Statement stmt = connection.createStatement();
    ResultSet result = stmt.executeQuery("SELECT * FROM HolidayTable");
    while (result.next()) {
      list.add(result.getDate(2).toLocalDate());
    }
    //System.out.println(list.toString());
    return list;
  }
  //***********************************************************************

  /**
   * Process each line in the input.
   *
   * @param scanner the scanner
   * @return a list of LocalDate
   * @throws IOException when there is an issue parsing
   */
  private List<LocalDate> parse(Scanner scanner) {
    List<LocalDate> list = new ArrayList<>();

    scanner.useDelimiter(Lexeme.DELIMITER);

    //read input
    while (scanner != null && scanner.hasNextLine()) {

      // read the date
      String isoDate = scanner.next(Lexeme.ISO_DATE_FORMAT);
      LocalDate date = LocalDate.parse(isoDate);

      list.add(date);
    }

    return list;
  }



  @Override
  public HolidayTreatment getHoliday(LocalDate dateOfEntry) {

    LocalDate nextHoliday = this.holidayList
        .stream()
        .filter(d -> d.compareTo(dateOfEntry) >= 0)
        .findFirst()
        .orElse(LocalDate.MAX);


    long daysBetween = DAYS.between(dateOfEntry, nextHoliday);

    if(daysBetween == 0) {
      return HolidayTreatment.Holiday;
    } else if (daysBetween == 1) {
      return HolidayTreatment.DayBeforeHoliday ;
    } else if (daysBetween < 7) {
      return HolidayTreatment.WeekBeforeHoliday ;
    }

    return HolidayTreatment.NonHoliday;
  }

  //test only, need to be deleted later!!!
  public List<LocalDate> getHolidayList() {
    return holidayList;
  }
}
