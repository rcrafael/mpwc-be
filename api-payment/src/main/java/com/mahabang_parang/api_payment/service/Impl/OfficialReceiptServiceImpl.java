package com.mahabang_parang.api_payment.service.Impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
import com.mahabang_parang.api_payment.repository.OfficialReceiptDetailsRepository;
import com.mahabang_parang.api_payment.repository.OfficialReceiptHeaderRepository;
import com.mahabang_parang.api_payment.repository.ShareCapitalRepository;
import com.mahabang_parang.api_payment.repository.ShareCbuRepository;
import com.mahabang_parang.api_payment.repository.ShareSavingsRepository;
import com.mahabang_parang.api_payment.service.OfficialReceiptService;

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

          Page<OfficialReceiptHeaderEntity> orHeaders = orHeaderRepo.findAll(pageable);

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

          Page<OfficialReceiptHeaderEntity> orHeaders = orHeaderRepo.findByCustomerId(customerId, pageable);

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
          
          String newOrNo = this.generateControlNumber();https://app.daily.dev/posts

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
