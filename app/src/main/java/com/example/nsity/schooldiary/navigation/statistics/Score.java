package com.example.nsity.schooldiary.navigation.statistics;

import com.example.nsity.schooldiary.navigation.marks.Subject;

/**
 * Created by nsity on 17.01.16.
 */
public class Score {

    private Subject subject;

    private double minAverageScore;
    private double maxAverageScore;
    private double averageScore;

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public double getMinAverageScore() {
        return minAverageScore;
    }

    public void setMinAverageScore(double minAverageScore) {
        this.minAverageScore = minAverageScore;
    }

    public double getMaxAverageScore() {
        return maxAverageScore;
    }

    public void setMaxAverageScore(double maxAverageScore) {
        this.maxAverageScore = maxAverageScore;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(double averageScore) {
        this.averageScore = averageScore;
    }
}
