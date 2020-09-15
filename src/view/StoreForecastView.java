package view;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import model.CustomerVisit;

public interface StoreForecastView {

  void setViewModel(List<CustomerVisit> customerRecords) throws IOException, SQLException;
}
