package algorithms;

import algorithms.FRSQ;
import algorithms.LeafSpine;

public class MRSQ {
    public long[] run(LeafSpine topo, long[] costs) {
        long[] ans = new long[costs.length];

        for (int i = 0; i < costs.length; i++) {

            int l = -1;
            int r = 30000;
            while (l < r-1) {
                int x = (l+r) / 2;
                if (new FRSQ().run(new LeafSpine(topo), x) > costs[i]) {
                    l = x;
                } else {
                    r = x;
                }
            }
            ans[i] = r;

        }

        new FRSQ().run(topo, ans[costs.length-1]);
        return ans;
    }

}
