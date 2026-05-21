// backend_go — thin shim around the shared used-symbol collector.
//
// The walk is identical between Java and Go (same IR, same TL-chunk
// prefixes), so the logic lives in backend_shared. Each backend
// keeps a snake_case alias for ergonomics.

package backend_go

import (
	"tom/tomgo/stable/library/tomast"
	"tom/tomgo/stable/tom/backend_shared"
)

func collectUsedSymbols(list *tomast.ConcCodeCodeList) map[string]bool {
	return backend_shared.CollectUsedSymbols(list, backend_shared.DefaultHelperPrefixes())
}

func collectStrategySymbols(list *tomast.ConcCodeCodeList, used map[string]bool) {
	backend_shared.CollectStrategySymbols(list, used, backend_shared.DefaultHelperPrefixes())
}

func scanInstrForUsedSymbols(i tomast.Instruction, used map[string]bool) {
	backend_shared.ScanInstrForUsedSymbols(i, used, backend_shared.DefaultHelperPrefixes())
}
