package cs6018.lifestyleapp.general;

/**
 * Created by suchaofan on 10/28/18.
 */

public class StepsData {

    private int steps;
    private int lastSaveSteps;
    private int goal;

    public StepsData() {}

    public StepsData(int steps, int lastSaveSteps, int goal) {
        this.steps = steps;
        this.lastSaveSteps = lastSaveSteps;
        this.goal = goal;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public int getLastSaveSteps() {
        return lastSaveSteps;
    }

    public void setLastSaveSteps(int lastSaveSteps) {
        this.lastSaveSteps = lastSaveSteps;
    }

    public int getGoal() { return goal; }

    public void setGoal(int stepsGoal) { this.goal = stepsGoal; }
}
