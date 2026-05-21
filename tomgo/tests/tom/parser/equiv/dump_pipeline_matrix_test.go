package tomparseq

import (
	"testing"
)

// TestPipelineDump_MutationMatrix pins, for every existing fixture, the set
// of pipeline phases that mutate the AST relative to the previous phase.
// This is the regression net for the porting effort: as each Go plugin
// lands, the matrix tells us upfront which fixtures the new plugin must
// reproduce exactly, and which it can pass through unchanged.
//
// Findings (Phase 5 baseline, observed on stable/dist/lib of branch tom-go):
//
//   - Transformer       : identity on all 30 fixtures (no Transformation decls)
//   - SyntaxChecker     : identity on all 30 fixtures (checker, no mutation)
//   - Desugarer         : mutates 13 fixtures (mostly `_` → fresh var)
//   - Typer             : mutates 7 additional fixtures past Desugarer
//                          (backquote-application / %strategy types)
//
// Each entry below is a fixture name + the four expected "diff vs previous
// phase" flags. Add new fixtures here when the canonical 30 list grows.
func TestPipelineDump_MutationMatrix(t *testing.T) {
	type expect struct {
		transformedDiffersFromParsed   bool
		syncheckedDiffersFromParsed    bool
		desugaredDiffersFromSynchecked bool
		typedDiffersFromDesugared      bool
	}
	matrix := map[string]expect{
		// All-pass-through fixtures (parser-only AST through Typer).
		"skeleton":             {false, false, false, false},
		"op_noargs":            {false, false, false, false},
		"op_slots":             {false, false, false, false},
		"typeterm_extends":     {false, false, false, false},
		"oplist_oparray":       {false, false, false, false},
		"include_local":        {false, false, false, false},
		"water_multi":          {false, false, false, false},
		"match0c_named":        {false, false, false, false},
		"match0f_multi":        {false, false, false, false},
		"match0o_bqbody":       {false, false, false, false},
		"match0p_bqbody_water": {false, false, false, false},
		"match0t_and":          {false, false, false, false},
		"meta0_minimal":        {false, false, false, false},
		// Desugarer rewrites `_` to a fresh variable name.
		"match0b":           {false, false, true, false},
		"match0d_appl":      {false, false, true, false},
		"match0e_appl_args": {false, false, true, false},
		"match0g_rules":     {false, false, true, false},
		"match0h_body":      {false, false, true, false},
		"match0i_explicit":  {false, false, true, false},
		"match0j_star":      {false, false, true, false},
		"match0k_annot":     {false, false, true, false},
		"match0l_anti":      {false, false, true, false},
		"match0m_or":        {false, false, true, false},
		// Typer attaches inferred types to backquote applications.
		"match0n_bqappl":      {false, false, false, true},
		"match0q_bqappl_body": {false, false, false, true},
		"match0r_bq_multi":    {false, false, false, true},
		"match0s_bqstar":      {false, false, false, true},
		"strategy0_minimal":   {false, false, false, true},
		"strategy2_visit_body": {false, false, false, true},
		// Both Desugarer and Typer touch this one.
		"strategy1_visit": {false, false, true, true},
	}
	if len(matrix) != 30 {
		t.Fatalf("matrix should cover 30 fixtures, has %d", len(matrix))
	}
	for name, exp := range matrix {
		t.Run(name, func(t *testing.T) {
			tc, input := resolveOrSkip(t, name)
			parsed, err := tc.DumpJavaPhase(input, PhaseParsed)
			if err != nil {
				t.Fatalf("parsed: %v", err)
			}
			transformed, err := tc.DumpJavaPhase(input, PhaseTransformed)
			if err != nil {
				t.Fatalf("transformed: %v", err)
			}
			synchecked, err := tc.DumpJavaPhase(input, PhaseSynchecked)
			if err != nil {
				t.Fatalf("synchecked: %v", err)
			}
			desugared, err := tc.DumpJavaPhase(input, PhaseDesugared)
			if err != nil {
				t.Fatalf("desugared: %v", err)
			}
			typed, err := tc.DumpJavaPhase(input, PhaseTyped)
			if err != nil {
				t.Fatalf("typed: %v", err)
			}
			check := func(label string, want bool, a, b string) {
				if got := a != b; got != want {
					t.Errorf("%s: expected diff=%v but got diff=%v", label, want, got)
				}
			}
			check("transformed vs parsed", exp.transformedDiffersFromParsed, transformed, parsed)
			check("synchecked vs parsed", exp.syncheckedDiffersFromParsed, synchecked, parsed)
			check("desugared vs synchecked", exp.desugaredDiffersFromSynchecked, desugared, synchecked)
			check("typed vs desugared", exp.typedDiffersFromDesugared, typed, desugared)
		})
	}
}
