package com.mahabang_parang.api_reports.service;

import com.mahabang_parang.api_reports.model.PaginationResponse;
import com.mahabang_parang.api_reports.model.interfaces.ShareCapCbuSavingsCollectionInterfaceProjection;
import com.mahabang_parang.api_reports.model.interfaces.ShareCapCbuSavingsCollectionListInterfaceProjection;

public interface CollectionReportService {

     public ShareCapCbuSavingsCollectionInterfaceProjection getShareCapCbuSavingsCollectionByCustomerId(String customerId);

     public ShareCapCbuSavingsCollectionInterfaceProjection getShareCapCbuSavingsCollectionByCustomerIdAndDateRange(
                                                            String customerId, 
                                                            String startDate, 
                                                            String endDate);

     public PaginationResponse<ShareCapCbuSavingsCollectionListInterfaceProjection> getShareCapCbuSavingsCollectionListByCustomerIdAndDateRange(
                                                            int pageNo,
                                                            int pageSize,
                                                            String customerId, 
                                                            String startDate, 
                                                            String endDate);

     public PaginationResponse<ShareCapCbuSavingsCollectionListInterfaceProjection> getShareCapCbuSavingsCollectionListByDateRange(
                                                            int pageNo,
                                                            int pageSize,
                                                            String startDate, 
                                                            String endDate);
}
