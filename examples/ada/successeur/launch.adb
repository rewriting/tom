with Ada.Text_IO; use Ada.Text_IO;

with Successeur; use Successeur;

procedure launch is
begin
Put_Line ("2+3 =" & Integer'Image(plus(2, 3)));
end Launch;
