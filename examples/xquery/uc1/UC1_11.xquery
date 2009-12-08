<!-- For each book with an author, return the book with its title and authors. For each book with an editor, return a reference with the book title and the editor's affiliation.

Solution in XQuery: -->

<bib>
{
        for $b in doc("http://bstore1.example.com/bib.xml")//book[author]
        return
            <book>
                { $b/title }
                { $b/author }
            </book>
}
{
        for $b in doc("http://bstore1.example.com/bib.xml")//book[editor]
        return
          <reference>
            { $b/title }
            {$b/editor/affiliation}
          </reference>
}
</bib>  

