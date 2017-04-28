# Overview

*Partial Evaluator of Simple Transition Systems* (PESTS) is a tool suite for the quotienting of finite transition systems.

# Usage

The partial evaluation procedure is provided the method quotientA() of class it.unige.parteval.Projection.
To use the method one must provide four inputs:
- P: a DFA representing the specification 
- A: the DFA of the agent to be evaluated
- SigmaB: the set of actions to appear in the new specification
- Gamma: the set of synchronization actions

# Running Tests

Several JUnit tests are available under /it/unige/parteval/tests.

For instance, running [PaperExample.java](https://github.com/SCPTeam/pests/blob/master/src/it/unige/parteval/Test/PaperExample.java) correstond to executing the partial evaluation of the specification

![Specification](https://github.com/SCPTeam/pests/blob/master/fig/P.png)

against the agent

![Agent](https://github.com/SCPTeam/pests/blob/master/fig/A.png)

which results in the quotient specification

![Quotient](https://github.com/SCPTeam/pests/blob/master/fig/PA.png)

# FlexFact Use Case

[FlexFact](http://www.rt.eei.uni-erlangen.de/FGdes/productionline.html) is a simulator for a flexible manufacturing plant.
A plant is composed by interconnecting some modules.
Modules are chosen among a finite set including, convoy belts, rotatory tables and processing machines.

Packege [flexfact](https://github.com/SCPTeam/pests/tree/master/src/it/unige/parteval/test/flexfact) contains the encoding of the FlexFact modules that can be used to build a model of a plant.

The class [FlexFactPlantTest](https://github.com/SCPTeam/pests/tree/master/src/it/unige/parteval/test/flexfact/FlexFactPlantTest.java) contains a test where a specification and a simple plant are used to synthesize a controller.
