goptions vsize=6.8cm hsize=10cm;
data example6_1;
input x@@;
t=intnx('quarter','1jan1978'd,_n_-1);
format t yyq4.;
cards;
40777      41778      43160      45897
41947      44061      44378      47237
43315      43396      44843      46835
42833      43548      44637      47107
42552      43526      45039      47940
43740      45007      46667      49325
44878      46234      47055      50318
46354      47260      48883      52605
48527      50237      51592      55152
50451      52294      54633      58802
53990      55477      57850      61978
;
proc x11 data=example6_1;
quarterly date=t;
var x;
output  out=out  b1=x d10=season d11=adjusted d12=trend d13=irr;
data out;
set out;
estimate=trend*season/100;
proc gplot data=out;
plot season*t=2 adjusted*t=2 trend*t=2 irr*t=2;
plot x*t=1 trend*t=2 /overlay;
symbol1 c=black i=join v=star;
symbol2 c=red i=join v=none w=2;
run;

proc forecast data=example6_1 interval=qtr lead=8 method=winters trend=2
     weight=(0.4,0.15,0.3) seasons=4 out=out2 outfull  outest=test;
id t;
var x;
proc gplot data=out2;
plot x*t=_type_/href='01jan1989'd;
symbol1 c=black v=star i=join;
symbol2 i=spline v=none c=red w=1;
symbol3 i=spline v=none l=3  c=blue;
symbol4 i=spline v=none l=3  c=blue;
run;

proc arima data=example6_1;
identify var=x(1,4);
estimate p=(4) noint;
forecast lead=0 id=t out=out3;
run;
proc gplot data=out3;
plot x*t=1 forecast*t=2 /overlay;
symbol1 c=black i=join v=star;
symbol2 c=red i=join v=none;
run;
