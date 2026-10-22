# Backend Go pour Tom+Java — plan d'implémentation (J−1 → J3)

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** ajouter au compilateur Tom+Java une cible Go (`-go`) validée par
comportement contre la version d'origine figée dans `legacy/`.

**Architecture:** `legacy/` = snapshot 100 % humain (commit `2918e95c7`,
pointe de `v3`) du compilateur Tom+Java, constructible seul. La racine
(`src/`, `stable/`) devient la version étendue : option `goCode`,
`GoGenerator.t`, branche Go de l'expander, délégation des `%gom` au binaire
`tomgo gom-block`. Les harnais Go (module `tom/tomgo`) comparent la nouvelle
chaîne à `legacy/` : T0 (sortie Java identique), T1 (Go de référence),
T2 (même comportement que l'original), T3 (couverture des constructions
utilisées par le compilateur).

**Tech Stack:** Tom+Java (`.t`), Ant via `./build.sh`, JDK 8 (Zulu 8 installé),
Go 1.26, `mage`.

Spec : `docs/superpowers/specs/2026-10-22-tom-go-backend-design.md`.

## Global Constraints

- `legacy/` est extrait de `2918e95c7` et **n'est plus jamais modifié** après la Task 1 (témoin du travail humain).
- Toutes les modifications du compilateur se font dans `src/` (puis promues dans `stable/` en Task 10).
- Avec l'option Go, la sortie `--java` (défaut) du nouveau compilateur reste octet-pour-octet identique à `legacy/` (T0).
- Gom n'est pas réécrit : le Tom Java délègue les `%gom` à `tomgo gom-block`.
- Toujours `unset TOM_HOME` (ou le positionner explicitement) avant d'appeler un `bin/tom` : le shell de l'utilisateur exporte `TOM_HOME=/USers/pem/github/tom/src/dist`.
- JDK 8 pour construire (`java -version` → `1.8.0_504`).
- Le Go généré est émis sur une seule ligne logique : chaque instruction finit par `;` et chaque bloc par `};` (sauf avant `else`). La syntaxe a été vérifiée : `package p; func f(t any) int { var x int = 1; _ = x; { if x == 1 { return 2 } else { x = 3; }; }; lbl: for { break lbl; }; return any(x).(int) }; func g() {};` compile.
- Les fichiers Tom+Go portés n'utilisent **pas** de littéraux chaîne bruts Go (`` `…` ``), réservés au backquote Tom.
- Messages de commit : `tom-go <jalon>: <verbe> <objet>`, terminés par `Co-Authored-By: Claude Opus 5.5 <noreply@anthropic.com>`.

## File Structure

| Fichier | Responsabilité |
|---|---|
| `legacy/**` | snapshot humain + `legacy/README.md` (installation) |
| `README.md` (racine) | section « Two versions » qui renvoie vers `legacy/` |
| `src/tom/engine/backend/GoGenerator.t` | **nouveau** — générateur Go |
| `src/tom/engine/backend/BackendPlugin.t` | option `goCode`, sélection du générateur |
| `src/tom/engine/TomStreamManager.java` | suffixe `.go`, mappings `go/` |
| `src/tom/engine/tools/SymbolTable.t` | types builtin Go |
| `src/tom/engine/expander/ExpanderPlugin.t` | branche Go des `%strategy` |
| `src/tom/engine/parser/antlr4/CstConverter.t` | mapping `%op Strategy` en Go |
| `src/tom/engine/parser/TomParserPlugin.t` | positionne `CstConverter.goTarget` |
| `src/tom/engine/parser/TomParserTool.t` | délégation `%gom` → `tomgo gom-block` |
| `src/tom/library/mapping/go/*.tom` | **nouveau** — mappings Go (copie de tomgo) |
| `build.xml` | copie de `go/**/*.tom` dans les dist |
| `tomgo/stable/library/sl/must.go` | `MustVisit` / `RecoverVisitFailure` |
| `tomgo/cmd/tomgo/gomblock.go` | sous-commande `gom-block` |
| `tomgo/tests/tomjava/harness.go` | utilitaires partagés : dist Tom, module Go temporaire |
| `tomgo/tests/javaparity/javaparity_test.go` | T0 |
| `tomgo/tests/port/**` | cas portés en Tom+Go (miroir de `test/`) |
| `tomgo/tests/port/golden/*.go` | références T1 |
| `tomgo/tests/port/oracle/*.json` | résultats `legacy/` mis en cache (T2) |
| `tomgo/tests/port/port_test.go` | T1 + T2 + tableau de bord |
| `tomgo/tests/coverage/coverage_test.go` | T3 |
| `tomgo/magefile.go` | cibles `PortJava`, `PortGo`, `Coverage` |

---

### Task 1 (J−1) : snapshot humain `legacy/` + documentation d'installation

**Files:**
- Create: `legacy/**` (extrait de `2918e95c7`), `legacy/README.md`
- Modify: `README.md`

**Interfaces:**
- Produces: `legacy/src/dist/bin/tom` (oracle utilisé par Tasks 2 et 8), après `cd legacy && ./build.sh stable && ./build.sh src.dist`.

- [ ] **Step 1 : vérifier que `2918e95c7` est la pointe humaine de `v3`**

Run: `git log --oneline -1 v3 && git merge-base v3 HEAD`
Expected: les deux affichent `2918e95c7…` (« simple debug everywhere »).

- [ ] **Step 2 : extraire le snapshot**

```bash
cd /Users/pem/github/tom
mkdir legacy
git archive 2918e95c7 src stable share utils build.sh build.xml \
  local.properties_template README NEWS AUTHORS LICENCE | tar -x -C legacy
ls legacy
```
Expected: `AUTHORS LICENCE NEWS README build.sh build.xml local.properties_template share src stable utils`.

- [ ] **Step 3 : construire avec JDK 8, sans TOM_HOME**

```bash
cd /Users/pem/github/tom/legacy
unset TOM_HOME
java -version 2>&1 | head -1          # openjdk version "1.8.0_504"
./build.sh stable  2>&1 | tail -3     # BUILD SUCCESSFUL
./build.sh src.dist 2>&1 | tail -3    # BUILD SUCCESSFUL
ls src/dist/bin/tom src/dist/lib/tom-compiler-full.jar
```
Expected: deux `BUILD SUCCESSFUL`, les deux fichiers existent. Si la
construction échoue, **ne pas modifier `legacy/`** : arrêter et rapporter
l'erreur à l'utilisateur (le snapshot doit rester 100 % humain).

- [ ] **Step 4 : test rapide du compilateur legacy**

```bash
cd /Users/pem/github/tom/legacy
export TOM_HOME=$PWD/src/dist
D=$(mktemp -d) && $TOM_HOME/bin/tom -d $D ../test/Peano.t && ls $D
```
Expected: `Peano.java`.

- [ ] **Step 5 : écrire `legacy/README.md`**

```markdown
# Tom — original human-written compiler (legacy snapshot)

This directory is a frozen copy of the Tom compiler (Tom+Java sources and
the bootstrapped Java compiler) exactly as written by its human authors.
It is taken verbatim from commit `2918e95c7` (tip of branch `v3`,
29 Aug 2019) and is **never modified**: it is kept as a witness of that
work and as the reference oracle for the Go extension developed at the
repository root.

The repository root contains the AI-assisted extension of this compiler
(Go target, see `../tomgo/`).

## Requirements

- A JDK 8 (`java -version` must report `1.8`). The build bundles Ant
  (`stable/lib/compiletime/`), nothing else is needed.
  Recent JDKs (24 and later) cannot run this snapshot's build: Ant runs
  Tom in-process and relies on `System.setSecurityManager`, which these
  JDKs removed.
- A POSIX shell (Linux, macOS).

## Build

    git clone https://github.com/rewriting/tom.git
    cd tom/legacy
    unset TOM_HOME            # an existing TOM_HOME would shadow this build
    ./build.sh stable         # bootstrap compiler (pure Java) -> stable/dist
    ./build.sh src.dist       # Tom+Java sources compiled by stable -> src/dist

## Install

    export TOM_HOME=$PWD/src/dist
    export PATH=$TOM_HOME/bin:$PATH

`tom` and `gom` are now on the PATH.

## Check

    mkdir -p /tmp/tomcheck && cd /tmp/tomcheck
    cat > Hello.t <<'EOF'
    public class Hello {
      %include { string.tom }
      public static void main(String[] args) {
        String s = "world";
        %match(s) {
          x -> { System.out.println("hello " + `x); }
        }
      }
    }
    EOF
    tom Hello.t
    javac -cp $TOM_HOME/lib/tom-runtime-full.jar Hello.java
    java  -cp $TOM_HOME/lib/tom-runtime-full.jar:. Hello     # hello world

## Layout

| Path | Content |
|---|---|
| `src/` | Tom+Java sources of the compiler (`src/tom/engine`, `src/tom/gom`, …) |
| `stable/` | bootstrapped Java compiler used to compile `src/` |
| `share/`, `utils/` | man pages, launcher templates |
```

- [ ] **Step 6 : exécuter la section « Check » du README telle quelle**

Copier-coller les commandes de `## Install` puis `## Check` depuis
`legacy/`. Expected: `hello world`. Si une commande échoue, corriger le
README (pas le code) puis recommencer.

- [ ] **Step 7 : ajouter la section « Two versions » au `README.md` racine**

Insérer après la section `## What is it?` :

```markdown
## Two versions in this repository

- [`legacy/`](legacy/README.md) — the original Tom+Java compiler, exactly as
  written by its human authors (frozen copy of commit `2918e95c7`).
  Start there to install the historical Tom; see its README.
- The repository root (`src/`, `stable/`, `tomgo/`) — the AI-assisted
  extension of that compiler with a Go target.
```

- [ ] **Step 8 : vérifier que rien de généré n'est ajouté, puis commit**

```bash
cd /Users/pem/github/tom
git status --short legacy | grep -v '^??' ; git add -n legacy | grep -E '/(dist|build|gen)/' | head
```
Expected: la seconde commande n'affiche rien (les `.gitignore` racine
couvrent `src/dist` etc. uniquement à la racine : si des fichiers de
`legacy/src/{dist,build,gen}` ou `legacy/stable/{dist,build,gen}` sont
listés, ajouter à `.gitignore` racine les lignes
`legacy/src/dist/`, `legacy/src/build/`, `legacy/src/gen/`,
`legacy/stable/dist/`, `legacy/stable/build/`, `legacy/stable/gen/`).

```bash
git add .gitignore README.md legacy
git commit -m "tom-go J-1: freeze the human-written compiler in legacy/ (2918e95c7)

Co-Authored-By: Claude Opus 5.5 <noreply@anthropic.com>"
```

---

### Task 2 (T0) : harnais partagé + non-régression Java

**Files:**
- Create: `tomgo/tests/tomjava/harness.go`, `tomgo/tests/javaparity/javaparity_test.go`
- Modify: `tomgo/magefile.go`

**Interfaces:**
- Produces (package `tom/tomgo/tests/tomjava`) :
  - `func RepoRoot(t testing.TB) string`
  - `type Dist struct{ Home string }`
  - `func LegacyDist(t testing.TB) Dist` — `$TOM_LEGACY_HOME` sinon `<repo>/legacy/src/dist`
  - `func NewDist(t testing.TB) Dist` — `$TOM_NEW_HOME` sinon `<repo>/src/dist`
  - `func (d Dist) Compile(t testing.TB, src, outDir string, args ...string) (combined string, err error)`
  - `func TomgoBin(t testing.TB) string`
  - `func SetupGoModule(t testing.TB, dir string)`
  - `func RunIn(dir string, env []string, name string, args ...string) (string, error)`

- [ ] **Step 1 : écrire `tomgo/tests/tomjava/harness.go`**

```go
// Package tomjava drives built Tom Java distributions (legacy/ and the
// repository root) and temporary Go modules for the cross-checking
// harnesses T0–T3. Every helper skips the calling test when a required
// tool (java, a built dist) is missing.
package tomjava

import (
	"os"
	"os/exec"
	"path/filepath"
	"strings"
	"sync"
	"testing"
)

// RepoRoot walks up from the working directory to the directory that
// holds both build.xml and tomgo/.
func RepoRoot(t testing.TB) string {
	t.Helper()
	dir, err := os.Getwd()
	if err != nil {
		t.Fatal(err)
	}
	for {
		if fileExists(filepath.Join(dir, "build.xml")) && fileExists(filepath.Join(dir, "tomgo", "go.mod")) {
			return dir
		}
		parent := filepath.Dir(dir)
		if parent == dir {
			t.Fatal("repository root not found")
		}
		dir = parent
	}
}

func fileExists(p string) bool { _, err := os.Stat(p); return err == nil }

// Dist is a built Tom distribution (the directory TOM_HOME points to).
type Dist struct{ Home string }

func dist(t testing.TB, env, rel string) Dist {
	t.Helper()
	if _, err := exec.LookPath("java"); err != nil {
		t.Skip("java not found in PATH")
	}
	home := os.Getenv(env)
	if home == "" {
		home = filepath.Join(RepoRoot(t), rel)
	}
	if !fileExists(filepath.Join(home, "bin", "tom")) {
		t.Skipf("no Tom distribution at %s (build it, or set %s)", home, env)
	}
	return Dist{Home: home}
}

// LegacyDist is the human-written compiler built in legacy/.
func LegacyDist(t testing.TB) Dist { return dist(t, "TOM_LEGACY_HOME", "legacy/src/dist") }

// NewDist is the extended compiler built at the repository root.
func NewDist(t testing.TB) Dist { return dist(t, "TOM_NEW_HOME", "src/dist") }

// Compile runs `bin/tom -d outDir args… src` with TOM_HOME=d.Home and
// TOMGO_BIN set, and returns the combined stdout+stderr.
func (d Dist) Compile(t testing.TB, src, outDir string, args ...string) (string, error) {
	t.Helper()
	full := append(append([]string{"-d", outDir}, args...), src)
	env := []string{"TOM_HOME=" + d.Home, "TOMGO_BIN=" + TomgoBin(t)}
	return RunIn(filepath.Dir(src), env, filepath.Join(d.Home, "bin", "tom"), full...)
}

// RunIn runs a command in dir with extra environment variables and
// returns its combined output.
func RunIn(dir string, env []string, name string, args ...string) (string, error) {
	cmd := exec.Command(name, args...)
	cmd.Dir = dir
	cmd.Env = append(os.Environ(), env...)
	out, err := cmd.CombinedOutput()
	return string(out), err
}

var (
	tomgoOnce sync.Once
	tomgoPath string
	tomgoErr  error
)

// TomgoBin builds cmd/tomgo once per test binary and returns its path.
func TomgoBin(t testing.TB) string {
	t.Helper()
	root := RepoRoot(t)
	tomgoOnce.Do(func() {
		dir, err := os.MkdirTemp("", "tomgo-harness-")
		if err != nil {
			tomgoErr = err
			return
		}
		tomgoPath = filepath.Join(dir, "tomgo")
		_, tomgoErr = RunIn(filepath.Join(root, "tomgo"), nil, "go", "build", "-o", tomgoPath, "./cmd/tomgo")
	})
	if tomgoErr != nil {
		t.Fatalf("building tomgo: %v", tomgoErr)
	}
	return tomgoPath
}

// SetupGoModule writes a go.mod in dir that resolves tom/tomgo to the
// local checkout, so generated code can import sl and sharedobjects.
func SetupGoModule(t testing.TB, dir string) {
	t.Helper()
	tomgo := filepath.Join(RepoRoot(t), "tomgo")
	mod := "module fixture\n\ngo 1.22\n\nrequire tom/tomgo v0.0.0\n\nreplace tom/tomgo => " + tomgo + "\n"
	if err := os.WriteFile(filepath.Join(dir, "go.mod"), []byte(mod), 0o644); err != nil {
		t.Fatal(err)
	}
	if out, err := RunIn(dir, nil, "go", "mod", "tidy"); err != nil {
		t.Fatalf("go mod tidy: %v\n%s", err, out)
	}
}

// Normalize replaces machine-specific paths by stable placeholders.
func Normalize(s string, repl map[string]string) string {
	for from, to := range repl {
		s = strings.ReplaceAll(s, from, to)
	}
	return s
}
```

- [ ] **Step 2 : écrire le test T0 `tomgo/tests/javaparity/javaparity_test.go`**

```go
// Package javaparity is T0: the extended compiler must produce, for the
// default Java target, exactly what the human-written compiler in legacy/
// produces on every test/ input.
package javaparity

import (
	"os"
	"path/filepath"
	"strings"
	"testing"

	"tom/tomgo/tests/tomjava"
)

func inputs(t *testing.T, root string) []string {
	var files []string
	err := filepath.Walk(filepath.Join(root, "test"), func(p string, info os.FileInfo, err error) error {
		if err == nil && !info.IsDir() && strings.HasSuffix(p, ".t") {
			files = append(files, p)
		}
		return err
	})
	if err != nil {
		t.Fatal(err)
	}
	return files
}

func TestJavaOutputIdenticalToLegacy(t *testing.T) {
	legacy, current := tomjava.LegacyDist(t), tomjava.NewDist(t)
	root := tomjava.RepoRoot(t)
	for _, src := range inputs(t, root) {
		rel, _ := filepath.Rel(root, src)
		t.Run(rel, func(t *testing.T) {
			outL, outN := t.TempDir(), t.TempDir()
			logL, errL := legacy.Compile(t, src, outL)
			logN, errN := current.Compile(t, src, outN)
			norm := func(s, out string) string {
				return tomjava.Normalize(s, map[string]string{out: "__OUT__", legacy.Home: "__HOME__", current.Home: "__HOME__"})
			}
			if (errL == nil) != (errN == nil) || norm(logL, outL) != norm(logN, outN) {
				t.Fatalf("diagnostics differ\nlegacy (%v):\n%s\nnew (%v):\n%s", errL, logL, errN, logN)
			}
			compareTrees(t, outL, outN)
		})
	}
}

func compareTrees(t *testing.T, a, b string) {
	t.Helper()
	seen := map[string]bool{}
	filepath.Walk(a, func(p string, info os.FileInfo, err error) error {
		if err != nil || info.IsDir() {
			return err
		}
		rel, _ := filepath.Rel(a, p)
		seen[rel] = true
		want, _ := os.ReadFile(p)
		got, err := os.ReadFile(filepath.Join(b, rel))
		if err != nil {
			t.Errorf("%s: missing in new output", rel)
		} else if string(want) != string(got) {
			t.Errorf("%s: content differs from legacy", rel)
		}
		return nil
	})
	filepath.Walk(b, func(p string, info os.FileInfo, err error) error {
		if err == nil && !info.IsDir() {
			if rel, _ := filepath.Rel(b, p); !seen[rel] {
				t.Errorf("%s: extra file in new output", rel)
			}
		}
		return err
	})
}
```

- [ ] **Step 3 : construire la dist racine (état actuel, sans modification)**

```bash
cd /Users/pem/github/tom && unset TOM_HOME && ./build.sh src.dist 2>&1 | tail -3
```
Expected: `BUILD SUCCESSFUL`.

- [ ] **Step 4 : lancer T0 — doit être vert d'emblée**

Run: `cd tomgo && go test ./tests/javaparity/ -count=1 2>&1 | tail -20`
Expected: `ok`. Si des diagnostics diffèrent seulement par un chemin ou un
horodatage, ajouter le remplacement correspondant dans la map de `norm`
(pas de tolérance plus large). Si un `.java` diffère, analyser :
à ce stade les sources sont identiques, seul `build.xml` (`fork=yes`) diffère.

- [ ] **Step 5 : cible mage `PortJava`**

Ajouter à `tomgo/magefile.go` :

```go
// PortJava runs T0: the root compiler's Java output must equal legacy/'s
// on every test/*.t (needs legacy/src/dist and src/dist built).
func PortJava() error {
	return sh.RunV("go", "test", "-count=1", "./tests/javaparity/...")
}
```

Run: `cd tomgo && mage portJava` → Expected: `ok`.

- [ ] **Step 6 : commit**

```bash
git add tomgo/tests/tomjava tomgo/tests/javaparity tomgo/magefile.go
git commit -m "tom-go T0: add Tom dist harness and Java-output parity test against legacy/

Co-Authored-By: Claude Opus 5.5 <noreply@anthropic.com>"
```

---

### Task 3 : côté tomgo — `sl.MustVisit` et `tomgo gom-block`

**Files:**
- Create: `tomgo/stable/library/sl/must.go`, `tomgo/stable/library/sl/must_test.go`, `tomgo/cmd/tomgo/gomblock.go`, `tomgo/cmd/tomgo/gomblock_test.go`
- Modify: `tomgo/cmd/tomgo/main.go` (dispatch + usage)

**Interfaces:**
- Produces:
  - `func sl.MustVisit(v any, err error) any`
  - `func sl.RecoverVisitFailure(err *error)` (à appeler via `defer`)
  - CLI `tomgo gom-block -o <dir> --pkg <name> <file.gom>` : écrit `<dir>/<lower(Module)>_gen.go` (package `<name>`) et `<dir>/<Module>.tom` (mapping à code hôte Go), imprime **le chemin absolu du `.tom`** sur stdout, code de sortie ≠ 0 en cas d'erreur.
  - `func writeGomBlock(mod gomast.GomModule, pkg, out string) (string, error)`

- [ ] **Step 1 : test de `MustVisit`/`RecoverVisitFailure`**

`tomgo/stable/library/sl/must_test.go` :

```go
package sl

import (
	"errors"
	"testing"
)

func visitThroughPanic(err error) (res any, out error) {
	defer RecoverVisitFailure(&out)
	return MustVisit(42, err), nil
}

func TestMustVisitPassesValue(t *testing.T) {
	res, err := visitThroughPanic(nil)
	if err != nil || res != 42 {
		t.Fatalf("got (%v, %v), want (42, nil)", res, err)
	}
}

func TestMustVisitCarriesFailure(t *testing.T) {
	_, err := visitThroughPanic(ErrVisitFailure)
	if !errors.Is(err, ErrVisitFailure) {
		t.Fatalf("got %v, want ErrVisitFailure", err)
	}
}

func TestRecoverVisitFailureRepanicsOthers(t *testing.T) {
	defer func() {
		if r := recover(); r != "boom" {
			t.Fatalf("recovered %v, want boom", r)
		}
	}()
	var err error
	func() {
		defer RecoverVisitFailure(&err)
		panic("boom")
	}()
}
```

Run: `cd tomgo && go test ./stable/library/sl/ -run Must -count=1`
Expected: FAIL — `undefined: RecoverVisitFailure`.

- [ ] **Step 2 : implémenter `tomgo/stable/library/sl/must.go`**

```go
package sl

// visitFailurePanic carries a visit error through a panic so that code
// generated by Tom's Go backend can mirror Java's checked VisitFailure:
// the generated inner methods call MustVisit, and the exported
// VisitLight wrapper turns the panic back into an error.
type visitFailurePanic struct{ err error }

// MustVisit returns v, or panics with a visit-failure carrier when err
// is non-nil.
func MustVisit(v any, err error) any {
	if err != nil {
		panic(visitFailurePanic{err})
	}
	return v
}

// RecoverVisitFailure converts a panic raised by MustVisit into *err.
// Any other panic is re-raised. Call it directly with defer.
func RecoverVisitFailure(err *error) {
	if r := recover(); r != nil {
		if vf, ok := r.(visitFailurePanic); ok {
			*err = vf.err
			return
		}
		panic(r)
	}
}
```

Run: `cd tomgo && go test ./stable/library/sl/ -count=1` → Expected: `ok`.

- [ ] **Step 3 : test de `gom-block`**

`tomgo/cmd/tomgo/gomblock_test.go` :

```go
package main

import (
	"os"
	"path/filepath"
	"strings"
	"testing"

	gomparser "tom/tomgo/stable/gom/parser"
)

func TestWriteGomBlock(t *testing.T) {
	dir := t.TempDir()
	src := filepath.Join(dir, "in.gom")
	body := "module M\nabstract syntax\nT = a() | f(x:T)\n"
	if err := os.WriteFile(src, []byte(body), 0o644); err != nil {
		t.Fatal(err)
	}
	mod, err := gomparser.ParseFile(src)
	if err != nil {
		t.Fatal(err)
	}
	out := filepath.Join(dir, "out")
	tomPath, err := writeGomBlock(mod, "fixture", out)
	if err != nil {
		t.Fatal(err)
	}
	if want := filepath.Join(out, "M.tom"); tomPath != want {
		t.Fatalf("mapping path = %s, want %s", tomPath, want)
	}
	goSrc, err := os.ReadFile(filepath.Join(out, "m_gen.go"))
	if err != nil || !strings.HasPrefix(string(goSrc), "package fixture") && !strings.Contains(string(goSrc), "\npackage fixture") {
		t.Fatalf("m_gen.go missing or wrong package: %v", err)
	}
	mapping, _ := os.ReadFile(tomPath)
	if !strings.Contains(string(mapping), "%op T f(") || strings.Contains(string(mapping), "instanceof") {
		t.Fatalf("mapping is not the Go-host-code mapping:\n%s", mapping)
	}
}
```

Run: `cd tomgo && go test ./cmd/tomgo/ -run TestWriteGomBlock -count=1`
Expected: FAIL — `undefined: writeGomBlock`.

- [ ] **Step 4 : implémenter `tomgo/cmd/tomgo/gomblock.go`**

```go
package main

import (
	"bytes"
	"flag"
	"fmt"
	"os"
	"path/filepath"
	"strings"

	"tom/tomgo/stable/gom/backend"
	gomparser "tom/tomgo/stable/gom/parser"
	"tom/tomgo/stable/library/gomast"
)

// runGomBlock compiles the body of a Tom `%gom { … }` block for the Go
// target. It is called by the Tom Java compiler (TomParserTool) when
// -go is active; the last stdout line is the generated mapping's path.
func runGomBlock(args []string) error {
	fs := flag.NewFlagSet("gom-block", flag.ContinueOnError)
	var out, pkg string
	fs.StringVar(&out, "o", "", "output directory (required)")
	fs.StringVar(&pkg, "pkg", "", "Go package of the enclosing .t file (required)")
	if err := fs.Parse(args); err != nil {
		return err
	}
	if fs.NArg() != 1 || out == "" || pkg == "" {
		return fmt.Errorf("usage: tomgo gom-block -o <dir> --pkg <name> <file.gom>")
	}
	mod, err := gomparser.ParseFile(fs.Arg(0))
	if err != nil {
		return err
	}
	tomPath, err := writeGomBlock(mod, pkg, out)
	if err != nil {
		return err
	}
	fmt.Println(tomPath)
	return nil
}

// writeGomBlock writes <out>/<lower(Module)>_gen.go in package pkg and
// <out>/<Module>.tom (Go-host-code mapping), and returns the absolute
// path of the mapping.
func writeGomBlock(mod gomast.GomModule, pkg, out string) (string, error) {
	name := gomparser.QualifiedName(mod)
	if i := strings.LastIndex(name, "."); i >= 0 {
		name = name[i+1:]
	}
	if name == "" {
		name = "M"
	}
	if err := os.MkdirAll(out, 0o755); err != nil {
		return "", err
	}
	opts := backend.Options{PackageName: pkg}
	var buf bytes.Buffer
	if err := backend.Generate(mod, opts, &buf); err != nil {
		return "", err
	}
	if err := os.WriteFile(filepath.Join(out, strings.ToLower(name)+"_gen.go"), buf.Bytes(), 0o644); err != nil {
		return "", err
	}
	tomPath := filepath.Join(out, name+".tom")
	if err := os.WriteFile(tomPath, backend.EmitTomMappingGo(mod, opts), 0o644); err != nil {
		return "", err
	}
	return filepath.Abs(tomPath)
}
```

Dans `tomgo/cmd/tomgo/main.go`, ajouter au `switch os.Args[1]` :

```go
	case "gom-block":
		if err := runGomBlock(os.Args[2:]); err != nil {
			fmt.Fprintln(os.Stderr, "error:", err)
			os.Exit(1)
		}
```

et dans la constante `usage`, après l'entrée `gom-batch` :

```
  gom-block -o <dir> --pkg <name> <file.gom>
                                   Compile a %gom block for the Go target
                                   (used by tom -go): writes <module>_gen.go
                                   and <Module>.tom, prints the .tom path.
```

- [ ] **Step 5 : tests verts**

Run: `cd tomgo && go test ./cmd/tomgo/ ./stable/library/sl/ -count=1 && go vet ./cmd/tomgo/ ./stable/library/sl/`
Expected: `ok` ×2, vet silencieux. Si `QualifiedName` renvoie un nom en
minuscules ou sans le module, ajuster le test (le nom de fichier `.tom`
doit être celui du module tel qu'écrit dans `module M`).

- [ ] **Step 6 : commit**

```bash
git add tomgo/stable/library/sl/must.go tomgo/stable/library/sl/must_test.go tomgo/cmd/tomgo
git commit -m "tom-go J0: add sl.MustVisit/RecoverVisitFailure and tomgo gom-block

Co-Authored-By: Claude Opus 5.5 <noreply@anthropic.com>"
```

---

### Task 4 (J0) : option `goCode` + `GoGenerator.t` + mappings Go

**Files:**
- Create: `src/tom/engine/backend/GoGenerator.t`, `src/tom/library/mapping/go/{int,string,boolean,Strategy,sl}.tom`, `src/tom/library/mapping/go/sl/{basic,composed}.tom`
- Modify: `src/tom/engine/backend/BackendPlugin.t`, `src/tom/engine/TomStreamManager.java`, `src/tom/engine/tools/SymbolTable.t`, `build.xml`

**Interfaces:**
- Consumes: rien des tasks précédentes (Task 2 sert à vérifier).
- Produces:
  - option Tom `goCode` (alias `-go`) ; activée, elle désactive `jCode`, `cCode`, `camlCode`, `pCode`, `aCode`.
  - `GoGenerator` : fonctions de mapping `tom_is_fun_sym_<op>`, `tom_make_<op>`, `tom_get_slot_<op>_<slot>`, `tom_equal_term_<Sort>`, `tom_is_sort_<Sort>`, `tom_empty_list_<op>`, `tom_cons_list_<op>`, `tom_append_list_<op>`, `tom_get_slice_<op>`, `tom_empty_array_<op>`, `tom_cons_array_<op>`, `tom_append_array_<op>`.
  - `GoGenerator.RECEIVER = "tom__strat"` ; méthodes de `%strategy` émises `func (tom__strat *<S>) name(…)`.
  - Types Go : `int`→`int64`, `long`→`int64`, `float`→`float32`, `double`→`float64`, `char`→`rune`, `String`→`string`, `boolean`→`bool`, universel→`any`, `void`→`""`, `intarray`→`[]int64`.
- Invocation : `tom -go -np -d <dir> X.t` → `<dir>/X.go`.

- [ ] **Step 1 : `BackendPlugin.t` — déclarer et sélectionner `goCode`**

Dans `PLATFORM_OPTIONS`, après la ligne `pCode` :

```java
        PluginOption("goCode", "go", "Generate Go code", BooleanValue(False()), ""),
```

Après `public static final String ACODE = "aCode";` :

```java
  public static final String GOCODE = "goCode"; 
```

Dans `run`, avant `} else if(getOptionBooleanValue(BackendPlugin.PCODE)) {` :

```java
          } else if(getOptionBooleanValue(BackendPlugin.GOCODE)) {
            generator = new GoGenerator(output, getOptionManager(), getSymbolTable());
```

Dans `optionChanged`, ajouter `setOptionValue(BackendPlugin.GOCODE, Boolean.FALSE);`
dans chacune des cinq branches existantes (camlCode, cCode, jCode, pCode,
aCode), puis une sixième branche avant la `}` finale du `if` :

```java
    } else if(optionName.equals(BackendPlugin.GOCODE) && ((Boolean)optionValue).booleanValue() ) {
      setOptionValue(BackendPlugin.CCODE, Boolean.FALSE);
      setOptionValue(BackendPlugin.CAMLCODE, Boolean.FALSE);
      setOptionValue(BackendPlugin.JCODE, Boolean.FALSE);
      setOptionValue(BackendPlugin.PCODE, Boolean.FALSE);
      setOptionValue(BackendPlugin.ACODE, Boolean.FALSE);
```

- [ ] **Step 2 : `TomStreamManager.java` — suffixe et sous-répertoire**

Avant `} else if (((Boolean)optionManager.getOptionValue("jCode")).booleanValue()) {` (ligne ~132) :

```java
    } else if (((Boolean)optionManager.getOptionValue("goCode")).booleanValue()) {
      inputSuffix = ".t";
      outputSuffix = ".go";
			importLanguageSubdir = "go";
```

- [ ] **Step 3 : `SymbolTable.t` — drapeau et types Go**

Après `private boolean aCode = false;` : `private boolean goCode = false;`

Dans `init`, en tête de la chaîne (`goCode` testé avant `cCode`) :

```java
    if(((Boolean)optionManager.getOptionValue("goCode")).booleanValue()) {
      goCode = true;
    } else if(((Boolean)optionManager.getOptionValue("cCode")).booleanValue()) {
```

(la ligne `if(((Boolean)optionManager.getOptionValue("cCode"))…` d'origine
devient le `else if` ci-dessus.)

Dans chaque getter, ajouter en première instruction du corps (avant
`String type = …;`) la ligne indiquée :

```java
// getIntType
if(goCode) { return ASTFactory.makeType(`concTypeOption(),TYPE_INT,"int64"); }
// getIntArrayType
if(goCode) { return ASTFactory.makeType(`concTypeOption(),TYPE_INT_ARRAY,"[]int64"); }
// getLongType
if(goCode) { return ASTFactory.makeType(`concTypeOption(),TYPE_LONG,"int64"); }
// getFloatType
if(goCode) { return ASTFactory.makeType(`concTypeOption(),TYPE_FLOAT,"float32"); }
// getCharType
if(goCode) { return ASTFactory.makeType(`concTypeOption(),TYPE_CHAR,"rune"); }
// getDoubleType
if(goCode) { return ASTFactory.makeType(`concTypeOption(),TYPE_DOUBLE,"float64"); }
// getBooleanType
if(goCode) { return ASTFactory.makeType(`concTypeOption(),TYPE_BOOLEAN,"bool"); }
// getStringType
if(goCode) { return ASTFactory.makeType(`concTypeOption(),TYPE_STRING,"string"); }
// getUniversalType
if(goCode) { return ASTFactory.makeType(`concTypeOption(),TYPE_UNIVERSAL,"any"); }
// getVoidType
if(goCode) { return ASTFactory.makeType(`concTypeOption(),TYPE_VOID,""); }
```

- [ ] **Step 4 : créer `src/tom/engine/backend/GoGenerator.t`**

Copier l'en-tête de licence de `PythonGenerator.t` (lignes 1-24) puis :

```java
package tom.engine.backend;

import java.io.IOException;
import java.util.ArrayList;

import tom.engine.TomBase;
import tom.engine.exception.TomRuntimeException;

import tom.engine.adt.tomsignature.*;
import tom.engine.adt.code.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomdeclaration.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomtype.types.*;

import tom.engine.tools.OutputCode;
import tom.engine.tools.SymbolTable;
import tom.platform.OptionManager;

/**
 * Go backend. The code is written on one logical line (as the other
 * backends do without -pretty), so every statement ends with ';' and
 * every block with '};' -- except before 'else'.
 */
public class GoGenerator extends GenericGenerator {

  // ------------------------------------------------------------
  %include { ../adt/tomsignature/TomSignature.tom }
  // ------------------------------------------------------------

  /** receiver of the methods generated for a %strategy */
  public static final String RECEIVER = "tom__strat";

  /** name of the %strategy being emitted by buildClass, null otherwise */
  private String currentClass = null;

  public GoGenerator(OutputCode output, OptionManager optionManager,
                     SymbolTable symbolTable) {
    super(output, optionManager, symbolTable);
  }

  /** Go spelling of a TomType; "any" when no target type is known */
  private String goType(TomType type) {
    %match(type) {
      Type[TlType=TLType(str)] -> { return `str; }
    }
    return "any";
  }

  private String goTLCode(TargetLanguageType tlType) {
    %match(tlType) {
      TLType(str) -> { return `str; }
    }
    return "any";
  }

  /** Go type of a sort name: builtin, declared %typeterm, or any */
  private String sortGoType(String sort, String moduleName) {
    SymbolTable st = getSymbolTable(moduleName);
    if(st.isBuiltinType(sort)) {
      return goType(st.getBuiltinType(sort));
    }
    TomType t = st.getType(sort);
    return (t == null) ? "any" : goType(t);
  }

  // ---------------------------------------------------------------- instructions

  protected void buildAssign(int deep, BQTerm var, OptionList optionList, Expression exp, String moduleName) throws IOException {
    generateBQTerm(deep,var,moduleName);
    output.write(" = ");
    generateExpression(deep,exp,moduleName);
    output.write("; ");
  }

  protected void buildAssignArray(int deep, BQTerm var, OptionList optionList, BQTerm index,
      Expression exp, String moduleName) throws IOException {
    generateArray(deep,var,index,moduleName);
    output.write(" = ");
    generateExpression(deep,exp,moduleName);
    output.write("; ");
  }

  protected void buildComment(int deep, String text) throws IOException {
    output.write("/* " + text.replace("*/","* /") + " */ ");
  }

  protected void buildDoWhile(int deep, Instruction succes, Expression exp, String moduleName) throws IOException {
    output.write("for { ");
    generateInstruction(deep+1,succes,moduleName);
    output.write(" if !(");
    generateExpression(deep,exp,moduleName);
    output.write(") { break; }; }; ");
  }

  protected void buildWhileDo(int deep, Expression exp, Instruction succes, String moduleName) throws IOException {
    output.write("for ");
    generateExpression(deep,exp,moduleName);
    output.write(" { ");
    generateInstruction(deep+1,succes,moduleName);
    output.write(" }; ");
  }

  protected void buildIf(int deep, Expression exp, Instruction succes, String moduleName) throws IOException {
    output.write("if ");
    generateExpression(deep,exp,moduleName);
    output.write(" { ");
    generateInstruction(deep+1,succes,moduleName);
    output.write(" }; ");
  }

  protected void buildIfWithFailure(int deep, Expression exp, Instruction succes, Instruction failure, String moduleName) throws IOException {
    output.write("if ");
    generateExpression(deep,exp,moduleName);
    output.write(" { ");
    generateInstruction(deep+1,succes,moduleName);
    output.write(" } else { ");
    generateInstruction(deep+1,failure,moduleName);
    output.write(" }; ");
  }

  protected void buildInstructionSequence(int deep, InstructionList instructionList, String moduleName) throws IOException {
    generateInstructionList(deep, instructionList, moduleName);
  }

  /* Go rejects unused variables: every Let is followed by "_ = x" */
  protected void buildLet(int deep, BQTerm var, OptionList optionList, TargetLanguageType tlType,
      Expression exp, Instruction body, String moduleName) throws IOException {
    output.write("var ");
    generateBQTerm(deep,var,moduleName);
    output.write(" " + goTLCode(tlType) + " = ");
    generateExpression(deep,exp,moduleName);
    output.write("; _ = ");
    generateBQTerm(deep,var,moduleName);
    output.write("; ");
    generateInstruction(deep,body,moduleName);
  }

  protected void buildLetRef(int deep, BQTerm var, OptionList optionList, TargetLanguageType tlType,
      Expression exp, Instruction body, String moduleName) throws IOException {
    buildLet(deep,var,optionList,tlType,exp,body,moduleName);
  }

  /* a labelled block is a one-shot labelled loop: "break label" leaves it */
  protected void buildNamedBlock(int deep, String blockName, InstructionList instList, String moduleName) throws IOException {
    output.write(blockName + ": for { ");
    generateInstructionList(deep+1,instList,moduleName);
    output.write(" break " + blockName + "; }; ");
  }

  protected void buildUnamedBlock(int deep, InstructionList instList, String moduleName) throws IOException {
    output.write("{ ");
    generateInstructionList(deep+1,instList,moduleName);
    output.write(" }; ");
  }

  protected void buildReturn(int deep, BQTerm exp, String moduleName) throws IOException {
    output.write("return ");
    generateBQTerm(deep,exp,moduleName);
    output.write("; ");
  }

  // ---------------------------------------------------------------- expressions

  protected void buildExpTrue(int deep) throws IOException {
    output.write(" true ");
  }

  protected void buildExpFalse(int deep) throws IOException {
    output.write(" false ");
  }

  protected void buildExpBottom(int deep, TomType type, String moduleName) throws IOException {
    SymbolTable st = getSymbolTable(moduleName);
    String sType = TomBase.getTomType(type);
    if(sType != null && (st.isIntType(sType) || st.isLongType(sType) || st.isCharType(sType)
          || st.isFloatType(sType) || st.isDoubleType(sType))) {
      output.write(" 0 ");
    } else if(sType != null && st.isBooleanType(sType)) {
      output.write(" false ");
    } else if(sType != null && st.isStringType(sType)) {
      output.write(" \"\" ");
    } else {
      output.write(" nil ");
    }
  }

  protected void buildExpNegation(int deep, Expression exp, String moduleName) throws IOException {
    output.write("!(");
    generateExpression(deep,exp,moduleName);
    output.write(")");
  }

  protected void buildExpAnd(int deep, Expression exp1, Expression exp2, String moduleName) throws IOException {
    output.write(" (");
    generateExpression(deep,exp1,moduleName);
    output.write(" && ");
    generateExpression(deep,exp2,moduleName);
    output.write(") ");
  }

  protected void buildExpOr(int deep, Expression exp1, Expression exp2, String moduleName) throws IOException {
    output.write(" (");
    generateExpression(deep,exp1,moduleName);
    output.write(" || ");
    generateExpression(deep,exp2,moduleName);
    output.write(") ");
  }

  /* Go has no ternary operator: an immediately-called closure (result typed any) */
  protected void buildExpConditional(int deep, Expression cond, Expression exp1, Expression exp2, String moduleName) throws IOException {
    output.write("func() any { if ");
    generateExpression(deep,cond,moduleName);
    output.write(" { return ");
    generateExpression(deep,exp1,moduleName);
    output.write(" }; return ");
    generateExpression(deep,exp2,moduleName);
    output.write(" }()");
  }

  /* any(e).(T) is valid whatever the static type of e */
  protected void buildExpCast(int deep, TargetLanguageType tlType, Expression exp, String moduleName) throws IOException {
    %match(tlType) {
      TLType(str) -> {
        output.write("any(");
        generateExpression(deep,exp,moduleName);
        output.write(").(" + `str + ")");
        return;
      }
    }
    generateExpression(deep,exp,moduleName);
  }

  private boolean useNativeEquality(String sType, String moduleName) {
    SymbolTable st = getSymbolTable(moduleName);
    return sType == null || st.isUnknownType(sType) || st.isBooleanType(sType);
  }

  protected void buildExpEqualTerm(int deep, TomType type, BQTerm exp1, TomTerm exp2, String moduleName) throws IOException {
    String sType = TomBase.getTomType(type);
    if(useNativeEquality(sType,moduleName)) {
      output.write("(");
      generateBQTerm(deep,exp1,moduleName);
      output.write(" == ");
      generateTomTerm(deep,exp2,moduleName);
      output.write(")");
    } else {
      output.write("tom_equal_term_" + sType + "(");
      generateBQTerm(deep,exp1,moduleName);
      output.write(", ");
      generateTomTerm(deep,exp2,moduleName);
      output.write(")");
    }
  }

  protected void buildExpEqualBQTerm(int deep, TomType type, BQTerm exp1, BQTerm exp2, String moduleName) throws IOException {
    String sType = TomBase.getTomType(type);
    if(useNativeEquality(sType,moduleName)) {
      output.write("(");
      generateBQTerm(deep,exp1,moduleName);
      output.write(" == ");
      generateBQTerm(deep,exp2,moduleName);
      output.write(")");
    } else {
      output.write("tom_equal_term_" + sType + "(");
      generateBQTerm(deep,exp1,moduleName);
      output.write(", ");
      generateBQTerm(deep,exp2,moduleName);
      output.write(")");
    }
  }

  // ---------------------------------------------------------------- mapping functions

  protected void genDeclInstr(String returnType, String declName, String suffix,
      String args[], Instruction instr, int deep, String moduleName) throws IOException {
    if(nodeclMode) {
      return;
    }
    StringBuilder s = new StringBuilder();
    s.append("func " + declName + "_" + suffix + "(");
    for(int i=0 ; i<args.length ; ) {
      s.append(args[i+1] + " " + args[i]);
      i+=2;
      if(i<args.length) {
        s.append(", ");
      }
    }
    s.append(") " + returnType + " { ");
    output.write(s);
    generateInstruction(deep,instr,moduleName);
    output.write(" }; ");
  }

  protected void genDecl(String returnType, String declName, String suffix,
      String args[], TargetLanguage tlCode, String moduleName) throws IOException {
    throw new TomRuntimeException("GoGenerator.genDecl is not used");
  }

  /* equals receives the sort's Go type (not any): mappings call methods on it */
  protected void buildEqualTermDecl(int deep, String varname1, String varname2,
      String type1, String type2, Expression code, String moduleName) throws IOException {
    if(code.isCode()) {
      code = code.setCode(code.getCode().replace("{0}",varname1).replace("{1}",varname2));
    }
    genDeclInstr(goType(getSymbolTable(moduleName).getBooleanType()), "tom_equal_term", type1,
        new String[] { sortGoType(type1,moduleName), varname1, sortGoType(type2,moduleName), varname2 },
        `Return(ExpressionToBQTerm(code)),deep,moduleName);
  }

  protected void genDeclMake(String prefix, String funName, TomType returnType,
      BQTermList argList, Instruction instr, String moduleName) throws IOException {
    if(nodeclMode) {
      return;
    }
    %match(instr) {
      ExpressionToInstruction(Code(code)) -> {
        String ncode = `code;
        int index = 0;
        %match(argList) {
          concBQTerm(_*,BQVariable[AstName=Name(varname)],_*) -> {
            ncode = ncode.replace("{"+index+"}",`varname);
            index++;
          }
        }
        instr = `ExpressionToInstruction(Code(ncode));
      }
    }
    StringBuilder s = new StringBuilder();
    s.append("func " + prefix + funName + "(");
    while(!argList.isEmptyconcBQTerm()) {
      BQTerm arg = argList.getHeadconcBQTerm();
matchBlock: {
      %match(arg) {
        BQVariable[AstName=Name(name), AstType=Type[TlType=tlType@TLType[]]] -> {
          s.append(`name + " " + TomBase.getTLCode(`tlType));
          break matchBlock;
        }
        _ -> {
          throw new TomRuntimeException("genDeclMake: strange term: " + arg);
        }
      }
    }
      argList = argList.getTailconcBQTerm();
      if(!argList.isEmptyconcBQTerm()) {
        s.append(", ");
      }
    }
    s.append(") " + goType(returnType) + " { return ");
    output.write(s);
    generateInstruction(0,instr,moduleName);
    output.write(" }; ");
  }

  protected void genDeclList(String name, String moduleName) throws IOException {
    if(nodeclMode) {
      return;
    }
    TomSymbol tomSymbol = getSymbolTable(moduleName).getSymbolFromName(name);
    TomType listType = TomBase.getSymbolCodomain(tomSymbol);
    TomType eltType = TomBase.getSymbolDomain(tomSymbol).getHeadconcTomType();
    String tomType = TomBase.getTomType(listType);
    String t = goType(listType);
    String isEmpty = "tom_is_empty_" + name + "_" + tomType;
    String head = "tom_get_head_" + name + "_" + tomType;
    String tail = "tom_get_tail_" + name + "_" + tomType;
    String cons = "tom_cons_list_" + name;
    String empty = "tom_empty_list_" + name + "()";
    String equal = "tom_equal_term_" + tomType;
    String isConc = "tom_is_fun_sym_" + name;
    String append = "tom_append_list_" + name;
    String slice = "tom_get_slice_" + name;
    StringBuilder s = new StringBuilder();
    s.append("func " + append + "(l1 " + t + ", l2 " + t + ") " + t + " { ");
    s.append("if " + isEmpty + "(l1) { return l2 } else if " + isEmpty + "(l2) { return l1 } ");
    if(listType == eltType) {
      s.append("else if " + isConc + "(l1) { if " + isEmpty + "(" + tail + "(l1)) { return " + cons + "(" + head + "(l1), l2) } else { return "
          + cons + "(" + head + "(l1), " + append + "(" + tail + "(l1), l2)) } } else { return " + cons + "(l1, l2) }; }; ");
    } else {
      s.append("else if " + isEmpty + "(" + tail + "(l1)) { return " + cons + "(" + head + "(l1), l2) } else { return "
          + cons + "(" + head + "(l1), " + append + "(" + tail + "(l1), l2)) }; }; ");
    }
    String headIn = head + "(begin)";
    String tailIn = tail + "(begin)";
    if(listType == eltType) {
      headIn = "func() " + t + " { if " + isConc + "(begin) { return " + head + "(begin) }; return begin }()";
      tailIn = "func() " + t + " { if " + isConc + "(begin) { return " + tail + "(begin) }; return " + empty + " }()";
    }
    s.append("func " + slice + "(begin " + t + ", end " + t + ", tail " + t + ") " + t + " { ");
    s.append("if " + equal + "(begin, end) { return tail } else if " + equal + "(end, tail) && (" + isEmpty + "(end) || "
        + equal + "(end, " + empty + ")) { return begin }; ");
    s.append("return " + cons + "(" + headIn + ", " + slice + "(" + tailIn + ", end, tail)) }; ");
    output.write(s.toString());
  }

  protected void genDeclArray(String name, String moduleName) throws IOException {
    if(nodeclMode || !getSymbolTable(moduleName).isUsedSymbolDestructor(name)) {
      return;
    }
    TomSymbol tomSymbol = getSymbolTable(moduleName).getSymbolFromName(name);
    TomType listType = TomBase.getSymbolCodomain(tomSymbol);
    String tomType = TomBase.getTomType(listType);
    String t = goType(listType);
    String it = goType(getSymbolTable(moduleName).getIntType());
    String empty = "tom_empty_array_" + name;
    String cons = "tom_cons_array_" + name;
    String elt = "tom_get_element_" + name + "_" + tomType;
    String size = "tom_get_size_" + name + "_" + tomType;
    StringBuilder s = new StringBuilder();
    s.append("func tom_get_slice_" + name + "(subject " + t + ", begin " + it + ", end " + it + ") " + t + " { ");
    s.append("var result " + t + " = " + empty + "(end-begin); for begin != end { result = " + cons + "(" + elt + "(subject, begin), result); begin++ }; return result }; ");
    s.append("func tom_append_array_" + name + "(l2 " + t + ", l1 " + t + ") " + t + " { ");
    s.append("var size1 " + it + " = " + size + "(l1); var size2 " + it + " = " + size + "(l2); var index " + it + "; ");
    s.append("var result " + t + " = " + empty + "(size1+size2); index = size1; ");
    s.append("for index > 0 { result = " + cons + "(" + elt + "(l1, size1-index), result); index-- }; index = size2; ");
    s.append("for index > 0 { result = " + cons + "(" + elt + "(l2, size2-index), result); index-- }; return result }; ");
    output.write(s.toString());
  }

  // ---------------------------------------------------------------- functions and %strategy

  protected void buildFunctionDef(int deep, String tomName, BQTermList argList, TomType codomain, TomType throwsType, Instruction instruction, String moduleName) throws IOException {
    buildMethod(deep,tomName,argList,codomain,instruction,moduleName,false);
  }

  protected void buildMethodDef(int deep, String tomName, BQTermList argList, TomType codomain, TomType throwsType, Instruction instruction, String moduleName) throws IOException {
    buildMethod(deep,tomName,argList,codomain,instruction,moduleName,true);
  }

  private void buildMethod(int deep, String tomName, BQTermList varList, TomType codomain,
      Instruction instruction, String moduleName, boolean isMethod) throws IOException {
    if(isMethod && currentClass != null) {
      output.write("func (" + RECEIVER + " *" + currentClass + ") " + tomName + "(");
    } else {
      output.write("func " + tomName + "(");
    }
    while(!varList.isEmptyconcBQTerm()) {
      BQTerm localVar = varList.getHeadconcBQTerm();
matchBlock: {
      %match(localVar) {
        v@BQVariable[AstType=type2] -> {
          generateBQTerm(deep,`v,moduleName);
          output.write(" " + goType(`type2));
          break matchBlock;
        }
        _ -> {
          throw new TomRuntimeException("GoGenerator.buildMethod: strange term: " + localVar);
        }
      }
    }
      varList = varList.getTailconcBQTerm();
      if(!varList.isEmptyconcBQTerm()) {
        output.write(", ");
      }
    }
    String ret = goType(codomain);
    output.write(") " + ret + " { ");
    generateInstruction(deep,instruction,moduleName);
    output.write(" }; ");
  }

  /*
   * %strategy S(p1:T1,…) extends E -> a struct embedding sl's
   * AbstractStrategyBasic, a constructor tom_new_S, one getter per
   * parameter, an exported VisitLight that converts MustVisit panics back
   * into errors, then the methods generated by the expander.
   * Limitation: Strategy-typed parameters are plain fields, they are not
   * exposed as children (enough for strategies used without mu over them).
   */
  protected void buildClass(int deep, String tomName, TomType extendsType, BQTerm superTerm, Declaration declaration, String moduleName) throws IOException {
    TomSymbol tomSymbol = getSymbolTable(moduleName).getSymbolFromName(tomName);
    TomTypeList tomTypes = TomBase.getSymbolDomain(tomSymbol);
    ArrayList<String> names = new ArrayList<String>();
    ArrayList<String> types = new ArrayList<String>();
    int index = 0;
    while(!tomTypes.isEmptyconcTomType()) {
      types.add(goType(tomTypes.getHeadconcTomType()));
      names.add(TomBase.getSlotName(tomSymbol, index).getString());
      tomTypes = tomTypes.getTailconcTomType();
      index++;
    }
    StringBuilder s = new StringBuilder();
    s.append("type " + tomName + " struct { *sl.AbstractStrategyBasic; ");
    for(int i = 0 ; i < names.size() ; i++) {
      s.append(names.get(i) + " " + types.get(i) + "; ");
    }
    s.append("}; func tom_new_" + tomName + "(");
    for(int i = 0 ; i < names.size() ; i++) {
      s.append(names.get(i) + " " + types.get(i) + (i+1 < names.size() ? ", " : ""));
    }
    s.append(") *" + tomName + " { " + RECEIVER + " := &" + tomName + "{");
    for(int i = 0 ; i < names.size() ; i++) {
      s.append(names.get(i) + ": " + names.get(i) + ", ");
    }
    s.append("}; " + RECEIVER + ".AbstractStrategyBasic = sl.NewAbstractStrategyBasic(");
    output.write(s.toString());
    generateBQTerm(deep,superTerm,moduleName);
    output.write("); return " + RECEIVER + " }; ");
    for(int i = 0 ; i < names.size() ; i++) {
      output.write("func (" + RECEIVER + " *" + tomName + ") get" + names.get(i) + "() " + types.get(i)
          + " { return " + RECEIVER + "." + names.get(i) + " }; ");
    }
    output.write("func (" + RECEIVER + " *" + tomName + ") VisitLight(v any, introspector sl.Introspector) (tom__res any, tom__err error) { "
        + "defer sl.RecoverVisitFailure(&tom__err); return " + RECEIVER + ".visitLight(v, introspector), nil }; ");
    currentClass = tomName;
    generateDeclaration(deep,`declaration,moduleName);
    currentClass = null;
  }

  protected void buildIntrospectorClass(int deep, String tomName, Declaration declaration, String moduleName) throws IOException {
    throw new TomRuntimeException("--genIntrospector is not supported with -go");
  }

  // ---------------------------------------------------------------- %transformation: unsupported

  protected String genResolveIsSortCode(String varName, String resolveStringName) throws IOException {
    throw new TomRuntimeException("%transformation is not supported with -go");
  }

  protected String genResolveIsFsymCode(String tomName, String varname) throws IOException {
    throw new TomRuntimeException("%transformation is not supported with -go");
  }

  protected String genResolveGetSlotCode(String tomName, String varname, String slotName) throws IOException {
    throw new TomRuntimeException("%transformation is not supported with -go");
  }

  protected void buildResolveClass(String wName, String tName, String extendsName, String moduleName) throws IOException {
    throw new TomRuntimeException("%transformation is not supported with -go");
  }

  protected void buildResolveInverseLinks(int deep, String fileFrom, String fileTo, TomNameList resolveNameList, String moduleName) throws IOException {
    throw new TomRuntimeException("%transformation is not supported with -go");
  }

  protected void genResolveDeclMake(String prefix, String funName, TomType returnType, BQTermList argList, String moduleName) throws IOException {
    throw new TomRuntimeException("%transformation is not supported with -go");
  }

  protected String genResolveMakeCode(String funName, BQTermList argList) throws IOException {
    throw new TomRuntimeException("%transformation is not supported with -go");
  }

  protected void buildReferenceClass(int deep, String refname, RefClassTracelinkInstructionList refclassTInstructions, String moduleName) {
    throw new TomRuntimeException("%transformation is not supported with -go");
  }

  protected void buildTracelink(int deep, String type, String name, Expression expr, String moduleName) throws IOException {
    throw new TomRuntimeException("%transformation is not supported with -go");
  }

  protected void buildTracelinkPopulateResolve(int deep, String refClassName, TomNameList tracedLinks, BQTerm current, BQTerm link, String moduleName) throws IOException {
    throw new TomRuntimeException("%transformation is not supported with -go");
  }

  protected void buildResolve(int deep, BQTerm bqterm, String moduleName) throws IOException {
    throw new TomRuntimeException("%transformation is not supported with -go");
  }
}
```

- [ ] **Step 5 : mappings Go**

```bash
cd /Users/pem/github/tom
mkdir -p src/tom/library/mapping/go/sl
cp tomgo/stable/library/mapping/go/{int,string,boolean,Strategy,sl}.tom src/tom/library/mapping/go/
cp tomgo/stable/library/mapping/go/sl/{basic,composed}.tom src/tom/library/mapping/go/sl/
```

Dans `build.xml`, dans la copie des mappings de la cible `src.dist`
(lignes ~917-929, l'`<fileset>` qui contient `<include name="java/**/*.tom"/>`)
et dans celle de `stable.dist` (lignes ~649-657), ajouter à côté de
l'include `java` :

```xml
        <include name="go/**/*.tom"/>
```

- [ ] **Step 6 : reconstruire `src/dist`**

```bash
cd /Users/pem/github/tom && unset TOM_HOME && ./build.sh src.dist 2>&1 | grep -E "error|BUILD" | head -30
```
Expected: `BUILD SUCCESSFUL`. Si `javac` signale « GoGenerator is not
abstract and does not override abstract method X », ajouter X en suivant
la version de `PythonGenerator.t` (même signature, corps Go ou
`throw new TomRuntimeException("… not supported with -go")`).
Vérifier aussi : `ls src/dist/share/tom/go/` → `int.tom … sl/`.

- [ ] **Step 7 : test de fumée — code hôte seul + `%match` sur `int`**

```bash
S=/private/tmp/claude-501/-Users-pem-github-tom/5a145986-8813-426a-94cd-8d4fe6344d29/scratchpad/smoke
rm -rf $S && mkdir -p $S && cd $S
cat > smoke.t <<'EOF'
package main

import "fmt"

%include { int.tom }

func classify(n int64) string {
  %match(n) {
    0 -> { return "zero" }
    x -> { return fmt.Sprint("n=", `x) }
  }
  return "unreachable"
}

func main() { fmt.Println(classify(0), classify(7)) }
EOF
printf 'module smoke\n\ngo 1.22\n' > go.mod
TOM_HOME=/Users/pem/github/tom/src/dist /Users/pem/github/tom/src/dist/bin/tom -go -np -d . smoke.t && ls && go run .
```
Expected: `smoke.go` créé, puis `zero n=7`. En cas d'échec de `go run`,
lire `smoke.go` (une seule ligne : `gofmt smoke.go` pour la lire), corriger
`GoGenerator.t`, reconstruire (Step 6), recommencer.

- [ ] **Step 8 : T0 toujours vert**

Run: `cd tomgo && mage portJava` → Expected: `ok`.

- [ ] **Step 9 : commit**

```bash
git add src/tom/engine/backend/GoGenerator.t src/tom/engine/backend/BackendPlugin.t \
  src/tom/engine/TomStreamManager.java src/tom/engine/tools/SymbolTable.t \
  src/tom/library/mapping/go build.xml
git commit -m "tom-go J0: add the goCode option and GoGenerator backend

Co-Authored-By: Claude Opus 5.5 <noreply@anthropic.com>"
```

---

### Task 5 (J1, T1) : harnais de portage + fixtures sans `%gom`

**Files:**
- Create: `tomgo/tests/port/port_test.go`, `tomgo/tests/port/{Peano,TestPeano,Array,TestArray,TestList}.t`, `tomgo/tests/port/golden/*.go`
- Modify: `tomgo/magefile.go`, `src/tom/engine/backend/GoGenerator.t` (corrections)

**Interfaces:**
- Consumes: `tomjava.NewDist`, `Dist.Compile`, `SetupGoModule`, `RunIn` (Task 2) ; `tom -go -np` (Task 4).
- Produces:
  - `type portCase struct{ Rel string; GoTest bool }` (Rel = chemin relatif à `test/`, ex. `numeric/TestNumericConditions.t`)
  - `var cases []portCase`
  - `func compileCase(t *testing.T, c portCase) (dir string)` — compile le `.t` porté dans un module temporaire, renvoie le répertoire.
  - `func runCase(t *testing.T, c portCase, dir string) (passed map[string]bool, stdout string, err error)`
  - flag `-update` (golden).

- [ ] **Step 1 : copier les 5 fixtures**

```bash
cd /Users/pem/github/tom/tomgo
mkdir -p tests/port/golden
for f in Peano TestPeano Array TestArray TestList; do cp tests/tom/compile/fixtures/go/$f.t tests/port/$f.t; done
```

- [ ] **Step 2 : écrire `tomgo/tests/port/port_test.go`**

```go
// Package port holds the Tom+Go ports of test/*.t and checks them:
// T1 (generated Go is pinned by golden files and builds) and, from
// Task 8, T2 (same behaviour as the original under legacy/).
package port

import (
	"bufio"
	"encoding/json"
	"flag"
	"os"
	"path/filepath"
	"strings"
	"testing"

	"tom/tomgo/tests/tomjava"
)

var update = flag.Bool("update", false, "rewrite golden/*.go from the current compiler")

type portCase struct {
	Rel    string // path relative to test/ (and to tests/port/)
	GoTest bool   // run with `go test` (JUnit-style) rather than `go run`
}

var cases = []portCase{
	{Rel: "Peano.t"},
	{Rel: "TestPeano.t", GoTest: true},
	{Rel: "Array.t"},
	{Rel: "TestArray.t", GoTest: true},
	{Rel: "TestList.t", GoTest: true},
}

func base(c portCase) string { return strings.TrimSuffix(filepath.Base(c.Rel), ".t") }

func compileCase(t *testing.T, c portCase) string {
	t.Helper()
	dist := tomjava.NewDist(t)
	dir := t.TempDir()
	src, err := filepath.Abs(c.Rel)
	if err != nil {
		t.Fatal(err)
	}
	if out, err := dist.Compile(t, src, dir, "-go", "-np"); err != nil {
		t.Fatalf("tom -go failed: %v\n%s", err, out)
	}
	gen := filepath.Join(dir, base(c)+".go")
	if c.GoTest {
		if err := os.Rename(gen, filepath.Join(dir, base(c)+"_test.go")); err != nil {
			t.Fatal(err)
		}
		gen = filepath.Join(dir, base(c)+"_test.go")
	}
	checkGolden(t, c, gen)
	tomjava.SetupGoModule(t, dir)
	return dir
}

func checkGolden(t *testing.T, c portCase, gen string) {
	t.Helper()
	got, err := os.ReadFile(gen)
	if err != nil {
		t.Fatal(err)
	}
	golden := filepath.Join("golden", strings.ReplaceAll(strings.TrimSuffix(c.Rel, ".t"), "/", "_")+".go")
	if *update {
		if err := os.WriteFile(golden, got, 0o644); err != nil {
			t.Fatal(err)
		}
		return
	}
	want, err := os.ReadFile(golden)
	if err != nil {
		t.Fatalf("missing golden %s (run with -update): %v", golden, err)
	}
	if string(want) != string(got) {
		t.Fatalf("generated Go differs from %s (run with -update if intended)", golden)
	}
}

// runCase runs the compiled case; for go-test cases it returns the set
// of test names that passed.
func runCase(t *testing.T, c portCase, dir string) (map[string]bool, string, error) {
	t.Helper()
	if !c.GoTest {
		out, err := tomjava.RunIn(dir, nil, "go", "run", ".")
		return nil, out, err
	}
	out, err := tomjava.RunIn(dir, nil, "go", "test", "-json", "-count=1", ".")
	passed := map[string]bool{}
	sc := bufio.NewScanner(strings.NewReader(out))
	for sc.Scan() {
		var ev struct{ Action, Test string }
		if json.Unmarshal(sc.Bytes(), &ev) == nil && ev.Test != "" && ev.Action == "pass" {
			passed[ev.Test] = true
		}
	}
	return passed, out, err
}

func TestGoBackend(t *testing.T) {
	for _, c := range cases {
		c := c
		t.Run(c.Rel, func(t *testing.T) {
			dir := compileCase(t, c)
			if _, out, err := runCase(t, c, dir); err != nil {
				t.Fatalf("running %s failed: %v\n%s", c.Rel, err, out)
			}
		})
	}
}
```

- [ ] **Step 3 : lancer — échec attendu (pas de golden)**

Run: `cd tomgo && go test ./tests/port/ -run TestGoBackend -count=1 2>&1 | tail -20`
Expected: FAIL `missing golden …` pour chaque cas (ou une erreur
`tom -go` / `go build` antérieure, à traiter Step 4).

- [ ] **Step 4 : boucle de mise au point**

Pour chaque cas, jusqu'à ce que `go test ./tests/port/ -run TestGoBackend -update` passe :
1. lire l'erreur (`tom -go`, compilation Go ou exécution) ;
2. inspecter le Go produit : relancer la commande `tom -go -np -d /tmp/x tests/port/<cas>.t` puis `gofmt /tmp/x/<cas>.go` ;
3. corriger **`GoGenerator.t`** (jamais le cas porté, sauf si sa traduction Go
   du code hôte est fautive : la comparer au `.t` original de `test/`) ;
4. `./build.sh src.dist`, relancer.

Points à surveiller, connus d'avance :
- signature `tom_is_sort_<S>(t any)` : les mappings écrits à la main doivent accepter `any` (`$t != nil` convient) ;
- `Cast` → `any(e).(T)` : erreur si `T` vide ;
- variables `tomMatch…` déclarées et seulement réaffectées : déjà couvert par `_ = x` ;
- code hôte de l'utilisateur après un bloc compilé : le `};` final sépare correctement.

- [ ] **Step 5 : vérifier les goldens puis relancer sans `-update`**

Relire chaque `golden/*.go` (`gofmt golden/TestList.go | less`) : pas de
code Java résiduel (`instanceof`, `new `, `null`). Puis :

Run: `cd tomgo && go test ./tests/port/ -run TestGoBackend -count=1`
Expected: `ok`.

- [ ] **Step 6 : cible mage `PortGo` et T0**

```go
// PortGo runs T1 (golden Go output) and T2 (behaviour vs legacy/) on the
// Tom+Go ports in tests/port/, and rewrites reports/port-dashboard.md.
func PortGo() error {
	return sh.RunV("go", "test", "-count=1", "./tests/port/...", "-args", "-write-dashboard")
}
```

(Le flag `-write-dashboard` est introduit en Task 8 ; d'ici là, retirer
`"-args", "-write-dashboard"`.) Run: `mage portGo && mage portJava` → `ok` ×2.

- [ ] **Step 7 : commit**

```bash
git add tomgo/tests/port tomgo/magefile.go src/tom/engine/backend/GoGenerator.t
git commit -m "tom-go J1: port harness, golden Go output for Peano/Array/List fixtures

Co-Authored-By: Claude Opus 5.5 <noreply@anthropic.com>"
```

---

### Task 6 (J1) : délégation des `%gom` à `tomgo gom-block`

**Files:**
- Modify: `src/tom/engine/parser/TomParserTool.t`, `tomgo/tests/port/port_test.go`
- Create: `tomgo/tests/port/{TestBackQuote,TestVarVarStar,TestNonVarSubjects,TestSublists}.t`, `tomgo/tests/port/numeric/TestNumericConditions.t`, goldens correspondants

**Interfaces:**
- Consumes: `tomgo gom-block` (Task 3), variable d'environnement `TOMGO_BIN` positionnée par `Dist.Compile` (Task 2).
- Produces: avec `-go`, `parseGomFile` renvoie le chemin du mapping `.tom` écrit par `tomgo gom-block` dans le répertoire du fichier de sortie, package Go = première ligne `package X` du `.t`.

- [ ] **Step 1 : ajouter les cas (ils échouent)**

```bash
cd /Users/pem/github/tom/tomgo
for f in TestBackQuote TestVarVarStar TestNonVarSubjects TestSublists; do cp tests/tom/compile/fixtures/go/$f.t tests/port/; done
mkdir -p tests/port/numeric && cp tests/tom/compile/fixtures/go/TestNumericConditions.t tests/port/numeric/
```

Dans `cases` de `port_test.go`, ajouter :

```go
	{Rel: "TestBackQuote.t", GoTest: true},
	{Rel: "TestVarVarStar.t", GoTest: true},
	{Rel: "TestNonVarSubjects.t", GoTest: true},
	{Rel: "TestSublists.t", GoTest: true},
	{Rel: "numeric/TestNumericConditions.t", GoTest: true},
```

Run: `cd tomgo && go test ./tests/port/ -run 'TestGoBackend/(TestBackQuote|numeric)' -count=1 2>&1 | tail`
Expected: FAIL — `tom -go` échoue sur le `%gom` (Gom Java appelé, mapping Java).

- [ ] **Step 2 : `TomParserTool.t` — branche Go**

En tête de `parseGomFile`, juste après `String res = "";` :

```java
    if(Boolean.TRUE == getOptionManager().getOptionValue("goCode")) {
      return parseGomFileGo(gomCode, initialGomLine);
    }
```

Ajouter dans la classe (avant `public void printTree`) :

```java
  /*
   * -go: the %gom block is compiled by `tomgo gom-block`, which writes the
   * Go structures next to the generated file and prints the path of the
   * Go-host-code mapping to include.
   */
  private String parseGomFileGo(String gomCode, int initialGomLine) {
    String currentFileName = getStreamManager().getInputFileName();
    try {
      File tmpFile = File.createTempFile("tmp", ".gom", null).getCanonicalFile();
      tmpFile.deleteOnExit();
      Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tmpFile),"UTF-8"));
      writer.write(gomCode);
      writer.close();
      File outDir = getStreamManager().getOutputFile().getCanonicalFile().getParentFile();
      outDir.mkdirs();
      String tomgo = System.getenv("TOMGO_BIN");
      if(tomgo == null || tomgo.length() == 0) {
        tomgo = "tomgo";
      }
      ProcessBuilder pb = new ProcessBuilder(tomgo, "gom-block",
          "--pkg", goPackageOf(new File(currentFileName)),
          "-o", outDir.getPath(), tmpFile.getPath());
      pb.redirectError(ProcessBuilder.Redirect.INHERIT);
      Process p = pb.start();
      java.io.BufferedReader r = new java.io.BufferedReader(new java.io.InputStreamReader(p.getInputStream(),"UTF-8"));
      String line;
      String last = null;
      while((line = r.readLine()) != null) {
        if(line.trim().length() > 0) {
          last = line.trim();
        }
      }
      if(p.waitFor() != 0 || last == null) {
        TomMessage.error(getLogger(), currentFileName, initialGomLine, TomMessage.gomFailure,currentFileName,Integer.valueOf(initialGomLine));
        return "";
      }
      return last;
    } catch (Exception e) {
      TomMessage.error(getLogger(), currentFileName, initialGomLine, TomMessage.gomInitFailure,currentFileName,Integer.valueOf(initialGomLine), e);
      return "";
    }
  }

  /** the Go package declared by the first `package X` line of a .t file */
  private static String goPackageOf(File f) throws IOException {
    java.util.regex.Pattern pat = java.util.regex.Pattern.compile("^\\s*package\\s+([A-Za-z_][A-Za-z0-9_]*)");
    java.io.BufferedReader r = new java.io.BufferedReader(new java.io.InputStreamReader(new FileInputStream(f),"UTF-8"));
    try {
      String line;
      while((line = r.readLine()) != null) {
        java.util.regex.Matcher m = pat.matcher(line);
        if(m.find()) {
          return m.group(1);
        }
      }
    } finally {
      r.close();
    }
    return "main";
  }
```

Vérifier en tête de fichier que `File`, `FileOutputStream`, `FileInputStream`,
`Writer`, `BufferedWriter`, `OutputStreamWriter`, `IOException` sont importés
(les quatre premiers sont déjà utilisés par `parseGomFile` ; ajouter
`import java.io.FileInputStream;` s'il manque).

- [ ] **Step 3 : le harnais compile le `.t` dans un répertoire qui contiendra aussi `<module>_gen.go`**

Rien à changer : `Dist.Compile` passe `-d dir` et `TOMGO_BIN`. Vérifier que
`getOutputFile()` pointe bien dans `dir` (sinon le `_gen.go` serait écrit
ailleurs) :

```bash
cd /Users/pem/github/tom && unset TOM_HOME && ./build.sh src.dist 2>&1 | tail -1
cd tomgo && go test ./tests/port/ -run 'TestGoBackend/TestBackQuote' -count=1 -update 2>&1 | tail
```
Expected: PASS ; le répertoire temporaire contient `TestBackQuote_test.go`,
`<module>_gen.go`, `<Module>.tom`, `go.mod`.

- [ ] **Step 4 : mettre au point les 5 cas (même boucle que Task 5 Step 4)**

Corrections attendues dans `GoGenerator.t` ou dans `tomgo` (backend Gom) ;
une correction dans `tomgo/stable/gom/backend` doit garder
`go test ./stable/gom/...` vert (dont `TestSelfBootstrap*`).

Run final: `cd tomgo && go test ./tests/port/ -count=1 && go test ./stable/gom/... -count=1`
Expected: `ok` ×2.

- [ ] **Step 5 : T0 + commit**

Run: `cd tomgo && mage portJava` → `ok`.

```bash
git add src/tom/engine/parser/TomParserTool.t tomgo/tests/port
git commit -m "tom-go J1: delegate %gom blocks to tomgo gom-block under -go

Co-Authored-By: Claude Opus 5.5 <noreply@anthropic.com>"
```

---

### Task 7 (J1) : `%strategy` en Go

**Files:**
- Modify: `src/tom/engine/expander/ExpanderPlugin.t`, `src/tom/engine/parser/antlr4/CstConverter.t`, `src/tom/engine/parser/TomParserPlugin.t`, `tomgo/tests/port/port_test.go`
- Create: `tomgo/tests/port/TestAlgebraic.t`, `tomgo/tests/port/golden/TestAlgebraic.go`

**Interfaces:**
- Consumes: `GoGenerator.buildClass` / `RECEIVER` (Task 4), `sl.MustVisit` / `sl.RecoverVisitFailure` (Task 3).
- Produces: avec `-go`, un `%strategy S(…) extends E { visit T { … } }` devient le type `*S` (satisfait `sl.Strategy`), construit par `tom_make_S(…)`.
- `public static boolean CstConverter.goTarget`.

- [ ] **Step 1 : ajouter le cas (il échoue)**

```bash
cp tomgo/tests/tom/compile/fixtures/go/TestAlgebraic.t tomgo/tests/port/
```
Ajouter `{Rel: "TestAlgebraic.t", GoTest: true},` à `cases`.

Run: `cd tomgo && go test ./tests/port/ -run 'TestGoBackend/TestAlgebraic' -count=1 2>&1 | tail`
Expected: FAIL (code Java `instanceof` dans le mapping de la stratégie, ou
`visitLight` Java).

- [ ] **Step 2 : mapping `%op Strategy` en Go (`CstConverter.t`)**

Ajouter dans la classe `CstConverter` : `public static boolean goTarget = false;`

Dans la règle `s@Cst_StrategyConstruct(...)` (lignes ~123-155), remplacer

```java
            getslots += %[get_slot(@`name@,t) { ((@`stratName@)$t).get@`name@() }]%;
```
par
```java
            if(goTarget) {
              getslots += %[get_slot(@`name@,t) { ($t).(*@`stratName@).get@`name@() }]%;
            } else {
              getslots += %[get_slot(@`name@,t) { ((@`stratName@)$t).get@`name@() }]%;
            }
```

et remplacer la déclaration `String opdecl = %[ … ]%;` par :

```java
        String opdecl;
        if(goTarget) {
          opdecl = %[
          %op Strategy @`stratName@(@opargs@) {
            is_fsym(t) { func() bool { _, ok := ($t).(*@`stratName@); return ok }() }
            @getslots@
            make(@makeargs@) { tom_new_@`stratName@(@newargs@) }
          }
        ]%;
        } else {
          opdecl = %[
          %op Strategy @`stratName@(@opargs@) {
            is_fsym(t) { ($t instanceof @`stratName@) }
            @getslots@
            make(@makeargs@) { new @`stratName@(@newargs@) }
          }
        ]%;
        }
```

Dans `TomParserPlugin.t`, dans `run`, juste après la ligne
`boolean java = ((Boolean)getOptionManager().getOptionValue("jCode")).booleanValue();` :

```java
    tom.engine.parser.antlr4.CstConverter.goTarget = ((Boolean)getOptionManager().getOptionValue("goCode")).booleanValue();
```

- [ ] **Step 3 : branche Go de l'expander (`ExpanderPlugin.t`)**

Après `private static boolean generateAdaCode = false;` :

```java
  private static boolean generateGoCode = false;
  /** prefix of calls to the generated visit methods ("tom__strat." in Go) */
  private static String callPrefix = "";
```

Dans `run`, remplacer la ligne `	if(getOptionBooleanValue("aCode")) {` par :

```java
	generateAdaCode = false;
	generateGoCode = false;
	callPrefix = "";
	if(getOptionBooleanValue("goCode")) {
		generateGoCode = true;
		callPrefix = tom.engine.backend.GoGenerator.RECEIVER + ".";
		visitFuncPrefix = "_";
		objectType = ASTFactory.makeType(`concTypeOption(),"undefined","any");
		genericType = ASTFactory.makeType(`concTypeOption(),"undefined","any");
		methodparameterType = ASTFactory.makeType(`concTypeOption(),"undefined","any");
		objectArrayType = ASTFactory.makeType(`concTypeOption(),"undefined","[]any");
		intType = ASTFactory.makeType(`concTypeOption(),"int","int64");
		basicStratType = ASTFactory.makeType(`concTypeOption(),"undefined","sl.AbstractStrategyBasic");
		introspectorType = ASTFactory.makeType(`concTypeOption(),"undefined","sl.Introspector");
		visitfailureType = ASTFactory.makeType(`concTypeOption(),"undefined","error");
		introspectorVar = `BQVariable(concOption(),Name("introspector"),introspectorType);
		objectVar = `BQVariable(concOption(),Name("o"),objectType);
		childVar = `BQVariable(concOption(),Name("child"),objectType);
		intVar = `BQVariable(concOption(),Name("i"),intType);
		objectArrayVar = `BQVariable(concOption(),Name("children"),objectArrayType);
	} else if(getOptionBooleanValue("aCode")) {
```

Dans `Expand_once`, juste après `Strategy(name,extendsTerm,visitList,hooks,orgTrack) -> {` :

```java
        if(generateGoCode && expander.getGenIntrospector()) {
          throw new TomRuntimeException("--genIntrospector is not supported with -go");
        }
```
(ajouter `import tom.engine.exception.TomRuntimeException;` s'il manque.)

Remplacer
```java
            Instruction returnStatement = `Return(FunctionCall(Name(visitFuncPrefix+ funcName),vType,subjectListAST));
```
par
```java
            Instruction returnStatement = `Return(FunctionCall(Name(callPrefix + visitFuncPrefix + funcName),vType,subjectListAST));
```

Remplacer
```java
          Instruction returnStatement = `Return(ExpressionToBQTerm(Cast(genericType,BQTermToExpression(FunctionCall(Name(dispatchInfo.get(type)),type,funcArg)))));
```
par
```java
          Instruction returnStatement = `Return(ExpressionToBQTerm(Cast(genericType,BQTermToExpression(FunctionCall(Name(callPrefix + dispatchInfo.get(type)),type,funcArg)))));
```

Remplacer le bloc
```java
          } else {
            environmentVar = `BQVariable(concOption(orgTrack),Name("environment"),EmptyType());
            return1 = `Return(ExpressionToBQTerm(Cast(type,TomInstructionToExpression(CodeToInstruction(TargetLanguageToCode(ITL("any.visit(environment,introspector)")))))));
            return2 = `Return(Composite(CompositeTL(ITL("any.visitLight(arg,introspector)"))));
          }
```
par
```java
          } else if(generateGoCode) {
            environmentVar = `BQVariable(concOption(orgTrack),Name("environment"),EmptyType());
            return2 = `Return(ExpressionToBQTerm(Cast(type,BQTermToExpression(Composite(CompositeTL(ITL("sl.MustVisit(" + callPrefix + "Any().VisitLight(arg,introspector))")))))));
            return1 = return2;
          } else {
            environmentVar = `BQVariable(concOption(orgTrack),Name("environment"),EmptyType());
            return1 = `Return(ExpressionToBQTerm(Cast(type,TomInstructionToExpression(CodeToInstruction(TargetLanguageToCode(ITL("any.visit(environment,introspector)")))))));
            return2 = `Return(Composite(CompositeTL(ITL("any.visitLight(arg,introspector)"))));
          }
```

Remplacer
```java
          Instruction ifThenElse = `If(testEnvNotNull,return1,return2);
```
par
```java
          Instruction ifThenElse;
          if(generateGoCode) {
            ifThenElse = return2; // the Go sl has no environment-based visit
          } else {
            ifThenElse = `If(testEnvNotNull,return1,return2);
          }
```

Remplacer
```java
        } else {
          ifList = `concInstruction(ifList*,              
              If(testEnvNotNull,
                Return(ExpressionToBQTerm(Cast(genericType,BQTermToExpression(Composite(CompositeTL(ITL("any.visit(environment,introspector)"))))))),
                Return(Composite(CompositeTL(ITL("any.visitLight(v,introspector)"))))));
        }
```
par
```java
        } else if(generateGoCode) {
          ifList = `concInstruction(ifList*,
              Return(Composite(CompositeTL(ITL("sl.MustVisit(" + callPrefix + "Any().VisitLight(v,introspector))")))));
        } else {
          ifList = `concInstruction(ifList*,              
              If(testEnvNotNull,
                Return(ExpressionToBQTerm(Cast(genericType,BQTermToExpression(Composite(CompositeTL(ITL("any.visit(environment,introspector)"))))))),
                Return(Composite(CompositeTL(ITL("any.visitLight(v,introspector)"))))));
        }
```

- [ ] **Step 4 : reconstruire, mettre au point, golden**

```bash
cd /Users/pem/github/tom && unset TOM_HOME && ./build.sh src.dist 2>&1 | tail -1
cd tomgo && go test ./tests/port/ -run 'TestGoBackend/TestAlgebraic' -count=1 -update
```
Même boucle que Task 5 Step 4. Points connus : le `.t` doit importer
`"tom/tomgo/stable/library/sl"` (c'est le cas de la fixture) ; `visitLight`
(minuscule) est la méthode générée par l'expander, `VisitLight` le wrapper
exporté écrit par `buildClass`.

- [ ] **Step 5 : tout le harnais + T0 + commit**

Run: `cd tomgo && go test ./tests/port/ -count=1 && mage portJava` → `ok` ×2.

```bash
git add src/tom/engine/expander/ExpanderPlugin.t src/tom/engine/parser/antlr4/CstConverter.t \
  src/tom/engine/parser/TomParserPlugin.t tomgo/tests/port
git commit -m "tom-go J1: compile %strategy to Go structs on top of the Go sl library

Co-Authored-By: Claude Opus 5.5 <noreply@anthropic.com>"
```

---

### Task 8 (T2) : oracle `legacy/` et tableau de bord

**Files:**
- Modify: `tomgo/tests/port/port_test.go`, `tomgo/magefile.go`
- Create: `tomgo/tests/port/oracle/*.json`, `tomgo/reports/port-dashboard.md`

**Interfaces:**
- Consumes: `cases`, `compileCase`, `runCase` (Task 5) ; `tomjava.LegacyDist` (Task 2).
- Produces:
  - `type oracle struct{ Passed []string `json:"passed"`; Stdout string `json:"stdout"`; ExitOK bool `json:"exitOK"` }`
  - `func legacyOracle(t *testing.T, c portCase) oracle` — lit `oracle/<rel>.json`, ou le calcule avec `legacy/` si `-update-oracle`.
  - flags `-update-oracle`, `-write-dashboard`.
  - statuts : `non porté`, `échec tom`, `échec go build`, `échec go test`, `divergence`, `OK`.

- [ ] **Step 1 : test d'équivalence (échoue : pas d'oracle)**

Ajouter à `port_test.go` :

```go
var (
	updateOracle   = flag.Bool("update-oracle", false, "recompute oracle/*.json with legacy/")
	writeDashboard = flag.Bool("write-dashboard", false, "rewrite ../../reports/port-dashboard.md")
)

type oracle struct {
	Passed []string `json:"passed"` // JUnit tests that passed, Go-capitalised (testFoo -> TestFoo)
	Stdout string   `json:"stdout"` // stdout of a main program
	ExitOK bool     `json:"exitOK"`
}

func oraclePath(c portCase) string {
	return filepath.Join("oracle", strings.ReplaceAll(strings.TrimSuffix(c.Rel, ".t"), "/", "_")+".json")
}

func capitalise(name string) string {
	if name == "" {
		return name
	}
	return strings.ToUpper(name[:1]) + name[1:]
}

// legacyOracle compiles and runs the ORIGINAL test/<Rel> with the
// human-written compiler (javac + JUnit or java main) and caches what
// it observed.
func legacyOracle(t *testing.T, c portCase) oracle {
	t.Helper()
	path := oraclePath(c)
	if !*updateOracle {
		data, err := os.ReadFile(path)
		if err != nil {
			t.Fatalf("missing %s (run with -update-oracle)", path)
		}
		var o oracle
		if err := json.Unmarshal(data, &o); err != nil {
			t.Fatal(err)
		}
		return o
	}
	legacy := tomjava.LegacyDist(t)
	root := tomjava.RepoRoot(t)
	src := filepath.Join(root, "test", c.Rel)
	dir := t.TempDir()
	if out, err := legacy.Compile(t, src, dir); err != nil {
		t.Fatalf("legacy tom failed: %v\n%s", err, out)
	}
	cp := filepath.Join(legacy.Home, "lib", "tom-runtime-full.jar") + string(os.PathListSeparator) +
		filepath.Join(root, "legacy", "stable", "lib", "compiletime", "junit.jar") + string(os.PathListSeparator) + dir
	var javaFiles []string
	filepath.Walk(dir, func(p string, info os.FileInfo, err error) error {
		if err == nil && strings.HasSuffix(p, ".java") {
			javaFiles = append(javaFiles, p)
		}
		return err
	})
	if out, err := tomjava.RunIn(dir, nil, "javac", append([]string{"-nowarn", "-cp", cp, "-d", dir}, javaFiles...)...); err != nil {
		t.Fatalf("javac failed: %v\n%s", err, out)
	}
	class := base(c)
	if pkg := javaPackage(t, src); pkg != "" {
		class = pkg + "." + class
	}
	var o oracle
	if c.GoTest {
		out, err := tomjava.RunIn(dir, nil, "java", "-cp", cp, "org.junit.runner.JUnitCore", class)
		o.ExitOK = err == nil
		failed := map[string]bool{}
		for _, line := range strings.Split(out, "\n") {
			// JUnitCore failure header: "1) testFoo(Class)"
			if i := strings.Index(line, ") "); i > 0 && strings.HasSuffix(line, ")") {
				name := line[i+2:]
				if j := strings.Index(name, "("); j > 0 {
					failed[name[:j]] = true
				}
			}
		}
		for _, m := range junitTests(t, src) {
			if !failed[m] {
				o.Passed = append(o.Passed, capitalise(m))
			}
		}
	} else {
		out, err := tomjava.RunIn(dir, nil, "java", "-cp", cp, class)
		o.Stdout, o.ExitOK = out, err == nil
	}
	data, _ := json.MarshalIndent(o, "", "  ")
	if err := os.WriteFile(path, data, 0o644); err != nil {
		t.Fatal(err)
	}
	return o
}

// junitTests lists the @Test methods of a JUnit .t source.
func junitTests(t *testing.T, src string) []string {
	data, err := os.ReadFile(src)
	if err != nil {
		t.Fatal(err)
	}
	var names []string
	lines := strings.Split(string(data), "\n")
	for i, line := range lines {
		if strings.TrimSpace(line) != "@Test" && !strings.HasPrefix(strings.TrimSpace(line), "@Test(") {
			continue
		}
		for _, next := range lines[i+1:] {
			if j := strings.Index(next, "void "); j >= 0 {
				rest := next[j+5:]
				if k := strings.Index(rest, "("); k > 0 {
					names = append(names, strings.TrimSpace(rest[:k]))
				}
				break
			}
		}
	}
	return names
}

func javaPackage(t *testing.T, src string) string {
	data, _ := os.ReadFile(src)
	for _, line := range strings.Split(string(data), "\n") {
		if s := strings.TrimSpace(line); strings.HasPrefix(s, "package ") {
			return strings.TrimSuffix(strings.TrimSpace(strings.TrimPrefix(s, "package ")), ";")
		}
	}
	return ""
}

func TestSameBehaviourAsLegacy(t *testing.T) {
	for _, c := range cases {
		c := c
		t.Run(c.Rel, func(t *testing.T) {
			want := legacyOracle(t, c)
			dir := compileCase(t, c)
			passed, out, err := runCase(t, c, dir)
			if c.GoTest {
				for _, name := range want.Passed {
					if !passed[name] {
						t.Errorf("%s passes under legacy/ but not in Go", name)
					}
					delete(passed, name)
				}
				for name := range passed {
					if !strings.Contains(name, "/") {
						t.Errorf("%s passes in Go but not under legacy/ (or does not exist there)", name)
					}
				}
				return
			}
			if (err == nil) != want.ExitOK || out != want.Stdout {
				t.Errorf("main program differs\nlegacy (ok=%v):\n%s\ngo (err=%v):\n%s", want.ExitOK, want.Stdout, err, out)
			}
		})
	}
}
```

Run: `cd tomgo && go test ./tests/port/ -run TestSameBehaviourAsLegacy -count=1 2>&1 | tail`
Expected: FAIL — `missing oracle/… (run with -update-oracle)`.

- [ ] **Step 2 : calculer les oracles**

Run: `mkdir -p tomgo/tests/port/oracle && cd tomgo && go test ./tests/port/ -run TestSameBehaviourAsLegacy -count=1 -update-oracle 2>&1 | tail -30`

Relire chaque `oracle/*.json` : `passed` doit lister les `@Test` de
l'original (moins ceux qui échouent sous `legacy/`). Un `.json` vide
pour un cas JUnit signale un problème d'extraction (`junitTests`) : le corriger.

- [ ] **Step 3 : mettre en cohérence les ports**

Relancer sans `-update-oracle`. Chaque erreur est l'un de :
- un test présent dans l'original mais absent/renommé dans le port → corriger le port (nom `TestFoo` pour `testFoo`) ;
- un test commenté dans l'original mais actif dans le port → le commenter dans le port ;
- un `main` dont la sortie diffère → aligner l'affichage Go du port sur l'original.

Run: `cd tomgo && go test ./tests/port/ -count=1` → Expected: `ok`.

- [ ] **Step 4 : tableau de bord**

Ajouter à `port_test.go` :

```go
// TestDashboard classifies every test/*.t (outside error/) and, with
// -write-dashboard, rewrites reports/port-dashboard.md.
func TestDashboard(t *testing.T) {
	if !*writeDashboard {
		t.Skip("run with -write-dashboard")
	}
	root := tomjava.RepoRoot(t)
	ported := map[string]portCase{}
	for _, c := range cases {
		ported[c.Rel] = c
	}
	var rows []string
	counts := map[string]int{}
	filepath.Walk(filepath.Join(root, "test"), func(p string, info os.FileInfo, err error) error {
		if err != nil || info.IsDir() || !strings.HasSuffix(p, ".t") {
			return err
		}
		rel, _ := filepath.Rel(filepath.Join(root, "test"), p)
		if strings.HasPrefix(rel, "error/") {
			return nil
		}
		status := "non porté"
		if c, ok := ported[rel]; ok {
			status = dashboardStatus(t, c)
		}
		counts[status]++
		rows = append(rows, "| `"+rel+"` | "+status+" |")
		return nil
	})
	var b strings.Builder
	b.WriteString("# Tableau de bord du portage Tom+Go (T2)\n\nGénéré par `mage portGo`.\n\n")
	for _, s := range []string{"OK", "divergence", "échec go test", "échec go build", "échec tom", "non porté"} {
		b.WriteString("- " + s + " : " + itoa(counts[s]) + "\n")
	}
	b.WriteString("\n| Cas | Statut |\n|---|---|\n" + strings.Join(rows, "\n") + "\n")
	if err := os.WriteFile(filepath.Join(root, "tomgo", "reports", "port-dashboard.md"), []byte(b.String()), 0o644); err != nil {
		t.Fatal(err)
	}
}

func itoa(n int) string { return strconv.Itoa(n) }

// dashboardStatus re-runs one case without failing the dashboard test.
func dashboardStatus(t *testing.T, c portCase) string {
	ok := t.Run("status/"+c.Rel, func(t *testing.T) { compileCase(t, c) })
	if !ok {
		return "échec tom"
	}
	var status string
	t.Run("run/"+c.Rel, func(t *testing.T) {
		dir := compileCase(t, c)
		if out, err := tomjava.RunIn(dir, nil, "go", "vet", "."); err != nil && strings.Contains(out, "build failed") {
			status = "échec go build"
			return
		}
		want := legacyOracle(t, c)
		passed, out, err := runCase(t, c, dir)
		switch {
		case c.GoTest && err != nil && len(passed) == 0:
			status = "échec go test"
		case c.GoTest:
			status = "OK"
			for _, n := range want.Passed {
				if !passed[n] {
					status = "divergence"
				}
			}
		case (err == nil) != want.ExitOK || out != want.Stdout:
			status = "divergence"
		default:
			status = "OK"
		}
	})
	return status
}
```

Ajouter `"strconv"` aux imports. Remettre `"-args", "-write-dashboard"`
dans la cible `PortGo` (Task 5 Step 6).

Run: `cd tomgo && mage portGo && head -12 reports/port-dashboard.md`
Expected: `- OK : 11`, `- non porté : 92`.

- [ ] **Step 5 : commit**

```bash
git add tomgo/tests/port tomgo/magefile.go tomgo/reports/port-dashboard.md
git commit -m "tom-go T2: legacy/ oracle, behavioural parity and port dashboard

Co-Authored-By: Claude Opus 5.5 <noreply@anthropic.com>"
```

---

### Task 8b : stratégies avec environnement (`visit(Environment)`, `Position`)

Ajoutée à la demande de l'utilisateur (2026-10-22). L'API à environnement
est utilisée par 16 fichiers de `test/` (dont `test/sl/TestEnvironment.t`,
`TestStrategy.testPosition`, `TestAlgebraic` qui appelle `s.visit(input)`)
et par le compilateur lui-même (`OptimizerPlugin.t` : `getEnvironment`,
`getPosition`), donc indispensable pour J3/J4.

**Spécification exécutable :** `src/tom/library/sl/` (Java) — `Strategy.java`,
`AbstractStrategy.java`, `AbstractStrategyBasic.java`,
`AbstractStrategyCombinator.java`, `Environment.java` (322 l.),
`Position.java` (371 l.), et la méthode `int visit(Introspector)` de chaque
combinateur déjà porté en Go (`Identity`, `Fail`, `Sequence`, `SequenceId`,
`Choice`, `ChoiceId`, `All`, `One`, `OneId`, `Mu`, `MuVar`, builtins).

**Files:**
- Create: `tomgo/stable/library/sl/environment.go`, `position.go` (+ tests)
- Modify: `tomgo/stable/library/sl/*.go` (interface `Strategy` + chaque combinateur), `src/tom/engine/expander/ExpanderPlugin.t` (branche Go du chemin environnement), `src/tom/engine/backend/GoGenerator.t` (`buildClass`), mappings Go (`Position`, `Environment` si les originaux les incluent), `tomgo/tests/port/TestStrategy.t` (réactiver `TestPosition`), `tomgo/tests/port/TestAlgebraic.t` (revenir à `s.visit` fidèle), `tomgo/tests/port/port_test.go`

**Interfaces (à suivre au plus près de Java, en Go idiomatique) :**
- `type Position`, `type Environment` portés de Java (pile de sujets, omega, `Up`/`Down`/`FollowPath`, `GetSubject`/`SetSubject`, `GetRoot`, `GetPosition`, `GetIntrospector`).
- `Strategy` gagne : `SetEnvironment(*Environment)`, `GetEnvironment() *Environment`, `VisitEnv(intro Introspector) int` (équivalent de `int visit(Introspector)` : 0 = succès, sinon échec, sujet lu/écrit dans l'environnement), et un point d'entrée `Visit(subject any, intro Introspector) (any, error)` (équivalent de `visit(T any, Introspector)` : crée l'environnement, initialise la stratégie, visite, renvoie la racine).
- Chaque combinateur implémente `VisitEnv` en portant le code Java correspondant ; `VisitLight` reste inchangé.
- Stratégies générées : `VisitEnv` comme `AbstractStrategyBasic.visit(Introspector)` (visitLight sur le sujet de l'environnement, `SetSubject`, échec → code d'échec) ; `_visit_T` reprend le test Java `environment != null ? any.visit(environment,introspector) : any.visitLight(arg,introspector)` ; `getEnvironment()` accessible depuis le code hôte d'une règle (`tom__strat.GetEnvironment()`, idiome à documenter).

**Étapes (TDD) :**
1. Tests Go de `Position`/`Environment` reprenant les comportements Java (navigation up/down, omega, `getPosition`).
2. `VisitEnv` sur chaque combinateur, testé sur l'AST jouet existant de `sl_test.go` ; un test par combinateur compare `Visit` et `VisitLight` quand le résultat doit coïncider.
3. Branche Go de l'expander + `buildClass` ; gosmoke `strategy_env` (règle qui lit `getEnvironment().getPosition()`).
4. Réactiver `TestPosition` dans `TestStrategy.t`, rendre `TestAlgebraic.t` fidèle (`s.visit`), porter fidèlement `test/sl/TestEnvironment.t` ; mettre à jour `Unsupported`, oracles (`-update-oracle`) et goldens.
5. `go test ./stable/library/sl/... ./tests/port/ ./tests/gosmoke/` et `mage portJava` verts ; commit(s) `tom-go J1: …`.

---

### Task 9 (T3) : inventaire des constructions utilisées par le compilateur

**Files:**
- Create: `tomgo/tests/coverage/coverage_test.go`, `tomgo/reports/coverage.md`
- Modify: `tomgo/magefile.go`

**Interfaces:**
- Consumes: la liste `cases` n'est pas importable (package `port`) : la checklist référence les cas par leur chemin, vérifiés par présence de `tests/port/<rel>` et par le statut `OK` lu dans `reports/port-dashboard.md`.
- Produces: `reports/coverage.md` — une ligne par construction : nombre d'occurrences dans `src/tom/engine` + `src/tom/platform`, et cas couvrants au statut OK.

- [ ] **Step 1 : écrire `tomgo/tests/coverage/coverage_test.go`**

```go
// Package coverage is T3: it inventories the Tom constructs used by the
// compiler's own sources and checks that each one is exercised by at
// least one Tom+Go port whose dashboard status is OK.
package coverage

import (
	"flag"
	"os"
	"path/filepath"
	"regexp"
	"sort"
	"strconv"
	"strings"
	"testing"

	"tom/tomgo/tests/tomjava"
)

var write = flag.Bool("write-coverage", false, "rewrite ../../reports/coverage.md")

type construct struct {
	Name string
	Re   *regexp.Regexp
}

var constructs = []construct{
	{"%match", regexp.MustCompile(`%match`)},
	{"%strategy", regexp.MustCompile(`%strategy`)},
	{"%gom", regexp.MustCompile(`%gom`)},
	{"%include sl.tom", regexp.MustCompile(`%include\s*\{[^}]*sl\.tom`)},
	{"%include Collection/List/Map mappings", regexp.MustCompile(`%include\s*\{[^}]*(Collection|List|Map|types/[A-Za-z]+)\.tom`)},
	{"%op/%oplist/%typeterm declarations", regexp.MustCompile(`%(op|oplist|oparray|typeterm)\s`)},
	{"variadic patterns (X*)", regexp.MustCompile(`\b[A-Za-z_]\w*\*`)},
	{"anti-patterns (!p)", regexp.MustCompile(`[(,]\s*!\s*[A-Za-z_]`)},
	{"aliases (x@p)", regexp.MustCompile(`\b[A-Za-z_]\w*@[A-Za-z_(]`)},
	{"constraints (<< && ||)", regexp.MustCompile(`<<|&&\s*[A-Za-z_]+\s*<<`)},
	{"backquote terms", regexp.MustCompile("`[A-Za-z_(]")},
	{"visit rules", regexp.MustCompile(`\bvisit\s+[A-Za-z_]\w*\s*\{`)},
	{"labelled rules (l:p -> a)", regexp.MustCompile(`\b[a-z]\w*\s*:\s*[A-Za-z_]\w*\(.*\)\s*->`)},
}

func count(t *testing.T, dirs []string, re *regexp.Regexp) int {
	n := 0
	for _, d := range dirs {
		filepath.Walk(d, func(p string, info os.FileInfo, err error) error {
			if err == nil && !info.IsDir() && strings.HasSuffix(p, ".t") {
				data, _ := os.ReadFile(p)
				n += len(re.FindAllIndex(data, -1))
			}
			return err
		})
	}
	return n
}

func TestCoverage(t *testing.T) {
	root := tomjava.RepoRoot(t)
	compiler := []string{filepath.Join(root, "src", "tom", "engine"), filepath.Join(root, "src", "tom", "platform")}
	dashboard, err := os.ReadFile(filepath.Join(root, "tomgo", "reports", "port-dashboard.md"))
	if err != nil {
		t.Skip("no port dashboard yet (run mage portGo)")
	}
	okCases := map[string]bool{}
	for _, line := range strings.Split(string(dashboard), "\n") {
		if strings.HasSuffix(line, "| OK |") {
			okCases[strings.Trim(strings.SplitN(line, "|", 3)[1], " `")] = true
		}
	}
	var ok []string
	for c := range okCases {
		ok = append(ok, c)
	}
	sort.Strings(ok)
	var b strings.Builder
	b.WriteString("# Couverture des constructions du compilateur (T3)\n\nGénéré par `mage coverage`.\n\n| Construction | Occurrences dans le compilateur | Cas OK qui l'exercent | Couvert |\n|---|---|---|---|\n")
	missing := 0
	for _, c := range constructs {
		uses := count(t, compiler, c.Re)
		var by []string
		for _, rel := range ok {
			data, _ := os.ReadFile(filepath.Join(root, "tomgo", "tests", "port", rel))
			if c.Re.Match(data) {
				by = append(by, "`"+rel+"`")
			}
		}
		covered := uses == 0 || len(by) > 0
		if !covered {
			missing++
		}
		mark := "☐"
		if covered {
			mark = "☑"
		}
		b.WriteString("| " + c.Name + " | " + strconv.Itoa(uses) + " | " + strings.Join(by, ", ") + " | " + mark + " |\n")
	}
	b.WriteString("\nConstructions non couvertes : " + strconv.Itoa(missing) + "\n")
	if *write {
		if err := os.WriteFile(filepath.Join(root, "tomgo", "reports", "coverage.md"), []byte(b.String()), 0o644); err != nil {
			t.Fatal(err)
		}
	}
	t.Logf("%d construct(s) not yet covered", missing)
}
```

(Le test ne fait pas échouer la CI tant que des constructions manquent :
la checklist est l'indicateur de sortie de J3, pas une barrière.)

- [ ] **Step 2 : cible mage et premier rapport**

```go
// Coverage runs T3 and rewrites reports/coverage.md.
func Coverage() error {
	return sh.RunV("go", "test", "-count=1", "-v", "./tests/coverage/...", "-args", "-write-coverage")
}
```

Run: `cd tomgo && mage coverage && cat reports/coverage.md`
Expected: tableau rempli ; `%match`, backquote, `%gom` ☑ ; `%strategy` ☑
(TestAlgebraic) ; `Collection/List/Map mappings` et probablement
`labelled rules` ☐. Vérifier à la main 2-3 compteurs (`grep -c '%strategy' -r src/tom/engine | …`)
pour valider les expressions régulières ; les ajuster si un compteur est
manifestement faux.

- [ ] **Step 3 : commit**

```bash
git add tomgo/tests/coverage tomgo/magefile.go tomgo/reports/coverage.md
git commit -m "tom-go T3: inventory compiler constructs and their Tom+Go coverage

Co-Authored-By: Claude Opus 5.5 <noreply@anthropic.com>"
```

---

### Task 10 (J0 fin) : promotion dans `stable/` et documentation

**Files:**
- Modify: `stable/**` (via `./build.sh bootinstall`), `stable/config/Tom.config` (inchangé sauf besoin), `CLAUDE.md`
- Create: `tomgo/reports/j0-go-backend.md`

- [ ] **Step 1 : promouvoir et reconstruire `stable`**

```bash
cd /Users/pem/github/tom && unset TOM_HOME
./build.sh bootinstall 2>&1 | tail -2     # BUILD SUCCESSFUL
./build.sh stable 2>&1 | tail -2          # BUILD SUCCESSFUL
ls stable/tom/engine/backend/GoGenerator.java stable/tom/library/mapping/go/
./build.sh src.dist 2>&1 | tail -2        # src recompilé par le nouveau stable
```

- [ ] **Step 2 : tous les harnais**

Run: `cd tomgo && mage all && mage portJava && mage portGo && mage coverage`
Expected: tout vert ; dashboard `OK : 11`.

- [ ] **Step 3 : rapport `tomgo/reports/j0-go-backend.md`**

Contenu (rempli avec les valeurs réelles observées) :
1. commandes exécutées et leur résultat (Tasks 1-10) ;
2. fichiers modifiés dans `src/` et lignes ajoutées (`git diff --stat 2918e95c7 -- src`) ;
3. métriques : T0 (n fichiers identiques / 103 + 18 erreurs), T1/T2 (11 OK / 103), T3 (constructions couvertes / total) ;
4. limites connues : `-gi` et `%transformation` refusés avec `-go` ; paramètres `Strategy` des `%strategy` non exposés comme enfants ; `ConditionalExpression` typée `any` ; Go généré sur une ligne (lire avec `gofmt`) ;
5. suite : J2 (porter `test/` par lots, tableau de bord), J3 (constructions ☐ de `coverage.md`).

- [ ] **Step 4 : mettre à jour `CLAUDE.md`**

Ajouter en tête de §1 un paragraphe « Trajectoire actuelle (depuis 2026-10-22) » :
la cible Go est ajoutée au compilateur Tom+Java (`-go`), `legacy/` est le
témoin humain, le compilateur Go écrit à la main (`tomgo/stable/tom/`) est
gelé ; renvoyer vers le spec, ce plan et `tomgo/reports/j0-go-backend.md`.
Ajouter à §8 (Cookbook) :

```bash
# Construire la version humaine (oracle) et la version étendue
(cd legacy && unset TOM_HOME && ./build.sh stable && ./build.sh src.dist)
unset TOM_HOME && ./build.sh src.dist
# Compiler un .t vers Go
TOM_HOME=$PWD/src/dist src/dist/bin/tom -go -np -d out X.t
# Harnais
cd tomgo && mage portJava && mage portGo && mage coverage
```

- [ ] **Step 5 : commit**

```bash
git add stable CLAUDE.md tomgo/reports/j0-go-backend.md
git commit -m "tom-go J0: promote the Go backend into stable/ and document it

Co-Authored-By: Claude Opus 5.5 <noreply@anthropic.com>"
```

---

## Après ce plan

- **J2** : porter le reste de `test/` par lots (un lot = un sous-répertoire :
  `sl/`, `gom/`, `antipatterns/`, …). Pour chaque cas : copier l'original
  dans `tomgo/tests/port/<rel>`, traduire le code hôte, ajouter à `cases`,
  `-update -update-oracle`, corriger `GoGenerator.t` si besoin, commit par lot.
- **J3** : pour chaque ☐ de `reports/coverage.md`, porter un cas de `test/`
  ou d'`examples/` qui l'exerce.
- **J4** : spec séparé (bootstrap), à rédiger quand la checklist T3 est complète.
