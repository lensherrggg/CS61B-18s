import edu.princeton.cs.algs4.Picture;
import java.awt.Color;
import java.util.Map;

public class SeamCarver {
    private Picture picture;
    private int width;
    private int height;

    public SeamCarver(Picture picture) {
        this.picture = picture;
        this.width = picture.width();
        this.height = picture.height();
    }

    // current picture
    public Picture picture() {
        return new Picture(this.picture);
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= width) {
            throw new IndexOutOfBoundsException("X Out of Bounds");
        }
        if (y < 0 || y >= height) {
            throw new IndexOutOfBoundsException("Y Out of Bounds");
        }
        return calcEnergyX(x, y) + calcEnergyY(x, y);
    }

    private double calcEnergyX(int x, int y) {
        int leftX = (x > 0) ? (x - 1) : (width - 1);
        int rightX = (x < width - 1) ? (x + 1) : 0;

        Color left = picture.get(leftX, y);
        Color right = picture.get(rightX, y);

        double rX = Math.abs(right.getRed() - left.getRed());
        double gX = Math.abs(right.getGreen() - left.getGreen());
        double bX = Math.abs(right.getBlue() - left.getBlue());

        return rX * rX + gX * gX + bX * bX;
    }

    private double calcEnergyY(int x, int y) {
        int upperY = (y > 0) ? (y - 1) : (height - 1);
        int lowerY = (y < height - 1) ? (y + 1) : 0;

        Color upper = picture.get(x, upperY);
        Color lower = picture.get(x, lowerY);

        double rY = Math.abs(upper.getRed() - lower.getRed());
        double gY = Math.abs(upper.getGreen() - lower.getGreen());
        double bY = Math.abs(upper.getBlue() - lower.getBlue());

        return rY * rY + gY * gY + bY * bY;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        transpose();
        int ans[] = findVerticalSeam();
        transpose();

        return ans;
    }

    private void transpose() {
        Picture temp = new Picture(height, width);
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                temp.set(row, col, picture.get(col, row));
            }
        }

        picture = temp;
        int tmp = height;
        this.height = width;
        this.width = tmp;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        double[][] minCost = new double[height][width];
        int[] ans = new int[height];
        for (int w = 0; w < width; w++) {
            minCost[0][w] = energy(w, 0);
        }

        for (int h = 1; h < height; h++) {
            for (int w = 0; w < width; w++) {
                double energy = energy(w, h);
                double min = getMin(minCost, h, w);
                minCost[h][w] = min + energy;
            }
        }

        double minVal = Double.MAX_VALUE;
        for (int w = 0; w < width; w++) {
            if (minCost[height - 1][w] < minVal) {
                minVal = minCost[height - 1][w];
                ans[height - 1] = w;
            }
        }

        // find index from bottom to top
        int count = height - 2;
        while (count >= 0) {
            ans[count] = findIndex(minCost, count + 1, ans[count + 1]);
            count -= 1;
        }

        return ans;
    }

    // find the lowest energy of upper level
    private double getMin(double[][] minCost, int h, int w) {
        if (w == 0) {
            return Math.min(minCost[h - 1][w], minCost[h - 1][w + 1]);
        }
        if (w == width - 1) {
            return Math.min(minCost[h - 1][w - 1], minCost[h - 1][w]);
        }
        return Math.min(minCost[h - 1][w - 1], Math.min(minCost[h - 1][w], minCost[h - 1][w + 1]));
    }

    // find the index of pixel of upper level with lowest energy
    private int findIndex(double[][] minCost, int h, int w) {
        if (w == 0) {
            if (minCost[h - 1][w] - minCost[h - 1][w + 1] <= 0) {
                return w;
            } else {
                return w + 1;
            }
        }

        if (w == width - 1) {
            if (minCost[h - 1][w - 1] - minCost[h - 1][w] <= 0) {
                return w - 1;
            } else {
                return w;
            }
        }

        int minIndex = -1;
        double min = Double.MAX_VALUE;
        for (int i = w - 1; i <= w + 1; i++) {
            if (minCost[h - 1][i] < min) {
                min = minCost[h - 1][i];
                minIndex = i;
            }
        }
        return minIndex;
    }

    // remove horizontal seam from picture
    public void removeHorizontalSeam(int[] seam) {
        if (checkHorizontalSeam(seam)) {
            picture = new Picture(SeamRemover.removeHorizontalSeam(picture, seam));
            height -= 1;
        } else {
            throw new IllegalArgumentException();
        }

    }

    private boolean checkHorizontalSeam(int[] seam) {
        if (seam.length != width) {
            return false;
        }
        return checkSeam(seam);
    }

    // remove vertical seam from picture
    public void removeVerticalSeam(int[] seam) {
        if (checkVerticalSeam(seam)) {
            picture = new Picture(SeamRemover.removeVerticalSeam(picture, seam));
            width -= 1;
        } else {
            throw new IllegalArgumentException();
        }
    }

    private boolean checkVerticalSeam(int[] seam) {
        if (seam.length != height) {
            return false;
        }
        return checkSeam(seam);
    }

    private boolean checkSeam(int[] seam) {
        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i] - seam[i + 1]) > 1) {
                return false;
            }
        }
        return true;
    }
}
