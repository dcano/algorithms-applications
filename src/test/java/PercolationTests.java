import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by David on 03/01/2017.
 */

public class PercolationTests {

    private static final int SIZE = 4;
    private Percolation percolation;


    @Before
    public void initialize(){
        percolation = new Percolation(SIZE);
    }

    @Test
    public void invalid_indices_when_opening(){
        int numExceptions = 0;
        int expectedExceptions = 4;
        try{
            percolation.open(0, 0);
        }
        catch(IndexOutOfBoundsException e){
            numExceptions++;
        }
        try{
            percolation.open(0, SIZE+1);
        }
        catch(IndexOutOfBoundsException e){
            numExceptions++;
        }
        try{
            percolation.open(SIZE+1, 0);
        }
        catch(IndexOutOfBoundsException e){
            numExceptions++;
        }
        try{
            percolation.open(SIZE+1, SIZE+1);
        }
        catch(IndexOutOfBoundsException e){
            numExceptions++;
        }
        assertThat(numExceptions, is(equalTo(expectedExceptions)));
    }

    @Test
    public void invalid_indices_at_isOpen(){
        int numExceptions = 0;
        int expectedExceptions = 4;
        try{
            percolation.isOpen(0, 0);
        }
        catch(IndexOutOfBoundsException e){
            numExceptions++;
        }
        try{
            percolation.isOpen(0, SIZE+1);
        }
        catch(IndexOutOfBoundsException e){
            numExceptions++;
        }
        try{
            percolation.isOpen(SIZE+1, 0);
        }
        catch(IndexOutOfBoundsException e){
            numExceptions++;
        }
        try{
            percolation.isOpen(SIZE+1, SIZE+1);
        }
        catch(IndexOutOfBoundsException e){
            numExceptions++;
        }
        assertThat(numExceptions, is(equalTo(expectedExceptions)));
    }

    @Test
    public void invalid_indices_at_isFull(){
        int numExceptions = 0;
        int expectedExceptions = 4;
        try{
            percolation.isFull(0, 0);
        }
        catch(IndexOutOfBoundsException e){
            numExceptions++;
        }
        try{
            percolation.isFull(0, SIZE+1);
        }
        catch(IndexOutOfBoundsException e){
            numExceptions++;
        }
        try{
            percolation.isFull(SIZE+1, 0);
        }
        catch(IndexOutOfBoundsException e){
            numExceptions++;
        }
        try{
            percolation.isFull(SIZE+1, SIZE+1);
        }
        catch(IndexOutOfBoundsException e){
            numExceptions++;
        }
        assertThat(numExceptions, is(equalTo(expectedExceptions)));
    }

    @Test
    public void system_percolates(){
        percolation.open(1,1);
        percolation.open(1,2);
        percolation.open(2,2);
        percolation.open(3,2);
        percolation.open(3,3);
        percolation.open(4,1);
        percolation.open(4,3);
        assertThat("System percolates", percolation.percolates(), is(equalTo(true)));
    }

    @Test
    public void system_not_percolates(){
        percolation.open(1,1);
        percolation.open(1,4);
        percolation.open(2,2);
        percolation.open(3,2);
        percolation.open(3,3);
        percolation.open(4,1);
        percolation.open(4,2);
        assertThat("System does not percolate", percolation.percolates(), is(equalTo(false)));

    }

}
