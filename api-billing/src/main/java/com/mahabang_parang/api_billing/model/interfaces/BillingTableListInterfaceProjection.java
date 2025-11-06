package com.mahabang_parang.api_billing.model.interfaces;

import java.time.LocalDateTime;
import java.util.Date;

public interface BillingTableListInterfaceProjection {

     String getId();
     String getReadingNo();

     String getCustomerId();
     String getAccountNo();
     String getFirstName();
     String getLastName();
     String getMiddleName();
     String getAddress();

     String getMeterNo();
     String getAlias();
     Integer getBranchCode();

     Integer getPrevious();
     Integer getPresent();
     Integer getM3Used();
     Double getRating();
     Double getReadingAmount();
     Double getParts();
     Double getTotalAmount();
     Double getMaintenanceFee();
     Double getLessAmount();
     Double getSeptageFee();
     String getRemarks();
     Integer getMonthOf();
     Date getDateFrom();
     Date getDateTo();
     Date getDueDate();
     Integer getStatus();
     String getCreatedBy();
     LocalDateTime getCreatedDate();
     String getUpdatedBy();
     LocalDateTime getDateUpdated();


}
