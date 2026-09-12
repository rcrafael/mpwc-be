package com.mahabang_parang.api_reports.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mahabang_parang.api_billing.model.entity.BillingEntity;
import com.mahabang_parang.api_reports.model.interfaces.CustomerMeterUsageInterfaceProjection;

@Repository
public interface CustomerMeterUsageRepository extends JpaRepository<BillingEntity, String> {

     @Query(value = "SELECT " +
          "IFNULL(c.account_no, '') AS accountNo, " +
          "IFNULL(NULLIF(TRIM(CONCAT(" +
               "IFNULL(c.last_name, ''), " +
               "IF(c.last_name IS NOT NULL AND c.first_name IS NOT NULL, ', ', ''), " +
               "IFNULL(c.first_name, ''), " +
               "IF(c.middle_name IS NOT NULL AND TRIM(c.middle_name) != '', CONCAT(' ', TRIM(c.middle_name)), '') " +
          ")), ''), IFNULL(c.alias, 'N/A')) AS customerName, " +
          "cm.meter_no AS meterNo, " +
          "IFNULL(SUM(CASE WHEN b.month_of = 1 THEN b.m3_used ELSE 0 END), 0) AS jan, " +
          "IFNULL(SUM(CASE WHEN b.month_of = 2 THEN b.m3_used ELSE 0 END), 0) AS feb, " +
          "IFNULL(SUM(CASE WHEN b.month_of = 3 THEN b.m3_used ELSE 0 END), 0) AS mar, " +
          "IFNULL(SUM(CASE WHEN b.month_of = 4 THEN b.m3_used ELSE 0 END), 0) AS apr, " +
          "IFNULL(SUM(CASE WHEN b.month_of = 5 THEN b.m3_used ELSE 0 END), 0) AS may, " +
          "IFNULL(SUM(CASE WHEN b.month_of = 6 THEN b.m3_used ELSE 0 END), 0) AS jun, " +
          "IFNULL(SUM(CASE WHEN b.month_of = 7 THEN b.m3_used ELSE 0 END), 0) AS jul, " +
          "IFNULL(SUM(CASE WHEN b.month_of = 8 THEN b.m3_used ELSE 0 END), 0) AS aug, " +
          "IFNULL(SUM(CASE WHEN b.month_of = 9 THEN b.m3_used ELSE 0 END), 0) AS sep, " +
          "IFNULL(SUM(CASE WHEN b.month_of = 10 THEN b.m3_used ELSE 0 END), 0) AS oct, " +
          "IFNULL(SUM(CASE WHEN b.month_of = 11 THEN b.m3_used ELSE 0 END), 0) AS nov, " +
          "IFNULL(SUM(CASE WHEN b.month_of = 12 THEN b.m3_used ELSE 0 END), 0) AS `dec` " +
          "FROM customer c " +
          "INNER JOIN customer_meter cm ON c.id = cm.customer_id AND cm.status IN (6, 7) AND cm.meter_no IS NOT NULL AND cm.meter_no != '' " +
          "LEFT JOIN reading b ON b.customer_id = c.id " +
               "AND b.meter_no = cm.meter_no " +
               "AND (YEAR(b.date_from) = :year OR (b.date_from IS NULL AND YEAR(b.created_date) = :year)) " +
               "AND b.status NOT IN (4, 5, 7, 8, 9) " +
          "GROUP BY c.account_no, customerName, cm.meter_no " +
          "ORDER BY c.account_no ASC, cm.meter_no ASC",
          nativeQuery = true)
     List<CustomerMeterUsageInterfaceProjection> getCustomerMeterUsagePerYear(@Param("year") int year);

}
