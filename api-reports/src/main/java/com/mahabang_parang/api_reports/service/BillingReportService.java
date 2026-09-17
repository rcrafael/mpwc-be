package com.mahabang_parang.api_reports.service;

import java.util.Date;

public interface BillingReportService {

    byte[] generateBillingReportSummary(Date startDate, Date endDate, Integer branchCode, Integer phase);

    byte[] generateBillingReportDetailed(Date startDate, Date endDate, Integer branchCode, Integer phase);

}
