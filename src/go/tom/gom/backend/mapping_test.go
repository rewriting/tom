package backend

import (
	"strings"
	"testing"

	gom "tom/gom/parser"
)

// The get_slot of the mapping reads the Go field of the slot, renamed
// when its exported name is a method of the alt struct (String_, Hash_…),
// with and without --gohooks.
func TestMappingGetSlotUsesRenamedField(t *testing.T) {
	src := "module M\nabstract syntax\nT = n(String:String, Hash:int, x:int) | a()\n"
	mod, err := gom.ParseBytes([]byte(src))
	if err != nil {
		t.Fatal(err)
	}
	for _, opts := range []Options{{}, {GoHooks: true}} {
		m := string(EmitTomMappingGo(mod, opts))
		for _, want := range []string{
			"get_slot(String,t) { ($t).(*NT).String_ }",
			"get_slot(Hash,t) { ($t).(*NT).Hash_ }",
			"get_slot(x,t) { ($t).(*NT).X }",
		} {
			if !strings.Contains(m, want) {
				t.Errorf("GoHooks=%v: mapping lacks %q:\n%s", opts.GoHooks, want, m)
			}
		}
	}
}
