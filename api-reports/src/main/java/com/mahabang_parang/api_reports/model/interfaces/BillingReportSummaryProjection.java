package com.mahabang_parang.api_reports.model.interfaces;

import java.util.Date;

public interface BillingReportSummaryProjection {
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
    Integer getM3Used();
    Double getAmount();
}
