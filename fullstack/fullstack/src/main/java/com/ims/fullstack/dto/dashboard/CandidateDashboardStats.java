//package com.ims.fullstack.dto.dashboard;
//
//import lombok.*;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class CandidateDashboardStats {
//    private Integer totalApplications;
//    private Integer activeApplications; // Not REJECTED/SELECTED
//    private Integer upcomingInterviews;
//    private Integer savedJobs; // Optional - can be 0 for now
//}

package com.ims.fullstack.dto.dashboard;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateDashboardStats {
    private Integer totalApplications;
    private Integer activeApplications;
    private Integer upcomingInterviews;
    private Integer savedJobs;
}