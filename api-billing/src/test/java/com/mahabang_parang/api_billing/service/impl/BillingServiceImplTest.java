package com.mahabang_parang.api_billing.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mahabang_parang.api_billing.model.dto.BillingDto;
import com.mahabang_parang.api_billing.model.entity.BillingEntity;
import com.mahabang_parang.api_billing.model.mapper.BillingMapper;
import com.mahabang_parang.api_billing.repository.BillingRepository;
import com.mahabang_parang.api_user.model.dto.CustomerMeterDto;
import com.mahabang_parang.api_user.service.CustomerMeterService;
import com.mahabang_parang.api_user.service.CustomerService;

@ExtendWith(MockitoExtension.class)
class BillingServiceImplTest {

    @Mock
    private BillingRepository billingRepo;

    @Mock
    private CustomerService custService;

    @Mock
    private CustomerMeterService meterService;

    @Mock
    private BillingMapper mapper;

    @InjectMocks
    private BillingServiceImpl billingService;

    private BillingEntity sampleBilling;
    private CustomerMeterDto sampleMeter;

    @BeforeEach
    void setUp() {
        sampleBilling = new BillingEntity();
        sampleBilling.setId("bill-123");
        sampleBilling.setMeterNo("MTR-001");
        sampleBilling.setPresent(120);
        sampleBilling.setPrevious(100);
        sampleBilling.setM3Used(20);
        sampleBilling.setStatus(1);

        sampleMeter = new CustomerMeterDto();
        sampleMeter.setMeterNo("MTR-001");
        sampleMeter.setPresent(120);
        sampleMeter.setPrevious(100);
    }

    @Test
    void testVoidBilling_normalValues() throws Exception {
        when(billingRepo.findById("bill-123")).thenReturn(Optional.of(sampleBilling));
        when(meterService.getCustomerMeterByMeterNo("MTR-001")).thenReturn(sampleMeter);
        when(billingRepo.save(any(BillingEntity.class))).thenAnswer(i -> i.getArgument(0));
        when(mapper.toDto(any(BillingEntity.class))).thenReturn(new BillingDto());

        billingService.voidBilling("bill-123", "user-admin");

        assertEquals(4, sampleBilling.getStatus());
        assertEquals("user-admin", sampleBilling.getUpdatedBy());

        ArgumentCaptor<CustomerMeterDto> meterCaptor = ArgumentCaptor.forClass(CustomerMeterDto.class);
        verify(meterService).updateCustomerMeter(meterCaptor.capture());
        CustomerMeterDto updatedMeter = meterCaptor.getValue();
        assertEquals(100, updatedMeter.getPresent());
        assertEquals(100, updatedMeter.getPrevious());
        assertEquals("user-admin", updatedMeter.getUpdatedBy());
    }

    @Test
    void testVoidBilling_nullMeterPresent_usesPrevious() throws Exception {
        sampleMeter.setPresent(null);
        sampleMeter.setPrevious(100);
        sampleBilling.setM3Used(20);

        when(billingRepo.findById("bill-123")).thenReturn(Optional.of(sampleBilling));
        when(meterService.getCustomerMeterByMeterNo("MTR-001")).thenReturn(sampleMeter);
        when(billingRepo.save(any(BillingEntity.class))).thenAnswer(i -> i.getArgument(0));
        when(mapper.toDto(any(BillingEntity.class))).thenReturn(new BillingDto());

        billingService.voidBilling("bill-123", "user-admin");

        ArgumentCaptor<CustomerMeterDto> meterCaptor = ArgumentCaptor.forClass(CustomerMeterDto.class);
        verify(meterService).updateCustomerMeter(meterCaptor.capture());
        CustomerMeterDto updatedMeter = meterCaptor.getValue();
        // Since sampleBilling has previous=100 and meter present was null, it aligns with previous=100
        assertEquals(100, updatedMeter.getPresent());
        assertEquals(100, updatedMeter.getPrevious());
    }

    @Test
    void testVoidBilling_nullMeterPrevious_initializesPrevious() throws Exception {
        sampleMeter.setPresent(120);
        sampleMeter.setPrevious(null);
        sampleBilling.setPrevious(100);
        sampleBilling.setM3Used(20);

        when(billingRepo.findById("bill-123")).thenReturn(Optional.of(sampleBilling));
        when(meterService.getCustomerMeterByMeterNo("MTR-001")).thenReturn(sampleMeter);
        when(billingRepo.save(any(BillingEntity.class))).thenAnswer(i -> i.getArgument(0));
        when(mapper.toDto(any(BillingEntity.class))).thenReturn(new BillingDto());

        billingService.voidBilling("bill-123", "user-admin");

        ArgumentCaptor<CustomerMeterDto> meterCaptor = ArgumentCaptor.forClass(CustomerMeterDto.class);
        verify(meterService).updateCustomerMeter(meterCaptor.capture());
        CustomerMeterDto updatedMeter = meterCaptor.getValue();
        assertEquals(100, updatedMeter.getPresent());
        assertEquals(100, updatedMeter.getPrevious());
    }

    @Test
    void testVoidBilling_nullM3Used_calculatesFromBillingPresentAndPrevious() throws Exception {
        sampleBilling.setM3Used(null);
        sampleBilling.setPresent(150);
        sampleBilling.setPrevious(120);
        sampleMeter.setPresent(150);
        sampleMeter.setPrevious(120);

        when(billingRepo.findById("bill-123")).thenReturn(Optional.of(sampleBilling));
        when(meterService.getCustomerMeterByMeterNo("MTR-001")).thenReturn(sampleMeter);
        when(billingRepo.save(any(BillingEntity.class))).thenAnswer(i -> i.getArgument(0));
        when(mapper.toDto(any(BillingEntity.class))).thenReturn(new BillingDto());

        billingService.voidBilling("bill-123", "user-admin");

        ArgumentCaptor<CustomerMeterDto> meterCaptor = ArgumentCaptor.forClass(CustomerMeterDto.class);
        verify(meterService).updateCustomerMeter(meterCaptor.capture());
        CustomerMeterDto updatedMeter = meterCaptor.getValue();
        // 150 - 30 = 120
        assertEquals(120, updatedMeter.getPresent());
    }

    @Test
    void testVoidBilling_allNulls_doesNotThrowNpe() throws Exception {
        sampleMeter.setPresent(null);
        sampleMeter.setPrevious(null);
        sampleBilling.setM3Used(null);
        sampleBilling.setPresent(null);
        sampleBilling.setPrevious(null);

        when(billingRepo.findById("bill-123")).thenReturn(Optional.of(sampleBilling));
        when(meterService.getCustomerMeterByMeterNo("MTR-001")).thenReturn(sampleMeter);
        when(billingRepo.save(any(BillingEntity.class))).thenAnswer(i -> i.getArgument(0));
        when(mapper.toDto(any(BillingEntity.class))).thenReturn(new BillingDto());

        billingService.voidBilling("bill-123", "user-admin");

        assertEquals(4, sampleBilling.getStatus());
        ArgumentCaptor<CustomerMeterDto> meterCaptor = ArgumentCaptor.forClass(CustomerMeterDto.class);
        verify(meterService).updateCustomerMeter(meterCaptor.capture());
        CustomerMeterDto updatedMeter = meterCaptor.getValue();
        assertEquals(0, updatedMeter.getPresent());
        assertEquals(0, updatedMeter.getPrevious());
    }

    @Test
    void testVoidBilling_meterNotFound_proceedsWithoutError() throws Exception {
        when(billingRepo.findById("bill-123")).thenReturn(Optional.of(sampleBilling));
        when(meterService.getCustomerMeterByMeterNo("MTR-001")).thenReturn(null);
        when(billingRepo.save(any(BillingEntity.class))).thenAnswer(i -> i.getArgument(0));
        when(mapper.toDto(any(BillingEntity.class))).thenReturn(new BillingDto());

        billingService.voidBilling("bill-123", "user-admin");

        assertEquals(4, sampleBilling.getStatus());
        verify(meterService, never()).updateCustomerMeter(any());
    }

    @Test
    void testDeleteBilling_nullMeterPresent_safelyReverts() throws Exception {
        sampleMeter.setPresent(null);
        sampleMeter.setPrevious(100);
        sampleBilling.setM3Used(20);

        when(billingRepo.findById("bill-123")).thenReturn(Optional.of(sampleBilling));
        when(meterService.getCustomerMeterByMeterNo("MTR-001")).thenReturn(sampleMeter);
        when(billingRepo.save(any(BillingEntity.class))).thenAnswer(i -> i.getArgument(0));
        when(mapper.toDto(any(BillingEntity.class))).thenReturn(new BillingDto());

        billingService.deleteBilling("bill-123", "user-admin");

        assertEquals(9, sampleBilling.getStatus());
        assertEquals("user-admin", sampleBilling.getUpdatedBy());

        ArgumentCaptor<CustomerMeterDto> meterCaptor = ArgumentCaptor.forClass(CustomerMeterDto.class);
        verify(meterService).updateCustomerMeter(meterCaptor.capture());
        CustomerMeterDto updatedMeter = meterCaptor.getValue();
        assertEquals(100, updatedMeter.getPresent());
        assertEquals(100, updatedMeter.getPrevious());
    }
}
