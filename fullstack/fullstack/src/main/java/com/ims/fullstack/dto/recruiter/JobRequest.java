//package com.ims.fullstack.dto.recruiter;
//
//import com.ims.fullstack.model.JobType;
//import lombok.*;
//import java.time.LocalDate;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class JobRequest {
//    private String title;
//    private String description;
//    private String department;
//    private String location;
//    private String salaryRange;
//    private String experienceRequired;
//    private String requiredSkills; // Comma-separated or JSON array
//    private JobType jobType;
//    private LocalDate lastDateToApply;
//}

package com.ims.fullstack.dto.recruiter;

import com.ims.fullstack.model.enums.JobType;
import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobRequest {
    private String title;
    private String description;
    private String department;
    private String location;
    private String salaryRange;
    private String experienceRequired;
    private String requiredSkills;
    private JobType jobType;
    private LocalDate lastDateToApply;
}