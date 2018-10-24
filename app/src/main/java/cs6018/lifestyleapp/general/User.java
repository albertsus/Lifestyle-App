package cs6018.lifestyleapp.general;

public class User {

    private static User INSTANCE = null;

    // Returns a single instance of this class, creating it if necessary.
    public static User getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new User();
        }
        return INSTANCE;
    }

    private static String UUID;

    public static void setUUID(String id) {
        UUID = id;
    }

    public static String getUUID() {
        return UUID;
    }

    private static String startWeight = null;
    private static String startBMI = null;
    private static String startCalories = null;
    private static String startHikes = null;

    public static void setStartData(User mUser) {
        if (startWeight == null) {
            startWeight = mUser.getWeight();
        }
        if (startBMI == null) {
            startBMI = mUser.getBmi();
        }
        if (startCalories == null) {
            startCalories = mUser.getBmr();
        }
        if (startHikes == null) {
            startHikes = "0";
        }
    }

    public static String getStartWeight() {
        return startWeight;
    }

    public static String getStartBMI() {
        return startBMI;
    }

    public static String getStartCalories() {
        return startCalories;
    }

    public static String getStartHikes() {
        return startHikes;
    }

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
    private String calories;

    // User Goals Data
    private String targetWeight;
    private String targetBMI;
    private String targetHikes;
    private String targetDailyCalories;
    private String weightGoal;

//    public User(String userName, String sex, String age, String city,
//                String nation, String height, String weight, String profilePic,
//                String bmi, String bmr, String calories, String targetWeight,
//                String targetBMI, String targetHikes, String targetDailyCalories,
//                String weightGoal) {
//        this.userName = userName;
//        this.sex = sex;
//        this.age = age;
//        this.city = city;
//        this.nation = nation;
//        this.height = height;
//        this.weight = weight;
//        this.profilePic = profilePic;
//        this.bmi = bmi;
//        this.bmr = bmr;
//        this.calories = calories;
//        this.targetWeight = targetWeight;
//        this.targetBMI = targetBMI;
//        this.targetHikes = targetHikes;
//        this.targetDailyCalories = targetDailyCalories;
//        this.weight = weightGoal;
//    }

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

    public String getCalories() { return calories; }

    public void setCalories(String calories) { this.calories = calories; }

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
