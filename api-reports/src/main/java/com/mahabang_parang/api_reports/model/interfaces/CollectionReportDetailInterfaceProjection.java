package com.mahabang_parang.api_reports.model.interfaces;

import java.util.Date;

public interface CollectionReportDetailInterfaceProjection {

    // === Header fields ===
    String getOrNo();
    Date   getOrDate();
    Double getTotalBillingAmount();
    Double getTotalShareAmount();
    Double getSeniorDiscount();
    Double getTotalAmountPay();

    // === Customer fields ===
    String getAccountNo();
    String getCustomerAlias();
    String getFirstName();
    String getMiddleName();
    String getLastName();

    // === Detail / meter fields ===
    String  getMeterNo();
    Integer getBranchCode();
    Integer getPhase();
    Double  getReadingAmountPaid();
    Double  getParts();
    Double  getMembership();
    Double  getTapping();
    Double  getCalibration();
    Double  getReconnection();
    Double  getSubReceivable();
    Double  getPenalty();
    Double  getOtherCollection();
    String  getOtherDescription();
    Double  getMaintenanceFee();
    Double  getDetailSeniorDiscount();
    Double  getSeptageFee();
    Double  getDetailTotalAmount();
    String  getRemarks();
}
