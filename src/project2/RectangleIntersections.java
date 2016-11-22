package project2;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;

/**
 * CSCI 6212 project 2
 * The divide and conquer scheme
 * Created by qichen on 10/7/16.
 */
class RectangleIntersections extends Scheme{


    RectangleIntersections(Rectangle[] samples) {
        this.samples = samples;
    }
    void report(){
        VerticalEdge[] v = new VerticalEdge[samples.length * 2];
        VerticalProjection[] h = new VerticalProjection[samples.length];
        // initialize the v and h array
        for( int i = 0; i < samples.length; i++){
            v[2*i] = new VerticalEdge(samples[i].id, samples[i].left, true);
            v[2*i + 1] = new VerticalEdge(samples[i].id, samples[i].right, false);
            h[i] = new VerticalProjection(samples[i].id,samples[i].top, samples[i].bottom);
        }

        // Arrays.sort() can sort the v and h array in O(nlogn)
        Arrays.sort(v, Comparator.comparing(VerticalEdge::getPosition));
        Arrays.sort(h, Comparator.comparing(VerticalProjection::getBottom));

        detect(v, h, 2 * samples.length);

    }

    // do the recursion here
    private void detect(VerticalEdge[] v, VerticalProjection[] h, int m) {
        if (m < 2) {
            return;
        }

        float leftBound = v[0].getPosition();
        float rightBound = v[v.length  - 1].getPosition();

        //HashMap<Integer, Integer> fromRectIDtoProjectionIndex = new HashMap<>();
        HashSet<Integer> h1belongTo = new HashSet<>();
        HashSet<Integer> h2belongTo = new HashSet<>();
        HashSet<Integer> s11 = new HashSet<>();
        HashSet<Integer> s22 = new HashSet<>();
        HashSet<Integer> s12 = new HashSet<>();
        HashSet<Integer> s21 = new HashSet<>();

        VerticalEdge[] v1 = new VerticalEdge[m/2];
        VerticalEdge[] v2 = new VerticalEdge[m - m/2];
        VerticalProjection[] h1, h2, h11, h12, h21, h22;

        for(int i = 0; i < m/2; i++){
            // use linear time to create sorted v1 and v2 and the hash set s11,s12,s22,s21
            v1[i] = v[i];
            if (!v[i].isLeft){
                // is right edge then must be in s11
                s11.add(v[i].belongTo);
            } else{
                // if it's left edge but the corresponding right edge is out of the
                // right bound of v, the rectangle spans v2, so it's put in s12
                if (samples[v[i].belongTo].right > rightBound) {
                    s12.add(v[i].belongTo);
                }
            }
            // put the corresponding h's belongTo in a hash set
            h1belongTo.add(v[i].belongTo);
        }

        for(int i = m/2; i < m; i++){
            // use linear time to create sorted v1 and v2
            v2[i - m/2] = v[i];
            if (v[i].isLeft){
                // is left edge then must be in s22
                s22.add(v[i].belongTo);
            } else{
                // if it's right edge but the corresponding left edge is out of the
                // left bound of v, the rectangle spans v1, so it's put in s21
                if (samples[v[i].belongTo].left < leftBound) {
                    s21.add(v[i].belongTo);
                }
            }
            // put the corresponding h's belongTo in a hash set
            h2belongTo.add(v[i].belongTo);
        }

        // build the h1 and h2 array in linear time
        int h1Pointer = 0;
        int h2Pointer = 0;
        h1 = new VerticalProjection[h1belongTo.size()];
        h2 = new VerticalProjection[h2belongTo.size()];
        for (VerticalProjection vp : h) {
            if (h1belongTo.contains(vp.belongTo)) {
                h1[h1Pointer] = vp;
                h1Pointer++;
            }
            /** CAUTION
             * it is not "else if" here
             * **/
            if (h2belongTo.contains(vp.belongTo)) {
                h2[h2Pointer] = vp;
                h2Pointer++;
            }
        }

            h11 = new VerticalProjection[s11.size()];
            h12 = new VerticalProjection[s12.size()];
            h21 = new VerticalProjection[s21.size()];
            h22 = new VerticalProjection[s22.size()];

            int h11Pointer = 0;
            int h12Pointer = 0;
            int h21Pointer = 0;
            int h22Pointer = 0;
            // build the h12,h21,h11,h22 array in linear time
            for (VerticalProjection vp : h) {
                if (s11.contains(vp.belongTo)) {
                    h11[h11Pointer] = vp;
                    h11Pointer++;

                } else if (s12.contains(vp.belongTo)) {
                    h12[h12Pointer] = vp;
                    h12Pointer++;

                } else if (s21.contains(vp.belongTo)) {
                    h21[h21Pointer] = vp;
                    h21Pointer++;

                } else if (s22.contains(vp.belongTo)) {
                    h22[h22Pointer] = vp;
                    h22Pointer++;
                }

            }

            stab(h12, h22);
            stab(h11, h21);
            stab(h12, h21);

            detect(v1, h1, m / 2);
            detect(v2, h2, m - m / 2);
        }

    private void stab(VerticalProjection[] hRepresentLeft, VerticalProjection[] hRepresentRight) {
        int i = 0;
        int j = 0;
        int k;
        int leftLength = hRepresentLeft.length;
        int rightLength = hRepresentRight.length;
        while(i < leftLength && j < rightLength){
            if(hRepresentLeft[i].bottom < hRepresentRight[j].bottom){
                k = j;
                while (k < rightLength && hRepresentRight[k].bottom < hRepresentLeft[i].top){
                    reportPair(hRepresentLeft[i].belongTo, hRepresentRight[k].belongTo);
                    k++;
                }
                i++;
            } else {
                k = i;
                while (k < leftLength && hRepresentLeft[k].bottom < hRepresentRight[j].top){
                    reportPair(hRepresentLeft[k].belongTo, hRepresentRight[j].belongTo);
                    k++;
                }
                j++;
            }
        }
    }

    private class VerticalEdge{
        int belongTo;
        float position;
        boolean isLeft;

        VerticalEdge(int id, float x, boolean isTheLeftEdge) {
            belongTo = id;
            position = x;
            isLeft = isTheLeftEdge;
        }

        float getPosition(){
            return position;
        }
    }

    private class VerticalProjection{
        int belongTo;
        float top, bottom;

        VerticalProjection(int id, float t, float b) {
            belongTo = id;
            top = t;
            bottom = b;
        }

        float getBottom() {
            return bottom;
        }
    }
}
