package com.mahabang_parang.api_billing.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.mahabang_parang.api_billing.model.PaginationResponse;
import com.mahabang_parang.api_billing.model.Paginator;
import com.mahabang_parang.api_billing.model.dto.BillingDto;
import com.mahabang_parang.api_billing.model.entity.BillingEntity;
import com.mahabang_parang.api_billing.model.mapper.BillingMapper;
import com.mahabang_parang.api_billing.repository.BillingRepository;
import com.mahabang_parang.api_billing.service.BillingService;

@Service
public class BillingServiceImpl implements BillingService  {


     @Autowired
     private BillingRepository billingRepo;

     @Autowired
     private BillingMapper mapper;




     @Override
     public PaginationResponse<BillingDto> getBilling(int pageNumber, int pageSize) throws Exception {

          PageRequest pageable = PageRequest.of(pageNumber, pageSize);
          
          Page<BillingEntity> billing = billingRepo.findAll(pageable);

          return new PaginationResponse<>(
               billing.getContent(),
               billing.getNumber(),
               billing.getSize(),
               billing.getTotalPages(),
               billing.getTotalElements()
          );
              
     }

     @Override
     public PaginationResponse<BillingDto> getBillingByCustId(String customerId, int pageNumber, int pageSize) throws Exception {
          PageRequest pageable = PageRequest.of(pageNumber, pageSize);
          
          Page<BillingEntity> billing = billingRepo.findByCustomerId(customerId, pageable);

          Paginator paginator = new Paginator(
               billing.getNumber(),
               billing.getSize(),
               billing.getTotalPages(),
               billing.getTotalElements()
          );

          PaginationResponse<BillingDto> pageResponse = new PaginationResponse<BillingDto>();
          pageResponse.setData(billing.getContent());
          pageResponse.setPaginator(paginator);

          return pageResponse;
     }
     @Override
     public BillingDto getBillingById(String billingId) throws Exception { 

          BillingEntity bill = billingRepo.findById(billingId).orElseThrow();

          return mapper.toDto(bill);
     }

     @Override
     public BillingDto create(BillingDto billing) throws Exception {
          BillingEntity billingEntity = mapper.toEntity(billing);
          billingEntity.setReadingNo(generateControlNumber());
          billingEntity = billingRepo.save(billingEntity);
          return mapper.toDto(billingEntity);
     }

     @Override
     public List<BillingDto> getRecentBillingByCustomerId(String customerId) throws Exception {
          int maxRow = 20;

          //get billing records with incomplete status
          List<BillingEntity> incompleteBillings = billingRepo.findByCustomerIdAndStatus(customerId, 2, maxRow); // 3 = Incomplete transaction status

          int incompleteCount = incompleteBillings.size();
          int availableRecentBillCount = maxRow - incompleteCount; // number of completed transaction to show; max 20 rows

          //get billing records with complete status
          List<BillingEntity> recentBillings = billingRepo.findByCustomerIdAndStatus(customerId, 1, availableRecentBillCount); //1 = Complete transaction status

          List<BillingDto> billing = new ArrayList<BillingDto>();

          billing.addAll(mapper.toDtoList(incompleteBillings));
          billing.addAll(mapper.toDtoList(recentBillings));

          return billing;
     }
     
     @Override
     public List<BillingDto> getUnpaidBillingByCustomerId(String customerId) throws Exception {
          List<BillingEntity> incompleteBillings = billingRepo.findByCustomerIdAndStatus(customerId, 2, 20);
          return mapper.toDtoList(incompleteBillings);
     }
     
     @Override
     public String generateReadingNo() {
          return generateControlNumber();
     }

     @Override
     public Integer updateBillingStatus(String readingNo, Integer newStatus) throws Exception {
          Integer bill = billingRepo.updateBillingStatus(newStatus, readingNo);
          return bill;
     }

     private String generateControlNumber() {
          String datePrefix  = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
          String lastReadingNo = billingRepo.findLastReadingNo("R"+datePrefix);

          int sequence = 1;
          if(lastReadingNo != null) {
               int lastSequence = Integer.parseInt(lastReadingNo.substring(9));
               sequence = lastSequence + 1;
          }

          return "R" + datePrefix + String.format("%05d", sequence);
     }

}
