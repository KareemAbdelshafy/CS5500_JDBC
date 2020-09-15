-- ----------------------------------------------------------------------
-- InsertCustomerVisit
-- ----------------------------------------------------------------------
DROP PROCEDURE IF EXISTS InsertCustomerVisit;

DELIMITER //
CREATE PROCEDURE InsertCustomerVisit

(IN IN_CustomerDescription VARCHAR(50), IN IN_HolidayDescription VARCHAR(50),
IN IN_WeatherDescription VARCHAR(50), IN IN_ArrivalTime DATETIME, IN IN_MinuteShopping INT, IN IN_ScenarioID INT)

BEGIN
SELECT CustomerTypeID INTO @CustomerTypeID FROM CustomerType WHERE CustomerDescription = IN_CustomerDescription;
SELECT WeatherTypeID INTO @WeatherTypeID FROM WeatherType  WHERE WeatherDescription = IN_WeatherDescription;
SELECT HolidayTypeID INTO @HolidayTypeID FROM HolidayType  WHERE HolidayDescription = IN_HolidayDescription;

INSERT INTO CustomerVisit (WeatherTypeID, CustomerTypeID, HolidayTypeID, ArrivalTime, MinuteShopping, ScenarioID )
VALUE (@WeatherTypeID, @CustomerTypeID, @HolidayTypeID, IN_ArrivalTime, IN_MinuteShopping, IN_ScenarioID);

END //
DELIMITER ;

-- ----------------------------------------------------------------------
-- InsertTrafficFactors
-- ----------------------------------------------------------------------
DROP PROCEDURE IF EXISTS InsertTrafficFactor;

DELIMITER //
CREATE PROCEDURE InsertTrafficFactor

(IN IN_seniorStartTime TIME, IN_seniorEndTime TIME, IN_seniorDayOfWeek ENUM
('MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'), IN_dinnerStartTime TIME,
IN_dinnerEndTime TIME, IN_lunchStartTime TIME, IN_lunchEndTime TIME, IN_storeOpenTime TIME, IN_storeCloseTime TIME,
IN_mon INT, IN_tue INT, IN_wed INT, IN_thur INT, IN_fri INT, IN_sat INT, IN_sun INT, IN_holidayFactor Double,
IN_dayBeforeHolidayFactor Double, IN_weekBeforeHolidayFactor Double, IN_lunchRushFactor Double,
IN_dinnerRushFactor Double, IN_grabAndGoFactor Double, IN_seniorDemographicPercent Double,
IN_percentOfTrafficThatIsSeniorDuringSeniorShopping Double, IN_percentAdditionalGrabAndGoTraffic Double,
IN_grabAndGoShoppingTime INT, IN_lunchShoppingTime INT, IN_dinnerSoppingTime INT, IN_weekendShoppingTime INT,
IN_seniorMinShoppingTime INT, IN_seniorMaxShoppingTime INT, IN_nonSeniorAvgShoppingTime INT,
IN_nonSeniorMinShoppingTime INT, IN_nonSeniorMaxShoppingTime INT)

BEGIN


INSERT INTO trafficfactor ( seniorStartTime, seniorEndTime, seniorDayOfWeek, dinnerStartTime,
dinnerEndTime, lunchStartTime, lunchEndTime, storeOpenTime, storeCloseTime, mon, tue, wed, thur, fri, sat, sun,
holidayFactor, dayBeforeHolidayFactor, weekBeforeHolidayFactor, lunchRushFactor, dinnerRushFactor, grabAndGoFactor,
seniorDemographicPercent, percentOfTrafficThatIsSeniorDuringSeniorShopping, percentAdditionalGrabAndGoTraffic,
grabAndGoShoppingTime, lunchShoppingTime, dinnerSoppingTime, weekendShoppingTime, seniorMinShoppingTime,
seniorMaxShoppingTime, nonSeniorAvgShoppingTime, nonSeniorMinShoppingTime, nonSeniorMaxShoppingTime)
VALUE (IN_seniorStartTime, IN_seniorEndTime, IN_seniorDayOfWeek, IN_dinnerStartTime, IN_dinnerEndTime,
IN_lunchStartTime, IN_lunchEndTime, IN_storeOpenTime, IN_storeCloseTime, IN_mon, IN_tue, IN_wed, IN_thur, IN_fri,
IN_sat, IN_sun, IN_holidayFactor, IN_dayBeforeHolidayFactor, IN_weekBeforeHolidayFactor, IN_lunchRushFactor,
IN_dinnerRushFactor, IN_grabAndGoFactor, IN_seniorDemographicPercent, IN_percentOfTrafficThatIsSeniorDuringSeniorShopping,
IN_percentAdditionalGrabAndGoTraffic, IN_grabAndGoShoppingTime, IN_lunchShoppingTime, IN_dinnerSoppingTime,
IN_weekendShoppingTime, IN_seniorMinShoppingTime, IN_seniorMaxShoppingTime, IN_nonSeniorAvgShoppingTime,
IN_nonSeniorMinShoppingTime, IN_nonSeniorMaxShoppingTime);


END //
DELIMITER ;

--
-- InsertScenario
--
DROP PROCEDURE IF EXISTS InsertScenario;

DELIMITER //
CREATE PROCEDURE InsertScenario

(IN IN_startDate date, IN IN_endDate date, IN IN_scenarioDescription VARCHAR(50))

BEGIN
SELECT max(TrafficFactorID) FROM trafficfactor INTO @TrafficFactorID;

INSERT INTO scenariotable ( TrafficFactorID, startDate, endDate, scenarioDescription)
VALUE ( @TrafficFactorID, IN_startDate, IN_endDate, IN_scenarioDescription);


END //
DELIMITER ;