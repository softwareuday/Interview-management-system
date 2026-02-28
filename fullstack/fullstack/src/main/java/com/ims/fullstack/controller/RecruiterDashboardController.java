//package com.ims.fullstack.controller;
//
//import com.ims.fullstack.dto.dashboard.DashboardActivityResponse;
//import com.ims.fullstack.dto.dashboard.DashboardStatsResponse;
//import com.ims.fullstack.service.DashboardStatsService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/recruiter/dashboard")
//@RequiredArgsConstructor
//@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
//public class RecruiterDashboardController {
//
//    private final DashboardStatsService dashboardStatsService;
//
//    // =============================
//    // STATS
//    // =============================
//    @PreAuthorize("hasRole('RECRUITER')")
//    @GetMapping("/stats")
//    public ResponseEntity<DashboardStatsResponse> getStats() {
//        return ResponseEntity.ok(dashboardStatsService.getStats());
//    }
//
//    // =============================
//    // RECENT ACTIVITIES
//    // =============================
//    @PreAuthorize("hasRole('RECRUITER')")
//    @GetMapping("/activities")
//    public ResponseEntity<List<DashboardActivityResponse>> getActivities() {
//        return ResponseEntity.ok(dashboardStatsService.getRecentActivities());
//    }
//}
package com.ims.fullstack.controller;

import com.ims.fullstack.dto.dashboard.DashboardActivityResponse;
import com.ims.fullstack.dto.dashboard.DashboardStatsResponse;
import com.ims.fullstack.service.DashboardStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/recruiter/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class RecruiterDashboardController {
    private final DashboardStatsService dashboardStatsService;

    @PreAuthorize("hasRole('RECRUITER')")
    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsResponse> getStats() {
        return ResponseEntity.ok(dashboardStatsService.getStats());
    }

    @PreAuthorize("hasRole('RECRUITER')")
    @GetMapping("/activities")
    public ResponseEntity<List<DashboardActivityResponse>> getActivities() {
        return ResponseEntity.ok(dashboardStatsService.getRecentActivities());
    }
}