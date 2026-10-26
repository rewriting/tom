package backend

import (
	"bytes"
	"go/format"
	"io"
	"time"

	"tom/engine/bridge"
	"tom/engine/platform"
	"tom/engine/tools"
)

// defaultDeep is BackendPlugin.defaultDeep (BackendPlugin.t:74).
const defaultDeep = 1

// Generate is BackendPlugin.run (BackendPlugin.t:110, to line 173) on a bridge
// bundle: it writes on w the code Java's backend writes in the output
// file, and its diagnostics on r; ok is false when an error was reported.
// port: the encoding option and the output file are GoBridgePlugin's (it
// writes what tomgo prints); the PlatformException branch
// (platformStopped) has no Go counterpart: an option error is a runtime
// error here (platform.Options.SetReporter).
func Generate(b *bridge.Bundle, w io.Writer, r *platform.Reporter) (ok bool) {
	opts := platform.NewOptions(b.Options)
	opts.SetReporter(r)
	file := b.InputName
	ran := platform.Run(r, file, func() {
		if opts.Bool("noOutput") { // !isActivated() (BackendPlugin.t:233)
			r.Info(file, 0, platform.BackendInactivated)
			return
		}
		start := time.Now()
		// Go with pretty (-p, -O2): the code is formatted like gofmt does
		// (BackendPlugin.formatGo); a failure keeps the unformatted code.
		var formatted *bytes.Buffer
		out := w
		if opts.Bool("goCode") && opts.Bool("pretty") {
			formatted = &bytes.Buffer{}
			out = formatted
		}
		output := tools.NewOutputCode(out, opts)
		if !opts.Bool("goCode") {
			// Java: CGenerator, CamlGenerator, PythonGenerator, AdaGenerator,
			// CSharpGenerator, JavaGenerator (D5)
			panic(&platform.RuntimeError{Msg: "no selected language for the Backend"})
		}
		st := tools.FromBundle(b)
		generator := newGoGenerator(output, opts, st)
		generator.setDeclPrefix(tools.GoDeclPrefix(b.InputName))
		pilCode := b.Code
		(&backendPlugin{st: st}).markUsedConstructorDestructor(pilCode)
		generator.generate(defaultDeep, pilCode, tools.DefaultModuleName)
		r.Info(file, 0, platform.TomGenerationPhase, int(time.Since(start).Milliseconds()))
		if err := output.Close(); err != nil {
			// Java passes one argument: {1} stays in the text
			r.Error(file, 0, platform.BackendIOException, err.Error())
		}
		if formatted != nil {
			code := formatted.Bytes()
			if src, err := format.Source(code); err == nil {
				code = src
			} else {
				r.Warning(file, 0, platform.GoFormatFailed, err.Error())
			}
			if _, err := w.Write(code); err != nil {
				r.Error(file, 0, platform.BackendIOException, err.Error())
			}
		}
	})
	return ran && !r.HasError()
}
