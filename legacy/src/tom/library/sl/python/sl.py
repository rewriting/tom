from copy import copy

class VisitFailure(Exception):
    def __init__(self, *args):
        Exception.__init__(self,*args)

class Visitable(object):

    def setChildren(self, _):
        """
        Returns Visitable
        Parameters:
            children: Visitable[]
        """
        raise Exception("Not yet implemented")


    def getChildren(self):
        """
        Returns Visitable[]
        """
        raise Exception("Not yet implemented")


    def getChildAt(self, _):
        """
        Returns Visitable
        Parameters:
            i: int
        """
        raise Exception("Not yet implemented")


    def setChildAt(self, _, child):
        """
        Returns Visitable
        Parameters:
            i: int
            child: Visitable
        """
        print child # to avoid warnings
        raise Exception("Not yet implemented")


    def getChildCount(self):
        """
        Returns int
        """
        raise Exception("Not yet implemented")


class Strategy(Visitable):

    def setEnvironment(self, _):
        """
        Returns void
        Parameters:
            p: Environment
        """
        raise Exception("Not yet implemented")


    def getEnvironment(self):
        """
        Returns Environment
        """
        raise Exception("Not yet implemented")


    def visitLight(self, _):
        """
        Returns Visitable
        Parameters:
            any: Visitable

        Throws: 
            VisitFailure
        """
        raise Exception("Not yet implemented")

    def visit_Visitable(self, _):
        """
        Returns Visitable
        Parameters:
            any: Visitable


        Throws: 
            VisitFailure
        """
        raise Exception("Not yet implemented")

    def visit_Environment(self, _):
        """
        Returns Visitable
        Parameters:
            envt: Environment
        Throws: 
            VisitFailure
        """
        raise Exception("Not yet implemented")

    def visit_noarg(self):
        raise Exception("Not yet implemented")

    def visit(self,any_or_env=None):
        if not any_or_env:
            return self.visit_noarg()
        elif isinstance(any_or_env,Visitable):
            return self.visit_Visitable(any_or_env)
        elif isinstance(any_or_env,Environment):
            return self.visit_Environment(any_or_env)
        else:
            raise Exception("Bad argument type")


    def accept(self, _):
        """
        Returns Strategy
        Parameters:
            v: tom.library.sl.reflective.StrategyFwd
        Throws: 
            VisitFailure
        """
        raise Exception("Not yet implemented")

class AbstractStrategy(Strategy):

    def __init__(self):
        self.visitors = None
        self.environment = None

    def initSubterm(self,*visitors):
        """
        Returns void
        Java modifiers:
             protected
        """
        self.visitors = list(visitors)

    def initSubtermFromArray(self,v):
        self.visitors = v

    def getChildCount(self):
        """
        Returns int
        """
        return len(self.visitors)

    def getChildAt(self, i):
        """
        Returns Visitable
        Parameters:
            i: int
        """
        return self.visitors[i]

    def getChildren(self):
        """
        Returns Visitable[]
        """
        # [:] is a shortcut to copy arrays in python
        return self.visitors[:]

    def setChildAt(self, i, child):
        """
        Returns Visitable
        Parameters:
            i: intchild: Visitable
        """
        self.visitors[i] = child;
        return self

    def setChildren(self, children):
        """
        Returns Visitable
        Parameters:
            children: Visitable[]
        """
        self.visitors = children[:]
        return self

    def accept(self, v):
        """
        Returns Strategy
        Parameters:
            v: tom.library.sl.reflective.StrategyFwd
        Throws: 
            VisitFailure
        """
        return v.visit_Strategy(self)

    def visit_Environment(self, envt):
        """
        Returns Visitable
        Parameters:
            envt: Environment
        Throws: 
            VisitFailure
        """
        AbstractStrategy.static_init(self, envt)
        # int
        status = self.visit();
        if status == Environment.SUCCESS: 
            return self.getRoot()
        else:
            raise VisitFailure()

    def visit_Visitable(self, any):
        """
        Returns Visitable
        Parameters:
            any: Visitable
        Throws: 
            VisitFailure
        """
        self.init();
        self.setRoot(any);
        # int
        status = self.visit();
        if status == Environment.SUCCESS: 
            return self.getRoot()
        else:
            raise VisitFailure()

    def getEnvironment(self):
        """
        Returns Environment
        """
        if self.environment != None: 
            return self.environment
        else:
            raise Exception("environment not initialized")

    def setEnvironment(self, env):
        """
        Returns void
        Parameters:
            env: Environment
        """
        self.environment = env;

    def getRoot(self):
        """
        Returns Visitable
        """
        return self.environment.getRoot()

    def setRoot(self, any):
        """
        Returns void
        Parameters:
            any: Visitable
        """
        self.environment.setRoot(any);

    def getSubject(self):
        """
        Returns Visitable
        """
        return self.environment.getSubject()

    def setSubject(self, any):
        """
        Returns void
        Parameters:
            any: Visitable
        """
        self.environment.setSubject(any);

    def init(self):
        """
        Returns void
        """
        AbstractStrategy.static_init(self, Environment());

    @staticmethod
    def static_init(s, env):
        """
        Returns void
        Parameters:
            s: Strategyenv: Environment
        Java modifiers:
             static
        """
        if isinstance(s,AbstractStrategy) and (s.environment == env): 
            return

        s.setEnvironment(env);
        for i in xrange(s.getChildCount()):
            child = s.getChildAt(i);
            if isinstance(child,Strategy): 
                AbstractStrategy.static_init(child, env)

    def __str__(self):
        return self.str2([])

    def str2(self,visited):
        visited.append(self)
        children = []
        for v in self.visitors:
            if v in visited:
                children.append(repr(v))
            else:
                children.append(v.str2(visited))
        return "%s(%s)" % (self.__class__.__name__,','.join(children))

class StrategyFwd(AbstractStrategy):

    ARG = 0
    
    def __init__(self, v):
        """
        Parameters:
          Strategy v
        """
        AbstractStrategy.__init__(self)
        self.initSubterm(v);

    def visitLight(self, any):
        """
        Returns Visitable
        Parameters:
            any: Visitable
        Throws: 
            VisitFailure
        """
        if isinstance(any,Strategy): 
            return any.accept(self)
        else:
            return self.visitors[self.ARG].visitLight(any)

    def visit_noarg(self):
        """
        Returns int
        """
        try:
            self.setSubject(self.visitLight(self.getSubject()))
            return Environment.SUCCESS
        except VisitFailure:
            return Environment.FAILURE

    def visit_Strategy(self, any):
        """
        Returns Strategy
        Parameters:
            any: Strategy
        Throws: 
            VisitFailure
        """
        return self.visitors[self.ARG].visitLight(any)

class Mu(AbstractStrategy):
    VAR = 0
    V = 1

    def __init__(self,var,v):
        AbstractStrategy.__init__(self)
        self.expanded = False
        self.initSubterm(var,v)
        self.muStrategyTopDown = MuStrategyTopDown()

    def visitLight(self,any):
        if not self.expanded:
            self.muExpand()
        return self.visitors[self.V].visitLight(any)

    def visit_noarg(self):
        if not self.expanded:
            self.muExpand()
        return self.visitors[self.V].visit()

    def isExpanded(self):
        return self.visitors[self.VAR].isExpanded()

    def muExpand(self):
        try:
            self.muStrategyTopDown.init()
            self.muStrategyTopDown.visitLight(self)
            self.expanded = True
        except VisitFailure, f:
            print "mu reduction failed : " + str(f)

class MuStrategyTopDown:

  def __init__(self):
      self.stack = []

  def init(self):
      self.stack = []
    
  def visitLight(self, any):
      self.visitLight2(any,None,0,set())
  
  def visitLight2(self,any,parent,childNumber,set):
      """
      any: the node we visit
      parent: the node we come from
      childNumber: the n-th subtemr of parent
      set: set of already visited parent
      """

      #check that the current element has not already been expanded
      if any in set:
          return
      else:
          set.add(parent)

      # match any { m@Mu(var@MuVar[],v) -> ...
      if isinstance(any,Mu):
          m = any
          var = m.getChildAt(Mu.VAR)
          v = m.getChildAt(Mu.V)
          if isinstance(var,MuVar):
              self.stack = [m] + self.stack
              self.visitLight2(v,m,0,set)
              self.visitLight2(var,None,0,set)
              self.stack = self.stack[1:]
              return

      # muvar@MuVar(n)
      elif isinstance(any,MuVar):
          muvar = any
          n = muvar.getName()
          if not muvar.isExpanded():
              for m in self.stack:
                  if m.visitors[Mu.VAR].getName() == n:
                      muvar.setInstance(m)
                      if parent != None:
                          # shortcut: the MuVar is replaced by a pointer 
                          # to the 2nd child of Mu
                          # after expansion there is no more Mu or MuVar
                          parent.setChildAt(childNumber,m.visitors[Mu.V])
                      return
              raise VisitFailure("any = %s" % str(any))

      childCount = any.getChildCount()
      for i in xrange(childCount):
          self.visitLight2(any.getChildAt(i),any,i,set)

class MuVar(AbstractStrategy):

    def __init__(self, name):
        """
        Parameters:
          String name
        """
        AbstractStrategy.__init__(self)
        self.initSubterm()
        self.instance = None
        self.name = name

#     def __eq__(self, o):
#         """
#         Returns boolean
#         Parameters:
#             o: Object
#         """
#         if isinstance(o,MuVar): 
#             if o != None: 
#                 if self.name != None: 
#                     return (self.name == o.name)
#                 else:
#                     return self.name == o.name and self.instance == o.instance
#         return False

    def __hash__(self):
        """
        Returns int
        """
        if self.name != None: 
            return self.name.__hash__()
        else:
            return self.instance.__hash__()

    def visitLight(self, any):
        """
        Returns Visitable
        Parameters:
            any: Visitable
        Java modifiers:
             final
        Throws: 
            VisitFailure
        """
        if self.instance != None: 
            return self.instance.visitLight(any)
        else:
            raise VisitFailure()

    def visit_noarg(self):
        """
        Returns int
        """
        if self.instance != None: 
            return self.instance.visit()
        else:
            return Environment.FAILURE

    def getInstance(self):
        """
        Returns Strategy
        """
        return self.instance

    def setInstance(self, v):
        """
        Returns void
        Parameters:
            v: Strategy
        Java modifiers:
             protected
        """
        self.instance = v


    def setName(self, name):
        """
        Returns void
        Parameters:
            name: String
        Java modifiers:
             protected
        """
        self.name = name


    def isExpanded(self):
        """
        Returns boolean
        Java modifiers:
             protected final
        """
        return self.instance != None

    def getName(self):
        """
        Returns String
        """
        return self.name


    def __str__(self):
        """
        Returns String
        """
        return "[" + self.name + "," + repr(self.instance) + "]"

    def str2(self,_):
        return self.__str__()

class Path:
    def add(self, _):
        """
        Returns Path
        Parameters:
            p: Path

        """
        raise Exception("Not yet implemented")

    def sub(self, _):
        """
        Returns Path
        Parameters:
            p: Path

        """
        raise Exception("Not yet implemented")

    def __add__(self,p):
        return self.add(p)

    def __sub__(self,p):
        return self.sub(p)

    def inverse(self):
        """
        Returns Path

        """
        raise Exception("Not yet implemented")

    def getCanonicalPath(self):
        """
        Returns Path

        """
        raise Exception("Not yet implemented")

    def __len__(self):
        """
        Returns int

        """
        raise Exception("Not yet implemented")

    def getHead(self):
        """
        Returns int

        """
        raise Exception("Not yet implemented")

    def getTail(self):
        """
        Returns Path

        """
        raise Exception("Not yet implemented")

    def conc(self, _):
        """
        Returns Path
        Parameters:
            i: int

        """
        raise Exception("Not yet implemented")

class Position(Path):
    """
    Parameters:
        int[] omega
        OU Position prefix, Position suffixe
    """
    def __init__(self, prefix=[], suffix=None):
        if suffix:
            self.__init_prefix_suffix(prefix,suffix)
        else:
            self.setValue(prefix)
        
    """
    Parameters:
        Position prefix
        Position suffix
    """
    def __init_prefix_suffix(self, prefix, suffix):
        self.omega = prefix.toArray() + suffix.toArray()

    def toArray(self):
        """
        Returns int[]
        """
        return self.omega[:self.depth()+1]

    def setValue(self, omega):
        """
        Returns void
        Parameters:
            omega: int[]
        """
        self.omega = omega[:]

    def clone(self):
        """
        Returns Object
        """
        clone = copy(self)
        clone.omega = self.omega[:self.depth()+1]
        return clone

    def __hash__(self):
        """
        Returns int
        """
        hashedData = self.omega[:self.depth()+1]
        return self.depth() * hashedData.__hash__()

    def depth(self):
        """
        Returns int
        """
        return len(self.omega)

    def __cmp__(self, path):
        """
        Returns int
        Parameters:
            path: Path
        """
        # Position
        p = Position.make(path);
        for i in xrange(self.depth()):
            if i == p.depth() or self.omega[i] > p.omega[i]: 
                return 1
            else:
                if self.omega[i] < p.omega[i]: 
                    return -1

        if self.depth() == p.depth():
            return 0
        else:
            return 1

    def getOmega(self, v):
        """
        Returns Strategy
        Parameters:
            v: Strategy
        """
        res = v;
        for i in xrange(self.depth()-1,-1,-1):
            res = Omega(self.omega[i], res);
        return res

    def getOmegaPath(self, v):
        """
        Returns Strategy
        Parameters:
            v: Strategy
        """
        return self.getOmegaPathAux(v, 0)

    def getOmegaPathAux(self, v, i):
        """
        Returns Strategy
        Parameters:
            v: Strategyi: int
        Java modifiers:
             private
        """
        if i >= self.depth()-1: 
            return v
        else:
            return Sequence(Omega(self.omega[i], self.getOmegaPathAux(v,i+1)),v)

    def getReplace(self, t):
        """
        Returns Strategy
        Parameters:
            t: Visitable
        """
        class AnonymousClass(Identity):
            def visit(self):
                self.environment.setSubject(t);
                return Environment.SUCCESS

            def visitLight(self, x):
                return t

        return self.getOmega(AnonymousClass())

    def getSubterm(self):
        """
        Returns Strategy
        """
        this = [self]
        class GetSubterm(AbstractStrategy):
            def __init__(self):
                AbstractStrategy.__init__(self)
                self.initSubterm()

            def visitLight(self, subject):
                """
                Returns Visitable
                Parameters:
                    subject: Visitable
                Throws: 
                    VisitFailure
                """
                ref = [None]
                class AnonymousClass1(AbstractStrategy):
                    def __init__(self):
                        AbstractStrategy.__init__(self)
                        self.initSubterm()

                    def visitLight(self, v):
                        ref[0] = v
                        return v

                this[0].getOmega(AnonymousClass1()).visitLight(subject);
                return ref[0]


            def visit_noarg(self):
                """
                Returns int
                """
                ref = [None]
                class AnonymousClass2(AbstractStrategy):
                    def __init__(self):
                        AbstractStrategy.__init__(self)
                        self.initSubterm()

                    def visit_noarg(self):
                        ref[0] = self.environment.getSubject();
                        return Environment.SUCCESS

                s = this[0].getOmega(AnonymousClass2())
                try:
                    s.visit(self.environment.getRoot());
                    self.environment.setSubject(ref[0]);
                    return Environment.SUCCESS
                except VisitFailure:
                    return Environment.FAILURE

        return GetSubterm()


    def __str__(self):
        """
        Returns String
        """
        return str(self.omega[:self.depth()+1])

    def add(self, p):
        """
        Returns Path
        Parameters:
            p: Path
        """
        if p.length() > 0: 
            result = self.conc(p.getHead());
            return result.add(p.getTail())
        else:
            return self.clone()

    def sub(self, p):
        """
        Returns Path
        Parameters:
            p: Path
        """
        return (Position.make(p).inverse()).add(self)


    def inverse(self):
        """
        Returns Path
        """
        res = self.omega[:]
        res.reverse()
        return res

    @staticmethod
    def make(p):
        """
        Returns Position
        Parameters:
            p: Path
        Java modifiers:
             static
        """
        pp = p.getCanonicalPath();
        size = len(pp)
        omega = [0 for _ in xrange(size)]
        for i in xrange(size):
            omega[i] = pp.getHead()
            pp = pp.getTail()
        return Position(omega)


    def __len__(self):
        """
        Returns int
        """
        return len(self.omega)


    def getHead(self):
        """
        Returns int
        """
        return self.omega[0]


    def getTail(self):
        """
        Returns Path
        """
        if len(self) == 0: 
            return None
        return Position(self.omega[1:])


    def conc(self, i):
        """
        Returns Path
        Parameters:
            i: int
        """
        res = self.omega[:]
        res.append(i)
        return Position(res)

    def up(self):
        """
        Returns Position
        """
        return self.omega[:-1]

    def down(self, i):
        """
        Returns Position
        Parameters:
            i: int
        """
        return self.conc(i)

    def getCanonicalPath(self):
        """
        Returns Path
        """
        if len(self) == 0: 
            return self.clone()
        normalizedTail = self.getTail().getCanonicalPath().toArray()
        if len(normalizedTail) == 0 or self.omega[0] != -normalizedTail[0]: 
            result = [self.omega[0]] + normalizedTail
            return Position(result)
        else:
            result = normalizedTail[1:]
            return Position(result)

    def hasPrefix(self, prefix):
        """
        Returns boolean
        Parameters:
            prefix: Position
        """
        prefixTab = prefix.toArray()
        if len(self.omega) < len(prefixTab): 
            return False
        for i in xrange(len(prefixTab)):
            if prefixTab[i] != self.omega[i]: 
                return False
        return True

    def getSuffix(self, prefix):
        """
        Returns Position
        Parameters:
            prefix: Position
        """
        if not self.hasPrefix(prefix): 
            return None
        return Position(self.omega[len(prefix.depth()):])


    def changePrefix(self, oldprefix, newprefix):
        """
        Returns Position
        Parameters:
            oldprefix: Positionnewprefix: Position
        """
        if not self.hasPrefix(oldprefix): 
            return None
        suffix = self.getSuffix(oldprefix);
        return Position(newprefix, suffix)

class DeRef(AbstractStrategy):

    ARG = 0
    
    def __init__(self, s, relative, strict):
        """
        Java modifiers:
           private
        Parameters:
           Strategy s
        boolean relative
        boolean strict
        """
        AbstractStrategy.__init__(self)
        self.initSubterm(s);
        self.relative = relative;
        self.strict = strict;

    @staticmethod
    def make(s):
        """
        Returns DeRef
        Parameters:
            s: Strategy
        Java modifiers:
             static
        """
        return DeRef(s, False, False)

    @staticmethod
    def makeRelative(s):
        """
        Returns DeRef
        Parameters:
            s: Strategy
        Java modifiers:
             static
        """
        return DeRef(s, True, False)

    @staticmethod
    def makeStrict(s):
        """
        Returns DeRef
        Parameters:
            s: Strategy
        Java modifiers:
             static
        """
        return DeRef(s, False, True)

    @staticmethod
    def makeRelativeStrict(s):
        """
        Returns DeRef
        Parameters:
            s: Strategy
        Java modifiers:
             static
        """
        return DeRef(s, True, True)

    def isRelative(self):
        """
        Returns boolean
        """
        return self.relative

    def isStrict(self):
        """
        Returns boolean
        """
        return self.strict

    def visitLight(self, _):
        """
        Returns Visitable
        Parameters:
            x: Visitable
        Throws: 
            VisitFailure
        """
        raise Exception("The strategy operator DeRef can be used only with the methods visit() and fire()")

    def visit_noarg(self):
        """
        Returns int
        """
        if isinstance(self.environment.getSubject(),Path): 
            self.visitPath(self.environment.getSubject())
        else:
            if self.strict: 
                pass #does nothing when it is not a Ref
            else:
                return self.visitors[self.ARG].visit()
        return Environment.SUCCESS

    def visitPath(self, path):
        """
        Returns void
        Parameters:
            path: Path
        Java modifiers:
             private
        """
        if self.relative: 
            current = self.environment.getPosition()
            self.environment.followPath(path)
            status = self.visitors[self.ARG].visit()
            if status != Environment.SUCCESS: 
                self.environment.followPath(current.sub(self.environment.getPosition()))
                return
            self.environment.followPath(current.sub(self.environment.getPosition()));

class Environment:

    SUCCESS = 0
    FAILURE = 1
    IDENTITY = 2

    """
    Java modifiers:
         private
    Parameters:
        int length
    """
    def __init__(self, length=8):
        self.status = Environment.SUCCESS
        self.omega = [0 for _ in xrange(length + 1)] # int[]
        self.subterm = [None for _ in xrange(length + 1)] # visitable[]
        self.current = 0;
        self.omega[0] = 0;


    def ensureLength(self, minLength):
        """
        Returns void
        Parameters:
            minLength: int
        Java modifiers:
             private
        """
        if minLength > len(self.omega):
            n_to_add = max(len(self.omega)*2, minLength) - len(self.omega)
            self.omega.append([0 for _ in xrange(n_to_add)])
            self.subterm.append([None for _ in xrange(n_to_add)])
            
    def clone(self):
        """
        Returns Object
        Throws: 
            CloneNotSupportedException
        """
        # Environment
        clone = copy(self);
        clone.omega = self.omega[:]
        clone.subterm = self.subterm[:]
        clone.current = self.current;
        return clone

    def __eq__(self, o):
        """
        Returns boolean
        Parameters:
            o: Object
        """
        if isinstance(o,Environment):
            if self.current == o.current:
                for i in xrange(self.current+1):
                    if self.omega[i] != o.omega[i] or self.subterm[i] != o.subterm[i]:
                        return False
                return True
            else:
                return False
        else:
            return False

    def __hash__(self):
        """
        Returns int
        """
        hashedOmega = self.omega[:self.current+1]
        hashedSubterm = self.subterm[:self.current+1]
        return (self.current+1) * hashedOmega.__hash__ * hashedSubterm.__hash__


    def getStatus(self):
        """
        Returns int
        """
        return self.status


    def setStatus(self, s):
        """
        Returns void
        Parameters:
            s: int
        """
        self.status = s;


    def getRoot(self):
        """
        Returns Visitable
        """
        return self.subterm[0]


    def setRoot(self, root):
        """
        Returns void
        Parameters:
            root: Visitable
        """
        self.subterm[0] = root;


    def getSubject(self):
        """
        Returns Visitable
        """
        return self.subterm[self.current]


    def setSubject(self, root):
        """
        Returns void
        Parameters:
            root: Visitable

        """
        self.subterm[self.current] = root;


    def getSubOmega(self):
        """
        Returns int
        Java modifiers:
             private

        """
        return self.omega[self.current]


    def getPosition(self):
        """
        Returns Position

        """
        reducedOmega = self.omega[1:self.depth()+1]
        return Position(reducedOmega)


    def depth(self):
        """
        Returns int
        """
        return self.current


    def up(self):
        """
        Returns void

        """
        childIndex = self.omega[self.current] - 1
        child = self.subterm[self.current]
        self.current -= 1
        self.subterm[self.current] = self.subterm[self.current].setChildAt(childIndex, child)


    def upLocal(self):
        """
        Returns void

        """
        self.current -= 1

    def down(self, n):
        """
        Returns void
        Parameters:
            n: int
        """
        if n > 0: 
            child = self.subterm[self.current];
            self.current +=1
            if self.current == len(self.omega): 
                self.ensureLength(self.current+1)
            self.omega[self.current] = n
            self.subterm[self.current] = child.getChildAt(n-1)

    def followPath(self, path):
        """
        Returns void
        Parameters:
            path: Path
        """
        normalizedPath = path.getCanonicalPath();
        length = len(normalizedPath)
        for _ in xrange(length):
            head = normalizedPath.getHead();
            normalizedPath = normalizedPath.getTail();
            if head > 0: 
                self.down(head);
                if isinstance(self.subterm[self.current],Path) and len(normalizedPath) != 0: 
                    self.followPath(self.subterm[self.current])
            else:
                self.up()


    def followPathLocal(self, path):
        """
        Returns void
        Parameters:
            path: Path
        """
        normalizedPath = path.getCanonicalPath()
        length = len(normalizedPath)
        for _ in xrange(length):
            head = normalizedPath.getHead();
            normalizedPath = normalizedPath.getTail();
            if head > 0: 
                self.down(head);
                if isinstance(self.subterm[self.current],Path) and len(normalizedPath) != 0: 
                    self.followPath(self.subterm[self.current]);

            else:
                self.upLocal()

    def __str__(self):
        """
        Returns String

        """
        r = "%s %s" % (str(self.getPosition()), 
                       str(map(str,self.subterm[:self.current+1])))
        return r

class Identity(AbstractStrategy):

    def __init__(self):
        AbstractStrategy.__init__(self)
        self.initSubterm();


    def visitLight(self, x):
        """
        Returns Visitable
        Parameters:
            x: Visitable
        """
        return x


    def visit_noarg(self):
        """
        Returns int

        """
        return Environment.SUCCESS

class Fail(AbstractStrategy):

    def __init__(self,message=""):
        AbstractStrategy.__init__(self)
        self.initSubterm()
        self.message = message

    def visitLight(self, _):
        """
        Returns Visitable
        Parameters:
            any: Visitable
        Throws: 
            VisitFailure
        """
        raise VisitFailure(self.message)

    def visit_noarg(self):
        """
        Returns int

        """
        return Environment.FAILURE

class Not(AbstractStrategy):

    ARG = 0

    def __init__(self, v):
        """
        Parameters:
          Strategy v
        """
        AbstractStrategy.__init__(self)
        self.initSubterm(v);

    def visitLight(self, x):
        """
        Returns Visitable
        Parameters:
            x: Visitable
        Throws: 
            VisitFailure
        """
        try:
            self.visitors[self.ARG].visitLight(x);
        except VisitFailure:
            return x
        raise VisitFailure()

    def visit_noarg(self):
        """
        Returns int
        """
        subject = self.environment.getSubject();
        status = self.visitors[self.ARG].visit();
        self.environment.setSubject(subject);
        if status != Environment.SUCCESS: 
            return Environment.SUCCESS
        else:
            return Environment.FAILURE

class One(AbstractStrategy):

    ARG = 0

    def __init__(self, v):
        """
        Parameters:
            Strategy v
        """
        AbstractStrategy.__init__(self)
        self.initSubterm(v)


    def visitLight(self, any):
        """
        Returns Visitable
        Parameters:
            any: Visitable
        Throws: 
            VisitFailure
        """
        childCount = any.getChildCount()
        for i in xrange(childCount):
            try:
                newChild = self.visitors[self.ARG].visitLight(any.getChildAt(i))
                return any.setChildAt(i, newChild)
            except VisitFailure:
                pass
        raise VisitFailure()

    def visit_noarg(self):
        """
        Returns int

        """
        childCount = self.environment.getSubject().getChildCount();
        for i in xrange(childCount):
            self.environment.down(i+1);
            status = self.visitors[self.ARG].visit();
            if status == Environment.SUCCESS: 
                self.environment.up();
                return Environment.SUCCESS
            else:
                self.environment.upLocal();
        return Environment.FAILURE

class OneId(AbstractStrategy):

    ARG = 0

    def __init__(self, v):
        """
        Parameters:
           Strategy v
        """
        AbstractStrategy.__init__(self)
        self.initSubterm(v);

    def visitLight(self, any):
        """
        Returns Visitable
        Parameters:
            any: Visitable
        Throws: 
            VisitFailure
        """
        childCount = any.getChildCount();
        for i in xrange(childCount):
            newSubterm = self.visitors[self.ARG].visitLight(any.getChildAt(i));
            if newSubterm != any.getChildAt(i): 
                return any.setChildAt(i, newSubterm)
        return any

    def visit_noarg(self):
        """
        Returns int
        """
        childCount = self.environment.getSubject().getChildCount()
        for i in xrange(childCount):
            self.environment.down(i+1)
            oldSubject = self.environment.getSubject()
            status = self.visitors[self.ARG].visit()
            newSubject = self.environment.getSubject()
            if status == Environment.SUCCESS and oldSubject != newSubject: 
                self.environment.up()
                return Environment.SUCCESS
            else:
                self.environment.upLocal()
        return Environment.SUCCESS

class Sequence(AbstractStrategy):

    FIRST = 0
    THEN = 1
    def __init__(self, first, then):
        """
        Parameters:
          Strategy first
          Strategy then
        """
        AbstractStrategy.__init__(self)
        self.initSubterm(first, then);


    def visitLight(self, visitable):
        """
        Returns Visitable
        Parameters:
            visitable: Visitable
        Throws: 
            VisitFailure
        """
        return self.visitors[self.THEN].visitLight(self.visitors[self.FIRST].visitLight(visitable))

    def visit_noarg(self):
        """
        Returns int
        """
        status = self.visitors[self.FIRST].visit();
        if status == Environment.SUCCESS: 
            return self.visitors[self.THEN].visit()
        return status

class SequenceId(AbstractStrategy):

    FIRST = 0
    THEN = 1

    def __init__(self, first, then):
        """
        Parameters:
            Strategy first
            Strategy then
        """
        AbstractStrategy.__init__(self)
        self.initSubterm(first, then);

    def visitLight(self, visitable):
        """
        Returns Visitable
        Parameters:
            visitable: Visitable
        Throws: 
            VisitFailure
        """
        v = self.visitors[self.FIRST].visitLight(visitable)
        if v != visitable:
            return self.visitors[self.THEN].visitLight(v)
        else:
            return v

    def visit_noarg(self):
        """
        Returns int
        """
        subject = self.environment.getSubject()
        status = self.visitors[self.FIRST].visit()
        if status == Environment.SUCCESS and self.environment.getSubject() != subject: 
            return self.visitors[self.THEN].visit()
        return status

class Choice(AbstractStrategy):

    FIRST = 0
    THEN = 1

    def __init__(self, first, then):
        """
        Parameters:
          Strategy first
          Strategy then
        """
        AbstractStrategy.__init__(self)
        self.initSubterm(first, then);

    def visitLight(self, visitable):
        """
        Returns Visitable
        Parameters:
            visitable: Visitable
        Throws: 
            VisitFailure
        """
        try:
            return self.visitors[self.FIRST].visitLight(visitable)
        except VisitFailure:
            return self.visitors[self.THEN].visitLight(visitable)

    def visit_noarg(self):
        """
        Returns int
        """
        subject = self.environment.getSubject();
        status = self.visitors[self.FIRST].visit();
        if status == Environment.SUCCESS: 
            return status
        else:
            self.environment.setSubject(subject);
            return self.visitors[self.THEN].visit()

class ChoiceId(AbstractStrategy):

    FIRST = 0
    THEN = 1

    def __init__(self, first, then):
        """
        Parameters:
           Strategy first
           Strategy then
        """
        AbstractStrategy.__init__(self)
        self.initSubterm(first, then);

    def visitLight(self, visitable):
        """
        Returns Visitable
        Parameters:
            visitable: Visitable
        Throws: 
            VisitFailure
        """
        v = self.visitors[self.FIRST].visitLight(visitable);
        if v == visitable: 
            return self.visitors[self.THEN].visitLight(v)
        else:
            return v

    def visit_noarg(self):
        """
        Returns int
        """
        subject = self.environment.getSubject();
        status = self.visitors[self.FIRST].visit();
        if status == Environment.SUCCESS and self.environment.getSubject() != subject: 
            return status
        else:
            self.environment.setSubject(subject);
            if status == Environment.SUCCESS: 
                return self.visitors[self.THEN].visit()
            else:
                return status

class IfThenElse(AbstractStrategy):

    CONDITION = 0
    TRUE_CASE = 1
    FALSE_CASE = 2

    def __init__(self, c, t, f=Identity()):
        """
        Parameters:
          Strategy c
          Strategy t
          Strategy f
        """
        AbstractStrategy.__init__(self)
        self.initSubterm(c, t, f);

    def visitLight(self, x):
        """
        Returns Visitable
        Parameters:
            x: Visitable
        Throws: 
            VisitFailure
        """
        success = None
        result = None
        try:
            self.visitors[self.CONDITION].visitLight(x);
            success = True;
        except VisitFailure:
            success = False;
        if success: 
            result = self.visitors[self.TRUE_CASE].visitLight(x);
        else:
            result = self.visitors[self.FALSE_CASE].visitLight(x);
        return result

    def visit_noarg(self):
        """
        Returns int
        """
        subject = self.environment.getSubject();
        status = self.visitors[self.CONDITION].visit();
        self.environment.setSubject(subject);
        if status == Environment.SUCCESS: 
            return self.visitors[self.TRUE_CASE].visit()
        else:
            return self.visitors[self.FALSE_CASE].visit()

class Omega(AbstractStrategy):

    ARG = 0

    def __init__(self, indexPosition, v):
        """
        Parameters:
        int indexPosition
        Strategy v
        """
        AbstractStrategy.__init__(self)
        self.initSubterm(v)
        self.indexPosition = indexPosition


    def getPos(self):
        """
        Returns int
        """
        return self.indexPosition


    def visitLight(self, any):
        """
        Returns Visitable
        Parameters:
            any: Visitable

        Throws: 
            VisitFailure
        """
        if self.indexPosition == 0: 
            return self.visitors[self.ARG].visitLight(any)
        else:
            if self.indexPosition > 0 and self.indexPosition <= any.getChildCount(): 
                childNumber = self.indexPosition - 1
                newChild = self.visitors[self.ARG].visitLight(any.getChildAt(childNumber))
                return any.setChildAt(childNumber, newChild)
            else:
                raise VisitFailure()

    def visit_noarg(self):
        """
        Returns int

        """
        if self.indexPosition == 0: 
            return self.visitors[self.ARG].visit()
        else:
            if self.indexPosition > 0 and self.indexPosition <= self.environment.getSubject().getChildCount(): 
                self.environment.down(self.indexPosition)
                status = self.visitors[self.ARG].visit();
                self.environment.up();
                return status
            else:
                return Environment.FAILURE

    def str2(self,visited):
        visited.append(self)
        v = self.visitors[self.ARG]
        if v in visited:
            return "Omega(%d,%s)" % (self.indexPosition, repr(v))
        else:
            return "Omega(%d,%s)" % (self.indexPosition, v.str2(visited))

class All(AbstractStrategy):
    """
    Java modifiers:
         final static
    Type:
        int
    """
    ARG = 0

    def __init__(self, v):
        """
        Parameters:
            Strategy v
        """
        AbstractStrategy.__init__(self)
        self.initSubterm(v);

    def visitLight(self, any):
        """
        Returns Visitable
        Parameters:
            any: Visitable
        Java modifiers:
             final
        Throws: 
            VisitFailure
        """
        childCount = any.getChildCount()
        result = any
        children = None
        for i in xrange(childCount):
            oldChild = any.getChildAt(i)
            newChild = self.visitors[self.ARG].visitLight(oldChild)
            if children != None: 
                children[i] = newChild
            elif newChild != oldChild:
                children = any.getChildren()
                children[i] = newChild
        if children != None:
            result = any.setChildren(children)
        return result

    def visit_noarg(self):
        """
        Returns int
        """
        any = self.environment.getSubject()
        childCount = any.getChildCount()
        children = None
        for i in xrange(childCount):
            oldChild = any.getChildAt(i)
            self.environment.down(i+1)
            status = self.visitors[self.ARG].visit()
            if status != Environment.SUCCESS: 
                self.environment.upLocal()
                return status

            newChild = self.environment.getSubject()
            if children != None: 
                children[i] = newChild
            else:
                if newChild != oldChild: 
                    children = any.getChildren()
                    children[i] = newChild

            self.environment.upLocal()

        if children != None: 
            self.environment.setSubject(any.setChildren(children))

        return Environment.SUCCESS

class AllSeq(AbstractStrategy):

    ARG = 0

    def __init__(self, v):
        """
        Parameters:
          Strategy v
        """
        AbstractStrategy.__init__(self)
        self.initSubterm(v);

    def visitLight(self, any):
        """
        Returns Visitable
        Parameters:
            any: Visitable
        Java modifiers:
             final
        Throws: 
            VisitFailure
        """
        childCount = any.getChildCount();
        result = any
        for i in xrange(childCount):
            newChild = self.visitors[self.ARG].visitLight(result.getChildAt(i));
            result = result.setChildAt(i,newChild);
        return result

    def visit_noarg(self):
        """
        Returns int
        """
        childCount = self.environment.getSubject().getChildCount();
        for _ in xrange(childCount):
            status = self.visitors[self.ARG].visit();
            if status != Environment.SUCCESS: 
                self.environment.up();
                return status
            else:
                self.environment.up();
        return Environment.SUCCESS
