let proof_chan = ref stdout

let in_chan =  ref stdin

let split_rew = ref false

open Arg

let print_ver () = 
  Printf.printf "%s version %s, compiled by OCaml version %s\n"
    Sys.executable_name
    "0.1"
    Sys.ocaml_version;
  flush stdout;
  exit 0

let usage = Printf.sprintf "Usage: %s [options] [file]
  If file is specified, inputs are read from it.
Options are:"
       Sys.executable_name

let usage_func = ref (fun () -> ())

let open_proofs p = proof_chan := open_out p

let speclist = Arg.align
  [ 
    "-p", String open_proofs, "file Print proofs in file";
    "--proofs", String open_proofs, "file Print proofs in file";
    "-s", Set split_rew," Parallelize rewriting";
    "--split", Set split_rew," Parallelize rewriting";
    "-v", Unit print_ver, " Print version";
    "--version", Unit print_ver, " Print version";
    "-help", Unit (fun () -> !usage_func ()), " Display this list of options";
    "--help", Unit (fun () -> !usage_func ()), " Display this list of options"
  ]

let _ = usage_func := (fun () -> Arg.usage speclist usage; exit 0)

let _ = 
  Arg.parse 
    speclist
    (fun s -> in_chan := open_in s) 
    usage

