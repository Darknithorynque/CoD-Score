package org.example;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class CoDResponse {
    String hexCode;

    double CoDScore;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    double TotalCalories;

    double RedPercent;

    double GreenPercent;

    double YellowPercent;

    double IdealCalorieIntake;
}
