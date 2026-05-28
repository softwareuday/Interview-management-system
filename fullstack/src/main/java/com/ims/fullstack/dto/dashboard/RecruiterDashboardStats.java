

package com.ims.fullstack.dto.dashboard;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecruiterDashboardStats {
    private Integer activeJobs;
    private Integer totalCandidates;
    private Integer interviewsToday;
    private Integer newApplications;
}