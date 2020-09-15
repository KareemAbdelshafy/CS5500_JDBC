CREATE SCHEMA IF NOT EXISTS CS5500;
USE CS5500;


DROP TABLE IF EXISTS WeatherForecastTable;
DROP TABLE IF EXISTS HolidayTable;
DROP TABLE IF EXISTS CustomerVisit;
DROP TABLE IF EXISTS ScenarioTable;
DROP TABLE IF EXISTS TrafficFactor;
DROP TABLE IF EXISTS HolidayType;
DROP TABLE IF EXISTS WeatherType;
DROP TABLE IF EXISTS CustomerType;
DROP VIEW IF EXISTS customerVisitData;

-- -----------------------------------------------------
-- Table CustomerType
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS CustomerType (
  CustomerTypeID INT NOT NULL,
  CustomerDescription ENUM('Lunch', 'Dinner', 'Senior', 'GrabAndGo', 'NonSenior') NOT NULL,
  CONSTRAINT pk_CustomerType_CustomerTypeID
	PRIMARY KEY (CustomerTypeID)
);

-- -----------------------------------------------------
-- Table WeatherType
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS WeatherType (
  WeatherTypeID INT NOT NULL ,
  WeatherDescription ENUM('Unknown', 'ReallyNice') NOT NULL,
  CONSTRAINT pk_WeatherType_WeatherTypeID
  PRIMARY KEY (WeatherTypeID)
);

-- -----------------------------------------------------
-- Table HolidayType
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS HolidayType (
  HolidayTypeID INT NOT NULL,
  HolidayDescription ENUM('NonHoliday', 'Holiday', 'DayBeforeHoliday', 'WeekBeforeHoliday') NOT NULL,
  CONSTRAINT pk_HolidayType_HolidayTypeID
  PRIMARY KEY (HolidayTypeID)
);

-- -----------------------------------------------------
-- Table TrafficFactor
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS TrafficFactor (
  TrafficFactorID INT NOT NULL AUTO_INCREMENT,
  seniorStartTime TIME,
  seniorEndTime TIME,
  seniorDayOfWeek ENUM('MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY') NOT NULL,
  dinnerStartTime TIME,
  dinnerEndTime TIME,
  lunchStartTime TIME,
  lunchEndTime TIME,
  storeOpenTime TIME,
  storeCloseTime TIME,
  mon INT,
  tue INT,
  wed INT,
  thur INT,
  fri INT,
  sat INT,
  sun INT,
  holidayFactor Double,
  dayBeforeHolidayFactor Double,
  weekBeforeHolidayFactor Double,
  lunchRushFactor Double,
  dinnerRushFactor Double,
  grabAndGoFactor Double,
  seniorDemographicPercent Double,
  percentOfTrafficThatIsSeniorDuringSeniorShopping Double,
  percentAdditionalGrabAndGoTraffic Double,
  grabAndGoShoppingTime INT,
  lunchShoppingTime INT,
  dinnerSoppingTime INT,
  weekendShoppingTime INT,
  seniorMinShoppingTime INT,
  seniorMaxShoppingTime INT,
  nonSeniorAvgShoppingTime INT,
  nonSeniorMinShoppingTime INT,
  nonSeniorMaxShoppingTime INT,
  PRIMARY KEY (TrafficFactorID)
);


-- -----------------------------------------------------
-- Table ScenarioTable
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS ScenarioTable (
  ScenarioID INT NOT NULL AUTO_INCREMENT,
  TrafficFactorID INT NOT NULL,
  StartDate DATE NOT NULL,
  EndDate DATE NOT NULL,
  ScenarioDescription TEXT,
  CONSTRAINT pk_ScenarioTable_ScenarioID
	PRIMARY KEY (ScenarioID),
  CONSTRAINT fk_ScenarioTable_TrafficFactorID
    FOREIGN KEY (TrafficFactorID)
    REFERENCES TrafficFactor (TrafficFactorID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);


-- -----------------------------------------------------
-- Table CustomerVisit
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS CustomerVisit (
  CustomerVisitID INT NOT NULL AUTO_INCREMENT,
  WeatherTypeID INT NOT NULL,
  CustomerTypeID INT NOT NULL,
  HolidayTypeID INT NOT NULL,
  ArrivalTime DATETIME NOT NULL,
  MinuteShopping INT NOT NULL,
  ScenarioID INT NOT NULL,
  CONSTRAINT pk_CustomerVisit_CustomerVisitID
  PRIMARY KEY (CustomerVisitID),
  CONSTRAINT fk_CustomerVisit_ScenarioID
    FOREIGN KEY (ScenarioID)
    REFERENCES ScenarioTable (ScenarioID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_CustomerVisit_WeatherTypeID
    FOREIGN KEY (WeatherTypeID)
    REFERENCES WeatherType (WeatherTypeID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_CustomerVisit_CustomerTypeID
    FOREIGN KEY (CustomerTypeID)
    REFERENCES CustomerType (CustomerTypeID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_CustomerVisit_HolidayTypeID
    FOREIGN KEY (HolidayTypeID)
    REFERENCES HolidayType (HolidayTypeID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);



-- -----------------------------------------------------
-- Table HolidayTable
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS HolidayTable (
  HolidayID INT NOT NULL,
  HolidayDate DATE NOT NULL,
  CONSTRAINT pk_HolidayTable_HolidayID
	PRIMARY KEY (HolidayID)
);

-- -----------------------------------------------------
-- Table WeatherForecastTable
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS WeatherForecastTable (
  WeatherForecastID INT NOT NULL,
  WeatherForecastDate DATE NOT NULL,
  WeatherTypeID INT NOT NULL,
  CONSTRAINT pk_WeatherForecastTable_WeatherForecastID
  PRIMARY KEY (WeatherForecastID),
  CONSTRAINT fk_WeatherForecastTable_WeatherTypeID
    FOREIGN KEY (WeatherTypeID)
    REFERENCES WeatherType (WeatherTypeID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

INSERT INTO CustomerType (CustomerTypeID, CustomerDescription)
  VALUE ('1', 'Lunch'),
  ('2', 'Dinner'),
  ('3', 'Senior'),
  ('4', 'Nonsenior'),
  ('5', 'GrabAndGo');

INSERT INTO WeatherType (WeatherTypeID, WeatherDescription)
  VALUE ('1', 'Unknown'),
  ('2', 'ReallyNice');

INSERT INTO HolidayType (HolidayTypeID, HolidayDescription)
  VALUE ('1', 'NonHoliday'),
  ('2', 'Holiday'),
  ('3', 'DayBeforeHoliday'),
  ('4', 'WeekBeforeHoliday');

INSERT INTO HolidayTable(HolidayID, HolidayDate)
	VALUE ('1', '2020-05-25'),
    ('2', '2020-07-04'),
    ('3', '2020-03-08');

INSERT INTO WeatherForecastTable (WeatherForecastID, WeatherForecastDate, WeatherTypeID)
  VALUE ('1', '2020-06-01', '1'),
  ('2', '2020-06-02', '2'),
  ('3', '2020-06-03', '1'),
  ('4', '2020-06-04', '2');

  INSERT TrafficFactor (  seniorStartTime,  seniorEndTime,  seniorDayOfWeek,  dinnerStartTime,
                      dinnerEndTime,  lunchStartTime,  lunchEndTime,  storeOpenTime,  storeCloseTime,
                      mon,  tue,  wed,  thur,  fri,  sat,  sun,  holidayFactor,  dayBeforeHolidayFactor,
                      weekBeforeHolidayFactor,  lunchRushFactor,  dinnerRushFactor,  grabAndGoFactor,
                      seniorDemographicPercent,  percentOfTrafficThatIsSeniorDuringSeniorShopping,
                      percentAdditionalGrabAndGoTraffic,  grabAndGoShoppingTime,  lunchShoppingTime,
                      dinnerSoppingTime,  weekendShoppingTime,  seniorMinShoppingTime,  seniorMaxShoppingTime,
                      nonSeniorAvgShoppingTime,  nonSeniorMinShoppingTime, nonSeniorMaxShoppingTime)
  value (           '10:00',  '12:00',  'Tuesday',  '17:00',
                    '18:30',  '12:00',  '13:00',  '06:00',  '21:00',
                    800,  1000,  1200,  900,  2500,  4000,  5000,  0.2,  1.4,
                    1.15,  1.15, 1.1,  1.4,
                    0.15,  0.4,
                    0.4, 20,  10,
                    20,  60,  45, 60,
                    25,  6, 75),
        (           '13:00',  '15:00',  'Wednesday',  '17:00',
                      '18:30',  '12:00',  '13:00',  '06:00',  '21:00',
                      800,  1000,  1200,  900,  2500,  4000,  5000,  0.2,  1.4,
                      1.15,  1.15, 1.1,  1.4,
                      0.15,  0.4,
                      0.4, 20,  10,
                      20,  60,  45, 60,
                      25,  6, 75),
        (           '13:00',  '15:00',  'Wednesday',  '16:00',
                              '17:00',  '12:00',  '13:00',  '06:00',  '21:00',
                              800,  1000,  1200,  900,  2500,  4000,  5000,  0.2,  1.4,
                              1.15,  1.15, 1.1,  1.4,
                              0.15,  0.4,
                              0.4, 20,  10,
                              20,  60,  45, 60,
                              25,  6, 75);

INSERT INTO ScenarioTable ( TrafficFactorID, StartDate, EndDate, ScenarioDescription)
 VALUE ('1', '2020-05-23', '2020-05-27', 'Original Factors, May23-May27'),
    ('1', '2020-07-04', '2020-07-04', 'Original Factors, July4'),
    (2, '2020-06-23', '2020-06-24', 'Wed SeniorHOurs, June23-June24'),
    (3, '2020-06-22', '2020-06-28', 'SeniorHOurs Wed, dinner 4pm, June22-June28');

Create View customerVisitData as
select
	wt.WeatherDescription
    , ct.CustomerDescription
    , ht.HolidayDescription
    , DayName(cv.arrivalTime) as day
    , Hour(cv.arrivalTime) as hour
    , cv.arrivalTime
    , cv.minuteShopping
    , cv.scenarioId

from
	CustomerVisit cv inner join WeatherType wt
		on cv.WeatherTypeID = wt.WeatherTypeID
	inner join CustomerType ct
		on cv.customerTypeId = ct.customerTypeId
	inner join HolidayType ht
		on cv.holidayTypeId = ht.holidayTypeId
order by
	cv.arrivalTime;