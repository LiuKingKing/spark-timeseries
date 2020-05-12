goptions vsize=6.8cm hsize=10cm;
data a; 
   input ozone @@; 
   date = intnx( 'month', '1jan1955'd, _n_ -1); 
   format date monyy.; 
   month = month( date ); 
   year = year( date ); 
   x1 = year >= 1960; 
   x2 =  5 < month < 11;
   x3 = 1 -x2; 
   cards; 
2.7  2.0  3.6  5.0  6.5  6.1  5.9  5.0  6.4  7.4  8.2  3.9 
4.1  4.5  5.5  3.8  4.8  5.6  6.3  5.9  8.7  5.3  5.7  5.7 
3.0  3.4  4.9  4.5  4.0  5.7  6.3  7.1  8.0  5.2  5.0  4.7 
3.7  3.1  2.5  4.0  4.1  4.6  4.4  4.2  5.1  4.6  4.4  4.0 
2.9  2.4  4.7  5.1  4.0  7.5  7.7  6.3  5.3  5.7  4.8  2.7 
1.7  2.0  3.4  4.0  4.3  5.0  5.5  5.0  5.4  3.8  2.4  2.0 
2.2  2.5  2.6  3.3  2.9  4.3  4.2  4.2  3.9  3.9  2.5  2.2 
2.4  1.9  2.1  4.5  3.3  3.4  4.1  5.7  4.8  5.0  2.8  2.9 
1.7  3.2  2.7  3.0  3.4  3.8  5.0  4.8  4.9  3.5  2.5  2.4 
1.6  2.3  2.5  3.1  3.5  4.5  5.7  5.0  4.6  4.8  2.1  1.4 
2.1  2.9  2.7  4.2  3.9  4.1  4.6  5.8  4.4  6.1  3.5  1.9 
1.8  1.9  3.7  4.4  3.8  5.6  5.7  5.1  5.6  4.8  2.5  1.5 
1.8  2.5  2.6  1.8  3.7  3.7  4.9  5.1  3.7  5.4  3.0  1.8 
2.1  2.6  2.8  3.2  3.5  3.5  4.9  4.2  4.7  3.7  3.2  1.8 
2.0  1.7  2.8  3.2  4.4  3.4  3.9  5.5  3.8  3.2  2.3  2.2 
1.3  2.3  2.7  3.3  3.7  3.0  3.8  4.7  4.6  2.9  1.7  1.3 
1.8  2.0  2.2  3.0  2.4  3.5  3.5  3.3  2.7  2.5  1.6  1.2 
1.5  2.0  3.1  3.0  3.5  3.4  4.0  3.8  3.1  2.1  1.6  1.3 
. . . . . . . . . . . .
; 
run;
proc gplot data=a;
plot ozone*date=1/href='1jan1960'd;
symbol1 c=red v=none i=join;
run;
proc arima data=a(where=(date < 1960));
identify var=ozone;
identify var=ozone(12) stationarity=(adf);
estimate q=(1)(12);
run;
proc arima data=a; 
    identify var=ozone crosscorr=( x1  x2 ) ; 
    identify var=ozone(12)  crosscorr=( x1(12)  x2 ) ; 
	estimate input=(x1 x2)   p=1 q=(12) noconstant method=ml; 
    forecast  lead=12 id=date interval=month ; 
	run; 

proc x12 data=a date=date;
   var ozone; 
   transform function=none; 
   transform power=1; 
   regression uservar=(x1); 
   identify sdiff=(1);
   arima model=((1 0 0)(0 1 1)12);
   estimate;
   x11;  
   output out=out b1 d10 d11 d12 d13; 
run ; 
data out;
set out;
label ozone_b1="ozone";
label ozone_d10="Season";
label ozone_d12="Tread";
label ozone_d13="Rondom";
proc gplot data=out;
plot ozone_b1*date ozone_d10*date=1 ozone_d12*date=1 ozone_d13*date=1/href='1jan1960'd;
run;
