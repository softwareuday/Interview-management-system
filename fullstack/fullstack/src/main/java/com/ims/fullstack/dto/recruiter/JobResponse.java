

package com.ims.fullstack.dto.recruiter;

import com.ims.fullstack.model.enums.JobType;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobResponse {
    private Long id;
    private String title;
    private String description;
    private String department;
    private String location;
    private String salaryRange;
    private String experienceRequired;
    private String status;
    private String companyName;
    private Integer applicantsCount;
    private Boolean hasApplied;
    private List<String> requiredSkills;
    private JobType jobType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDate lastDateToApply;
}