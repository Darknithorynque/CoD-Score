package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CoDService {

    CoDResponse response = new CoDResponse();

    Logger logger = LoggerFactory.getLogger(CoDService.class);

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
            double idealCalorieIntake = CalculateIdealCalorieIntake(request.getHeight(), request.getWeight(), request.getAge(), request.getActivityLevel(), request.isSex());
            if (request.totalCalories() == 0 || idealCalorieIntake == 0) {
                logger.warn("Total calories or ideal calorie intake is zero. Returning gray color.");
                return HexCode.GRAY.getHexCode(); // Default to gray
            }

            double modify = Math.max(0.81, Math.min(1.2, request.totalCalories()) / idealCalorieIntake);
            double redPercentage = (request.getRedLabelMealCalories() / request.totalCalories()) * 100;
            double yellowPercentage = (request.getYellowLabelMealCalories() / request.totalCalories()) * 100;
            double greenPercentage = (request.getGreenLabelMealCalories() / request.totalCalories()) * 100;

            response.setRedPercent(redPercentage);
            response.setYellowPercent(yellowPercentage);
            response.setGreenPercent(greenPercentage);
            response.setIdealCalorieIntake(idealCalorieIntake);

            double score = ((redPercentage * 3) + (yellowPercentage * 2) + (greenPercentage * 1))
                    * modify * (1 + 0.005 * redPercentage) * (1 + 0.0025 * yellowPercentage);
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
        else if (score < 150) return HexCode.DARK_GREEN;
        else if (score < 170) return HexCode.LIGHT_GREEN;
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


    public double CalculateIdealCalorieIntake(double height, double weight, int age, ActivityLevel activityLevel, boolean sex) {

        try {
            double bmrFormula = 10*weight + 6.25*height - 5*age;
            bmrFormula = sex ? bmrFormula + 5 : bmrFormula - 161;

            double activityFactor = switch (activityLevel){
                case SEDENTARY -> 1.2;
                case LIGHTLY_ACTIVE -> 1.375;
                case MODERATELY_ACTIVE -> 1.55;
                case VERY_ACTIVE -> 1.75;
            };

            //to lose 0.75 to 1kg weekly
            return activityFactor * bmrFormula - 500;
        } catch (ArithmeticException e) {
            logger.error("Arithmetic error during CoD calculation: {}", e.getMessage(), e);
            throw new RuntimeException("Arithmetic error during CoD calculation.");
        } catch (Exception e) {
            logger.error("Unexpected error during CoD calculation: {}", e.getMessage(), e);
            throw new RuntimeException("Unexpected error during CoD calculation.");
        }


    }
}
