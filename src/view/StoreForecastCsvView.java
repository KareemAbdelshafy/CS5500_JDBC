package view;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import model.CustomerVisit;

public class StoreForecastCsvView implements StoreForecastView {

  private final BufferedWriter bufferedWriter;

  public StoreForecastCsvView(String filePath) throws IOException {
    this(new FileWriter(filePath));
  }
  public StoreForecastCsvView(FileWriter writer) {
    this(new BufferedWriter(writer));
  }

  public StoreForecastCsvView(BufferedWriter bufferedWriter) {
    this.bufferedWriter = bufferedWriter;
  }

  @Override
  public void setViewModel(List<CustomerVisit> customerRecords) throws IOException {
    // for every record write it to the BufferedWriter
    for (CustomerVisit customerRecordViewModel :customerRecords) {
      bufferedWriter.write(String.valueOf(customerRecordViewModel.getId())  );
      bufferedWriter.write(",");
      LocalDateTime localDateTime = customerRecordViewModel.getArrivalTime();
      bufferedWriter.write(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(localDateTime));
      bufferedWriter.write(",");
      bufferedWriter.write(String.valueOf(customerRecordViewModel.getMinutesInStore()));
      bufferedWriter.write(",");
      bufferedWriter.write(String.valueOf(customerRecordViewModel.getCustomerType()));
      bufferedWriter.newLine();
    }
    bufferedWriter.close();
  }
}
