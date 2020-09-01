package algorithms;

import java.util.Random;

public class LeafSpine {
    public long[] capSource, capDestination;
    public long[] spinToLeaf, leafToSpin;

    static Random rnd = new Random(0);

    public int n,t,k;

    public boolean[][] canSend;

    public long[] capacityGap;

    public LeafSpine(int n, int t, int k, int sumCapSource, int sumCapDestination) {
        this.n = n;
        this.t = t;
        this.k = k;

        canSend = new boolean[n*t][n*t];
        capSource = new long[n*t];
        capDestination = new long[n*t];
        capacityGap = new long[n*t];
        spinToLeaf = new long[t];
        leafToSpin = new long[t];

        for (int i = 0; i < sumCapSource / 5; i++) {
            capSource[rnd.nextInt(n*t)] += 5;
        }

        for (int i = 0; i < sumCapDestination / 5; i++) {
            capDestination[rnd.nextInt(n*t)] += 5;
        }


        for (int i = 0; i  < t*n; i++) {
            for (int id = 0; id < 2; id++) {
                int prev = rnd.nextInt(t * n);
                while (canSend[prev][i] || prev == i) {
                    prev = rnd.nextInt(t * n);
                }
                canSend[prev][i] = true;
            }
        }


        update_capacity_gap();
    }

    public LeafSpine(int n, int t, int k, long[] capIn, long[] capOut, boolean[][] canSnd) {
        this.n = n;
        this.t = t;
        this.k = k;

        canSend = new boolean[n*t][n*t];
        capSource = capIn.clone();
        capDestination = capOut.clone();
        capacityGap = new long[n*t];
        spinToLeaf = new long[t];
        leafToSpin = new long[t];

        for (int i = 0; i < n*t; i++) {
            canSend[i] = canSnd[i].clone();
        }

        update_capacity_gap();
    }

    private void update_capacity_gap() {
        for (int i = 0; i < t; i++) {
            for (int j = 0; j < n; j++) {
                leafToSpin[i] += capSource[i * n + j];
                spinToLeaf[i] += capDestination[i * n + j];
            }
            leafToSpin[i] = (leafToSpin[i] + k - 1) / k;
            spinToLeaf[i] = (spinToLeaf[i] + k - 1) / k + (t - 2) - (t - 2) / k;
        }

        for (int i = 0; i < t*n; i++) {
            capacityGap[i] = - capDestination[i];
        }

        for (int i = 0; i < t*n; i++) {
            for (int j = 0; j < t*n; j++) {
                if (canSend[i][j]) {
                    capacityGap[j] += capSource[i];
                }
            }
        }

        verify_consistency();
    }

    private void verify_consistency() {
        for (int i = 0; i < n*t; i++) {
            long tt = -capDestination[i];
            for (int j = 0; j < n*t; j++) {
                if (canSend[j][i]) {
                    tt += capSource[j];
                }
            }
            if (tt != capacityGap[i]) {
                throw new AssertionError();
            }
        }
    }

    public LeafSpine(LeafSpine topo) {
        this.n = topo.n;
        this.t = topo.t;
        this.k = topo.k;

        canSend = new boolean[n*t][n*t];
        for (int i = 0; i < n*t; i++) {
            canSend[i] = topo.canSend[i].clone();
        }
        capSource = topo.capSource.clone();
        capDestination = topo.capDestination.clone();
        capacityGap = topo.capacityGap.clone();
        spinToLeaf = topo.spinToLeaf.clone();
        leafToSpin = topo.leafToSpin.clone();
        verify_consistency();
    }



    public int get_no_of_removed_queues() {
        int ans = 0;
        for (int i = 0; i < n*t; i++) {
            if (capacityGap[i] <= 0) {
                ans++;
            }
        }
        return ans;
    }

    public long total_cap() {
        long ans = 0;
        for (int i = 0; i < n*t; i++) {
            if (capacityGap[i] > 0) {
                ans += capDestination[i];
            }
        }
        return ans;
    }


    private long getNewCapacitySpinToLeafLink(int leaf) {
        long sum = 0;
        for (int i = 0; i < n; i++) {
            if (capacityGap[leaf * n + i] > 0) {
                sum += capDestination[leaf * n + i];
            }
        }
        for (int v = 0; v < n * t; v++) {
            if (v / n == leaf) {
                continue;
            }
            for (int i = 0; i < n; i++) {
                if (canSend[v][leaf*n + i] && capacityGap[leaf * n + i] <= 0) {
                    sum += capSource[v];
                    break;
                }
            }
        }
        return Math.max((sum + k-1) / k + (t - 2) - (t - 2) / k, spinToLeaf[leaf]);

    }

    public long calculateRemove(int pos) {
        long delta = capacityGap[pos];
        capDestination[pos] += delta;
        capacityGap[pos] = 0;
        long deltaCore = getNewCapacitySpinToLeafLink(pos / n) - spinToLeaf[pos / n];
        capDestination[pos] -= delta;
        capacityGap[pos] = delta;
        return delta + deltaCore*k;
    }

    public void increaseCapacity(int toRemove) {
        capDestination[toRemove] += capacityGap[toRemove];
        capacityGap[toRemove] = 0;
        spinToLeaf[toRemove / n] = getNewCapacitySpinToLeafLink(toRemove / n);
    }

    public void reduceSource(int toRemoveSource, long val) {
        long sum  = 0;
        for (int i = 0; i < n; i++) {
            sum += capSource[toRemoveSource / n * n + i];
        }
        capSource[toRemoveSource] -= val;
        sum -= val;
        leafToSpin[toRemoveSource / n] = (sum + k - 1) / k;
        for (int i = 0; i < n*t; i++) {
            if (canSend[toRemoveSource][i]) {
                capacityGap[i] -= val;
            }
        }
        verify_consistency();
    }

    public long makeSQplus() {
        verify_consistency();
        long[] oldCapacityGap = capacityGap.clone();
        long[] oldCapacityDestination = capDestination.clone();
        long plus = 0;
        for (int i = 0; i < n*t; i++) {
            if (capacityGap[i] > 0) {
                plus += capacityGap[i];
                capDestination[i] += capacityGap[i];
                capacityGap[i] = 0;
            }
        }
        verify_consistency();
        for (int i = 0; i < t; i++) {
            plus += k*(getNewCapacitySpinToLeafLink(i) - spinToLeaf[i]);
        }
        capacityGap = oldCapacityGap;
        capDestination = oldCapacityDestination;
        return plus;
    }
}
