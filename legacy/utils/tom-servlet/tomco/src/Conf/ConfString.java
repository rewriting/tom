package Conf;

/**
 * Classe qui contient toutes les variables utilisées pour l'initialisation et
 * la configuration du formulaire
 * @author cynthia
 */
public class ConfString {
	
	public final static String butUpload = "Charger fichier";
    public final static String butRun = "Run";
    public final static String butReset= "Reset";
    public final static String butStop= "Stop";
    public final static String butSave= "Save";
    public final static String selected = "selected=\"selected\"";
    public final static String checked = "checked=\"checked\"";
    public final static String hidden = "style=\"visibility:hidden\"";
    public final static String ScriptVide = " Le code source est obligatoire ";
    public final static String nomInvalide="Fichier non créé : minimum attendu \"class \"nomFichier \\s {";
    public final static String regex="class\\s+([\\w_$]+?)\\s";
    public final static String nameClass="class";
    public final static String CodeInitial = "public class HelloWorld {\n	public static void main(String argv[])"+
    " {\n		System.out.println(\"Hello World\");\n	}\n}";
    public final static String CodeInitial2= "import main.peano.types.*;\n  public class Main {"+
    "\n  %gom {\n    module Peano\n    abstract syntax\n    Nat = zero()\n        | suc(pred:Nat)"+
    "\n        | plus(x1:Nat, x2:Nat)\n  }\n  public final static void main(String[] args) {"+
    "\n    Nat z   = `zero();\n    Nat one = `suc(z);\n    System.out.println(z);"+
    "\n    System.out.println(one);\n  }\n}";

}
