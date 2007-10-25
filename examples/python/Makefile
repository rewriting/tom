all: test.py
	python test.py

test.py: test.py.t
	tom -pCode test.py.t -o test.py
	./pindent -r test.py
	@echo ""

test.py.t: test.py.t.before_indent
	cp test.py.t.before_indent test.py.t
	./pindent -c test.py.t

clean:
	rm test.py test.py.t *~ *.pyc

