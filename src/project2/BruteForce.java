package project2;

/**
 * The brute-force scheme for rectangle intersection problem
 * Created by qichen on 10/7/16.
 */
public class BruteForce extends Scheme{

    public BruteForce(Rectangle[] samples) {
        this.samples = samples;
    }
    public void search() {
        for(int i = 0; i < samples.length - 1; i++){
            for (int j = i + 1; j< samples.length; j++){
                check(samples[i], samples[j]);
            }
        }
    }

    private void check(Rectangle rect1, Rectangle rect2) {
        // the overlap condition
        // see the three pics at http://articles.leetcode.com/determine-if-two-rectangles-overlap/
        if (rect1.bottom < rect2.top &&
                rect1.top > rect2.bottom &&
                rect1.right > rect2.left && rect1.left < rect2.right ) {
            reportPair(rect1.id, rect2.id);
        }
    }
}
