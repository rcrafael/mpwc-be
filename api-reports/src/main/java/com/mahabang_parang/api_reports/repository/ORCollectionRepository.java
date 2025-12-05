package com.mahabang_parang.api_reports.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mahabang_parang.api_payment.model.entity.OfficialReceiptHeaderEntity;
import com.mahabang_parang.api_reports.model.interfaces.ShareCapCbuSavingsCollectionInterfaceProjection;
import com.mahabang_parang.api_reports.model.interfaces.ShareCapCbuSavingsCollectionListInterfaceProjection;

public interface ORCollectionRepository extends JpaRepository<OfficialReceiptHeaderEntity, String> {

     @Query(value = "SELECT " + 
          "cust.account_no AS accountNo, " +
          "SUM(shareCap.amount) As totalShareCap, " +
          "SUM(cbu.amount) As totalCbu, " +
          "SUM(savings.amount) As totalSavings " +
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
          "SUM(shareCap.amount) As totalShareCap, " +
          "SUM(cbu.amount) As totalCbu, " +
          "SUM(savings.amount) As totalSavings " +
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
     ShareCapCbuSavingsCollectionInterfaceProjection getShareCapCbuSavingsCollectionByCustomerIdAndDateRange(String customerId, String startDate, String endDate);


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
     Page<ShareCapCbuSavingsCollectionListInterfaceProjection> getShareCapCbuSavingsCollectionListByDateRange(String startDate, String endDate, Pageable pageable);


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
     Page<ShareCapCbuSavingsCollectionListInterfaceProjection> getShareCapCbuSavingsCollectionListByCustomerIdAndDateRange(String customerId, String startDate, String endDate, Pageable pageable);

}
