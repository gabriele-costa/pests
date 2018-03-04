## From natural projection to partial model checking and back

__**Gabriele Costa**__, Letterio Galletta (IMT Lucca) 

David Basin (ETH Zurich)

Chiara Bodei, Pierpaolo Degano (University of Pisa) 

---
### System composition (A|B)

Given two transition systems A and B, their (parallel) composition has
- a state for each pair of A's and B's states
- an initial/final state for each pair of initial/final states
- a (synchronous) transition whenever A and B have an action in common
- the remaining transitions of A and B

**Composition is everyday's operation**

---
### System decomposition (X\A)

Given two transition systems X and A find B such that A|B 


---

### Natural projection 
$$\pi : 2^{\Sigma_0} \times \Sigma_0^\ast \rightarrow \Sigma_0^\ast$$

- Removes from a trace $\eta \in \Sigma_0^\ast$ the symbols belonging to $\Sigma \subseteq \Sigma_0$
- **Example:** `$\pi_{\{b,g,u\}}(bungabunga) = nana$`
- Can be extended to a language $\mathcal{L} \subseteq \Sigma_0^\ast$
- **Example:** `$\pi_{\{b,g,u\}}(bunga^\ast) = na^\ast$`

---

### Partial model checking


---

### NP vs. PMC


---


### NP vs. PMC


---


### NP vs. PMC


---


### NP vs. PMC


---

### Conclusion
<!--stackedit_data:
eyJoaXN0b3J5IjpbLTIxMjgzOTEzOTEsOTU2MjAwMjY1XX0=
-->