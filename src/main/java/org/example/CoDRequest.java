package org.example;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class CoDRequest {

    @NotNull(message = "Red labelled meal calories cannot be null")
    @Min(0)
    private double redLabelMealCalories;

    @NotNull(message = "Yellow labelled meal calories cannot be null")
    @Min(0)
    private double yellowLabelMealCalories;

    @NotNull(message = "Green labelled meal calories cannot be null")
    @Min(0)
    private double greenLabelMealCalories;

    @NotNull(message = "Age cannot be null")
    @Positive
    private int age;

    @NotNull
    @Positive
    private double height;

    @NotNull
    @Positive
    private double weight;

    @NotNull
    private boolean sex;

    @NotNull
    private ActivityLevel activityLevel;

    public double totalCalories(){
        return this.redLabelMealCalories + this.yellowLabelMealCalories + this.greenLabelMealCalories;
    }

}
