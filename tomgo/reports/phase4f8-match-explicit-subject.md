# Phase 4.F.8 — `pattern << bqterm` : sujet explicite

Extension de `parseActionRule` pour reconnaître la forme
`pattern '<<' bqterm` qui **remplace** le sujet implicite venant des
parenthèses du `%match(…)` par un bqterm explicite.

---

## 1. Spec exécutable (référence Java)

`stable/tom/engine/parser/antlr4/CstBuilder.java:288-292` — la grammaire
ANTLR détecte le token `<<` (`MATCH_SYMBOL`) et construit :

```java
res = Cst_MatchTermConstraint.make(lhs_pattern, rhs_bqterm, rhs_type);
```

`stable/tom/engine/parser/antlr4/AstBuilder.java:683-698` — la branche
`Cst_MatchTermConstraint` du `convert(CstConstraint, …)` :

```java
BQTerm currentSubject = convert(cst.getsubject());     // RHS du '<<'
TomType type = …;                                       // déduit du subject
return MatchConstraint.make(convert(cst.getpattern()), currentSubject, type);
```

Important : la branche `Cst_MatchArgumentConstraint` (sans `<<`) utilise
`subjectList[subjectIndex]` — c'est-à-dire le sujet **implicite** de
`%match(s1, s2, …)`. La branche `Cst_MatchTermConstraint` utilise
**uniquement** le bqterm explicite et **ignore** la `subjectList`.

Donc pour `%match(t) { Foo() << t -> { } }`, le sujet de la contrainte
est le `t` à droite du `<<`, pas celui des parenthèses (même si ce sont
deux variables homonymes — leurs OriginTracking diffèrent).

---

## 2. Implémentation Go

`internal/tomparser/parser.go` — la boucle qui consommait juste des
patterns devient une boucle de `ruleSlot{pat, explicit?}` :

```go
type ruleSlot struct {
    pat      tomast.TomTerm
    explicit tomast.BQTerm // nil if implicit
}
var slots []ruleSlot
for {
    pat, err := p.parsePattern()
    if err != nil { return nil, err }
    slot := ruleSlot{pat: pat}
    p.skipBlankInline()
    if !p.atEnd() && p.peek(0) == '<' && p.peek(1) == '<' {
        p.advance(); p.advance() // '<<'
        p.skipBlankInline()
        bq, err := p.parseSubject() // bare ID for now → BQVariable
        if err != nil { return nil, fmt.Errorf("after '<<': %w", err) }
        slot.explicit = bq
        p.skipBlankInline()
    }
    slots = append(slots, slot)
    // … ',' or break …
}
if len(slots) != len(subjects) { return nil, ... }

matchConstraints := make([]tomast.Constraint, len(slots))
for i, slot := range slots {
    subj := subjects[i]
    if slot.explicit != nil { subj = slot.explicit }
    matchConstraints[i] = tomast.MakeMatchConstraint(slot.pat, subj, unknownType())
}
```

`parseSubject` est réutilisé tel quel pour parser le bqterm RHS — il
produit `BQVariable(concOption(OT(Name(id), <ligne_RHS>, file),
ModuleName("default")), Name(id), unknownType)`. La ligne est celle où
apparaît le RHS dans le source, ce qui distingue son OT de celui du
sujet des parens.

Grammaire en doc-comment :
```
actionRule : ruleSlot (',' ruleSlot)* '->' '{' BALANCED '}'
ruleSlot   : pattern ('<<' bqterm)?
```

**Limites volontaires** :
- RHS bqterm restreint à `ID` (bare variable). Pas de `Op(args)`,
  pas de backquote, pas de type annotation. Lever quand on attaquera
  les backquote terms (4.G+).
- Pas de `Cst_NoType` vs `Cst_Type` distinction (la version Java
  permet `pattern << bqterm:Type`). On émet toujours `unknownType()`.

---

## 3. Fixture + tests

- `tomgo/testdata/parse/match0i_explicit/scenario.t` :
  ```
  public class Match0i {
    public void f(Object t) {
      %match(t) {
        Foo() << t -> { }
      }
    }
  }
  ```
- Le `t` du `%match(t)` est en ligne 3 ; le `t` du `<< t` est en
  ligne 4. La contrainte produite utilise le second (OT ligne 4).
- `TestParseMatch0iExplicit` épingle l'AST.
- Entrée `"match0i_explicit"` ajoutée au harnais cross-language.

---

## 4. Résultat

```
$ go test ./internal/tomparser/... -v -run TestParseMatch
=== RUN   TestParseMatch0b           --- PASS
=== RUN   TestParseMatch0cNamed      --- PASS
=== RUN   TestParseMatch0dAppl       --- PASS
=== RUN   TestParseMatch0eApplArgs   --- PASS
=== RUN   TestParseMatch0fMulti      --- PASS
=== RUN   TestParseMatch0gRules      --- PASS
=== RUN   TestParseMatch0hBody       --- PASS
=== RUN   TestParseMatch0iExplicit   --- PASS
ok    tom/tomgo/internal/tomparser    0.34s
```

Tous les autres paquets verts.

---

## 5. Couverture courante du parser

**15 fixtures** validées (1 ajoutée) :

| Fixture              | Construction validée                                                  |
| -------------------- | --------------------------------------------------------------------- |
| `skeleton`           | `%typeterm` minimal                                                   |
| `op_noargs`          | `%op` sans slots                                                      |
| `op_slots`           | `%op` avec slots                                                      |
| `typeterm_extends`   | `%typeterm X extends Y`                                               |
| `oplist_oparray`     | `%oplist` / `%oparray`                                                |
| `include_local`      | `%include`                                                            |
| `water_multi`        | water ANTLR-fidèle (3 visibles)                                       |
| `match0b`            | `%match` minimal — wildcard `_`, body vide                            |
| `match0c_named`      | `%match` — pattern variable nommée `x`                                |
| `match0d_appl`       | `%match` — application nullaire `Foo()`                               |
| `match0e_appl_args`  | `%match` — application avec sous-patterns `Foo(x, Bar())`             |
| `match0f_multi`      | `%match` — multi-sujets `x, y -> { }` sur `(a, b)`                    |
| `match0g_rules`      | `%match` — plusieurs rules                                            |
| `match0h_body`       | `%match` — body non-vide ` doSomething(); `                           |
| `match0i_explicit`   | `%match` — sujet explicite `Foo() << t -> { }` **(nouveau)**          |

---

## 6. Suite logique

Reste pour `%match` :
1. **Contraintes AND/OR** (chaînes `<<` reliées par `&&` / `||`) —
   demande de manipuler des `AndConstraint`/`OrConstraint` au-delà du
   simple multi-sujets.
2. **Type annotation** sur le RHS : `pattern << bqterm:Type`.
3. **RHS bqterm complexe** (Op(args), backquote) — lié à la phase 4.G
   (backquote terms).
4. **`%match` imbriqué** dans un body (island-mode switching).
5. Pattern variable étoile `x*` / `_*`.
6. Pattern annoté `pat@name`.
7. Pattern anti `!pat`.

Au-delà du `%match`, autres îlots à porter :
- `%strategy ... extends ... { visit Sort { … } }`.
- `%gom { … }` inline.
- Backquote terms `` `Op(args) ``.
- Metaquote `%[ … ]%`.
