package com.mahabang_parang.api_reports.model.interfaces;

import java.util.Date;

public interface BillingReportDetailedProjection {
    String getReadingNo();
    String getAccountNo();
    String getMeterNo();
    String getCustomerName();
    String getAlias();
    Integer getBranchCode();
    Integer getPhase();
    String getAddress();
    Integer getMonthOf();
    Date getDateFrom();
    Date getDateTo();
    Date getDueDate();
    Integer getPrevious();
    Integer getPresent();
    Integer getM3Used();
    Double getReadingAmount();
    Double getParts();
    Double getMaintenanceFee();
    Double getSeptageFee();
    Double getLessAmount();
    Double getTotalAmount();
}
