package com.mahabang_parang.api_reports.model.interfaces;

import java.util.Date;

public interface OtherCollectionReportSummaryProjection {
    String getOrNo();
    Date   getOrDate();
    String getAccountNo();
    String getCustomerName();
    String getAddress();
    Integer getStatus();
    Double getTotalAmountPay();
}
