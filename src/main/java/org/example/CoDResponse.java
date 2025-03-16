package org.example;

import lombok.Data;
import lombok.Setter;

@Data
public class CoDResponse {
    String hexCode;

    @Setter
    private double coDScore;

    @Setter
    private double totalCalories;

    @Setter
    private double redPercent;

    @Setter
    private double greenPercent;

    @Setter
    private double yellowPercent;

    @Setter
    private double idealCalorieIntake;

    // Method to format the number to 2 decimal places
    private double formatNumber(double number) {
        return Math.round(number * 100.0) / 100.0;
    }

    // Custom setter for totalCalories
    public void setTotalCalories(double totalCalories) {
        this.totalCalories = formatNumber(totalCalories);
    }

    // Custom setter for redPercent
    public void setRedPercent(double redPercent) {
        this.redPercent = formatNumber(redPercent);
    }

    // Custom setter for yellowPercent
    public void setYellowPercent(double yellowPercent) {
        this.yellowPercent = formatNumber(yellowPercent);
    }

    // Custom setter for greenPercent
    public void setGreenPercent(double greenPercent) {
        this.greenPercent = formatNumber(greenPercent);
    }

    // Custom setter for coDScore
    public void setCoDScore(double coDScore) {
        this.coDScore = formatNumber(coDScore);
    }

    // Custom setter for idealCalorieIntake
    public void setIdealCalorieIntake(double idealCalorieIntake) {
        this.idealCalorieIntake = formatNumber(idealCalorieIntake);
    }
}
