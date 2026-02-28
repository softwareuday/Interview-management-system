//package com.ims.fullstack.service;
//
//import org.springframework.stereotype.Service;
//
//@Service
//public class ResumeParserService {
//
//    public String extractText(String resumeText) {
//        if (resumeText == null) return "";
//        return resumeText
//                .toLowerCase()
//                .replaceAll("[^a-z0-9 ]", " ");
//    }
//}
package com.ims.fullstack.service;

import org.springframework.stereotype.Service;

@Service
public class ResumeParserService {
    public String extractText(String resumeText) {
        if (resumeText == null) return "";
        return resumeText.toLowerCase().replaceAll("[^a-z0-9 ]", " ");
    }
}