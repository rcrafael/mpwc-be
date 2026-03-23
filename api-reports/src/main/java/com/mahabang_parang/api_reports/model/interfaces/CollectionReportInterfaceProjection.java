package com.mahabang_parang.api_reports.model.interfaces;

import java.util.Date;

public interface CollectionReportInterfaceProjection {
    String getOrNo();
    Date   getOrDate();
    Double getTotalAmountPay();
    Double getTotalShareAmount();
    Double getTotalBillingAmount();
    Double getSeniorDiscount();
    String getAccountNo();
    String getCustomerAlias();
    String getFirstName();
    String getMiddleName();
    String getLastName();
}
