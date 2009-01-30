<!-- In the Procedure section of Report1, what Instruments were used in the second Incision?

Solution in XQuery: -->

for $s in doc("report1.xml")//section[section.title = "Procedure"]
return ($s//incision)[2]/instrument

