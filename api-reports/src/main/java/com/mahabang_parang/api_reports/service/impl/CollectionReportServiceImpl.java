package com.mahabang_parang.api_reports.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mahabang_parang.api_reports.model.PaginationResponse;
import com.mahabang_parang.api_reports.model.interfaces.ShareCapCbuSavingsCollectionInterfaceProjection;
import com.mahabang_parang.api_reports.model.interfaces.ShareCapCbuSavingsCollectionListInterfaceProjection;
import com.mahabang_parang.api_reports.repository.ORCollectionRepository;
import com.mahabang_parang.api_reports.service.CollectionReportService;

@Service
public class CollectionReportServiceImpl implements CollectionReportService {

     @Autowired
     private ORCollectionRepository orCollectionRepo;

     @Override
     public ShareCapCbuSavingsCollectionInterfaceProjection getShareCapCbuSavingsCollectionByCustomerId(String customerId) {
          ShareCapCbuSavingsCollectionInterfaceProjection result = orCollectionRepo.getShareCapCbuSavingsCollectionByCustomerId(customerId);
          return result;
     }

     @Override
     public ShareCapCbuSavingsCollectionInterfaceProjection getShareCapCbuSavingsCollectionByCustomerIdAndDateRange(
                                                            String customerId, 
                                                            String startDate, 
                                                            String endDate) {
          ShareCapCbuSavingsCollectionInterfaceProjection result = 
                              orCollectionRepo.getShareCapCbuSavingsCollectionByCustomerIdAndDateRange(customerId, startDate, endDate);
          return result;
     }

     @Override
     public PaginationResponse<ShareCapCbuSavingsCollectionListInterfaceProjection> getShareCapCbuSavingsCollectionListByCustomerIdAndDateRange(
                                                            int pageNo,
                                                            int pageSize,
                                                            String customerId, 
                                                            String startDate, 
                                                            String endDate) {

          Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("orDate").descending());

          Page<ShareCapCbuSavingsCollectionListInterfaceProjection> result = 
                              orCollectionRepo.getShareCapCbuSavingsCollectionListByCustomerIdAndDateRange(customerId, startDate, endDate, pageable);
          return new PaginationResponse<>(
               result.getContent(),
               result.getNumber(),
               result.getSize(),
               result.getTotalPages(),
               result.getTotalElements()
          );

     }

     @Override
     public PaginationResponse<ShareCapCbuSavingsCollectionListInterfaceProjection> getShareCapCbuSavingsCollectionListByDateRange(
                                                            int pageNo,
                                                            int pageSize,
                                                            String startDate, 
                                                            String endDate) {

          Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("orDate").descending());

          Page<ShareCapCbuSavingsCollectionListInterfaceProjection> result = 
                              orCollectionRepo.getShareCapCbuSavingsCollectionListByDateRange(startDate, endDate, pageable);
          return new PaginationResponse<>(
               result.getContent(),
               result.getNumber(),
               result.getSize(),
               result.getTotalPages(),
               result.getTotalElements()
          );

     }


}
