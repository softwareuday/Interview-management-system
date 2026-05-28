package com.ims.fullstack.service;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class GeminiAtsService {

    private static final Set<String> STOP_WORDS = Set.of(
            "a", "an", "and", "are", "as", "at", "be", "by", "for", "from", "has", "he",
            "in", "is", "it", "its", "of", "on", "that", "the", "to", "was", "were",
            "will", "with", "i", "you", "we", "they", "this", "these", "those",
            "am", "do", "does", "did", "doing", "have", "having", "not", "so", "but",
            "or", "nor", "yet", "using", "based", "design", "implement", "write",
            "maintain", "ensure", "collaborate", "optimize", "reviews", "developers",
            "like", "such", "able", "ability", "experience", "knowledge", "skills",
            "work", "team", "business", "product", "code", "clean", "best", "practices",
            "participate", "efficient", "application", "restful", "frameworks", "scalability"
    );

    public Map<String, Object> analyzeResume(String resumeText, String jobDescription) {
        Set<String> resumeWords = extractMeaningfulWords(resumeText);
        Set<String> jobWords = extractMeaningfulWords(jobDescription);

        Set<String> matched = new HashSet<>(resumeWords);
        matched.retainAll(jobWords);

        Set<String> missing = new HashSet<>(jobWords);
        missing.removeAll(resumeWords);

        int score = jobWords.isEmpty() ? 0 : (int) ((matched.size() * 100.0) / jobWords.size());

        String recommendation;
        if (score >= 70) {
            recommendation = "Excellent match! Your resume strongly aligns with the job requirements. Proceed to apply.";
        } else if (score >= 50) {
            recommendation = "Good match. Consider adding these missing keywords to your resume: " +
                    String.join(", ", missing.stream().limit(5).toList());
        } else {
            recommendation = "Low match. Tailor your resume to include key skills like: " +
                    String.join(", ", missing.stream().limit(5).toList());
        }

        Map<String, Object> result = new HashMap<>();
        result.put("score", Math.min(score, 100));
        result.put("matchedSkills", new ArrayList<>(matched));
        result.put("missingSkills", new ArrayList<>(missing).stream().limit(15).toList());
        result.put("recommendation", recommendation);
        return result;
    }

    private Set<String> extractMeaningfulWords(String text) {
        if (text == null || text.trim().isEmpty()) return Set.of();
        String[] words = text.toLowerCase().replaceAll("[^a-z0-9 ]", " ").split("\\s+");
        Set<String> filtered = new HashSet<>();
        for (String w : words) {
            if (w.length() > 2 && !STOP_WORDS.contains(w)) {
                filtered.add(w);
            }
        }
        return filtered;
    }
}