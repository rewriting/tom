package gom

import (
	"fmt"
	"strings"

	"tom/tomgo/internal/gomast"
)

// Lift converts a hand-written V1 *Module (the AST shape this package
// has historically used) to the canonical Gom V2 AST emitted by
// tom/tomgo when run on src/tom/gom/adt/Gom.gom.
//
// The mapping is mechanical:
//
//	*Module             → gomast.GomModule(ModuleName, SectionList)
//	  .Name (dotted)    → GomModuleName.Name (joined with ".")
//	  .Imports          → ImportsSection(ConcImportedModule(GomModuleName…))
//	  .Sorts            → SortType productions, each with an
//	                      AlternativeList of Alternative
//	  .Hooks            → Hook productions
//
// Defaults applied:
//
//   - Every GomType is built with `ExpressionType` specialization (the
//     spec the current parser does not distinguish).
//   - Every Field is built with `None` ScopeSpecifier.
//   - `Alternative.Line` and `GomHook.Line` are preserved by storing
//     them as `Origin(Line)` inside the OptionList of that node.
//   - `SortDecl.Line` is NOT preserved because the V2 SortType node
//     has no Option slot. The corresponding V1 field is zeroed by
//     Lower; round-trip tests treat it as "lost on purpose".
func Lift(m *Module) gomast.GomModule {
	name := gomast.MakeGomModuleName(strings.Join(m.Name, "."))

	var sections []gomast.Section
	if len(m.Imports) > 0 {
		names := make([]gomast.GomModuleName, 0, len(m.Imports))
		for _, imp := range m.Imports {
			names = append(names, gomast.MakeGomModuleName(imp))
		}
		sections = append(sections, gomast.MakeImports(gomast.MakeConcImportedModule(names...)))
	}

	var prods []gomast.Production
	for _, s := range m.Sorts {
		sortGT := gomast.MakeGomType(gomast.MakeExpressionType(), s.Name)
		alts := make([]gomast.Alternative, 0, len(s.Alternatives))
		for _, alt := range s.Alternatives {
			fields := make([]gomast.Field, 0, len(alt.Args))
			for _, a := range alt.Args {
				argGT := gomast.MakeGomType(gomast.MakeExpressionType(), a.Type)
				if a.Variadic {
					fields = append(fields, gomast.MakeStarredField(argGT, gomast.MakeNone()))
				} else {
					fields = append(fields, gomast.MakeNamedField(a.Name, argGT, gomast.MakeNone()))
				}
			}
			alts = append(alts, gomast.MakeAlternative(
				alt.Op,
				gomast.MakeConcField(fields...),
				sortGT,
				originOptionList(alt.Line),
			))
		}
		prods = append(prods, gomast.MakeSortType(
			sortGT,
			gomast.MakeConcAtom(),
			gomast.MakeConcAlternative(alts...),
		))
	}

	for _, h := range m.Hooks {
		args := make([]gomast.Arg, 0, len(h.Args))
		for _, a := range h.Args {
			args = append(args, gomast.MakeArg(a))
		}
		prods = append(prods, gomast.MakeHook(
			scopeToIdKind(h.Scope),
			h.PointCut,
			gomast.MakeHookKind(h.Kind),
			gomast.MakeConcArg(args...),
			gomast.MakeHookCode(h.Body),
			originOptionList(h.Line),
		))
	}

	if len(prods) > 0 {
		sections = append(sections, gomast.MakePublic(gomast.MakeConcProduction(prods...)))
	}
	return gomast.MakeGomModule(name, gomast.MakeConcSection(sections...))
}

// Lower is the inverse of Lift. It walks the V2 GomModule and rebuilds
// a V1 *Module. SortDecl.Line is left at zero (V2 has no place for it).
func Lower(g gomast.GomModule) (*Module, error) {
	gm, ok := g.(*gomast.GomModuleGomModule)
	if !ok {
		return nil, fmt.Errorf("Lower: expected *GomModuleGomModule, got %T", g)
	}
	name := gm.ModuleName.(*gomast.GomModuleNameGomModuleName).Name
	parts := strings.Split(name, ".")
	m := &Module{Name: parts}

	sectionList, ok := gm.SectionList.(*gomast.ConcSectionSectionList)
	if !ok {
		return nil, fmt.Errorf("Lower: expected ConcSectionSectionList, got %T", gm.SectionList)
	}
	for _, sec := range sectionList.Slots {
		switch s := sec.(type) {
		case *gomast.ImportsSection:
			imps, ok := s.ImportList.(*gomast.ConcImportedModuleImportList)
			if !ok {
				return nil, fmt.Errorf("Lower: bad ImportList %T", s.ImportList)
			}
			for _, imp := range imps.Slots {
				m.Imports = append(m.Imports, imp.(*gomast.GomModuleNameGomModuleName).Name)
			}
		case *gomast.PublicSection:
			prodList, ok := s.ProductionList.(*gomast.ConcProductionProductionList)
			if !ok {
				return nil, fmt.Errorf("Lower: bad ProductionList %T", s.ProductionList)
			}
			for _, p := range prodList.Slots {
				switch prod := p.(type) {
				case *gomast.SortTypeProduction:
					sd, err := lowerSortType(prod)
					if err != nil {
						return nil, err
					}
					m.Sorts = append(m.Sorts, *sd)
				case *gomast.HookProduction:
					h, err := lowerHook(prod)
					if err != nil {
						return nil, err
					}
					m.Hooks = append(m.Hooks, *h)
				case *gomast.AtomDeclProduction:
					// Phase 3 does not produce AtomDecl yet (V1 has no
					// concept of it). Drop with no error so a Lift'd
					// V1 round-trips, but log via an explicit ignored
					// branch for clarity.
					_ = prod
				default:
					return nil, fmt.Errorf("Lower: unsupported production %T", prod)
				}
			}
		default:
			return nil, fmt.Errorf("Lower: unsupported section %T", sec)
		}
	}
	return m, nil
}

func lowerSortType(prod *gomast.SortTypeProduction) (*SortDecl, error) {
	gt, ok := prod.Type.(*gomast.GomTypeGomType)
	if !ok {
		return nil, fmt.Errorf("Lower: SortType.Type is %T, expected GomTypeGomType", prod.Type)
	}
	sd := &SortDecl{Name: gt.Name}
	altList, ok := prod.AlternativeList.(*gomast.ConcAlternativeAlternativeList)
	if !ok {
		return nil, fmt.Errorf("Lower: AlternativeList is %T", prod.AlternativeList)
	}
	for _, a := range altList.Slots {
		alt, ok := a.(*gomast.AlternativeAlternative)
		if !ok {
			return nil, fmt.Errorf("Lower: Alternative is %T", a)
		}
		v1alt := Alternative{Op: alt.Name, Line: extractLine(alt.Option)}
		fields, ok := alt.DomainList.(*gomast.ConcFieldFieldList)
		if !ok {
			return nil, fmt.Errorf("Lower: DomainList is %T", alt.DomainList)
		}
		for _, f := range fields.Slots {
			switch fld := f.(type) {
			case *gomast.NamedFieldField:
				gt, ok := fld.FieldType.(*gomast.GomTypeGomType)
				if !ok {
					return nil, fmt.Errorf("Lower: NamedField type is %T", fld.FieldType)
				}
				v1alt.Args = append(v1alt.Args, Arg{Name: fld.Name, Type: gt.Name})
			case *gomast.StarredFieldField:
				gt, ok := fld.FieldType.(*gomast.GomTypeGomType)
				if !ok {
					return nil, fmt.Errorf("Lower: StarredField type is %T", fld.FieldType)
				}
				v1alt.Args = append(v1alt.Args, Arg{Type: gt.Name, Variadic: true})
				v1alt.Variadic = true
			default:
				return nil, fmt.Errorf("Lower: unsupported Field %T", fld)
			}
		}
		sd.Alternatives = append(sd.Alternatives, v1alt)
	}
	return sd, nil
}

func lowerHook(prod *gomast.HookProduction) (*GomHook, error) {
	h := &GomHook{
		Scope:    idKindToScope(prod.NameType),
		PointCut: prod.Name,
		Line:     extractLine(prod.Option),
	}
	kk, ok := prod.HookType.(*gomast.HookKindHookKind)
	if !ok {
		return nil, fmt.Errorf("Lower: HookType is %T", prod.HookType)
	}
	h.Kind = kk.Kind
	argList, ok := prod.Args.(*gomast.ConcArgArgList)
	if !ok {
		return nil, fmt.Errorf("Lower: ArgList is %T", prod.Args)
	}
	for _, a := range argList.Slots {
		h.Args = append(h.Args, a.(*gomast.ArgArg).Name)
	}
	switch hc := prod.HookContent.(type) {
	case *gomast.HookCodeHookContent:
		h.Body = hc.StringCode
	case *gomast.HookRulesHookContent:
		return nil, fmt.Errorf("Lower: HookRules not yet supported (Phase 3 only handles textual hook bodies)")
	default:
		return nil, fmt.Errorf("Lower: unsupported HookContent %T", hc)
	}
	return h, nil
}

// scopeToIdKind maps the V1 string scope to its V2 IdKind term.
// The empty (unscoped) hook defaults to KindOperator, matching the
// ANTLR rule `(hookScope)? pointCut=ID … -> ^( Hook ^( KindOperator) … )`.
func scopeToIdKind(scope string) gomast.IdKind {
	switch scope {
	case "sort":
		return gomast.MakeKindSort()
	case "module":
		return gomast.MakeKindModule()
	default:
		return gomast.MakeKindOperator()
	}
}

func idKindToScope(k gomast.IdKind) string {
	switch k.(type) {
	case *gomast.KindSortIdKind:
		return "sort"
	case *gomast.KindModuleIdKind:
		return "module"
	case *gomast.KindOperatorIdKind:
		return "" // operator scope is the implicit default in V1
	default:
		return ""
	}
}

// originOptionList wraps a line number as `OptionList(Origin(Line))`.
// A zero line still produces an OptionList — that way the V2 shape is
// always the same and Lower never has to guess.
func originOptionList(line int) gomast.Option {
	return gomast.MakeOptionList(gomast.MakeOrigin(int64(line)))
}

func extractLine(opt gomast.Option) int {
	list, ok := opt.(*gomast.OptionListOption)
	if !ok {
		return 0
	}
	for _, o := range list.Slots {
		if origin, ok := o.(*gomast.OriginOption); ok {
			return int(origin.Line)
		}
	}
	return 0
}
