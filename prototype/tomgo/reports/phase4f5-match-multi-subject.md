# Phase 4.F.5 — `%match` multi-sujets

Extension du parser pour accepter `%match(a, b) { p1, p2 -> { … } }`
avec **N sujets et N patterns** par règle.

Cette sous-phase dépend de **4.A.1** (correction du hook AU sur
`AndConstraint` : absorption de l'unité `TrueConstraint()`). Sans
cette correction, l'AST Go aurait divergé byte-pour-byte du Java au
moment de la cross-validation.

---

## 1. Spec exécutable (référence Java)

`AstBuilder.java:664-748`, méthode `convert(CstConstraint cst,
CstBQTermList subjectList, int subjectIndex)`. Pour une rule
`p1, p2 -> { }` sur `%match(a, b)` :

1. Le CST construit `ConsCst_AndConstraint(c1, ConsCst_AndConstraint(c2, EmptyCst_AndConstraint))` où `cI = Cst_MatchArgumentConstraint(pattern=pI)`.
2. `convert(cst, subjectList=[a,b], idx=0)` se déroule en cons-récursion :
   - `chead = convert(c1, subjectList, 0)` → `MatchConstraint(p1, a, T_a)`
   - `ctail = convert(tail, subjectList, 1)` (récursif) :
     - `chead' = MatchConstraint(p2, b, T_b)`
     - `ctail' = convert(Empty, subjectList, 2)` → `TrueConstraint()` (ligne 703)
     - returns `ConsAndConstraint.make(chead', ConsAndConstraint.make(ctail', EmptyAndConstraint.make()))`
     - hooks AU : `Empty→TrueConstraint`, `Cons(TrueConstraint,TrueConstraint)→TrueConstraint`, `Cons(MC2, TrueConstraint)→MC2`
     - donc `ctail = MC2`
   - returns `ConsAndConstraint.make(MC1, ConsAndConstraint.make(MC2, EmptyAndConstraint.make()))`
   - hooks AU : `Empty→TrueConstraint`, `Cons(MC2, TrueConstraint)→MC2`, `Cons(MC1, MC2)→ConsAndConstraint(MC1, MC2)`
   - Print canonique : `AndConstraint(MC1, MC2)` (la cons-chain rendue en variadic).

Donc pour 2 sujets : `AndConstraint(MatchConstraint(p1, a, T_a), MatchConstraint(p2, b, T_b))`.
Pour 1 sujet : `MatchConstraint(p1, a, T_a)` directement (la chaîne
ConsAndConstraint(MC, Empty) se réduit via le hook tail-unit).

---

## 2. Implémentation Go

`internal/tomparser/parser.go` — `parseActionRule` refondu :

```go
patterns := make([]tomast.TomTerm, 0, len(subjects))
for {
    pat, err := p.parsePattern()
    if err != nil { return nil, err }
    patterns = append(patterns, pat)
    p.skipBlankInline()
    if p.atEnd() {
        return nil, fmt.Errorf("unterminated action rule at %s", p.cur)
    }
    if p.peek(0) == ',' {
        p.advance() // ','
        p.skipBlankInline()
        continue
    }
    break
}
if len(patterns) != len(subjects) {
    return nil, fmt.Errorf("action rule has %d patterns but %%match has %d subjects at %s",
        len(patterns), len(subjects), p.cur)
}

matchConstraints := make([]tomast.Constraint, len(patterns))
for i, pat := range patterns {
    matchConstraints[i] = tomast.MakeMatchConstraint(pat, subjects[i], unknownType())
}
constraint := tomast.MakeAndConstraint(matchConstraints...)
```

Le `MakeAndConstraint(matchConstraints...)` exploite directement la
sémantique du hook AU corrigé en 4.A.1 :
- 1 sous-pattern → `AndConstraint(MC)` se réduit à `MC` (bare) — donc
  les fixtures à 1 sujet existantes (`match0b`/`c`/`d`/`e`) restent
  byte-stables (vérifié : tous les `TestParseMatch0*` passent
  inchangés).
- N ≥ 2 → `AndConstraint(MC1, MC2, …)` (variadic).

Grammaire en doc-comment :
```
actionRule : pattern (',' pattern)* '->' '{' BALANCED '}'
```

---

## 3. Fixture + tests

- `tomgo/testdata/parse/match0f_multi/scenario.t` :
  ```
  public class Match0f {
    public void f(Object a, Object b) {
      %match(a, b) {
        x, y -> { }
      }
    }
  }
  ```
- `TestParseMatch0fMulti` épingle l'AST Go (capturé à l'exécution,
  validé contre l'analyse Java byte-à-byte).
- Entrée `"match0f_multi"` ajoutée à la slice `fixtures` de
  `TestGoParserAgainstJava`.

---

## 4. Résultat

```
$ go test ./internal/tomparser/... -v -run TestParseMatch
=== RUN   TestParseMatch0b          --- PASS
=== RUN   TestParseMatch0cNamed     --- PASS
=== RUN   TestParseMatch0dAppl      --- PASS
=== RUN   TestParseMatch0eApplArgs  --- PASS
=== RUN   TestParseMatch0fMulti     --- PASS
ok    tom/tomgo/internal/tomparser    0.39s
```

`TestSelfBootstrap_Tomast` et tous les `TestHook_*` (y compris les
nouveaux `TestHook_*_AbsorbsXxxUnit` de 4.A.1) toujours verts.

---

## 5. Couverture courante du parser

**12 fixtures** validées (1 ajoutée) :

| Fixture              | Construction validée                                           |
| -------------------- | -------------------------------------------------------------- |
| `skeleton`           | `%typeterm` minimal                                            |
| `op_noargs`          | `%op` sans slots                                               |
| `op_slots`           | `%op` avec slots                                               |
| `typeterm_extends`   | `%typeterm X extends Y`                                        |
| `oplist_oparray`     | `%oplist` / `%oparray`                                         |
| `include_local`      | `%include`                                                     |
| `water_multi`        | water ANTLR-fidèle (3 visibles)                                |
| `match0b`            | `%match` minimal — wildcard `_`                                |
| `match0c_named`      | `%match` — pattern variable nommée `x`                         |
| `match0d_appl`       | `%match` — application nullaire `Foo()`                        |
| `match0e_appl_args`  | `%match` — application avec sous-patterns `Foo(x, Bar())`      |
| `match0f_multi`      | `%match` — multi-sujets `x, y -> { }` sur `(a, b)` **(nouveau)**|

---

## 6. Suite logique

1. ~~Pattern variable nommée `x`~~ ✅ (4.F.2).
2. ~~Pattern application nullaire `Foo()`~~ ✅ (4.F.3).
3. ~~Pattern application `Foo(x, y)` avec sous-patterns~~ ✅ (4.F.4).
4. ~~Multi-subjects `%match(a, b) { p1, p2 -> { … } }`~~ ✅ (cette
   phase).
5. **Body non vide** dans l'action rule — produire un `RawAction`
   qui contient le code Java du body sous forme de `TL`/`ITL`,
   plutôt que `concInstruction()` vide. Demande un sous-parser
   host-language au niveau de l'action.
6. Contraintes `pattern << bqterm` (AND/OR) — extensions de
   `parseActionRule` pour reconnaître `<<` à droite d'un pattern.
7. Plusieurs rules dans un même `%match` (`p1 -> { } p2 -> { }`).
