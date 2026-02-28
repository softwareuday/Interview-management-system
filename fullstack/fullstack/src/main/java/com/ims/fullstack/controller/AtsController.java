//package com.ims.fullstack.ats.controller;
//
//import com.ims.fullstack.dto.ats.AtsScanRequest;
//import com.ims.fullstack.dto.ats.AtsScanResponse;
//import com.ims.fullstack.model.AtsScoreResult;
//import com.ims.fullstack.service.AtsScoringService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/ats")
//@RequiredArgsConstructor
//public class AtsController {
//
//    private final AtsScoringService atsScoringService;
//
//    @PostMapping("/scan")
//    public AtsScanResponse scan(@RequestBody AtsScanRequest request) {
//
//        AtsScoreResult result =
//                atsScoringService.scan(request.getCandidateId(), request.getJobId());
//
//        return AtsScanResponse.builder()
//                .atsScore(result.getScore())
//                .matchedKeywords(result.getMatched())
//                .missingKeywords(result.getMissing())
//                .build();
//    }
//}
package com.ims.fullstack.controller;

import com.ims.fullstack.dto.ats.AtsScanRequest;
import com.ims.fullstack.dto.ats.AtsScanResponse;
import com.ims.fullstack.model.AtsScoreResult;
import com.ims.fullstack.service.AtsScoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ats")
@RequiredArgsConstructor
public class AtsController {
    private final AtsScoringService atsScoringService;

    @PostMapping("/scan")
    public AtsScanResponse scan(@RequestBody AtsScanRequest request) {
        AtsScoreResult result = atsScoringService.scan(request.getCandidateId(), request.getJobId());
        return AtsScanResponse.builder()
                .atsScore(result.getScore())
                .matchedKeywords(result.getMatched())
                .missingKeywords(result.getMissing())
                .build();
    }
}