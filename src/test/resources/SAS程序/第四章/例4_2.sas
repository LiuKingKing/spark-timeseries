goptions vsize=6.8cm hsize=10cm;
data a;
input day	overshort;
cards;
1 	78 
2 	-58 
3 	53 
4 	-63 
5 	13 
6 	-6 
7 	-16 
8 	-14 
9 	3 
10 	-74 
11 	89 
12 	-48 
13 	-14 
14 	32 
15 	56 
16 	-86 
17 	-66 
18 	50 
19 	26 
20 	59 
21 	-47 
22 	-83 
23 	2 
24 	-1 
25 	124 
26 	-106 
27 	113 
28 	-76 
29 	-47 
30 	-32 
31 	39 
32 	-30 
33 	6 
34 	-73 
35 	18 
36 	2 
37 	-24 
38 	23 
39 	-38 
40 	91 
41 	-56 
42 	-58 
43 	1 
44 	14 
45 	-4 
46 	77 
47 	-127 
48 	97 
49 	10 
50 	-28 
51 	-17 
52 	23 
53 	-2 
54 	48 
55 	-131 
56 	65 
57 	-17 
;
proc gplot;
plot overshort*day=1;
symbol1 c=red v=none i=join;
proc arima data=a;
identify var=overshort stationarity=(adf=2);
estimate q=1 ;
run;
