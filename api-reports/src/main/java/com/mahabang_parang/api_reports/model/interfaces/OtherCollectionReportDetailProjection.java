package com.mahabang_parang.api_reports.model.interfaces;

import java.util.Date;

public interface OtherCollectionReportDetailProjection {

    // === Parent / Receipt Header fields ===
    String getOrNo();
    Date   getOrDate();
    String getAccountNo();
    String getCustomerName();
    String getAddress();
    Integer getStatus();
    Double getTotalAmountPay();

    // === Child / Detail fields ===
    String getOrdId();
    String getDescription();
    String getRemarks();
    Double getDetailTotalAmount();
}
