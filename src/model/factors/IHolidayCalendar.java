package model.factors;

import java.time.LocalDate;

/**
 * The {@code IHolidayCalendar} provides a access to the HolidayTreatment of a given day.
 */
public interface IHolidayCalendar {

  /**
   * Determine the {@code HolidayTreatment} of a given day.
   * @param dateOfEntry the date in question.
   * @return {@code HolidayTreatment} indicating how to treat this date.
   */
  HolidayTreatment getHoliday(LocalDate dateOfEntry);

  public enum HolidayTreatment {NonHoliday, Holiday, DayBeforeHoliday, WeekBeforeHoliday}
}
