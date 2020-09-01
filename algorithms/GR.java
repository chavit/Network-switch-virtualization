package algorithms;

import algorithms.LeafSpine;

import java.util.Arrays;

public class GR {
    public long[][] run(LeafSpine topo, long[] costs) {
        boolean[] canRemove = new boolean[topo.t * topo.n];

        long[] ans_queue = new long[costs.length];
        long[] ans_cap = new long[costs.length];

        int id = 0;
        ans_queue[0] = topo.get_no_of_removed_queues();
        ans_cap[0] = topo.total_cap();


        long totalRemoved = 0;
        Arrays.fill(canRemove, true);
        while (true) {

            long bestCost = Long.MAX_VALUE;
            int toRemove = -1;

            for (int i = 0; i < topo.t*topo.n; i++) {
                if (topo.capacityGap[i] <= 0 || !canRemove[i]) {
                    continue;
                }
                long candCost = topo.capacityGap[i];
                if (candCost < bestCost) {
                    bestCost = candCost;
                    toRemove = i;
                }
            }


            if (toRemove == -1) {
                for (int i = id; i < costs.length; i++) {
                    ans_queue[i] = topo.get_no_of_removed_queues();
                    ans_cap[i] = topo.get_no_of_removed_queues();
                }
                break;
            }

            int toRemoveMax = 0;
            int toRemoveSource = -1;

            for (int i = 0; i < topo.t * topo.n; i++) {
                if (topo.capSource[i] >0 && topo.canSend[i][toRemove]) {
                    int cost = 0;
                    for (int j = 0; j < topo.t * topo.n; j++) {
                        if (topo.canSend[i][j] && topo.capacityGap[j] > 0) {
                            cost++;
                        }
                    }
                    if (cost > toRemoveMax) {
                        toRemoveMax = cost;
                        toRemoveSource = i;
                    }
                }
            }

            if (toRemoveSource == -1) {
                continue;
            }

            totalRemoved += Math.min(topo.capSource[toRemoveSource], topo.capacityGap[toRemove]);
            topo.reduceSource(toRemoveSource, Math.min(topo.capSource[toRemoveSource], topo.capacityGap[toRemove]));

            while (id < costs.length && totalRemoved > costs[id]) {
                id++;
            }

            if (id >= costs.length) {
                break;
            }

            ans_queue[id] = topo.get_no_of_removed_queues();
            ans_cap[id] = topo.total_cap();


        }

        return new long[][]{ans_queue, ans_cap};
    }

}
