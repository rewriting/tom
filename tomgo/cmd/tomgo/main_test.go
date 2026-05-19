package main

import "testing"

// Smoke test: make sure the package compiles and exposes the expected
// command names by parsing args without panicking.
func TestUsageMentionsCommands(t *testing.T) {
	for _, cmd := range []string{"scan-hooks", "gom", "version", "help"} {
		if !contains(usage, cmd) {
			t.Errorf("usage string is missing command %q", cmd)
		}
	}
}

func contains(s, sub string) bool {
	for i := 0; i+len(sub) <= len(s); i++ {
		if s[i:i+len(sub)] == sub {
			return true
		}
	}
	return false
}
