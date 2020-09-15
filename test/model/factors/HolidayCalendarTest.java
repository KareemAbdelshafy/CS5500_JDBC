package model.factors;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;
import model.factors.IHolidayCalendar.HolidayTreatment;
import org.junit.Before;
import org.junit.Test;

public class HolidayCalendarTest {

  private LocalDate july4;
  private LocalDate july3;
  private LocalDate july2;
  private LocalDate jun28;
  private LocalDate jun27;
  private LocalDate jun26;


  private LocalDate jan2021;

  private IHolidayCalendar calendar;

  @Before
  public void setup() throws IOException {
    String csv = "2020-01-01" + System.lineSeparator()
        + "2020-05-16" + System.lineSeparator()
        + "2020-07-04" + System.lineSeparator()
        + "2020-11-26";

    calendar = new HolidayCalendar(new Scanner(csv));

    july4 = LocalDate.of(2020, 7, 4);
    july3 = LocalDate.of(2020, 7, 3);
    july2 = LocalDate.of(2020, 7, 2);
    jun28 = LocalDate.of(2020, 6, 28);
    jun27 = LocalDate.of(2020, 6, 27);
    jun26 = LocalDate.of(2020, 6, 26);

    jan2021 = LocalDate.of(2021, 1, 1);
  }

  @Test
  public void testGetHoliday() {
    assertEquals(HolidayTreatment.Holiday, calendar.getHoliday(july4));
  }

  @Test
  public void testGetDayBeforeHoliday() {
    assertEquals(HolidayTreatment.DayBeforeHoliday, calendar.getHoliday(july3));
  }

  @Test
  public void testGetWeekBeforeHolidayEnd() {
    assertEquals(HolidayTreatment.WeekBeforeHoliday, calendar.getHoliday(july2));
  }

  @Test
  public void testGetWeekBeforeHolidayStart() {
    assertEquals(HolidayTreatment.WeekBeforeHoliday, calendar.getHoliday(jun28));
  }

  @Test
  public void testGetNonHoliday() {
    assertEquals(HolidayTreatment.NonHoliday, calendar.getHoliday(jun27));
  }

  @Test
  public void testGetNonHolidayInFuture() {
    assertEquals(HolidayTreatment.NonHoliday, calendar.getHoliday(jan2021));
  }
}