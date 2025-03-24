package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CoDService {

    CoDResponse response = new CoDResponse();

    Logger logger = LoggerFactory.getLogger(CoDService.class);

    @Value("${coefficient.red-color}")
    private int redColorCoefficient;

    @Value("${coefficient.yellow-color}")
    private int yellowColorCoefficient;

    @Value("${coefficient.green-color}")
    private int greenColorCoefficient;


    public ResponseEntity<CoDResponse> colorOfDay(CoDRequest request) {

        try {
            String hexCode = calculateCoD(request);
            response.setTotalCalories(request.totalCalories());
            response.setHexCode(hexCode);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException | ArithmeticException e) {
            logger.error(e.getMessage(), e);
            response.setHexCode(HexCode.GRAY.getHexCode());
        }
        return ResponseEntity.ok(response);
    }

    public String calculateCoD(CoDRequest request) {

        try {
            double idealCalorieIntake = calculateIdealCalorieIntake(request.getHeight(), request.getWeight(), request.getAge(), request.getActivityLevel(), request.isSex());
            if (request.totalCalories() == 0 || idealCalorieIntake == 0) {
                logger.warn("Total calories or ideal calorie intake is zero. Returning gray color.");
                return HexCode.GRAY.getHexCode(); // Default to gray
            }

            double modify = Math.max(0.81, Math.min(1.2, (request.totalCalories() / idealCalorieIntake)));
            double redPercentage = (request.getRedLabelMealCalories() * 100) / request.totalCalories();
            double yellowPercentage = (request.getYellowLabelMealCalories() * 100) / request.totalCalories();
            double greenPercentage = (request.getGreenLabelMealCalories() * 100) / request.totalCalories();

            response.setRedPercent(redPercentage);
            response.setYellowPercent(yellowPercentage);
            response.setGreenPercent(greenPercentage);
            response.setIdealCalorieIntake(idealCalorieIntake);

            double redEffect =  (request.getRedLabelMealCalories()/ request.totalCalories())*redColorCoefficient;
            double yellowEffect = (request.getYellowLabelMealCalories()/ request.totalCalories())*yellowColorCoefficient;
            double greenEffect = (request.getGreenLabelMealCalories()/ request.totalCalories())*greenColorCoefficient;
            System.out.println("Red: "+redEffect +" "+ redPercentage + "Yellow: "+ yellowEffect+ " Green: "+greenEffect + " modify: "+modify);

            double score = (redEffect + yellowEffect+ greenEffect)
                    * modify;
            response.setCoDScore(score);

            return getColorFromScore(score).getHexCode();
        } catch (ArithmeticException e) {
            logger.error("Arithmetic error during CoD calculation: {}", e.getMessage(), e);
            throw new ArithmeticException("Error calculating Color of Day due to invalid arithmetic operation.");
        } catch (Exception e) {
            logger.error("Unexpected error during CoD calculation: {}", e.getMessage(), e);
            throw new RuntimeException("Unexpected error in colorOfDay calculation.");
        }

    }

    private HexCode getColorFromScore(double score) {
        if (score >= 81 && score < 125) return HexCode.GREEN;
        else if (score < 150) return HexCode.MILD_GREEN;
        else if (score < 170) return HexCode.DARK_GREEN;
        else if (score < 190) return HexCode.YELLOW;
        else if (score < 210) return HexCode.GOLD;
        else if (score < 230) return HexCode.ORANGE;
        else if (score < 240) return HexCode.DARK_ORANGE;
        else if (score < 260) return HexCode.RED_ORANGE;
        else if (score < 280) return HexCode.LIGHT_RED;
        else if (score < 300) return HexCode.RED;
        else if (score > 300) return HexCode.DEEP_RED;
        return HexCode.GRAY;
    }


    public double calculateIdealCalorieIntake(double height, double weight, int age, ActivityLevel activityLevel, boolean sex) {

        try {
            double bmrFormula = 10*weight + 6.25*height - 5*age;
            bmrFormula = sex ? bmrFormula + 5 : bmrFormula - 161;

            double requiredCalorieDeficit = bodyMassIndexFactory(height,weight);

            double activityFactor = switch (activityLevel){
                case SEDENTARY -> 1.2;
                case LIGHTLY_ACTIVE -> 1.375;
                case MODERATELY_ACTIVE -> 1.55;
                case VERY_ACTIVE -> 1.75;
            };
            

            //to lose 0.75 to 1kg weekly
            return activityFactor * bmrFormula - requiredCalorieDeficit;
        } catch (ArithmeticException e) {
            logger.error("Arithmetic error during CoD calculation: {}", e.getMessage(), e);
            throw new RuntimeException("Arithmetic error during CoD calculation.");
        } catch (Exception e) {
            logger.error("Unexpected error during CoD calculation: {}", e.getMessage(), e);
            throw new RuntimeException("Unexpected error during CoD calculation.");
        }


    }

    public double bodyMassIndexFactory(double height, double weight){
        double bmi =  weight/(height/100*height/100);
        
        if (bmi <= 18.5){
            return -500;
        } else if (bmi > 18.5 && bmi<= 24.9) {
            return 0;
        } else if (bmi > 24.9 && bmi <= 29.9) {
            return 600;
        } else if (bmi>29.9 && bmi <= 34.9) {
            return 800;
        } else if (bmi > 34.9 && bmi <= 39.9) {
            return 1000;
        } else if (bmi > 39.9) {
            return 1300;
        } else {
            return 0;
        }
    }

}
