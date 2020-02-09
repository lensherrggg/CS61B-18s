package lab14;

import lab14lib.*;

public class SawToothGenerator implements Generator {
    private int period;
    private int state;

    public SawToothGenerator(int period) {
        state = 0;
        this.period = period;
    }

    private double normalize(int toConvert) {
        return 2.0 * toConvert / (period - 1) - 1;
    }

    @Override
    public double next() {
        double val = normalize(state);
        state = (state + 1) % period;
        return val;
    }
}
