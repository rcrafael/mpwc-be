package com.mahabang_parang.api_payment.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mahabang_parang.api_payment.model.entity.OfficialReceiptHeaderEntity;
import com.mahabang_parang.api_payment.model.interfaces.OfficialReceiptTableListInterfaceProjection;

public interface OfficialReceiptHeaderRepository extends JpaRepository<OfficialReceiptHeaderEntity, String> {

        @Query("SELECT o.orNo FROM OfficialReceiptHeaderEntity o WHERE o.orNo LIKE :datePrefix% ORDER BY o.orNo DESC LIMIT 1")
        String findLastOrNo(@Param("datePrefix") String datePrefix);

        Page<OfficialReceiptHeaderEntity> findByCustomerIdAndIsPartSaleAndIsDeleted(String customerId,
                        Boolean isPartSale, Boolean isDeleted, Pageable pageable);

        Page<OfficialReceiptHeaderEntity> findByIsPartSaleAndIsDeleted(Boolean isPartSale, Boolean isDeleted,
                        Pageable pageable);

        // Page<OfficialReceiptHeaderEntity>
        // findByCustomerIdContainingIgnoreCaseOrOrNoContainingIgnoreCase(String
        // customerId, String orNo, Pageable pageable);

        @Query("SELECT o FROM OfficialReceiptHeaderEntity o WHERE LOWER(o.customerId) LIKE LOWER(CONCAT('%', :customerId, '%')) OR LOWER(o.orNo) LIKE LOWER(CONCAT('%', :orNo, '%')) AND o.isPartSale = :isPartSale AND o.isDeleted = false")
        Page<OfficialReceiptHeaderEntity> searchByCustomerIdOrOrNo(
                        @Param("customerId") String customerId,
                        @Param("orNo") String orNo,
                        @Param("isPartSale") Boolean isPartSale,
                        Pageable pageable);

        @Query(value = "SELECT DISTINCT orh.id, " + 
                                "orh.or_no AS orNo, " +
                                "orh.customer_id AS customerId, " +
                                "cust.account_no AS accountNo, " +
                                "cust.first_name AS firstName, " +
                                "cust.middle_name AS middleName, " +
                                "cust.last_name AS lastName, " +
                                "cust.address AS address, " +
                                "orh.or_date AS orDate, " +
                                "orh.total_billing_amount AS totalBillingAmount, " +
                                "orh.total_share_amount AS totalShareAmount, " +
                                "orh.total_amount_pay AS totalAmountPay, " +
                                "orh.senior_discount AS seniorDiscount, " +
                                "orh.entry_date AS entryDate, " +
                                "orh.created_by AS createdBy, " +
                                "orh.check_no AS checkNo, " +
                                "orh.check_bank AS checkBank, " +
                                "orh.check_date AS checkDate, " +
                                "orh.status AS `status`, " +
                                "orh.is_part_sale AS isPartSale, " +
                                "orh.is_outside_collection AS isOutsideCollection, " +
                                "orh.outside_customer_name As outsideCustomerName, " +
                                "orh.outside_customer_address As outsideCustomerAddress, " +
                                "orh.is_deleted AS isDeleted " +
                        "FROM official_receipt_header orh " +
                        "INNER JOIN customer cust " +
                                " ON orh.customer_id = cust.id " +
                        "WHERE (LOWER(orh.customer_id) LIKE LOWER(CONCAT('%', :customerId, '%')) " +
                                "OR LOWER(cust.first_name) LIKE LOWER(CONCAT('%', :customerName, '%')) " +
                                "OR LOWER(cust.middle_name) LIKE LOWER(CONCAT('%', :customerName, '%')) " +
                                "OR LOWER(cust.last_name) LIKE LOWER(CONCAT('%', :customerName, '%')) " +
                                "OR LOWER(orh.or_no) LIKE LOWER(CONCAT('%', :orNo, '%')) )" + 
                                "AND orh.is_part_sale = :isPartSale " + 
                                "AND orh.is_deleted = false",
                countQuery = "SELECT COUNT(DISTINCT orh.id) " + 
                        "FROM official_receipt_header orh " +
                        "INNER JOIN customer cust " +
                                " ON orh.customer_id = cust.id " +
                        "WHERE (LOWER(orh.customer_id) LIKE LOWER(CONCAT('%', :customerId, '%')) " + 
                                "OR LOWER(cust.first_name) LIKE LOWER(CONCAT('%', :customerName, '%')) " +
                                "OR LOWER(cust.middle_name) LIKE LOWER(CONCAT('%', :customerName, '%')) " +
                                "OR LOWER(cust.last_name) LIKE LOWER(CONCAT('%', :customerName, '%')) " +
                                "OR LOWER(orh.or_no) LIKE LOWER(CONCAT('%', :orNo, '%')) )" + 
                                "AND orh.is_part_sale = :isPartSale " + 
                                "AND orh.is_deleted = false",
                nativeQuery = true)
        Page<OfficialReceiptTableListInterfaceProjection> searchByCustomerNameOrAccountNoOrOrNo(
                        @Param("customerName") String customerName,
                        @Param("customerId") String customerId,
                        @Param("orNo") String orNo,
                        @Param("isPartSale") Boolean isPartSale,
                        Pageable pageable);

        @Query("SELECT IFNULL(SUM(o.totalAmountPay),0.00) FROM OfficialReceiptHeaderEntity o WHERE o.orDate BETWEEN :startDate AND :endDate AND o.status = 1 AND o.isPartSale = :isPartSale")
        Double sumOfOfficialReceiptByDate(Date startDate, Date endDate, Boolean isPartSale);

}
