# Phase 4.F.7 — `%match` avec body non-vide

Le body `{ … }` d'une action rule est désormais consommé par le
**même pipeline water** que la water inter-îlots (4.F.0), et abaissé
en `CodeToInstruction(TargetLanguageToCode(TL(…)))` placé dans le
`concInstruction(…)` du `AbstractBlock`. La compatibilité du body
vide est préservée (la pipeline produit zéro hostblock pour un
contenu purement whitespace → `concInstruction()` vide).

---

## 1. Spec exécutable (référence Java)

`stable/tom/engine/parser/antlr4/AstBuilder.java:109`
(branche `HOSTBLOCK` de `convert(CstBlock cst)`) produit :

```java
CodeToInstruction.make(
  TargetLanguageToCode.make(
    TL.make(content, TextPosition(startLine, startCol),
                     TextPosition(endLine,   endCol))
  )
)
```

Le contenu et les positions viennent de la `CstOption` du hostblock,
**après** le pas de simplification de `CstConverter.simplifyCstBlockList`
(le même qui fusionne les blocks adjacents au top-level — cf. water
ANTLR-fidèle de 4.F.0).

Pour un body pur host code (pas d'îlot TOM imbriqué), le résultat est
**un seul** HOSTBLOCK ⇒ **un seul** `CodeToInstruction(…)` dans le
`concInstruction(…)`.

---

## 2. Implémentation Go

`internal/tomparser/parser.go` :

1. Nouvelle méthode `captureBalancedBlock` (jumelle non-destructive
   de `consumeBalancedBlock`) qui retourne `(content, start, error)`
   où :
   - `content` est la chaîne entre `{` et `}` (exclus),
   - `start` est la position du premier byte **après** le `{`.

2. Nouvelle fonction `lowerActionBody(content, start)
   []tomast.Instruction` qui réutilise la pipeline 4.F.0 :
   ```go
   tokens := tokenizeWater(content, start)
   blocks := buildHostblocks(tokens)
   if len(blocks) == 0 {
       return nil
   }
   merged := mergeHostblocks(blocks)
   tl := tomast.MakeTL(
       merged.content,
       tomast.MakeTextPosition(int64(merged.startLine), int64(merged.startCol)),
       tomast.MakeTextPosition(int64(merged.endLine),   int64(merged.endCol)),
   )
   return []tomast.Instruction{
       tomast.MakeCodeToInstruction(tomast.MakeTargetLanguageToCode(tl)),
   }
   ```

3. `parseActionRule` appelle `captureBalancedBlock` + `lowerActionBody`
   à la place de `consumeBalancedBlock` :
   ```go
   bodyContent, bodyStart, err := p.captureBalancedBlock()
   if err != nil { return nil, err }
   bodyInstructions := lowerActionBody(bodyContent, bodyStart)
   action := tomast.MakeRawAction(tomast.MakeIf(
       tomast.MakeTrueTL(),
       tomast.MakeAbstractBlock(tomast.MakeConcInstruction(bodyInstructions...)),
       tomast.MakeNop(),
   ))
   ```

`consumeBalancedBlock` est conservé pour les blocs « opaques » des
autres îlots (`%typeterm`, `%op`, etc.).

**Limites volontaires** :
- Pas d'îlots TOM imbriqués dans le body (pas de `%match` dedans).
  Le pipeline 4.F.0 traite tout le body comme du water pur ; un `%`
  qui apparaîtrait serait considéré comme un caractère visible
  ordinaire. À lever en 4.F.8+.
- Pas de string-literal / comment awareness (les `{` `}` dans des
  string Java mal placées casseraient `captureBalancedBlock`). Idem,
  conforme au choix fait pour le top-level.

---

## 3. Fixture + tests

- `tomgo/testdata/parse/match0h_body/scenario.t` :
  ```
  public class Match0h {
    public void f(Object t) {
      %match(t) {
        _ -> { doSomething(); }
      }
    }
  }
  ```
- Body content capturé : `" doSomething(); "` (16 caractères, incluant
  l'espace gauche et droit autour du visible).
- Pipeline 4.F.0 → hostblock unique : start=(4,13), end=(4,29), content
  identique au captured (la merge n'ajoute pas de padding pour un seul
  visible). On émet
  ```
  CodeToInstruction(
    TargetLanguageToCode(
      TL(" doSomething(); ", TextPosition(4,13), TextPosition(4,29))))
  ```
- `TestParseMatch0hBody` épingle l'AST byte-pour-byte.
- Entrée `"match0h_body"` ajoutée au harnais cross-language.

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
=== RUN   TestParseMatch0hBody      --- PASS
ok    tom/tomgo/internal/tomparser    0.43s
```

Les tests à body vide (`match0b`/`c`/`d`/`e`/`f`/`g`) restent
byte-stables — la pipeline produit zéro hostblock pour un body
purement whitespace, donc `concInstruction()` reste vide.

---

## 5. Couverture courante du parser

**14 fixtures** validées (1 ajoutée) :

| Fixture              | Construction validée                                            |
| -------------------- | --------------------------------------------------------------- |
| `skeleton`           | `%typeterm` minimal                                             |
| `op_noargs`          | `%op` sans slots                                                |
| `op_slots`           | `%op` avec slots                                                |
| `typeterm_extends`   | `%typeterm X extends Y`                                         |
| `oplist_oparray`     | `%oplist` / `%oparray`                                          |
| `include_local`      | `%include`                                                      |
| `water_multi`        | water ANTLR-fidèle (3 visibles)                                 |
| `match0b`            | `%match` minimal — wildcard `_`, body vide                      |
| `match0c_named`      | `%match` — pattern variable nommée `x`                          |
| `match0d_appl`       | `%match` — application nullaire `Foo()`                         |
| `match0e_appl_args`  | `%match` — application avec sous-patterns `Foo(x, Bar())`       |
| `match0f_multi`      | `%match` — multi-sujets `x, y -> { }` sur `(a, b)`              |
| `match0g_rules`      | `%match` — plusieurs rules                                      |
| `match0h_body`       | `%match` — body non-vide ` doSomething(); ` **(nouveau)**       |

---

## 6. Suite logique

Reste pour finir `%match` :
1. **Contraintes `pattern << bqterm`** (AND/OR) — extensions de
   `parseActionRule` pour reconnaître `<<` à droite d'un pattern.
2. **`%match` imbriqué dans un body** — il faut switcher le pipeline
   en mode "island" à l'intérieur du body, comme le fait
   `TomIslandParser` côté Java.
3. Pattern variable étoile `x*` / `_*`.
4. Pattern annoté `pat@name`.
5. Pattern anti `!pat`.

Au-delà du `%match`, les autres îlots à porter :
- `%strategy ... extends ... { visit Sort { … } }`.
- `%gom { … }` (gom inline).
- Backquote terms `` `Op(args) ``.
- Metaquote `%[ … ]%`.
