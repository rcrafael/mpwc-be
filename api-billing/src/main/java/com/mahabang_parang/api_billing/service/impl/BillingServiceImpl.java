package com.mahabang_parang.api_billing.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mahabang_parang.api_billing.model.PaginationRequest;
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


          System.out.println("-------------------------------------------------------------------------------");
          System.out.println("BILLING CONTENT: " + billing.getContent());
          System.out.println("-------------------------------------------------------------------------------");

          return new PaginationResponse<>(
               billing.getContent(),
               billing.getNumber(),
               billing.getSize(),
               billing.getTotalPages(),
               billing.getTotalElements()
          );
          

          
     }


     public PaginationResponse<BillingDto> getBillingByCustId(String customerId, int pageNumber, int pageSize) throws Exception {
          PageRequest pageable = PageRequest.of(pageNumber, pageSize);
          
          Page<BillingEntity> billing = billingRepo.findByCustId(customerId, pageable);


          System.out.println("-------------------------------------------------------------------------------");
          System.out.println("BILLING CONTENT: " + billing.getContent());
          System.out.println("-------------------------------------------------------------------------------");

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

     public BillingDto getBillingById(String billingId) throws Exception { 

          BillingEntity bill = billingRepo.findById(billingId).orElseThrow();

          return mapper.toDto(bill);
     }


     public BillingDto create(BillingDto billing) throws Exception {
          BillingEntity billingEntity = mapper.toEntity(billing);
          billingEntity = billingRepo.save(billingEntity);
          System.out.println("-------------------------------------------------------------------------------");
          System.out.println("BILLING CONTENT CREATED: " + billing);
          System.out.println("-------------------------------------------------------------------------------");

          return mapper.toDto(billingEntity);
     }
}
