<!-- Create a flat list of all the title-author pairs, with each pair enclosed in a "result" element. -->
<results>
  {
    for $b in doc("http://bstore1.example.com/bib.xml")/bib/book,
        $t in $b/title,
        $a in $b/author
    return
        <result>
            { $t }    
            { $a }
        </result>
  }
</results>