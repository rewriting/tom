<!-- In the Procedure section of Report1, what are the first two Instruments to be used?

Solution in XQuery:

-->

for $s in doc("report1.xml")//section[section.title = "Procedure"]
return ($s//instrument)[position()<=2]

