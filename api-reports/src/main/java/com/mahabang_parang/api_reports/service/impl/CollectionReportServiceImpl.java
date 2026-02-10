package com.mahabang_parang.api_reports.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mahabang_parang.api_billing.repository.BillingRepository;
import com.mahabang_parang.api_payment.repository.OfficialReceiptHeaderRepository;
import com.mahabang_parang.api_reports.model.PaginationResponse;
import com.mahabang_parang.api_reports.model.dto.DashboardCardDataDto;
import com.mahabang_parang.api_reports.model.interfaces.ShareCapCbuSavingsCollectionInterfaceProjection;
import com.mahabang_parang.api_reports.model.interfaces.ShareCapCbuSavingsCollectionListInterfaceProjection;
import com.mahabang_parang.api_reports.repository.ORCollectionRepository;
import com.mahabang_parang.api_reports.service.CollectionReportService;
import com.mahabang_parang.api_user.repository.CustomerMeterRepository;
import com.mahabang_parang.api_user.repository.CustomerRepository;

@Service
public class CollectionReportServiceImpl implements CollectionReportService {

     private final ORCollectionRepository orCollectionRepo;
     private final BillingRepository billingRepo;
     private final CustomerRepository customerRepo;
     private final CustomerMeterRepository customerMeterRepo;
     private final OfficialReceiptHeaderRepository orHeaderRepo;

     public CollectionReportServiceImpl(ORCollectionRepository _orCollectionRepo,
                                        BillingRepository _billingRepo,
                                        CustomerRepository _customerRepo,
                                        CustomerMeterRepository _customerMeterRepo,
                                        OfficialReceiptHeaderRepository _orHeaderRepo
     ) {
          this.orCollectionRepo = _orCollectionRepo;
          this.billingRepo = _billingRepo;
          this.customerRepo = _customerRepo;
          this.customerMeterRepo = _customerMeterRepo;
          this.orHeaderRepo = _orHeaderRepo;
     }

     @Override
     public ShareCapCbuSavingsCollectionInterfaceProjection getShareCapCbuSavingsCollectionByCustomerId(String customerId) {
          ShareCapCbuSavingsCollectionInterfaceProjection result = orCollectionRepo.getShareCapCbuSavingsCollectionByCustomerId(customerId);
          return result;
     }

     @Override
     public ShareCapCbuSavingsCollectionInterfaceProjection getShareCapCbuSavingsCollectionByCustomerIdAndDateRange(
                                                            String customerId, 
                                                            Date startDate, 
                                                            Date endDate) {
          ShareCapCbuSavingsCollectionInterfaceProjection result = 
                              orCollectionRepo.getShareCapCbuSavingsCollectionByCustomerIdAndDateRange(customerId, startDate, endDate);
          return result;
     }

     @Override
     public PaginationResponse<ShareCapCbuSavingsCollectionListInterfaceProjection> getShareCapCbuSavingsCollectionListByCustomerIdAndDateRange(
                                                            int pageNo,
                                                            int pageSize,
                                                            String customerId, 
                                                            Date startDate, 
                                                            Date endDate) {

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
                                                            Date startDate, 
                                                            Date endDate) {

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

     
     @Override
     public DashboardCardDataDto getDashboardCardData(Date startDate, Date endDate) {
          
          DashboardCardDataDto result = new DashboardCardDataDto();
          Long userCount = customerRepo.count();
          Long activeMeterCount = customerMeterRepo.countByStatus(6); //6 = Active
          Integer totalCubicMeterUsage =  billingRepo.totalCubicMeterUsageByDate(startDate, endDate);
          Double totalUnpaidBillingAmount = billingRepo.sumOfUnpaidBillingByDate(startDate, endDate);
          Double totalOfficialReceiptAmount = orHeaderRepo.sumOfOfficialReceiptByDate(startDate, endDate, false);
          Double totalOtherCollectionAmount = orHeaderRepo.sumOfOfficialReceiptByDate(startDate, endDate, true);
          double totalShareCapAmount = orCollectionRepo.sumOfShareCapitalByDateRange(startDate, endDate);
          double totalCbuAmount = orCollectionRepo.sumOfCbuByDateRange(startDate, endDate);

          result.setUserCount(userCount);
          result.setActiveMeterCount(activeMeterCount);
          result.setTotalCubicMeterUsage(totalCubicMeterUsage);
          result.setTotalUnpaidBillingAmount(totalUnpaidBillingAmount);
          result.setTotalOfficialReceiptAmount(totalOfficialReceiptAmount);
          result.setTotalOtherCollectionAmount(totalOtherCollectionAmount);
          result.setTotalShareCapAmount(totalShareCapAmount);
          result.setTotalCbuAmount(totalCbuAmount);
          
          return result;

     }


}
