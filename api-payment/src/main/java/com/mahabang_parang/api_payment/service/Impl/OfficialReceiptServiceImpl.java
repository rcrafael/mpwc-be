package com.mahabang_parang.api_payment.service.Impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.print.attribute.standard.MediaSize.Other;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.mahabang_parang.api_payment.model.dto.ShareCapitalDto;
import com.mahabang_parang.api_payment.model.dto.ShareCbuDto;
import com.mahabang_parang.api_payment.model.dto.ShareSavingsDto;
import com.mahabang_parang.api_payment.model.entity.OfficialReceiptDetailsEntity;
import com.mahabang_parang.api_payment.model.entity.OfficialReceiptHeaderEntity;
import com.mahabang_parang.api_payment.model.entity.ShareCapitalEntity;
import com.mahabang_parang.api_payment.model.entity.ShareCbuEntity;
import com.mahabang_parang.api_payment.model.entity.ShareSavingsEntity;
import com.mahabang_parang.api_payment.model.mapper.OfficialReceiptDetailsMapper;
import com.mahabang_parang.api_payment.model.mapper.OfficialReceiptHeaderMapper;
import com.mahabang_parang.api_payment.model.mapper.ShareCapitalMapper;
import com.mahabang_parang.api_payment.model.mapper.ShareCbuMapper;
import com.mahabang_parang.api_payment.model.mapper.ShareSavingsMapper;
import com.mahabang_parang.api_payment.model.payload.OfficiaReceiptPayload;
import com.mahabang_parang.api_payment.model.payload.OtherPaymentPayload;
import com.mahabang_parang.api_payment.model.response.OfficialReceiptResponse;
import com.mahabang_parang.api_payment.model.response.OtherPaymentResponse;
import com.mahabang_parang.api_payment.repository.OfficialReceiptDetailsRepository;
import com.mahabang_parang.api_payment.repository.OfficialReceiptHeaderRepository;
import com.mahabang_parang.api_payment.repository.ShareCapitalRepository;
import com.mahabang_parang.api_payment.repository.ShareCbuRepository;
import com.mahabang_parang.api_payment.repository.ShareSavingsRepository;
import com.mahabang_parang.api_payment.service.OfficialReceiptService;
import com.mahabang_parang.api_user.model.dto.CustomerDto;
import com.mahabang_parang.api_user.service.CustomerService;

@Transactional
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

     @Override
     public PaginationResponse<OfficialReceiptHeaderDto> getOfficialReceiptAll(int pageNo, int pageSize, String sortColumn) throws Exception {

          Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortColumn).descending());

          Page<OfficialReceiptHeaderEntity> orHeaders = orHeaderRepo.findByIsPartSale(false, pageable);

          return new PaginationResponse<>(
               orHeaderMapper.toDtoList(orHeaders.getContent()),
               orHeaders.getNumber(),
               orHeaders.getSize(),
               orHeaders.getTotalPages(),
               orHeaders.getTotalElements()
          );
     }

     @Override
     public PaginationResponse<OfficialReceiptHeaderDto> getOfficialReceiptByCustomerId(int pageNo, int pageSize, String customerId) throws Exception {
     
          Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("orDate").descending());

          Page<OfficialReceiptHeaderEntity> orHeaders = orHeaderRepo.findByCustomerIdAndIsPartSale(customerId,false, pageable);

          return new PaginationResponse<>(
               orHeaderMapper.toDtoList(orHeaders.getContent()),
               orHeaders.getNumber(),
               orHeaders.getSize(),
               orHeaders.getTotalPages(),
               orHeaders.getTotalElements()
          );
     }

     @Override
     public OfficialReceiptDto createOfficialReceipt(OfficiaReceiptPayload payload) throws Exception {
          
          OfficialReceiptHeaderEntity orHeaderEntity = orHeaderMapper.toEntity(payload.getHeader());
          
          String newOrNo = this.generateControlNumber();

          orHeaderEntity.setOrNo(newOrNo);
          List<OfficialReceiptDetailsEntity> detailsList = orDetailsMapper.toEntityList(payload.getDetails());
          
          // set the orNo for the OR details
          for (OfficialReceiptDetailsEntity d : detailsList) {
               d.setOrNo(newOrNo);
          } 

          OfficialReceiptDto result = new OfficialReceiptDto();
          OfficialReceiptHeaderEntity resultHeader = orHeaderRepo.save(orHeaderEntity);
          List<OfficialReceiptDetailsEntity> resultDetails = orDetailsRepo.saveAll(detailsList);

          for(OfficialReceiptDetailsDto details: payload.getDetails()) {
               billingService.updateBillingStatus(details.getReadingNo(),details.getBillingStatus());
          }
          
          ShareCapitalEntity shareCapitalEntity = new ShareCapitalEntity();
          if(payload.getShareCapital() != null) {
               shareCapitalEntity = shareCapMapper.toEntity(payload.getShareCapital());
               shareCapitalEntity.setOrNo(newOrNo);
               shareCapitalEntity = shareCapRepo.save(shareCapitalEntity); 
               result.setShareCapital(shareCapMapper.toDto(shareCapitalEntity));
          }

          ShareCbuEntity cbuEntity = new ShareCbuEntity();
          if(payload.getCbu() != null) {
               cbuEntity = cbuMapper.toEntity(payload.getCbu());
               cbuEntity.setOrNo(newOrNo);
               cbuEntity = cbuRepo.save(cbuEntity);
               result.setCbu(cbuMapper.toDto(cbuEntity));
          }

          ShareSavingsEntity savingsEntity = new ShareSavingsEntity();
          if(payload.getSavings() != null) {
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
     public PaginationResponse<OfficialReceiptHeaderWithCustomerDto> getOfficialReceiptWithCustomer(int pageNo, int pageSize, String sortColumn) throws Exception {
          Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortColumn).descending());

          Page<OfficialReceiptHeaderEntity> orHeaders = orHeaderRepo.findByIsPartSale(false, pageable);

          List<String> ids = new ArrayList<String>();
          for(OfficialReceiptHeaderEntity orh : orHeaders) {
               ids.add(orh.getCustomerId());
          }

          List<CustomerDto> customers = customerService.getCustomerByIds(ids);

          List<OfficialReceiptHeaderWithCustomerDto> orWithCust = new ArrayList<OfficialReceiptHeaderWithCustomerDto>();

          for(OfficialReceiptHeaderEntity orh : orHeaders) {
               OfficialReceiptHeaderWithCustomerDto orc = new OfficialReceiptHeaderWithCustomerDto();
               CustomerDto c = customers.stream().filter(cc -> cc.getId().equals(orh.getCustomerId())).findFirst().orElse(null);

               orc.setId(orh.getId());
               orc.setOrNo(orh.getOrNo());
               orc.setCustomerId(orh.getCustomerId());
               orc.setCustomer(c);
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
               orHeaders.getTotalElements()
          );
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

          OfficialReceiptResponse or = new OfficialReceiptResponse();
          or.setHeader(orc);
          or.setDetails(orDetails);
          or.setShareCapital(shareCap);
          or.setCbu(cbu);
          or.setSavings(savings);
          return or;
     }

     @Override
      public PaginationResponse<OtherPaymentHeaderDto> getOtherCollectionAll(int pageNo, int pageSize, String sortColumn) throws Exception {
     
          Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortColumn).descending());

          Page<OfficialReceiptHeaderEntity> orHeaders = orHeaderRepo.findByIsPartSale(true, pageable);

          return new PaginationResponse<>(
               orHeaderMapper.toDtoList(orHeaders.getContent()),
               orHeaders.getNumber(),
               orHeaders.getSize(),
               orHeaders.getTotalPages(),
               orHeaders.getTotalElements()
          );

      }

      @Override
      public PaginationResponse<OtherPaymentHeaderDto> getOtherCollectionByCustomerId(int pageNo, int pageSize, String customerId) throws Exception {
          
          Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("orDate").descending());

          Page<OfficialReceiptHeaderEntity> orHeaders = orHeaderRepo.findByCustomerIdAndIsPartSale(customerId,true, pageable);

          return new PaginationResponse<>(
               orHeaderMapper.toDtoList(orHeaders.getContent()),
               orHeaders.getNumber(),
               orHeaders.getSize(),
               orHeaders.getTotalPages(),
               orHeaders.getTotalElements()
          );

      }

      @Override
      public PaginationResponse<OtherPaymentHeaderWithCustomerDto> getOtherCollectionWithCustomer(int pageNo, int pageSize, String sortColumn) throws Exception {
          
          Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortColumn).descending());

          Page<OfficialReceiptHeaderEntity> orHeaders = orHeaderRepo.findByIsPartSale(true, pageable);

          List<String> ids = new ArrayList<String>();
          for(OfficialReceiptHeaderEntity orh : orHeaders) {
               ids.add(orh.getCustomerId());
          }

          List<CustomerDto> customers = customerService.getCustomerByIds(ids);

          List<OtherPaymentHeaderWithCustomerDto> orWithCust = new ArrayList<OtherPaymentHeaderWithCustomerDto>();

          for(OfficialReceiptHeaderEntity orh : orHeaders) {
               OtherPaymentHeaderWithCustomerDto orc = new OtherPaymentHeaderWithCustomerDto();
               CustomerDto c = customers.stream().filter(cc -> cc.getId().equals(orh.getCustomerId())).findFirst().orElse(null);

               orc.setId(orh.getId());
               orc.setOrNo(orh.getOrNo());
               orc.setCustomerId(orh.getCustomerId());
               orc.setCustomer(c);
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
               orHeaders.getTotalElements()
          );
      }

      @Override
      public OtherPaymentResponse getOtherCollectionWithDetailsById(String orhId) throws Exception {
          OfficialReceiptHeaderEntity orHeader = orHeaderRepo.findById(orhId).orElseThrow();
          
          String orNo = orHeader.getOrNo();

          CustomerDto customer = new CustomerDto();

          if(orHeader.getCustomerId() != null && orHeader.getCustomerId().equals("")){
               customer = customerService.getCustomer(orHeader.getCustomerId());
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

          OtherPaymentResponse or = new OtherPaymentResponse();
          or.setHeader(orc);
          or.setDetails(orDetails);
          return or;
      } 

      private String generateControlNumber() {
          String datePrefix  = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
          String lastReadingNo = orHeaderRepo.findLastOrNo("OR"+datePrefix);

          int sequence = 1;
          if(lastReadingNo != null) {
               int lastSequence = Integer.parseInt(lastReadingNo.substring(10));
               sequence = lastSequence + 1;
          }

          return "OR" + datePrefix + String.format("%05d", sequence);
     }

}
