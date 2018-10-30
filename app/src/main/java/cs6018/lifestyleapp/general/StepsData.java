package cs6018.lifestyleapp.general;

/**
 * Created by suchaofan on 10/28/18.
 */

public class StepsData {

    private int steps;
    private int lastSaveSteps;

    public StepsData() {}

    public StepsData(int steps, int lastSaveSteps) {
        this.steps = steps;
        this.lastSaveSteps = lastSaveSteps;
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
}
