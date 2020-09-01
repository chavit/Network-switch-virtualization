# Abstracting Networks with Measurable Guarantees
This repository contains the code to reproduce the experimental evaluation in the paper "Abstracting Networks with Measurable Guarantees".

## Compile: 

`javac run_experiments.java` 

## Run:

`java run_experiments run_all_experiments` - run all experiments on randomly generated networks. This command prints into the file `result_all.txt` all data points generating Figure 9.  

We have added `result_all.txt` to the repository.

The following four commands allows to run GA, GR, FRSQ, MRSQ on specific networks:

`java run_experiments GA "input_file" "value_of_C"`

`java run_experiments GR "input_file" "value_of_R`

`java run_experiments FRSQ "input_file" "value_of_R`

`java run_experiments MRSQ "input_file" "value_of_C` 

"input_file" - contains a description of a three-layered leaf-spine topology in the following format: 
the first line contains the number of spines, the number of leafs, and the number of servers connected with a single leaf;
the second line contains source link capacities; the third line contains destination link capacities; 
each of the next lines contains two number `u` and `v` meaning that `u` can send packets to `v`. (see `sample_input.txt` for example).

Each of these four commands prints the resulting network and representing switch into the file `result.txt`. 
The instance of  `result.txt` in the repository was obtained by the following command:
`java run_experiments GA sample_input.txt 1000`.
