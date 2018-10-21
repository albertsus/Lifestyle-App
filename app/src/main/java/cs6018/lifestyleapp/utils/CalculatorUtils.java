package cs6018.lifestyleapp.utils;

public class CalculatorUtils {

    public static Float computeBMI(String weight, String height) {
        Float bmi = (Float.parseFloat(weight) * 703) / ((Float.parseFloat(height)) * (Float.parseFloat(height)));
        bmi = Float.parseFloat(String.format("%.2f", bmi));
        return bmi;
    }

    public static int computeBMR(String weight, String height, String sex, String age) {
        int lbs = Integer.parseInt(weight);
        int inches = Integer.parseInt(height);
        int ageNum = Integer.parseInt(age);
        return sex.equals("Male") ?
                ((66 + (6 * lbs) + (12 * inches) - (6 * ageNum))) :
                ((655 + (4 * lbs) + (5 * inches) - (7 * ageNum)));
    }

    public static Float[] computeNormalWeight(String height) {
        Float lWeight = 19 * ((Float.parseFloat(height)) * (Float.parseFloat(height)));
        Float hWeight = 25 * ((Float.parseFloat(height)) * (Float.parseFloat(height)));
        return new Float[] {lWeight, hWeight};
    }

}
