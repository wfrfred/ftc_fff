package pid;


/**
 * @author deng
 * @date 2019/12/30
 */
public class PIDParam {
    private double kP;
    private double kI;
    private double kD;
    private double targetVal;
    private double rangeMin;
    private double rangeMax;
    private boolean useRange;

    public PIDParam(double kP, double kI, double kD, double targetVal) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.targetVal = targetVal;
        this.useRange = false;
    }

    public PIDParam(double kP, double kI, double kD, double targetVal, double rangeMin, double rangeMax) {
        if (rangeMin > rangeMax) {
            throw new RuntimeException("illegal range");
        }

        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.targetVal = targetVal;
        this.rangeMin = rangeMin;
        this.rangeMax = rangeMax;
        this.useRange = true;
    }

    public double getRangeMax() {
        return rangeMax;
    }

    public double getTargetVal() {
        return targetVal;
    }

    public double getRangeMin() {
        return rangeMin;
    }


    public double getKP() {
        return kP;
    }

    public boolean isUseRange() {
        return useRange;
    }

    public double getKI() {
        return kI;
    }

    public double getKD() {
        return kD;
    }

    public void setTargetVal(double targetVal) {
        this.targetVal = targetVal;
    }
}
