package com.mahabang_parang.api_billing.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.mahabang_parang.api_billing.model.interfaces.BillingTableListInterfaceProjection;
import com.mahabang_parang.api_billing.model.mapper.BillingMapper;
import com.mahabang_parang.api_billing.model.payload.UpdateBillingPayload;
import com.mahabang_parang.api_billing.model.response.BillingTableListResponse;
import com.mahabang_parang.api_billing.repository.BillingRepository;
import com.mahabang_parang.api_billing.service.BillingService;
import com.mahabang_parang.api_user.model.dto.CustomerDto;
import com.mahabang_parang.api_user.model.dto.CustomerMeterDto;
import com.mahabang_parang.api_user.service.CustomerMeterService;
import com.mahabang_parang.api_user.service.CustomerService;

@Service
public class BillingServiceImpl implements BillingService  {


     @Autowired
     private BillingRepository billingRepo;

     @Autowired
     private CustomerService custService;

     @Autowired
     private CustomerMeterService meterService;

     @Autowired
     private BillingMapper mapper;


     @Override
     public PaginationResponse<BillingTableListResponse> getBilling(int pageNumber, int pageSize) throws Exception {

          PageRequest pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdDate").descending());
          
          Page<BillingTableListInterfaceProjection> billingProjection = billingRepo.findAllBilling(pageable);

          Page<BillingTableListResponse> billingResponse = billingProjection.map(this::convertProjectionToDto);

          return new PaginationResponse<>(
               billingResponse.getContent(),
               billingResponse.getNumber(),
               billingResponse.getSize(),
               billingResponse.getTotalPages(),
               billingResponse.getTotalElements()
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
     public BillingWithCustomerInfoDto getBillingWithCustomerInfoById(String billingId) throws Exception { 

          BillingEntity bill = billingRepo.findById(billingId).orElseThrow();
          BillingDto billingDto = mapper.toDto(bill);

          CustomerDto customer = custService.getCustomer(billingDto.getCustomerId());
          BillingWithCustomerInfoDto billingWithCustomer = new BillingWithCustomerInfoDto();

          CustomerMeterDto meterInfo = meterService.getCustomerMeterByMeterNo(billingDto.getMeterNo());

          billingWithCustomer.setId(billingDto.getId());
          billingWithCustomer.setReadingNo(billingDto.getReadingNo());
          billingWithCustomer.setCustomerId(billingDto.getCustomerId());
          billingWithCustomer.setCustomer(customer);
          billingWithCustomer.setMeterNo(billingDto.getMeterNo());
          billingWithCustomer.setMeterInfo(meterInfo);
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
     public BillingDto getBillingById(String billingId) throws Exception { 

          BillingEntity bill = billingRepo.findById(billingId).orElseThrow();
          BillingDto billingDto = mapper.toDto(bill);

          return billingDto;
     }

     @Override
     public BillingDto getBillingByReadingNo(String readingNo) throws Exception {
          BillingEntity billingEntity = billingRepo.findByReadingNo(readingNo);
          return mapper.toDto(billingEntity);
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

     @Override
     public BillingDto update(UpdateBillingPayload payload) throws Exception {

          BillingDto billing = getBillingById(payload.getId());
          BillingEntity entity = mapper.toEntity(billing);

          LocalDateTime dateUpdated = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

          entity.setMonthOf(payload.getMonthOf());
          entity.setDateFrom(payload.getDateFrom());
          entity.setDateTo(payload.getDateTo());
          entity.setDueDate(payload.getDueDate());
          entity.setPrevious(payload.getPrevious());
          entity.setPresent(payload.getPresent());
          entity.setM3Used(payload.getM3Used());
          entity.setReadingAmount(payload.getReadingAmount());
          entity.setSeptageFee(payload.getSeptageFee());
          entity.setParts(payload.getParts());
          entity.setMaintenanceFee(payload.getMaintenanceFee());
          entity.setTotalAmount(payload.getTotalAmount());
          entity.setDateUpdated(dateUpdated);
          BillingDto billingUpdated = mapper.toDto(this.billingRepo.save(entity));
          
          return billingUpdated;

     }


     @Override
     public PaginationResponse<BillingTableListResponse> searchBilling(String keyword, int pageNumber, int pageSize) throws Exception {
          PageRequest pageable = PageRequest.of(pageNumber, pageSize);
          
          Page<BillingTableListInterfaceProjection> billingProjection = billingRepo.search(keyword, keyword, keyword, keyword, pageable);
          
          Page<BillingTableListResponse> billingResponse = billingProjection.map(this::convertProjectionToDto);


          return new PaginationResponse<BillingTableListResponse>(
               billingResponse.getContent(),
               billingResponse.getNumber(),
               billingResponse.getSize(),
               billingResponse.getTotalPages(),
               billingResponse.getTotalElements()
          );
          
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

     private BillingTableListResponse convertProjectionToDto(BillingTableListInterfaceProjection projection) {
          
          BillingTableListResponse billing = new BillingTableListResponse();

          billing.setId(projection.getId());
          billing.setReadingNo(projection.getReadingNo());
          billing.setCustomerId(projection.getCustomerId());
          billing.setAccountNo(projection.getAccountNo());
          billing.setFirstName(projection.getFirstName());
          billing.setLastName(projection.getLastName());
          billing.setMiddleName(projection.getMiddleName());
          billing.setAddress(projection.getAddress());
          billing.setMeterNo(projection.getMeterNo());
          billing.setAlias(projection.getAlias());
          billing.setBranchCode(projection.getBranchCode());
          billing.setPrevious(projection.getPrevious());
          billing.setPresent(projection.getPresent());
          billing.setM3Used(projection.getM3Used());
          billing.setRating(projection.getRating());
          billing.setReadingAmount(projection.getReadingAmount());
          billing.setParts(projection.getParts());
          billing.setTotalAmount(projection.getTotalAmount());
          billing.setMaintenanceFee(projection.getMaintenanceFee());
          billing.setLessAmount(projection.getLessAmount());
          billing.setSeptageFee(projection.getSeptageFee());
          billing.setRemarks(projection.getRemarks());
          billing.setMonthOf(projection.getMonthOf());
          billing.setDateFrom(projection.getDateFrom());
          billing.setDateTo(projection.getDateTo());
          billing.setDueDate(projection.getDueDate());
          billing.setStatus(projection.getStatus());
          billing.setCreatedBy(projection.getCreatedBy());
          billing.setCreatedDate(projection.getCreatedDate());
          billing.setUpdatedBy(projection.getUpdatedBy());
          billing.setDateUpdated(projection.getDateUpdated());

          return billing;

     }

}
