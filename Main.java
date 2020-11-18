import algorithms.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

public class Main {

    static PrintWriter writer;

    static void printTable(double[] dx, double[][] result, String name, String colName) {
        writer.println(name);
        for (int i = 0; i < dx.length; i++) {
            writer.printf(colName+" = %.2f%%", dx[i]);
            for (int j = 0; j < result.length; j++) {
                writer.printf(j == 0 ? ": %.2f":", %.2f", result[j][i]);
            }
            writer.println();
        }
        writer.println();
    }


    static void run_increment_experiments() {

        double[] percents = new double[13];
        for (int i = 0; i < percents.length; i++) {
            percents[i] = i*0.5;
        }

        double[][] tableQueue = new double[6][percents.length];
        double[][] tableSum = new double[6][percents.length];

        for (int cout = 15000; cout <= 20000; cout += 1000) {
            long[] tot_queue = new long[percents.length];
            long[] tot_sum = new long[percents.length];
            for (int i = 0; i < 100; i++) {
                LeafSpine topo = new LeafSpine(16, 9, 4, 10000, cout);
                long[] incr = new long[percents.length];
                for (int j = 0; j < incr.length; j++) {
                    incr[j] = (long)(0.01*percents[j]*topo.initCap);
                }
                long[][] ress = new GA().run(topo, incr);



                for (int j = 0; j < incr.length; j++) {
                    tot_queue[j] += ress[0][j];
                    tot_sum[j] += ress[1][j];
                }
            }
            for (int i = 0; i < percents.length; i++) {
                tableQueue[(cout - 15000)/1000][i] = 0.01*tot_queue[i];
                tableSum[(cout - 15000)/1000][i] = 0.01*tot_sum[i]/cout;
            }
        }


        for (int i= 0; i < tableQueue.length; i++) {
            for (int j = 0; j < tableQueue[0].length; j++) {
                tableQueue[i][j] = tableQueue[i][j]/144.0*100.0;
                tableSum[i][j] = tableSum[i][j]*100.0;
            }
        }
        printTable(percents, tableQueue, "The value of Q(S) for S representing networks produced by GA(G, C) (Fig.9a).\n" +
                "cout: 15000, 16000, 17000, 18000, 19000, 20000", "C");
        printTable(percents, tableSum, "The value of B(S) for S representing networks produced by GA(G, C) (Fig.9b).\ncout: 15000, 16000, 17000, 18000, 19000, 20000", "C");

    }


    static void run_decrement_experiments() {

        double[] percents = new double[11];
        for (int i = 0; i < percents.length; i++) {
            percents[i] = i;
        }

        long[] decr = new long[11];
        for (int i = 0; i < decr.length; i++) {
            decr[i] = 100 * i;
        }

        double[][] tableQueue = new double[6][decr.length];
        double[][] tableSum = new double[6][decr.length];

        for (int cout = 15000; cout <= 20000; cout += 1000) {
            long[] tot_queue = new long[decr.length];
            long[] tot_sum = new long[decr.length];
            for (int i = 0; i < 100; i++) {
                LeafSpine topo = new LeafSpine(16, 9, 4, 10000, cout);
                long[][] ress = new GR().run(topo, decr);
                for (int j = 0; j < decr.length; j++) {
                    tot_queue[j] += ress[0][j];
                    tot_sum[j] += ress[1][j];
                }
            }
            for (int i = 0; i < decr.length; i++) {
                tableQueue[(cout - 15000)/1000][i] = 0.01*tot_queue[i];
                tableSum[(cout - 15000)/1000][i] = 0.01*tot_sum[i]/cout;
            }
        }


        for (int i= 0; i < tableQueue.length; i++) {
            for (int j = 0; j < tableQueue[0].length; j++) {
                tableQueue[i][j] = tableQueue[i][j]/144.0*100.0;
                tableSum[i][j] = tableSum[i][j]*100.0;
            }
        }

        printTable(percents, tableQueue, "The value of Q(S) for S representing networks produced by GR(G, C) (Fig.9c).\ncout: 15000, 16000, 17000, 18000, 19000, 20000", "R");
        printTable(percents, tableSum, "The value of B(S) for S representing networks produced by GR(G, C) (Fig.9d).\ncout: 15000, 16000, 17000, 18000, 19000, 20000", "R");

    }


    static void run_increment_decrement_experiments() {
        double[] percents = new double[13];
        for (int i = 0; i < percents.length; i++) {
            percents[i] = i*0.5;
        }



        double[][] tableRed= new double[6][percents.length];

        for (int cout = 15000; cout <= 20000; cout += 1000) {
            long[] tot_red = new long[percents.length];
            for (int i = 0; i < 100; i++) {
                LeafSpine topo = new LeafSpine(16, 9, 4, 10000, cout);
                long[] incr = new long[percents.length];
                for (int j = 0; j < incr.length; j++) {
                    incr[j] = (long)(0.01*percents[j]*topo.initCap);
                }
                long[] ress = new MRSQ().run(topo, incr);
                for (int j = 0; j < incr.length; j++) {
                    tot_red[j] += ress[j];
                }
            }
            for (int i = 0; i < percents.length; i++) {
                tableRed[(cout - 15000)/1000][i] = 0.01*tot_red[i];
            }
        }

        for (int i = 0; i < tableRed.length; i++) {
            for (int j = 0; j < tableRed[0].length; j++) {
                tableRed[i][j] /= 100;
            }
        }

        printTable(percents, tableRed, "The value of R returned by MRSQ(G, C) (Fig.9e).\ncout: 15000, 16000, 17000, 18000, 19000, 20000", "C");

    }


    private static LeafSpine read_leaf_spine(String arg) throws FileNotFoundException {
        Scanner in = new Scanner(new File(arg));
        int k = in.nextInt(), t = in.nextInt(), n = in.nextInt();
        long[] capIn = new long[n*t];
        long[] capOut = new long[n*t];
        for (int i = 0; i < t*n; i++) {
            capIn[i] = in.nextInt();
        }
        for (int i = 0; i < t*n; i++) {
            capOut[i] = in.nextInt();
        }
        boolean[][] arr = new boolean[n*t][n*t];
        while (in.hasNext()) {
            int u = in.nextInt()-1;
            int v = in.nextInt()-1;
            arr[u][v] = true;
        }
        LeafSpine spine = new LeafSpine(n,t,k, capIn, capOut, arr);
        in.close();
        return spine;
    }

    public static void main(String[] args) throws FileNotFoundException {
        if (args.length == 1 && args[0].equals("run_all_experiments")) {
            writer = new PrintWriter("result_all.txt");
            Locale.setDefault(Locale.US);
            run_increment_experiments();
            run_decrement_experiments();
            run_increment_decrement_experiments();
            writer.close();
            return;
        }
        if (args.length != 3 || !Arrays.asList("GA", "GR", "FRSQ", "MRSQ").contains(args[0])) {
            System.out.println("Bad arguments");
            System.out.println();
            return;
        }

        LeafSpine topo = read_leaf_spine(args[1]);
        if (args[0].equals("GA")) {
            new GA().run(topo, new long[]{Long.parseLong(args[2])});
            print_topo(topo, 0);
            return;
        }

        if (args[0].equals("GR")) {
            new GR().run(topo, new long[]{Long.parseLong(args[2])});
            print_topo(topo, 0);
            return;
        }

        if (args[0].equals("FRSQ")) {
            new FRSQ().run(topo, Long.parseLong(args[2]));
            print_topo(topo, 1);
            return;
        }

        if (args[0].equals("MRSQ")) {
            new MRSQ().run(topo, new long[]{Long.parseLong(args[2])});
            print_topo(topo, 1);
        }

    }

    private static void print_topo(LeafSpine topo, int switch_type) throws FileNotFoundException {
        writer = new PrintWriter("result.txt");
        writer.println("The network with modified link capacities");
        writer.print("Source link capacities:");
        for (int i = 0; i < topo.t* topo.n; i++) {
            writer.print(" "+topo.capSource[i]);
        }
        writer.println();
        writer.print("Destination link capacities:");
        for (int i = 0; i < topo.t* topo.n; i++) {
            writer.print(" "+topo.capDestination[i]);
        }
        writer.println();
        writer.print("Leaf-to-spine link capacities:");
        for (int i = 0; i < topo.t; i++) {
            writer.print(" "+topo.leafToSpin[i]);
        }
        writer.println();
        writer.print("Spine-to-leaf link capacities:");
        for (int i = 0; i < topo.t; i++) {
            writer.print(" "+topo.spinToLeaf[i]);
        }
        writer.println();
        if (switch_type == 0) {
            writer.println("Representing virtual CIOQ switch");
            writer.print("Input queues:");
            for (int i = 0; i < topo.t * topo.n; i++) {
                int sum = 0;
                for (int j = 0; j < topo.t * topo.n; j++) {
                    if (topo.canSend[i][j]) {
                        sum += topo.capDestination[j];
                    }
                }
                writer.print(" " + Math.min(sum, topo.capSource[i]));
            }
            writer.println();
            writer.print("Output queues:");
            for (int i = 0; i < topo.t * topo.n; i++) {
                writer.print(" " + (topo.capacityGap[i] > 0 ? topo.capDestination[i] : -1));
            }
        } else {
            writer.println("Representing virtual SQ switch");
            writer.print("Queues:");
            for (int i = 0; i < topo.t * topo.n; i++) {
                if (topo.capacityGap[i] > 0 ) {
                    topo.increaseCapacity(i);
                }
            }
            for (int i = 0; i < topo.t * topo.n; i++) {
                int sum = 0;
                for (int j = 0; j < topo.t * topo.n; j++) {
                    if (topo.canSend[i][j]) {
                        sum += topo.capDestination[j];
                    }
                }
                writer.print(" " + Math.min(sum, topo.capSource[i]));
            }
        }
        writer.close();

    }

}
