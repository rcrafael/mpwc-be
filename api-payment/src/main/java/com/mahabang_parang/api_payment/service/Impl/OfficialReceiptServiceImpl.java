package com.mahabang_parang.api_payment.service.Impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mahabang_parang.api_payment.model.PaginationResponse;
import com.mahabang_parang.api_payment.model.dto.OfficialReceiptDetailsDto;
import com.mahabang_parang.api_payment.model.dto.OfficialReceiptHeaderDto;
import com.mahabang_parang.api_payment.model.entity.OfficialReceiptDetailsEntity;
import com.mahabang_parang.api_payment.model.entity.OfficialReceiptHeaderEntity;
import com.mahabang_parang.api_payment.model.mapper.OfficialReceiptDetailsMapper;
import com.mahabang_parang.api_payment.model.mapper.OfficialReceiptHeaderMapper;
import com.mahabang_parang.api_payment.repository.OfficialReceiptDetailsRepository;
import com.mahabang_parang.api_payment.repository.OfficialReceiptHeaderRepository;
import com.mahabang_parang.api_payment.service.OfficialReceiptService;

@Transactional
@Service
public class OfficialReceiptServiceImpl implements OfficialReceiptService {

     @Autowired
     private OfficialReceiptHeaderRepository orHeaderRepo;
     @Autowired
     private OfficialReceiptDetailsRepository orDetailsRepo;

     @Autowired 
     OfficialReceiptHeaderMapper orHeaderMapper;
     @Autowired
     OfficialReceiptDetailsMapper orDetailsMapper;
     
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
     public OfficialReceiptHeaderDto createOfficialReceipt(OfficialReceiptHeaderDto header,
               List<OfficialReceiptDetailsDto> details) throws Exception {
          
          OfficialReceiptHeaderEntity orHeaderEntity = orHeaderMapper.toEntity(header);
          orHeaderEntity.setOrNo(generateControlNumber());
          List<OfficialReceiptDetailsEntity> detailsList = orDetailsMapper.toEntityList(details);
          
          OfficialReceiptHeaderEntity resultHeader = orHeaderRepo.save(orHeaderEntity);
          List<OfficialReceiptDetailsEntity> resultDetails = orDetailsRepo.saveAll(detailsList);

          for(OfficialReceiptDetailsEntity r : resultDetails) {
               System.out.println("==================================================================");
               System.out.println("Official Receipt Detals Created: " + r.toString());
               System.out.println("==================================================================");
          }

          return orHeaderMapper.toDto(resultHeader);

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
