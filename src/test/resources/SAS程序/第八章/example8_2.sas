goptions vsize=7cm hsize=11cm;
data example8_2;
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
proc x12 data=example8_2 start='78q1' seasons=4;
   id t;
   var x; 
   transform function=none; 
   regression predefined=(loq seasonal);
   identify printreg;
   run;
proc x12 data=example8_2 start='78q1' seasons=4;
   id t;
   var x; 
   transform function=none; 
   identify diff=(0,1) sdiff=(0,1);
   arima model=((1,1,0)(1,1,0)4);
   estimate;
   run;
  proc x12 data=example8_2 start='78q1' seasons=4;
   id t;
   var x; 
   transform function=none; 
   identify diff=(0,1) sdiff=(0,1);
   arima model=((0,1,0)(1,1,0)4);
   estimate;
   x11 mode=mult;
   output out=out b1 d10 d11 d12 d13; 
run ; 
proc gplot data=out;
plot x_b1*t=1 x_d10*t=1 x_d12*t=1 x_d13*t=1/href='1jan1960'd;
symbol1 c=red v=none i=join;
run;
