## From natural projection to partial model checking and back

__**Gabriele Costa**__, Letterio Galletta (IMT Lucca)

David Basin (ETH Zurich)

Chiara Bodei, Pierpaolo Degano (University of Pisa)

---
### Outline

- Problem statement
- Ongoing collision
- Partial model checking vs. natural projection
- Quotienting with PESTS
- Conclusion and future work

---
### System composition (A|B)

Given two transition systems A and B, their (parallel) composition has
- a state for each pair of A's and B's states
- an initial/final state for each pair of initial/final states
- *synchronous* transitions on common actions
- *asynchronous* transitions on the other actions

**Composition is everyday's operation**

---
### System decomposition (A\B)

Given two transition systems A and B find X such that X|B = A

- \ a.k.a. **quotienting** operator
- Less common but relevant for problems on interest
- **Example:** Sub-module construction (SCP) and controller synthesis (CSP)
- *X could not exist*

**Difficult to solve in general**

---

### Example: Drone Package Delivery

Unmanned aerial vehicles (UAV) operating on a docking station

- **Adder A$_n$** repeatedly puts down $n$ items (action $d$) and sends a synchronous signal (action $s$). Eventually terminates with a synchronous action $t$

- **Remover B$_n$** repeatedly picks up $n$ items (action $u$). Sync like A

- **Docking station P$_n$** has a limited, $n$-elements stack

---

### Example: Drone Package Delivery

<img src="pitch/adder-w.png" width="50%" height="50%">
<img src="pitch/remover-w.png" width="50%" height="50%">

---

### Example: Drone Package Delivery


<img src="pitch/product-w.png" width="70%" height="70%">

<img src="pitch/buffer-n.png" width="70%" height="70%">

---

### Natural projection [Wonham ]
$$\pi : 2^{\Sigma_0} \times \Sigma_0^\ast \rightarrow \Sigma_0^\ast$$

- Removes from a trace $\eta \in \Sigma_0^\ast$ the symbols belonging to $\Sigma \subseteq \Sigma_0$
- **Example:** `$\pi_{\{b,g,u\}}(bungabunga) = nana$`
- Can be extended to a language $\mathcal{L} \subseteq \Sigma_0^\ast$
- **Example:** `$\pi_{\{b,g,u\}}(bunga^\ast) = na^\ast$`

---

### Partial model checking [Andersen 99]
$$// : \Phi \times \mathbb{A} \rightarrow \Phi$$
- Given a ($\mu$-calculus) formula $\phi$ and a transition system A find $\phi' = \phi // A$ such that for all B

A|B $\models \phi$  iff B $\models \phi'$

- Several applications in the formal methods community (e.g. program refinement and monitoring)

---

### NP vs. PMC

| | NP | PMC |
|---|---|---|
| Agent | FSA | LTS |
| Specification | FSA | $L_\mu$ |
| Complexity | EXPTIME* | EXPTIME |
| Tools | TCT, IDES3, DESTool | mCRL2, CADP, MuDiv |


*: PTIME for a specific class of FSA

---

### Gravitational waves

**Control theory** and **formal methods** are colliding [Ehlers et al. 14]

![galaxies](pitch/collision-label.png)

---

### A common framework

- We redefine NP to work with LTS agents and $L_\mu$ specifications.

- A trace $\sigma$ is an alternation of state and action symbols.

- Given LTSs $A$ and $B$ with $\Gamma = \Sigma_A \cap \Sigma_B$, the **natural projection** on $A$ of a trace $\sigma$ is

$\pi_{A}(\langle s_A, s_B \rangle) = s_A$

$\pi_{A}(\langle s_A, s_B \rangle a \langle s'_A, s'_B \rangle \cdot \sigma) = s_A a s'_A \cdot \pi_{A}({\sigma})$ if $a \in \Sigma_A$

\pi_{A}(\langle s_A, s_B \rangle b \langle s_A, s'_B \rangle \cdot \sigma) = \pi_{A}({\sigma})$ if $b \in \Sigma_B \setminus \Gamma$


---

###


---


### NP vs. PMC


---

### Conclusion

---

### References

[Andersen99] H. R. Andersen, Partial Model Checking
[W] Wonham
