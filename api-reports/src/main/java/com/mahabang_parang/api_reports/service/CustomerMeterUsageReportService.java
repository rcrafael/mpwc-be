package com.mahabang_parang.api_reports.service;

import java.util.List;
import com.mahabang_parang.api_reports.model.interfaces.CustomerMeterUsageInterfaceProjection;

public interface CustomerMeterUsageReportService {

    List<CustomerMeterUsageInterfaceProjection> getCustomerMeterUsageData(int year);

    byte[] generateCustomerMeterUsageExcel(int year);

}
