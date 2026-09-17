package com.mahabang_parang.api_reports.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mahabang_parang.api_billing.model.entity.BillingEntity;
import com.mahabang_parang.api_reports.model.interfaces.BillingReportDetailedProjection;
import com.mahabang_parang.api_reports.model.interfaces.BillingReportSummaryProjection;

@Repository
public interface BillingReportRepository extends JpaRepository<BillingEntity, String> {

    @Query(value =
            "SELECT " +
            "  b.reading_no AS readingNo, " +
            "  IFNULL(c.account_no, '') AS accountNo, " +
            "  IFNULL(b.meter_no, '') AS meterNo, " +
            "  IFNULL(NULLIF(TRIM(CONCAT(" +
            "    IFNULL(c.last_name, ''), " +
            "    IF(c.last_name IS NOT NULL AND c.first_name IS NOT NULL, ', ', ''), " +
            "    IFNULL(c.first_name, ''), " +
            "    IF(c.middle_name IS NOT NULL AND TRIM(c.middle_name) != '', CONCAT(' ', TRIM(c.middle_name)), '') " +
            "  )), ''), IFNULL(c.alias, '')) AS customerName, " +
            "  IFNULL(c.alias, '') AS alias, " +
            "  cm.branch_code AS branchCode, " +
            "  cm.phase AS phase, " +
            "  IFNULL(c.address, '') AS address, " +
            "  b.month_of AS monthOf, " +
            "  b.date_from AS dateFrom, " +
            "  b.date_to AS dateTo, " +
            "  b.due_date AS dueDate, " +
            "  b.m3_used AS m3Used, " +
            "  b.total_amount AS amount " +
            "FROM reading b " +
            "INNER JOIN customer c ON b.customer_id = c.id " +
            "LEFT JOIN customer_meter cm ON b.customer_id = cm.customer_id AND b.meter_no = cm.meter_no " +
            "WHERE b.status = 1 " +
            "  AND b.date_from >= :startDate " +
            "  AND b.date_to <= :endDate " +
            "  AND (:branchCode = -1 OR cm.branch_code = :branchCode) " +
            "  AND (:phase = -1 OR cm.phase = :phase) " +
            "ORDER BY b.date_from ASC, b.reading_no ASC",
            nativeQuery = true)
    List<BillingReportSummaryProjection> getBillingReportSummary(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("branchCode") int branchCode,
            @Param("phase") int phase);

    @Query(value =
            "SELECT " +
            "  b.reading_no AS readingNo, " +
            "  IFNULL(c.account_no, '') AS accountNo, " +
            "  IFNULL(b.meter_no, '') AS meterNo, " +
            "  IFNULL(NULLIF(TRIM(CONCAT(" +
            "    IFNULL(c.last_name, ''), " +
            "    IF(c.last_name IS NOT NULL AND c.first_name IS NOT NULL, ', ', ''), " +
            "    IFNULL(c.first_name, ''), " +
            "    IF(c.middle_name IS NOT NULL AND TRIM(c.middle_name) != '', CONCAT(' ', TRIM(c.middle_name)), '') " +
            "  )), ''), IFNULL(c.alias, '')) AS customerName, " +
            "  IFNULL(c.alias, '') AS alias, " +
            "  cm.branch_code AS branchCode, " +
            "  cm.phase AS phase, " +
            "  IFNULL(c.address, '') AS address, " +
            "  b.month_of AS monthOf, " +
            "  b.date_from AS dateFrom, " +
            "  b.date_to AS dateTo, " +
            "  b.due_date AS dueDate, " +
            "  b.previous AS previous, " +
            "  b.present AS present, " +
            "  b.m3_used AS m3Used, " +
            "  b.reading_amount AS readingAmount, " +
            "  b.parts AS parts, " +
            "  b.maintenance_fee AS maintenanceFee, " +
            "  b.septage_fee AS septageFee, " +
            "  b.less_amount AS lessAmount, " +
            "  b.total_amount AS totalAmount " +
            "FROM reading b " +
            "INNER JOIN customer c ON b.customer_id = c.id " +
            "LEFT JOIN customer_meter cm ON b.customer_id = cm.customer_id AND b.meter_no = cm.meter_no " +
            "WHERE b.status = 1 " +
            "  AND b.date_from >= :startDate " +
            "  AND b.date_to <= :endDate " +
            "  AND (:branchCode = -1 OR cm.branch_code = :branchCode) " +
            "  AND (:phase = -1 OR cm.phase = :phase) " +
            "ORDER BY b.date_from ASC, b.reading_no ASC",
            nativeQuery = true)
    List<BillingReportDetailedProjection> getBillingReportDetailed(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("branchCode") int branchCode,
            @Param("phase") int phase);

}
