package console;

import java.sql.*;
import java.util.Scanner;

public class CreateScenario {

  public static void collectData(Connection connection) throws SQLException {

    Scanner scanner = new Scanner(System.in);

    String sqlQuery = "INSERT INTO TrafficFactor (TraffiCFactorName) value (?) ";

    PreparedStatement stmtTF = connection.prepareStatement(sqlQuery , Statement.RETURN_GENERATED_KEYS);

    System.out.println("Please enter traffic factor name");
    stmtTF.setString(1,scanner.nextLine());
    stmtTF.executeUpdate();



    ResultSet rs = stmtTF.getGeneratedKeys();
    rs.next();
    int TFID = rs.getInt(1);



    //prepare query statement
    String query = "INSERT INTO TFValue ( TFItem, ValueString , TrafficFactorID)  VALUE  (?, ?, ?) ;" ;

    CallableStatement stmt = connection.prepareCall(query);

    //create factors
    System.out.println("seniorStartTime(In the format of h:mm ex(06:00):");
    String Value = scanner.nextLine() ;
    Time seniorStartTime = java.sql.Time.valueOf(Value+":00");
    stmt.setString(1, "seniorStartTime");
    stmt.setString(2, Value);
    stmt.setInt(3, TFID);
    stmt.execute();

    System.out.println("seniorEndTime(In the format of h:mm ex(06:00):");
    Value = scanner.nextLine();
    Time seniorEndTime = java.sql.Time.valueOf(Value+":00");
    stmt.setString(1, "seniorEndTime");
    stmt.setString(2, Value);
    stmt.setInt(3, TFID);
    stmt.execute();

    System.out.println("seniorDayOfWeek(MONDAY):");
    Value = scanner.nextLine();
    String seniorDayOfWeek = Value.toUpperCase();
    stmt.setString(1,"seniorDayOfWeek");
    stmt.setString(2, Value);
    stmt.setInt(3, TFID);
    stmt.execute();

    System.out.println("dinnerStartTime In the format of h:mm ex(06:00)");
    Value = scanner.nextLine();
    Time dinnerStartTime = java.sql.Time.valueOf(Value+":00");
    stmt.setString(1,"dinnerStartTime");
    stmt.setString(2, Value);
    stmt.setInt(3, TFID);
    stmt.execute();

    System.out.println("dinnerEndTime: In the format of h:mm ex(06:00)");
    Value = scanner.nextLine();
    Time dinnerEndTime = java.sql.Time.valueOf(Value+":00");
    stmt.setString(1, "dinnerEndTime");
    stmt.setString(2, Value);
    stmt.setInt(3, TFID);
    stmt.execute();

    System.out.println("lunchStartTime: In the format of h:mm ex(06:00)");
    Value = scanner.nextLine();
    Time lunchStartTime = java.sql.Time.valueOf(Value+":00");
    stmt.setString(1, "lunchStartTime");
    stmt.setString(2, Value);
    stmt.setInt(3, TFID);
    stmt.execute();

    System.out.println("lunchEndTime:In the format of h:mm ex(06:00)");
    Value = scanner.nextLine();
    Time lunchEndTime = java.sql.Time.valueOf(Value+":00");
    stmt.setString(1, "lunchEndTime");
    stmt.setString(2, Value);
    stmt.setInt(3, TFID);
    stmt.execute();

    System.out.println("storeOpenTime: In the format of h:mm ex(06:00)");
    Value = scanner.nextLine();
    Time storeOpenTime = java.sql.Time.valueOf(Value+":00");
    stmt.setString(1, "storeOpenTime");
    stmt.setString(2, Value);
    stmt.setInt(3, TFID);
    stmt.execute();

    System.out.println("storeCloseTime: In the format of h:mm ex(06:00)");
    Value = scanner.nextLine();
    Time storeCloseTime = java.sql.Time.valueOf(Value+":00");
    stmt.setString(1, "storeCloseTime");
    stmt.setString(2, Value);
    stmt.setInt(3, TFID);
    stmt.execute();

    //******************************
    System.out.println("monday regular traffic(800): ");
    int mon = scanner.nextInt();
    stmt.setString(1, "mon");
    stmt.setString(2, Integer.toString(mon));
    stmt.setInt(3, TFID);
    stmt.execute();

    System.out.println("tue");
    int tue = scanner.nextInt();
    stmt.setString(1, "tue");
    stmt.setString(2, Integer.toString(tue));
    stmt.setInt(3, TFID);
    stmt.execute();

    System.out.println("wed");
    int wed = scanner.nextInt();
    stmt.setString(1, "wed");
    stmt.setString(2, Integer.toString(wed));
    stmt.setInt(3, TFID);
    stmt.execute();

    System.out.println("thur");
    int thur = scanner.nextInt();
    stmt.setString(1, "thur");
    stmt.setString(2, Integer.toString(thur));
    stmt.setInt(3, TFID);
    stmt.execute();

    System.out.println("fri");
    int fri = scanner.nextInt();
    stmt.setString(1, "fri");
    stmt.setString(2, Integer.toString(fri));
    stmt.setInt(3, TFID);
    stmt.execute();

    System.out.println("sat");
    int sat = scanner.nextInt();
    stmt.setString(1, "sat");
    stmt.setString(2, Integer.toString(sat));
    stmt.setInt(3, TFID);
    stmt.execute();

    System.out.println("sun");
    int sun = scanner.nextInt();
    stmt.setString(1, "sun");
    stmt.setString(2, Integer.toString(sun));
    stmt.setInt(3, TFID);
    stmt.execute();

    System.out.println("holidayFactor");
    double holidayFactor = scanner.nextDouble();
    stmt.setString(1, "holidayFactor");
    stmt.setString(2, Double.toString(holidayFactor));
    stmt.setInt(3, TFID);
    stmt.execute();

    System.out.println("dayBeforeHolidayFactor");
    double dayBeforeHolidayFactor = scanner.nextDouble();
    stmt.setString(1, "dayBeforeHolidayFactor");
    stmt.setString(2, Double.toString(dayBeforeHolidayFactor));
    stmt.setInt(3, TFID);
    stmt.execute();

    System.out.println("weekBeforeHolidayFactor");
    double weekBeforeHolidayFactor = scanner.nextDouble();
    stmt.setString(1, "weekBeforeHolidayFactor");
    stmt.setString(2, Double.toString(weekBeforeHolidayFactor));
    stmt.setInt(3, TFID);
    stmt.execute();

    System.out.println("lunchRushFactor");
    double lunchRushFactor = scanner.nextDouble();
    stmt.setString(1, "lunchRushFactor");
    stmt.setString(2, Double.toString(lunchRushFactor));
    stmt.setInt(3, TFID);
    stmt.execute();

    System.out.println("dinnerRushFactor");
    double dinnerRushFactor = scanner.nextDouble();
    stmt.setString(1, "dinnerRushFactor");
    stmt.setString(2, Double.toString(dinnerRushFactor));
    stmt.setInt(3, TFID);
    stmt.execute();

    System.out.println("grabAndGoFactor");
    double grabAndGoFactor = scanner.nextDouble();
    stmt.setString(1, "grabAndGoFactor");
    stmt.setString(2, Double.toString(grabAndGoFactor));
    stmt.setInt(3, TFID);
    stmt.execute();

    System.out.println("seniorDemographicPercent");
    double seniorDemographicPercent = scanner.nextDouble();
    stmt.setString(1, "seniorDemographicPercent");
    stmt.setString(2, Double.toString(seniorDemographicPercent));
    stmt.setInt(3, TFID);
    stmt.execute();

    System.out.println("percentOfTrafficThatIsSeniorDuringSeniorShopping");
    double percentOfTrafficThatIsSeniorDuringSeniorShopping = scanner.nextDouble();
    stmt.setString(1, "percentOfTrafficThatIsSeniorDuringSeniorShopping");
    stmt.setString(2, Double.toString(percentOfTrafficThatIsSeniorDuringSeniorShopping));
    stmt.setInt(3, TFID);
    stmt.execute();

    System.out.println("percentAdditionalGrabAndGoTraffic");
    double percentAdditionalGrabAndGoTraffic = scanner.nextDouble();
    stmt.setString(1, "percentAdditionalGrabAndGoTraffic");
    stmt.setString(2, Double.toString(percentAdditionalGrabAndGoTraffic));
    stmt.setInt(3, TFID);
    stmt.execute();

    System.out.println("grabAndGoShoppingTime");
    int grabAndGoShoppingTime = scanner.nextInt();
    stmt.setString(1, "grabAndGoShoppingTime");
    stmt.setString(2, Integer.toString(grabAndGoShoppingTime));
    stmt.setInt(3, TFID);
    stmt.execute();

    System.out.println("lunchShoppingTime");
    int lunchShoppingTime = scanner.nextInt();
    stmt.setString(1, "lunchShoppingTime");
    stmt.setString(2, Integer.toString(lunchShoppingTime));
    stmt.setInt(3, TFID);
    stmt.execute();

    System.out.println("dinnerSoppingTime");
    int dinnerSoppingTime = scanner.nextInt();
    stmt.setString(1, "dinnerSoppingTime");
    stmt.setString(2, Integer.toString(dinnerSoppingTime));
    stmt.setInt(3, TFID);
    stmt.execute();

    System.out.println("weekendShoppingTime");
    int weekendShoppingTime = scanner.nextInt();
    stmt.setString(1, "weekendShoppingTime");
    stmt.setString(2, Integer.toString(weekendShoppingTime));
    stmt.setInt(3, TFID);
    stmt.execute();

    System.out.println("seniorMinShoppingTime");
    int seniorMinShoppingTime = scanner.nextInt();
    stmt.setString(1, "seniorMinShoppingTime");
    stmt.setString(2, Integer.toString(seniorMinShoppingTime));
    stmt.setInt(3, TFID);
    stmt.execute();

    System.out.println("seniorMaxShoppingTime");
    int seniorMaxShoppingTime = scanner.nextInt();
    stmt.setString(1, "seniorMaxShoppingTime");
    stmt.setString(2, Integer.toString(seniorMaxShoppingTime));
    stmt.setInt(3, TFID);
    stmt.execute();

    System.out.println("nonSeniorAvgShoppingTime");
    int nonSeniorAvgShoppingTime = scanner.nextInt();
    stmt.setString(1, "nonSeniorAvgShoppingTime");
    stmt.setString(2, Integer.toString(nonSeniorAvgShoppingTime));
    stmt.setInt(3, TFID);
    stmt.execute();

    System.out.println("nonSeniorMinShoppingTime");
    int nonSeniorMinShoppingTime = scanner.nextInt();
    stmt.setString(1, "nonSeniorMinShoppingTime");
    stmt.setString(2, Integer.toString(nonSeniorMinShoppingTime));
    stmt.setInt(3, TFID);
    stmt.execute();

    System.out.println("nonSeniorMaxShoppingTime");
    int nonSeniorMaxShoppingTime = scanner.nextInt();
    stmt.setString(1, "nonSeniorMaxShoppingTime");
    stmt.setString(2, Integer.toString(nonSeniorMaxShoppingTime));
    stmt.setInt(3, TFID);
    stmt.execute();

    // 3. insert factors to db
    stmt.execute();

    Scanner scanner1 = new Scanner(System.in);
    // 2. creating new scenario
    String query2 = "CALL InsertScenario (?,?,?);";
    CallableStatement stmt2 = connection.prepareCall(query2);

    System.out.println("Start Date:(2015-03-31) ");
    Date startDate = Date.valueOf(scanner1.nextLine());
    stmt2.setDate(1,startDate);

    System.out.println("End Date:");
    Date endDate = Date.valueOf(scanner1.nextLine());
    stmt2.setDate(2,endDate);

    System.out.println("ScenarioDescription: ");
    String scenarioDescription = scanner1.nextLine();
    stmt2.setString(3, scenarioDescription);

    stmt2.execute();
  }
}
