package lab14;

import lab14lib.Generator;

public class AcceleratingSawToothGenerator implements Generator {
    private int period;
    private double factor;
    private int state;

    public AcceleratingSawToothGenerator(int period, double factor) {
        this.period = period;
        this.factor = factor;
        this.state = 0;
    }

    private double normalize(int toConvert, int period) {
        return 2.0 * toConvert / (period - 1) - 1;
    }

    @Override
    public double next() {
        double val = normalize(state, period);
        state += 1;
        if (state >= period) {
            state = 0;
            period = (int) Math.floor(period * factor);
        }
        return val;
    }
}
