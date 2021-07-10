package ir.ac.kntu.solution;

import ir.ac.kntu.util.Timer;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Matin Ghanabri
 */
public class Solution {
    @Test
    public void timerTest0() {
        Timer timer = new Timer(0, 0, 0);
        for (int i = 0; i < 100; i++) {
            timer.next();
        }
        assertEquals(timer.getValue(), 100);
        assertEquals(timer.toString(), "00:01:40");
    }

    @Test
    public void timerTest1() {
        Timer timer = new Timer(0, 0, 0);
        for (int i = 0; i < 3800; i++) {
            timer.next();
        }
        assertEquals(timer.getValue(), 3800);
        assertEquals(timer.toString(), "01:03:20");
    }
}
