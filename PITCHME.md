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

Given two transition systems X and A find B such that A|B = X

- Inverse operation of A|B
- Less common but relevant for some problems on interest
- **Example** Sub-module construction and controller synthesis
- B could not exist (partial function)

**Difficult to solve in general**

---

### Natural projection 
$$\pi : 2^{\Sigma_0} \times \Sigma_0^\ast \rightarrow \Sigma_0^\ast$$

- Removes from a trace $\eta \in \Sigma_0^\ast$ the symbols belonging to $\Sigma \subseteq \Sigma_0$
- **Example:** `$\pi_{\{b,g,u\}}(bungabunga) = nana$`
- Can be extended to a language $\mathcal{L} \subseteq \Sigma_0^\ast$
- **Example:** `$\pi_{\{b,g,u\}}(bunga^\ast) = na^\ast$`

---

### Partial model checking [A99]
$$// : \Phi \times \mathbb{A} \rightarrow \Phi$$
- Given a ($\mu$-calculus) formula $\phi$ and a transition system A find $\phi' = \phi // A$ such that for all B

A|B $\models \phi$  iff B $\models \phi'$

- Several applications in the formal methods community (e.g. program refinement and monitoring)

---

### NP vs. PMC

| | NP | PMC |
|---|---|---|
| Computational model | RE | LTS |
| Specification language | FSA | $L_\mu$ |
| Complexity | EXPTIME* | EXPTIME | 
| Tools | TCT, IDES3, DESTool | mCRL2, CADP, MuDiv |


*: PTIME for a specific class of discrete-event systems

---
### A common framework for NP

For LTSs $A$ and $B$ with $\Gamma = \Sigma_A \cap \Sigma_B$, the **natural projection on A** of a trace $\sigma$, in symbols $\pi_{A}({\sigma})$, is defined as follows:
 $
\proj{\spair{s_A}{s_B}}{A} & = & s_A \\
\proj{\spair{s_A}{s_B} \xrightarrow{a}_{A \parallel B} \spair{s'_A}{s'_B} \cdot \sigma}{A} & = & s_A \xrightarrow{a}_{A} s'_A \cdot \proj{\sigma}{A} & \textnormal{if } a \in \Sigma_A  \\
\proj{\spair{s_A}{s_B} \xrightarrow{b}_{A \parallel B} \spair{s_A}{s'_B} \cdot \sigma}{A} & = & \proj{\sigma}{A} & \textnormal{if } b \in \Sigma_B \setminus \Gamma 

Natural projection on second component $B$ is analogously defined.
We extend the natural projection to sets of traces in the usual way: $\proj{T}{A} = \{\proj{\sigma}{A} \mid \sigma \in T\}$.


The \emph{inverse projection} of a trace $\sigma$ over an LTS $A \parallel B$, in symbols $\iproj{\sigma}{A}$, is defined as $\iproj{\sigma}{A} = \{ \sigma' \mid \proj{\sigma'}{A} = \sigma \}$. 
Its extension to sets is $\iproj{T}{A} = \bigcup\limits_{\sigma \in T} \iproj{\sigma}{A}$.\qed

---

### 


---


### NP vs. PMC


---

### Conclusion

---

### References

[A99] H. R. Andersen, Partial Model Checking
[W] Wonham

<!--stackedit_data:
eyJoaXN0b3J5IjpbODg4MTEzNTEzXX0=
-->
<!--stackedit_data:
eyJoaXN0b3J5IjpbMjA4ODYwMjMwMV19
-->
