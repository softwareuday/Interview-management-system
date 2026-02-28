//package com.ims.fullstack.util;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//public class TextSimilarityUtil {
//
//    public static Set<String> extractKeywords(String text) {
//        return Arrays.stream(text.split("\\s+"))
//                .filter(word -> word.length() > 2)
//                .collect(Collectors.toSet());
//    }
//
//    public static int calculateScore(Set<String> resume, Set<String> job) {
//        if (job.isEmpty()) return 0;
//        long matched = resume.stream().filter(job::contains).count();
//        return (int) ((matched * 100) / job.size());
//    }
//}
package com.ims.fullstack.util;

import java.util.*;
import java.util.stream.Collectors;

public class TextSimilarityUtil {
    public static Set<String> extractKeywords(String text) {
        return Arrays.stream(text.split("\\s+"))
                .filter(word -> word.length() > 2)
                .collect(Collectors.toSet());
    }

    public static int calculateScore(Set<String> resume, Set<String> job) {
        if (job.isEmpty()) return 0;
        long matched = resume.stream().filter(job::contains).count();
        return (int) ((matched * 100) / job.size());
    }
}