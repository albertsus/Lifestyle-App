package cs6018.lifestyleapp;

public class User {

    // User data.
    private String userName = "Ann12345";
    private String sex = "Female";
    private String city = "Swansea";
    private String nation = "Wales";
    private Integer height = 120;
    private Integer weight = 145;

    // User goals.
    private Integer targetWeight = 130;
    private Integer targetBMI = 22;

    private Integer targetHikes = 3;
    private Integer targetDailyCalories = 2000;
    private enum Season {
        LOOSE, GAIN, MAINTAIN
    }

    public Integer getTargetBMI() {
        return targetBMI;
    }

    public void setTargetBMI(Integer targetBMI) {
        this.targetBMI = targetBMI;
    }

    public Integer getTargetHikes() {
        return targetHikes;
    }

    public void setTargetHikes(Integer targetHikes) {
        this.targetHikes = targetHikes;
    }

    public Integer getTargetDailyCalories() {
        return targetDailyCalories;
    }

    public void setTargetDailyCalories(Integer targetDailyCalories) {
        this.targetDailyCalories = targetDailyCalories;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    private String age = "38";

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

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getTargetWeight() {
        return targetWeight;
    }

    public void setTargetWeight(Integer targetWeight) {
        this.targetWeight = targetWeight;
    }



}
