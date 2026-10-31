package com.mahabang_parang.api_user.model.constant;

import java.util.List;
import java.util.Map;

public final class PageConstants {

     private PageConstants() {}

     public static final String DASHBOARD = "dashboard";
     public static final String CUSTOMER = "customer";
     public static final String BILLING = "billing";
     public static final String PAYMENT_OR = "payment_or";
     public static final String PAYMENT_OTHER = "payment_other";
     public static final String REPORTS = "reports";
     public static final String SETTINGS = "settings";
     public static final String ADMIN_USERS = "admin_users";

     public static final List<String> ALL_PAGES = List.of(
          DASHBOARD,
          CUSTOMER,
          BILLING,
          PAYMENT_OR,
          PAYMENT_OTHER,
          REPORTS,
          SETTINGS,
          ADMIN_USERS
     );

     public static final List<Map<String, String>> ALL_PAGE_DEFINITIONS = List.of(
          Map.of("code", DASHBOARD, "label", "Dashboard", "description", "Overview and analytical charts"),
          Map.of("code", CUSTOMER, "label", "Customer", "description", "Customer management and profiles"),
          Map.of("code", BILLING, "label", "Billing", "description", "Billing and meter reading management"),
          Map.of("code", PAYMENT_OR, "label", "OR Payment", "description", "Official receipt payments"),
          Map.of("code", PAYMENT_OTHER, "label", "Other Payment", "description", "Non-OR and other transactions"),
          Map.of("code", REPORTS, "label", "Reports", "description", "Collection and usage reports"),
          Map.of("code", SETTINGS, "label", "Settings", "description", "Water rates, parts, and default values"),
          Map.of("code", ADMIN_USERS, "label", "Administration", "description", "User management and permissions")
     );
}
