package com.mahabang_parang.api_user.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mahabang_parang.api_user.model.constant.PageConstants;
import com.mahabang_parang.api_user.model.entity.RoleEntity;
import com.mahabang_parang.api_user.model.entity.UserEntity;
import com.mahabang_parang.api_user.model.entity.UserPageAccessEntity;

import io.jsonwebtoken.Claims;

public class JwtUtilPageAccessTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
    }

    @Test
    void testGenerateToken_superadminHasAllPages() {
        RoleEntity role = new RoleEntity();
        role.setRole("Superadmin");

        UserEntity user = new UserEntity();
        user.setId("u-1");
        user.setEmail("tech@example.com");
        user.setUsername("techadmin");
        user.setRole(role);

        String token = jwtUtil.generateToken(user);
        assertNotNull(token);

        List<?> pages = jwtUtil.extractClaim(token, claims -> claims.get("pageAccess", List.class));
        assertNotNull(pages);
        assertEquals(PageConstants.ALL_PAGES.size(), pages.size());
        assertTrue(pages.contains(PageConstants.DASHBOARD));
        assertTrue(pages.contains(PageConstants.BILLING));
    }

    @Test
    void testGenerateToken_customUserPageAccess() {
        RoleEntity role = new RoleEntity();
        role.setRole("User");

        UserEntity user = new UserEntity();
        user.setId("u-2");
        user.setEmail("staff@example.com");
        user.setUsername("staff1");
        user.setRole(role);

        user.getPageAccess().add(UserPageAccessEntity.builder()
                .user(user)
                .pageCode(PageConstants.CUSTOMER)
                .build());
        user.getPageAccess().add(UserPageAccessEntity.builder()
                .user(user)
                .pageCode(PageConstants.BILLING)
                .build());

        String token = jwtUtil.generateToken(user);
        assertNotNull(token);

        List<?> pages = jwtUtil.extractClaim(token, claims -> claims.get("pageAccess", List.class));
        assertNotNull(pages);
        assertEquals(2, pages.size());
        assertTrue(pages.contains(PageConstants.CUSTOMER));
        assertTrue(pages.contains(PageConstants.BILLING));
    }
}
