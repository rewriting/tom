# Phase 4.F.14 — Backquote dans le body d'une action rule

Premier cas : le body d'une action rule contient un **backquote
variable** isolé (entouré uniquement de whitespace). Le parser
reconnaît le `` ` `` et bascule en mode-island au milieu du water,
émettant un `BQTermToInstruction(BQVariable(...))` au lieu de laisser
le backquote opaque dans un TL.

C'est le pas qui ouvre le portage des backquotes "en pratique" — la
4.F.13 ne couvrait que le contexte très contenu du RHS de `<<`, pas
le body.

---

## 1. Spec exécutable (référence Java)

CST (`CstBuilder.java:236-256`, `exitBlock`) :
Le body est itéré enfant par enfant via le ParseTree. Chaque enfant est
soit du water (`WaterContext`) emballé en `HOSTBLOCK`, soit un îlot
(`IslandContext`) qui crée un `Cst_BQTermToBlock` (entre autres).

Pour `{ return \`Baz(x); }` :
```
CstBlockList = [
  HOSTBLOCK("return "),
  Cst_BQTermToBlock(Cst_BQAppl("Baz", [x])),
  HOSTBLOCK("; ")
]
```

AST (`AstBuilder.java:101-544`, `convert(CstBlock)`) :
- `HOSTBLOCK` → `CodeToInstruction(TargetLanguageToCode(TL(...)))`
  (ligne 109).
- `Cst_BQTermToBlock` → `BQTermToInstruction(convert(bqterm))` (ligne
  119).

Les positions sont préservées dans le `TL` (start, end).

Pour `{ \`x }` (notre fixture — water = pure whitespace) :
- Les deux water-chunks sont vides (purement whitespace) → aucun
  HOSTBLOCK émis (cf. règle 4.F.0).
- Le seul îlot devient `BQTermToInstruction(BQVariable(..., Name("x"),
  ...))`.

Résultat final :
```
AbstractBlock(concInstruction(
  BQTermToInstruction(BQVariable(
    concOption(OriginTracking(Name("x"), <line>, file), ModuleName("default")),
    Name("x"),
    unknownType
  ))
))
```

---

## 2. Implémentation Go

### Sub-parser

Nouveau constructeur `newSubParser(src, filename, start) *parser` qui
crée un parser scoping sur le contenu du body avec une position de
départ explicite. Pas de partage d'état avec le parser principal —
juste un outil pour réutiliser `parseBQTerm` et `tokenizeWater` à
l'intérieur du body content.

### `lowerActionBody` refondu

```go
func lowerActionBody(content string, start position, filename string) ([]tomast.Instruction, error) {
    sub := newSubParser(content, filename, start)
    var insts []tomast.Instruction
    waterStartIdx := 0
    waterStartPos := start

    flushWater := func(endIdx int, endPos position) {
        if endIdx <= waterStartIdx { return }
        chunk := content[waterStartIdx:endIdx]
        tokens := tokenizeWater(chunk, waterStartPos)
        blocks := buildHostblocks(tokens)
        if len(blocks) == 0 { return }   // chunk purement whitespace
        merged := mergeHostblocks(blocks)
        tl := tomast.MakeTL(merged.content,
            tomast.MakeTextPosition(int64(merged.startLine), int64(merged.startCol)),
            tomast.MakeTextPosition(int64(merged.endLine),   int64(merged.endCol)))
        insts = append(insts, tomast.MakeCodeToInstruction(tomast.MakeTargetLanguageToCode(tl)))
    }

    for !sub.atEnd() {
        if sub.peek(0) == '`' {
            flushWater(sub.idx, sub.cur)
            bq, err := sub.parseBQTerm()
            if err != nil {
                return nil, fmt.Errorf("at %s: %w", sub.cur, err)
            }
            insts = append(insts, tomast.MakeBQTermToInstruction(bq))
            waterStartIdx = sub.idx
            waterStartPos = sub.cur
            continue
        }
        sub.advance()
    }
    flushWater(sub.idx, sub.cur)
    return insts, nil
}
```

### Call site

`parseActionRule` passe maintenant `p.filename` à `lowerActionBody` et
gère l'erreur :
```go
bodyInstructions, err := lowerActionBody(bodyContent, bodyStart, p.filename)
if err != nil { return nil, fmt.Errorf("action body: %w", err) }
```

### Compatibilité

Le cas "body sans backquote" garde son comportement byte-stable : la
boucle parcourt le contenu sans jamais déclencher le branch `\``, et le
`flushWater` final émet exactement la même TL qu'avant. Les fixtures
`match0b/c/d/e/f/g/h/i/j/k/l/m/n` restent vertes sans modification.

---

## 3. Fixture + tests

- `tomgo/testdata/parse/match0o_bqbody/scenario.t` :
  ```
  public class Match0o {
    public void f(Object t) {
      %match(t) {
        x -> { `x }
      }
    }
  }
  ```
  Body : `" \`x "` (espace + backquote-x + espace).
- AST produit pour l'`AbstractBlock` :
  ```
  AbstractBlock(concInstruction(
    BQTermToInstruction(BQVariable(
      concOption(OriginTracking(Name("x"),4,"__INPUT__"),ModuleName("default")),
      Name("x"),
      Type(concTypeOption(),"unknown type",EmptyTargetLanguageType())
    ))
  ))
  ```
- `TestParseMatch0oBqBody` épingle l'AST.
- Entrée `"match0o_bqbody"` ajoutée au harnais cross-language.

---

## 4. Résultat

```
$ mage test
... 14 fixtures match*, all PASS
ok    tom/tomgo/stable/tom/parser/parser    0.30s
```

Tous les autres paquets verts (`backend`, `equiv`, `gom`, `gomast`,
`tomparseq`, `sharedobjects`, `cmd/tomgo`).

---

## 5. Couverture courante du parser

**21 fixtures** validées. Le `%match` couvre maintenant :
- patterns : `_`, `_*`, `x`, `x*`, `Foo(args)`, `(F1|F2)(args)`,
  `pat@name`, `!pat`,
- sujets : implicites + explicites `<<`,
- multi-sujets, multi-rules,
- body vide / non-vide pur host-code / **non-vide avec backquote
  variable**.

---

## 6. Suite logique

1. **Backquote application dans le body** : `{ return \`Foo(x); }`.
   Devrait fonctionner *out of the box* (parseBQTerm gère déjà
   l'application). À valider avec une fixture.
2. **Water mélangé avec backquote** : `{ return \`x; }`. Le water
   "return " avant le backquote et ";" après produiraient deux TL +
   un BQTermToInstruction au milieu. À valider.
3. **Multiple backquotes** : `{ \`a + \`b }`.
4. **Backquote dans une expression**, à l'intérieur d'une autre
   construction (`if`, `while`).
5. **Scope de backquote** : `\` (x==y || x==z) `` — backquote qui
   étend sa portée par l'enclosing brace. Pas trivial.
6. **Métaquote** `%[ … ]%` dans un body.
