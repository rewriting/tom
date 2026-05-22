// Package backend — bundled-gom artefact location.
//
// `LocateBundled` finds the jars + Gom.config that tomgo ships
// alongside its own sources (under stable/lib/). Returned values:
//
//   - libDir    absolute path of the `stable/lib` directory.
//   - classpath `:`-joined list of every .jar under runtime/,
//                 tom/, tools/, compiletime/.
//   - gomConfig path of the aterm-encoded `Gom.config` plugin
//                 pipeline.
//
// All three are empty when no `stable/lib/tom/gom.jar` is found
// walking up from CWD. Callers (the parser when expanding `%gom`
// for Java target, the `tomgo gom --target java` CLI wrapper)
// should check `libDir == ""` and skip / error appropriately.

package backend

import (
	"os"
	"path/filepath"
	"strings"
)

// LocateBundled walks up from the current working directory
// looking for the marker `stable/lib/tom/gom.jar`. The first
// directory containing it provides `libDir`; the classpath is
// built from every `*.jar` under its subdirectories.
func LocateBundled() (libDir, classpath, gomConfig string) {
	cwd, _ := os.Getwd()
	for dir := cwd; ; {
		p := filepath.Join(dir, "stable", "lib", "tom", "gom.jar")
		if _, err := os.Stat(p); err == nil {
			libDir = filepath.Join(dir, "stable", "lib")
			break
		}
		parent := filepath.Dir(dir)
		if parent == dir {
			return "", "", ""
		}
		dir = parent
	}
	var jars []string
	for _, sub := range []string{"runtime", "tom", "tools", "compiletime"} {
		entries, err := os.ReadDir(filepath.Join(libDir, sub))
		if err != nil {
			continue
		}
		for _, e := range entries {
			if filepath.Ext(e.Name()) == ".jar" {
				jars = append(jars, filepath.Join(libDir, sub, e.Name()))
			}
		}
	}
	classpath = strings.Join(jars, string(os.PathListSeparator))
	gomConfig = filepath.Join(libDir, "config", "Gom.config")
	return libDir, classpath, gomConfig
}
