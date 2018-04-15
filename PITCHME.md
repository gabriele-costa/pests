## From natural projection to partial model checking and back

__**Gabriele Costa**__, Letterio Galletta (IMT Lucca)

David Basin (ETH Zurich)

Chiara Bodei, Pierpaolo Degano (University of Pisa)

---
### Outline

- Problem overview
- Partial model checking vs. natural projection
- A new quotienting algorithm and tool
- Conclusion and future work

---
### Composition (A|B)

Given two transition systems A and B, their (parallel) composition has
- a state for each pair of A's and B's states
- an initial/final state for each pair of initial/final states
- *synchronous* transitions on common actions
- *asynchronous* transitions on the other actions

**Common in system modeling and analysis**

---

### Decomposition (A\B)

Given two transition systems A and B find X such that X|B = A

- \ a.k.a. **quotienting** operator
- Less common but relevant for problems on interest
- **Example:** Sub-module construction (SCP) and controller synthesis (CSP)
- *X could not exist*

**Hard to solve in general**

---

### Example: Drone Package Delivery

Unmanned aerial vehicles (UAV) operating on a docking station

- **Adder A$_n$** repeatedly puts down $n$ items (action $d$) and sends a synchronous signal (action $s$). Eventually terminates with a synchronous action $t$

- **Remover B$_n$** repeatedly picks up $n$ items (action $u$). Sync like A

- **Docking station P$_n$** has a limited, $n$-elements stack

---

### Example: Drone Package Delivery

**A$_2$**

<img src="pitch/adder-w.png" width="50%" height="50%">

**B$_1$**

<img src="pitch/remover-w.png" width="50%" height="50%">

---

### Example: Drone Package Delivery

**A$_2$ | B$_1$**

<img src="pitch/product-w.png" width="70%" height="70%">

**P$_n$**

<img src="pitch/buffer-n.png" width="70%" height="70%">

---

### Example: Drone Package Delivery

- Does A$_2$ | B$_1$ fulfil P$_2$ (in symbols A$_2$ | B$_1$ $\models$ P$_n$)?
 - No (e.g., $d d u s d d$)

- Given A, can we modify B to fulfil P? (SCP)

- Is there an agent C (controller) that enforces P on A | B? (CSP)

---

### Natural projection [Wonham]
$$\pi : 2^{\Sigma_0} \times \Sigma_0^\ast \rightarrow \Sigma_0^\ast$$

- Removes from a trace $\eta \in \Sigma_0^\ast$ the symbols belonging to $\Sigma \subseteq \Sigma_0$
 - **Example:** `$\pi_{\{b,g,u\}}(bungabunga) = nana$`
- Can be extended to a language $\mathcal{L} \subseteq \Sigma_0^\ast$
 - **Example:** `$\pi_{\{b,g,u\}}(bunga^\ast) = na^\ast$`
- Can be extended to finite state automata
---

### Partial model checking [Andersen]
$$// : \Phi \times \mathbb{A} \rightarrow \Phi$$
- Given a ($\mu$-calculus) formula $\phi$ and a transition system A find $\phi' = \phi // A$ such that for all B

A|B $\models \phi$  iff B $\models \phi'$

- Several applications in the formal methods community (e.g. program refinement and monitoring)

---

### NP vs. PMC

| | NP | PMC |
|---|---|---|
| Agent | FSA | LTS |
| Specification | FSA | $\mu K$ |
| Complexity | EXPTIME* | EXPTIME |
| Tools | TCT, IDES3, DESTool | mCRL2, CADP, MuDiv |


*: PTIME for a specific class of FSA

---

### Gravitational waves

**Control theory** and **formal methods** are colliding [Ehlers et al.]

![galaxies](pitch/collision-label.png)

---

### A common framework (1)


- We redefine NP to work with LTS agents and $\mu K$ specifications
 - Finite LTS $\sim$ FSA (with all accepting states)
 - Any FSA `$X$` can be encoded as a `$\mu K$` formula `$\Phi_X$`
- A trace $\sigma$ is an alternation of state and action symbols
 - **Example:** $q_0 d q_1 d q_2$ (rather than $d d$) is a trace of A$_2$

---

### A common framework (2)

Given LTSs $A$ and $B$ with $\Gamma = \Sigma_A \cap \Sigma_B$, the **natural projection** on $A$ of a trace $\sigma$ is

- `$\pi_{A}(\langle s_A, s_B \rangle) = s_A$`
- `$\pi_{A}(\langle s_A, s_B \rangle a \langle s'_A, s'_B \rangle \cdot \sigma) = s_A a s'_A \cdot \pi_{A}({\sigma})$`
 - for any action `$a \in \Sigma_A$`
- `$\pi_{A}(\langle s_A, s_B \rangle b \langle s_A, s'_B \rangle \cdot \sigma) = \pi_{A}({\sigma})$`
 - for any *asynchronous* `$b \in \Sigma_B$`

*We extend `$\pi$` to languages*

---

### Theoretical results

*Encoding NP as PMC we can prove*

**Theorem:** `$\mathcal{L}(\Phi_P // A) = \pi_{B}(\mathcal{L}(P))$`

**Theorem:** The following statements are equivalent
1. `$ A|B \models P$`
2. `$ A \models \Phi_P // B$` (resp. B and A)
3. `$\mathcal{L}(A) \subseteq \pi_{A}(\mathcal{L}(P))$` (resp. B)

---

### Practical results

- A new quotienting algorithm for FSA
 - Efficient on DFA (`$O(n^5)$` vs. `$O(n^6)$`)
- *PESTS*: a OS implementation on github
 - [https://github.com/SCPTeam/pests](https://github.com/SCPTeam/pests)

---

### Example end / Demo

---

### Conclusion

*Results*
-

*Future work*
- Symbolic automata and MSO logic

---

### References

- [Andersen] H. R. Andersen, Partial model checking, 1999
- [Wonham] W. M. Wonham, Supervisory control of discrete-event systems, (online) 2017
- [Ehlers et al.] R. Ehlers, S. Lafortune, S. Tripakis, M. Vardi. Bridging
the Gap between Supervisory Control and Reactive Synthesis: Case of Full Observation and Centralized Control, 2014
