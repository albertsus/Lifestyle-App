package cs6018.lifestyleapp;

public class CalculatorUtils {

    public static int computeBMI(String weight, String height) {
        int lbs = Integer.parseInt(weight);
        int inches = Integer.parseInt(height);
        return lbs * 703 / (inches * inches);
    }

    public static int computeBMR(String weight, String height, String sex, String age) {
        int lbs = Integer.parseInt(weight);
        int inches = Integer.parseInt(height);
        int ageNum = Integer.parseInt(age);
        return sex.equals("Male") ?
                ((66 + (6 * lbs) + (12 * inches) - (6 * ageNum))) :
                ((655 + (4 * lbs) + (5 * inches) - (7 * ageNum)));
    }

}
