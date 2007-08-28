from sl import *

class Term(Visitable):
    def __init__(self,name,children):
        self.name = name
        self.children = children

    def setChildren(self,children):
        return Term(self.name,children[:])

    def getChildren(self):
        return self.children[:]

    def getChildAt(self,i):
        return self.children[i]

    def setChildAt(self,i,c):
        children = self.children[:]
        children[i] = c
        return Term(self.name,children)

    def getChildCount(self):
        return len(self.children)

    def __str__(self):
        return "%s(%s)" % (self.name, ','.join(map(str,self.children)))

    def __eq__(self,o):
        return (isinstance(o,Term) 
                and o.name == self.name 
                and o.children == self.children)

    def __ne__(self,o):
        return not self == o

def Constructor(name,arity):
    def f(*args):
        if len(args) != arity:
            raise Exception("wrong number of argument for %s" % name)
        return Term(name,list(args))
    return f

