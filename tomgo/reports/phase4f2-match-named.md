# Phase 4.F.2 — `%match` avec pattern variable nommée

Extension du parser TOM Go pour accepter un identifier sur le côté
gauche d'une action rule (`x -> { … }`), en plus du wildcard anonyme
déjà couvert en 4.F.1 (`_ -> { … }`).

---

## 1. Spec exécutable (référence Java)

Fichier de référence : `stable/tom/engine/parser/antlr4/AstBuilder.java`,
lignes 752–766.

```java
// Pattern variable nommée (x) :
Variable.make(
  EmptyconcOption.make(),    // pas d'OriginTracking sur le pattern
  Name.make(varname),        // Name("x")
  TYPE_UNKNOWN,              // unknown type
  EmptyconcConstraint.make() // pas de contraintes
)

// Pattern wildcard anonyme (_) — pour comparaison :
Variable.make(
  EmptyconcOption.make(),    // idem
  EmptyName.make(),          // EmptyName (≠ Name(""))
  TYPE_UNKNOWN,
  EmptyconcConstraint.make()
)
```

Différence unique entre les deux formes : `EmptyName()` ↔ `Name("x")`.
Aucun OriginTracking n'est attaché — c'est le `MatchConstraint` parent
qui porte les options de position via le `ConstraintInstruction`.

CST en amont (`CstBuilder.java`) :
- Identifier seul → `Cst_Variable.make(varname)` (ligne 457)
- `_` → `Cst_UnamedVariable.make()` (ligne 461)

---

## 2. Implémentation Go

`internal/tomparser/parser.go` — `parsePattern` étendu :

```go
func (p *parser) parsePattern() (tomast.TomTerm, error) {
    if p.atEnd() {
        return nil, fmt.Errorf("expected pattern at %s", p.cur)
    }
    // '_' isolé (pas suivi d'un autre caractère identifier)
    if p.peek(0) == '_' && !isIdentChar(p.peek(1)) {
        p.advance()
        return tomast.MakeVariable(
            tomast.MakeConcOption(),
            tomast.MakeEmptyName(),
            unknownType(),
            tomast.MakeConcConstraint(),
        ), nil
    }
    if !isIdentStart(p.peek(0)) {
        return nil, fmt.Errorf("expected pattern variable at %s", p.cur)
    }
    name, err := p.readIdent()
    if err != nil {
        return nil, err
    }
    return tomast.MakeVariable(
        tomast.MakeConcOption(),
        tomast.MakeName(name),
        unknownType(),
        tomast.MakeConcConstraint(),
    ), nil
}
```

La grammaire en commentaire de tête évolue de :
```
pattern : '_'              (anonymous Variable only)
```
à :
```
pattern : '_' | ID         (anonymous or named Variable)
```

---

## 3. Fixture + tests

- `tomgo/testdata/parse/match0c_named/scenario.t` :
  ```
  public class Match0c {
    public void f(Object t) {
      %match(t) {
        x -> { }
      }
    }
  }
  ```
- `tomgo/internal/tomparser/parser_test.go` : `TestParseMatch0cNamed`
  épingle la chaîne AST attendue (dérivée par analogie ligne-pour-ligne
  avec `TestParseMatch0b`, en ne substituant que `EmptyName()` →
  `Name("x")` et `Match0` → `Match0c`).
- `tomgo/internal/tomparseq/dump_skeleton_test.go` : nouvelle entrée
  `"match0c_named"` dans la slice `fixtures` de
  `TestGoParserAgainstJava`. Lance la comparaison Go ⇄ Java
  byte-pour-byte quand `stable/dist/lib/` est disponible (skip propre
  sinon).

---

## 4. Résultat

```
$ go test ./internal/tomparser/...
ok    tom/tomgo/internal/tomparser    0.30s

$ go test ./internal/...
ok    tom/tomgo/internal/backend      (cached)
ok    tom/tomgo/internal/equivtest    (cached)
ok    tom/tomgo/internal/gom          (cached)
ok    tom/tomgo/internal/gomast       (cached)
ok    tom/tomgo/internal/tomparseq    0.18s
ok    tom/tomgo/internal/tomparser    0.30s
```

`TestSelfBootstrap` et `TestSelfBootstrap_Tomast` toujours verts — la
modification de `parser.go` ne touche pas le backend ni les ADT
générés.

---

## 5. Couverture courante du parser

**9 fixtures** validées (1 ajoutée) :

| Fixture            | Construction validée                                   |
| ------------------ | ------------------------------------------------------ |
| `skeleton`         | `%typeterm` minimal                                    |
| `op_noargs`        | `%op` sans slots                                       |
| `op_slots`         | `%op` avec slots                                       |
| `typeterm_extends` | `%typeterm X extends Y`                                |
| `oplist_oparray`   | `%oplist` / `%oparray`                                 |
| `include_local`    | `%include`                                             |
| `water_multi`      | water ANTLR-fidèle (3 visibles)                        |
| `match0b`          | `%match` minimal — wildcard `_`                        |
| `match0c_named`    | `%match` — pattern variable nommée **(nouveau)**       |

---

## 6. Suite logique

Dans l'ordre d'effort croissant (cf. CLAUDE.md §5) :

1. ~~Pattern variable nommée `x`~~ ✅ (cette phase).
2. **Pattern application `Foo()`** sans sous-patterns.
3. Pattern application `Foo(args)` avec sous-patterns.
4. Body non vide dans l'action rule (instructions Java consommées en
   `TL`/`ITL`).
5. Multi-subjects `%match(a, b) { p1, p2 -> { … } }`.
6. Contraintes `pattern << bqterm` (AND/OR).
