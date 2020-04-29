package com.example.clicker.ui.login;

import com.google.firebase.database.PropertyName;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Question {
    private String question_text;
    private Map<String, String> answer_correctness_map;
    private int num_responses;
    private boolean is_active;

    Question() {
        this.question_text = "";
        this.answer_correctness_map = new HashMap<String, String>();
        this.num_responses = 0;
        this.is_active = false;
    }

    Question(Question other) {
        this.question_text = other.question_text;
        this.num_responses = other.num_responses;
        this.is_active = other.is_active;
        this.answer_correctness_map = new HashMap<String, String>();
        for (Map.Entry<String, String> e : other.getAnswerCorrectnessMap().entrySet()) {
            this.answer_correctness_map.put(e.getKey(), e.getValue());
        }
    }

    public String getQuestionText() { return this.question_text; }

    @PropertyName("AnswerMap")
    public Map<String, String> getAnswerCorrectnessMap() { return this.answer_correctness_map; }

    public int getNumResponses() { return this.num_responses; }

    @PropertyName("isActive")
    public boolean isActive() { return this.is_active; }

    public void setQuestionText(String text) { this.question_text = text; }

    @PropertyName("AnswerMap")
    public void setAnswerCorrectnessMap(Map<String, String> map_in) {
        for (Map.Entry<String, String> e : map_in.entrySet()) {
            this.answer_correctness_map.put(e.getKey(), e.getValue());
        }
    }

    @PropertyName("isActive")
    public void setActive(boolean active) {
        this.is_active = active;
    }

    public void sanitize(boolean for_db) {
        List<Pair<String, String>> sanitized_list = new ArrayList<Pair<String, String>>();
        List<String> to_be_sanitized_list = new ArrayList<String>();
        for (Map.Entry<String, String> e : answer_correctness_map.entrySet()) {
            String to_be_sanitized = e.getKey();
            Pair<String, String> sanitized = sanitizeString(to_be_sanitized, for_db, false);
            to_be_sanitized_list.add(to_be_sanitized);
            sanitized_list.add(sanitized);
        }

        for (int i = 0; i < sanitized_list.size(); ++i) {
            answer_correctness_map.remove(to_be_sanitized_list.get(i));
            answer_correctness_map.put(sanitized_list.get(i).getKey(), sanitized_list.get(i).getValue());
        }

        this.question_text = sanitizeString(this.question_text, for_db, true).getKey();
    }

    public Pair<String, String> sanitizeString(String str, boolean for_db, boolean is_question_text) {
        List<Pair<String, String>> rules_list = getReplacementRules(for_db);

        String is_correct = str.contains("\\correct") ? "true" : "false";
        String sanitized = str.replace("\\correct", "");
        int i = 0;
        int j = sanitized.length() - 1;
        while (sanitized.charAt(i) == ' ') { ++i; }
        while (sanitized.charAt(j) == ' ') { --j; }
        sanitized = str.substring(i, j + 1);

        if (!is_question_text && (answer_correctness_map.get(str).equals("true") || answer_correctness_map.size() == 1)) {
            is_correct = "true";
        }

        for (int count = 0; count < rules_list.size(); ++count) {
            Pair<String, String> p = rules_list.get(count);
            sanitized = sanitized.replace(p.getKey(), p.getValue());
        }

        return Pair.of(sanitized, is_correct);
    }

    public static List<Pair<String, String>> getReplacementRules(boolean for_db) {
        return new ArrayList<Pair<String, String>>(
                for_db ?
                        Arrays.asList( // rules for sending to db
                                Pair.of(".", "\\period"),
                                Pair.of("$", "\\dollarSign"),
                                Pair.of("[", "\\leftSquareBracket"),
                                Pair.of("]", "\\rightSquareBracket"),
                                Pair.of("#", "\\hashtag"),
                                Pair.of("/", "\\forwardSlash")
                        )
                        :
                        Arrays.asList( // rules for displaying to client
                                Pair.of("\\period", "."),
                                Pair.of("\\dollarSign", "$"),
                                Pair.of("\\leftSquareBracket", "["),
                                Pair.of("\\rightSquareBracket", "]"),
                                Pair.of("\\hashtag", "#"),
                                Pair.of("\\forwardSlash", "/")
                        )
        );
    }
}
