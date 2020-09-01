package algorithms;

import algorithms.LeafSpine;

public class FRSQ {
    public long run(LeafSpine topo, long max_cost) {
       while (max_cost > 0){
            int opt_pos = -1;
            int max_red = 0;
            for (int i = 0; i < topo.t * topo.n; i++) {
                if (topo.capSource[i] == 0) {
                    continue;
                }
                int curred = 0;
                for (int j = 0; j < topo.t * topo.n; j++) {
                    curred += (topo.canSend[i][j] && topo.capacityGap[j] > 0) ? 1 : 0;
                }
                if (curred > max_red) {
                    max_red = curred;
                    opt_pos = i;
                }
            }

            if (opt_pos == -1) {
                 break;
            }
            long to_reduce = topo.capSource[opt_pos];
            for (int i = 0; i < topo.t * topo.n; i++) {
                if (topo.canSend[opt_pos][i] && topo.capacityGap[i] > 0) {
                    to_reduce = Math.min(to_reduce, topo.capacityGap[i]);
                }
            }

            topo.reduceSource(opt_pos, to_reduce);
            max_cost -= to_reduce;
        }
        return topo.makeSQplus();
    }

}
