package com.mahabang_parang.api_billing.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.mahabang_parang.api_billing.model.entity.BillingEntity;

public interface BillingRepository extends JpaRepository<BillingEntity, String> {

     @Query("SELECT b.readingNo FROM BillingEntity b WHERE b.readingNo LIKE :datePrefix% ORDER BY b.readingNo DESC LIMIT 1")
     String findLastReadingNo(@Param("datePrefix") String datePrefix);

     @Query("SELECT b from BillingEntity b WHERE b.customerId = :customerId AND b.status = :status ORDER BY b.createdDate DESC LIMIT :limit")
     List<BillingEntity> findByCustomerIdAndStatus(@Param("customerId") String customerId, @Param("status") int status, @Param("limit") int limit);

     @Query(value = "SELECT b.id, b.reading_no, b.customer_id, c.account_no, b.meter_no, b.previous, b.present, b.m3_used, b.reading_amount, b.parts, b.total_amount, b.maintenance_fee, b.less_amount, b.septage_fee, b.remarks, b.month_of, b.date_from, b.date_to, b.due_date, b.status, b.created_by, b.created_date, b.updated_by, b.date_updated, c.first_name, c.middle_name, c.last_name, c.alias, c.branch_code, c.address FROM reading b LEFT JOIN customer c ON b.customer_id = c.id ORDER BY b.created_date DESC", nativeQuery = true)
     Page<Object[]> findAllWithCustomer(Pageable pageable);

     @Modifying
     @Transactional
     @Query("UPDATE BillingEntity b SET b.status = :newStatus WHERE b.readingNo = :readingNo")
     Integer updateBillingStatus(@Param("newStatus") Integer newStatus, @Param("readingNo")  String readingNo);

     Page<BillingEntity> findAll(Pageable pageable);
     Page<BillingEntity> findByCustomerId(String custId, Pageable pageable);
     BillingEntity findByReadingNo(String readingNo);
}
