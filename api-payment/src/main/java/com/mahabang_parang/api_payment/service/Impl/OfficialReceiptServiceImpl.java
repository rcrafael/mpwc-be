package com.mahabang_parang.api_payment.service.Impl;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mahabang_parang.api_billing.model.dto.BillingDto;
import com.mahabang_parang.api_billing.service.BillingService;
import com.mahabang_parang.api_payment.model.PaginationResponse;
import com.mahabang_parang.api_payment.model.dto.OfficialReceiptDetailsDto;
import com.mahabang_parang.api_payment.model.dto.OfficialReceiptDto;
import com.mahabang_parang.api_payment.model.dto.OfficialReceiptHeaderDto;
import com.mahabang_parang.api_payment.model.dto.OfficialReceiptHeaderWithCustomerDto;
import com.mahabang_parang.api_payment.model.dto.OtherPaymentDetailsDto;
import com.mahabang_parang.api_payment.model.dto.OtherPaymentDto;
import com.mahabang_parang.api_payment.model.dto.OtherPaymentHeaderDto;
import com.mahabang_parang.api_payment.model.dto.OtherPaymentHeaderWithCustomerDto;
import com.mahabang_parang.api_payment.model.dto.PrintReceiptDto;
import com.mahabang_parang.api_payment.model.dto.ShareCapitalDto;
import com.mahabang_parang.api_payment.model.dto.ShareCbuDto;
import com.mahabang_parang.api_payment.model.dto.ShareSavingsDto;
import com.mahabang_parang.api_payment.model.entity.OfficialReceiptDetailsEntity;
import com.mahabang_parang.api_payment.model.entity.OfficialReceiptHeaderEntity;
import com.mahabang_parang.api_payment.model.entity.ShareCapitalEntity;
import com.mahabang_parang.api_payment.model.entity.ShareCbuEntity;
import com.mahabang_parang.api_payment.model.entity.ShareSavingsEntity;
import com.mahabang_parang.api_payment.model.interfaces.OfficialReceiptTableListInterfaceProjection;
import com.mahabang_parang.api_payment.model.mapper.OfficialReceiptDetailsMapper;
import com.mahabang_parang.api_payment.model.mapper.OfficialReceiptHeaderMapper;
import com.mahabang_parang.api_payment.model.mapper.ShareCapitalMapper;
import com.mahabang_parang.api_payment.model.mapper.ShareCbuMapper;
import com.mahabang_parang.api_payment.model.mapper.ShareSavingsMapper;
import com.mahabang_parang.api_payment.model.payload.OfficiaReceiptPayload;
import com.mahabang_parang.api_payment.model.payload.OtherPaymentPayload;
import com.mahabang_parang.api_payment.model.payload.UpdateStatusPayload;
import com.mahabang_parang.api_payment.model.response.OfficialReceiptResponse;
import com.mahabang_parang.api_payment.model.response.OfficialReceiptTableListResponse;
import com.mahabang_parang.api_payment.model.response.OtherPaymentResponse;
import com.mahabang_parang.api_payment.repository.OfficialReceiptDetailsRepository;
import com.mahabang_parang.api_payment.repository.OfficialReceiptHeaderRepository;
import com.mahabang_parang.api_payment.repository.ShareCapitalRepository;
import com.mahabang_parang.api_payment.repository.ShareCbuRepository;
import com.mahabang_parang.api_payment.repository.ShareSavingsRepository;
import com.mahabang_parang.api_payment.service.OfficialReceiptService;
import com.mahabang_parang.api_payment.service.ReceiptService;
import com.mahabang_parang.api_user.model.dto.CustomerDto;
import com.mahabang_parang.api_user.service.CustomerService;

@Transactional(rollbackFor = Exception.class)
@Service
public class OfficialReceiptServiceImpl implements OfficialReceiptService {

     @Autowired
     private OfficialReceiptHeaderRepository orHeaderRepo;
     @Autowired
     private OfficialReceiptDetailsRepository orDetailsRepo;
     @Autowired
     private ShareCapitalRepository shareCapRepo;
     @Autowired
     private ShareCbuRepository cbuRepo;
     @Autowired
     private ShareSavingsRepository savingsRepo;

     @Autowired
     private BillingService billingService;
     @Autowired
     private CustomerService customerService;

     @Autowired
     private OfficialReceiptHeaderMapper orHeaderMapper;
     @Autowired
     private OfficialReceiptDetailsMapper orDetailsMapper;
     @Autowired
     private ShareCapitalMapper shareCapMapper;
     @Autowired
     private ShareCbuMapper cbuMapper;
     @Autowired
     private ShareSavingsMapper savingsMapper;

     @Autowired
     private ReceiptService receiptService;

     @Override
     public PaginationResponse<OfficialReceiptHeaderDto> getOfficialReceiptAll(int pageNo, int pageSize,
               String sortColumn) throws Exception {

          Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortColumn).descending());

          Page<OfficialReceiptHeaderEntity> orHeaders = orHeaderRepo.findByIsPartSaleAndIsDeleted(false, false,
                    pageable);

          return new PaginationResponse<>(
                    orHeaderMapper.toDtoList(orHeaders.getContent()),
                    orHeaders.getNumber(),
                    orHeaders.getSize(),
                    orHeaders.getTotalPages(),
                    orHeaders.getTotalElements());
     }

     @Override
     public PaginationResponse<OfficialReceiptHeaderDto> getOfficialReceiptByCustomerId(int pageNo, int pageSize,
               String customerId) throws Exception {

          Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("orDate").descending());

          Page<OfficialReceiptHeaderEntity> orHeaders = orHeaderRepo
                    .findByCustomerIdAndIsPartSaleAndIsDeleted(customerId, false, false, pageable);

          return new PaginationResponse<>(
                    orHeaderMapper.toDtoList(orHeaders.getContent()),
                    orHeaders.getNumber(),
                    orHeaders.getSize(),
                    orHeaders.getTotalPages(),
                    orHeaders.getTotalElements());
     }

     @Override
     public OfficialReceiptDto createOfficialReceipt(OfficiaReceiptPayload payload) throws Exception {

          OfficialReceiptHeaderEntity orHeaderEntity = orHeaderMapper.toEntity(payload.getHeader());

          String newOrNo = this.generateControlNumber();

          orHeaderEntity.setOrNo(newOrNo);
          orHeaderEntity.setIsPartSale(false);
          List<OfficialReceiptDetailsEntity> detailsList = orDetailsMapper.toEntityList(payload.getDetails());

          // set the orNo for the OR details
          for (OfficialReceiptDetailsEntity d : detailsList) {
               d.setOrNo(newOrNo);
          }

          OfficialReceiptDto result = new OfficialReceiptDto();
          OfficialReceiptHeaderEntity resultHeader = orHeaderRepo.save(orHeaderEntity);
          List<OfficialReceiptDetailsEntity> resultDetails = orDetailsRepo.saveAll(detailsList);

          for (OfficialReceiptDetailsDto details : payload.getDetails()) {
               billingService.updateBillingStatus(details.getReadingNo(), details.getBillingStatus());
          }

          ShareCapitalEntity shareCapitalEntity = new ShareCapitalEntity();
          if (payload.getShareCapital() != null) {
               shareCapitalEntity = shareCapMapper.toEntity(payload.getShareCapital());
               shareCapitalEntity.setOrNo(newOrNo);
               shareCapitalEntity = shareCapRepo.save(shareCapitalEntity);
               result.setShareCapital(shareCapMapper.toDto(shareCapitalEntity));
          }

          ShareCbuEntity cbuEntity = new ShareCbuEntity();
          if (payload.getCbu() != null) {
               cbuEntity = cbuMapper.toEntity(payload.getCbu());
               cbuEntity.setOrNo(newOrNo);
               cbuEntity = cbuRepo.save(cbuEntity);
               result.setCbu(cbuMapper.toDto(cbuEntity));
          }

          ShareSavingsEntity savingsEntity = new ShareSavingsEntity();
          if (payload.getSavings() != null) {
               savingsEntity = savingsMapper.toEntity(payload.getSavings());
               savingsEntity.setOrNo(newOrNo);
               savingsEntity = savingsRepo.save(savingsEntity);
               result.setSavings(savingsMapper.toDto(savingsEntity));
          }

          result.setHeader(orHeaderMapper.toDto(resultHeader));
          result.setDetails(orDetailsMapper.toDtoList(resultDetails));

          return result;

     }

     @Override
     public PaginationResponse<OfficialReceiptTableListResponse> getOfficialReceiptWithSearch(String keyword,
               int pageNo, int pageSize) throws Exception {
          Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("entryDate").descending());

          Page<OfficialReceiptTableListInterfaceProjection> orHeadersProjections = this.orHeaderRepo
                    .searchByCustomerNameOrAccountNoOrOrNo(keyword, keyword, keyword, false, pageable);

          Page<OfficialReceiptTableListResponse> orResponse = orHeadersProjections
                    .map(this::convertInterfaceToResponse);

          return new PaginationResponse<OfficialReceiptTableListResponse>(
                    orResponse.getContent(),
                    orResponse.getNumber(),
                    orResponse.getSize(),
                    orResponse.getTotalPages(),
                    orResponse.getTotalElements());

     }

     @Override
     public String generateOfficialReceiptNo() throws Exception {
          return generateControlNumber();
     }

     @Override
     public OtherPaymentDto createOtherCollection(OtherPaymentPayload payload) throws Exception {

          String newOrNo = this.generateControlNumber();

          OtherPaymentHeaderDto header = payload.getHeader();
          List<OtherPaymentDetailsDto> details = payload.getDetails();

          OfficialReceiptHeaderEntity entity = new OfficialReceiptHeaderEntity();
          entity.setOrNo(newOrNo);
          entity.setCustomerId(header.getCustomerId());
          entity.setOrDate(header.getOrDate());
          entity.setEntryDate(header.getEntryDate());
          entity.setCreatedBy(header.getCreatedBy());
          entity.setTotalBillingAmount(header.getTotalBillingAmount());
          entity.setTotalAmountPay(header.getTotalAmountPay());
          entity.setCheckNo(header.getCheckNo());
          entity.setCheckBank(header.getCheckBank());
          entity.setCheckDate(header.getCheckDate());
          entity.setStatus(header.getStatus());
          entity.setIsPartSale(true);
          entity.setIsOutsideCollection(header.getIsOutsideCollection());
          entity.setOutsideCustomerName(header.getOutsideCustomerName());
          entity.setOutsideCustomerAddress(header.getOutsideCustomerAddress());
          entity.setIsDeleted(false);

          List<OfficialReceiptDetailsEntity> detailsEntity = new ArrayList<>();
          for (OtherPaymentDetailsDto d : details) {
               OfficialReceiptDetailsEntity ent = new OfficialReceiptDetailsEntity();
               ent.setOrNo(newOrNo);
               ent.setPartId(d.getPartId());
               ent.setParts(d.getParts());
               ent.setOtherDescription(d.getOtherDescription());
               ent.setTotalAmount(d.getTotalAmount());
               ent.setRemarks(d.getRemarks());
               detailsEntity.add(ent);
          }

          OfficialReceiptHeaderEntity resultHeader = orHeaderRepo.save(entity);
          List<OfficialReceiptDetailsEntity> resultDetails = orDetailsRepo.saveAll(detailsEntity);

          OtherPaymentDto result = new OtherPaymentDto();
          result.setHeader(orHeaderMapper.toDto(resultHeader));
          result.setDetails(orDetailsMapper.toDtoList(resultDetails));

          return result;
     }

     @Override
     public PaginationResponse<OfficialReceiptTableListResponse> getOfficialReceiptWithCustomer(int pageNo,
               int pageSize, String sortColumn) throws Exception {
          Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortColumn).descending());

          Page<OfficialReceiptHeaderEntity> orHeaders = orHeaderRepo.findByIsPartSaleAndIsDeleted(false, false,
                    pageable);

          List<String> ids = new ArrayList<String>();
          for (OfficialReceiptHeaderEntity orh : orHeaders) {
               ids.add(orh.getCustomerId());
          }

          List<CustomerDto> customers = customerService.getCustomerByIds(ids);

          List<OfficialReceiptTableListResponse> orWithCust = new ArrayList<OfficialReceiptTableListResponse>();

          for (OfficialReceiptHeaderEntity orh : orHeaders) {
               OfficialReceiptTableListResponse orc = new OfficialReceiptTableListResponse();
               CustomerDto c = customers.stream().filter(cc -> cc.getId().equals(orh.getCustomerId())).findFirst()
                         .orElse(null);

               orc.setId(orh.getId());
               orc.setOrNo(orh.getOrNo());
               orc.setCustomerId(orh.getCustomerId());
               if(c != null) {
                    orc.setAccountNo(c.getAccountNo());
                    orc.setFirstName(c.getFirstName());
                    orc.setMiddleName(c.getMiddleName());
                    orc.setLastName(c.getLastName());
                    orc.setAddress(c.getAddress());
               }
               
               orc.setOrDate(orh.getOrDate());
               orc.setTotalBillingAmount(orh.getTotalBillingAmount());
               orc.setTotalShareAmount(orh.getTotalShareAmount());
               orc.setTotalAmountPay(orh.getTotalAmountPay());
               orc.setSeniorDiscount(orh.getSeniorDiscount());
               orc.setEntryDate(orh.getEntryDate());
               orc.setCreatedBy(orh.getCreatedBy());
               orc.setCheckNo(orh.getCheckNo());
               orc.setCheckBank(orh.getCheckBank());
               orc.setCheckDate(orh.getCheckDate());
               orc.setStatus(orh.getStatus());
               orc.setIsPartSale(orh.getIsPartSale());
               orc.setIsOutsideCollection(orh.getIsOutsideCollection());
               orc.setOutsideCustomerName(orh.getOutsideCustomerName());
               orWithCust.add(orc);
          }

          return new PaginationResponse<>(
                    orWithCust,
                    orHeaders.getNumber(),
                    orHeaders.getSize(),
                    orHeaders.getTotalPages(),
                    orHeaders.getTotalElements());
     }

     @Override
     public OfficialReceiptResponse getOfficialReceiptWithDetailsById(String id) throws Exception {
          OfficialReceiptHeaderEntity orHeader = orHeaderRepo.findById(id).orElseThrow();

          String orNo = orHeader.getOrNo();

          CustomerDto customer = customerService.getCustomer(orHeader.getCustomerId());
          List<OfficialReceiptDetailsDto> orDetails = orDetailsMapper.toDtoList(orDetailsRepo.findByOrNo(orNo));
          ShareCapitalDto shareCap = shareCapMapper.toDto(shareCapRepo.findByOrNo(orNo));
          ShareCbuDto cbu = cbuMapper.toDto(cbuRepo.findByOrNo(orNo));
          ShareSavingsDto savings = savingsMapper.toDto(savingsRepo.findByOrNo(orNo));
          OfficialReceiptHeaderWithCustomerDto orc = new OfficialReceiptHeaderWithCustomerDto();
          orc.setId(orHeader.getId());
          orc.setOrNo(orHeader.getOrNo());
          orc.setCustomerId(orHeader.getCustomerId());
          orc.setCustomer(customer);
          orc.setOrDate(orHeader.getOrDate());
          orc.setTotalBillingAmount(orHeader.getTotalBillingAmount());
          orc.setTotalShareAmount(orHeader.getTotalShareAmount());
          orc.setTotalAmountPay(orHeader.getTotalAmountPay());
          orc.setSeniorDiscount(orHeader.getSeniorDiscount());
          orc.setEntryDate(orHeader.getEntryDate());
          orc.setCreatedBy(orHeader.getCreatedBy());
          orc.setCheckNo(orHeader.getCheckNo());
          orc.setCheckBank(orHeader.getCheckBank());
          orc.setCheckDate(orHeader.getCheckDate());
          orc.setStatus(orHeader.getStatus());
          orc.setIsPartSale(orHeader.getIsPartSale());
          orc.setIsOutsideCollection(orHeader.getIsOutsideCollection());
          orc.setOutsideCustomerName(orHeader.getOutsideCustomerName());
          orc.setIsDeleted(orHeader.getIsDeleted());

          OfficialReceiptResponse or = new OfficialReceiptResponse();
          or.setHeader(orc);
          or.setDetails(orDetails);
          or.setShareCapital(shareCap);
          or.setCbu(cbu);
          or.setSavings(savings);
          return or;
     }

     @Override
     public PaginationResponse<OtherPaymentHeaderDto> getOtherCollectionAll(int pageNo, int pageSize, String sortColumn)
               throws Exception {

          Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortColumn).descending());

          Page<OfficialReceiptHeaderEntity> orHeaders = orHeaderRepo.findByIsPartSaleAndIsDeleted(true, false,
                    pageable);

          return new PaginationResponse<>(
                    orHeaderMapper.toDtoList(orHeaders.getContent()),
                    orHeaders.getNumber(),
                    orHeaders.getSize(),
                    orHeaders.getTotalPages(),
                    orHeaders.getTotalElements());

     }

     @Override
     public PaginationResponse<OtherPaymentHeaderDto> getOtherCollectionByCustomerId(int pageNo, int pageSize,
               String customerId) throws Exception {

          Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("orDate").descending());

          Page<OfficialReceiptHeaderEntity> orHeaders = orHeaderRepo
                    .findByCustomerIdAndIsPartSaleAndIsDeleted(customerId, true, false, pageable);

          return new PaginationResponse<>(
                    orHeaderMapper.toDtoList(orHeaders.getContent()),
                    orHeaders.getNumber(),
                    orHeaders.getSize(),
                    orHeaders.getTotalPages(),
                    orHeaders.getTotalElements());

     }

     @Override
     public PaginationResponse<OfficialReceiptTableListResponse> getOtherCollectionWithCustomer(int pageNo,
               int pageSize, String sortColumn) throws Exception {

          Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortColumn).descending());

          Page<OfficialReceiptHeaderEntity> orHeaders = orHeaderRepo.findByIsPartSaleAndIsDeleted(true, false,
                    pageable);

          List<String> ids = new ArrayList<String>();
          for (OfficialReceiptHeaderEntity orh : orHeaders) {
               ids.add(orh.getCustomerId());
          }

          List<CustomerDto> customers = customerService.getCustomerByIds(ids);

          List<OfficialReceiptTableListResponse> orWithCust = new ArrayList<OfficialReceiptTableListResponse>();

          for (OfficialReceiptHeaderEntity orh : orHeaders) {
               OfficialReceiptTableListResponse orc = new OfficialReceiptTableListResponse();
               CustomerDto c = customers.stream().filter(cc -> cc.getId().equals(orh.getCustomerId())).findFirst()
                         .orElse(null);

               orc.setId(orh.getId());
               orc.setOrNo(orh.getOrNo());
               orc.setCustomerId(orh.getCustomerId());
               if(c != null) {
                    orc.setAccountNo(c.getAccountNo());
                    orc.setFirstName(c.getFirstName());
                    orc.setMiddleName(c.getMiddleName());
                    orc.setLastName(c.getLastName());
                    orc.setAddress(c.getAddress());
               }
               orc.setOrDate(orh.getOrDate());
               orc.setTotalBillingAmount(orh.getTotalBillingAmount());
               orc.setTotalAmountPay(orh.getTotalAmountPay());
               orc.setEntryDate(orh.getEntryDate());
               orc.setCreatedBy(orh.getCreatedBy());
               orc.setCheckNo(orh.getCheckNo());
               orc.setCheckBank(orh.getCheckBank());
               orc.setCheckDate(orh.getCheckDate());
               orc.setStatus(orh.getStatus());
               orc.setIsPartSale(orh.getIsPartSale());
               orc.setIsOutsideCollection(orh.getIsOutsideCollection());
               orc.setOutsideCustomerName(orh.getOutsideCustomerName());
               orWithCust.add(orc);
          }

          return new PaginationResponse<>(
                    orWithCust,
                    orHeaders.getNumber(),
                    orHeaders.getSize(),
                    orHeaders.getTotalPages(),
                    orHeaders.getTotalElements());
     }

     @Override
     public OtherPaymentResponse getOtherCollectionWithDetailsById(String orhId) throws Exception {
          OfficialReceiptHeaderEntity orHeader = orHeaderRepo.findById(orhId).orElseThrow();

          String orNo = orHeader.getOrNo();

          CustomerDto customer = new CustomerDto();


          System.out.println("============================================================================");
          System.out.println("CUSTOMER ID: " + orHeader.getCustomerId());
          System.out.println("============================================================================");

          if (orHeader.getCustomerId() != null && !orHeader.getCustomerId().equals("")) {
               customer = customerService.getCustomer(orHeader.getCustomerId());
               System.out.println("CUSTOMER INFO: " + customer.toString());
               System.out.println("============================================================================");
          }

          List<OfficialReceiptDetailsDto> orDetails = orDetailsMapper.toDtoList(orDetailsRepo.findByOrNo(orNo));

          OtherPaymentHeaderWithCustomerDto orc = new OtherPaymentHeaderWithCustomerDto();
          orc.setId(orHeader.getId());
          orc.setOrNo(orHeader.getOrNo());
          orc.setCustomerId(orHeader.getCustomerId());
          orc.setCustomer(customer);
          orc.setOrDate(orHeader.getOrDate());
          orc.setTotalBillingAmount(orHeader.getTotalBillingAmount());
          orc.setTotalAmountPay(orHeader.getTotalAmountPay());
          orc.setEntryDate(orHeader.getEntryDate());
          orc.setCreatedBy(orHeader.getCreatedBy());
          orc.setCheckNo(orHeader.getCheckNo());
          orc.setCheckBank(orHeader.getCheckBank());
          orc.setCheckDate(orHeader.getCheckDate());
          orc.setStatus(orHeader.getStatus());
          orc.setIsPartSale(orHeader.getIsPartSale());
          orc.setIsOutsideCollection(orHeader.getIsOutsideCollection());
          orc.setOutsideCustomerName(orHeader.getOutsideCustomerName());
          orc.setIsDeleted(orHeader.getIsDeleted());

          OtherPaymentResponse or = new OtherPaymentResponse();
          or.setHeader(orc);
          or.setDetails(orDetails);
          return or;
     }

     @Override
     public PaginationResponse<OfficialReceiptTableListResponse> getOtherCollectionAllWithSearch(String keyword,
               int pageNo, int pageSize) throws Exception {
          Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("entryDate").descending());

          Page<OfficialReceiptTableListInterfaceProjection> orHeadersProjections = this.orHeaderRepo
                    .searchByCustomerNameOrAccountNoOrOrNo(keyword, keyword, keyword, true, pageable);

          Page<OfficialReceiptTableListResponse> orResponse = orHeadersProjections
                    .map(this::convertInterfaceToResponse);

          return new PaginationResponse<OfficialReceiptTableListResponse>(
                    orResponse.getContent(),
                    orResponse.getNumber(),
                    orResponse.getSize(),
                    orResponse.getTotalPages(),
                    orResponse.getTotalElements());
     }

     @Override
     public String deleteRecord(String id) throws Exception {
          OfficialReceiptHeaderEntity orHeader = orHeaderRepo.findById(id).orElseThrow();
          orHeader.setIsDeleted(true);
          orHeaderRepo.save(orHeader);
          return "Successfully deleted";
     }

     @Override
     public OfficialReceiptHeaderDto updateRecordStatus(UpdateStatusPayload payload) throws Exception {
          OfficialReceiptHeaderEntity orHeader = orHeaderRepo.findById(payload.getId()).orElseThrow();
          orHeader.setStatus(payload.getStatus());
          orHeaderRepo.save(orHeader);
          return orHeaderMapper.toDto(orHeader);
     }

     @Override
     public byte[] printExcelOR(String id) throws Exception {
          System.out.println("============================================================================");
          System.out.println("PRINT EXCEL API TRIGGERED!");
          System.err.println("============================================================================");

          System.out.println("GETTING OR HEADER WITH ID: " + id);
          OfficialReceiptHeaderEntity orHeader = orHeaderRepo.findById(id).orElseThrow();
          System.out.println("============================================================================");
          System.out.println("OFFICIAL RECEIPT: " + orHeader.toString());
          System.out.println("============================================================================");

          List<OfficialReceiptDetailsEntity> orDetails = new ArrayList<OfficialReceiptDetailsEntity>();
          if (orHeader != null) {
               System.out.println("GETTING OFFICIAL RECEIPT DETAILS WITH OR NO: " + orHeader.getOrNo());
               System.out.println("=================================================================");
               orDetails = orDetailsRepo.findByOrNo(orHeader.getOrNo());
               System.out.println("OFFICIAL RECEIPT DETAILS: " + orDetails.toString());
               System.out.println("=================================================================");
          }

          double totalReadingAmountPaid = 0;
          double totalParts = 0;
          double totalMembership = 0;
          double totalTapping = 0;
          double totalCalibration = 0;
          double totalReconnection = 0;
          double totalSubReceivable = 0;
          double totalPenalty = 0;
          double totalOtherCollection = 0;
          double totalMaintenanceFee = 0;
          double totalSeptageFee = 0;
          double totalSeniorDiscount = 0;
          double finalTotalAmount = 0;

          List<BillingDto> billing = new ArrayList<BillingDto>();

          if (orDetails.size() > 0) {

               totalReadingAmountPaid = orDetails.stream()
                         .mapToDouble(or -> Optional.ofNullable(or.getReadingAmountPaid()).orElse(0.0)).sum();
               totalParts = orDetails.stream().mapToDouble(or -> Optional.ofNullable(or.getParts()).orElse(0.0)).sum();
               totalMembership = orDetails.stream()
                         .mapToDouble(or -> Optional.ofNullable(or.getMembership()).orElse(0.0)).sum();
               totalTapping = orDetails.stream().mapToDouble(or -> Optional.ofNullable(or.getTapping()).orElse(0.0))
                         .sum();
               totalCalibration = orDetails.stream()
                         .mapToDouble(or -> Optional.ofNullable(or.getCalibration()).orElse(0.0)).sum();
               totalReconnection = orDetails.stream()
                         .mapToDouble(or -> Optional.ofNullable(or.getReconnection()).orElse(0.0)).sum();
               totalSubReceivable = orDetails.stream()
                         .mapToDouble(or -> Optional.ofNullable(or.getSubReceivable()).orElse(0.0)).sum();
               totalPenalty = orDetails.stream().mapToDouble(or -> Optional.ofNullable(or.getPenalty()).orElse(0.0))
                         .sum();
               totalOtherCollection = orDetails.stream()
                         .mapToDouble(or -> Optional.ofNullable(or.getOtherCollection()).orElse(0.0)).sum();
               totalMaintenanceFee = orDetails.stream()
                         .mapToDouble(or -> Optional.ofNullable(or.getMaintenanceFee()).orElse(0.0)).sum();
               totalSeptageFee = orDetails.stream()
                         .mapToDouble(or -> Optional.ofNullable(or.getSeptageFee()).orElse(0.0)).sum();
               totalSeniorDiscount = orDetails.stream()
                         .mapToDouble(or -> Optional.ofNullable(or.getSeniorDiscount()).orElse(0.0)).sum();
               finalTotalAmount = orDetails.stream()
                         .mapToDouble(or -> Optional.ofNullable(or.getTotalAmount()).orElse(0.0)).sum();

               if (!orHeader.getIsPartSale()) {
                    for (OfficialReceiptDetailsEntity or : orDetails) {
                         System.out.println("GETTING BILLING BY READING NO: " + or.getReadingNo());
                         System.out.println("=================================================================");
                         BillingDto b = billingService.getBillingByReadingNo(or.getReadingNo());
                         System.out.println("BILLING: " + b.toString());
                         System.out.println("=================================================================");
                         billing.add(b);
                    }
               }

          }

          String billingMonths = "";
          String meterNumbers = "";
          if (billing.size() > 0) {
               for (BillingDto b : billing) {
                    billingMonths = billingMonths + convertIntToMMM(b.getMonthOf()) + ", ";
                    meterNumbers = meterNumbers + b.getMeterNo() + ", ";
               }
               billingMonths = billingMonths.substring(0, billingMonths.length() - 2);
               meterNumbers = meterNumbers.substring(0, meterNumbers.length() - 2);
          }

          CustomerDto customer = new CustomerDto();
          if (orHeader.getCustomerId() != null && !orHeader.getCustomerId().equals("")) {
               customer = customerService.getCustomer(orHeader.getCustomerId());
          }

          System.out.println("=================================================================");
          System.out.println("CUSTOMER: " + customer.toString());
          System.out.println("=================================================================");

          PrintReceiptDto receipt = new PrintReceiptDto();
          receipt.setId(orHeader.getId());
          receipt.setOrNo(orHeader.getOrNo());
          receipt.setCustomerId(orHeader.getCustomerId());
          receipt.setCustomer(customer);
          receipt.setOutsideCustomerName(orHeader.getOutsideCustomerName());
          receipt.setOutsideCustomerAddress(orHeader.getOutsideCustomerAddress());
          receipt.setOrDate(orHeader.getOrDate());
          receipt.setMonthOf(billingMonths);
          receipt.setTotalBillingAmount(orHeader.getTotalBillingAmount());
          receipt.setTotalShareAmount(orHeader.getTotalShareAmount());
          receipt.setTotalAmountPay(orHeader.getTotalAmountPay());

          receipt.setMeterNo(meterNumbers);
          receipt.setReadingAmountPaid(totalReadingAmountPaid);
          receipt.setParts(totalParts);
          receipt.setMembership(totalMembership);
          receipt.setTapping(totalTapping);
          receipt.setCalibration(totalCalibration);
          receipt.setReconnection(totalReconnection);
          receipt.setSubReceivable(totalSubReceivable);
          receipt.setPenalty(totalPenalty);
          receipt.setOtherCollection(totalOtherCollection);
          receipt.setOtherDescription("");
          receipt.setMaintenanceFee(totalMaintenanceFee);
          receipt.setSeptageFee(totalSeptageFee);
          receipt.setSeniorDiscount(totalSeniorDiscount);
          receipt.setTotalAmount(finalTotalAmount);
          receipt.setRemarks("");

          return receiptService.generateExcelFromTemplate(receipt);
     }

     @Override
     public byte[] printPdfOR(String id) throws Exception {
          // First generate Excel
          byte[] excelBytes = printExcelOR(id);

          // Then convert to PDF
          return receiptService.convertExcelToPdf(excelBytes);
     }

     private OfficialReceiptTableListResponse convertInterfaceToResponse(
               OfficialReceiptTableListInterfaceProjection orInterface) {
          OfficialReceiptTableListResponse orResponse = new OfficialReceiptTableListResponse();
          orResponse.setId(orInterface.getId());
          orResponse.setOrNo(orInterface.getOrNo());
          orResponse.setCustomerId(orInterface.getCustomerId());
          orResponse.setFirstName(orInterface.getFirstName());
          orResponse.setLastName(orInterface.getLastName());
          orResponse.setMiddleName(orInterface.getMiddleName());
          orResponse.setAddress(orInterface.getAddress());
          orResponse.setOrDate(orInterface.getOrDate());
          orResponse.setTotalBillingAmount(orInterface.getTotalBillingAmount());
          orResponse.setTotalShareAmount(orInterface.getTotalShareAmount());
          orResponse.setTotalAmountPay(orInterface.getTotalAmountPay());
          orResponse.setSeniorDiscount(orInterface.getSeniorDiscount());
          orResponse.setEntryDate(orInterface.getEntryDate());
          orResponse.setCreatedBy(orInterface.getCreatedBy());
          orResponse.setCheckNo(orInterface.getCheckNo());
          orResponse.setCheckBank(orInterface.getCheckBank());
          orResponse.setCheckDate(orInterface.getCheckDate());
          orResponse.setStatus(orInterface.getStatus());
          orResponse.setIsPartSale(orInterface.getIsPartSale());
          orResponse.setIsOutsideCollection(orInterface.getIsOutsideCollection());
          orResponse.setOutsideCustomerName(orInterface.getOutsideCustomerName());
          orResponse.setOutsideCustomerAddress(orInterface.getOutsideCustomerAddress());
          orResponse.setIsDeleted(orInterface.getIsDeleted());
          return orResponse;
     }

     @Override
     public OfficialReceiptHeaderDto voidRecord(String id) throws Exception {

          System.out.println("=======================================================");
          System.out.println("VOIDING RECORD OR: " + id);
          System.out.println("=======================================================");

          UpdateStatusPayload payload = new UpdateStatusPayload();
          payload.setId(id);
          payload.setStatus(4);
          OfficialReceiptHeaderDto orHeader = this.updateRecordStatus(payload);

          List<OfficialReceiptDetailsDto> orDetails = orDetailsMapper.toDtoList(orDetailsRepo.findByOrNo(orHeader.getOrNo()));

          for (OfficialReceiptDetailsDto orDetail : orDetails) {
               if (orDetail.getReadingNo() != null) {
                    System.out.println("=======================================================");
                    System.out.println("VOIDING RECORD BILLING: " + orDetail.getReadingNo());
                    System.out.println("=======================================================");
                    billingService.updateBillingStatus(orDetail.getReadingNo(), 2); //2 - INCOMPLETE
               }
          }

          return orHeader;

     }

     private String generateControlNumber() {
          String datePrefix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
          String lastReadingNo = orHeaderRepo.findLastOrNo("OR" + datePrefix);

          int sequence = 1;
          if (lastReadingNo != null) {
               int lastSequence = Integer.parseInt(lastReadingNo.substring(10));
               sequence = lastSequence + 1;
          }

          return "OR" + datePrefix + String.format("%05d", sequence);
     }

     private String convertIntToMMM(int monthNumber) {
          if (monthNumber < 1 || monthNumber > 12) {
               throw new IllegalArgumentException("Month number must be between 1 and 12.");
          }
          Month month = Month.of(monthNumber);
          return month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH); // Or your desired locale
     }

}
