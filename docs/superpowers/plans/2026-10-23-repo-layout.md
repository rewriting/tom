# Réorganisation du dépôt (src/go, test/go, prototype/tomgo) — plan d'implémentation

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** ranger le code Go validé dans `src/go/tom` (module `tom`) et `test/go` (module `tomtest`), geler le prototype dans `prototype/tomgo`, sans changer aucun comportement.

**Architecture:** arbre Go jumeau de l'arbre Java (`src/tom/X` ↔ `src/go/tom/X`, `test/X` ↔ `test/go/X`). Module `tom` neutre vis-à-vis de l'emplacement : la copie utilisée se choisit par `replace` (la dist embarque le runtime dans `<dist>/lib/go/tom`). Prototype autonome avec ses propres copies.

**Tech Stack:** Go 1.26 (modules + `go.work`), mage, Ant (`./build.sh`, JDK 8), Tom+Java.

Spec : `docs/superpowers/specs/2026-10-23-repo-layout-design.md`.

## Global Constraints

- Aucun changement de comportement : mêmes résultats T0, T2 (13 OK / 0 divergence / 90 non porté), T3 (13/17), smoke (8 cas + options), tests `sl`/Gom Go, auto-amorce de `tom/gom/adt` octet-identique.
- Goldens : seules les lignes d'import peuvent changer ; oracles inchangés.
- `legacy/` et `stable/` (Java) ne sont pas modifiés.
- Module `tom` enraciné dans `src/go/tom/` ; module `tomtest` dans `test/go/` avec `replace tom => ../../src/go/tom` ; chemins d'import sans `src/`, `stable/`, ni `/go` final.
- Correspondance des chemins (exacte) :
  `tom/tomgo/stable/library/sl` → `tom/library/sl` ;
  `tom/tomgo/stable/library/sharedobjects` → `tom/library/sharedobjects` ;
  `tom/tomgo/stable/gom/parser` → `tom/gom/parser` ;
  `tom/tomgo/stable/gom/backend` → `tom/gom/backend` ;
  `tom/tomgo/stable/library/gomast` → `tom/gom/adt` (package `gomast` → `adt`) ;
  `tom/tomgo/cmd/tomgo` → `tom/gom/cmd/gomgo` (binaire `gomgo`, variable `GOMGO_BIN`) ;
  `tom/tomgo/tests/tomjava` → `tomtest/harness` (package `harness`) ;
  `tomgo/tests/port/*` → `test/go/*` (package `port` conservé) ; `tomgo/tests/{javaparity,gosmoke,coverage}` → `test/go/{javaparity,smoke,coverage}` ;
  `tomgo/reports/{j0-go-backend,port-dashboard,coverage}.md` → `doc/go/`.
- `unset TOM_HOME` (ou le fixer explicitement) avant tout `./build.sh`/`bin/tom` ; JDK 8 pour Ant.
- Jamais `git add -A` ; ne jamais ajouter : `.DS_Store`, `build.xml.bak*`, `tomgo/.claude/`, `tomgo/reports/phase5-overnight-parity.md`. Un refus de permission se signale, il ne se contourne pas.
- Commits `tom-go layout: <verbe> <objet>`, terminés par `Co-Authored-By: Claude Opus 5.5 <noreply@anthropic.com>`. Chaque commit laisse les tests verts.

## File Structure

| Chemin | Rôle |
|---|---|
| `src/go/tom/go.mod` | module `tom` |
| `src/go/tom/library/{sl,sharedobjects}/` | runtime Go |
| `src/go/tom/gom/{parser,backend,adt}/`, `src/go/tom/gom/cmd/gomgo/` | Gom écrit en Go + CLI |
| `test/go/go.mod` | module `tomtest` |
| `test/go/*.t`, `test/go/<sous-rép>/*.t`, `test/go/port_test.go`, `test/go/testdata/`, `test/go/aterm/` | ports Tom+Go et T1/T2 |
| `test/go/{harness,javaparity,smoke,coverage}/` | harnais, T0, smoke, T3 |
| `go.work`, `magefiles/` | espace de travail et orchestration à la racine |
| `doc/go/` | rapports validés + README des emplacements |
| `prototype/tomgo/` | prototype gelé (module `tom/tomgo`) |

---

### Task 1 : mesures de référence (aucun commit)

**Files:** aucun (sortie dans le scratchpad).

**Interfaces:**
- Produces: `/private/tmp/claude-501/-Users-pem-github-tom/5a145986-8813-426a-94cd-8d4fe6344d29/scratchpad/layout-baseline.md` : résultats de référence comparés en Task 6.

- [ ] **Step 1 : vérifier que les dists sont à jour**

```bash
cd /Users/pem/github/tom && unset TOM_HOME
ls legacy/src/dist/bin/tom src/dist/bin/tom
./build.sh src.dist 2>&1 | tail -1      # BUILD SUCCESSFUL
```

- [ ] **Step 2 : lancer toutes les suites et consigner les résultats**

```bash
cd /Users/pem/github/tom/tomgo
B=/private/tmp/claude-501/-Users-pem-github-tom/5a145986-8813-426a-94cd-8d4fe6344d29/scratchpad/layout-baseline.md
{ echo "# baseline $(git rev-parse --short HEAD)"
  go test -count=1 ./... 2>&1 | grep -E '^(ok|FAIL|---|\?)' 
  go test -count=1 -v -run 'TestJavaOutputIdentical' -timeout 30m ./tests/javaparity/ 2>&1 | grep -E 'inputs|files compared|^(ok|FAIL)'
  head -12 reports/port-dashboard.md
  tail -3 reports/coverage.md
} > $B 2>&1; cat $B
```
Expected : tous `ok` ; tableau de bord « OK : 13 », couverture 13/17.

---

### Task 2 : module `tom` dans `src/go/tom` (runtime + Gom Go)

**Files:**
- Create: `src/go/tom/go.mod`, `src/go/tom/library/sl/*`, `src/go/tom/library/sharedobjects/*`, `src/go/tom/gom/parser/*`, `src/go/tom/gom/backend/*`, `src/go/tom/gom/adt/*`, `src/go/tom/gom/cmd/gomgo/*`, `src/go/tom/gom/backend/engine_adt_test.go`
- Not modified: `tomgo/**` (reste fonctionnel pendant la transition)

**Interfaces:**
- Produces: packages `tom/library/sl`, `tom/library/sharedobjects`, `tom/gom/parser`, `tom/gom/backend`, `tom/gom/adt` (package `adt`), commande `tom/gom/cmd/gomgo` (sous-commandes `gom`, `gom-batch`, `gom-block` ; pas de `scan-hooks`, pas de `--target java`).
- Le code généré par `tom/gom/backend` importe `"tom/library/sharedobjects"` et `sl "tom/library/sl"`.
- `backend.GenerateToDir` écrit un `go.mod` `require tom v0.0.0` + `replace tom => <racine du module tom>` (racine trouvée en remontant jusqu'à un `go.mod` dont la ligne `module` vaut exactement `tom`).

- [ ] **Step 1 : copier les sources (pas de `git mv` : le prototype garde les siennes)**

```bash
cd /Users/pem/github/tom
mkdir -p src/go/tom/library src/go/tom/gom/cmd
cp -R tomgo/stable/library/sl            src/go/tom/library/sl
cp -R tomgo/stable/library/sharedobjects src/go/tom/library/sharedobjects
cp -R tomgo/stable/gom/parser            src/go/tom/gom/parser
cp -R tomgo/stable/gom/backend           src/go/tom/gom/backend
cp -R tomgo/stable/library/gomast        src/go/tom/gom/adt
cp -R tomgo/cmd/tomgo                    src/go/tom/gom/cmd/gomgo
# hors périmètre validé (dépendent de tomast ou de la cible Java)
rm src/go/tom/library/sl/tomast_smoke_test.go
rm src/go/tom/gom/backend/bundle.go src/go/tom/gom/backend/selfbootstrap_tomast_test.go src/go/tom/gom/backend/tomast_hooks_test.go
cat > src/go/tom/go.mod <<'EOF'
module tom

go 1.26
EOF
```

- [ ] **Step 2 : réécrire les imports et le nom du package `gomast`**

```bash
cd /Users/pem/github/tom/src/go/tom
grep -rl 'tom/tomgo' . | xargs sed -i '' \
  -e 's#"tom/tomgo/stable/library/sharedobjects"#"tom/library/sharedobjects"#g' \
  -e 's#"tom/tomgo/stable/library/sl"#"tom/library/sl"#g' \
  -e 's#"tom/tomgo/stable/gom/parser"#"tom/gom/parser"#g' \
  -e 's#"tom/tomgo/stable/gom/backend"#"tom/gom/backend"#g' \
  -e 's#"tom/tomgo/stable/library/gomast"#gomast "tom/gom/adt"#g'
sed -i '' 's/^package gomast$/package adt/' gom/adt/*.go
grep -rn 'tom/tomgo\|tomgo' . | grep -v '_test.go:.*//' | head -40
```
L'alias `gomast "tom/gom/adt"` garde les références `gomast.X` du code
existant inchangées (ce sera le seul alias de ce type). Traiter chaque
occurrence restante de `tomgo` listée par le dernier `grep` :
- `gom/backend/backend.go` : la chaîne émise `"tom/tomgo/stable/library/sharedobjects"` → `"tom/library/sharedobjects"`, `sl "tom/tomgo/stable/library/sl"` → `sl "tom/library/sl"` ; `locateTomgoRoot` → `locateTomModuleRoot` (cherche un `go.mod` dont une ligne vaut exactement `module tom`) et le `go.mod` généré par `GenerateToDir` : `require tom v0.0.0` / `replace tom => <racine>` ; supprimer toute référence à `RunBundledJavaGom`.
- `gom/cmd/gomgo/main.go` : retirer `scan-hooks` et l'option `--target` (le cas `java` et `runGomJava`) ; texte d'usage : nom de commande `gomgo`, description « Gom written in Go (used by `tom -go`) ».
- `gom/adt/doc.go` (s'il existe) et les commentaires qui parlent de `tomgo` : mettre à jour les chemins.
- Tests du backend qui lisent `tomgo/src/gom/adt` ou `tomgo/stable/library/gomast` (`selfbootstrap_test.go`, `backend_adt_test.go`) : ils lisent désormais `src/tom/gom/adt/*.gom` et `src/go/tom/gom/adt/` ; le package attendu dans la régénération est `adt`.

- [ ] **Step 3 : remplacer les tests dépendant de `tomast` par un test qui n'a pas besoin du package committé**

`src/go/tom/gom/backend/engine_adt_test.go` :

```go
package backend_test

import (
	"os"
	"os/exec"
	"path/filepath"
	"testing"

	gomast "tom/gom/adt"
	"tom/gom/backend"
	gomparser "tom/gom/parser"
)

// TestEngineADTCompiles generates the Tom engine ADT (src/tom/engine/adt/*.gom,
// with its hooks) into a temporary module and builds it. It replaces the
// prototype's committed-tomast tests: the validated tree does not ship tomast,
// but the backend must keep handling the engine ADT for the future Tom+Go
// compiler.
func TestEngineADTCompiles(t *testing.T) {
	if _, err := exec.LookPath("go"); err != nil {
		t.Skip("go not in PATH")
	}
	root := repoRoot(t)
	files, err := filepath.Glob(filepath.Join(root, "src", "tom", "engine", "adt", "*.gom"))
	if err != nil || len(files) == 0 {
		t.Fatalf("no engine .gom files: %v", err)
	}
	var mods []gomast.GomModule
	for _, f := range files {
		m, err := gomparser.ParseFile(f)
		if err != nil {
			t.Fatalf("%s: %v", f, err)
		}
		mods = append(mods, m)
	}
	out := t.TempDir()
	if _, err := backend.GenerateBatchToDir(mods, backend.Options{PackageName: "tomast"}, out); err != nil {
		t.Fatal(err)
	}
	cmd := exec.Command("go", "build", "./...")
	cmd.Dir = out
	if b, err := cmd.CombinedOutput(); err != nil {
		t.Fatalf("generated engine ADT does not build: %v\n%s", err, b)
	}
}

func repoRoot(t *testing.T) string {
	t.Helper()
	dir, _ := os.Getwd()
	for {
		if _, err := os.Stat(filepath.Join(dir, "build.xml")); err == nil {
			return dir
		}
		parent := filepath.Dir(dir)
		if parent == dir {
			t.Fatal("repository root not found")
		}
		dir = parent
	}
}
```

`GenerateBatchToDir(modules []gomast.GomModule, opts Options, dir string) (string, error)`
est la signature actuelle (vérifier dans `backend.go`). Si le
`go.mod` écrit par `GenerateBatchToDir` pointe vers la racine du module
`tom` (Step 2), le build trouve `tom/library/sharedobjects` et `sl`.

- [ ] **Step 4 : tests verts, auto-amorce octet-identique**

```bash
cd /Users/pem/github/tom/src/go/tom
gofmt -l . ; go vet ./... && go test -count=1 ./...
```
Expected : `gofmt -l` vide, tous `ok` (dont `TestSelfBootstrap` : la
régénération de `gom/adt` depuis `src/tom/gom/adt/*.gom` est
octet-identique au contenu copié, au nom de package près déjà appliqué).
Si `TestSelfBootstrap` diffère uniquement par `package gomast`/imports :
c'est que le backend émet encore l'ancien nom — corriger le backend (option
`PackageName: "adt"` passée par le test), pas les fichiers.

- [ ] **Step 5 : le prototype n'est pas touché**

Run: `cd /Users/pem/github/tom/tomgo && go test -count=1 ./stable/... ./cmd/...` → `ok` (inchangé).

- [ ] **Step 6 : commit**

```bash
cd /Users/pem/github/tom
git add src/go
git commit -m "tom-go layout: add module tom (src/go/tom) with the validated Go runtime and Gom

Copied from tomgo/: stable/library/sl -> library/sl, stable/library/sharedobjects ->
library/sharedobjects, stable/gom/{parser,backend} -> gom/{parser,backend},
stable/library/gomast -> gom/adt, cmd/tomgo -> gom/cmd/gomgo.

Co-Authored-By: Claude Opus 5.5 <noreply@anthropic.com>"
```

---

### Task 3 : `test/go` (module `tomtest`), `gomgo` dans la chaîne Java, runtime dans la dist

**Files:**
- Move (`git mv`): `tomgo/tests/port/*` → `test/go/`, `tomgo/tests/tomjava` → `test/go/harness`, `tomgo/tests/javaparity` → `test/go/javaparity`, `tomgo/tests/gosmoke` → `test/go/smoke`, `tomgo/tests/coverage` → `test/go/coverage`, `tomgo/reports/{j0-go-backend,port-dashboard,coverage}.md` → `doc/go/`
- Create: `test/go/go.mod`
- Modify: `src/tom/engine/parser/TomParserTool.t` (l. ~277-300), `build.xml` (`src.dist` et `stable.dist`), `test/go/**` (imports, chemins), `tomgo/magefile.go` (cibles PortJava/PortGo/Coverage pointent vers `../test/go`, provisoire jusqu'à Task 4)

**Interfaces:**
- Consumes: module `tom` (Task 2), commande `tom/gom/cmd/gomgo`.
- Produces: package `tomtest/harness` : `RepoRoot`, `LegacyDist`, `NewDist`, `Dist.Compile` (pose `TOM_HOME` et `GOMGO_BIN`), `GomgoBin(t) string` (remplace `TomgoBin`, construit `tom/gom/cmd/gomgo` depuis `src/go/tom`), `SetupGoModule(t, dir)` (écrit `require tom v0.0.0` + `replace tom => <NewDist.Home>/lib/go/tom`), `RunIn`, `Normalize`.
- Le tableau de bord s'écrit dans `doc/go/port-dashboard.md`, la couverture dans `doc/go/coverage.md`.

- [ ] **Step 1 : déplacer les tests et les rapports**

```bash
cd /Users/pem/github/tom
mkdir -p test/go doc/go
for f in tomgo/tests/port/*; do git mv "$f" test/go/; done
git mv tomgo/tests/tomjava    test/go/harness
git mv tomgo/tests/javaparity test/go/javaparity
git mv tomgo/tests/gosmoke    test/go/smoke
git mv tomgo/tests/coverage   test/go/coverage
git mv tomgo/reports/j0-go-backend.md tomgo/reports/port-dashboard.md tomgo/reports/coverage.md doc/go/
cat > test/go/go.mod <<'EOF'
module tomtest

go 1.26

require tom v0.0.0

replace tom => ../../src/go/tom
EOF
```

- [ ] **Step 2 : réécrire imports et chemins dans `test/go`**

```bash
cd /Users/pem/github/tom/test/go
grep -rl 'tom/tomgo' . | xargs sed -i '' \
  -e 's#"tom/tomgo/tests/tomjava"#"tomtest/harness"#g' \
  -e 's#"tom/tomgo/stable/library/sl"#"tom/library/sl"#g' \
  -e 's#"tom/tomgo/stable/library/sharedobjects"#"tom/library/sharedobjects"#g'
sed -i '' 's/^package tomjava$/package harness/' harness/*.go
grep -rl 'tomjava\.' . | xargs sed -i '' 's/tomjava\./harness./g'
grep -rn 'tomgo' . | head -40
```

Traiter chaque occurrence restante :
- `harness/harness.go` :
  - `RepoRoot` : racine = répertoire qui contient `build.xml` **et** `test/go/go.mod` ;
  - `TomgoBin` → `GomgoBin` : construit `tom/gom/cmd/gomgo` (`go build -o <tmp>/gomgo ./gom/cmd/gomgo` exécuté dans `src/go/tom`) ;
  - `Compile` pose `GOMGO_BIN=<GomgoBin>` (plus `TOMGO_BIN`) ;
  - `SetupGoModule` écrit :
    ```go
    mod := "module fixture\n\ngo 1.22\n\nrequire tom v0.0.0\n\nreplace tom => " +
    	filepath.Join(NewDist(t).Home, "lib", "go", "tom") + "\n"
    ```
    et échoue (`t.Fatalf`) si `<dist>/lib/go/tom/go.mod` n'existe pas, avec un message « rebuild src.dist ».
- `port_test.go` : dashboard → `filepath.Join(root, "doc", "go", "port-dashboard.md")` ; chemins des ports relatifs au package (inchangés : `test/go` est le répertoire du package) ; `TestCasesCoverPorts` : exclut `aterm/`, `testdata/`, `harness/`, `javaparity/`, `smoke/`, `coverage/`.
- `coverage/coverage_test.go` : dashboard `doc/go/port-dashboard.md`, ports `test/go/<rel>`, sortie `doc/go/coverage.md`.
- `smoke/gosmoke_test.go` : le test qui détecte un import `"tom/tomgo/` détecte désormais `"tom/` (programme qui importe le runtime) pour appeler `SetupGoModule`.
- Les `.t` (ports et smoke) qui importent le runtime : `sl "tom/library/sl"`.
- `doc/go/j0-go-backend.md` : remplacer les chemins `tomgo/tests/...`, `tomgo/stable/library/...`, `tomgo/reports/...` par les nouveaux (table « Correspondance » des Global Constraints) ; ajouter une ligne en tête : « Chemins mis à jour par la réorganisation du 2026-10-23 (voir docs/superpowers/specs/2026-10-23-repo-layout-design.md). »

- [ ] **Step 3 : `TomParserTool.t` lance `gomgo`**

Dans `parseGomFileGo`, remplacer :

```java
    String tomgo = System.getenv("TOMGO_BIN");
```
par
```java
    String tomgo = System.getenv("GOMGO_BIN");
```
```java
      tomgo = "tomgo";
```
par
```java
      tomgo = "gomgo";
```
et dans le message d'erreur `(set TOMGO_BIN or put tomgo in the PATH)` par
`(set GOMGO_BIN or put gomgo in the PATH)`. Renommer la variable locale
`tomgo` en `gomgo` dans la méthode.

- [ ] **Step 4 : `build.xml` embarque le runtime Go dans les dists**

Ajouter, juste après le bloc `<!-- tom share -->` de la cible `src.dist`
(lignes ~918-930) :

```xml
    <!-- Go runtime and Gom (module tom), used by programs generated with -go -->
    <copy todir="${src.dist}/lib/go/tom">
      <fileset dir="${basedir}/src/go/tom">
        <include name="**/*.go"/>
        <include name="go.mod"/>
        <exclude name="**/*_test.go"/>
        <exclude name="**/testdata/**"/>
      </fileset>
    </copy>
```

et la même copie dans `stable.dist` après son bloc `<!-- tom share -->`
(lignes ~648-658), avec `todir="${stable.dist}/lib/go/tom"` et la même
source `${basedir}/src/go/tom` (le runtime Go n'est pas bootstrappé : il
vient de `src/go`).

- [ ] **Step 5 : magefile provisoire**

Dans `tomgo/magefile.go`, `PortJava`, `PortGo`, `Coverage` exécutent leurs
`go test` avec `sh.RunV` dans `../test/go` (utiliser
`sh.RunWithV(nil, ...)` impossible pour le cwd : écrire un petit helper
`runIn(dir string, args ...string) error` basé sur `exec.Command` avec
`Dir`). Ces cibles migrent vers `magefiles/` en Task 4.

- [ ] **Step 6 : reconstruire et tout tester**

```bash
cd /Users/pem/github/tom && unset TOM_HOME && ./build.sh src.dist 2>&1 | tail -1
ls src/dist/lib/go/tom/go.mod src/dist/lib/go/tom/library/sl/strategy.go
cd test/go && gofmt -l . ; go vet ./... && go test -count=1 ./aterm/... ./smoke/ && \
  go test -count=1 ./ -args -update      # régénère les goldens
git diff --stat -- testdata/golden ; git diff -U0 -- testdata/golden | grep '^[-+]' | grep -v '^[-+][-+]' | grep -v 'import\|tom/library\|tom/tomgo' | head
```
Expected : `BUILD SUCCESSFUL` ; le dernier `grep` n'affiche **rien**
(goldens : seules les lignes d'import changent). Puis :

```bash
cd /Users/pem/github/tom/test/go
go test -count=1 ./ && go test -count=1 -timeout 30m ./javaparity/ && go test -count=1 ./coverage/ -args -write-coverage
cd /Users/pem/github/tom/tomgo && mage portGo
git -C /Users/pem/github/tom diff --stat -- test/go/testdata/oracle doc/go/port-dashboard.md doc/go/coverage.md
```
Expected : tous `ok` ; oracles inchangés (aucune ligne dans le stat) ;
dashboard identique (OK : 13) ; couverture identique (13/17) — seuls des
chemins dans le texte des rapports peuvent changer.

- [ ] **Step 7 : commit**

```bash
cd /Users/pem/github/tom
git add test/go doc/go src/tom/engine/parser/TomParserTool.t build.xml tomgo/magefile.go
git status --short | grep -v '^??'
git commit -m "tom-go layout: move Tom+Go tests to test/go (module tomtest), ship the Go runtime in dists, gomgo for %gom

Co-Authored-By: Claude Opus 5.5 <noreply@anthropic.com>"
```

---

### Task 3b : aterm dans le runtime validé (`src/go/tom/library/aterm`)

Décision de l'utilisateur (2026-10-23) : aterm est une bibliothèque de
termes (runtime, comme `aterm.jar` en Java), pas un outil de test. On
installe la version complète du prototype.

**Files:**
- Create: `src/go/tom/library/aterm/**` (copie de `tomgo/stable/library/aterm`, imports réécrits), `src/tom/library/mapping/go/aterm.tom` si les ports en ont besoin
- Modify: `test/go/Peano.t`, `test/go/Array.t` (et tout port qui importe le sous-ensemble), goldens correspondants
- Delete: `test/go/aterm/`

**Interfaces:**
- Produces: package `tom/library/aterm` (package `aterm`).

- [ ] **Step 1 :** copier `tomgo/stable/library/aterm` vers `src/go/tom/library/aterm` (cp, pas git mv : le prototype garde sa copie), réécrire ses imports `tom/tomgo/...` selon la table des Global Constraints, `go test ./library/aterm/` vert dans `src/go/tom`.
- [ ] **Step 2 :** comparer l'API utilisée par `test/go/aterm` (le sous-ensemble) avec celle de `tom/library/aterm` ; si une fonction utilisée par les ports manque, l'ajouter à `tom/library/aterm` avec un test (en reprenant la sémantique Java `aterm`, pas celle du sous-ensemble si elles diffèrent — signaler toute différence).
- [ ] **Step 3 :** faire importer `tom/library/aterm` par les ports (le code hôte seulement change), régénérer leurs goldens, vérifier T2 (Peano/Array : stdout identique à l'oracle legacy), supprimer `test/go/aterm/` et ses mentions (`TestCasesCoverPorts`, magefile `PortGo`).
- [ ] **Step 4 :** `go test` vert dans `src/go/tom` et `test/go`, `mage portGo` (13 OK) ; commit `tom-go layout: install aterm in module tom, ports use it`.

---

### Task 4 : geler le prototype, `go.work` et `magefiles/` à la racine

**Files:**
- Move: `tomgo/` → `prototype/tomgo/`
- Create: `prototype/tomgo/README.md`, `go.work`, `magefiles/go.mod`, `magefiles/magefile.go`
- Modify: en-têtes des copies gelées dans `prototype/tomgo/stable/library/{sl,sharedobjects,gomast}/*.go`, `prototype/tomgo/stable/gom/{parser,backend}/*.go`, `prototype/tomgo/cmd/tomgo/*.go` ; `prototype/tomgo/magefile.go` (retirer PortJava/PortGo/Coverage) ; `.gitignore`

**Interfaces:**
- Produces: `mage -d magefiles -l` (depuis la racine) liste `Test`, `Vet`, `PortJava`, `PortGo`, `Coverage`, `SelfBootstrap`, `RegenGomAdt`, `All`.

- [ ] **Step 1 : déplacer le prototype**

```bash
cd /Users/pem/github/tom
git mv tomgo prototype/tomgo
```
(Les fichiers non suivis — `tomgo/.claude/`, binaires — sont déplacés à la
main s'ils gênent : `mv tomgo/* prototype/tomgo/` n'est PAS nécessaire ;
vérifier avec `ls tomgo` ce qui reste et le signaler dans le rapport, sans
le supprimer.)

- [ ] **Step 2 : `go.work` à la racine**

```bash
cat > go.work <<'EOF'
go 1.26

use (
	./src/go/tom
	./test/go
	./magefiles
	./prototype/tomgo
)
EOF
go -C src/go/tom build ./... && go -C test/go vet ./... && go -C prototype/tomgo build ./...
```
Si `go` signale une ambiguïté entre les modules `tom` et `tom/tomgo` (le
second a pour préfixe le premier), retirer `./prototype/tomgo` de `go.work`
et documenter dans `prototype/tomgo/README.md` : `GOWORK=off go test ./...`.

- [ ] **Step 3 : `magefiles/`**

`magefiles/go.mod` :
```
module tommage

go 1.26

require github.com/magefile/mage v1.17.2
```
`magefiles/magefile.go` : reprendre de `prototype/tomgo/magefile.go` les
cibles `PortJava`, `PortGo`, `Coverage` (avec le helper `runIn`) en les
faisant pointer vers `test/go` (chemin calculé depuis la racine du dépôt :
le répertoire courant de mage est la racine quand on lance
`mage -d magefiles -w .`), et ajouter :

```go
// Test runs the unit tests of the validated Go tree (module tom).
func Test() error { return runIn("src/go/tom", "go", "test", "-count=1", "./...") }

// Vet vets the validated Go tree and the Tom+Go test harnesses.
func Vet() error {
	if err := runIn("src/go/tom", "go", "vet", "./..."); err != nil {
		return err
	}
	return runIn("test/go", "go", "vet", "./...")
}

// SelfBootstrap checks that tom/gom/adt regenerates byte-identically.
func SelfBootstrap() error {
	return runIn("src/go/tom", "go", "test", "-count=1", "-run", "TestSelfBootstrap", "./gom/backend/")
}

// RegenGomAdt regenerates src/go/tom/gom/adt from src/tom/gom/adt/*.gom.
func RegenGomAdt() error {
	files, err := filepath.Glob("src/tom/gom/adt/*.gom")
	if err != nil || len(files) == 0 {
		return fmt.Errorf("no .gom in src/tom/gom/adt: %v", err)
	}
	args := append([]string{"run", "./gom/cmd/gomgo", "gom-batch", "--pkg", "adt", "-o", "gom/adt"}, rel("src/go/tom", files)...)
	return runIn("src/go/tom", "go", args...)
}

// All runs vet, the unit tests and the self-bootstrap check.
func All() { mg.SerialDeps(Vet, Test, SelfBootstrap) }
```
avec `rel(base string, files []string) []string` qui rend les chemins
relatifs à `base` (`filepath.Rel`), et `runIn(dir, name string, args ...string) error`
(`exec.Command`, `Dir = dir`, stdout/stderr hérités). Vérifier que
`RegenGomAdt` puis `git diff --stat src/go/tom/gom/adt` ne montre aucun
changement.

- [ ] **Step 4 : marquer le prototype comme gelé**

En tête de chaque `.go` (non généré) de `prototype/tomgo/stable/library/sl`,
`prototype/tomgo/stable/library/sharedobjects`, `prototype/tomgo/stable/gom/parser`,
`prototype/tomgo/stable/gom/backend`, `prototype/tomgo/cmd/tomgo`, et du
`doc.go` de `prototype/tomgo/stable/library/gomast`, insérer avant la
ligne `package` :

```go
// Frozen prototype copy — the maintained version is src/go/tom/<chemin> (module tom).
```
avec `<chemin>` = `library/sl`, `library/sharedobjects`, `gom/parser`,
`gom/backend`, `gom/cmd/gomgo`, `gom/adt`. Ne pas modifier les fichiers
générés autrement que `doc.go` (l'auto-amorce du prototype doit rester
octet-identique).

`prototype/tomgo/README.md` (remplace l'ancien `tomgo/README.md`, dont le
contenu est conservé sous un titre « Historique ») :

```markdown
# tomgo — prototype gelé

Ce répertoire est le prototype de compilateur Tom écrit à la main en Go
(phases 0 à 6). Il est **gelé** : il compile et ses tests passent, mais il
n'évolue plus. Il sert de réservoir de code : on y copie ce dont on a
besoin, que l'on intègre ensuite dans l'arbre validé.

La version maintenue de ce qui a été validé est ailleurs :

| Ici (copie gelée) | Version maintenue |
|---|---|
| `stable/library/sl` | `src/go/tom/library/sl` |
| `stable/library/sharedobjects` | `src/go/tom/library/sharedobjects` |
| `stable/gom/{parser,backend}` | `src/go/tom/gom/{parser,backend}` |
| `stable/library/gomast` | `src/go/tom/gom/adt` |
| `cmd/tomgo` (gom, gom-batch, gom-block) | `src/go/tom/gom/cmd/gomgo` |
| `tests/{port,tomjava,javaparity,gosmoke,coverage}` | `test/go/…` (déplacés) |

Lancer les tests du prototype : `cd prototype/tomgo && go test ./...`.
```

Retirer `PortJava`, `PortGo`, `Coverage` de `prototype/tomgo/magefile.go`.

- [ ] **Step 5 : `.gitignore`**

Remplacer les entrées `tomgo/tomgo`, `tomgo/gom`, `tomgo/tom` par
`prototype/tomgo/tomgo`, `prototype/tomgo/gom`, `prototype/tomgo/tom`, et
ajouter `src/go/tom/gom/cmd/gomgo/gomgo`.

- [ ] **Step 6 : tout tester**

```bash
cd /Users/pem/github/tom
mage -d magefiles -w . -l
mage -d magefiles -w . all
mage -d magefiles -w . portGo && mage -d magefiles -w . coverage
(cd prototype/tomgo && go test -count=1 ./...)
```
Expected : tous verts ; prototype vert (y compris son auto-amorce).

- [ ] **Step 7 : commit**

```bash
git add go.work magefiles prototype/tomgo .gitignore
git status --short | grep -v '^??'
git commit -m "tom-go layout: freeze the tomgo prototype in prototype/tomgo, add go.work and root magefiles

Co-Authored-By: Claude Opus 5.5 <noreply@anthropic.com>"
```

---

### Task 5 : documentation (CLAUDE.md, doc/go, notes dans les anciens spec/plan)

**Files:**
- Create: `doc/go/README.md`
- Modify: `CLAUDE.md`, `docs/superpowers/specs/2026-10-22-tom-go-backend-design.md`, `docs/superpowers/plans/2026-10-22-tom-go-backend.md`, `legacy/README.md` n'est PAS modifié.

- [ ] **Step 1 : `doc/go/README.md`**

```markdown
# Cible Go de Tom — où sont les choses

| Quoi | Où | Statut |
|---|---|---|
| Compilateur Tom+Java d'origine (humain) | `legacy/` | témoin, gelé |
| Compilateur Tom+Java étendu (`-go`) | `src/tom/`, promu dans `stable/` | validé |
| Runtime Go `sl`, `sharedobjects` | `src/go/tom/library/` (module `tom`) | validé |
| Gom écrit en Go (`gomgo`) | `src/go/tom/gom/` | validé |
| Mappings Go | `src/tom/library/mapping/go/` | validé |
| Tests Tom+Go (ports de `test/`), T0–T3, smoke | `test/go/` (module `tomtest`) | validé |
| Rapports | `doc/go/` | à jour |
| Prototype tomgo (compilateur Go écrit à la main) | `prototype/tomgo/` | gelé, réservoir de code |

Utiliser le runtime dans un projet : `require tom v0.0.0` et
`replace tom => $TOM_HOME/lib/go/tom` dans `go.mod`, puis
`import sl "tom/library/sl"`.

## Orientation pour la suite

- **J4 — compilateur en Tom+Go, stratégie hybride (décidée le 2026-10-23).**
  On remplace les phases du compilateur Tom+Java une par une par leur
  version Go, dans la chaîne Java qui marche. Un pont Java ⇄ Go fait
  passer le terme de travail **et** la table des symboles (formats aterm
  déjà produits par `--intermediate` : `.tfix.*` et `.table`) ; un plugin
  « externe » de la plateforme Java lance la phase Go ; une option choisit
  Java ou Go phase par phase. Ordre : (0) pont seul avec une phase
  identité, (1) desugarer, (2) parser (reprise du parser Go du
  prototype, complété), (3) typer, expander, compilateur de motifs,
  optimizer, backend, (4) orchestration Go et point fixe. Chaque étape
  est validée par T0 (sortie Java identique à `legacy/`) et T2. Un spec
  J4 dédié sera rédigé avant de commencer.
- **Reporté :** `src/tom` → `src/java/tom` (et `stable/java`) ; à faire
  seulement quand un compilateur Tom+Go fonctionnera.
```

- [ ] **Step 2 : `CLAUDE.md`**

Réécrire §2 (cartographie) et §3 (disposition du code Go) à partir de ce
tableau ; dans le cookbook, remplacer toutes les commandes `cd tomgo && mage …`
par `mage -d magefiles -w . <cible>` et les chemins `tomgo/tests/…` /
`tomgo/stable/library/…` par les nouveaux ; `TOMGO_BIN` → `GOMGO_BIN`,
binaire `gomgo` (`go build -o /tmp/gomgo ./src/go/tom/gom/cmd/gomgo`
lancé depuis la racine : `go -C src/go/tom build -o /tmp/gomgo ./gom/cmd/gomgo`) ;
le `go.mod` utilisateur : `replace tom => $TOM_HOME/lib/go/tom`. Les
sections historiques (phases 4–6 du prototype) restent, sous le bandeau
existant, avec la mention que leurs chemins sont désormais sous
`prototype/tomgo/`.

- [ ] **Step 3 : notes dans le spec et le plan du 2026-10-22**

En tête de chacun, sous le titre :

```markdown
> **Chemins mis à jour le 2026-10-23** : voir
> `docs/superpowers/specs/2026-10-23-repo-layout-design.md` (table
> « Correspondance des chemins » du plan `2026-10-23-repo-layout.md`).
```

- [ ] **Step 4 : contrôle des références**

```bash
cd /Users/pem/github/tom
grep -rn "tomgo" src test magefiles doc/go CLAUDE.md go.work | grep -v "prototype/tomgo\|Frozen prototype" | head -40
```
Chaque ligne restante doit être une mention historique volontaire (la
lister dans le rapport) ; les autres sont corrigées.

- [ ] **Step 5 : commit**

```bash
git add doc/go/README.md CLAUDE.md docs/superpowers/specs/2026-10-22-tom-go-backend-design.md docs/superpowers/plans/2026-10-22-tom-go-backend.md
git commit -m "tom-go layout: document the new repository map (CLAUDE.md, doc/go)

Co-Authored-By: Claude Opus 5.5 <noreply@anthropic.com>"
```

---

### Task 6 : vérification finale contre la référence (aucun commit sauf correctif)

- [ ] **Step 1 : reconstruire les dists et tout relancer**

```bash
cd /Users/pem/github/tom && unset TOM_HOME
./build.sh src.dist 2>&1 | tail -1
./build.sh stable 2>&1 | tail -1 && ls stable/dist/lib/go/tom/go.mod
mage -d magefiles -w . all
mage -d magefiles -w . portJava
mage -d magefiles -w . portGo
mage -d magefiles -w . coverage
(cd prototype/tomgo && go test -count=1 ./...)
```

- [ ] **Step 2 : comparer avec `layout-baseline.md` (Task 1)**

Mêmes comptes T0 (inputs / fichiers comparés, deux modes), même tableau
de bord (13 OK / 0 divergence / 90 non porté), même couverture (13/17),
mêmes tests verts (aux noms de packages près). Toute différence est un
défaut à corriger (commit `tom-go layout: fix …`).

- [ ] **Step 3 : utilisation depuis la dist, comme un utilisateur**

```bash
S=/private/tmp/claude-501/-Users-pem-github-tom/5a145986-8813-426a-94cd-8d4fe6344d29/scratchpad/layout-user
rm -rf $S && mkdir -p $S && cp test/go/TestAlgebraic.t $S/ && cd $S
go -C /Users/pem/github/tom/src/go/tom build -o $S/gomgo ./gom/cmd/gomgo
GOMGO_BIN=$S/gomgo TOM_HOME=/Users/pem/github/tom/src/dist /Users/pem/github/tom/src/dist/bin/tom -go -d . TestAlgebraic.t
mv TestAlgebraic.go TestAlgebraic_test.go
printf 'module user\n\ngo 1.22\n\nrequire tom v0.0.0\n\nreplace tom => /Users/pem/github/tom/src/dist/lib/go/tom\n' > go.mod
GOWORK=off go mod tidy && GOWORK=off go test ./...
```
Expected : `ok` — le runtime embarqué dans la dist suffit.
