package com.mahabang_parang.api_payment.model.interfaces;

import java.util.Date;

public interface OfficialReceiptTableListInterfaceProjection {

     String getId();
     String getOrNo();
     String getCustomerId();
     String getAccountNo();
     String getFirstName();
     String getLastName();
     String getMiddleName();
     String getAddress();
     Date getOrDate();
     Double getTotalBillingAmount();
     Double getTotalShareAmount();
     Double getTotalAmountPay();
     Double getSeniorDiscount();
     Date getEntryDate();
     String getCreatedBy();
     String getCheckNo();
     String getCheckBank();
     Date getCheckDate();
     Integer getStatus();
     Boolean getIsPartSale();
     Boolean getIsOutsideCollection();
     String getOutsideCustomerName();
     String getOutsideCustomerAddress();
     Boolean getIsDeleted();

}
