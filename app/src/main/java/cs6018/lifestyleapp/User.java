package cs6018.lifestyleapp;

public class User {

    // User Profile Data
    private String userName;
    private String sex;
    private String age;
    private String city;
    private String nation;
    private String height;
    private String weight;
    private String profilePic;
    private String bmi;
    private String bmr;

    // User Goals Data
    private String targetWeight;
    private String targetBMI;
    private String targetHikes;
    private String targetDailyCalories;
    private String weightGoal;

    private enum WEIGHT_GOAL {
        LOOSE, GAIN, MAINTAIN
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getProfilePic() { return profilePic; }

    public void setProfilePic(String pic) { this.profilePic = pic; }

    public String getBmi() { return bmi; }

    public void setBmi(String bmi) { this.bmi = bmi; }

    public String getBmr() { return bmr; }

    public void setBmr(String bmr) { this.bmr = bmr; }

    public String getTargetWeight() {
        return targetWeight;
    }

    public void setTargetWeight(String targetWeight) {
        this.targetWeight = targetWeight;
    }

    public String getTargetBMI() {
        return targetBMI;
    }

    public void setTargetBMI(String targetBMI) {
        this.targetBMI = targetBMI;
    }

    public String getTargetHikes() {
        return targetHikes;
    }

    public void setTargetHikes(String targetHikes) {
        this.targetHikes = targetHikes;
    }

    public String getTargetDailyCalories() {
        return targetDailyCalories;
    }

    public void setTargetDailyCalories(String targetDailyCalories) {
        this.targetDailyCalories = targetDailyCalories;
    }

    public String getWeightGoal() {
        return weightGoal;
    }

    public void setWeightGoal(String wg) {
        this.weightGoal = wg;
    }

}
