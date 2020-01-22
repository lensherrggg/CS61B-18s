package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] grid;
    private int size;
    private int top;
    private int bottom;
    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF ufTopOnly;
    private int numOpenSites = 0;

    /** create N-by-N grid, with all sites initially blocked */
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("N must be bigger than 0");
        }
        grid = new boolean[N][N];
        size = N;
        top = 0;
        bottom = N * N + 1;
        uf = new WeightedQuickUnionUF(N * N + 2);
        ufTopOnly = new WeightedQuickUnionUF(N * N + 1); // prevent backwash
    }

    /** transform (row, col) to 1D coordinate */
    private int xyTo1D(int row, int col) {
        return row * size + col + 1;
    }

    /** check if (row, col) is out of bound */
    private void validate(int row, int col) {
        if (row < 0 || col < 0 || row >= size || col >= size) {
            throw new IndexOutOfBoundsException("Index out of bound");
        }
    }

    /** open the site (row, col) if it is not open already  */
    public void open(int row, int col) {
        validate(row, col);
        if (!isOpen(row, col)) {
            grid[row][col] = true;
            numOpenSites += 1;
        }
        if (row == 0) {
            uf.union(top, xyTo1D(row, col));
            ufTopOnly.union(top, xyTo1D(row, col));
        }
        if (row == size - 1) {
            uf.union(xyTo1D(row, col), bottom);
        }
        if (row > 0 && isOpen(row - 1, col)) {
            uf.union(xyTo1D(row, col), xyTo1D(row - 1, col));
            ufTopOnly.union(xyTo1D(row, col), xyTo1D(row - 1, col));
        }
        if (row < size - 1 && isOpen(row + 1, col)) {
            uf.union(xyTo1D(row, col), xyTo1D(row + 1, col));
            ufTopOnly.union(xyTo1D(row, col), xyTo1D(row + 1, col));
        }
        if (col > 0 && isOpen(row, col - 1)) {
            uf.union(xyTo1D(row, col), xyTo1D(row, col - 1));
            ufTopOnly.union(xyTo1D(row, col), xyTo1D(row, col - 1));
        }
        if (col < size - 1 && isOpen(row, col + 1)) {
            uf.union(xyTo1D(row, col), xyTo1D(row, col + 1));
            ufTopOnly.union(xyTo1D(row, col), xyTo1D(row, col + 1));
        }
    }
    /** is the site (row, col) open? */
    public boolean isOpen(int row, int col) {
        validate(row, col);
        return grid[row][col];
    }
    /** is the site (row, col) full? */
    public boolean isFull(int row, int col) {
        validate(row, col);
        return ufTopOnly.connected(xyTo1D(row, col), top);
    }
    /** number of open sites */
    public int numberOfOpenSites() {
        return numOpenSites;
    }
    /** does the system percolate? */
    public boolean percolates() {
        return uf.connected(top, bottom);
    }
    /** use for unit testing (not required) */
    public static void main(String[] args)  {

    }
}
