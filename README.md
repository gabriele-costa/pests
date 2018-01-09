# PESTS

**PESTS** (*Partial Evaluator of Simple Transition Systems*) is a tool suite for the partial evaluation of finite state agents.
The tool implements the algorithm for quotienting finite-state automata introduced in the paper:

> *Gabriele Costa, David Basin, Chiara Bodei, Pierpaolo Degano, and Letterio Galletta*, `From Natural Projection to Partial Model Checking and Back’. TACAS 2018 (accepted).

Among the others, PESTS can be used to address the following problems: 
1. reducing the verification of a parallel composition to that of a single component;
2. synthesizing a submodule that respects a global specification: Submodule Construction Problem (SCP);
3. synthesizing a controller for a given component: Controller Synthesis Problem (CSP).

Given a specification S and an agent A (both defined through a deterministic finite state automaton), the tool returns (the most permissive specification of) a new agent A’ that, put in parallel with A, satisfies S.

## Version

This documentation refers to **PESTS version 1.1 (TACAS)**, available at [https://github.com/SCPTeam/pests/releases](https://github.com/SCPTeam/pests/releases).

**DOI**: [10.5281/zenodo.1135110](http://dx.doi.org/10.5281/zenodo.1135110)

## Requirements

- Oracle Java runtime environment (JRE) or Java development kit (JDK) version 8 or greater (http://www.oracle.com/technetwork/java/javase/downloads/index.html). 
PESTS has been also tested with the OpenJDK version 8 (http://openjdk.java.net/)

### TACAS virtual machine instructions

1. open a terminal and type `$ java -version` to check that a JRE is correctly installed and its version.
2. if no JRE is installed, install it through `$ sudo apt install default-jre`.

## Installation

PESTS is implemented in pure Java and requires no particular installation procedures.
It is provided as a self-contained Java archive (.jar) and it can be downloaded from [GitHub](https://github.com/SCPTeam/pests/releases).

**TACAS release**

The TACAS release consists of a .zip archive containing:
- `pests.jar`: the PESTS tool binary
- `test`: a folder containing the files necessary to replicate the experiments presented at TACAS '18
- `scp.sh` and `csp.sh`: shell scripts to replicate the experiments

## Description

The main operation performed by PESTS is the **partial evaluation** (aka *quotienting*) of a specification against a finite transition system A.
The result of the partial evaluation is a new specification that another transition system B must fulfil to ensure that the parallel composition of A and B fulfils the original specification.

The inputs that must be provided to the partial evaluation procedure are:
- P: the original specification
- A: the transition system
- S: the set of synchronous actions between A and B
- G: the set of asynchronous actions of B

## Usage

PESTS can be used both as a *stand-alone, command line tool* and a *library*. 

### PESTS as a stand-alone tool

Open a terminal and browse to the folder containing pests.jar.
Then type `$ java -jar pests.jar -h` to get the following help message.

``` 
$ java -jar pests.jar -h
Partial Evaluator of Simple Transition Systems (PESTS)
Usage: java -jar pests.jar INPUT [OPTIONS]

INPUT must be an existing file containing a textual description of a finite state agent

OPTIONS are a subset of the following:
-o=FILE	            write output on FILE. Writes on standard output if not specified and option -f is set to txt.
-s=FILE             read specification from FILE. Uses FALSE if not specified.
-a=v1,...,vN        use v1,...,vN as synchronous actions. Uses the empty set if not specified.
-b=v1,...,vN        use v1,...,vN as asynchronous actions. Uses the empty set if not specified.
-f=txt|svg|png|pdf  use the specified output format. Omitting this option is equivalent to -f=txt.
-v                  activate verbose output.
-h                  print this message and exit.
```

The INPUT file contains the transition system A, while the -s options is used to pass the (file containing the) specification P.
Options -a and -b permit one to define sets S and G, respectively. 
Actions must be valid identifiers, i.e., alpha-numeric sequences without spaces.

#### Input file format

The input file contains the specification of a finite transition system defined as a deterministic finite state automaton (DFA).
The file is structured in four parts:
- *Initial* -- specifies the name of the initial state
- *Finals* -- specifies the list of the names of final states
- *Actions* -- specifies the action labels
- *Transitions* -- specifies the labeled transitions of the DFA

**Input file examples**

The following example defines a 3-states DFA

```
    initial:q0;
    finals:q0,q1,q2; 
    actions:a,s,t;
    q0--t->q2;
    q0--a->q1;
    q1--s->q0;
```

corresponding to the following diagram (where all the states are final).
```
          t
   +-----------> q2
   |
   |      a             
-> q0 ---------> q1 
   ^             |
   |      s      |
   +-------------+
```

### PESTS as a library

As a Java library, pests.jar must be included in the build path of the Java compiler as usual, i.e., setting the `-cp` option or adding it with a IDE-specific procedure.
A simple and common usage scenario is the following
```
DFAutomatonImpl P = ... // create a specification P adding states and transitions
DFAutomatonImpl A = ... // create the transition system A (similar to creating P)
Set<String> S     = ... // Set of synchronous actions
Set<String> G     = ... // Set of asynchronous actions
			
DFAutomatonImpl PP = Projection.unify(Projection.partialA(P, A, G, S), S);
```

Here the most relevant classes and methods are:
- Class `it.unige.automata.impl.DFAutomatonImpl`: a concrete implementation of a DFA.
- Class `it.unige.automata.impl.NFAutomatonImpl`: a concrete implementation of a NFA.
- Class `it.unige.parteval.Projection`: utility class implementing the core algorithms.
- Method `partialA`: the implementation of the quotienting algorithm (Table 2 of the paper).
- Method `unify`: the implementation of the unification algorithm (Table 3 of the paper).

Javadoc describing the meaning and functionalities of the relevant classes are provided with the PESTS source code.

## How to run the TACAS paper experiments 

To replicate the experiments table presented in the paper follow the steps below.

1. Download **PESTS version 1.1 TACAS** ([pests-v1.1-tacas.zip](https://github.com/SCPTeam/pests/releases)).
2. Decompress the .zip archive to some folder (e.g., with `$ unzip pests-v1.1-tacas.zip`) 
3. Enter the unzipped folder and open a terminal there
4. Make sure that both `scp.sh` and `csp.sh` are executable (in case add the executable flag with `$ chmod +x scp.sh` and `$ chmod +x scp.sh`)
5. Run the shell script setting the number of experiments (between 1 and 20) to be executed. For example `./scp.sh 5` performs the first 5 experiments corresponding to the first 5 rows of the experiments table in the paper appendix (Table 4).

> **NOTE**: For each experiment, the output of the tool has following structure.
> ```
> ** SCP Test number 1 **
> Starting partial evaluation.
> Partial evaluation: Time=10 ms, States=24, Transitions=36
> Unification: Time=4 ms, States=9, Transitions=11
> Writing output.
> Completed.
> ```
> The output shown above refers to the first line of the SCP table (`** SCP Test number 1 **`).
> The total number of states and transitions processed by the partial evaluation algorithm is reported in the third line.
> These values must be compared with the second and third column of the table, respectively.
> Instead, the total execution time is given by the sum of the two phases (i.e. partial evaluation and unification).
> For instance, in the example the total time amounts to 14 ms.

> **WARNING**: Experiments above 15 may take up to some hours.

## Further tests and and examples

The package `it.unige.parteval.test` includes several JUnit tests and some case studies.
Moreover, the package `it.unige.parteval.test.flexfact` provides utility classes representing the components of the [Flexible manufacturing system](http://www.rt.techfak.fau.de/FGdes/index.html).
These components can be used to assemble virtual plants for partial evaluation purposes.