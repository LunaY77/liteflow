package com.yomahub.liteflow.test.ai.model.structure.output;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Step {

    @JsonProperty("explanation")
    private String explanation;

    @JsonProperty("output")
    private String output;

    // Getters and Setters
    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("    Step {\n");
        sb.append("      Explanation: \"").append(explanation).append("\"\n");
        sb.append("      Output: \"").append(output).append("\"\n");
        sb.append("    }");
        return sb.toString();
    }
}