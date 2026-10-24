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
