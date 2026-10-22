# Phase 4.A.1 — Correction du hook AU : absorption de l'unité

Bug pré-existant introduit en phase 4.A. Le hook AU généré par
`emitAUPrologue` (`internal/backend/backend.go`) aplatissait les
sous-termes imbriqués mais **n'absorbait pas** l'élément unité dans
la liste plate, contrairement au hook Java.

---

## 1. Symptôme

```go
tomast.MakeAndConstraint(MC, tomast.MakeTrueConstraint())
// AVANT : AndConstraint(MatchConstraint(...), TrueConstraint())
// APRÈS : MatchConstraint(...)   (= MC)
```

```go
tomast.MakeAndConstraint(tomast.MakeTrueConstraint(), tomast.MakeTrueConstraint())
// AVANT : AndConstraint(TrueConstraint(), TrueConstraint())
// APRÈS : TrueConstraint()
```

Le cas où `len(args) == 0 → unit` était déjà couvert.

---

## 2. Spec Java (référence)

`stable/tom/gom/expander/HookTypeExpander.java:569` (cons-make hook
généré par `makeAUHookList`) :

```java
if (head == userNeutral) { return tail; }
if (tail == userNeutral) { return head; }
```

Appliqué à chaque `Cons<Op>.make(head, tail)`. Effet net pour une
construction `Cons*(MC1, Cons*(unit, Empty))` :
1. `Empty<Op>.make()` → `unit` (hook AU sur Empty)
2. `Cons<Op>(unit, unit)` → `unit` (head == userNeutral)
3. `Cons<Op>(MC1, unit)` → `MC1` (tail == userNeutral)

Donc un cons-list-imbriqué avec un unique élément non-unité se
réduit au bare élément (pas de wrapper `AndConstraint(...)`).

---

## 3. Fix Go (`internal/backend/backend.go`)

Dans `emitAUPrologue`, après l'étape de flattening, ajout d'un
filtre qui supprime les éléments égaux à l'unité, puis short-circuit
sur 0 ou 1 élément :

```go
unit := MakeTrueConstraint()
filtered := flat[:0]
for _, a := range flat {
    if a != unit {
        filtered = append(filtered, a)
    }
}
flat = filtered
if len(flat) == 0 {
    return unit
}
if len(flat) == 1 {
    return flat[0]
}
```

L'égalité repose sur le pointer-equality offerte par le
hash-consing : tous les `MakeTrueConstraint()` retournent le même
pointeur canonique.

Côté tomast régénéré, le seul fichier modifié est
`internal/tomast/tomconstraint.go` (les deux constructeurs
`MakeAndConstraint` et `MakeOrConstraint`).
`MakeOrConstraintDisjunction` reste inchangé (AU sans unité — le
filtre n'est pas émis quand `unit == ""`).

---

## 4. Tests

`internal/backend/tomast_hooks_test.go` :

- **`TestHook_AndConstraint_AssociativityFlattens`** réécrit : utilise
  `FalseConstraint` (qui n'est pas l'unité d'AndConstraint) au lieu
  de `TrueConstraint` (qui serait absorbé) — vérifie toujours
  l'aplatissement sans interférer avec l'absorption.
- **`TestHook_AndConstraint_AbsorbsTrueConstraintUnit`** (nouveau) :
  - `AndConstraint(b, unit) == b`
  - `AndConstraint(unit, b) == b`
  - `AndConstraint(unit, unit, unit) == unit`
  - `AndConstraint(unit, b, unit) == b`
  - `AndConstraint(b, b, b)` reste en 3-élément (vérification du
    print exact).
- **`TestHook_OrConstraint_AbsorbsFalseConstraintUnit`** (nouveau) :
  pendant pour OrConstraint avec FalseConstraint comme unité.

Tous verts. `TestSelfBootstrap_Tomast` toujours OK (la regen
produit un tomast byte-stable contre le commit).

---

## 5. Pourquoi maintenant

Découvert pendant la préparation de la phase **4.F.5**
(multi-subjects). Le parser Java consomme `%match(a, b) { p1, p2 -> {
… } }` en construisant un `ConsCst_AndConstraint(...)` puis le
convertit en chaîne de `Cons<Constraint>` ; les hooks AU absorbent
les `TrueConstraint` intermédiaires et le résultat **dépend** de
cette absorption.

Sans ce fix, la phase 4.F.5 aurait épinglé un AST Go qui aurait
divergé byte-pour-byte du Java au moment où `stable/dist/lib/` est
disponible pour la cross-validation.
