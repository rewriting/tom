# Phase 4.F.3 — `%match` avec application nullaire `Foo()`

Extension du parser TOM Go pour accepter une application d'opérateur
**sans argument** sur le côté gauche d'une action rule, en plus de `_`
(4.F.1) et `x` (4.F.2).

---

## 1. Spec exécutable (référence Java)

Deux chemins Java sont impliqués :

- **CST** (`stable/tom/engine/parser/antlr4/CstBuilder.java:446-472`,
  méthode `exitPattern`) : `Foo()` ⇒ `Cst_Appl(symbolList, patternList)`
  via la branche `ctx.explicitArgs() != null` (ligne 452). Les
  parenthèses vides comptent comme `explicitArgs` non null avec une
  liste interne vide.
- **AST** (`stable/tom/engine/parser/antlr4/AstBuilder.java:793-804`,
  méthode `convert(CstPattern)`) :
  ```java
  // Cas Cst_Appl
  OptionList optionList = EmptyconcOption.make();
  Theory theory = extractTheory(symbolList);     // vide pour "Foo" (pas de ? ni ??)
  if(theory.length() > 0) { /* ajoute MatchingTheory */ }
  TomNameList nameList = convert(symbolList);    // → concTomName(Name("Foo"))
  TomList argList = convert(patternList);        // vide → concTomTerm()
  ConstraintList constraintList = EmptyconcConstraint.make();
  return TermAppl.make(optionList, nameList, argList, constraintList);
  ```

Résultat final pour `Foo()` sans `?`/`??` :
```
TermAppl(concOption(), concTomName(Name("Foo")), concTomTerm(), concConstraint())
```

À noter : le `Cst_Constant` (`AstBuilder.java:767-773`) produit la
**même** forme d'AST, mais ce n'est PAS le chemin emprunté par
`Foo()` — celui-là sert pour les constantes pures (style entiers ou
chaînes, gérées via `exitConstant`/`Cst_ConstantOr`).

---

## 2. Implémentation Go

`internal/tomparser/parser.go` — `parsePattern` étendu avec une
discrimination `ID` vs `ID '(' ')'` après lecture de l'identifier :

```go
// après readIdent() :
save := p.idx
saveCur := p.cur
p.skipBlankInline()
if !p.atEnd() && p.peek(0) == '(' {
    p.advance() // '('
    p.skipBlankInline()
    if p.atEnd() || p.peek(0) != ')' {
        return nil, fmt.Errorf("only empty arg list supported in pattern application yet at %s", p.cur)
    }
    p.advance() // ')'
    return tomast.MakeTermAppl(
        tomast.MakeConcOption(),
        tomast.MakeConcTomName(tomast.MakeName(name)),
        tomast.MakeConcTomTerm(),
        tomast.MakeConcConstraint(),
    ), nil
}
// rewind whitespace skip — caller (named variable path) ignores
// trailing blanks anyway, but we keep the cursor explicit
p.idx = save
p.cur = saveCur
return tomast.MakeVariable(...)
```

Une application avec arguments non vides est explicitement rejetée
avec un message ciblé (`only empty arg list supported in pattern
application yet`). Ce sera levé en 4.F.4.

La grammaire en doc-comment passe de :
```
pattern : '_' | ID                              (anonymous or named Variable)
```
à :
```
pattern : '_' | ID | ID '(' ')'                  (Variable or nullary TermAppl)
```

---

## 3. Fixture + tests

- `tomgo/testdata/parse/match0d_appl/scenario.t` :
  ```
  public class Match0d {
    public void f(Object t) {
      %match(t) {
        Foo() -> { }
      }
    }
  }
  ```
- `tomgo/internal/tomparser/parser_test.go` : `TestParseMatch0dAppl`
  épingle la chaîne AST attendue (dérivée par analogie avec `match0b`,
  en substituant le pattern par `TermAppl(concOption(),concTomName(Name("Foo")),concTomTerm(),concConstraint())`).
- `tomgo/internal/tomparseq/dump_skeleton_test.go` : nouvelle entrée
  `"match0d_appl"` dans la slice `fixtures`. Skip propre sans
  `stable/dist/lib/`.

---

## 4. Résultat

```
$ go test ./internal/tomparser/... -v -run TestParseMatch
=== RUN   TestParseMatch0b      --- PASS
=== RUN   TestParseMatch0cNamed --- PASS
=== RUN   TestParseMatch0dAppl  --- PASS
ok    tom/tomgo/internal/tomparser    0.39s

$ go test ./internal/... ./library/... ./cmd/...
(tous verts)
```

---

## 5. Couverture courante du parser

**10 fixtures** validées (1 ajoutée) :

| Fixture            | Construction validée                                          |
| ------------------ | ------------------------------------------------------------- |
| `skeleton`         | `%typeterm` minimal                                           |
| `op_noargs`        | `%op` sans slots                                              |
| `op_slots`         | `%op` avec slots                                              |
| `typeterm_extends` | `%typeterm X extends Y`                                       |
| `oplist_oparray`   | `%oplist` / `%oparray`                                        |
| `include_local`    | `%include`                                                    |
| `water_multi`      | water ANTLR-fidèle (3 visibles)                               |
| `match0b`          | `%match` minimal — wildcard `_`                               |
| `match0c_named`    | `%match` — pattern variable nommée `x`                        |
| `match0d_appl`     | `%match` — application nullaire `Foo()` **(nouveau)**         |

---

## 6. Suite logique

1. ~~Pattern variable nommée `x`~~ ✅ (4.F.2).
2. ~~Pattern application nullaire `Foo()`~~ ✅ (cette phase).
3. **Pattern application avec sous-patterns `Foo(x, y)`** — extension
   directe : récursion sur `parsePattern` pour chaque sous-pattern,
   `argList` non vide → `concTomTerm(p1, p2, …)`.
4. Body non vide dans l'action rule (instructions Java consommées en
   `TL`/`ITL`).
5. Multi-subjects `%match(a, b) { p1, p2 -> { … } }`.
6. Contraintes `pattern << bqterm` (AND/OR).
