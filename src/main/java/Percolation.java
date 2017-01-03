import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.stream.*;

/**
 * Implements percolation based on weighted union-find algorithm
 * Created by David on 02/01/2017.
 */
// Corner cases.  By convention, the row and column indices are integers between 1 and n, where (1, 1)
// is the upper-left site: Throw a java.lang.IndexOutOfBoundsException if any argument to open(),
// isOpen(), or isFull() is outside its prescribed range.
// The constructor should throw a java.lang.IllegalArgumentException if n ≤ 0.
public class Percolation {

    private static final int FULL = 0;
    private static final int OPEN = 1;

    private final int N;

    private final int VIRTUAL_TOP;
    private final int VIRTUAL_BOTTOM;

    private int openSites = 0;
    private int[][] grid;
    private final WeightedQuickUnionUF wUnionUF;

    // create n-by-n grid, with all sites blocked
    public Percolation(int n){
        if(n < 0){
            throw new IllegalArgumentException("Grid size should be greated than 0");
        }
        grid = new int[n][n];
        wUnionUF = new WeightedQuickUnionUF((n*n)+2);
        N = n;

        VIRTUAL_TOP = N * N;
        VIRTUAL_BOTTOM = (N * N) + 1;
        initialize();
    }

    //open virtual sites and all paths to lower and upper rows
    private void initialize(){
        //open from VIRTUAL_TOP to first row elements
        IntStream.rangeClosed(1, N).map(col -> plainIndex(0, col - 1)).forEach(j -> wUnionUF.union(VIRTUAL_TOP, j));

        //open from VIRTUAL_BOTTOM to last row elements
        IntStream.rangeClosed(1, N).map(col -> plainIndex(N-1, col - 1)).forEach(j -> wUnionUF.union(VIRTUAL_BOTTOM, j));
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col){
        checkIndices(row, col);
        //if not yet open, open it
        if(isFull(row, col)){
            grid[row-1][col-1] = OPEN;

            this.openSites++;
            //check if any of the neighbors is open, if so, connect current site to it
            //check (row, col-1),(row,col+1),(row+1,col),(row-1,col)
            connectWithNeighborBelow(row, col);
            connectWithNeighborAbove(row, col);
            connectWithNeighborLeft(row, col);
            connectWithNeighborRight(row, col);
        }
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col){
        checkIndices(row, col);
        return grid[row-1][col-1] == OPEN;
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col){
        return !this.isOpen(row, col);
    }

    // number of open sites
    public int numberOfOpenSites(){
        return this.openSites;
    }

    // does the system percolate?
    public boolean percolates(){
        return wUnionUF.connected(VIRTUAL_TOP, VIRTUAL_BOTTOM);
    }

    /**
     * Connect with neighbor below (row, col)
     * @param row
     * @param col
     */
    private void connectWithNeighborBelow(int row, int col) {
        connectWithNeighbor(row, col, row, col + 1);
    }

    /**
     * Connect with neighbot above (row, col)
     * @param row
     * @param col
     */
    private void connectWithNeighborAbove(int row, int col) {
        connectWithNeighbor(row, col, row, col - 1);
    }

    /**
     * Connect with neighbor at the left of (row, col)
     * @param row
     * @param col
     */
    private void connectWithNeighborLeft(int row, int col) {
        connectWithNeighbor(row, col, row - 1, col);
    }

    /**
     * Connect with neighbor at the right of (row, col)
     * @param row
     * @param col
     */
    private void connectWithNeighborRight(int row, int col) {
        connectWithNeighbor(row, col, row + 1, col);
    }

    /**
     * Connect a site with a given neighbor
     * @param row
     * @param col
     * @param neighborRow
     * @param neighborCol
     */
    private void connectWithNeighbor(int row, int col, int neighborRow, int neighborCol) {
        if(isValidNeighbor(neighborRow, neighborCol)) {
            int neighborIndex = plainIndex(neighborRow - 1, neighborCol - 1);
            int siteIndex = plainIndex(row - 1, col - 1);
            //if neighbor is open and not yet connected to current site, connect both
            if(isOpen(neighborRow, neighborCol) && !wUnionUF.connected(siteIndex, neighborIndex)){
                //connect row-col with i-j
                wUnionUF.union(siteIndex, neighborIndex);
            }
        }
    }

    /**
     * Check if current neighbor is valid
     * @param row
     * @param col
     * @return
     */
    private boolean isValidNeighbor(int row, int col) {
        boolean valid = true;
        try{
            checkIndices(row, col);
        }
        catch(IndexOutOfBoundsException ex){
            valid = false;
        }
        return valid;
    }

    /**
     * Convert input grid coordinates to corresponding plain index in the UF array
     * @param i
     * @param j
     * @return
     */
    private int plainIndex(int i, int j){
        return i * N + j;
    }

    /**
     * Check correctness of site indices
     * @param row
     * @param col
     */
    private void checkIndices(int row, int col) {
        StringBuilder errorMessage = new StringBuilder("");
        if(row <= 0 || row > N){
            errorMessage.append(String.format("Row should be between 1 and N. Current value %d%n", row));
        }
        if(col <= 0 || col > N) {
            errorMessage.append(String.format("Column should be between 1 and N. Current value %d", col));
        }
        if(errorMessage.length()>0){
            throw new IndexOutOfBoundsException(errorMessage.toString());
        }
    }
}
