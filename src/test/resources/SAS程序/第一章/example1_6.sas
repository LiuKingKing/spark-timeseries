goptions vsize=6.8cm hsize=10cm;
data example1_6;
input  price1 price2;
time=intnx('month','1jan2004'd,_n_-1);
format time date.;
cards;
12.85 15.31
13.29 14.23
12.41 14.69
15.21 13.27
14.23 16.75
13.56 15.33
;
proc gplot data=example1_6;
plot price1*time=1 price2*time=2/overlay;
symbol1 c=black v=star i=join;
symbol2 c=red v=square i=spline;
run;
