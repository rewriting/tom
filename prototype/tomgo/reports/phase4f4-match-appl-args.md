# Phase 4.F.4 — `%match` avec application + sous-patterns `Foo(p1, …, pN)`

Extension du parser pour consommer une liste d'arguments non vide dans
une application de pattern. La 4.F.3 acceptait `Foo()` ; cette
sous-phase rend `Foo(x, Bar())` byte-équivalent au Java.

---

## 1. Spec exécutable (référence Java)

Identique au cas nullaire (`AstBuilder.java:793-804`, branche
`Cst_Appl`), mais avec `argList` non vide :

```java
TomList argList = convert( cst.getpatternList() );   // récursion sur chaque sous-pattern
return TermAppl.make(optionList, nameList, argList, constraintList);
```

`convert(CstPatternList)` mappe chaque sous-`CstPattern` via le même
`convert(CstPattern)` que pour le pattern racine — donc les
sous-patterns suivent exactement les mêmes règles : `_`, `x`, `Foo()`,
`Foo(...)`, etc.

Pour le fixture `Foo(x, Bar())` :
- arg 1 : `x` → `Variable(concOption(), Name("x"), unknownType, concConstraint())`
- arg 2 : `Bar()` → `TermAppl(concOption(), concTomName(Name("Bar")), concTomTerm(), concConstraint())`
- pattern entier :
  ```
  TermAppl(
    concOption(),
    concTomName(Name("Foo")),
    concTomTerm(
      Variable(concOption(),Name("x"),unknownType,concConstraint()),
      TermAppl(concOption(),concTomName(Name("Bar")),concTomTerm(),concConstraint())
    ),
    concConstraint()
  )
  ```

---

## 2. Implémentation Go

`internal/tomparser/parser.go` — la branche application de
`parsePattern` délègue maintenant à une nouvelle fonction
`parsePatternArgList` après avoir consommé la `(` :

```go
if !p.atEnd() && p.peek(0) == '(' {
    p.advance() // '('
    args, err := p.parsePatternArgList()
    if err != nil {
        return nil, err
    }
    if p.atEnd() || p.peek(0) != ')' {
        return nil, fmt.Errorf("expected ')' to close pattern application at %s", p.cur)
    }
    p.advance() // ')'
    return tomast.MakeTermAppl(
        tomast.MakeConcOption(),
        tomast.MakeConcTomName(tomast.MakeName(name)),
        tomast.MakeConcTomTerm(args...),
        tomast.MakeConcConstraint(),
    ), nil
}
```

`parsePatternArgList` est un classique :

```go
func (p *parser) parsePatternArgList() ([]tomast.TomTerm, error) {
    var args []tomast.TomTerm
    p.skipBlankInline()
    if !p.atEnd() && p.peek(0) == ')' {
        return args, nil
    }
    for {
        sub, err := p.parsePattern()
        if err != nil { return nil, err }
        args = append(args, sub)
        p.skipBlankInline()
        if p.atEnd() {
            return nil, fmt.Errorf("unterminated pattern arg list at %s", p.cur)
        }
        if p.peek(0) == ')' { return args, nil }
        if p.peek(0) != ',' {
            return nil, fmt.Errorf("expected ',' or ')' in pattern arg list at %s", p.cur)
        }
        p.advance() // ','
        p.skipBlankInline()
    }
}
```

La récursion via `parsePattern` couvre **gratuitement** :
- sous-patterns variables (`x`, `_`),
- sous-patterns applications (`Bar()`, `Baz(z)`),
- mélanges arbitraires (`Foo(_, Bar(x, _))`).

Grammaire en doc-comment :
```
pattern : '_' | ID | ID '(' (pattern (',' pattern)*)? ')'
                                                       (Variable or TermAppl)
```

---

## 3. Fixture + tests

- `tomgo/testdata/parse/match0e_appl_args/scenario.t` exerce une
  arité 2 mixte (variable + application nullaire) :
  ```
  public class Match0e {
    public void f(Object t) {
      %match(t) {
        Foo(x, Bar()) -> { }
      }
    }
  }
  ```
- `TestParseMatch0eApplArgs` épingle la chaîne attendue.
- Entrée `"match0e_appl_args"` ajoutée à la slice `fixtures` de
  `TestGoParserAgainstJava`.

---

## 4. Résultat

```
$ go test ./internal/tomparser/... -v -run TestParseMatch
=== RUN   TestParseMatch0b           --- PASS
=== RUN   TestParseMatch0cNamed      --- PASS
=== RUN   TestParseMatch0dAppl       --- PASS
=== RUN   TestParseMatch0eApplArgs   --- PASS
ok    tom/tomgo/internal/tomparser    0.29s
```

Tous les autres paquets verts (`backend`, `equivtest`, `gom`,
`gomast`, `tomparseq`, `sharedobjects`, `cmd/tomgo`).

---

## 5. Couverture courante du parser

**11 fixtures** validées (1 ajoutée) :

| Fixture             | Construction validée                                         |
| ------------------- | ------------------------------------------------------------ |
| `skeleton`          | `%typeterm` minimal                                          |
| `op_noargs`         | `%op` sans slots                                             |
| `op_slots`          | `%op` avec slots                                             |
| `typeterm_extends`  | `%typeterm X extends Y`                                      |
| `oplist_oparray`    | `%oplist` / `%oparray`                                       |
| `include_local`     | `%include`                                                   |
| `water_multi`       | water ANTLR-fidèle (3 visibles)                              |
| `match0b`           | `%match` minimal — wildcard `_`                              |
| `match0c_named`     | `%match` — pattern variable nommée `x`                       |
| `match0d_appl`      | `%match` — application nullaire `Foo()`                      |
| `match0e_appl_args` | `%match` — application avec sous-patterns `Foo(x, Bar())` **(nouveau)** |

---

## 6. Suite logique

1. ~~Pattern variable nommée `x`~~ ✅ (4.F.2).
2. ~~Pattern application nullaire `Foo()`~~ ✅ (4.F.3).
3. ~~Pattern application `Foo(x, y)` avec sous-patterns~~ ✅ (cette
   phase).
4. **Body non vide** dans l'action rule — il faut produire un
   `RawAction` qui encapsule effectivement le corps Java (séquence de
   `TL`/`ITL`) au lieu du `If(TrueTL(), AbstractBlock(concInstruction()),
   Nop())` qu'on émet aujourd'hui pour le body vide.
5. Multi-subjects `%match(a, b) { p1, p2 -> { … } }` — étendre
   `parseActionRule` pour consommer N patterns en parallèle des N
   subjects.
6. Contraintes `pattern << bqterm` (AND/OR).
