// Package equivtest drives a side-by-side comparison of the Go code
// emitted by `tom/tomgo/internal/backend` and the Java code emitted by
// the historical reference Gom compiler shipped as
// `applications/prototype3D/lib/tom-compiler-full.jar`.
//
// For a target .gom file plus a pair of hand-written scenario drivers
// (one Go, one Java) that exercise the same operations on the same
// inputs, this package:
//
//  1. generates the Go package via the backend and writes scenario.go;
//  2. invokes the reference Gom compiler to generate the Java sources
//     and copies Scenario.java alongside;
//  3. builds and runs both programs;
//  4. diffs the two stdout streams line by line.
//
// Java is detected at runtime; tests that depend on this package call
// SkipIfNoJava so they degrade cleanly on JVM-less machines.
package equivtest

import (
	"bytes"
	"fmt"
	"os"
	"os/exec"
	"path/filepath"
	"strings"

	"tom/tomgo/stable/gom/backend"
	"tom/tomgo/stable/gom/parser"
)

// JavaToolchain captures the paths needed to run the Java reference
// pipeline. Resolve returns one populated automatically by inspecting
// the host environment.
type JavaToolchain struct {
	Java       string // absolute path to `java`
	Javac      string // absolute path to `javac`
	ConfigXML  string // path to a Gom.xml plugin config
	CompilerJar string // tom-compiler-full.jar (contains tom.gom.Gom)
	RuntimeJar  string // tom-runtime-full.jar  (runtime needed by generated code)
	ExtraJars   []string // shared-objects.jar, aterm.jar, jjtraveler.jar, antlr*.jar, args4j.jar, …
}

// Classpath returns a colon-separated classpath suitable for `java -cp`.
func (j JavaToolchain) Classpath(extra ...string) string {
	parts := append([]string{j.CompilerJar, j.RuntimeJar}, j.ExtraJars...)
	parts = append(parts, extra...)
	return strings.Join(parts, string(os.PathListSeparator))
}

// Resolve locates a JDK and the reference Gom jars given the path to
// the TOM repository root. Returns an error explaining what is missing
// when something cannot be found.
func Resolve(repoRoot string) (JavaToolchain, error) {
	tc := JavaToolchain{}
	var err error
	tc.Java, tc.Javac, err = findJavaTools()
	if err != nil {
		return tc, err
	}
	must := func(rel string) (string, error) {
		p := filepath.Join(repoRoot, rel)
		if _, err := os.Stat(p); err != nil {
			return "", fmt.Errorf("missing %s under %s", rel, repoRoot)
		}
		return p, nil
	}
	if tc.CompilerJar, err = must("applications/prototype3D/lib/tom-compiler-full.jar"); err != nil {
		return tc, err
	}
	if tc.RuntimeJar, err = must("applications/prototype3D/lib/tom-runtime-full.jar"); err != nil {
		return tc, err
	}
	if tc.ConfigXML, err = must("utils/eclipse-plugin/plugin/config/Gom.xml"); err != nil {
		return tc, err
	}
	extras := []string{
		"stable/lib/runtime/shared-objects.jar",
		"stable/lib/runtime/aterm.jar",
		"stable/lib/runtime/jjtraveler.jar",
		"stable/lib/runtime/TNode.jar",
		"stable/lib/tools/args4j-2.0.10.jar",
		"stable/lib/tools/antlr-3.2.jar",
		"stable/lib/tools/antlr-2.7.7.jar",
		"stable/lib/tools/antlr-4.5.3-complete.jar",
	}
	for _, e := range extras {
		p := filepath.Join(repoRoot, e)
		if _, err := os.Stat(p); err == nil {
			tc.ExtraJars = append(tc.ExtraJars, p)
		}
	}
	return tc, nil
}

// findJavaTools returns the (java, javac) pair found via JAVA_HOME, the
// Homebrew default location, or $PATH — in that order.
func findJavaTools() (string, string, error) {
	if h := os.Getenv("JAVA_HOME"); h != "" {
		j := filepath.Join(h, "bin", "java")
		jc := filepath.Join(h, "bin", "javac")
		if _, err := os.Stat(j); err == nil {
			return j, jc, nil
		}
	}
	if _, err := os.Stat("/opt/homebrew/opt/openjdk/bin/javac"); err == nil {
		return "/opt/homebrew/opt/openjdk/bin/java", "/opt/homebrew/opt/openjdk/bin/javac", nil
	}
	j, errJ := exec.LookPath("java")
	jc, errC := exec.LookPath("javac")
	if errJ == nil && errC == nil {
		out, err := exec.Command(j, "-version").CombinedOutput()
		if err != nil || strings.Contains(string(out), "Unable to locate a Java Runtime") {
			return "", "", fmt.Errorf("`java` on PATH is a stub: %s", strings.TrimSpace(string(out)))
		}
		return j, jc, nil
	}
	return "", "", fmt.Errorf("no JDK found (set JAVA_HOME, install /opt/homebrew/opt/openjdk, or put `java`+`javac` on PATH)")
}

// CompareTarget runs the equivalence pipeline for one named target.
// workDir is a fresh, empty directory. The function returns nil on
// byte-identical stdout from both languages; otherwise it returns an
// error whose message contains a unified-style diff.
func CompareTarget(
	tc JavaToolchain,
	gomFile string,
	scenarioGo string,
	scenarioJava string,
	workDir string,
) error {
	if err := os.MkdirAll(workDir, 0o755); err != nil {
		return err
	}

	// 1) Generate Go package via tomgo backend.
	mod, err := gom.ParseFile(gomFile)
	if err != nil {
		return fmt.Errorf("parse %s: %w", gomFile, err)
	}
	goPkgDir := filepath.Join(workDir, "go")
	if _, err := backend.GenerateToDir(mod, backend.Options{}, goPkgDir); err != nil {
		return fmt.Errorf("backend.GenerateToDir: %w", err)
	}
	// The generated `tomgen/<pkg>` lives in its own module under go/.
	// Place scenario.go (package main) in a sibling module that
	// imports it via a `replace ../go`.
	mainDir := filepath.Join(workDir, "go-main")
	if err := os.MkdirAll(mainDir, 0o755); err != nil {
		return err
	}
	scnGoBytes, err := os.ReadFile(scenarioGo)
	if err != nil {
		return err
	}
	if err := os.WriteFile(filepath.Join(mainDir, "scenario.go"), scnGoBytes, 0o644); err != nil {
		return err
	}
	// Read the module name out of go/go.mod so the replace directive
	// matches whatever the backend chose for this .gom.
	pkgModule := "tomgen/" + gom.QualifiedName(mod) // fallback
	if data, err := os.ReadFile(filepath.Join(goPkgDir, "go.mod")); err == nil {
		for _, line := range strings.Split(string(data), "\n") {
			if strings.HasPrefix(line, "module ") {
				pkgModule = strings.TrimSpace(strings.TrimPrefix(line, "module "))
				break
			}
		}
	}
	mainGoMod := fmt.Sprintf(
		"module scenario\n\ngo 1.22\n\nrequire %s v0.0.0\nreplace %s => ../go\nrequire tom/tomgo v0.0.0\nreplace tom/tomgo => %s\n",
		pkgModule, pkgModule, backendTomgoRoot())
	if err := os.WriteFile(filepath.Join(mainDir, "go.mod"), []byte(mainGoMod), 0o644); err != nil {
		return err
	}
	// Run go build && run.
	goOut, err := runCapture(mainDir, "go", "run", ".")
	if err != nil {
		return fmt.Errorf("go side failed: %w\n--- stdout/stderr ---\n%s", err, goOut)
	}

	// 2) Generate Java reference via tom.gom.Gom.
	javaSrcDir := filepath.Join(workDir, "java-src")
	if err := os.MkdirAll(javaSrcDir, 0o755); err != nil {
		return err
	}
	gomOut, err := runCapture("",
		tc.Java, "-cp", tc.Classpath(),
		"tom.gom.Gom", "-X", tc.ConfigXML,
		"-d", javaSrcDir, gomFile)
	if err != nil {
		return fmt.Errorf("reference Gom Java failed: %w\n%s", err, gomOut)
	}

	// Copy Scenario.java next to the generated tree.
	scnJavaBytes, err := os.ReadFile(scenarioJava)
	if err != nil {
		return err
	}
	if err := os.WriteFile(filepath.Join(javaSrcDir, "Scenario.java"), scnJavaBytes, 0o644); err != nil {
		return err
	}
	javaClassDir := filepath.Join(workDir, "java-classes")
	if err := os.MkdirAll(javaClassDir, 0o755); err != nil {
		return err
	}
	// Find every .java under javaSrcDir so javac picks them up.
	var javaFiles []string
	_ = filepath.Walk(javaSrcDir, func(p string, info os.FileInfo, err error) error {
		if err == nil && !info.IsDir() && strings.HasSuffix(p, ".java") {
			javaFiles = append(javaFiles, p)
		}
		return nil
	})
	if len(javaFiles) == 0 {
		return fmt.Errorf("no .java files under %s", javaSrcDir)
	}
	javacArgs := append([]string{"-d", javaClassDir, "-cp", tc.Classpath()}, javaFiles...)
	out, err := runCapture("", tc.Javac, javacArgs...)
	if err != nil {
		return fmt.Errorf("javac failed: %w\n%s", err, out)
	}
	javaOut, err := runCapture("", tc.Java, "-cp", tc.Classpath(javaClassDir), "Scenario")
	if err != nil {
		return fmt.Errorf("java -cp Scenario failed: %w\n%s", err, javaOut)
	}

	// 3) Compare. Normalise trailing whitespace so a stray CR or
	// trailing newline isn't reported as a difference.
	goLines := strings.Split(strings.TrimRight(goOut, "\n"), "\n")
	javaLines := strings.Split(strings.TrimRight(javaOut, "\n"), "\n")

	// Persist both stdout streams so a passing run still leaves an
	// inspectable trace under workDir.
	_ = os.WriteFile(filepath.Join(workDir, "go-stdout.txt"), []byte(goOut), 0o644)
	_ = os.WriteFile(filepath.Join(workDir, "java-stdout.txt"), []byte(javaOut), 0o644)

	if equalLines(goLines, javaLines) {
		return nil
	}
	return fmt.Errorf("Go and Java stdout differ:\n%s", unifiedDiff(goLines, javaLines))
}

func equalLines(a, b []string) bool {
	if len(a) != len(b) {
		return false
	}
	for i := range a {
		if a[i] != b[i] {
			return false
		}
	}
	return true
}

func unifiedDiff(a, b []string) string {
	var buf bytes.Buffer
	max := len(a)
	if len(b) > max {
		max = len(b)
	}
	for i := 0; i < max; i++ {
		var ga, jb string
		if i < len(a) {
			ga = a[i]
		}
		if i < len(b) {
			jb = b[i]
		}
		if ga == jb {
			fmt.Fprintf(&buf, "  %s\n", ga)
		} else {
			fmt.Fprintf(&buf, "- go  : %s\n", ga)
			fmt.Fprintf(&buf, "+ java: %s\n", jb)
		}
	}
	return buf.String()
}

func runCapture(dir, name string, args ...string) (string, error) {
	cmd := exec.Command(name, args...)
	if dir != "" {
		cmd.Dir = dir
	}
	out, err := cmd.CombinedOutput()
	return string(out), err
}

// backendTomgoRoot mirrors backend.locateTomgoRoot for the equivtest
// scenario's go.mod replace directive.
func backendTomgoRoot() string {
	dir, err := os.Getwd()
	if err != nil {
		return ".."
	}
	for {
		if data, err := os.ReadFile(filepath.Join(dir, "go.mod")); err == nil && bytes.Contains(data, []byte("module tom/tomgo")) {
			return dir
		}
		parent := filepath.Dir(dir)
		if parent == dir {
			return ".."
		}
		dir = parent
	}
}

// LocateRepoRoot walks up from cwd looking for the directory that
// contains both `stable/` and `applications/prototype3D/`.
func LocateRepoRoot() (string, error) {
	dir, err := os.Getwd()
	if err != nil {
		return "", err
	}
	for {
		if _, err := os.Stat(filepath.Join(dir, "stable")); err == nil {
			if _, err := os.Stat(filepath.Join(dir, "applications", "prototype3D")); err == nil {
				return dir, nil
			}
		}
		parent := filepath.Dir(dir)
		if parent == dir {
			return "", fmt.Errorf("repo root (with stable/ and applications/prototype3D/) not found from %s", dir)
		}
		dir = parent
	}
}
