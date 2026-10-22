# Phase 4.F.6 — Plusieurs rules dans un même `%match`

Validation que le parser actuel accepte déjà des `%match` à plusieurs
règles d'action (`r1 -> { } r2 -> { }`).

---

## 1. Spec (référence Java)

`AstBuilder.java` lowers each `Cst_ConstraintAction` to one
`ConstraintInstruction`. Plusieurs `ConstraintAction` dans un même
`%match` produisent plusieurs `ConstraintInstruction` listés dans la
variadic `concConstraintInstruction` portée par le constructor `Match`.

Pour la fixture `match0g_rules` (deux règles, ligne 4 et ligne 5) :
```
Match(
  concConstraintInstruction(
    ConstraintInstruction(MatchConstraint(EmptyName, t-bq, T), action, OT(CA, 4)),
    ConstraintInstruction(MatchConstraint(Name("x"), t-bq, T), action, OT(CA, 5))
  ),
  matchOptions
)
```

L'OriginTracking de chaque rule reflète sa ligne (l'`OT(Name("ConstraintAction"), <ligne>, file)`).

---

## 2. Implémentation Go

Aucun code parser à ajouter : la boucle existe déjà dans `parseMatch`
depuis la 4.F.1 :

```go
for {
    p.skipBlankInline()
    if p.atEnd() { return nil, fmt.Errorf("unterminated %%match body from %s", p.cur) }
    if p.peek(0) == '}' { break }
    rule, err := p.parseActionRule(subjects)
    if err != nil { return nil, err }
    rules = append(rules, rule)
}
```

La boucle consomme tant que ce n'est pas un `}`. Aucun séparateur
entre rules (le `}` de la rule précédente est consommé par
`consumeBalancedBlock` dans `parseActionRule`). Pas de
modification de code nécessaire ; il manquait juste la fixture pour
**prouver** que ça marche byte-pour-byte contre la spec.

---

## 3. Fixture + tests

- `tomgo/testdata/parse/match0g_rules/scenario.t` :
  ```
  public class Match0g {
    public void f(Object t) {
      %match(t) {
        _ -> { }
        x -> { }
      }
    }
  }
  ```
- `TestParseMatch0gRules` épingle l'AST capturé par dump direct ;
  vérifie notamment que les OriginTracking distinguent les lignes 4 et 5.
- Entrée `"match0g_rules"` ajoutée à la slice `fixtures` de
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
=== RUN   TestParseMatch0gRules     --- PASS
ok    tom/tomgo/internal/tomparser    0.34s
```

Tous les autres paquets verts.

---

## 5. Couverture courante du parser

**13 fixtures** validées (1 ajoutée) :

| Fixture              | Construction validée                                            |
| -------------------- | --------------------------------------------------------------- |
| `skeleton`           | `%typeterm` minimal                                             |
| `op_noargs`          | `%op` sans slots                                                |
| `op_slots`           | `%op` avec slots                                                |
| `typeterm_extends`   | `%typeterm X extends Y`                                         |
| `oplist_oparray`     | `%oplist` / `%oparray`                                          |
| `include_local`      | `%include`                                                      |
| `water_multi`        | water ANTLR-fidèle (3 visibles)                                 |
| `match0b`            | `%match` minimal — wildcard `_`                                 |
| `match0c_named`      | `%match` — pattern variable nommée `x`                          |
| `match0d_appl`       | `%match` — application nullaire `Foo()`                         |
| `match0e_appl_args`  | `%match` — application avec sous-patterns `Foo(x, Bar())`       |
| `match0f_multi`      | `%match` — multi-sujets `x, y -> { }` sur `(a, b)`              |
| `match0g_rules`      | `%match` — plusieurs rules `_ → {} x → {}` **(nouveau)**        |

---

## 6. Suite logique

Reste à finir `%match` :
1. **Body non vide** dans l'action rule — le plus gros morceau ; demande
   un sous-parser host-language (Java) qui dump les instructions en
   `TL`/`ITL` au lieu de `concInstruction()` vide.
2. Contraintes `pattern << bqterm` (AND/OR) — extensions de
   `parseActionRule` pour reconnaître `<<` à droite d'un pattern.
