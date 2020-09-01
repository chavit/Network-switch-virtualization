# Abstracting Networks with Measurable Guarantees
This repository contains a code reproducing the experimental study in the paper "Abstracting Networks with Measurable Guarantees".

## Compile: 

`javac Main.java` 

## Run:

`java Main run_all_experiments` - run all experiments on randomly generated networks. This command prints into the file `result_all.txt` all data points generating Figure 9.  

We have added `result_all.txt` to the repository.

The following four commands allow to run GA, GR, FRSQ, MRSQ on specific networks:

`java Main GA "input_file" "value_of_C"`

`java Main GR "input_file" "value_of_R"`

`java Main FRSQ "input_file" "value_of_R"`

`java Main MRSQ "input_file" "value_of_C"` 

"input_file" - contains a description of a three-layered leaf-spine topology in the following format: 
the first line contains the number of spines, the number of leafs, and the number of servers connected with a single leaf;
the second line contains source link capacities; the third line contains destination link capacities; 
each of the next lines contains two number `u` and `v` meaning that server `u` can send packets to server `v`. (see `sample_input.txt` for example).

Each of these four commands prints the resulting network and representing switch into the file `result.txt`. 
The instance of  `result.txt` in the repository was obtained by the following command:
`java Main GA sample_input.txt 1000`.
