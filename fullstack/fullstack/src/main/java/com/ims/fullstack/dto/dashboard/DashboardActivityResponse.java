
package com.ims.fullstack.dto.dashboard;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class DashboardActivityResponse {
    private String type;
    private String description;
    private LocalDateTime time;
}