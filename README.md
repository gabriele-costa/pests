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
