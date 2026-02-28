//package com.ims.fullstack.dto.dashboard;
//
//import lombok.*;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class RecruiterDashboardStats {
//    private Integer activeJobs;        // status = OPEN
//    private Integer totalCandidates;   // Sum of all applicants
//    private Integer interviewsToday;   // Interviews scheduled for today
//    private Integer newApplications;   // Applications in last 24 hours
//}

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