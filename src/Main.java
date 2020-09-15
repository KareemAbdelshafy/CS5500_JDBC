import console.RunScenarioFromDb;
import console.RunScenarioFromFile;
import console.CreateScenario;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;

public class Main {

  public static void main(String[] args) throws IOException, SQLException {


    Connection connection = getConnectionToSql();

    //Adding factors through an interactive console.
    if (args[0].equals("create-factors")) {
      CreateScenario.collectData(connection);
    } else if (args[0].equals("select-scenario")) {
      RunScenarioFromDb.selectAndRun(connection);
    } else if (args[0].equals("run-from-file")) {
      System.out.println("Run from file started!");
      LocalDate fromDate = getFrom(args);
      LocalDate toDate = getTo(args, fromDate);
      System.out.println(fromDate + " "+ toDate);
      RunScenarioFromFile.run(fromDate, toDate);
      System.out.println("Run from file finished!");
    }
  }

  private static Connection getConnectionToSql() throws SQLException {

    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/newCS5500?" +
            "user=root&password=123456&serverTimezone=EST");
//    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/newCS5500" ,"root","123456");
    return conn;
  }

  /**
   * Create the start date from the console input or the default.
   *
   * @param args the console input
   * @return either the date parsed on the console OR today
   */
  private static LocalDate getFrom(String[] args) {
    if (args != null && args.length > 1) {
      try {
        return LocalDate.parse(args[1]);
      } catch (Exception e) {
        // swallow exception and return default
      }
    }
    // default to today
    return LocalDate.now();
  }

  /**
   * Create the end date from the console input or the default.
   *
   * @param args the console input
   * @return either the end parsed on the console OR 1 month from the start
   */
  private static LocalDate getTo(String[] args, LocalDate from) {
    if (args != null && args.length > 2) {
      try {
        return LocalDate.parse(args[2]);
      } catch (Exception e) {
        // swallow exception and return default
      }
    }
    // defajult to 1 month from start
    return from.plusMonths(1);
  }

}
