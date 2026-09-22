package com.mahabang_parang.api_reports.model.interfaces;

import java.util.Date;

public interface UnpaidBillingReportProjection {
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
    Integer getStatus();
    Integer getM3Used();
    Double getAmountToPay();
    Double getAmountPaid();
    Double getRemainingBalance();
}
