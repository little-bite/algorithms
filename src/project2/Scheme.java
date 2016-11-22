package project2;

import java.util.HashSet;

/**
 * The interface for both schemes
 * Created by qichen on 10/8/16.
 */
abstract class Scheme {
    public HashSet<TwoSet> result = new HashSet<>();
    Rectangle[] samples;

    protected void reportPair(int leftBelongTo, int rightBelongTo) {
        this.result.add(new TwoSet(leftBelongTo, rightBelongTo));
    }

}
