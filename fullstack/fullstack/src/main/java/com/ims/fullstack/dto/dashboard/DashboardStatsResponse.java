
package com.ims.fullstack.dto.dashboard;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardStatsResponse {
    private long totalJobs;
    private long openJobs;
    private long totalApplications;
    private long totalInterviews;
}