goptions vsize=6.8cm hsize=10cm;
data a;
input rain;
time=intnx('month','01jan1970'd,_n_-1);
format time monyy5.;
cards;
0
0
0
0
0
11
22
48
45
0
0
0
0
0
0
0
4
31
31
25
20
5
0
0
0
0
0
0
1
7
1
39
7
0
0
0
0
0
0
0
12
66
7
73
14
4
0
0
0
0
0
0
0
22
34
16
6
0
0
0
0
0
0
0
8
12
10
7
13
0
0
0
0
0
0
0
27
19
32
54
0
0
0
0
;
run;
proc gplot data=a;
plot rain*time=1;
symbol1 i=join v=star c=red;
run;
