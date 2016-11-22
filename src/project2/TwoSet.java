package project2;

/**
 * use complex number to represent a pair
 * Created by qichen on 10/8/16.
 */
public class TwoSet {
    private int a, b;
    TwoSet(int r, int i){
        if(r<i) {
            a = r;
            b = i;
        } else {
            b = r;
            a = i;
        }
    }

    // only if hash code is same, equals() can be invoked
    //super.hashCode() is related to toString(), every String is an unique instance,
    // so if not overrode two instances will never match,
    // because equals() won't be called
    @Override
    public int hashCode() {
        return a * 31 + b;
    }

    @Override
    public boolean equals(Object obj) {
        if (! (obj instanceof TwoSet)) {
            return false;
        }
        TwoSet q = (TwoSet) obj;
        return (q.a == this.a && q.b ==this.b)
                || (q.a == this.b && q.b ==this.a) ;
    }

    @Override
    public String toString() {
        return a + " and " + b + ".";
        //return "Rectangles " + a + " and " + b + " intersect.";
    }
}
