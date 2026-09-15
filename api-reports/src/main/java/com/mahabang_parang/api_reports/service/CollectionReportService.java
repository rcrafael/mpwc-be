package com.mahabang_parang.api_reports.service;

import java.util.Date;

import com.mahabang_parang.api_reports.model.PaginationResponse;
import com.mahabang_parang.api_reports.model.dto.DashboardCardDataDto;
import com.mahabang_parang.api_reports.model.interfaces.ShareCapCbuSavingsCollectionInterfaceProjection;
import com.mahabang_parang.api_reports.model.interfaces.ShareCapCbuSavingsCollectionListInterfaceProjection;

public interface CollectionReportService {

     public ShareCapCbuSavingsCollectionInterfaceProjection getShareCapCbuSavingsCollectionByCustomerId(String customerId);

     public ShareCapCbuSavingsCollectionInterfaceProjection getShareCapCbuSavingsCollectionByCustomerIdAndDateRange(
                                                            String customerId, 
                                                            Date startDate, 
                                                            Date endDate);

     public PaginationResponse<ShareCapCbuSavingsCollectionListInterfaceProjection> getShareCapCbuSavingsCollectionListByCustomerIdAndDateRange(
                                                            int pageNo,
                                                            int pageSize,
                                                            String customerId, 
                                                            Date startDate, 
                                                            Date endDate);

     public PaginationResponse<ShareCapCbuSavingsCollectionListInterfaceProjection> getShareCapCbuSavingsCollectionListByDateRange(
                                                            int pageNo,
                                                            int pageSize,
                                                            Date startDate, 
                                                            Date endDate);

     public DashboardCardDataDto getDashboardCardData(Date startDate, Date endDate);

     public byte[] generateCollectionReportExcel(Date startDate, Date endDate, Integer branchCode, Integer phase);

     public byte[] generateCollectionReportDetails(Date startDate, Date endDate, Integer branchCode, Integer phase);

     public byte[] generateOtherCollectionReportSummary(Date startDate, Date endDate);

     public byte[] generateOtherCollectionReportDetails(Date startDate, Date endDate);

     public byte[] generateOutsideCollectionReportSummary(Date startDate, Date endDate);

     public byte[] generateOutsideCollectionReportDetails(Date startDate, Date endDate);
}
