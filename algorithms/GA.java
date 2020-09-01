package algorithms;

import algorithms.LeafSpine;

public class GA {
    public long[][] run(LeafSpine topo, long[] costs) {
        long[] ans_queue = new long[costs.length];
        long[] ans_cap = new long[costs.length];

        int id = 0;
        ans_queue[0] = topo.get_no_of_removed_queues();
        ans_cap[0] = topo.total_cap();

        int totalUsed = 0;
        while (true) {
            long bestCost = Long.MAX_VALUE;
            int toRemove = -1;
            for (int i = 0; i < topo.t*topo.n; i++) {
                if (topo.capacityGap[i] <= 0) {
                    continue;
                }
                long candCost = topo.calculateRemove(i);
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

            totalUsed += bestCost;
            topo.increaseCapacity(toRemove);

            while (id < costs.length && totalUsed > costs[id]) {
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
