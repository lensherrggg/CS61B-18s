package lab14;

import lab14lib.Generator;

public class StrangeBitwiseGenerator implements Generator {
    private int period;
    private int state;

    public StrangeBitwiseGenerator(int period) {
        state = 0;
        this.period = period;
    }

    private double normalize(int toConvert) {
        return 2.0 * toConvert / (period - 1) - 1;
    }

    @Override
    public double next() {
        state += 1;
        int weirdState = state & (state >>> 3) % period;
        weirdState = state & (state >> 3) & (state >> 8) % period;
        return normalize(weirdState % period);
    }
}
