package com.mahabang_parang.api_reports.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mahabang_parang.api_payment.model.entity.OfficialReceiptHeaderEntity;
import com.mahabang_parang.api_reports.model.interfaces.CollectionReportDetailInterfaceProjection;
import com.mahabang_parang.api_reports.model.interfaces.ShareCapCbuSavingsCollectionInterfaceProjection;
import com.mahabang_parang.api_reports.model.interfaces.ShareCapCbuSavingsCollectionListInterfaceProjection;

public interface ORCollectionRepository extends JpaRepository<OfficialReceiptHeaderEntity, String> {

     @Query(value = "SELECT " + 
          "cust.account_no AS accountNo, " +
          "IFNULL(SUM(shareCap.amount),0) As totalShareCap, " +
          "IFNULL(SUM(cbu.amount),0) As totalCbu, " +
          "IFNULL(SUM(savings.amount),0) As totalSavings " +
          "FROM official_receipt_header orh " +
               "LEFT JOIN customer cust " +
                    "ON orh.customer_id = cust.id " +
               "LEFT JOIN share_capital shareCap " +
                    "ON orh.or_no = shareCap.or_no AND shareCap.transaction_type = 1 " +
               "LEFT JOIN share_cbu cbu " +
                    "ON orh.or_no = cbu.or_no AND cbu.transaction_type = 1 " +
               "LEFT JOIN share_savings savings " +
                    "ON orh.or_no = savings.or_no AND savings.transaction_type = 1 " +
          "WHERE orh.customer_id = :customerId " +
               " AND orh.status = 1 AND orh.is_part_sale = FALSE " +
          "GROUP BY cust.account_no",
          nativeQuery = true)
     ShareCapCbuSavingsCollectionInterfaceProjection getShareCapCbuSavingsCollectionByCustomerId(String customerId);

     @Query(value = "SELECT " + 
          "cust.account_no AS accountNo, " +
          "IFNULL(SUM(shareCap.amount),0) As totalShareCap, " +
          "IFNULL(SUM(cbu.amount),0) As totalCbu, " +
          "IFNULL(SUM(savings.amount),0) As totalSavings " +
          "FROM official_receipt_header orh " +
               "LEFT JOIN customer cust " +
                    "ON orh.customer_id = cust.id " +
               "LEFT JOIN share_capital shareCap " +
                    "ON orh.or_no = shareCap.or_no AND shareCap.transaction_type = 1 " +
               "LEFT JOIN share_cbu cbu " +
                    "ON orh.or_no = cbu.or_no AND cbu.transaction_type = 1 " +
               "LEFT JOIN share_savings savings " +
                    "ON orh.or_no = savings.or_no AND savings.transaction_type = 1 " +
          "WHERE orh.customer_id = :customerId " +
               " AND orh.status = 1 AND orh.is_part_sale = FALSE " +
               " AND orh.or_date BETWEEN :startDate AND :endDate " +
          "GROUP BY cust.account_no",
          nativeQuery = true)
     ShareCapCbuSavingsCollectionInterfaceProjection getShareCapCbuSavingsCollectionByCustomerIdAndDateRange(String customerId, Date startDate, Date endDate);


     @Query(value = "SELECT " + 
          "orh.id, " +
          "cust.account_no AS accountNo, " +
          "cust.first_name AS firstName, " +
          "cust.middle_name AS middleName, " +
          "cust.last_name AS lastName, " +
          "cust.address AS address, " +
          "orh.or_no AS orNo, " +
          "shareCap.amount AS shareCap, " +
          "cbu.amount AS cbu, " +
          "savings.amount AS savings, " +
          "orh.or_date AS orDate " +
          "FROM official_receipt_header orh " +
               "LEFT JOIN customer cust " +
                    "ON orh.customer_id = cust.id " +
               "LEFT JOIN share_capital shareCap " +
                    "ON orh.or_no = shareCap.or_no AND shareCap.transaction_type = 1 " +
               "LEFT JOIN share_cbu cbu " +
                    "ON orh.or_no = cbu.or_no AND cbu.transaction_type = 1 " +
               "LEFT JOIN share_savings savings " +
                    "ON orh.or_no = savings.or_no AND savings.transaction_type = 1 " +
          "WHERE orh.status = 1 AND orh.is_part_sale = FALSE " +
               " AND shareCap.amount > 0 AND cbu.amount > 0 AND savings.amount > 0 " +
               " AND orh.or_date BETWEEN :startDate AND :endDate ",
          nativeQuery = true)
     Page<ShareCapCbuSavingsCollectionListInterfaceProjection> getShareCapCbuSavingsCollectionListByDateRange(Date startDate, Date endDate, Pageable pageable);


     @Query(value = "SELECT " + 
          "orh.id, " +
          "cust.account_no AS accountNo, " +
          "cust.first_name AS firstName, " +
          "cust.middle_name AS middleName, " +
          "cust.last_name AS lastName, " +
          "cust.address AS address, " +
          "orh.or_no AS orNo, " +
          "shareCap.amount AS shareCap, " +
          "cbu.amount AS cbu, " +
          "savings.amount AS savings, " +
          "orh.or_date AS orDate " +
          "FROM official_receipt_header orh " +
               "LEFT JOIN customer cust " +
                    "ON orh.customer_id = cust.id " +
               "LEFT JOIN share_capital shareCap " +
                    "ON orh.or_no = shareCap.or_no AND shareCap.transaction_type = 1 " +
               "LEFT JOIN share_cbu cbu " +
                    "ON orh.or_no = cbu.or_no AND cbu.transaction_type = 1 " +
               "LEFT JOIN share_savings savings " +
                    "ON orh.or_no = savings.or_no AND savings.transaction_type = 1 " +
          "WHERE orh.customer_id = :customerId " +
               " AND orh.status = 1 AND orh.is_part_sale = FALSE " +
               " AND shareCap.amount > 0 AND cbu.amount > 0 AND savings.amount > 0 " +
               " AND orh.or_date BETWEEN :startDate AND :endDate ",
          nativeQuery = true)
     Page<ShareCapCbuSavingsCollectionListInterfaceProjection> getShareCapCbuSavingsCollectionListByCustomerIdAndDateRange(String customerId, Date startDate, Date endDate, Pageable pageable);


     @Query(value = "SELECT " + 
          "IFNULL(SUM(shareCap.amount),0) As totalShareCap " +
          "FROM share_capital shareCap " +
               "LEFT JOIN official_receipt_header orh " +
                    "ON orh.or_no = shareCap.or_no AND shareCap.transaction_type = 1 " +
          "WHERE orh.or_date BETWEEN :startDate AND :endDate AND orh.status = 1",
          nativeQuery = true)
     Double sumOfShareCapitalByDateRange(Date startDate, Date endDate);
     
     @Query(value = "SELECT " + 
          "IFNULL(SUM(cbu.amount),0) As totalShareCap " +
          "FROM share_cbu cbu " +
               "LEFT JOIN official_receipt_header orh " +
                    "ON orh.or_no = cbu.or_no AND cbu.transaction_type = 1 " +
          "WHERE orh.or_date BETWEEN :startDate AND :endDate AND orh.status = 1",
          nativeQuery = true)
     Double sumOfCbuByDateRange(Date startDate, Date endDate);

     @Query(value = "SELECT DISTINCT " + 
          "orh.or_no AS orNo, " +
          "orh.or_date AS orDate, " +
          "orh.total_billing_amount AS totalBillingAmount, " +
          "orh.total_share_amount AS totalShareAmount, " +
          "orh.senior_discount AS seniorDiscount, " +
          "orh.total_amount_pay AS totalAmountPay, " +
          "cust.account_no AS accountNo, " +
          "cust.alias AS customerAlias, " +
          "cust.first_name AS firstName, " +
          "cust.middle_name AS middleName, " +
          "cust.last_name AS lastName " +
          "FROM official_receipt_header orh " +
               "LEFT JOIN customer cust " +
                    "ON orh.customer_id = cust.id " +
               "LEFT JOIN official_receipt_detail ord " +
                    "ON ord.or_no = orh.or_no " +
               "LEFT JOIN customer_meter cm " +
                    "ON cm.meter_no = ord.meter_no " +
          "WHERE orh.or_date BETWEEN :startDate AND :endDate " +
          "  AND orh.status = 1 " +
          "  AND (:branchCode IS NULL OR :branchCode = -1 OR cm.branch_code = :branchCode) " +
          "  AND (:phase      IS NULL OR :phase = -1      OR cm.phase       = :phase) " +
          "ORDER BY orh.or_date ASC, orh.or_no ASC",
          nativeQuery = true)
     List<com.mahabang_parang.api_reports.model.interfaces.CollectionReportInterfaceProjection> getCollectionReportByDateRange(
             @Param("startDate")  Date startDate,
             @Param("endDate")    Date endDate,
             @Param("branchCode") Integer branchCode,
             @Param("phase")      Integer phase);


     @Query(value =
             "SELECT " +
             "  orh.or_no               AS orNo, " +
             "  orh.or_date             AS orDate, " +
             "  orh.total_billing_amount AS totalBillingAmount, " +
             "  orh.total_share_amount  AS totalShareAmount, " +
             "  orh.senior_discount     AS seniorDiscount, " +
             "  orh.total_amount_pay    AS totalAmountPay, " +
             "  cust.account_no         AS accountNo, " +
             "  cust.alias              AS customerAlias, " +
             "  cust.first_name         AS firstName, " +
             "  cust.middle_name        AS middleName, " +
             "  cust.last_name          AS lastName, " +
             "  ord.meter_no            AS meterNo, " +
             "  cm.branch_code          AS branchCode, " +
             "  cm.phase                AS phase, " +
             "  ord.reading_amount_paid AS readingAmountPaid, " +
             "  ord.parts               AS parts, " +
             "  ord.membership          AS membership, " +
             "  ord.tapping             AS tapping, " +
             "  ord.calibration         AS calibration, " +
             "  ord.reconnection        AS reconnection, " +
             "  ord.sub_receivable      AS subReceivable, " +
             "  ord.penalty             AS penalty, " +
             "  ord.other_collection    AS otherCollection, " +
             "  ord.other_description   AS otherDescription, " +
             "  ord.maintenance_fee     AS maintenanceFee, " +
             "  ord.senior_discount     AS detailSeniorDiscount, " +
             "  ord.septage_fee         AS septageFee, " +
             "  ord.total_amount        AS detailTotalAmount, " +
             "  ord.remarks             AS remarks " +
             "FROM official_receipt_header orh " +
             "  INNER JOIN official_receipt_detail ord ON ord.or_no = orh.or_no " +
             "  LEFT  JOIN customer cust               ON cust.id   = orh.customer_id " +
             "  LEFT  JOIN customer_meter cm           ON cm.meter_no = ord.meter_no " +
             "WHERE orh.or_date BETWEEN :startDate AND :endDate " +
             "  AND orh.status = 1 " +
             "  AND (:branchCode IS NULL OR :branchCode = -1 OR cm.branch_code = :branchCode) " +
             "  AND (:phase      IS NULL OR :phase = -1      OR cm.phase       = :phase) " +
             "ORDER BY orh.or_date ASC, orh.or_no ASC",
             nativeQuery = true)
      List<CollectionReportDetailInterfaceProjection> getCollectionReportDetailsByDateRange(
              @Param("startDate")  Date startDate,
              @Param("endDate")    Date endDate,
              @Param("branchCode") Integer branchCode,
              @Param("phase")      Integer phase);

}
