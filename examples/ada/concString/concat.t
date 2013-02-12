with System; use System;
with Ada.Text_IO; use Ada.Text_IO;
package body Concat is

	%include { string.tom }

	function acc2str(a: access String) return String is begin return a.all; end;
	
	procedure extraireValeur(a: access String) is
	begin
		%match(a) {
			concString('<',tag*, ' ', reste*, '/>') -> {
				--Extraction de la balise
				put_line("Balise: " & acc2str(`tag*));
				extraireValeur(`reste*);
				return;					
			}
			
			concString(nom*, '=', '"', valeur*, '"', ' ', reste*) -> {
				--Extraction des differents champs
				put_line("Nom: '" & acc2str(`nom*) & "' Valeur: '" & acc2str(`valeur*) & "'");
				extraireValeur(`reste*);
				return;
			}
		}
 	end extraireValeur;
 	
 	
	procedure main is
		S: access String;
	begin
	
		put_line("===[ Exemple 1 ]===");
		S := new String'("abcabc");
		put_line("S = '" & acc2str(S) & "'");

		%match(S) {
			concString(_*,L*,_*,L*,_*) -> {
				if ( `L*'length > 0) then
					put_line("repetition de '" & acc2str(`L*) & "' dans la chaine S");
				end if;
			}
		}
		put_line("");
		
		put_line("===[ Exemple 2 ]===");
		S := new String'("Hello World!");
		put_line("S = '" & acc2str(S) & "'");

		%match(S) {
			concString(before*,'l',after*) -> {
				put_line("Presence dans S d'un 'l' après '" & acc2str(`before*) & "' et avant '" & acc2str(`after*) & "'");
			}
		}
		put_line("");
		
		put_line("===[ Exemple 3 ]===");
		S := new String'("<link title=""Creative Commons"" type=""application/rdf+xml"" href=""/wiki/index.php5?title=tom-2.8:Language_Basics_%E2%80%93_Level_2&amp;action=creativecommons"" rel=""meta"" />");
		
		put_line("S = '" & acc2str(S) & "'");

		extraireValeur(S);

	end Main;
end Concat;
