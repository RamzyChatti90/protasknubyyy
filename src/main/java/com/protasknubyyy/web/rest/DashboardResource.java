package com.protasknubyyy.web.rest;

import com.protasknubyyy.service.DashboardService;
import com.protasknubyyy.service.dto.DashboardDataDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * REST controller for managing dashboard data.
 */
@RestController
@RequestMapping("/api")
public class DashboardResource {

    private final Logger log = LoggerFactory.getLogger(DashboardResource.class);

    private final DashboardService dashboardService;

    public DashboardResource(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    /**
     * {@code GET /dashboard} : get the dashboard data.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dashboardDataDTO.
     */
    @GetMapping("/dashboard")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<DashboardDataDTO> getDashboardData() {
        log.debug("REST request to get DashboardData");
        DashboardDataDTO dashboardData = dashboardService.getDashboardData();
        return ResponseEntity.ok().body(dashboardData);
    }
}