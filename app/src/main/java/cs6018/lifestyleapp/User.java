package cs6018.lifestyleapp;

public class User {

    // User Profile Data
    private String userName;
    private String sex;
    private String age;
    private String city;
    private String nation;
    private Integer height;
    private Integer weight;

    // User Goals Data
    private Integer targetWeight;
    private Integer targetBMI;
    private Integer targetHikes;
    private Integer targetDailyCalories;

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


}
