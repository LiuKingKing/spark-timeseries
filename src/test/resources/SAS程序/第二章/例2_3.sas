goptions vsize=6.8cm hsize=10cm;
data a;
input percent;
year=intnx('year','01jan1978'd,_n_-1);
format year year4.;
cards;
23.9
21.6
21.6
22.0
21.8
22.4
24.8
28.7
29.1
29.6
30.5
32.1
31.5
33.7
34.8
33.7
33.6
32.9
32.8
34.2
36.2
37.8
39.0
40.5
41.5
41.2
40.4
40.5
40.9
41.9
41.8
43.4
43.2
43.4
44.6
;
proc gplot;
plot percent*year=1;
symbol1 c=red v=star i=join;
run;
