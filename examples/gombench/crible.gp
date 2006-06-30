set title "Crible d'Ératostène"
set out "crible.pdf"
set terminal pdf
plot "crible.dat" using 1:2 title 'Manual' with linespoint, \
     "crible.dat" using 1:3 title 'Apigen' with linespoint, \
     "crible.dat" using 1:4 title 'Gom' with linespoint
