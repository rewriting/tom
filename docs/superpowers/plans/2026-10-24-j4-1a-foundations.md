# J4.1a — fondations Go du backend : `platform`, `OutputCode`, `SymbolTable`, `TomBase`, `mage regenTomGo` — plan d'implémentation

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** écrire en Go et en Tom+Go les fondations sur lesquelles J4.1b
portera le backend Go : options, messages et exceptions (`platform`),
`OutputCode`, les sous-ensembles de `ASTFactory`, `SymbolTable` et
`TomBase` qu'utilise le backend (`tools`), compilés par une nouvelle cible
`mage regenTomGo` avec `src/dist` (D4), avec des tests unitaires et une
preuve d'équivalence avec Java.

**Architecture:** deux paquetages Go nouveaux dans le module `tom` :
`tom/engine/platform` (Go pur : options lues dans le paquet du pont,
messages `TomMessage`, protocole de diagnostics de `tomgo`, exceptions
d'exécution Java en `panic` typées) et `tom/engine/tools` (`outputcode.go`
en Go pur, `astfactory.t`, `symboltable.t`, `tombase.t` en Tom+Go). Chaque
`X.t` est compilé par `src/dist/bin/tom -go -np -I src/go/tom/engine/adt`
en `X.t.go` à côté de lui, ignoré par git dans `src/` et copié dans
l'amorce `stable/go/tom` par `promoteGo`, comme les ADT. L'équivalence
avec Java est vérifiée par deux petits programmes Java de test compilés
contre la dist : l'un rejoue des scripts d'écriture sur `OutputCode`,
l'autre interroge `SymbolTable`/`TomBase` sur les paquets `.tfix.bridge` ;
les programmes Go jumeaux doivent produire les mêmes octets.

**Tech Stack:** Go 1.26+ (module `tom`, `tomtools`), Tom+Go (`tom -go
-np`, mapping `adt.tom` de l'ADT du moteur), Tom+Java (`src/dist`, JDK 8
pour la construire et pour les programmes Java de test), `mage` (`mage -d
magefiles -w . <cible>`).

Spec : `docs/superpowers/specs/2026-10-24-j4-tom-go-compiler-design.md`
(§3.3 taille, §4.3 sources et compilation du Tom+Go, §4.4 structure du
code porté, §5 tests, §6 ligne J4.1a, §8 D4 et D5). Jalon précédent :
`doc/go/j4-0-report.md`, plan `docs/superpowers/plans/2026-10-24-j4-0-bridge.md`.

## Global Constraints

- **Décisions (spec §8)** : D4 `src/dist` compile le Tom+Go (cible
  `regenTomGo`), avec la garde de fraîcheur de la dist ; D5 le côté Go ne
  cible que Go : les branches des autres cibles (`cCode`, `jCode`,
  `aCode`, `pCode`, `camlCode`, `builtinToWrapper`…) sont **omises**,
  avec un commentaire `// Java: … (D5)` à l'endroit omis ; les sources
  Java restent intactes.
- **`src/` ne contient que des sources** : les `X.t.go` générés sont
  ignorés par git dans `src/` et commités dans l'amorce `stable/go/tom`
  (`mage -d magefiles -w . promoteGo`). `mage bootstrap` reste sans Java.
- **`legacy/` n'est jamais modifié.** J4.1a ne modifie pas `src/tom/` (sauf
  correction d'un défaut de `-go`, voir « Risques et conditions
  d'arrêt »).
- **Comptes de référence, inchangés à la fin du jalon** : T0 `102/764`
  (défaut) et `103/765` (`-np`), planchers du test `minDefault = 102`,
  `minNP = 103` ; tableau de bord T2 `77 OK / 11 OK (diagnostics) / 9
  échoue dans legacy / 2 non supporté / 4 support / 0 divergence / 0 non
  porté` ; T3 16 lignes, 0 non couverte ; `bridgeCheck` : 110 paquets,
  `String()` identique `1139 / 1139` ; goldens T1 inchangés.
- Toujours `unset TOM_HOME` avant `./build.sh` ou un `bin/tom`. JDK 8
  pour construire (`java -version` → `1.8.0_…`) ; `javac`/`java` du `PATH`
  (JDK 8) pour les programmes Java de test.
- **Binaires périmés** : toute modification de `src/go/tom/**/*.go` (y
  compris un `X.t.go` régénéré) rend `src/dist/bin/{gomgo,tomgo}` plus
  anciens que leurs sources ; `CheckFresh` fait alors échouer les harnais
  (`mage j4`, `smoke`, `portGo`…). Avant eux : `unset TOM_HOME &&
  ./build.sh src.dist`.
- **Style du port** (spec §4.4) : chaque méthode Java devient une fonction
  ou méthode Go **de même nom** (initiale en capitale), dans le même ordre,
  précédée d'un commentaire `// <Nom> is <nomJava> (<Fichier>:<ligne>)` ;
  un écart voulu est noté `// port: …` ; le code se lit comme le code qui
  l'entoure (Go idiomatique, `gofmt` pour les `.go` écrits à la main).
  Une méthode Java qui rend `null` pour « absent » devient, en Go, un
  résultat `nil` (terme) ou un couple `(valeur, bool)` (chaîne). Les
  exceptions d'exécution (`TomRuntimeException`, `IllegalArgumentException`)
  deviennent `panic(&platform.RuntimeError{…})` ; un `System.out.println`
  de diagnostic n'est **pas** porté (la sortie standard de `tomgo` porte le
  code généré) : `// port:` le signale.
- Les fichiers Tom+Go (`.t`) n'utilisent pas de littéraux chaîne bruts Go
  (`` `…` ``), réservés au backquote Tom, ni de variable de motif dont le
  nom est un mot-clé Go (`type` → `ty`).
- Git : jamais `git add -A` ; indexer des chemins explicites et committer
  avec `git commit --only <chemins>` ; ne pas committer les fichiers non
  suivis connus (`.DS_Store`, `build.xml.bak*`, `tomgo/`) ; ne jamais
  toucher aux entrées de `git stash` ; pas de réécriture d'historique.
  Messages `tom-go J4.1a: <verbe> <objet>`, terminés par la ligne
  `Co-Authored-By` du modèle qui implémente (par exemple
  `Co-Authored-By: Claude Sonnet 5.5 <noreply@anthropic.com>`). Un refus de
  permission : s'arrêter et le signaler.
- **Modèles** (politique de l'utilisateur) : Haiku pour une tâche
  mécanique dont le code est entièrement donné, Sonnet par défaut, Opus
  pour la revue finale du jalon. Les vérifications longues (T0 ≈ 4 min,
  `portGo` ≈ 4 min, `bridgeCheck` ≈ 6 min) sont lancées **une fois par
  tâche** par l'implémenteur, à la fin ; le contrôleur ne les relance qu'à
  la fin du jalon.

## Disposition retenue (et pourquoi)

- **Un paquetage Go par paquetage ou phase Java** (spec §4.3) :
  `src/go/tom/engine/tools` (paquetage `tools`) pour `tom.engine.tools` et
  `src/go/tom/engine/platform` (paquetage `platform`) pour les options et
  les messages ; J4.1b ajoutera `src/go/tom/engine/backend`.
- **`TomBase` va dans `tools`**, comme le dit le spec (§4.3) : en Java,
  `tom.engine.TomBase` et `tom.engine.tools.SymbolTable` dépendent l'un de
  l'autre (`TomBase.getTermType(…, SymbolTable)`), et Go interdit les
  cycles d'import ; un paquetage `engine` à la racine de
  `src/go/tom/engine` mêlerait en outre le code au-dessus de `adt/`,
  `bridge/` et `cmd/`.
- **`platform` est sous `engine/`** (spec §4.3) : il ne porte que ce dont
  le suffixe Go a besoin (options du paquet, messages, diagnostics) et non
  la plateforme de plugins Java, dont l'orchestration Go émergera au fil
  des jalons (spec §4.5). Il est en Go pur : `OptionManager` et
  `TomMessage` n'ont pas de construction Tom dans le sous-ensemble utile
  (spec §4.3 : « Du Go pur … peut voisiner en `.go` »).
- **`OutputCode` est en Go pur** (`outputcode.go`) : l'original est un
  `.java` sans construction Tom.
- **Noms des fichiers générés : `X.t` → `X.t.go`**, à côté du `.t`. Ce
  suffixe permet de mêler dans un paquetage des `.go` écrits à la main
  (commités) et des `.go` générés (ignorés par une seule règle
  `/src/go/tom/**/*.t.go`), et rend le lien source → généré évident pour
  `regentomgo` et `tomGoFresh`. Le préfixe des déclarations générées
  (`tom_<F>_`, J4.0b) vient du nom du `.t` et ne change pas
  (`symboltable.t` → `tom_symboltable_`). Les `.t` sont en minuscules
  (convention Go) ; leur en-tête nomme l'original Java.
- **Programme de régénération dans `tomtools`**
  (`test/tools/j4/regentomgo`) : il réutilise la garde de fraîcheur du
  harnais (`harness.CheckFreshCompiler`) ; `mage regenTomGo` le lance.

## File Structure

| Fichier | Responsabilité | Tâche |
|---|---|---|
| `src/go/tom/engine/platform/{options,messages,report,errors}.go`, `platform_test.go` | **nouveau** — options du paquet, messages, diagnostics, exceptions | 1 |
| `src/go/tom/engine/cmd/tomgo/main.go` | utilise `platform.WriteDiag` | 1 |
| `src/go/tom/engine/tools/doc.go`, `outputcode.go`, `outputcode_test.go` | **nouveau** — paquetage `tools`, `OutputCode` | 2 |
| `test/tools/j4/java/{Quote,StubOptionManager,OutputCodeScript}.java` | **nouveau** — programmes Java de test | 2 |
| `test/tools/j4/javahelpers_test.go`, `outputcode_test.go` | **nouveau** — compilation des programmes Java ; équivalence `OutputCode` | 2 |
| `test/tools/harness/harness.go` | `CheckFreshCompiler` | 3 |
| `test/tools/j4/regentomgo/main.go` | **nouveau** — compilation des `.t` du compilateur | 3 |
| `test/tools/j4/j4_test.go` | `TestTomGoRegen` | 3 |
| `magefiles/magefile.go` | `RegenTomGo`, `TomGoFresh`, copie depuis l'amorce dans `Bootstrap` | 3, 6 |
| `.gitignore` | `/src/go/tom/**/*.t.go` | 3 |
| `src/go/tom/engine/tools/astfactory.t`, `astfactory_test.go` | **nouveau** — `NewType`, `MakeSingleLineCode` | 3 |
| `src/go/tom/engine/tools/symboltable.t`, `symboltable_test.go` | **nouveau** — `SymbolTable` | 4 |
| `src/go/tom/engine/tools/tombase.t`, `tombase_test.go` | **nouveau** — `TomBase` | 5 |
| `test/tools/j4/java/SymbolTableQueries.java`, `test/tools/j4/queries_test.go` | **nouveau** — équivalence des requêtes | 6 |
| `stable/go/tom/**` | amorce (`promoteGo`) | 3, 4, 5, 7 |
| `doc/go/README.md`, `CLAUDE.md`, `doc/go/j4-dashboard.md`, `doc/go/j4-1a-report.md` (**nouveau**) | documentation | 3, 7 |

## Sous-ensemble porté (référence pour J4.1b)

Relevé par `grep` des appels dans les fichiers du chemin Go du backend :
`src/tom/engine/backend/{BackendPlugin,AbstractGenerator,GenericGenerator,GoGenerator}.t`
(la passe `Collector` est dans `BackendPlugin.t:270-600`). Seul ce qui
suit est porté ; le reste de ces classes sert les autres cibles ou les
phases d'avant le backend et sera porté avec elles.

**`OutputCode`** (`src/tom/engine/tools/OutputCode.java`) → `tools.OutputCode`

| Java | Go | Appelants Java |
|---|---|---|
| `new OutputCode(Writer, OptionManager)` (`:49`) | `NewOutputCode(w io.Writer, opts *platform.Options) *OutputCode` | `BackendPlugin.t:120` |
| `new OutputCode(new StringWriter())` (`:60`) | `NewStringOutputCode() *OutputCode` | `GenericGenerator.t:216,249` |
| `write(String)` (`:101`) | `Write(s string)` | environ 160 appels |
| `write(int)` (`:111`) | `WriteInt(n int64)` | `AbstractGenerator.t:266` |
| `write(int deep, String)` (`:115`) | `WriteDeep(deep int, s string)` | `AbstractGenerator.t:154,190`, `GenericGenerator.t:887`, `GoGenerator.t:317,726` |
| `write(int deep, String, int line, int length)` (`:240`) | `WriteTL(deep int, s string, line, length int64)` | `AbstractGenerator.t:600` |
| `writeln()` (`:120`) | `Writeln()` | `AbstractGenerator.t:938` |
| `writeOpenBrace`, `writeCloseBrace`, `writeComa` (`:80-91`) | `WriteOpenBrace`, `WriteCloseBrace`, `WriteComa` | générateurs |
| `setSingleLine`, `unsetSingleLine` (`:64-70`) | `SetSingleLine`, `UnsetSingleLine` | générateurs |
| `close()` (`:299`) | `Close() error` | `BackendPlugin.t:153` |
| `stringDump()` (`:309`) | `StringDump() string` | `GenericGenerator.t:239,251` |

Non portés : `writeSpace`, `writeSemiColon`, `writeUnderscore`, `getFile`,
les surcharges `StringBuilder` (Go n'a que des `string`),
`writeln(String)` et `writeln(int, String)` (internes : `writelnDeep`),
`indent` (sans effet avec `-go` : `indent = pCode || aCode`), branches
`aCode` et `cCode` de `write(int, String, int, int)` (D5).

**`ASTFactory`** (`src/tom/engine/tools/ASTFactory.t`) → `tools`

| Java | Go | Appelants Java |
|---|---|---|
| `makeType(TypeOptionList, String, String)` (`:307`) | `NewType(typeOptions TypeOptionList, tomName, tlName string) TomType` (`adt.MakeType` est déjà le constructeur de l'opérateur `Type`) | `SymbolTable.t:259-368` |
| `makeSingleLineCode(String, boolean)` (`:387`) | `MakeSingleLineCode(code string, pretty bool) string` | `GenericGenerator.t:948` |

**`SymbolTable`** (`src/tom/engine/tools/SymbolTable.t`) → `*tools.SymbolTable`

| Java | Go | Appelants Java |
|---|---|---|
| `new SymbolTable()` + `init(om)` (`:79`), avec `-go` | `NewSymbolTable() *SymbolTable` | (plateforme) |
| — | `FromBundle(b *bridge.Bundle) *SymbolTable` (table, types, marques du paquet) | pilote J4.1b |
| `putSymbol`, `getSymbolFromName` (`:113-119`) | `PutSymbol(name string, s TomSymbol)`, `GetSymbolFromName(name string) TomSymbol` (`nil` si absent) | 31 appels (les 4 fichiers) |
| `putType`, `getType` (`:131`, `:164`) | `PutType(name string, t TomType)`, `GetType(name string) TomType` | `GoGenerator.t` (4), `TomBase.getTermType(Expression)` |
| `keySymbolIterable` (`:484`) | `KeySymbols() []string` (triés) | `toTerm`, tests |
| `fromTerm`, `toTerm` (`:493-509`) | `FromTerm(TomSymbolTable)`, `ToTerm() TomSymbolTable` | `BackendPlugin.t:242` (commentaire), pont |
| `getUsedKeyEntries`, `getTypeEntries` (`:149-162`) | `GetUsedKeyEntries() []KeyEntry`, `GetTypeEntries() []TomType` | pont, tests |
| `isUsedSymbolConstructor/Destructor(TomSymbol)`, `isUsedSymbolAC`, `isUsedType(TomType)` (`:175-189`) | `IsUsedSymbolConstructor(s)`, `IsUsedSymbolDestructor(s)`, `IsUsedSymbolAC(s)`, `IsUsedType(t)` | `BackendPlugin.t:274,284` |
| `setUsedSymbolConstructor/Destructor(TomSymbol)`, `setUsedType(TomType)` (`:191-205`) | `SetUsedSymbolConstructor(s)`, `SetUsedSymbolDestructor(s)`, `SetUsedType(t)` | `BackendPlugin.t:279,289` |
| `setUsedSymbolConstructor/Destructor(String)`, `setUsedType(String)` (`:207-233`) | `SetUsedSymbolConstructorName`, `SetUsedSymbolDestructorName`, `SetUsedTypeName` | `BackendPlugin.t:293`, `Collector` |
| `isUsedSymbolConstructor/Destructor(String)`, `isUsedType(String)` (`:235-257`) | `IsUsedSymbolConstructorName`, `IsUsedSymbolDestructorName`, `IsUsedTypeName` | `AbstractGenerator.t:673-827` (27 appels) |
| `getIntType` … `getVoidType`, `getIntArrayType` (`:259-369`), branche `goCode` seule | `GetIntType()` … `GetVoidType()`, `GetIntArrayType()` | `AbstractGenerator`, `GenericGenerator`, `GoGenerator` |
| `isIntType` … `isUnknownType`, `isBuiltinType`, `getBuiltinType` (`:371-478`) | mêmes noms en capitale | `GenericGenerator`, `GoGenerator` |
| inliners `put…`/`get…` (`:559-697`) | `PutIsFsym`/`GetIsFsym`, `PutIsSort`/`GetIsSort`, `PutResolveGetSlot`/`GetResolveGetSlot`, `PutGetSlot`/`GetGetSlot`, `PutGetHead`/`GetGetHead`, `PutGetTail`/`GetGetTail`, `PutGetElementArray`/`GetGetElementArray`, `PutGetSizeArray`/`GetGetSizeArray`, `PutIsEmptyList`/`GetIsEmptyList`, `PutMake`/`GetMake`, `PutMakeEmptyArray`/`GetMakeEmptyArray`, `PutMakeEmptyList`/`GetMakeEmptyList`, `PutMakeAddArray`/`GetMakeAddArray`, `PutMakeAddList`/`GetMakeAddList`, `PutEqualTerm`/`GetEqualTerm` ; un `get` rend `(code string, ok bool)` (`null` Java → `ok == false` ; `putResolveGetSlot` stocke `""`, qui n'est pas « absent ») | `BackendPlugin.t:444-592` (`put`), `GenericGenerator.t` (`get`) |
| `getIntArrayOp` (`:699`) | `GetIntArrayOp() string` | `GenericGenerator.t` |

Non portés : `getMapSymbolName`, `regenerateFromTerm` (doublon de
`fromTerm`), `getSymbolFromType`, `getUsedTypes`, `setUsedSymbolAC(String)`,
`builtinToWrapper` (Java), `isResolveSymbol` (`%transformation`, refusé
par `-go`), `isNumericType`, `updateConstrainedSymbolCodomain`,
`keySymbolIterator`, et les types des autres cibles (D5).

**`TomBase`** (`src/tom/engine/TomBase.t`) → fonctions de `tools`

| Java | Go | Appelants Java |
|---|---|---|
| `DEFAULT_MODULE_NAME` (`:73`) | `const DefaultModuleName = "default"` | `BackendPlugin.t:149,266` |
| `goDeclPrefix` (`:88`) | `GoDeclPrefix(inputFileName string) string` | `BackendPlugin.t:129` |
| `getTomType` (`:107`) | `GetTomType(t TomType) (string, bool)` (`null` → `false`) | `GenericGenerator`, `GoGenerator.t:514,522` (15) |
| `getTLType`, `getTLCode` (`:118-135`) | `GetTLType(t TomType) string`, `GetTLCode(t TargetLanguageType) string` | `GenericGenerator`, `GoGenerator` (30) |
| `getSymbolCodomain`, `getSymbolDomain` (`:138-156`) | `GetSymbolCodomain(s TomSymbol) TomType`, `GetSymbolDomain(s TomSymbol) TomTypeList` | 17 appels |
| `tomNumberListToString` (`:159`) | `TomNumberListToString(l TomNumberList) string` (sans le cache LRU, optimisation seulement) | `AbstractGenerator.t:219-445` |
| `getModuleName` (`:408`) | `GetModuleName(l OptionList) (string, bool)` | `BackendPlugin.t:305` (`Collector`) |
| `getSlotName`, `getSlotIndex` (`:431-464`) | `GetSlotName(s TomSymbol, number int) TomName`, `GetSlotIndex(s TomSymbol, slotName TomName) int` | `GenericGenerator`, `GoGenerator` |
| `elementAt`, `getSlotType` (`:466-485`) | `ElementAt(l TomTypeList, index int) TomType`, `GetSlotType(s TomSymbol, slotName TomName) TomType` | via `getTermType(BQTerm)` |
| `getSymbolFromName(String, SymbolTable)` (`:536`) | `GetSymbolFromName(name string, st *SymbolTable) TomSymbol` | `BackendPlugin`, générateurs |
| `getTermType(TomTerm/BQTerm/Expression, SymbolTable)` (`:540-619`) | `GetTermType(t TomTerm, st)`, `GetTermTypeBQ(t BQTerm, st)`, `GetTermTypeExpr(t Expression, st)` | `AbstractGenerator.t:78-83`, `GoGenerator.t:514,522` |

**Options** (`TomGenericPlugin.getOption{Boolean,Integer,String}Value`,
`TomOptionManager.getOptionValue`) → `platform.Options` (`Bool`, `Int`,
`String`, `Has`). Noms lus par le chemin Go du backend : `goCode`, `pretty`,
`noDeclaration`, `inline`, `inlineplus` (sans effet avec `-go` :
`GenericGenerator.t:72-74` fait `inline &= (cCode || jCode)`), `cCode`,
`aCode`, `pCode` (faux avec `-go`, vérifiés par `NewOutputCode`),
`encoding`, `noOutput`, `verbose`.

**Messages** (`TomMessage`, `PluginPlatformMessage`) → `platform.Message`
et `platform.Reporter` : `backendInactivated`, `backendIOException`,
`exceptionMessage`, `tomGenerationPhase`, `platformStopped`,
`optionNotFound` ; niveaux `ERROR`, `WARNING` (relayés par
`GoBridgePlugin`) et `INFO` (seulement avec `verbose` ; pas encore relayé :
J4.1b).

**Exceptions** (spec §4.4) : `platform.RuntimeError` (valeur de `panic`),
`platform.CatchRuntime` (le `catch(RuntimeException)` de
`GoGenerator.termType`, `GoGenerator.t:512-526`), `platform.Run` (point de
reprise unique du pilote : `BackendPlugin.t:159-166`).

---

### Task 1 : paquetage `tom/engine/platform` (options, messages, diagnostics, exceptions)

**Modèle : Haiku** (code entièrement donné, Go pur, sans traduction de
Tom).

**Files:**
- Create: `src/go/tom/engine/platform/options.go`, `messages.go`, `report.go`, `errors.go`, `platform_test.go`
- Modify: `src/go/tom/engine/cmd/tomgo/main.go`

**Interfaces:**
- Consumes: `tom/engine/bridge` (`bridge.Option{Name, Kind, Value string}`), `tom/library/aterm` (`*aterm.GomError`).
- Produces:
  ```go
  type Options struct{ /* unexported */ }
  func NewOptions(opts []bridge.Option) *Options
  func (o *Options) Has(name string) bool
  func (o *Options) Bool(name string) bool
  func (o *Options) Int(name string) int
  func (o *Options) String(name string) string
  type Message struct{ Name, Pattern string }
  func (m Message) Text(args ...any) string
  var BackendInactivated, BackendIOException, ExceptionMessage, OptionNotFound, TomGenerationPhase, PlatformStopped Message
  func WriteDiag(w io.Writer, level, file string, line int, msg string)
  type Reporter struct{ /* unexported */ }
  func NewReporter(w io.Writer, verbose bool) *Reporter
  func (r *Reporter) Error(file string, line int, m Message, args ...any)
  func (r *Reporter) Warning(file string, line int, m Message, args ...any)
  func (r *Reporter) Info(file string, line int, m Message, args ...any)
  func (r *Reporter) HasError() bool
  func (r *Reporter) Counts() (errors, warnings int)
  type RuntimeError struct{ Msg string }
  func IsRuntime(v any) bool
  func CatchRuntime(f func()) (ok bool)
  func Run(r *Reporter, file string, f func()) (ok bool)
  func JavaString(v any) string
  ```
  (La méthode s'appelle `Text` et non `Format` : `go vet` réserve `Format`
  à la signature de `fmt.Formatter`.)

- [ ] **Step 1 : tests (TDD)**

`src/go/tom/engine/platform/platform_test.go` :

```go
package platform

import (
	"errors"
	"strings"
	"testing"

	"tom/engine/bridge"
	"tom/library/aterm"
)

func opts() *Options {
	return NewOptions([]bridge.Option{
		{Name: "encoding", Kind: "string", Value: "UTF-8"},
		{Name: "optLevel", Kind: "int", Value: "3"},
		{Name: "pretty", Kind: "bool", Value: "true"},
		{Name: "wall", Kind: "bool", Value: "false"},
	})
}

// panicMsg runs f and returns the message of the *RuntimeError it panics with.
func panicMsg(t *testing.T, f func()) string {
	t.Helper()
	var msg string
	func() {
		defer func() {
			v := recover()
			e, ok := v.(*RuntimeError)
			if !ok {
				t.Fatalf("panic %v (%T), want a *RuntimeError", v, v)
			}
			msg = e.Msg
		}()
		f()
	}()
	return msg
}

func TestOptions(t *testing.T) {
	o := opts()
	if !o.Bool("pretty") || o.Bool("wall") || o.Int("optLevel") != 3 || o.String("encoding") != "UTF-8" {
		t.Fatal("values")
	}
	if !o.Has("pretty") || o.Has("nope") {
		t.Fatal("Has")
	}
	if got := panicMsg(t, func() { o.Bool("nope") }); got != "TomOptionManager: Option 'nope' not found" {
		t.Fatalf("unknown option: %q", got)
	}
	if got := panicMsg(t, func() { o.Int("pretty") }); got != "option pretty is a bool, not a int" {
		t.Fatalf("kind: %q", got)
	}
}

func TestMessageText(t *testing.T) {
	cases := []struct {
		m    Message
		args []any
		want string
	}{
		{BackendIOException, []any{"a.go", "disk full"}, "BackendPlugin : IOException occurs writting to file 'a.go': disk full "},
		{TomGenerationPhase, []any{1234567}, "TOM generation phase (1,234,567 ms)"},
		{TomGenerationPhase, []any{int64(-12)}, "TOM generation phase (-12 ms)"},
		{ExceptionMessage, []any{"X.t"}, "X.t: Unhandled exception\n"},
		{OptionNotFound, []any{nil}, "TomOptionManager: Option 'null' not found"},
		{Message{"q", "'{0}' is {0}, {1}"}, []any{"x"}, "{0} is x, {1}"},
		{PlatformStopped, nil, "Platform stopped"},
	}
	for _, c := range cases {
		if got := c.m.Text(c.args...); got != c.want {
			t.Errorf("%s: %q, want %q", c.m.Name, got, c.want)
		}
	}
}

func TestReporter(t *testing.T) {
	var b strings.Builder
	r := NewReporter(&b, false)
	r.Error("X.t", 3, BackendIOException, "a\tb", "c\nd")
	r.Warning("X.t", 0, PlatformStopped)
	r.Info("X.t", 0, TomGenerationPhase, 5) // not verbose: nothing
	want := "ERROR\tX.t\t3\tBackendPlugin : IOException occurs writting to file 'a b': c d \n" +
		"WARNING\tX.t\t0\tPlatform stopped\n"
	if b.String() != want {
		t.Fatalf("got %q, want %q", b.String(), want)
	}
	if e, w := r.Counts(); e != 1 || w != 1 || !r.HasError() {
		t.Fatalf("counts %d %d", e, w)
	}
	b.Reset()
	NewReporter(&b, true).Info("X.t", 0, TomGenerationPhase, 5)
	if b.String() != "INFO\tX.t\t0\tTOM generation phase (5 ms)\n" {
		t.Fatalf("info: %q", b.String())
	}
}

func TestCatchRuntime(t *testing.T) {
	if !CatchRuntime(func() {}) {
		t.Fatal("no panic")
	}
	var nilMap map[string]*int
	for name, f := range map[string]func(){
		"RuntimeError": func() { panic(&RuntimeError{Msg: "x"}) },
		"GomError":     func() { panic(&aterm.GomError{Msg: "This TomName has no String"}) },
		"nil deref":    func() { _ = *nilMap["a"] },
		"assertion":    func() { var v any = 1; _ = v.(string) },
	} {
		if CatchRuntime(f) {
			t.Errorf("%s: ok, want caught", name)
		}
	}
	defer func() {
		if v := recover(); v == nil || !errors.Is(v.(error), errOther) {
			t.Fatalf("other panic: %v", v)
		}
	}()
	CatchRuntime(func() { panic(errOther) })
}

var errOther = errors.New("other")

func TestRun(t *testing.T) {
	var b strings.Builder
	r := NewReporter(&b, false)
	if Run(r, "X.t", func() { panic(&RuntimeError{Msg: "boom"}) }) {
		t.Fatal("Run ok")
	}
	if b.String() != "ERROR\tX.t\t-1\tX.t: Unhandled exception \ntomgo: boom\n" || !r.HasError() {
		t.Fatalf("got %q", b.String())
	}
	if !Run(r, "X.t", func() {}) {
		t.Fatal("Run without panic")
	}
}

func TestJavaString(t *testing.T) {
	if JavaString(nil) != "null" || JavaString(3) != "3" || JavaString("s") != "s" {
		t.Fatal("JavaString")
	}
}
```

Run: `cd /Users/pem/github/tom/src/go/tom && go test -count=1 ./engine/platform/`
Expected: FAIL (paquetage vide : `undefined: NewOptions`…).

- [ ] **Step 2 : implémentation**

`src/go/tom/engine/platform/options.go` :

```go
// Package platform holds what the Go suffix of the compiler takes from the
// Java platform (tom.platform) and from tom.engine.TomMessage (spec J4
// §4.4; plan J4.1a): the options of the compilation, read from the bridge
// bundle; the messages and the diagnostics protocol GoBridgePlugin relays;
// the Go form of Java's runtime exceptions.
package platform

import (
	"strconv"

	"tom/engine/bridge"
)

// Options are the options of the compilation as the bridge bundle carries
// them (spec J4 §4.2: every option of TomOptionManager, by its canonical
// name). Java: TomOptionManager.getOptionValue (TomOptionManager.t:184)
// and the getOption{Boolean,Integer,String}Value of TomGenericPlugin
// (TomGenericPlugin.t:258-280).
type Options struct {
	byName map[string]bridge.Option
}

// NewOptions indexes the options of a bundle.
func NewOptions(opts []bridge.Option) *Options {
	o := &Options{byName: make(map[string]bridge.Option, len(opts))}
	for _, x := range opts {
		o.byName[x.Name] = x
	}
	return o
}

// Has reports whether the option exists.
func (o *Options) Has(name string) bool {
	_, ok := o.byName[name]
	return ok
}

// value is the value of an option of the given kind. An unknown option is
// Java's optionNotFound error followed by a RuntimeException
// (TomOptionManager.t:286-292), a kind mismatch Java's ClassCastException:
// both are runtime exceptions here.
func (o *Options) value(name, kind string) string {
	x, ok := o.byName[name]
	if !ok {
		panic(&RuntimeError{Msg: OptionNotFound.Text(name)})
	}
	if x.Kind != kind {
		panic(&RuntimeError{Msg: "option " + name + " is a " + x.Kind + ", not a " + kind})
	}
	return x.Value
}

// Bool is getOptionBooleanValue.
func (o *Options) Bool(name string) bool { return o.value(name, "bool") == "true" }

// Int is getOptionIntegerValue.
func (o *Options) Int(name string) int {
	n, err := strconv.Atoi(o.value(name, "int"))
	if err != nil {
		panic(&RuntimeError{Msg: "option " + name + ": " + err.Error()})
	}
	return n
}

// String is getOptionStringValue.
func (o *Options) String(name string) string { return o.value(name, "string") }
```

`src/go/tom/engine/platform/messages.go` :

```go
package platform

import (
	"fmt"
	"strconv"
	"strings"
)

// Message is a TomMessage (or a PluginPlatformMessage): the name of its
// Java field and its java.text.MessageFormat pattern.
type Message struct {
	Name    string
	Pattern string
}

// The messages of the backend path (J4.1b) and of the options.
var (
	// BackendInactivated is TomMessage.backendInactivated (TomMessage.java:206).
	BackendInactivated = Message{"backendInactivated", "The BackendPlugin is not activated and thus WILL NOT RUN:No output will be generated "}
	// BackendIOException is TomMessage.backendIOException (TomMessage.java:208).
	BackendIOException = Message{"backendIOException", "BackendPlugin : IOException occurs writting to file ''{0}'': {1} "}
	// ExceptionMessage is TomMessage.exceptionMessage (TomMessage.java:72).
	ExceptionMessage = Message{"exceptionMessage", "{0}: Unhandled exception\n"}
	// OptionNotFound is TomMessage.optionNotFound (TomMessage.java:80).
	OptionNotFound = Message{"optionNotFound", "TomOptionManager: Option ''{0}'' not found"}
	// TomGenerationPhase is TomMessage.tomGenerationPhase (TomMessage.java:298).
	TomGenerationPhase = Message{"tomGenerationPhase", "TOM generation phase ({0,number,integer} ms)"}
	// PlatformStopped is PluginPlatformMessage.platformStopped (PluginPlatformMessage.java:91).
	PlatformStopped = Message{"platformStopped", "Platform stopped"}
)

// Text formats the message as java.text.MessageFormat.format does for the
// patterns of the compiler: {n} and {n,number,integer} arguments ("{n}"
// stays when the argument is missing), '' for a quote, '…' for quoted
// text. port: a number is written with the grouping separator ',' (Java
// uses the default locale's; only INFO messages show numbers).
func (m Message) Text(args ...any) string {
	var b strings.Builder
	p := m.Pattern
	for i := 0; i < len(p); i++ {
		switch c := p[i]; c {
		case '\'':
			if i+1 < len(p) && p[i+1] == '\'' {
				b.WriteByte('\'')
				i++
				continue
			}
			j := strings.IndexByte(p[i+1:], '\'')
			if j < 0 {
				b.WriteString(p[i+1:])
				return b.String()
			}
			b.WriteString(p[i+1 : i+1+j])
			i += j + 1
		case '{':
			j := strings.IndexByte(p[i:], '}')
			if j < 0 {
				panic(&RuntimeError{Msg: "unmatched braces in the pattern of " + m.Name})
			}
			spec := strings.Split(p[i+1:i+j], ",")
			n, err := strconv.Atoi(strings.TrimSpace(spec[0]))
			if err != nil {
				panic(&RuntimeError{Msg: "bad argument number in the pattern of " + m.Name})
			}
			if n < len(args) {
				b.WriteString(formatArg(args[n]))
			} else {
				b.WriteString("{" + spec[0] + "}")
			}
			i += j
		default:
			b.WriteByte(c)
		}
	}
	return b.String()
}

// formatArg is MessageFormat's text of an argument: "null" for nil, a
// grouped number for an integer, the string itself, fmt.Sprint otherwise.
func formatArg(a any) string {
	switch v := a.(type) {
	case nil:
		return "null"
	case int:
		return groupDigits(strconv.FormatInt(int64(v), 10))
	case int64:
		return groupDigits(strconv.FormatInt(v, 10))
	case string:
		return v
	default:
		return fmt.Sprint(v)
	}
}

// groupDigits inserts ',' every three digits from the right ("-1234567" →
// "-1,234,567").
func groupDigits(s string) string {
	sign := ""
	if strings.HasPrefix(s, "-") {
		sign, s = "-", s[1:]
	}
	var b strings.Builder
	for i := 0; i < len(s); i++ {
		if i > 0 && (len(s)-i)%3 == 0 {
			b.WriteByte(',')
		}
		b.WriteByte(s[i])
	}
	return sign + b.String()
}
```

`src/go/tom/engine/platform/report.go` :

```go
package platform

import (
	"fmt"
	"io"
	"strings"
)

// WriteDiag writes one diagnostic line, the protocol GoBridgePlugin reads
// on the standard error of tomgo: LEVEL TAB file TAB line TAB message; the
// tabs and line breaks of the file and of the message become spaces.
func WriteDiag(w io.Writer, level, file string, line int, msg string) {
	clean := strings.NewReplacer("\t", " ", "\r", " ", "\n", " ")
	fmt.Fprintf(w, "%s\t%s\t%d\t%s\n", level, clean.Replace(file), line, clean.Replace(msg))
}

// Reporter writes the diagnostics of the Go suffix (Java: TomMessage.error,
// warning and info, through the logger and the status handler of the
// platform) and counts them.
type Reporter struct {
	w        io.Writer
	verbose  bool
	errors   int
	warnings int
}

// NewReporter writes on w (tomgo: its standard error); Info writes only
// when verbose (the option verbose).
func NewReporter(w io.Writer, verbose bool) *Reporter {
	return &Reporter{w: w, verbose: verbose}
}

// Error is TomMessage.error.
func (r *Reporter) Error(file string, line int, m Message, args ...any) {
	r.errors++
	WriteDiag(r.w, "ERROR", file, line, m.Text(args...))
}

// Warning is TomMessage.warning.
func (r *Reporter) Warning(file string, line int, m Message, args ...any) {
	r.warnings++
	WriteDiag(r.w, "WARNING", file, line, m.Text(args...))
}

// Info is TomMessage.info, shown with --verbose only. port: GoBridgePlugin
// does not relay INFO lines yet (J4.1b).
func (r *Reporter) Info(file string, line int, m Message, args ...any) {
	if r.verbose {
		WriteDiag(r.w, "INFO", file, line, m.Text(args...))
	}
}

// HasError is the status handler's hasError.
func (r *Reporter) HasError() bool { return r.errors > 0 }

// Counts returns the numbers of errors and warnings written.
func (r *Reporter) Counts() (errors, warnings int) { return r.errors, r.warnings }
```

`src/go/tom/engine/platform/errors.go` :

```go
package platform

import (
	"fmt"
	"runtime"

	"tom/library/aterm"
)

// RuntimeError is the panic value of Java's TomRuntimeException and of the
// other runtime exceptions the compiler throws on purpose
// (IllegalArgumentException, InternalError): spec J4 §4.4.
type RuntimeError struct{ Msg string }

func (e *RuntimeError) Error() string { return e.Msg }

// IsRuntime reports whether a recovered panic value is one Java's
// catch(RuntimeException) would catch: a *RuntimeError, a *aterm.GomError
// (the getters and casts of the Gom terms: UnsupportedOperationException,
// IllegalArgumentException) or a runtime.Error (a nil dereference:
// NullPointerException; a failed type assertion: ClassCastException; an
// index out of range).
func IsRuntime(v any) bool {
	switch v.(type) {
	case *RuntimeError, *aterm.GomError, runtime.Error:
		return true
	}
	return false
}

// CatchRuntime runs f and reports whether it returned normally: a runtime
// exception (IsRuntime) is recovered and gives false; any other panic goes
// on. Java: try { f } catch(RuntimeException e) { } (GoGenerator.t:512-526).
func CatchRuntime(f func()) (ok bool) {
	defer func() {
		if v := recover(); v != nil {
			if !IsRuntime(v) {
				panic(v)
			}
			ok = false
		}
	}()
	f()
	return true
}

// Run is the single recovery point of the driver (spec J4 §4.4): it runs
// f; a runtime exception becomes the ERROR exceptionMessage at line -1, as
// in BackendPlugin.run (BackendPlugin.t:159-166), followed by the panic
// text on a plain line (Java: printStackTrace), and Run returns false.
func Run(r *Reporter, file string, f func()) (ok bool) {
	defer func() {
		if v := recover(); v != nil {
			if !IsRuntime(v) {
				panic(v)
			}
			r.Error(file, -1, ExceptionMessage, file)
			fmt.Fprintf(r.w, "tomgo: %v\n", v)
			ok = false
		}
	}()
	f()
	return true
}

// JavaString is String.valueOf(o) for the values of the compiler: "null"
// for nil, fmt.Sprint otherwise (a Gom term's String() is Java's
// toString()).
func JavaString(v any) string {
	if v == nil {
		return "null"
	}
	return fmt.Sprint(v)
}
```

Dans `src/go/tom/engine/cmd/tomgo/main.go` : supprimer la fonction `diag`
et son commentaire, importer `tom/engine/platform`, et remplacer chaque
appel `diag(` par `platform.WriteDiag(` (mêmes arguments). Retirer
l'import `strings` s'il n'est plus utilisé.

- [ ] **Step 3 : vérification**

Run: `cd /Users/pem/github/tom/src/go/tom && gofmt -l engine/ ; go vet ./engine/... && go test -count=1 ./engine/...`
Expected: `gofmt -l` n'affiche rien ; `ok` pour `engine/platform`, `engine/bridge`, `engine/cmd/tomgo`.

Run (une fois) : `cd /Users/pem/github/tom && unset TOM_HOME && ./build.sh src.dist 2>&1 | tail -1 && mage -d magefiles -w . all && mage -d magefiles -w . j4`
Expected: `BUILD SUCCESSFUL` ; tout vert (`TestGoFrom` relaie toujours les diagnostics de `tomgo`).

- [ ] **Step 4 : commit**

```bash
P="src/go/tom/engine/platform src/go/tom/engine/cmd/tomgo/main.go"
git add $P
git commit --only $P -m "tom-go J4.1a: tom/engine/platform (options, messages, diagnostics, runtime exceptions)" -m "Co-Authored-By: Claude Haiku 5.5 <noreply@anthropic.com>"
```

**Acceptance:** tests verts ; `tomgo` écrit ses diagnostics par
`platform.WriteDiag` ; `mage all` et `mage j4` verts.

---

### Task 2 : `OutputCode` en Go, identique à Java sur des scripts d'écriture

**Modèle : Sonnet** (code donné, mais sémantique fine de la
synchronisation des lignes et des points-virgules Go à vérifier contre
Java).

**Files:**
- Create: `src/go/tom/engine/tools/doc.go`, `src/go/tom/engine/tools/outputcode.go`, `src/go/tom/engine/tools/outputcode_test.go`
- Create: `test/tools/j4/java/Quote.java`, `test/tools/j4/java/StubOptionManager.java`, `test/tools/j4/java/OutputCodeScript.java`
- Create: `test/tools/j4/javahelpers_test.go`, `test/tools/j4/outputcode_test.go`

**Interfaces:**
- Consumes: `platform.Options`, `platform.RuntimeError` (Task 1) ; `bridge.Option`, `bridge.JavaQuote`.
- Produces: `tools.OutputCode` et ses méthodes (table « Sous-ensemble porté ») ; dans `test/tools/j4` : `func javaHelpers(t *testing.T, dist harness.Dist) string` (classpath des programmes Java de test, réutilisé en Task 6) ; programmes Java `Quote` (`quote`/`unquote`, même format que `bridge.JavaQuote`) et `StubOptionManager` (`set(name, value)`).

- [ ] **Step 1 : tests unitaires (TDD)**

`src/go/tom/engine/tools/outputcode_test.go` :

```go
package tools

import (
	"bufio"
	"errors"
	"strings"
	"testing"

	"tom/engine/bridge"
	"tom/engine/platform"
)

// goOptions are the options of a -go compilation, pretty or not.
func goOptions(pretty bool) *platform.Options {
	p := "false"
	if pretty {
		p = "true"
	}
	return platform.NewOptions([]bridge.Option{
		{Name: "aCode", Kind: "bool", Value: "false"},
		{Name: "cCode", Kind: "bool", Value: "false"},
		{Name: "goCode", Kind: "bool", Value: "true"},
		{Name: "pCode", Kind: "bool", Value: "false"},
		{Name: "pretty", Kind: "bool", Value: p},
	})
}

func newGo(pretty bool) (*OutputCode, *strings.Builder) {
	var sb strings.Builder
	return NewOutputCode(&sb, goOptions(pretty)), &sb
}

func TestWriteTLLineSync(t *testing.T) {
	o, sb := newGo(false)
	o.WriteTL(0, "f()", 3, 0)                // padded to line 3
	o.WriteTL(0, "a := 1\nreturn a\n", 3, 2) // at its line
	o.WriteTL(0, "x\ny", 4, 1)               // line 4 is behind: single line
	if want := "\n\nf()a := 1\nreturn a\nx; y"; sb.String() != want {
		t.Fatalf("got %q, want %q", sb.String(), want)
	}
}

func TestSingleLineContinuesOutputLine(t *testing.T) {
	o, sb := newGo(false)
	o.Write("f(x)")
	o.SetSingleLine()
	o.WriteTL(0, "\ny", 1, 1)
	o.UnsetSingleLine()
	if want := "f(x); y"; sb.String() != want {
		t.Fatalf("got %q, want %q", sb.String(), want)
	}
	o, sb = newGo(false)
	o.SetSingleLine()
	o.WriteTL(0, "a\tb\r\nc", 1, 1)
	if want := "a b ; c"; sb.String() != want {
		t.Fatalf("got %q, want %q", sb.String(), want)
	}
}

func TestGoSingleLine(t *testing.T) {
	cases := []struct{ before, s, want string }{
		{"", "x\ny", "x; y"},
		{"", "return\nx", "return; x"},
		{"", "else\nx", "else x"},
		{"", "go\nx", "go x"},
		{"", "{\nx", "{ x"},
		{"", "a++\nb", "a++; b"},
		{"", "a\n;b", "a ;b"},
		{"", "a\n\n;b", "a  ;b"},
		{"", "s := \"x\"\nt", "s := \"x\"; t"},
		{"", "m[i]\n}", "m[i]; }"},
		{"", "x  \t\ny", "x  \t; y"},
		{"", "case 1:\nx", "case 1: x"},
		{"", "x\n", "x; "},
		{"", "été\nx", "été; x"},
		{"return a +", "\nb", " b"},
		{"f(x)", "\ny", "; y"},
	}
	for _, c := range cases {
		if got := goSingleLine(c.before, c.s); got != c.want {
			t.Errorf("goSingleLine(%q, %q) = %q, want %q", c.before, c.s, got, c.want)
		}
	}
}

func TestStringOutputCode(t *testing.T) {
	o := NewStringOutputCode()
	o.WriteTL(0, "x\ny", 0, 1) // no goCode: newlines become spaces
	o.WriteInt(-42)
	o.WriteOpenBrace()
	o.WriteComa()
	o.WriteCloseBrace()
	o.WriteDeep(3, "z")
	o.Writeln() // not pretty: nothing
	if got := o.StringDump(); got != "x y-42(,)z" {
		t.Fatalf("got %q", got)
	}
}

func TestPretty(t *testing.T) {
	for _, c := range []struct{ s, want string }{
		{"  a\n\n  b\n\n", "a\n\nb\n"},
		{"\n\n", ""},
		{"", "\n"},
	} {
		o, sb := newGo(true)
		o.WriteTL(2, c.s, 1, int64(strings.Count(c.s, "\n")))
		if sb.String() != c.want {
			t.Errorf("WriteTL(%q) = %q, want %q", c.s, sb.String(), c.want)
		}
	}
	o, sb := newGo(true)
	o.Writeln()
	if sb.String() != "\n" {
		t.Fatalf("Writeln = %q", sb.String())
	}
}

type failingWriter struct{ n int }

func (w *failingWriter) Write(p []byte) (int, error) {
	w.n++
	return 0, errors.New("disk full")
}

func TestCloseAndErrors(t *testing.T) {
	var sb strings.Builder
	bw := bufio.NewWriter(&sb)
	o := NewOutputCode(bw, goOptions(false))
	o.Write("abc")
	if err := o.Close(); err != nil || sb.String() != "abc" {
		t.Fatalf("Close: %v, %q", err, sb.String())
	}
	fw := &failingWriter{}
	o = NewOutputCode(fw, goOptions(false))
	o.Write("a")
	o.Write("b")
	if err := o.Close(); err == nil || err.Error() != "disk full" || fw.n != 1 {
		t.Fatalf("Close: %v after %d writes", err, fw.n)
	}
	defer func() {
		if _, ok := recover().(*platform.RuntimeError); !ok {
			t.Fatal("StringDump of a non-string OutputCode must panic with a *platform.RuntimeError")
		}
	}()
	o.StringDump()
}
```

Run: `cd /Users/pem/github/tom/src/go/tom && go test -count=1 ./engine/tools/`
Expected: FAIL (`undefined: NewOutputCode`…).

- [ ] **Step 2 : implémentation**

`src/go/tom/engine/tools/doc.go` :

```go
// Package tools is the Go port of the subset of tom.engine.tools and of
// tom.engine.TomBase the backend uses (spec J4 §4.3; plan J4.1a):
// OutputCode (outputcode.go), ASTFactory (astfactory.t), SymbolTable
// (symboltable.t) and TomBase (tombase.t). A .t file is Tom+Go, compiled by
// mage regenTomGo into X.t.go (gitignored in src/, kept in the amorce
// stable/go/tom). Only the Go target is ported (D5).
package tools
```

`src/go/tom/engine/tools/outputcode.go` :

```go
package tools

import (
	"io"
	"strconv"
	"strings"
	"unicode"
	"unicode/utf8"

	"tom/engine/platform"
)

// OutputCode is tom.engine.tools.OutputCode (OutputCode.java) for the Go
// target (D5): the indentation of the C, Ada and Python targets (indent =
// pCode || aCode) and the #line directives of C are not ported, -go turns
// those options off.
type OutputCode struct {
	w           io.Writer
	sb          *strings.Builder // the StringWriter of NewStringOutputCode
	err         error            // the first write error
	lineCounter int64
	singleLine  int
	pretty      bool
	goCode      bool
	lineTail    string // Go only: the end of the output line being written (see goSingleLine)
}

// NewOutputCode is new OutputCode(file, optionManager) (OutputCode.java:49).
func NewOutputCode(w io.Writer, opts *platform.Options) *OutputCode {
	if opts.Bool("cCode") || opts.Bool("aCode") || opts.Bool("pCode") {
		panic(&platform.RuntimeError{Msg: "OutputCode: the Go suffix generates Go only (cCode, aCode or pCode is set)"})
	}
	return &OutputCode{w: w, lineCounter: 1, pretty: opts.Bool("pretty"), goCode: opts.Bool("goCode")}
}

// NewStringOutputCode is new OutputCode(new StringWriter())
// (OutputCode.java:60): no option is read, so neither pretty nor goCode.
func NewStringOutputCode() *OutputCode {
	sb := &strings.Builder{}
	return &OutputCode{w: sb, sb: sb, lineCounter: 1}
}

// SetSingleLine is setSingleLine (OutputCode.java:64).
func (o *OutputCode) SetSingleLine() { o.singleLine++ }

// UnsetSingleLine is unsetSingleLine (OutputCode.java:68).
func (o *OutputCode) UnsetSingleLine() { o.singleLine-- }

// raw writes s; port: Java prints "write error" and goes on, here the
// first error is kept for Close and the next writes are dropped.
func (o *OutputCode) raw(s string) {
	if o.err != nil {
		return
	}
	if _, err := io.WriteString(o.w, s); err != nil {
		o.err = err
	}
}

// WriteOpenBrace is writeOpenBrace (OutputCode.java:80).
func (o *OutputCode) WriteOpenBrace() { o.Write("(") }

// WriteCloseBrace is writeCloseBrace (OutputCode.java:84).
func (o *OutputCode) WriteCloseBrace() { o.Write(")") }

// WriteComa is writeComa (OutputCode.java:88).
func (o *OutputCode) WriteComa() { o.Write(",") }

// Write is write(String) (OutputCode.java:101).
func (o *OutputCode) Write(s string) {
	o.raw(s)
	o.track(s)
}

// WriteInt is write(int) (OutputCode.java:111).
func (o *OutputCode) WriteInt(n int64) { o.Write(strconv.FormatInt(n, 10)) }

// WriteDeep is write(int deep, String) (OutputCode.java:115); indent(deep)
// writes nothing with -go (indent = pCode || aCode).
func (o *OutputCode) WriteDeep(deep int, s string) { o.Write(s) }

// Writeln is writeln() (OutputCode.java:120): a newline in pretty mode,
// not tracked (Java writes it on the file directly).
func (o *OutputCode) Writeln() {
	if o.pretty {
		o.raw("\n")
	}
}

// writelnDeep is writeln(int deep, String) (OutputCode.java:131).
func (o *OutputCode) writelnDeep(deep int, s string) {
	o.WriteDeep(deep, s)
	o.Writeln()
}

// makeSingleLine is makeSingleLine (OutputCode.java:156).
func (o *OutputCode) makeSingleLine(s string) string {
	if o.goCode {
		s = goSingleLine(o.lineTail, s)
	}
	return strings.NewReplacer("\n", " ", "\r", " ", "\t", " ").Replace(s)
}

// goKeywords is GO_KEYWORDS (OutputCode.java:166): the keywords after
// which Go inserts no semicolon at the end of a line.
var goKeywords = map[string]bool{
	"case": true, "chan": true, "const": true, "default": true, "defer": true,
	"else": true, "for": true, "func": true, "go": true, "goto": true, "if": true,
	"import": true, "interface": true, "map": true, "package": true, "range": true,
	"select": true, "struct": true, "switch": true, "type": true, "var": true,
}

// goSingleLine is goSingleLine (OutputCode.java:182): Go code put on a
// single line keeps the semicolons Go inserts at the end of a line: a
// newline becomes "; " where the Go lexer would insert one, and a space
// elsewhere; the first line of s continues the output line written so
// far (before); no "; " is written when the next non-blank line starts
// with an explicit ';'.
func goSingleLine(before, s string) string {
	var res strings.Builder
	lines := strings.Split(s, "\n") // Java: s.split("\n", -1)
	for i, ln := range lines {
		res.WriteString(ln)
		if i < len(lines)-1 {
			line := ln
			if i == 0 {
				line = before + ln
			}
			semicolon := goInsertsSemicolon(line)
			if semicolon {
				for _, next := range lines[i+1:] {
					if n := javaTrim(next); n != "" {
						semicolon = n[0] != ';'
						break
					}
				}
			}
			if semicolon {
				res.WriteString("; ")
			} else {
				res.WriteString(" ")
			}
		}
	}
	return res.String()
}

// track is track (OutputCode.java:209): lineTail is the end of the output
// line written so far. port: Java keeps its last 256 UTF-16 chars, this
// keeps the last 256 runes (the same on BMP text).
func (o *OutputCode) track(s string) {
	if !o.goCode {
		return
	}
	var t string
	if nl := strings.LastIndexByte(s, '\n'); nl >= 0 {
		t = s[nl+1:]
	} else {
		t = o.lineTail + s
	}
	if n := utf8.RuneCountInString(t); n > 256 {
		t = string([]rune(t)[n-256:])
	}
	o.lineTail = t
}

// javaSpace is the class \s of java.util.regex: [ \t\n\x0B\f\r].
const javaSpace = " \t\n\x0B\f\r"

// javaTrim is String.trim: it removes the characters <= ' ' at both ends.
func javaTrim(s string) string {
	return strings.TrimFunc(s, func(r rune) bool { return r <= ' ' })
}

// isJavaLetterOrDigit is Character.isLetterOrDigit on one UTF-16 char: a
// rune outside the BMP (a surrogate pair in Java) is neither.
func isJavaLetterOrDigit(r rune) bool {
	return r <= 0xFFFF && (unicode.IsLetter(r) || unicode.IsDigit(r))
}

// goInsertsSemicolon is goInsertsSemicolon (OutputCode.java:218): whether
// the Go lexer inserts a semicolon at the end of line (after an
// identifier that is not one of goKeywords, a literal, ++, --, or a
// closing bracket).
func goInsertsSemicolon(line string) bool {
	l := strings.TrimRight(line, javaSpace)
	if l == "" {
		return false
	}
	r, _ := utf8.DecodeLastRuneInString(l)
	switch r {
	case ')', ']', '}', '"', '\'', '`':
		return true
	}
	if strings.HasSuffix(l, "++") || strings.HasSuffix(l, "--") {
		return true
	}
	if isJavaLetterOrDigit(r) || r == '_' {
		i := len(l)
		for i > 0 {
			c, size := utf8.DecodeLastRuneInString(l[:i])
			if !isJavaLetterOrDigit(c) && c != '_' {
				break
			}
			i -= size
		}
		return !goKeywords[l[i:]]
	}
	return false
}

// javaSplit is s.split("\n") of Java: the trailing empty strings are
// removed, and "" gives [""].
func javaSplit(s string) []string {
	if s == "" {
		return []string{""}
	}
	parts := strings.Split(s, "\n")
	for len(parts) > 0 && parts[len(parts)-1] == "" {
		parts = parts[:len(parts)-1]
	}
	return parts
}

// WriteTL is write(int deep, String s, int line, int length)
// (OutputCode.java:240), the branches of the Go target: without pretty,
// host code is written at its source line (newlines are added up to
// line) or, when that line is behind or single-line mode is on, on a
// single line; with pretty, each line is written without its leading
// spaces. Java: the aCode branch and the #line of cCode are omitted (D5).
func (o *OutputCode) WriteTL(deep int, s string, line, length int64) {
	if !o.pretty {
		if o.singleLine > 0 || o.lineCounter > line {
			o.Write(o.makeSingleLine(s))
			return
		}
		for o.lineCounter < line {
			o.Write("\n")
			o.lineCounter++
		}
		o.Write(s)
		o.lineCounter += length
		return
	}
	lines := javaSplit(s)
	if len(lines) == 0 {
		o.WriteDeep(deep, strings.TrimLeft(s, javaSpace))
		return
	}
	for _, ln := range lines {
		o.writelnDeep(deep, strings.TrimLeft(ln, javaSpace))
	}
}

// Close is close (OutputCode.java:299): it flushes and closes the writer
// when it can, and returns the first error.
func (o *OutputCode) Close() error {
	if f, ok := o.w.(interface{ Flush() error }); ok {
		if err := f.Flush(); err != nil && o.err == nil {
			o.err = err
		}
	}
	if c, ok := o.w.(io.Closer); ok {
		if err := c.Close(); err != nil && o.err == nil {
			o.err = err
		}
	}
	return o.err
}

// StringDump is stringDump (OutputCode.java:309): the text written in an
// OutputCode of NewStringOutputCode. Java: InternalError otherwise.
func (o *OutputCode) StringDump() string {
	if o.sb == nil {
		panic(&platform.RuntimeError{Msg: "OutputCode does not contain any string"})
	}
	return o.sb.String()
}
```

Run: `cd /Users/pem/github/tom/src/go/tom && gofmt -l engine/tools ; go vet ./engine/tools/ && go test -count=1 ./engine/tools/`
Expected: rien pour `gofmt -l` ; `ok`. Un cas de `TestGoSingleLine` qui
échoue : relire la ligne Java correspondante (`OutputCode.java:182-238`)
avant de toucher au test ; l'attendu du test se calcule à la main d'après
Java, il ne s'ajuste pas au code Go.

- [ ] **Step 3 : programmes Java de test**

`test/tools/j4/java/Quote.java` :

```java
/**
 * The quoting of tom/engine/bridge.JavaQuote (\\ \" \n \r \t escaped, any
 * other char below 0x20 or 0x7f written \u00XX) and its inverse, for the
 * J4.1a equivalence helpers.
 */
public class Quote {
  public static String quote(String s) {
    StringBuilder b = new StringBuilder("\"");
    for(int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      switch(c) {
        case '\\': b.append("\\\\"); break;
        case '"': b.append("\\\""); break;
        case '\n': b.append("\\n"); break;
        case '\r': b.append("\\r"); break;
        case '\t': b.append("\\t"); break;
        default:
          if(c < 0x20 || c == 0x7f) {
            b.append(String.format("\\u%04x", (int)c));
          } else {
            b.append(c);
          }
      }
    }
    return b.append('"').toString();
  }

  public static String unquote(String q) {
    if(q.length() < 2 || q.charAt(0) != '"' || q.charAt(q.length()-1) != '"') {
      throw new IllegalArgumentException("not a quoted string: " + q);
    }
    StringBuilder b = new StringBuilder();
    for(int i = 1; i < q.length()-1; i++) {
      char c = q.charAt(i);
      if(c != '\\') {
        b.append(c);
        continue;
      }
      char e = q.charAt(++i);
      switch(e) {
        case 'n': b.append('\n'); break;
        case 'r': b.append('\r'); break;
        case 't': b.append('\t'); break;
        case 'u': b.append((char)Integer.parseInt(q.substring(i+1, i+5), 16)); i += 4; break;
        default: b.append(e);
      }
    }
    return b.toString();
  }
}
```

`test/tools/j4/java/StubOptionManager.java` :

```java
import java.util.*;

import tom.platform.ConfigurationManager;
import tom.platform.OptionManager;
import tom.platform.adt.platformoption.types.PlatformOptionList;

/** An OptionManager holding fixed values (J4.1a equivalence helpers). */
public class StubOptionManager implements OptionManager {
  private final Map<String,Object> values = new HashMap<String,Object>();

  public StubOptionManager set(String name, Object value) {
    values.put(name, value);
    return this;
  }

  public int initialize(ConfigurationManager confManager, String[] commandLine) { return 0; }
  public void setGlobalOptionList(PlatformOptionList optionList) { }
  public List<String> getInputToCompileList() { return new ArrayList<String>(); }
  public void setOptionValue(String name, Object value) { values.put(name, value); }

  public Object getOptionValue(String name) {
    if(!values.containsKey(name)) {
      throw new RuntimeException("StubOptionManager: no option " + name);
    }
    return values.get(name);
  }
}
```

`test/tools/j4/java/OutputCodeScript.java` :

```java
import java.io.*;

import tom.engine.tools.OutputCode;

/**
 * Replays OutputCode scripts (J4.1a, test/tools/j4/outputcode_test.go).
 * Args: <scripts file> <results file>, both UTF-8. A script starts with
 * "script go|string <pretty>" and ends with "end"; the operations are
 * write <q>, int <n>, deep <d> <q>, tl <d> <line> <length> <q>, writeln,
 * open, closebrace, coma, single, unsingle (q: a string quoted by
 * Quote.quote). For each script, one line "result <q>" is written.
 */
public class OutputCodeScript {
  public static void main(String[] args) throws Exception {
    BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(args[0]), "UTF-8"));
    Writer out = new OutputStreamWriter(new FileOutputStream(args[1]), "UTF-8");
    OutputCode oc = null;
    for(String line; (line = in.readLine()) != null; ) {
      String[] f = line.split(" ", 2);
      String op = f[0];
      String rest = f.length > 1 ? f[1] : "";
      if(op.equals("script")) {
        String[] g = rest.split(" ");
        if(g[0].equals("go")) {
          StubOptionManager om = new StubOptionManager()
            .set("cCode", Boolean.FALSE).set("aCode", Boolean.FALSE).set("pCode", Boolean.FALSE)
            .set("pretty", Boolean.valueOf(g[1].equals("true"))).set("goCode", Boolean.TRUE);
          oc = new OutputCode(new StringWriter(), om);
        } else {
          oc = new OutputCode(new StringWriter());
        }
      } else if(op.equals("write")) {
        oc.write(Quote.unquote(rest));
      } else if(op.equals("int")) {
        oc.write(Integer.parseInt(rest));
      } else if(op.equals("deep")) {
        String[] g = rest.split(" ", 2);
        oc.write(Integer.parseInt(g[0]), Quote.unquote(g[1]));
      } else if(op.equals("tl")) {
        String[] g = rest.split(" ", 4);
        oc.write(Integer.parseInt(g[0]), Quote.unquote(g[3]), Integer.parseInt(g[1]), Integer.parseInt(g[2]));
      } else if(op.equals("writeln")) {
        oc.writeln();
      } else if(op.equals("open")) {
        oc.writeOpenBrace();
      } else if(op.equals("closebrace")) {
        oc.writeCloseBrace();
      } else if(op.equals("coma")) {
        oc.writeComa();
      } else if(op.equals("single")) {
        oc.setSingleLine();
      } else if(op.equals("unsingle")) {
        oc.unsetSingleLine();
      } else if(op.equals("end")) {
        out.write("result " + Quote.quote(oc.stringDump()) + "\n");
        oc = null;
      } else {
        throw new RuntimeException("bad operation: " + line);
      }
    }
    out.close();
  }
}
```

`test/tools/j4/javahelpers_test.go` :

```go
package j4

import (
	"os"
	"path/filepath"
	"testing"

	"tomtools/harness"
)

// javaHelpers compiles test/tools/j4/java/*.java (javac and java of the
// PATH: JDK 8) against the jars bin/tom runs with
// (lib/tom-compiler-full.jar, lib/tom-runtime-full.jar) and returns the
// classpath that runs them.
func javaHelpers(t *testing.T, dist harness.Dist) string {
	t.Helper()
	harness.LookPath(t, "javac")
	harness.LookPath(t, "java")
	jars := filepath.Join(dist.Home, "lib", "tom-compiler-full.jar") + string(os.PathListSeparator) +
		filepath.Join(dist.Home, "lib", "tom-runtime-full.jar")
	srcs, err := filepath.Glob(filepath.Join(harness.ToolsDir(harness.RepoRoot(t)), "j4", "java", "*.java"))
	if err != nil || len(srcs) == 0 {
		t.Fatalf("no Java helper in test/tools/j4/java: %v", err)
	}
	classes := t.TempDir()
	args := append([]string{"-encoding", "UTF-8", "-nowarn", "-cp", jars, "-d", classes}, srcs...)
	if out, err := harness.RunIn(classes, nil, "javac", args...); err != nil {
		t.Fatalf("javac: %v\n%s", err, out)
	}
	return classes + string(os.PathListSeparator) + jars
}
```

- [ ] **Step 4 : test d'équivalence**

`test/tools/j4/outputcode_test.go` :

```go
package j4

import (
	"fmt"
	"math/rand"
	"os"
	"path/filepath"
	"strconv"
	"strings"
	"testing"

	"tom/engine/bridge"
	"tom/engine/platform"
	"tom/engine/tools"
	"tomtools/harness"
)

// handScripts are the cases of the unit tests of tools.OutputCode, as
// scripts of OutputCodeScript.java.
var handScripts = []string{
	"script go false\ntl 0 3 0 \"f()\"\ntl 0 3 2 \"a := 1\\nreturn a\\n\"\ntl 0 4 1 \"x\\ny\"\nend\n",
	"script go false\nwrite \"f(x)\"\nsingle\ntl 0 1 1 \"\\ny\"\nunsingle\nend\n",
	"script go false\nsingle\ntl 0 1 1 \"a\\tb\\r\\nc\"\nend\n",
	"script string false\ntl 0 0 1 \"x\\ny\"\nint -42\nopen\ncoma\nclosebrace\ndeep 3 \"z\"\nwriteln\nend\n",
	"script go true\ntl 2 1 4 \"  a\\n\\n  b\\n\\n\"\nend\n",
	"script go true\ntl 2 1 2 \"\\n\\n\"\nend\n",
	"script go true\ntl 2 1 0 \"\"\nwriteln\nend\n",
}

var scriptPieces = []string{"x", "y1", "return", "break", "else", "func", "go", "a++", "b--", "f(x)", "m[i]", "}", "{",
	"\"s\"", "'c'", "`r`", ";", "; y := 2", "", "   ", "\t", "\r", "été", "x :=", "+", "if a {", "case 1:", "_ = s", "// c", "é"}

var scriptSeps = []string{"\n", " ", "\n\n", " \n ", "\r\n"}

// randomText is a piece of Go host code drawn with r, made to exercise
// goSingleLine: identifiers, keywords, closing brackets, literals, ++/--,
// explicit ';', blank lines, tabs, '\r', non-ASCII letters.
func randomText(r *rand.Rand) string {
	var b strings.Builder
	for k := 1 + r.Intn(4); k > 0; k-- {
		b.WriteString(scriptPieces[r.Intn(len(scriptPieces))])
		if k > 1 {
			b.WriteString(scriptSeps[r.Intn(len(scriptSeps))])
		}
	}
	if r.Intn(3) == 0 {
		b.WriteString("\n")
	}
	return b.String()
}

// randomScript is a script of OutputCodeScript.java drawn with r; the line
// numbers wander around the line counter so that both the padding and the
// single-line branches of WriteTL are taken.
func randomScript(r *rand.Rand, mode string, pretty bool) string {
	var b strings.Builder
	fmt.Fprintf(&b, "script %s %v\n", mode, pretty)
	line, single := int64(1), 0
	for n := 5 + r.Intn(20); n > 0; n-- {
		switch k := r.Intn(20); {
		case k < 6:
			fmt.Fprintf(&b, "write %s\n", bridge.JavaQuote(randomText(r)))
		case k < 14:
			s := randomText(r)
			length := int64(strings.Count(s, "\n"))
			if r.Intn(5) == 0 {
				length += int64(r.Intn(3)) - 1
			}
			line += int64(r.Intn(5)) - 1
			fmt.Fprintf(&b, "tl %d %d %d %s\n", r.Intn(3), line, length, bridge.JavaQuote(s))
		case k == 14:
			fmt.Fprintf(&b, "int %d\n", r.Intn(2000)-1000)
		case k == 15:
			fmt.Fprintf(&b, "deep %d %s\n", r.Intn(3), bridge.JavaQuote(randomText(r)))
		case k == 16:
			b.WriteString([]string{"writeln\n", "open\n", "closebrace\n", "coma\n"}[r.Intn(4)])
		case k < 19:
			if single < 2 {
				b.WriteString("single\n")
				single++
			}
		default:
			if single > 0 {
				b.WriteString("unsingle\n")
				single--
			}
		}
	}
	b.WriteString("end\n")
	return b.String()
}

func boolOption(name string, v bool) bridge.Option {
	return bridge.Option{Name: name, Kind: "bool", Value: strconv.FormatBool(v)}
}

// replay runs one script on tools.OutputCode and returns what it wrote.
func replay(t *testing.T, script string) string {
	t.Helper()
	var oc *tools.OutputCode
	var buf strings.Builder
	unquote := func(q string) string {
		s, err := strconv.Unquote(q)
		if err != nil {
			t.Fatalf("bad quoted string %s: %v", q, err)
		}
		return s
	}
	atoi := func(s string) int64 {
		n, err := strconv.ParseInt(s, 10, 64)
		if err != nil {
			t.Fatal(err)
		}
		return n
	}
	for _, line := range strings.Split(strings.TrimSuffix(script, "\n"), "\n") {
		op, rest, _ := strings.Cut(line, " ")
		switch op {
		case "script":
			g := strings.Split(rest, " ")
			if g[0] == "go" {
				buf.Reset()
				oc = tools.NewOutputCode(&buf, platform.NewOptions([]bridge.Option{
					boolOption("aCode", false), boolOption("cCode", false), boolOption("goCode", true),
					boolOption("pCode", false), boolOption("pretty", g[1] == "true"),
				}))
			} else {
				oc = tools.NewStringOutputCode()
			}
		case "write":
			oc.Write(unquote(rest))
		case "int":
			oc.WriteInt(atoi(rest))
		case "deep":
			g := strings.SplitN(rest, " ", 2)
			oc.WriteDeep(int(atoi(g[0])), unquote(g[1]))
		case "tl":
			g := strings.SplitN(rest, " ", 4)
			oc.WriteTL(int(atoi(g[0])), unquote(g[3]), atoi(g[1]), atoi(g[2]))
		case "writeln":
			oc.Writeln()
		case "open":
			oc.WriteOpenBrace()
		case "closebrace":
			oc.WriteCloseBrace()
		case "coma":
			oc.WriteComa()
		case "single":
			oc.SetSingleLine()
		case "unsingle":
			oc.UnsetSingleLine()
		case "end":
			if strings.HasPrefix(script, "script go ") {
				if err := oc.Close(); err != nil {
					t.Fatal(err)
				}
				return buf.String()
			}
			return oc.StringDump()
		default:
			t.Fatalf("bad operation %q", line)
		}
	}
	t.Fatal("script without end")
	return ""
}

// TestOutputCodeJava replays the hand-written scripts and 400 random ones
// (fixed seed) on tom.engine.tools.OutputCode (Java, OutputCodeScript) and
// on tools.OutputCode (Go): the outputs must be identical byte for byte.
func TestOutputCodeJava(t *testing.T) {
	dist := harness.NewDist(t)
	cp := javaHelpers(t, dist)
	r := rand.New(rand.NewSource(1))
	scripts := append([]string(nil), handScripts...)
	for _, m := range []struct {
		mode   string
		pretty bool
		n      int
	}{{"go", false, 200}, {"go", true, 100}, {"string", false, 100}} {
		for i := 0; i < m.n; i++ {
			scripts = append(scripts, randomScript(r, m.mode, m.pretty))
		}
	}
	dir := t.TempDir()
	in, out := filepath.Join(dir, "scripts.txt"), filepath.Join(dir, "results.txt")
	if err := os.WriteFile(in, []byte(strings.Join(scripts, "")), 0o644); err != nil {
		t.Fatal(err)
	}
	if msg, err := harness.RunIn(dir, nil, "java", "-cp", cp, "OutputCodeScript", in, out); err != nil {
		t.Fatalf("java OutputCodeScript: %v\n%s", err, msg)
	}
	b, err := os.ReadFile(out)
	if err != nil {
		t.Fatal(err)
	}
	results := strings.Split(strings.TrimSuffix(string(b), "\n"), "\n")
	if len(results) != len(scripts) {
		t.Fatalf("%d results for %d scripts", len(results), len(scripts))
	}
	same, differ := 0, 0
	for i, s := range scripts {
		want, err := strconv.Unquote(strings.TrimPrefix(results[i], "result "))
		if err != nil {
			t.Fatalf("result %d: %v", i, err)
		}
		if got := replay(t, s); got != want {
			differ++
			if differ <= 3 {
				t.Errorf("script %d:\n%s\nGo   %q\nJava %q", i, s, got, want)
			}
			continue
		}
		same++
	}
	t.Logf("OutputCode: scripts %d, identiques %d, différents %d", len(scripts), same, differ)
}
```

Run: `cd /Users/pem/github/tom && unset TOM_HOME && ./build.sh src.dist 2>&1 | tail -1 && (cd test/tools && TOMGO_REQUIRE_TOOLS=1 go test -count=1 -v ./j4/ -run TestOutputCodeJava 2>&1 | grep -E 'scripts|ok|FAIL')`
Expected: `BUILD SUCCESSFUL` ; `OutputCode: scripts 407, identiques 407, différents 0` ; `ok`.
Un écart : comparer la ligne Java qui l'explique et corriger le Go (jamais le
script ni Java) ; s'il vient d'une différence assumée (runes au-delà du BMP),
l'écrire en `// port:` et retirer ce caractère des pièces aléatoires, en le
disant dans le commit.

- [ ] **Step 5 : vérification et commit**

Run (une fois) : `cd /Users/pem/github/tom && mage -d magefiles -w . all && mage -d magefiles -w . j4`
Expected: tout vert.

```bash
P="src/go/tom/engine/tools test/tools/j4/java test/tools/j4/javahelpers_test.go test/tools/j4/outputcode_test.go"
git add $P
git commit --only $P -m "tom-go J4.1a: OutputCode in Go, identical to Java on 407 write scripts" -m "Co-Authored-By: Claude Sonnet 5.5 <noreply@anthropic.com>"
```

**Acceptance:** tests unitaires verts ; `TestOutputCodeJava` : 407 scripts,
0 différent ; `mage all`, `mage j4` verts.

---

### Task 3 : `mage regenTomGo` (`X.t` → `X.t.go` avec `src/dist`), amorce des sources Tom+Go, premier `.t` (`astfactory.t`)

**Modèle : Sonnet** (outillage spécifié ; premier `.t` du compilateur,
petit).

**Files:**
- Modify: `test/tools/harness/harness.go` (`CheckFreshCompiler`)
- Create: `test/tools/j4/regentomgo/main.go`
- Modify: `test/tools/j4/j4_test.go` (`TestTomGoRegen`)
- Modify: `magefiles/magefile.go` (`RegenTomGo`, `TomGoFresh`, `checkTomGoFresh`, `tomGoSources`, `tomGoFromAmorce` ; `Bootstrap`, `Test`, `Vet`, `VetStandalone`, `J4`, `BridgeCheck`, `PromoteGo`)
- Modify: `.gitignore`
- Create: `src/go/tom/engine/tools/astfactory.t`, `src/go/tom/engine/tools/astfactory_test.go`
- Modify: `stable/go/tom/**` (`promoteGo`), `doc/go/README.md`, `CLAUDE.md`

**Interfaces:**
- Consumes: `harness.FindRoot`, `harness.RunIn`, `harness.CheckFresh` (existant) ; `adt.tom` généré par `mage bootstrap`.
- Produces: `func CheckFreshCompiler(root, home string) error` (harnais) ; programme `go run ./j4/regentomgo [-check] [-v]` (dans `test/tools`) ; cibles `mage regenTomGo`, `mage tomGoFresh` ; règle « `X.t` → `X.t.go` » ; `tools.NewType(typeOptions adt.TypeOptionList, tomName, tlName string) adt.TomType`, `tools.MakeSingleLineCode(code string, pretty bool) string`.

- [ ] **Step 1 : garde de fraîcheur du compilateur seul**

Dans `test/tools/harness/harness.go`, extraire de `CheckFresh` la partie
qui compare le jar aux sources Java (de `jar := …` au `return
fmt.Errorf("stale compiler: …")` inclus) dans

```go
// CheckFreshCompiler returns an error when the compiler of the dist at
// home (lib/tom-compiler-full.jar) is older than one of the sources it is
// built from: src/tom/**/*.{t,java,tom,config}, src/tom/library/mapping/**
// and src/config/*.config of the repository root. regentomgo needs only
// this: it runs the compiler, not the Go commands of the dist.
func CheckFreshCompiler(root, home string) error {
	// … the jar part of CheckFresh, moved verbatim …
	return nil
}
```

et faire commencer `CheckFresh` par

```go
	if err := CheckFreshCompiler(root, home); err != nil {
		return err
	}
```

(son commentaire : « CheckFresh is CheckFreshCompiler plus the Go commands
installed in bin/ … »). Comportement inchangé.

Run: `cd /Users/pem/github/tom/test/tools && go vet ./harness/ && go test -count=1 ./harness/`
Expected: `ok`.

- [ ] **Step 2 : `regentomgo`**

`test/tools/j4/regentomgo/main.go` :

```go
// Command regentomgo compiles the Tom+Go sources of the compiler
// (src/go/tom/engine/**/*.t, testdata excluded) with the dist src/dist
// (spec J4 §4.3, decision D4) into X.t.go next to each X.t:
//
//	tom -go -np -I src/go/tom/engine/adt -o <tmp>/X.t.go X.t
//
// A generated file is written only when its content changes (mage
// tomGoFresh and CheckFresh compare dates). The compiler of the dist must
// be fresh (harness.CheckFreshCompiler). Each generated file is also
// compared with the amorce (stable/go/tom, same path): a difference is
// reported (run mage promoteGo).
//
// With -check nothing is written, and a generated file that differs from
// src or from the amorce is an error.
package main

import (
	"bytes"
	"flag"
	"fmt"
	"io/fs"
	"os"
	"path/filepath"
	"sort"
	"strings"

	"tomtools/harness"
)

func main() {
	check := flag.Bool("check", false, "write nothing; fail when a generated file differs from src or from the amorce")
	verbose := flag.Bool("v", false, "print the output of the compiler")
	flag.Parse()
	if err := run(*check, *verbose); err != nil {
		fmt.Fprintln(os.Stderr, "regentomgo:", err)
		os.Exit(1)
	}
}

// sources returns the Tom+Go sources under dir (testdata excluded), sorted.
func sources(dir string) ([]string, error) {
	var out []string
	err := filepath.WalkDir(dir, func(p string, d fs.DirEntry, err error) error {
		if err != nil {
			return err
		}
		if d.IsDir() {
			if d.Name() == "testdata" {
				return filepath.SkipDir
			}
			return nil
		}
		if strings.HasSuffix(p, ".t") {
			out = append(out, p)
		}
		return nil
	})
	sort.Strings(out)
	return out, err
}

func run(check, verbose bool) error {
	root, err := harness.FindRoot()
	if err != nil {
		return err
	}
	home := filepath.Join(root, "src", "dist")
	if err := harness.CheckFreshCompiler(root, home); err != nil {
		return err
	}
	module := filepath.Join(root, "src", "go", "tom")
	adtDir := filepath.Join(module, "engine", "adt")
	if _, err := os.Stat(filepath.Join(adtDir, "adt.tom")); err != nil {
		return fmt.Errorf("no adt.tom in %s: run mage -d magefiles -w . bootstrap", adtDir)
	}
	srcs, err := sources(filepath.Join(module, "engine"))
	if err != nil {
		return err
	}
	tmp, err := os.MkdirTemp("", "regentomgo-")
	if err != nil {
		return err
	}
	defer os.RemoveAll(tmp)
	env := []string{"TOM_HOME=" + home, "GOMGO_BIN=" + filepath.Join(home, "bin", "gomgo")}
	var written, unchanged, stale, amorce []string
	for i, src := range srcs {
		rel, _ := filepath.Rel(module, src)
		gen := filepath.Join(tmp, fmt.Sprint(i), filepath.Base(src)+".go")
		if err := os.MkdirAll(filepath.Dir(gen), 0o755); err != nil {
			return err
		}
		out, err := harness.RunIn(filepath.Dir(src), env, filepath.Join(home, "bin", "tom"),
			"-go", "-np", "-I", adtDir, "-o", gen, src)
		if verbose || err != nil {
			fmt.Print(out)
		}
		if err != nil {
			return fmt.Errorf("tom -go %s: %v", rel, err)
		}
		b, err := os.ReadFile(gen)
		if err != nil {
			return fmt.Errorf("%s: no generated file: %v", rel, err)
		}
		if a, err := os.ReadFile(filepath.Join(root, "stable", "go", "tom", rel+".go")); err != nil || !bytes.Equal(a, b) {
			amorce = append(amorce, rel+".go")
		}
		dst := src + ".go"
		if old, err := os.ReadFile(dst); err == nil && bytes.Equal(old, b) {
			unchanged = append(unchanged, rel+".go")
			continue
		}
		if check {
			stale = append(stale, rel+".go")
			continue
		}
		if err := os.WriteFile(dst, b, 0o644); err != nil {
			return err
		}
		written = append(written, rel+".go")
	}
	fmt.Printf("regentomgo: %d source(s), %d written, %d unchanged\n", len(srcs), len(written), len(unchanged))
	for _, w := range written {
		fmt.Println("  written", w)
	}
	for _, s := range stale {
		fmt.Println("  differs from src (run mage regenTomGo):", s)
	}
	for _, a := range amorce {
		fmt.Println("  differs from the amorce (run mage promoteGo):", a)
	}
	if check && len(stale)+len(amorce) > 0 {
		return fmt.Errorf("%d generated file(s) differ from src, %d from the amorce", len(stale), len(amorce))
	}
	return nil
}
```

- [ ] **Step 3 : cibles mage, garde, amorce**

Dans `magefiles/magefile.go` :

1. Après `syncGenerated`, ajouter :

```go
// tomGoSources returns the Tom+Go sources (*.t) of src/go/tom, testdata
// excluded; the Go generated from X.t is X.t.go, next to it (mage
// regenTomGo; gitignored in src/, kept in the amorce by promoteGo).
func tomGoSources() ([]string, error) {
	var out []string
	err := filepath.WalkDir(tomDir, func(p string, d os.DirEntry, err error) error {
		if err != nil {
			return err
		}
		if d.IsDir() {
			if d.Name() == "testdata" {
				return filepath.SkipDir
			}
			return nil
		}
		if strings.HasSuffix(p, ".t") {
			out = append(out, p)
		}
		return nil
	})
	sort.Strings(out)
	return out, err
}

// tomGoFromAmorce gives each Tom+Go source X.t of src/go/tom that lacks
// its generated X.t.go (a fresh clone) the copy of the amorce
// (stable/go/tom, same path), so that module tom builds without Java. A
// source the amorce does not know yet is left to regenTomGo (tomGoFresh
// reports it).
func tomGoFromAmorce() error {
	srcs, err := tomGoSources()
	if err != nil {
		return err
	}
	for _, s := range srcs {
		gen := s + ".go"
		if _, err := os.Stat(gen); err == nil {
			continue
		}
		rel, _ := filepath.Rel(tomDir, gen)
		b, err := os.ReadFile(filepath.Join(stableGoDir, rel))
		if err != nil {
			continue
		}
		if err := os.WriteFile(gen, b, 0o644); err != nil {
			return err
		}
		fmt.Println("bootstrap: " + gen + " taken from the amorce " + stableGoDir)
	}
	return nil
}

// checkTomGoFresh fails when a Tom+Go source X.t of src/go/tom has no
// generated X.t.go or is newer than it.
func checkTomGoFresh() error {
	srcs, err := tomGoSources()
	if err != nil {
		return err
	}
	for _, s := range srcs {
		st, err := os.Stat(s)
		if err != nil {
			return err
		}
		gt, err := os.Stat(s + ".go")
		if err != nil {
			return fmt.Errorf("%s has no generated %s.go: run mage -d magefiles -w . regenTomGo (needs src/dist)", s, filepath.Base(s))
		}
		if st.ModTime().After(gt.ModTime()) {
			return fmt.Errorf("%s is newer than its generated %s.go: run mage -d magefiles -w . regenTomGo (needs src/dist), "+
				"or delete the .t.go to take the amorce's (bootstrap)", s, filepath.Base(s))
		}
	}
	return nil
}

// TomGoFresh fails when the Go generated from a Tom+Go source of
// src/go/tom (X.t -> X.t.go) is missing or older than its source.
func TomGoFresh() error {
	mg.Deps(Bootstrap)
	return checkTomGoFresh()
}

// RegenTomGo compiles the Tom+Go sources of the compiler
// (src/go/tom/engine/**/*.t) with src/dist (D4; its compiler must be
// fresh) into their X.t.go; needs a built src/dist (JDK 8,
// ./build.sh src.dist). After it: promoteGo, then bootstrap, to put them
// in the amorce.
func RegenTomGo() error {
	mg.Deps(Bootstrap)
	return runIn(toolsDir, "go", "run", "./j4/regentomgo")
}
```

2. `Bootstrap` : remplacer le `return nil` final par `return tomGoFromAmorce()` et ajouter au commentaire : « It also gives each Tom+Go source its generated X.t.go from the amorce when src lacks it (tomGoFromAmorce). »
3. `Test`, `Vet`, `VetStandalone`, `J4`, `BridgeCheck` : `mg.Deps(Bootstrap)` devient `mg.Deps(TomGoFresh)`.
4. `PromoteGo` : en tête, `if err := checkTomGoFresh(); err != nil { return err }` (pas de dépendance à `Bootstrap` : `promoteGo` sert justement quand `bootstrap` échoue après une évolution de gomgo), et compléter son commentaire : « The Go generated from the Tom+Go sources (X.t.go, mage regenTomGo) is copied with the rest of the module. »

`.gitignore`, après le bloc des ADT :

```
# Go generated by mage regenTomGo from the Tom+Go sources of the compiler
# (X.t -> X.t.go, next to the source); promoteGo keeps it in the amorce
/src/go/tom/**/*.t.go
```

Run: `cd /Users/pem/github/tom && mage -d magefiles -w . -l | grep -iE 'regentomgo|tomgofresh' && mage -d magefiles -w . tomGoFresh && echo ok`
Expected: les deux cibles listées ; `ok` (aucun `.t` encore).

- [ ] **Step 4 : premier `.t` (TDD)**

`src/go/tom/engine/tools/astfactory_test.go` :

```go
package tools

import (
	"fmt"
	"testing"

	"tom/engine/adt"
)

func TestNewType(t *testing.T) {
	got := NewType(adt.MakeConcTypeOption(), "int", "int64")
	if want := `Type(concTypeOption(),"int",TLType("int64"))`; fmt.Sprint(got) != want {
		t.Fatalf("got %v, want %s", got, want)
	}
	if got != adt.MakeType(adt.MakeConcTypeOption(), "int", adt.MakeTLType("int64")) {
		t.Fatal("NewType is not maximally shared")
	}
}

func TestMakeSingleLineCode(t *testing.T) {
	if got := MakeSingleLineCode("a\n\tb\r", false); got != "a  b " {
		t.Fatalf("got %q", got)
	}
	if got := MakeSingleLineCode("a\n\tb\r", true); got != "a\n\tb\r" {
		t.Fatalf("pretty: got %q", got)
	}
}
```

`src/go/tom/engine/tools/astfactory.t` :

```
package tools

// The subset of tom.engine.tools.ASTFactory (src/tom/engine/tools/ASTFactory.t)
// that the symbol table and the backend use (plan J4.1a).

import (
	"strings"

	. "tom/engine/adt"
	"tom/library/aterm"
)

%include { adt.tom }

// NewType is ASTFactory.makeType (ASTFactory.t:307): the type tomName
// implemented by the target-language type tlName. (adt.MakeType is the
// constructor of the operator Type.)
func NewType(typeOptions TypeOptionList, tomName string, tlName string) TomType {
  return `Type(typeOptions, tomName, TLType(tlName))
}

// MakeSingleLineCode is ASTFactory.makeSingleLineCode (ASTFactory.t:387).
func MakeSingleLineCode(code string, pretty bool) string {
  if !pretty {
    code = strings.NewReplacer("\n", " ", "\t", " ", "\r", " ").Replace(code)
  }
  return code
}

var _ = aterm.GetInstance
```

(`adt.tom` inclut `aterm.tom` : l'import de `tom/library/aterm` est
nécessaire, comme dans `test/go/j4/testdata/adtuse/adtuse.t`.)

Run: `cd /Users/pem/github/tom && mage -d magefiles -w . tomGoFresh 2>&1 | tail -1`
Expected: échec `… astfactory.t has no generated astfactory.t.go: run mage … regenTomGo`.

Run: `mage -d magefiles -w . regenTomGo && ls src/go/tom/engine/tools/ && git status --short src/go/tom && (cd src/go/tom && go vet ./engine/tools/ && go test -count=1 ./engine/tools/)`
Expected: `regentomgo: 1 source(s), 1 written, 0 unchanged`, `written engine/tools/astfactory.t.go`, puis `differs from the amorce (run mage promoteGo): engine/tools/astfactory.t.go` ; `astfactory.t.go` présent mais **absent** de `git status` (ignoré) ; `go vet` et `go test` verts.
Si `tom -go` refuse le fichier ou si `go vet` rejette le code généré :
voir « Risques et conditions d'arrêt » (ne pas contourner dans le `.t`).

- [ ] **Step 5 : amorce et contre-épreuves**

```bash
cd /Users/pem/github/tom
mage -d magefiles -w . promoteGo && mage -d magefiles -w . bootstrap
ls stable/go/tom/engine/tools/ stable/go/tom/engine/platform/
cp src/go/tom/engine/tools/astfactory.t.go /tmp/astfactory.t.go.keep && rm src/go/tom/engine/tools/astfactory.t.go
mage -d magefiles -w . bootstrap && cmp src/go/tom/engine/tools/astfactory.t.go /tmp/astfactory.t.go.keep && echo amorce-ok
touch src/go/tom/engine/tools/astfactory.t && mage -d magefiles -w . tomGoFresh 2>&1 | grep -c 'is newer than its generated'
mage -d magefiles -w . regenTomGo
```
Expected: l'amorce contient `engine/tools/{doc,outputcode,astfactory.t}.go`
et `engine/platform/*.go` ; `bootstrap … taken from the amorce` puis
`amorce-ok` (un clone neuf se construit sans Java) ; `1` (la garde voit le
`.t` modifié) ; `regenTomGo` : `0 written, 1 unchanged` (contenu identique :
le fichier n'est pas réécrit) — puis `touch src/go/tom/engine/tools/astfactory.t.go`
pour que la garde repasse (le contenu est à jour).

- [ ] **Step 6 : test `TestTomGoRegen`**

Dans `test/tools/j4/j4_test.go` :

```go
// TestTomGoRegen checks that the Go generated from the Tom+Go sources of
// the compiler (src/go/tom/engine/**/*.t -> X.t.go) is up to date in src
// and in the amorce stable/go/tom (go run ./j4/regentomgo -check, with
// src/dist).
func TestTomGoRegen(t *testing.T) {
	harness.LookPath(t, "go")
	harness.NewDist(t)
	root := harness.RepoRoot(t)
	out, err := harness.RunIn(harness.ToolsDir(root), nil, "go", "run", "./j4/regentomgo", "-check")
	if err != nil {
		t.Fatalf("regentomgo -check: %v\n%s", err, out)
	}
	t.Log(strings.TrimSpace(out))
}
```

- [ ] **Step 7 : documentation**

`doc/go/README.md`, « Sources et amorce » : un point « Sources Tom+Go du
compilateur » : `src/go/tom/engine/**/X.t` est compilé par `mage
regenTomGo` (`src/dist`, D4, garde `CheckFreshCompiler`) en `X.t.go` à
côté du `.t`, ignoré par git ; `promoteGo` le copie dans l'amorce ;
`bootstrap` reprend le `X.t.go` de l'amorce quand `src` n'en a pas ;
`mage tomGoFresh` (préalable de `test`, `vet`, `j4`, `bridgeCheck`) refuse
un `X.t` plus récent que son `X.t.go` ; après une régénération qui change
un fichier, reconstruire `src/dist` avant les harnais (`CheckFresh` des
binaires). `CLAUDE.md` : cookbook (« Cible Go du compilateur Tom+Java ») :
`mage -d magefiles -w . regenTomGo`, `tomGoFresh`, la règle `X.t` → `X.t.go`,
et dans le tableau du §2 la ligne `src/go/tom/` (paquetages `engine/tools`,
`engine/platform`).

- [ ] **Step 8 : vérification et commit**

Run (une fois) : `cd /Users/pem/github/tom && unset TOM_HOME && ./build.sh src.dist 2>&1 | tail -1 && mage -d magefiles -w . all && mage -d magefiles -w . j4`
Expected: tout vert, `TestTomGoRegen` compris (`0 written`, aucune
différence avec l'amorce).

```bash
P="test/tools/harness/harness.go test/tools/j4/regentomgo test/tools/j4/j4_test.go magefiles/magefile.go .gitignore src/go/tom/engine/tools/astfactory.t src/go/tom/engine/tools/astfactory_test.go stable/go/tom doc/go/README.md CLAUDE.md"
git add $P
git status --short | grep '\.t\.go' && echo "STOP: a generated .t.go of src is staged"
git commit --only $P -m "tom-go J4.1a: mage regenTomGo (X.t -> X.t.go with src/dist), Tom+Go sources in the amorce; astfactory.t" -m "Co-Authored-By: Claude Sonnet 5.5 <noreply@anthropic.com>"
```
(`stable/go/tom` ne doit contenir que les ajouts de `platform` et `tools` :
`git diff --cached --stat stable/go/tom` avant le commit ; tout autre
changement de l'amorce : s'arrêter.)

**Acceptance:** `mage regenTomGo` compile `astfactory.t` avec `src/dist` ;
le `.t.go` est ignoré dans `src/` et présent dans l'amorce ; un `src` sans
`.t.go` se reconstruit depuis l'amorce ; un `.t` modifié non régénéré est
refusé ; `TestTomGoRegen` vert.

---

### Task 4 : `symboltable.t` (sous-ensemble `SymbolTable` du backend, `FromBundle`)

**Modèle : Sonnet** (port méthode par méthode, code donné ; adaptation
éventuelle aux limites du parser îlot).

**Files:**
- Create: `src/go/tom/engine/tools/symboltable.t`, `src/go/tom/engine/tools/symboltable_test.go`
- Modify: `stable/go/tom/**` (`promoteGo`)

**Interfaces:**
- Consumes: `NewType` (Task 3), `platform.RuntimeError` (Task 1), `bridge.Bundle` (`Table`, `Types`, `Used`).
- Produces: `type SymbolTable struct`, `NewSymbolTable`, `FromBundle` et les méthodes de la table « Sous-ensemble porté / SymbolTable », signatures exactes ci-dessous.

- [ ] **Step 1 : tests (TDD)**

`src/go/tom/engine/tools/symboltable_test.go` :

```go
package tools

import (
	"fmt"
	"strings"
	"testing"

	"tom/engine/adt"
	"tom/engine/bridge"
	"tom/engine/platform"
)

const (
	typeT  = `Type(concTypeOption(),"T",TLType("t"))`
	symA   = `Symbol(Name("a"),TypesToType(concTomType(),` + typeT + `),concPairNameDecl(),concOption())`
	symB   = `Symbol(Name("b"),TypesToType(concTomType(),` + typeT + `),concPairNameDecl(),concOption())`
	tableB = `Table(concTomEntry(Entry("b",` + symB + `),Entry("a",` + symA + `)))`
)

func TestBuiltinTypes(t *testing.T) {
	st := NewSymbolTable()
	for _, c := range []struct {
		got  adt.TomType
		want string
	}{
		{st.GetIntType(), `Type(concTypeOption(),"int",TLType("int64"))`},
		{st.GetIntArrayType(), `Type(concTypeOption(),"intarray",TLType("[]int64"))`},
		{st.GetLongType(), `Type(concTypeOption(),"long",TLType("int64"))`},
		{st.GetFloatType(), `Type(concTypeOption(),"float",TLType("float32"))`},
		{st.GetCharType(), `Type(concTypeOption(),"char",TLType("rune"))`},
		{st.GetDoubleType(), `Type(concTypeOption(),"double",TLType("float64"))`},
		{st.GetBooleanType(), `Type(concTypeOption(),"boolean",TLType("bool"))`},
		{st.GetStringType(), `Type(concTypeOption(),"String",TLType("string"))`},
		{st.GetUniversalType(), `Type(concTypeOption(),"universal",TLType("any"))`},
		{st.GetVoidType(), `Type(concTypeOption(),"void",TLType(""))`},
	} {
		if fmt.Sprint(c.got) != c.want {
			t.Errorf("got %v, want %s", c.got, c.want)
		}
	}
	if st.GetIntArrayOp() != "concInt" {
		t.Fatal("GetIntArrayOp")
	}
}

func TestTypePredicates(t *testing.T) {
	st := NewSymbolTable()
	for _, n := range []string{"int", "long", "char", "String", "boolean", "double", "float"} {
		if !st.IsBuiltinType(n) || st.GetBuiltinType(n) == nil {
			t.Errorf("%s is builtin", n)
		}
	}
	for _, n := range []string{"intarray", "void", "universal", "T"} {
		if st.IsBuiltinType(n) {
			t.Errorf("%s is not builtin", n)
		}
	}
	if !st.IsUnknownType("unknown type") || st.IsUnknownType("int") {
		t.Fatal("IsUnknownType")
	}
	if st.GetBuiltinType("String") != st.GetStringType() {
		t.Fatal("GetBuiltinType is not maximally shared")
	}
	defer func() {
		e, ok := recover().(*platform.RuntimeError)
		if !ok || e.Msg != "getBuiltinType error on term: Nat" {
			t.Fatalf("GetBuiltinType(Nat): %v", e)
		}
	}()
	st.GetBuiltinType("Nat")
}

func TestSymbolsAndTerm(t *testing.T) {
	st := NewSymbolTable()
	st.FromTerm(adt.TomSymbolTableFromString(tableB))
	if got := strings.Join(st.KeySymbols(), " "); got != "a b" {
		t.Fatalf("KeySymbols %q", got)
	}
	if fmt.Sprint(st.GetSymbolFromName("a")) != symA || st.GetSymbolFromName("c") != nil {
		t.Fatal("GetSymbolFromName")
	}
	if got := fmt.Sprint(st.ToTerm()); got != tableB {
		t.Fatalf("ToTerm %s", got)
	}
	if st.GetType("T") != nil || fmt.Sprint(st.GetType("int")) != fmt.Sprint(st.GetIntType()) {
		t.Fatal("GetType before PutType")
	}
	ty := adt.TomTypeFromString(typeT)
	st.PutType("T", ty)
	if st.GetType("T") != ty {
		t.Fatal("GetType")
	}
}

func TestUsedMarks(t *testing.T) {
	st := NewSymbolTable()
	st.FromTerm(adt.TomSymbolTableFromString(tableB))
	st.PutType("T", adt.TomTypeFromString(typeT))
	a := st.GetSymbolFromName("a")
	st.SetUsedSymbolConstructor(a)
	st.SetUsedSymbolDestructorName("b")
	st.SetUsedSymbolDestructorName("nosuch")
	st.SetUsedTypeName("T")
	st.SetUsedTypeName("int")
	st.SetUsedTypeName("nosuch")
	if !st.IsUsedSymbolConstructor(a) || st.IsUsedSymbolDestructor(a) || !st.IsUsedSymbolConstructorName("a") ||
		!st.IsUsedSymbolDestructorName("b") || st.IsUsedSymbolConstructorName("nosuch") || st.IsUsedSymbolAC(a) {
		t.Fatal("symbol marks")
	}
	if !st.IsUsedTypeName("T") || !st.IsUsedType(st.GetIntType()) || st.IsUsedTypeName("long") {
		t.Fatal("type marks")
	}
	var got []string
	for _, ke := range st.GetUsedKeyEntries() {
		got = append(got, fmt.Sprint(ke))
	}
	want := []string{
		`UsedSymbolConstructor(` + symA + `)`,
		`UsedSymbolDestructor(` + symB + `)`,
		`UsedType(` + typeT + `)`,
		`UsedType(Type(concTypeOption(),"int",TLType("int64")))`,
	}
	if strings.Join(got, "\n") != strings.Join(want, "\n") {
		t.Fatalf("GetUsedKeyEntries:\n%s", strings.Join(got, "\n"))
	}
}

func TestInliners(t *testing.T) {
	st := NewSymbolTable()
	if _, ok := st.GetIsFsym("f"); ok {
		t.Fatal("no inliner yet")
	}
	st.PutIsFsym("f", "isf")
	st.PutGetSlot("f", "x", "gx")
	st.PutResolveGetSlot("g", "y")
	st.PutMake("empty_list_l", "m")
	if c, ok := st.GetIsFsym("f"); !ok || c != "isf" {
		t.Fatal("GetIsFsym")
	}
	if c, ok := st.GetGetSlot("f", "x"); !ok || c != "gx" {
		t.Fatal("GetGetSlot")
	}
	if c, ok := st.GetResolveGetSlot("g", "y"); !ok || c != "" {
		t.Fatal("GetResolveGetSlot: present, empty")
	}
	// Java's keys are prefix+name in one map: make_ + empty_list_l is make_empty_list_ + l
	if c, ok := st.GetMakeEmptyList("l"); !ok || c != "m" {
		t.Fatal("shared inliner keys")
	}
}

func TestFromBundle(t *testing.T) {
	text := strings.Join([]string{
		"tom-bridge 1",
		"from Backend",
		"input /x/X.t",
		`inputname "X.t"`,
		"output /x/X.go",
		"option goCode bool true",
		"used UsedType(" + typeT + ")",
		"type " + typeT,
		"table " + tableB,
		"code Tom(concCode())",
		"end",
	}, "\n") + "\n"
	b, err := bridge.Read(strings.NewReader(text))
	if err != nil {
		t.Fatal(err)
	}
	st := FromBundle(b)
	if st.ToTerm() != b.Table || fmt.Sprint(st.GetType("T")) != typeT || !st.IsUsedTypeName("T") {
		t.Fatal("FromBundle")
	}
	if len(st.GetUsedKeyEntries()) != 1 || st.GetUsedKeyEntries()[0] != b.Used[0] ||
		len(st.GetTypeEntries()) != 1 || st.GetTypeEntries()[0] != b.Types[0] {
		t.Fatal("marks and types")
	}
}
```

- [ ] **Step 2 : `symboltable.t`**

```
package tools

// The subset of tom.engine.tools.SymbolTable (src/tom/engine/tools/SymbolTable.t)
// the backend uses (plan J4.1a, « Sous-ensemble porté »), for the Go
// target only (D5): the types of the other targets, builtinToWrapper and
// what the phases before the backend use (getSymbolFromType,
// updateConstrainedSymbolCodomain, isResolveSymbol, isNumericType…) are
// not ported.

import (
	"fmt"
	"sort"

	. "tom/engine/adt"
	"tom/engine/bridge"
	"tom/engine/platform"
	"tom/library/aterm"
)

%include { adt.tom }

// The builtin sorts (SymbolTable.t:51-61).
const (
	typeInt       = "int"
	typeLong      = "long"
	typeFloat     = "float"
	typeChar      = "char"
	typeDouble    = "double"
	typeString    = "String"
	typeBoolean   = "boolean"
	typeUniversal = "universal"
	typeVoid      = "void"
	typeIntArray  = "intarray"
	intArrayOp    = "concInt"
)

// typeUnknown is TYPE_UNKNOWN (SymbolTable.t:63).
func typeUnknown() TomType {
  return `Type(concTypeOption(), "unknown type", EmptyTargetLanguageType())
}

// SymbolTable is tom.engine.tools.SymbolTable: a single table for every
// module (getSymbolTable(moduleName) ignores the module, spec J4 §3.1).
type SymbolTable struct {
	mapSymbolName map[string]TomSymbol // Java: TreeMap (KeySymbols sorts the names)
	mapTypeName   map[string]TomType   // Java: TreeMap (GetTypeEntries sorts the names)
	usedKeyEntry  map[KeyEntry]bool    // Java: HashSet (membership; the terms are maximally shared)
	mapInliner    map[string]string    // Java: HashMap (SymbolTable.t:559)
}

// NewSymbolTable is new SymbolTable() then init(optionManager) with -go
// (SymbolTable.t:79): the builtin types are those of Go.
func NewSymbolTable() *SymbolTable {
	return &SymbolTable{
		mapSymbolName: map[string]TomSymbol{},
		mapTypeName:   map[string]TomType{},
		usedKeyEntry:  map[KeyEntry]bool{},
		mapInliner:    map[string]string{},
	}
}

// FromBundle is the symbol table the bridge hands over (spec J4 §4.2): the
// symbols of its table line (fromTerm), its declared types (putType, keyed
// by their name, as AstBuilder.t:175 does) and the used marks of the
// earlier phases (the Collector of the backend adds its own).
func FromBundle(b *bridge.Bundle) *SymbolTable {
	st := NewSymbolTable()
	st.FromTerm(b.Table)
	for _, ty := range b.Types {
		%match(ty) {
			Type[TomType=name] -> { st.PutType(`name, ty) }
		}
	}
	for _, ke := range b.Used {
		st.usedKeyEntry[ke] = true
	}
	return st
}

// PutSymbol is putSymbol (SymbolTable.t:113).
func (st *SymbolTable) PutSymbol(name string, astSymbol TomSymbol) {
	st.mapSymbolName[name] = astSymbol
}

// GetSymbolFromName is getSymbolFromName (SymbolTable.t:117): nil when unknown.
func (st *SymbolTable) GetSymbolFromName(name string) TomSymbol {
	return st.mapSymbolName[name]
}

// PutType is putType (SymbolTable.t:131).
func (st *SymbolTable) PutType(name string, astType TomType) {
	st.mapTypeName[name] = astType
}

// GetUsedKeyEntries is getUsedKeyEntries (SymbolTable.t:149): the used
// marks sorted by their text. port: Go compares bytes, Java UTF-16 chars
// (the same order on BMP text).
func (st *SymbolTable) GetUsedKeyEntries() []KeyEntry {
	res := make([]KeyEntry, 0, len(st.usedKeyEntry))
	for ke := range st.usedKeyEntry {
		res = append(res, ke)
	}
	sort.Slice(res, func(i, j int) bool { return fmt.Sprint(res[i]) < fmt.Sprint(res[j]) })
	return res
}

// GetTypeEntries is getTypeEntries (SymbolTable.t:160): the declared types
// in the order of their names.
func (st *SymbolTable) GetTypeEntries() []TomType {
	names := make([]string, 0, len(st.mapTypeName))
	for n := range st.mapTypeName {
		names = append(names, n)
	}
	sort.Strings(names)
	res := make([]TomType, len(names))
	for i, n := range names {
		res[i] = st.mapTypeName[n]
	}
	return res
}

// GetType is getType (SymbolTable.t:164): a declared type, else the
// builtin int, else nil.
func (st *SymbolTable) GetType(name string) TomType {
	if ty, ok := st.mapTypeName[name]; ok {
		return ty
	}
	if name == typeInt {
		return st.GetIntType()
	}
	return nil
}

// IsUsedSymbolConstructor is isUsedSymbolConstructor(TomSymbol) (SymbolTable.t:175).
func (st *SymbolTable) IsUsedSymbolConstructor(symbol TomSymbol) bool {
	ke := `UsedSymbolConstructor(symbol)
	return st.usedKeyEntry[ke]
}

// IsUsedSymbolDestructor is isUsedSymbolDestructor(TomSymbol) (SymbolTable.t:179).
func (st *SymbolTable) IsUsedSymbolDestructor(symbol TomSymbol) bool {
	ke := `UsedSymbolDestructor(symbol)
	return st.usedKeyEntry[ke]
}

// IsUsedSymbolAC is isUsedSymbolAC (SymbolTable.t:183).
func (st *SymbolTable) IsUsedSymbolAC(symbol TomSymbol) bool {
	ke := `UsedSymbolAC(symbol)
	return st.usedKeyEntry[ke]
}

// IsUsedType is isUsedType(TomType) (SymbolTable.t:187).
func (st *SymbolTable) IsUsedType(ty TomType) bool {
	ke := `UsedType(ty)
	return st.usedKeyEntry[ke]
}

// SetUsedSymbolConstructor is setUsedSymbolConstructor(TomSymbol) (SymbolTable.t:191).
func (st *SymbolTable) SetUsedSymbolConstructor(symbol TomSymbol) {
	ke := `UsedSymbolConstructor(symbol)
	st.usedKeyEntry[ke] = true
}

// SetUsedSymbolDestructor is setUsedSymbolDestructor(TomSymbol) (SymbolTable.t:195).
func (st *SymbolTable) SetUsedSymbolDestructor(symbol TomSymbol) {
	ke := `UsedSymbolDestructor(symbol)
	st.usedKeyEntry[ke] = true
}

// SetUsedType is setUsedType(TomType) (SymbolTable.t:203).
func (st *SymbolTable) SetUsedType(ty TomType) {
	ke := `UsedType(ty)
	st.usedKeyEntry[ke] = true
}

// SetUsedSymbolConstructorName is setUsedSymbolConstructor(String) (SymbolTable.t:207).
func (st *SymbolTable) SetUsedSymbolConstructorName(name string) {
	if symbol := st.GetSymbolFromName(name); symbol != nil {
		st.SetUsedSymbolConstructor(symbol)
	}
}

// SetUsedSymbolDestructorName is setUsedSymbolDestructor(String) (SymbolTable.t:214).
func (st *SymbolTable) SetUsedSymbolDestructorName(name string) {
	if symbol := st.GetSymbolFromName(name); symbol != nil {
		st.SetUsedSymbolDestructor(symbol)
	}
}

// SetUsedTypeName is setUsedType(String) (SymbolTable.t:228).
func (st *SymbolTable) SetUsedTypeName(name string) {
	if ty := st.GetType(name); ty != nil {
		st.SetUsedType(ty)
	}
}

// IsUsedSymbolConstructorName is isUsedSymbolConstructor(String) (SymbolTable.t:235).
func (st *SymbolTable) IsUsedSymbolConstructorName(name string) bool {
	if symbol := st.GetSymbolFromName(name); symbol != nil {
		return st.IsUsedSymbolConstructor(symbol)
	}
	return false
}

// IsUsedSymbolDestructorName is isUsedSymbolDestructor(String) (SymbolTable.t:243).
func (st *SymbolTable) IsUsedSymbolDestructorName(name string) bool {
	if symbol := st.GetSymbolFromName(name); symbol != nil {
		return st.IsUsedSymbolDestructor(symbol)
	}
	return false
}

// IsUsedTypeName is isUsedType(String) (SymbolTable.t:251).
func (st *SymbolTable) IsUsedTypeName(name string) bool {
	if ty := st.GetType(name); ty != nil {
		return st.IsUsedType(ty)
	}
	return false
}

// GetIntType is getIntType (SymbolTable.t:259); Java: the other targets (D5).
func (st *SymbolTable) GetIntType() TomType { return NewType(`concTypeOption(), typeInt, "int64") }

// GetIntArrayType is getIntArrayType (SymbolTable.t:268).
func (st *SymbolTable) GetIntArrayType() TomType { return NewType(`concTypeOption(), typeIntArray, "[]int64") }

// GetLongType is getLongType (SymbolTable.t:277).
func (st *SymbolTable) GetLongType() TomType { return NewType(`concTypeOption(), typeLong, "int64") }

// GetFloatType is getFloatType (SymbolTable.t:286).
func (st *SymbolTable) GetFloatType() TomType { return NewType(`concTypeOption(), typeFloat, "float32") }

// GetCharType is getCharType (SymbolTable.t:295).
func (st *SymbolTable) GetCharType() TomType { return NewType(`concTypeOption(), typeChar, "rune") }

// GetDoubleType is getDoubleType (SymbolTable.t:306).
func (st *SymbolTable) GetDoubleType() TomType { return NewType(`concTypeOption(), typeDouble, "float64") }

// GetBooleanType is getBooleanType (SymbolTable.t:317).
func (st *SymbolTable) GetBooleanType() TomType { return NewType(`concTypeOption(), typeBoolean, "bool") }

// GetStringType is getStringType (SymbolTable.t:332).
func (st *SymbolTable) GetStringType() TomType { return NewType(`concTypeOption(), typeString, "string") }

// GetUniversalType is getUniversalType (SymbolTable.t:343).
func (st *SymbolTable) GetUniversalType() TomType { return NewType(`concTypeOption(), typeUniversal, "any") }

// GetVoidType is getVoidType (SymbolTable.t:358).
func (st *SymbolTable) GetVoidType() TomType { return NewType(`concTypeOption(), typeVoid, "") }

// IsIntType is isIntType (SymbolTable.t:371).
func (st *SymbolTable) IsIntType(ty string) bool { return ty == typeInt }

// IsIntArrayType is isIntArrayType (SymbolTable.t:375).
func (st *SymbolTable) IsIntArrayType(ty string) bool { return ty == typeIntArray }

// IsLongType is isLongType (SymbolTable.t:379).
func (st *SymbolTable) IsLongType(ty string) bool { return ty == typeLong }

// IsFloatType is isFloatType (SymbolTable.t:383).
func (st *SymbolTable) IsFloatType(ty string) bool { return ty == typeFloat }

// IsCharType is isCharType (SymbolTable.t:387).
func (st *SymbolTable) IsCharType(ty string) bool { return ty == typeChar }

// IsStringType is isStringType (SymbolTable.t:391).
func (st *SymbolTable) IsStringType(ty string) bool { return ty == typeString }

// IsBooleanType is isBooleanType (SymbolTable.t:395).
func (st *SymbolTable) IsBooleanType(ty string) bool { return ty == typeBoolean }

// IsDoubleType is isDoubleType (SymbolTable.t:399).
func (st *SymbolTable) IsDoubleType(ty string) bool { return ty == typeDouble }

// IsVoidType is isVoidType (SymbolTable.t:403).
func (st *SymbolTable) IsVoidType(ty string) bool { return ty == typeVoid }

// IsUnknownType is isUnknownType (SymbolTable.t:407).
func (st *SymbolTable) IsUnknownType(ty string) bool {
	t := `Type(concTypeOption(), ty, EmptyTargetLanguageType())
	return t == typeUnknown()
}

// IsBuiltinType is isBuiltinType (SymbolTable.t:425); Java: builtinToWrapper
// (SymbolTable.t:411) is Java only (D5).
func (st *SymbolTable) IsBuiltinType(ty string) bool {
	return st.IsIntType(ty) || st.IsLongType(ty) || st.IsCharType(ty) ||
		st.IsStringType(ty) || st.IsBooleanType(ty) || st.IsDoubleType(ty) ||
		st.IsFloatType(ty)
}

// GetBuiltinType is getBuiltinType (SymbolTable.t:460). port: Java also
// prints "Not a builtin type" on stdout, which carries the generated code
// here.
func (st *SymbolTable) GetBuiltinType(ty string) TomType {
	switch {
	case st.IsIntType(ty):
		return st.GetIntType()
	case st.IsLongType(ty):
		return st.GetLongType()
	case st.IsCharType(ty):
		return st.GetCharType()
	case st.IsStringType(ty):
		return st.GetStringType()
	case st.IsBooleanType(ty):
		return st.GetBooleanType()
	case st.IsDoubleType(ty):
		return st.GetDoubleType()
	case st.IsFloatType(ty):
		return st.GetFloatType()
	}
	panic(&platform.RuntimeError{Msg: "getBuiltinType error on term: " + ty})
}

// KeySymbols is keySymbolIterable (SymbolTable.t:484): the symbol names, sorted.
// port: Go compares bytes, Java's TreeMap UTF-16 chars (the same order on BMP text).
func (st *SymbolTable) KeySymbols() []string {
	names := make([]string, 0, len(st.mapSymbolName))
	for n := range st.mapSymbolName {
		names = append(names, n)
	}
	sort.Strings(names)
	return names
}

// FromTerm is fromTerm (SymbolTable.t:493).
func (st *SymbolTable) FromTerm(table TomSymbolTable) {
	list := table.GetEntryList()
	for !list.IsEmptyconcTomEntry() {
		entry := list.GetHeadconcTomEntry()
		st.PutSymbol(entry.GetStrName(), entry.GetAstSymbol())
		list = list.GetTailconcTomEntry()
	}
}

// ToTerm is toTerm (SymbolTable.t:502): each entry is put in front of the
// list, so the names come in the reverse order.
func (st *SymbolTable) ToTerm() TomSymbolTable {
	list := `concTomEntry()
	for _, name := range st.KeySymbols() {
		symbol := st.GetSymbolFromName(name)
		entry := `Entry(name, symbol)
		list = `concTomEntry(entry, list*)
	}
	return `Table(list)
}

// The prefixes of the inliners (SymbolTable.t:561-574): one map, keyed by
// prefix+name, as in Java (make_ + "empty_list_l" is make_empty_list_ + "l").
const (
	prefixIsFsym          = "is_fsym_"
	prefixGetSlot         = "get_slot_"
	prefixGetHead         = "get_head_"
	prefixGetTail         = "get_tail_"
	prefixGetElementArray = "get_element_array_"
	prefixGetSizeArray    = "get_size_array_"
	prefixIsEmptyList     = "is_empty_list_"
	prefixIsSort          = "is_sort_"
	prefixMake            = "make_"
	prefixMakeEmptyArray  = "make_empty_array_"
	prefixMakeEmptyList   = "make_empty_list_"
	prefixMakeAddArray    = "make_append_"
	prefixMakeAddList     = "make_insert_"
	prefixEqualTerm       = "equal_"
)

// putInliner is putInliner (SymbolTable.t:576).
func (st *SymbolTable) putInliner(prefix, opname, code string) {
	st.mapInliner[prefix+opname] = code
}

// getInliner is getInliner (SymbolTable.t:580): ok is false where Java
// returns null.
func (st *SymbolTable) getInliner(prefix, opname string) (code string, ok bool) {
	code, ok = st.mapInliner[prefix+opname]
	return code, ok
}

// PutIsFsym is putIsFsym (SymbolTable.t:584).
func (st *SymbolTable) PutIsFsym(opname, code string) { st.putInliner(prefixIsFsym, opname, code) }

// GetIsFsym is getIsFsym (SymbolTable.t:588).
func (st *SymbolTable) GetIsFsym(opname string) (string, bool) { return st.getInliner(prefixIsFsym, opname) }

// PutIsSort is putIsSort (SymbolTable.t:592).
func (st *SymbolTable) PutIsSort(ty, code string) { st.putInliner(prefixIsSort, ty, code) }

// GetIsSort is getIsSort (SymbolTable.t:596).
func (st *SymbolTable) GetIsSort(ty string) (string, bool) { return st.getInliner(prefixIsSort, ty) }

// PutResolveGetSlot is putResolveGetSlot (SymbolTable.t:601): an empty
// code (the backend generates it), which is not « absent ».
func (st *SymbolTable) PutResolveGetSlot(opname, slotname string) {
	st.putInliner(prefixGetSlot, opname+slotname, "")
}

// GetResolveGetSlot is getResolveGetSlot (SymbolTable.t:604).
func (st *SymbolTable) GetResolveGetSlot(opname, slotname string) (string, bool) {
	return st.getInliner(prefixGetSlot, opname+slotname)
}

// PutGetSlot is putGetSlot (SymbolTable.t:608).
func (st *SymbolTable) PutGetSlot(opname, slotname, code string) {
	st.putInliner(prefixGetSlot, opname+slotname, code)
}

// GetGetSlot is getGetSlot (SymbolTable.t:611).
func (st *SymbolTable) GetGetSlot(opname, slotname string) (string, bool) {
	return st.getInliner(prefixGetSlot, opname+slotname)
}

// PutGetHead is putGetHead (SymbolTable.t:615).
func (st *SymbolTable) PutGetHead(opname, code string) { st.putInliner(prefixGetHead, opname, code) }

// GetGetHead is getGetHead (SymbolTable.t:618).
func (st *SymbolTable) GetGetHead(opname string) (string, bool) { return st.getInliner(prefixGetHead, opname) }

// PutGetTail is putGetTail (SymbolTable.t:622).
func (st *SymbolTable) PutGetTail(opname, code string) { st.putInliner(prefixGetTail, opname, code) }

// GetGetTail is getGetTail (SymbolTable.t:625).
func (st *SymbolTable) GetGetTail(opname string) (string, bool) { return st.getInliner(prefixGetTail, opname) }

// PutGetElementArray is putGetElementArray (SymbolTable.t:629).
func (st *SymbolTable) PutGetElementArray(opname, code string) {
	st.putInliner(prefixGetElementArray, opname, code)
}

// GetGetElementArray is getGetElementArray (SymbolTable.t:632).
func (st *SymbolTable) GetGetElementArray(opname string) (string, bool) {
	return st.getInliner(prefixGetElementArray, opname)
}

// PutGetSizeArray is putGetSizeArray (SymbolTable.t:636).
func (st *SymbolTable) PutGetSizeArray(opname, code string) {
	st.putInliner(prefixGetSizeArray, opname, code)
}

// GetGetSizeArray is getGetSizeArray (SymbolTable.t:640).
func (st *SymbolTable) GetGetSizeArray(opname string) (string, bool) {
	return st.getInliner(prefixGetSizeArray, opname)
}

// PutIsEmptyList is putIsEmptyList (SymbolTable.t:645).
func (st *SymbolTable) PutIsEmptyList(opname, code string) { st.putInliner(prefixIsEmptyList, opname, code) }

// GetIsEmptyList is getIsEmptyList (SymbolTable.t:648).
func (st *SymbolTable) GetIsEmptyList(opname string) (string, bool) {
	return st.getInliner(prefixIsEmptyList, opname)
}

// PutMake is putMake (SymbolTable.t:652).
func (st *SymbolTable) PutMake(opname, code string) { st.putInliner(prefixMake, opname, code) }

// GetMake is getMake (SymbolTable.t:655).
func (st *SymbolTable) GetMake(opname string) (string, bool) { return st.getInliner(prefixMake, opname) }

// PutMakeEmptyArray is putMakeEmptyArray (SymbolTable.t:659).
func (st *SymbolTable) PutMakeEmptyArray(opname, code string) {
	st.putInliner(prefixMakeEmptyArray, opname, code)
}

// GetMakeEmptyArray is getMakeEmptyArray (SymbolTable.t:662).
func (st *SymbolTable) GetMakeEmptyArray(opname string) (string, bool) {
	return st.getInliner(prefixMakeEmptyArray, opname)
}

// PutMakeEmptyList is putMakeEmptyList (SymbolTable.t:666).
func (st *SymbolTable) PutMakeEmptyList(opname, code string) {
	st.putInliner(prefixMakeEmptyList, opname, code)
}

// GetMakeEmptyList is getMakeEmptyList (SymbolTable.t:669).
func (st *SymbolTable) GetMakeEmptyList(opname string) (string, bool) {
	return st.getInliner(prefixMakeEmptyList, opname)
}

// PutMakeAddArray is putMakeAddArray (SymbolTable.t:673).
func (st *SymbolTable) PutMakeAddArray(opname, code string) {
	st.putInliner(prefixMakeAddArray, opname, code)
}

// GetMakeAddArray is getMakeAddArray (SymbolTable.t:676).
func (st *SymbolTable) GetMakeAddArray(opname string) (string, bool) {
	return st.getInliner(prefixMakeAddArray, opname)
}

// PutMakeAddList is putMakeAddList (SymbolTable.t:680).
func (st *SymbolTable) PutMakeAddList(opname, code string) {
	st.putInliner(prefixMakeAddList, opname, code)
}

// GetMakeAddList is getMakeAddList (SymbolTable.t:683).
func (st *SymbolTable) GetMakeAddList(opname string) (string, bool) {
	return st.getInliner(prefixMakeAddList, opname)
}

// PutEqualTerm is putEqualTerm (SymbolTable.t:687).
func (st *SymbolTable) PutEqualTerm(ty, code string) { st.putInliner(prefixEqualTerm, ty, code) }

// GetEqualTerm is getEqualTerm (SymbolTable.t:690).
func (st *SymbolTable) GetEqualTerm(ty string) (string, bool) { return st.getInliner(prefixEqualTerm, ty) }

// GetIntArrayOp is getIntArrayOp (SymbolTable.t:699).
func (st *SymbolTable) GetIntArrayOp() string { return intArrayOp }

var _ = aterm.GetInstance
```

- [ ] **Step 3 : génération, tests, amorce**

Run: `cd /Users/pem/github/tom && mage -d magefiles -w . regenTomGo && (cd src/go/tom && go vet ./engine/tools/ && go test -count=1 ./engine/tools/) && mage -d magefiles -w . promoteGo && mage -d magefiles -w . bootstrap`
Expected: `1 written, 1 unchanged` ; tests verts ; amorce mise à jour
(`stable/go/tom/engine/tools/symboltable.t.go`). Un nom de `tools` en
conflit avec un nom exporté de `adt` (import `.`) donne `… redeclared in
this block` : renommer le nom Go (jamais celui de `adt`), le noter dans
le commit, et mettre à jour la table « Sous-ensemble porté » de ce plan
dans le même commit.

- [ ] **Step 4 : vérification et commit**

Run (une fois) : `cd /Users/pem/github/tom && unset TOM_HOME && ./build.sh src.dist 2>&1 | tail -1 && mage -d magefiles -w . all && mage -d magefiles -w . j4`
Expected: tout vert (`TestTomGoRegen` : `0 written`, aucune différence
avec l'amorce).

```bash
P="src/go/tom/engine/tools/symboltable.t src/go/tom/engine/tools/symboltable_test.go stable/go/tom"
git add $P
git commit --only $P -m "tom-go J4.1a: symboltable.t (SymbolTable subset of the backend, FromBundle)" -m "Co-Authored-By: Claude Sonnet 5.5 <noreply@anthropic.com>"
```

**Acceptance:** `symboltable.t` compilé par `regenTomGo` ; tests verts ;
`FromBundle` rend la table, les types et les marques du paquet à
l'identique (`ToTerm() == b.Table`).

---

### Task 5 : `tombase.t` (sous-ensemble `TomBase` du backend)

**Modèle : Sonnet** (port méthode par méthode, code donné ; la sémantique
des `null` et des exceptions est vérifiée contre Java en Task 6).

**Files:**
- Create: `src/go/tom/engine/tools/tombase.t`, `src/go/tom/engine/tools/tombase_test.go`
- Modify: `stable/go/tom/**` (`promoteGo`)

**Interfaces:**
- Consumes: `*SymbolTable` (`GetSymbolFromName`, `GetType`) (Task 4), `platform.RuntimeError`.
- Produces: `DefaultModuleName`, `GoDeclPrefix`, `GetTomType`, `GetTLType`, `GetTLCode`, `GetSymbolCodomain`, `GetSymbolDomain`, `TomNumberListToString`, `GetModuleName`, `GetSlotName`, `GetSlotIndex`, `ElementAt`, `GetSlotType`, `GetSymbolFromName`, `GetTermType`, `GetTermTypeBQ`, `GetTermTypeExpr` (signatures de la table « Sous-ensemble porté / TomBase »).

- [ ] **Step 1 : tests (TDD)**

`src/go/tom/engine/tools/tombase_test.go` :

```go
package tools

import (
	"fmt"
	"testing"

	"tom/engine/adt"
	"tom/engine/platform"
)

const (
	typeA = `Type(concTypeOption(),"A",TLType("a"))`
	typeB = `Type(concTypeOption(),"B",TLType("b"))`
	// f(x:A, y:B):T, the slot y declared by a get_slot named yy
	symF = `Symbol(Name("f"),TypesToType(concTomType(` + typeA + `,` + typeB + `),` + typeT + `),` +
		`concPairNameDecl(PairNameDecl(Name("x"),EmptyDeclaration()),` +
		`PairNameDecl(Name("y"),GetSlotDecl(Name("f"),Name("yy"),BQVariable(concOption(),Name("t"),EmptyType()),Code("c"),ModuleName("m")))),concOption())`
)

func tableF() *SymbolTable {
	st := NewSymbolTable()
	st.PutSymbol("f", adt.TomSymbolFromString(symF))
	return st
}

// runtimeMsg returns the message of the *platform.RuntimeError f panics with.
func runtimeMsg(t *testing.T, f func()) (msg string) {
	t.Helper()
	defer func() {
		e, ok := recover().(*platform.RuntimeError)
		if !ok {
			t.Fatalf("want a *platform.RuntimeError, got %v", e)
		}
		msg = e.Msg
	}()
	f()
	return ""
}

func TestGoDeclPrefix(t *testing.T) {
	for in, want := range map[string]string{
		"Peano.t": "tom_Peano_", "dir/my-file.t": "tom_my_file_", "": "tom_stdin_", "-": "tom_stdin_",
		".t": "tom__t_", "a.b.t": "tom_a_b_", "été.t": "tom__t__",
	} {
		if got := GoDeclPrefix(in); got != want {
			t.Errorf("GoDeclPrefix(%q) = %q, want %q", in, got, want)
		}
	}
}

func TestTypes(t *testing.T) {
	if n, ok := GetTomType(adt.TomTypeFromString(typeT)); !ok || n != "T" {
		t.Fatal("GetTomType(Type)")
	}
	for _, s := range []string{`EmptyType()`, `TypeVar("X",1)`} {
		if _, ok := GetTomType(adt.TomTypeFromString(s)); ok {
			t.Errorf("GetTomType(%s): ok", s)
		}
	}
	if m := runtimeMsg(t, func() { GetTomType(adt.TomTypeFromString(`Codomain("x")`)) }); m != `getTomType error on term: Codomain("x")` {
		t.Fatalf("GetTomType(Codomain): %q", m)
	}
	if GetTLType(adt.TomTypeFromString(typeT)) != "t" {
		t.Fatal("GetTLType")
	}
	if m := runtimeMsg(t, func() { GetTLType(adt.TomTypeFromString(`Type(concTypeOption(),"T",EmptyTargetLanguageType())`)) }); m != "getTLCode error on term: EmptyTargetLanguageType()" {
		t.Fatalf("GetTLType(empty TL): %q", m)
	}
	if fmt.Sprint(GetSymbolCodomain(nil)) != "EmptyType()" || fmt.Sprint(GetSymbolDomain(nil)) != "concTomType()" {
		t.Fatal("codomain and domain of nil")
	}
	f := adt.TomSymbolFromString(symF)
	if fmt.Sprint(GetSymbolCodomain(f)) != typeT || fmt.Sprint(GetSymbolDomain(f)) != `concTomType(`+typeA+`,`+typeB+`)` {
		t.Fatal("codomain and domain of f")
	}
}

func TestSlots(t *testing.T) {
	f := adt.TomSymbolFromString(symF)
	if fmt.Sprint(GetSlotName(f, 0)) != `Name("x")` || fmt.Sprint(GetSlotName(f, 1)) != `Name("yy")` {
		t.Fatal("GetSlotName")
	}
	if m := runtimeMsg(t, func() { GetSlotName(f, 2) }); m != "getSlotName: bad index error" {
		t.Fatalf("GetSlotName(2): %q", m)
	}
	y, yy := adt.TomNameFromString(`Name("y")`), adt.TomNameFromString(`Name("yy")`)
	if GetSlotIndex(f, y) != 1 || GetSlotIndex(f, yy) != -1 {
		t.Fatal("GetSlotIndex")
	}
	if fmt.Sprint(GetSlotType(f, y)) != typeB || GetSlotType(nil, y) != nil {
		t.Fatal("GetSlotType")
	}
	if m := runtimeMsg(t, func() { GetSlotType(f, yy) }); m != "illegal list index: -1" {
		t.Fatalf("GetSlotType(yy): %q", m)
	}
}

func TestNumberListAndModule(t *testing.T) {
	l := adt.TomNumberListFromString(`concTomNumber(Position(1),MatchNumber(2),NameNumber(Name("x")),RenamedVar(PositionName(concTomNumber(Begin(3)))))`)
	if got := TomNumberListToString(l); got != "Position1Match2xRenamedVarBegin3" {
		t.Fatalf("TomNumberListToString: %q", got)
	}
	if m, ok := GetModuleName(adt.OptionListFromString(`concOption(ModuleName("m"))`)); !ok || m != "m" {
		t.Fatal("GetModuleName")
	}
	if _, ok := GetModuleName(adt.OptionListFromString(`concOption()`)); ok {
		t.Fatal("GetModuleName of no option")
	}
}

func TestTermTypes(t *testing.T) {
	st := tableF()
	for s, want := range map[string]string{
		`Variable(concOption(),Name("x"),` + typeA + `,concConstraint())`:                                typeA,
		`TermAppl(concOption(),concTomName(Name("f")),concTomTerm(),concConstraint())`:                  typeT,
		`TermAppl(concOption(),concTomName(AntiName(Name("f"))),concTomTerm(),concConstraint())`:        typeT,
		`RecordAppl(concOption(),concTomName(Name("g")),concSlot(),concConstraint())`:                   `EmptyType()`,
		`AntiTerm(Variable(concOption(),Name("x"),` + typeA + `,concConstraint()))`:                      typeA,
	} {
		if got := fmt.Sprint(GetTermType(adt.TomTermFromString(s), st)); got != want {
			t.Errorf("GetTermType(%s) = %s, want %s", s, got, want)
		}
	}
	if m := runtimeMsg(t, func() { GetTermType(adt.TomTermFromString(`TruePattern()`), st) }); m != "getTermType error on term: TruePattern()" {
		t.Fatalf("TruePattern: %q", m)
	}
	for s, want := range map[string]string{
		`BQVariable(concOption(),Name("v"),` + typeB + `)`:   typeB,
		`FunctionCall(Name("g"),` + typeA + `,concBQTerm())`: typeA,
		`Subterm(Name("f"),Name("y"),BQDefault())`:           typeB,
		`BuildTerm(Name("f"),concBQTerm(),"m")`:              typeT,
		`BQAppl(concOption(),Name("g"),concBQTerm())`:        `EmptyType()`,
		`ExpressionToBQTerm(Cast(` + typeA + `,Code("c")))`:  typeA,
	} {
		if got := fmt.Sprint(GetTermTypeBQ(adt.BQTermFromString(s), st)); got != want {
			t.Errorf("GetTermTypeBQ(%s) = %s, want %s", s, got, want)
		}
	}
	if m := runtimeMsg(t, func() { GetTermTypeBQ(adt.BQTermFromString(`BQDefault()`), st) }); m != "getTermType error on term: BQDefault()" {
		t.Fatalf("BQDefault: %q", m)
	}
	if got := fmt.Sprint(GetTermTypeExpr(adt.ExpressionFromString(`Integer(3)`), st)); got != fmt.Sprint(st.GetIntType()) {
		t.Fatalf("Integer: %s", got)
	}
	if m := runtimeMsg(t, func() { GetTermTypeExpr(adt.ExpressionFromString(`Code("c")`), st) }); m != `getTermType error on term: Code("c")` {
		t.Fatalf("Code: %q", m)
	}
	if GetSymbolFromName("f", st) != st.GetSymbolFromName("f") {
		t.Fatal("GetSymbolFromName")
	}
}
```

(`adt.TomNameFromString` existe comme les autres `XFromString` du
paquetage ; s'il manquait, construire le nom par `adt.MakeName("y")`.)

- [ ] **Step 2 : `tombase.t`**

```
package tools

// The subset of tom.engine.TomBase (src/tom/engine/TomBase.t) the backend
// uses (plan J4.1a, « Sous-ensemble porté »). It lives in package tools:
// TomBase and SymbolTable use each other, and Go has no import cycle.

import (
	"path/filepath"
	"strconv"
	"strings"

	. "tom/engine/adt"
	"tom/engine/platform"
	"tom/library/aterm"
)

%include { adt.tom }

// DefaultModuleName is DEFAULT_MODULE_NAME (TomBase.t:73).
const DefaultModuleName = "default"

// GoDeclPrefix is goDeclPrefix (TomBase.t:88): tom_<F>_, F the base name
// of the input file without its extension, each character outside
// [A-Za-z0-9_] replaced by '_'; "stdin" for "" or "-". port: a rune
// outside the BMP gives "__" (two UTF-16 chars in Java).
func GoDeclPrefix(inputFileName string) string {
	f := inputFileName
	if slash := strings.LastIndexAny(f, "/"+string(filepath.Separator)); slash >= 0 {
		f = f[slash+1:]
	}
	if dot := strings.LastIndexByte(f, '.'); dot > 0 {
		f = f[:dot]
	}
	if f == "" || f == "-" {
		f = "stdin"
	}
	var b strings.Builder
	for _, r := range f {
		switch {
		case r >= 'A' && r <= 'Z', r >= 'a' && r <= 'z', r >= '0' && r <= '9', r == '_':
			b.WriteRune(r)
		case r > 0xFFFF:
			b.WriteString("__")
		default:
			b.WriteByte('_')
		}
	}
	return "tom_" + b.String() + "_"
}

// GetTomType is getTomType (TomBase.t:107): ok is false where Java returns
// null (EmptyType, TypeVar).
func GetTomType(ty TomType) (string, bool) {
  %match {
    Type[TomType=s] << ty -> { return `s, true }
    EmptyType() << ty || TypeVar[] << ty -> { return "", false }
  }
  panic(&platform.RuntimeError{Msg: "getTomType error on term: " + platform.JavaString(ty)})
}

// GetTLType is getTLType (TomBase.t:118).
func GetTLType(ty TomType) string {
  %match(ty) {
    Type[TlType=tlType] -> { return GetTLCode(`tlType) }
  }
  panic(&platform.RuntimeError{Msg: "getTLType error on term: " + platform.JavaString(ty)})
}

// GetTLCode is getTLCode (TomBase.t:128).
func GetTLCode(ty TargetLanguageType) string {
  %match(ty) {
    TLType(str) -> { return `str }
  }
  panic(&platform.RuntimeError{Msg: "getTLCode error on term: " + platform.JavaString(ty)})
}

// GetSymbolCodomain is getSymbolCodomain (TomBase.t:138).
func GetSymbolCodomain(symbol TomSymbol) TomType {
	if symbol != nil {
		return symbol.GetTypesToType().GetCodomain()
	}
	return `EmptyType()
}

// GetSymbolDomain is getSymbolDomain (TomBase.t:149).
func GetSymbolDomain(symbol TomSymbol) TomTypeList {
	if symbol != nil {
		return symbol.GetTypesToType().GetDomain()
	}
	return `concTomType()
}

// TomNumberListToString is tomNumberListToString (TomBase.t:159). port:
// without Java's LRU cache (a performance matter only).
func TomNumberListToString(numberList TomNumberList) string {
	var buf strings.Builder
	for !numberList.IsEmptyconcTomNumber() {
		number := numberList.GetHeadconcTomNumber()
		numberList = numberList.GetTailconcTomNumber()
		%match(number) {
			Position(n) -> { buf.WriteString("Position" + strconv.FormatInt(`n, 10)) }
			MatchNumber(n) -> { buf.WriteString("Match" + strconv.FormatInt(`n, 10)) }
			PatternNumber(n) -> { buf.WriteString("Pattern" + strconv.FormatInt(`n, 10)) }
			ListNumber(n) -> { buf.WriteString("List" + strconv.FormatInt(`n, 10)) }
			IndexNumber(n) -> { buf.WriteString("Index" + strconv.FormatInt(`n, 10)) }
			Begin(n) -> { buf.WriteString("Begin" + strconv.FormatInt(`n, 10)) }
			End(n) -> { buf.WriteString("End" + strconv.FormatInt(`n, 10)) }
			Save(n) -> { buf.WriteString("Save" + strconv.FormatInt(`n, 10)) }
			AbsVar(n) -> { buf.WriteString("AbsVar" + strconv.FormatInt(`n, 10)) }
			RenamedVar(tomName) -> { buf.WriteString("RenamedVar" + numberIdentifier(`tomName)) }
			NameNumber(tomName) -> { buf.WriteString(numberIdentifier(`tomName)) }
		}
	}
	return buf.String()
}

// numberIdentifier is the identifier of the name of a RenamedVar or a
// NameNumber in tomNumberListToString (TomBase.t:208-237).
func numberIdentifier(tomName TomName) string {
	identifier := "Empty"
	%match(tomName) {
		Name(name) -> { identifier = `name }
		PositionName(localNumberList) -> { identifier = TomNumberListToString(`localNumberList) }
	}
	return identifier
}

// GetModuleName is getModuleName (TomBase.t:408): ok is false where Java
// returns null.
func GetModuleName(optionList OptionList) (string, bool) {
	%match(optionList) {
		concOption(_*, ModuleName(moduleName), _*) -> { return `moduleName, true }
	}
	return "", false
}

// GetSlotName is getSlotName (TomBase.t:431). port: Java also prints
// "getSlotName: bad index error" on stdout, which carries the generated
// code here.
func GetSlotName(symbol TomSymbol, number int) TomName {
	pairNameDeclList := symbol.GetPairNameDeclList()
	for index := 0; !pairNameDeclList.IsEmptyconcPairNameDecl() && index < number; index++ {
		pairNameDeclList = pairNameDeclList.GetTailconcPairNameDecl()
	}
	if pairNameDeclList.IsEmptyconcPairNameDecl() {
		panic(&platform.RuntimeError{Msg: "getSlotName: bad index error"})
	}
	pairNameDecl := pairNameDeclList.GetHeadconcPairNameDecl()
	decl := pairNameDecl.GetSlotDecl()
	%match(decl) {
		GetSlotDecl[SlotName=name] -> { return `name }
	}
	return pairNameDecl.GetSlotName()
}

// GetSlotIndex is getSlotIndex (TomBase.t:450): -1 when absent.
func GetSlotIndex(tomSymbol TomSymbol, slotName TomName) int {
	index := 0
	pairNameDeclList := tomSymbol.GetPairNameDeclList()
	for !pairNameDeclList.IsEmptyconcPairNameDecl() {
		if pairNameDeclList.GetHeadconcPairNameDecl().GetSlotName() == slotName {
			return index
		}
		pairNameDeclList = pairNameDeclList.GetTailconcPairNameDecl()
		index++
	}
	return -1
}

// ElementAt is elementAt (TomBase.t:466). Java: IllegalArgumentException.
func ElementAt(l TomTypeList, index int) TomType {
	if 0 > index || index > l.Length() {
		panic(&platform.RuntimeError{Msg: "illegal list index: " + strconv.Itoa(index)})
	}
	for i := 0; i < index; i++ {
		l = l.GetTailconcTomType()
	}
	return l.GetHeadconcTomType()
}

// GetSlotType is getSlotType (TomBase.t:476): nil when symbol is not a Symbol.
func GetSlotType(symbol TomSymbol, slotName TomName) TomType {
	%match(symbol) {
		Symbol[TypesToType=TypesToType(typeList, _)] -> { return ElementAt(`typeList, GetSlotIndex(symbol, slotName)) }
	}
	return nil
}

// GetSymbolFromName is getSymbolFromName(String, SymbolTable) (TomBase.t:536).
func GetSymbolFromName(tomName string, symbolTable *SymbolTable) TomSymbol {
	return symbolTable.GetSymbolFromName(tomName)
}

// GetTermType is getTermType(TomTerm, SymbolTable) (TomBase.t:540).
func GetTermType(t TomTerm, symbolTable *SymbolTable) TomType {
	%match(t) {
		(TermAppl|RecordAppl)[NameList=concTomName(headName, _*)] -> {
			head := `headName
			var tomName string
			if head.IsAntiName() {
				tomName = head.GetName().GetString()
			} else {
				tomName = head.GetString()
			}
			if tomSymbol := symbolTable.GetSymbolFromName(tomName); tomSymbol != nil {
				return tomSymbol.GetTypesToType().GetCodomain()
			}
			return `EmptyType()
		}
		(Variable|VariableStar)[AstType=ty] -> { return `ty }
		AntiTerm(term) -> { return GetTermType(`term, symbolTable) }
	}
	panic(&platform.RuntimeError{Msg: "getTermType error on term: " + platform.JavaString(t)})
}

// GetTermTypeBQ is getTermType(BQTerm, SymbolTable) (TomBase.t:569).
func GetTermTypeBQ(t BQTerm, symbolTable *SymbolTable) TomType {
	%match(t) {
		(BQVariable|BQVariableStar)[AstType=ty] -> { return `ty }
		FunctionCall[AstType=ty] -> { return `ty }
		ExpressionToBQTerm(expr) -> { return GetTermTypeExpr(`expr, symbolTable) }
		ListHead[Codomain=ty] -> { return `ty }
		ListTail[Variable=term] -> { return GetTermTypeBQ(`term, symbolTable) }
		Subterm[AstName=Name(name), SlotName=slotName] -> {
			return GetSlotType(symbolTable.GetSymbolFromName(`name), `slotName)
		}
		(BQAppl|BuildConstant|BuildTerm|BuildEmptyList|BuildConsList|BuildAppendList|BuildEmptyArray|BuildConsArray|BuildAppendArray)[AstName=Name(name)] -> {
			if tomSymbol := symbolTable.GetSymbolFromName(`name); tomSymbol != nil {
				return tomSymbol.GetTypesToType().GetCodomain()
			}
			return `EmptyType()
		}
	}
	panic(&platform.RuntimeError{Msg: "getTermType error on term: " + platform.JavaString(t)})
}

// GetTermTypeExpr is getTermType(Expression, SymbolTable) (TomBase.t:603).
// port: Java also prints the error on stdout, which carries the generated
// code here.
func GetTermTypeExpr(t Expression, symbolTable *SymbolTable) TomType {
	%match(t) {
		(GetHead|GetSlot|GetElement)[Codomain=ty] -> { return `ty }
		BQTermToExpression[AstTerm=bqterm] -> { return GetTermTypeBQ(`bqterm, symbolTable) }
		Conditional[Cond=cond] -> { return GetTermTypeExpr(`cond, symbolTable) }
		IsFsym[Variable=bqterm] -> { return GetTermTypeBQ(`bqterm, symbolTable) }
		GetTail[Variable=term] -> { return GetTermTypeBQ(`term, symbolTable) }
		GetSliceList[VariableBeginAST=term] -> { return GetTermTypeBQ(`term, symbolTable) }
		GetSliceArray[SubjectListName=term] -> { return GetTermTypeBQ(`term, symbolTable) }
		Cast[AstType=ty] -> { return `ty }
		AddOne[Variable=bqterm] -> { return GetTermTypeBQ(`bqterm, symbolTable) }
		Integer[] -> { return symbolTable.GetType("int") }
	}
	panic(&platform.RuntimeError{Msg: "getTermType error on term: " + platform.JavaString(t)})
}

var _ = aterm.GetInstance
```

Chaque règle porte la même règle Java, dans le même ordre (l'ordre compte
quand deux règles filtrent le même terme). Si `tom -go` refuse une
construction (disjonction de symboles, `%match` sans sujet avec `||`,
`concOption(_*, …, _*)`) : s'arrêter (« Risques et conditions d'arrêt »),
ne pas réécrire la règle autrement.

- [ ] **Step 3 : génération, tests, amorce**

Run: `cd /Users/pem/github/tom && mage -d magefiles -w . regenTomGo && (cd src/go/tom && go vet ./engine/tools/ && go test -count=1 ./engine/tools/) && mage -d magefiles -w . promoteGo && mage -d magefiles -w . bootstrap`
Expected: `1 written, 2 unchanged` ; tests verts ; amorce mise à jour.

- [ ] **Step 4 : vérification et commit**

Run (une fois) : `cd /Users/pem/github/tom && unset TOM_HOME && ./build.sh src.dist 2>&1 | tail -1 && mage -d magefiles -w . all && mage -d magefiles -w . j4`
Expected: tout vert.

```bash
P="src/go/tom/engine/tools/tombase.t src/go/tom/engine/tools/tombase_test.go stable/go/tom"
git add $P
git commit --only $P -m "tom-go J4.1a: tombase.t (TomBase subset of the backend)" -m "Co-Authored-By: Claude Sonnet 5.5 <noreply@anthropic.com>"
```

**Acceptance:** `tombase.t` compilé par `regenTomGo` ; tests verts, cas
d'erreur compris (messages identiques à ceux de Java).

---

### Task 6 : équivalence Java des requêtes `SymbolTable`/`TomBase` sur les paquets du pont

**Modèle : Sonnet** (deux programmes jumeaux entièrement donnés ; un écart
qui ne s'explique pas en une analyse : s'arrêter et rapporter, le
contrôleur décide d'une reprise par Opus).

**Files:**
- Create: `test/tools/j4/java/SymbolTableQueries.java`, `test/tools/j4/queries_test.go`
- Modify: `magefiles/magefile.go` (`BridgeCheck` lance aussi `TestSymbolTableQueries`)

**Interfaces:**
- Consumes: `javaHelpers` (Task 2), `tools.FromBundle` et le sous-ensemble (Tasks 4–5), `platform.CatchRuntime`, `platform.JavaString`, `bridge.Read`, `harness.DumpsEnv` (`TOMGO_DUMPS`).
- Produces: `TestSymbolTableQueries` (lancé par `mage j4` sur un petit corpus et par `mage bridgeCheck` sur tous les paquets T1 + smoke).

Principe : pour un paquet, le programme Java (classes de `src/dist`) et le
programme Go (`tom/engine/tools`) construisent la table (symboles, types,
marques) et écrivent les mêmes lignes : par symbole, son codomaine, son
domaine, `getTomType`/`getTLType` du codomaine, ses marques, `getSlotName`
de chaque position (plus une hors bornes), `getSlotIndex`/`getSlotType` de
chaque slot ; par nom de type (builtins, types déclarés, codomaines),
`getType`, `isUsedType`, `isBuiltinType`, `isUnknownType`,
`getBuiltinType`, `is…Type` ; les types builtin ; marques et types triés ;
`toTerm` ; puis, en parcours préfixe du code, `getTermType` de chaque
`TomTerm`, `BQTerm` et `Expression`, `tomNumberListToString` de chaque
`TomNumberList`, `getModuleName` de chaque `OptionList` ; enfin
`goDeclPrefix` de `input` et `inputname`. Une exception d'exécution
s'écrit `!error`, `null` s'écrit `null`. Les sorties doivent être
identiques octet pour octet.

- [ ] **Step 1 : programme Java**

`test/tools/j4/java/SymbolTableQueries.java` :

```java
import java.io.*;
import java.util.*;

import tom.engine.TomBase;
import tom.engine.tools.SymbolTable;
import tom.engine.adt.code.types.BQTerm;
import tom.engine.adt.code.types.Code;
import tom.engine.adt.tomexpression.types.Expression;
import tom.engine.adt.tomname.types.TomNumberList;
import tom.engine.adt.tomoption.types.OptionList;
import tom.engine.adt.tomsignature.types.KeyEntry;
import tom.engine.adt.tomsignature.types.TomSymbol;
import tom.engine.adt.tomsignature.types.TomSymbolTable;
import tom.engine.adt.tomslot.types.PairNameDecl;
import tom.engine.adt.tomslot.types.PairNameDeclList;
import tom.engine.adt.tomterm.types.TomTerm;
import tom.engine.adt.tomtype.types.TomType;
import tom.library.sl.Visitable;
import tom.library.sl.VisitableBuiltin;

/**
 * Answers the queries of the backend on SymbolTable and TomBase (J4.1a,
 * test/tools/j4/queries_test.go; goQueries writes the same lines in Go).
 * Args: <outDir> <bundle>...; for the k-th bundle (from 0) it writes
 * <outDir>/<k>.txt in UTF-8.
 */
public class SymbolTableQueries {
  interface Query { Object get(); }

  private static String q(Query f) {
    try {
      Object o = f.get();
      return o == null ? "null" : o.toString();
    } catch(RuntimeException e) {
      return "!error";
    }
  }

  private static final String[] NAMES = {"int", "long", "float", "char", "double", "boolean", "String",
    "universal", "void", "intarray", "unknown type", "nosuchtype"};

  private final SymbolTable st = new SymbolTable();
  private final StringBuilder out = new StringBuilder();
  private int index = 0;

  private void emit(String s) { out.append(s).append('\n'); }

  public static void main(String[] args) throws Exception {
    // TomBase and SymbolTable print some errors on System.out: keep them out
    System.setOut(new PrintStream(new ByteArrayOutputStream()));
    for(int k = 1; k < args.length; k++) {
      String res = new SymbolTableQueries().run(args[k]);
      Writer w = new OutputStreamWriter(new FileOutputStream(new File(args[0], (k-1) + ".txt")), "UTF-8");
      w.write(res);
      w.close();
    }
  }

  private String run(String bundle) throws IOException {
    st.init(new StubOptionManager().set("goCode", Boolean.TRUE));
    String input = "", inputName = "";
    Code code = null;
    BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(bundle), "UTF-8"));
    for(String line; (line = in.readLine()) != null; ) {
      int sp = line.indexOf(' ');
      String key = sp < 0 ? line : line.substring(0, sp);
      String rest = sp < 0 ? "" : line.substring(sp + 1);
      if(key.equals("input")) {
        input = rest;
      } else if(key.equals("inputname")) {
        inputName = Quote.unquote(rest);
      } else if(key.equals("used")) {
        KeyEntry ke = KeyEntry.fromString(rest);
        if(ke.isUsedSymbolConstructor()) {
          st.setUsedSymbolConstructor(ke.getSymbol());
        } else if(ke.isUsedSymbolDestructor()) {
          st.setUsedSymbolDestructor(ke.getSymbol());
        } else if(ke.isUsedSymbolAC()) {
          st.setUsedSymbolAC(ke.getSymbol());
        } else {
          st.setUsedType(ke.getTypedef());
        }
      } else if(key.equals("type")) {
        TomType t = TomType.fromString(rest);
        st.putType(t.getTomType(), t);
      } else if(key.equals("table")) {
        st.fromTerm(TomSymbolTable.fromString(rest));
      } else if(key.equals("code")) {
        code = Code.fromString(rest);
      }
    }
    in.close();
    symbols();
    types();
    emit("builtins " + st.getIntType() + " " + st.getIntArrayType() + " " + st.getLongType() + " " + st.getFloatType()
        + " " + st.getCharType() + " " + st.getDoubleType() + " " + st.getBooleanType() + " " + st.getStringType()
        + " " + st.getUniversalType() + " " + st.getVoidType() + " " + st.getIntArrayOp());
    for(KeyEntry ke : st.getUsedKeyEntries()) {
      emit("usedEntry " + ke);
    }
    for(TomType t : st.getTypeEntries()) {
      emit("typeEntry " + t);
    }
    emit("table " + st.toTerm());
    walk(code);
    emit("prefix " + TomBase.goDeclPrefix(input) + " " + TomBase.goDeclPrefix(inputName));
    return out.toString();
  }

  private void symbols() {
    for(final String name : st.keySymbolIterable()) {
      final TomSymbol s = st.getSymbolFromName(name);
      final TomType codomain = TomBase.getSymbolCodomain(s);
      emit("symbol " + name);
      emit("  codomain " + codomain);
      emit("  domain " + TomBase.getSymbolDomain(s));
      emit("  tomType " + q(() -> TomBase.getTomType(codomain)));
      emit("  tlType " + q(() -> TomBase.getTLType(codomain)));
      emit("  used " + st.isUsedSymbolConstructor(s) + " " + st.isUsedSymbolDestructor(s) + " " + st.isUsedSymbolAC(s)
          + " " + st.isUsedSymbolConstructor(name) + " " + st.isUsedSymbolDestructor(name));
      int n = s.getPairNameDeclList().length();
      for(int i = 0; i <= n; i++) {
        final int k = i;
        emit("  slotName " + i + " " + q(() -> TomBase.getSlotName(s, k)));
      }
      for(PairNameDeclList l = s.getPairNameDeclList(); !l.isEmptyconcPairNameDecl(); l = l.getTailconcPairNameDecl()) {
        final PairNameDecl p = l.getHeadconcPairNameDecl();
        emit("  slot " + p.getSlotName() + " index " + TomBase.getSlotIndex(s, p.getSlotName())
            + " type " + q(() -> TomBase.getSlotType(s, p.getSlotName())));
      }
    }
  }

  private void types() {
    TreeSet<String> names = new TreeSet<String>(Arrays.asList(NAMES));
    for(TomType t : st.getTypeEntries()) {
      if(t.isType()) {
        names.add(t.getTomType());
      }
    }
    for(String name : st.keySymbolIterable()) {
      TomType c = TomBase.getSymbolCodomain(st.getSymbolFromName(name));
      if(c.isType()) {
        names.add(c.getTomType());
      }
    }
    for(final String n : names) {
      emit("type " + n + " get " + q(() -> st.getType(n)) + " used " + st.isUsedType(n)
          + " builtin " + st.isBuiltinType(n) + " unknown " + st.isUnknownType(n)
          + " builtinType " + q(() -> st.getBuiltinType(n))
          + " is " + st.isIntType(n) + " " + st.isLongType(n) + " " + st.isFloatType(n) + " " + st.isCharType(n)
          + " " + st.isStringType(n) + " " + st.isBooleanType(n) + " " + st.isDoubleType(n));
    }
  }

  private void walk(final Object node) {
    if(!(node instanceof Visitable) || node instanceof VisitableBuiltin) {
      return;
    }
    int i = index++;
    if(node instanceof TomTerm) {
      emit("termType T " + i + " " + q(() -> TomBase.getTermType((TomTerm)node, st)));
    }
    if(node instanceof BQTerm) {
      emit("termType B " + i + " " + q(() -> TomBase.getTermType((BQTerm)node, st)));
    }
    if(node instanceof Expression) {
      emit("termType E " + i + " " + q(() -> TomBase.getTermType((Expression)node, st)));
    }
    if(node instanceof TomNumberList) {
      emit("numberList " + i + " " + q(() -> TomBase.tomNumberListToString((TomNumberList)node)));
    }
    if(node instanceof OptionList) {
      emit("moduleName " + i + " " + q(() -> TomBase.getModuleName((OptionList)node)));
    }
    Visitable v = (Visitable)node;
    for(int k = 0; k < v.getChildCount(); k++) {
      walk(v.getChildAt(k));
    }
  }
}
```

- [ ] **Step 2 : programme Go et test**

`test/tools/j4/queries_test.go` :

```go
package j4

import (
	"fmt"
	"io/fs"
	"os"
	"path/filepath"
	"sort"
	"strings"
	"testing"
	"time"

	"tom/engine/adt"
	"tom/engine/bridge"
	"tom/engine/platform"
	"tom/engine/tools"
	"tom/library/sharedobjects"
	"tomtools/harness"
)

// queryCorpus are the sources (under test/go) whose bundles mage j4
// queries; mage bridgeCheck queries those of T1 and of the smoke tests.
var queryCorpus = []string{"Peano.t", "TestArray.t", "acmatching/TestAC.t", "TestStrategy.t", "gom/TestList.t"}

var queryTypeNames = []string{"int", "long", "float", "char", "double", "boolean", "String",
	"universal", "void", "intarray", "unknown type", "nosuchtype"}

type queries struct {
	st    *tools.SymbolTable
	out   strings.Builder
	index int
}

func (q *queries) emit(s string) {
	q.out.WriteString(s)
	q.out.WriteByte('\n')
}

// try is q() of SymbolTableQueries.java: the Java text of the value,
// "null" for nil, "!error" for a runtime exception.
func try(f func() any) string {
	var v any
	if !platform.CatchRuntime(func() { v = f() }) {
		return "!error"
	}
	return platform.JavaString(v)
}

// goQueries writes, for a bundle, the lines SymbolTableQueries.java
// writes in Java.
func goQueries(b *bridge.Bundle) string {
	q := &queries{st: tools.FromBundle(b)}
	st := q.st
	q.symbols()
	q.types()
	q.emit("builtins " + strings.Join([]string{
		fmt.Sprint(st.GetIntType()), fmt.Sprint(st.GetIntArrayType()), fmt.Sprint(st.GetLongType()),
		fmt.Sprint(st.GetFloatType()), fmt.Sprint(st.GetCharType()), fmt.Sprint(st.GetDoubleType()),
		fmt.Sprint(st.GetBooleanType()), fmt.Sprint(st.GetStringType()), fmt.Sprint(st.GetUniversalType()),
		fmt.Sprint(st.GetVoidType()), st.GetIntArrayOp(),
	}, " "))
	for _, ke := range st.GetUsedKeyEntries() {
		q.emit("usedEntry " + fmt.Sprint(ke))
	}
	for _, ty := range st.GetTypeEntries() {
		q.emit("typeEntry " + fmt.Sprint(ty))
	}
	q.emit("table " + fmt.Sprint(st.ToTerm()))
	q.walk(b.Code)
	q.emit("prefix " + tools.GoDeclPrefix(b.Input) + " " + tools.GoDeclPrefix(b.InputName))
	return q.out.String()
}

func (q *queries) symbols() {
	for _, name := range q.st.KeySymbols() {
		s := q.st.GetSymbolFromName(name)
		codomain := tools.GetSymbolCodomain(s)
		q.emit("symbol " + name)
		q.emit("  codomain " + platform.JavaString(codomain))
		q.emit("  domain " + platform.JavaString(tools.GetSymbolDomain(s)))
		q.emit("  tomType " + try(func() any {
			n, ok := tools.GetTomType(codomain)
			if !ok {
				return nil
			}
			return n
		}))
		q.emit("  tlType " + try(func() any { return tools.GetTLType(codomain) }))
		q.emit(fmt.Sprintf("  used %v %v %v %v %v", q.st.IsUsedSymbolConstructor(s), q.st.IsUsedSymbolDestructor(s),
			q.st.IsUsedSymbolAC(s), q.st.IsUsedSymbolConstructorName(name), q.st.IsUsedSymbolDestructorName(name)))
		n := s.GetPairNameDeclList().Length()
		for i := 0; i <= n; i++ {
			q.emit(fmt.Sprintf("  slotName %d %s", i, try(func() any { return tools.GetSlotName(s, i) })))
		}
		for l := s.GetPairNameDeclList(); !l.IsEmptyconcPairNameDecl(); l = l.GetTailconcPairNameDecl() {
			p := l.GetHeadconcPairNameDecl()
			q.emit(fmt.Sprintf("  slot %s index %d type %s", platform.JavaString(p.GetSlotName()),
				tools.GetSlotIndex(s, p.GetSlotName()), try(func() any { return tools.GetSlotType(s, p.GetSlotName()) })))
		}
	}
}

func (q *queries) types() {
	set := map[string]bool{}
	for _, n := range queryTypeNames {
		set[n] = true
	}
	add := func(t adt.TomType) {
		if t.IsType() {
			set[t.GetTomType()] = true
		}
	}
	for _, t := range q.st.GetTypeEntries() {
		add(t)
	}
	for _, name := range q.st.KeySymbols() {
		add(tools.GetSymbolCodomain(q.st.GetSymbolFromName(name)))
	}
	names := make([]string, 0, len(set))
	for n := range set {
		names = append(names, n)
	}
	sort.Strings(names)
	st := q.st
	for _, n := range names {
		q.emit(fmt.Sprintf("type %s get %s used %v builtin %v unknown %v builtinType %s is %v %v %v %v %v %v %v",
			n, try(func() any { return st.GetType(n) }), st.IsUsedTypeName(n), st.IsBuiltinType(n), st.IsUnknownType(n),
			try(func() any { return st.GetBuiltinType(n) }),
			st.IsIntType(n), st.IsLongType(n), st.IsFloatType(n), st.IsCharType(n),
			st.IsStringType(n), st.IsBooleanType(n), st.IsDoubleType(n)))
	}
}

// gomTerm is a term of the engine ADT; the other children (strings,
// numbers: Java's VisitableBuiltin) are skipped, as in Java.
type gomTerm interface {
	sharedobjects.Term
	ChildCount() int
	ChildAt(i int) any
}

func (q *queries) walk(node any) {
	v, ok := node.(gomTerm)
	if !ok {
		return
	}
	i := q.index
	q.index++
	if t, ok := node.(adt.TomTerm); ok {
		q.emit(fmt.Sprintf("termType T %d %s", i, try(func() any { return tools.GetTermType(t, q.st) })))
	}
	if t, ok := node.(adt.BQTerm); ok {
		q.emit(fmt.Sprintf("termType B %d %s", i, try(func() any { return tools.GetTermTypeBQ(t, q.st) })))
	}
	if t, ok := node.(adt.Expression); ok {
		q.emit(fmt.Sprintf("termType E %d %s", i, try(func() any { return tools.GetTermTypeExpr(t, q.st) })))
	}
	if l, ok := node.(adt.TomNumberList); ok {
		q.emit(fmt.Sprintf("numberList %d %s", i, try(func() any { return tools.TomNumberListToString(l) })))
	}
	if l, ok := node.(adt.OptionList); ok {
		q.emit(fmt.Sprintf("moduleName %d %s", i, try(func() any {
			m, ok := tools.GetModuleName(l)
			if !ok {
				return nil
			}
			return m
		})))
	}
	for k := 0; k < v.ChildCount(); k++ {
		q.walk(v.ChildAt(k))
	}
}

// bundles returns the bridge bundles to query: every *.tfix.bridge of
// TOMGO_DUMPS when set (mage bridgeCheck), otherwise those of queryCorpus,
// compiled here with --goFrom Backend --goCheck --intermediate.
func bundles(t *testing.T, dist harness.Dist) (paths []string, floor int) {
	t.Helper()
	collect := func(dir string) {
		filepath.WalkDir(dir, func(p string, d fs.DirEntry, err error) error {
			if err == nil && !d.IsDir() && strings.HasSuffix(p, ".tfix.bridge") {
				paths = append(paths, p)
			}
			return nil
		})
	}
	if dumps := os.Getenv(harness.DumpsEnv); dumps != "" {
		collect(dumps)
		sort.Strings(paths)
		return paths, 110
	}
	root := harness.RepoRoot(t)
	out := t.TempDir()
	for k, rel := range queryCorpus {
		dir := filepath.Join(out, fmt.Sprint(k))
		src := filepath.Join(harness.GoTestDir(root), filepath.FromSlash(rel))
		if msg, err := dist.Compile(t, src, dir, "-go", "-np", "--goFrom", "Backend", "--goCheck", "--intermediate"); err != nil {
			t.Fatalf("tom -go %s: %v\n%s", rel, err, msg)
		}
		collect(dir)
	}
	sort.Strings(paths)
	return paths, len(queryCorpus)
}

// TestSymbolTableQueries compares the answers of the Go SymbolTable and
// TomBase (tom/engine/tools) with those of Java on bridge bundles: the
// outputs of goQueries and SymbolTableQueries.java must be identical.
func TestSymbolTableQueries(t *testing.T) {
	dist := harness.NewDist(t)
	cp := javaHelpers(t, dist)
	paths, floor := bundles(t, dist)
	if len(paths) < floor {
		t.Fatalf("%d bundles, want at least %d", len(paths), floor)
	}
	outDir := t.TempDir()
	args := append([]string{"-cp", cp, "SymbolTableQueries", outDir}, paths...)
	if msg, err := harness.RunInTimeout(outDir, 10*time.Minute, nil, "java", args...); err != nil {
		t.Fatalf("java SymbolTableQueries: %v\n%s", err, msg)
	}
	same, differ := 0, 0
	for k, p := range paths {
		want, err := os.ReadFile(filepath.Join(outDir, fmt.Sprint(k)+".txt"))
		if err != nil {
			t.Fatal(err)
		}
		f, err := os.Open(p)
		if err != nil {
			t.Fatal(err)
		}
		b, err := bridge.Read(f)
		f.Close()
		if err != nil {
			t.Fatalf("%s: %v", p, err)
		}
		got := goQueries(b)
		if got == string(want) {
			same++
			continue
		}
		differ++
		g, w := strings.Split(got, "\n"), strings.Split(string(want), "\n")
		for i := 0; i < len(g) || i < len(w); i++ {
			var gl, wl string
			if i < len(g) {
				gl = g[i]
			}
			if i < len(w) {
				wl = w[i]
			}
			if gl != wl {
				t.Errorf("%s: line %d differs\nGo   %.300s\nJava %.300s", p, i+1, gl, wl)
				break
			}
		}
	}
	t.Logf("requêtes SymbolTable/TomBase : paquets %d, identiques %d, différents %d", len(paths), same, differ)
}
```

(Si un fichier du corpus demande des options propres — voir `TomArgs`
dans `test/tools/parity/parity_test.go` — et ne se compile pas avec
`-go -np` seuls, le remplacer par `TestList.t` et le dire dans le commit.)

- [ ] **Step 3 : `bridgeCheck`**

Dans `magefiles/magefile.go`, `BridgeCheck` : remplacer `"-run",
"TestDumpsRoundTrip"` par `"-run", "TestDumpsRoundTrip|TestSymbolTableQueries"`
et compléter son commentaire (« … then re-reads every dump in Go and
compares the queries of the Go SymbolTable/TomBase with Java on every
bundle »).

- [ ] **Step 4 : exécution**

Run: `cd /Users/pem/github/tom && unset TOM_HOME && ./build.sh src.dist 2>&1 | tail -1 && (cd test/tools && TOMGO_REQUIRE_TOOLS=1 go test -count=1 -v ./j4/ -run TestSymbolTableQueries 2>&1 | grep -E 'requêtes|ok|FAIL|differs')`
Expected: `requêtes SymbolTable/TomBase : paquets 5, identiques 5, différents 0` ; `ok`.

Run: `mage -d magefiles -w . bridgeCheck 2>&1 | tee /tmp/bridgecheck-j41a.log | grep -E 'identiques|requêtes|^ok|FAIL'`
Expected: P0 inchangé (`total 1139, identiques 1139, différents 0`) ;
`requêtes SymbolTable/TomBase : paquets 110, identiques 110, différents 0`.

Un écart : lire la ligne Java et la ligne Go, trouver la méthode Java en
cause et corriger le port (`symboltable.t`, `tombase.t`, puis
`regenTomGo`, `promoteGo`, `bootstrap`, `./build.sh src.dist`) ; jamais
les programmes de requêtes pour masquer une différence. Une différence
d'ordre (`KeySymbols`, `GetUsedKeyEntries`) due à des caractères hors
BMP : la documenter en `// port:` et s'arrêter pour décision.

- [ ] **Step 5 : vérification et commit**

Run (une fois) : `mage -d magefiles -w . all && mage -d magefiles -w . j4`
Expected: tout vert.

```bash
P="test/tools/j4/java/SymbolTableQueries.java test/tools/j4/queries_test.go magefiles/magefile.go"
git add $P
git commit --only $P -m "tom-go J4.1a: SymbolTable/TomBase queries identical to Java on the bridge bundles" \
  -m "mage j4: 5 bundles; mage bridgeCheck: 110 bundles (T1 + smoke), 0 different." -m "Co-Authored-By: Claude Sonnet 5.5 <noreply@anthropic.com>"
```
(Ajouter `src/go/tom/engine/tools/*.t` et `stable/go/tom` si un écart a
conduit à corriger le port.)

**Acceptance:** `TestSymbolTableQueries` : 5 paquets sous `mage j4`, 110
sous `mage bridgeCheck`, 0 différent ; P0 inchangé.

---

### Task 7 : clôture du jalon J4.1a

**Modèle : Sonnet** (vérifications, documentation) ; revue finale du
jalon : **Opus** (politique de l'utilisateur : revues finales).

**Files:**
- Create: `doc/go/j4-1a-report.md`
- Modify: `doc/go/j4-dashboard.md`, `doc/go/README.md`, `CLAUDE.md`, `stable/go/tom/**` (si `promoteGo` change encore quelque chose)
- Hors dépôt (non suivi) : `.superpowers/sdd/progress.md` (journal)

- [ ] **Step 1 : tout relancer depuis un état propre**

`git status --short` ne montre que les fichiers non suivis connus
(`.DS_Store`, `build.xml.bak*`, `tomgo/`). Puis, une fois :

```bash
cd /Users/pem/github/tom && unset TOM_HOME
./build.sh stable 2>&1 | tail -1 && ./build.sh src.dist 2>&1 | tail -1
mage -d magefiles -w . regenTomGo        # attendu : 0 written
mage -d magefiles -w . all
mage -d magefiles -w . j4                # TestOutputCodeJava, TestTomGoRegen, TestSymbolTableQueries…
mage -d magefiles -w . bridgeCheck
mage -d magefiles -w . portGo && mage -d magefiles -w . coverage
mage -d magefiles -w . portJava          # ~4 min
(cd test/tools && go test -count=1 ./smoke/)
```
Expected: tout vert ; comptes de référence (Global Constraints) ;
`doc/go/port-dashboard.md` et `doc/go/coverage.md` inchangés hors date
(sinon s'arrêter).

- [ ] **Step 2 : tableau de bord**

`doc/go/j4-dashboard.md` : une section « J4.1a — fondations » : scripts
`OutputCode` (identiques / différents), paquets interrogés
(`SymbolTable`/`TomBase` : 5 sous `mage j4`, 110 sous `bridgeCheck`,
identiques / différents), sources Tom+Go compilées par `regenTomGo` (3),
date, commandes.

- [ ] **Step 3 : rapport**

`doc/go/j4-1a-report.md`, au format de `doc/go/j4-0-report.md` : résultat
en un paragraphe ; tableau des commits par tâche ; disposition retenue
(paquetages, `X.t.go`, `regenTomGo`, amorce) ; sous-ensemble porté (renvoi
à ce plan, écarts `// port:` relevés par `grep -n 'port:' src/go/tom/engine`) ;
vérifications finales (commande → résultat) ; points ouverts pour J4.1b
(relais des lignes `INFO` par `GoBridgePlugin`, `inline` sans effet avec
`-go`, binaires à reconstruire après une régénération, limites du parser
îlot rencontrées) ; « Tout relancer ».

- [ ] **Step 4 : documentation**

`doc/go/README.md` : « où sont les choses » (ligne « Compilateur en
Tom+Go : `src/go/tom/engine/{platform,tools}` », rapport J4.1a) et
« Orientation pour la suite » (J4.1a fait ; prochain plan : J4.1b, backend
Go). `CLAUDE.md` : §1 (J4.1a livré, prochain : J4.1b), §2/§3 (paquetages
`engine/platform`, `engine/tools`, règle `X.t` → `X.t.go`), §9 (dernier
rapport : `doc/go/j4-1a-report.md`).

- [ ] **Step 5 : commit**

```bash
P="doc/go/j4-1a-report.md doc/go/j4-dashboard.md doc/go/README.md CLAUDE.md"
git add $P
git commit --only $P -m "tom-go J4.1a: close milestone (report, docs)" -m "Co-Authored-By: Claude Sonnet 5.5 <noreply@anthropic.com>"
```
(Ajouter `doc/go/port-dashboard.md`/`doc/go/coverage.md` s'ils n'ont
changé que par leur date.)

- [ ] **Step 6 : journal (non commité)**

Ajouter à `.superpowers/sdd/progress.md` une ligne par tâche (commit,
modèle, résultat) et la ligne de clôture « J4.1a COMPLETE … NEXT: J4.1b
plan (backend Go) ».

**Acceptance (fin de J4.1a, spec §6) :** `OutputCode`, sous-ensembles
`SymbolTable`/`TomBase`, options et messages en Go et Tom+Go, avec tests
unitaires verts ; `.t` compilés par `mage regenTomGo` avec `src/dist` ;
équivalence avec Java : `OutputCode` sur 407 scripts, requêtes sur 110
paquets ; T0, T1, T2, T3, smoke et P0 inchangés.

---

## Risques et conditions d'arrêt

| Risque | Conduite |
|---|---|
| `tom -go` n'accepte pas une construction Tom du port (disjonction de symboles, `%match` sans sujet avec `||`, backquote dans une expression, variable de motif) ou produit du Go faux | **s'arrêter et rapporter** ; la correction se fait dans `src/tom` (compilateur Java, Opus), avec une fixture smoke (`test/go/smoke/testdata/<nom>/`) qui reproduit le cas, dans un commit à part, `./build.sh src.dist`, smoke, T1 et T0 inchangés ; **jamais** de contournement dans le `.t` porté |
| `go vet` rejette le code généré par `tom -go` (vet de `mage vet`, de `promoteGo` sur l'amorce) | même conduite : défaut du `GoGenerator` Java, corrigé avec une fixture smoke |
| Nom de `tools` en conflit avec un nom exporté de `adt` (import `.`) | renommer le nom Go de `tools`, l'écrire dans le commit et dans la table « Sous-ensemble porté » |
| `.t.go` de `src` périmé ou absent | `mage tomGoFresh` (préalable de `test`, `vet`, `j4`, `bridgeCheck`) le refuse ; `regenTomGo` (avec `src/dist`) ou suppression du `.t.go` pour reprendre celui de l'amorce |
| Amorce en retard sur les `.t` commités | `TestTomGoRegen` (`regentomgo -check`) dans `mage j4` ; chaque tâche qui touche un `.t` finit par `promoteGo` + `bootstrap` et committe `stable/go/tom` |
| Binaires de la dist périmés après une régénération | `CheckFresh` les détecte ; `./build.sh src.dist` avant les harnais (Global Constraints) |
| Écart Java/Go dans les requêtes (nul contre exception, ordre des noms, liste dont la queue n'est pas une liste) | corriger le port ; un écart d'ordre dû à des caractères hors BMP se documente en `// port:` et se soumet au contrôleur |
| Formatage des nombres des messages (`MessageFormat` suit la locale Java) | séparateur `,` en Go ; seulement dans les messages `INFO` ; noté au rapport |
| Lignes `INFO` de `tomgo` non relayées par `GoBridgePlugin` | noté pour J4.1b (le pilote Go en émettra) |
| Modification nécessaire de `legacy/`, chute d'un compte de référence, refus de permission | s'arrêter et rapporter |
