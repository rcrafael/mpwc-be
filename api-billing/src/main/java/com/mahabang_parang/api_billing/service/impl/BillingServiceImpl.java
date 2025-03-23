package com.mahabang_parang.api_billing.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.mahabang_parang.api_billing.model.PaginationResponse;
import com.mahabang_parang.api_billing.model.Paginator;
import com.mahabang_parang.api_billing.model.dto.BillingDto;
import com.mahabang_parang.api_billing.model.dto.BillingWithCustomerInfoDto;
import com.mahabang_parang.api_billing.model.entity.BillingEntity;
import com.mahabang_parang.api_billing.model.mapper.BillingMapper;
import com.mahabang_parang.api_billing.repository.BillingRepository;
import com.mahabang_parang.api_billing.service.BillingService;
import com.mahabang_parang.api_user.model.dto.CustomerDto;
import com.mahabang_parang.api_user.service.CustomerService;

@Service
public class BillingServiceImpl implements BillingService  {


     @Autowired
     private BillingRepository billingRepo;

     @Autowired
     private CustomerService custService;

     @Autowired
     private BillingMapper mapper;




     @Override
     public PaginationResponse<BillingDto> getBilling(int pageNumber, int pageSize) throws Exception {

          PageRequest pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdDate").descending());
          
          Page<BillingEntity> billing = billingRepo.findAll(pageable);

          List<String> ids = new ArrayList<String>();
          for(BillingEntity b : billing) {
               ids.add(b.getCustomerId());
          }

          List<CustomerDto> cust = custService.getCustomerByIds(ids);

          System.out.println("CUSTOMER COUNT: " + cust.size());


          List<BillingWithCustomerInfoDto> billingTable = new ArrayList<BillingWithCustomerInfoDto>();
          
          for(BillingEntity b : billing) {
               BillingWithCustomerInfoDto bill = new BillingWithCustomerInfoDto();
               CustomerDto c = new CustomerDto();

               for(CustomerDto cc : cust) { 
                    
                    if(cc.getId().equals(b.getCustomerId())) {
                         c = cc;
                         System.out.println("CUSTOMER: " + cc.toString());
                         break;
                    }
               }

               bill.setId(b.getId());
               bill.setReadingNo(b.getReadingNo());
               bill.setCustomerId(b.getCustomerId());
               bill.setCustomer(c);
               bill.setMeterNo(b.getMeterNo());
               bill.setPrevious(b.getPrevious());
               bill.setPresent(b.getPresent()); 
               bill.setM3Used(b.getM3Used());
               bill.setReadingAmount(b.getReadingAmount());
               bill.setParts(b.getParts());      
               bill.setTotalAmount(b.getTotalAmount());
               bill.setMaintenanceFee(b.getMaintenanceFee());
               bill.setLessAmount(b.getLessAmount());
               bill.setSeptageFee(b.getSeptageFee());
               bill.setRemarks(b.getRemarks());
               bill.setMonthOf(b.getMonthOf());
               bill.setDateFrom(b.getDateFrom());
               bill.setDateTo(b.getDateTo());
               bill.setDueDate(b.getDueDate());
               bill.setStatus(b.getStatus());
               bill.setCreatedBy(b.getCreatedBy());
               bill.setCreatedDate(b.getCreatedDate());
               bill.setUpdatedBy(b.getUpdatedBy());
               bill.setDateUpdated(b.getDateUpdated());
               billingTable.add(bill);
          }

          return new PaginationResponse<>(
               billingTable,
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
     public BillingWithCustomerInfoDto getBillingById(String billingId) throws Exception { 

          BillingEntity bill = billingRepo.findById(billingId).orElseThrow();
          BillingDto billingDto = mapper.toDto(bill);

          CustomerDto customer = custService.getCustomer(billingDto.getCustomerId());
          BillingWithCustomerInfoDto billingWithCustomer = new BillingWithCustomerInfoDto();

          billingWithCustomer.setId(billingDto.getId());
          billingWithCustomer.setReadingNo(billingDto.getReadingNo());
          billingWithCustomer.setCustomerId(billingDto.getCustomerId());
          billingWithCustomer.setCustomer(customer);
          billingWithCustomer.setMeterNo(billingDto.getMeterNo());
          billingWithCustomer.setPrevious(billingDto.getPrevious());
          billingWithCustomer.setPresent(billingDto.getPresent()); 
          billingWithCustomer.setM3Used(billingDto.getM3Used());
          billingWithCustomer.setReadingAmount(billingDto.getReadingAmount());
          billingWithCustomer.setParts(billingDto.getParts());      
          billingWithCustomer.setTotalAmount(billingDto.getTotalAmount());
          billingWithCustomer.setMaintenanceFee(billingDto.getMaintenanceFee());
          billingWithCustomer.setLessAmount(billingDto.getLessAmount());
          billingWithCustomer.setSeptageFee(billingDto.getSeptageFee());
          billingWithCustomer.setRemarks(billingDto.getRemarks());
          billingWithCustomer.setMonthOf(billingDto.getMonthOf());
          billingWithCustomer.setDateFrom(billingDto.getDateFrom());
          billingWithCustomer.setDateTo(billingDto.getDateTo());
          billingWithCustomer.setDueDate(billingDto.getDueDate());
          billingWithCustomer.setStatus(billingDto.getStatus());
          billingWithCustomer.setCreatedBy(billingDto.getCreatedBy());
          billingWithCustomer.setCreatedDate(billingDto.getCreatedDate());
          billingWithCustomer.setUpdatedBy(billingDto.getUpdatedBy());
          billingWithCustomer.setDateUpdated(billingDto.getDateUpdated());

          return billingWithCustomer;
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
          List<BillingEntity> incompleteBillings = billingRepo.findByCustomerIdAndStatus(customerId, 2, maxRow); // 2 = Incomplete transaction status

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
