<!-- For each book in the bibliography, list the title and authors, grouped inside a "result" element.-->

<results>
{
    for $b in doc("http://bstore1.example.com/bib.xml")/bib/book
    return
        <result>
            { $b/title }
            { $b/author  }
        </result>
}
</results>

