CREATE SCHEMA IF NOT EXISTS newCS5500;
USE newCS5500;


DROP TABLE IF EXISTS WeatherForecastTable;
DROP TABLE IF EXISTS HolidayTable;
DROP TABLE IF EXISTS CustomerVisit;
DROP TABLE IF EXISTS ScenarioTable;
DROP TABLE IF EXISTS HolidayType;
DROP TABLE IF EXISTS WeatherType;
DROP TABLE IF EXISTS CustomerType;
DROP VIEW IF EXISTS customerVisitData;
DROP TABLE IF EXISTS TFValue;
DROP TABLE IF EXISTS TFItem;
DROP TABLE IF EXISTS TrafficFactor;





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
-- Table TrafficFactorItem
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS TFItem (
  TFItem VARCHAR(100),
  PRIMARY KEY (TFItem)
  );

-- -----------------------------------------------------
-- Table TrafficFactor
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS TrafficFactor (
  TrafficFactorID INT NOT NULL AUTO_INCREMENT,
  TrafficFactorName VARCHAR(45),
  PRIMARY KEY (TrafficFactorID)
  );

-- -----------------------------------------------------
-- Table TrafficFactorValue
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS TFValue (
  TFValueID INT NOT NULL AUTO_INCREMENT,
  ValueString VARCHAR(45) NOT NULL,
  TFItem VARCHAR(100) NOT NULL,
  TrafficFactorID INT NOT NULL,
  PRIMARY KEY (TFValueID),
  FOREIGN KEY (TFItem) REFERENCES TFItem(TFItem),
  FOREIGN KEY (TrafficFactorID) REFERENCES TrafficFactor(TrafficFactorID)
  );



INSERT INTO TrafficFactor (TrafficFactorName)
  VALUE ('First Traffic Factor'), ('Second Traffic Factor'),('Third Traffic Factor');


INSERT INTO TFItem (TFItem)
  VALUE ('seniorStartTime'),('seniorEndTime'),('seniorDayOfWeek'),('dinnerStartTime'),
  ('dinnerEndTime'),('lunchStartTime'),('lunchEndTime'),('storeOpenTime'),('storeCloseTime'),
  ('mon') , ('tue') , ('wed') , ('thur'), ('fri') , ('sat'),('sun'),('holidayFactor'),
  ('dayBeforeHolidayFactor'),('weekBeforeHolidayFactor'),('lunchRushFactor'),('dinnerRushFactor'),
  ('grabAndGoFactor'), ('seniorDemographicPercent') ,('percentOfTrafficThatIsSeniorDuringSeniorShopping'),
 ('percentAdditionalGrabAndGoTraffic') , ('grabAndGoShoppingTime'),('lunchShoppingTime'),('dinnerSoppingTime'),
  ('weekendShoppingTime'),('seniorMinShoppingTime'),('seniorMaxShoppingTime'),('nonSeniorAvgShoppingTime'),
  ('nonSeniorMinShoppingTime'),('nonSeniorMaxShoppingTime');




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

INSERT INTO TFValue ( TFItem, ValueString , TrafficFactorID)
VALUE  ('seniorStartTime', '10:00', 1),
    ('seniorEndTime', '12:00' , 1),
     ('seniorDayOfWeek', 'Tuesday' , 1),
      ('dinnerStartTime','17:00',1),
      ('dinnerEndTime',  '18:30', 1),
      ('lunchStartTime',  '12:00', 1),
      ('lunchEndTime', '13:00' , 1),
       ('storeOpenTime',  '06:00' , 1),
        ('storeCloseTime', '21:00' , 1),
        ('mon',  '800' , 1),
        ('tue',  '1000' , 1),
        ('wed',  '1200' , 1),
        ('thur', '900' , 1),
         ('fri',  '2500' , 1),
         ('sat',  '4000' , 1),
         ('sun',  '5000' , 1),
         ('holidayFactor',  '0.2' , 1),
          ('dayBeforeHolidayFactor', '1.4',1),
          ('weekBeforeHolidayFactor', '1.15' , 1),
           ('lunchRushFactor', '1.15' ,1),
            ('dinnerRushFactor', '1.1' , 1),
             ('grabAndGoFactor', '1.4' , 1),
             ('seniorDemographicPercent', '0.15' ,1),
             ('percentOfTrafficThatIsSeniorDuringSeniorShopping', '0.4',1),
             ('percentAdditionalGrabAndGoTraffic', '0.4' , 1),
              ('grabAndGoShoppingTime',  '20' , 1),
              ('lunchShoppingTime', '10' , 1),
                      ('dinnerSoppingTime',  '20',1),
                      ('weekendShoppingTime', '60',1),
                       ('seniorMinShoppingTime',  '45',1),
                       ('seniorMaxShoppingTime', '60' ,1),
                      ('nonSeniorAvgShoppingTime', '25' ,1),
                       ('nonSeniorMinShoppingTime', '6',1),
                       ('nonSeniorMaxShoppingTime' , '75',1);


INSERT INTO ScenarioTable ( TrafficFactorID, StartDate, EndDate, ScenarioDescription)
 VALUE (1, '2020-05-23', '2020-05-27', 'Original Factors, May23-May27'),
    (1, '2020-07-04', '2020-07-04', 'Original Factors, July4');

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

/**/