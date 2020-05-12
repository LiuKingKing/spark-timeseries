data a;
input year	w	cpi	u	mw;
lagw=lag(w);
lagcpi=lag(cpi);
lagu=lag(u);
lagmw=lag(mw);
cards;
1962	2.8	1.477	6.457	15
1963	2.7	1.12	6.192	0
1964	2.7	1.107	5.763	8.696
1965	2.2	1.424	5.018	0
1966	2.9	1.188	4.17	0
1967	4.5	2.775	3.725	12
1968	5.1	2.7	3.723	14.286
1969	5.5	3.943	3.686	0
1970	6.2	5.058	3.556	0
1971	6.2	6.019	5.395	0
1972	6.3	4.629	6.272	0
1973	5.5	3.506	5.433	0
1974	6.2	4.677	4.522	0
1975	9.1	10.247	6.163	31.25
1976	7.6	10.273	8.625	9.524
1977	6.9	6.147	7.877	0
1978	7.5	6.388	6.679	15.217
1979	7.2	6.51	5.494	9.434
;
run;
proc gplot data=a;
plot w*year=1 cpi*year=1 u*year=1 mw*year=1;
symbol1 c=red i=join v=star;
run;

proc arima data=a;
identify var=w stationarity=(adf);
identify var=cpi stationarity=(adf);
identify var=u stationarity=(adf);
identify var=mw stationarity=(adf);
identify var=w(1) stationarity=(adf);
identify var=cpi(1) stationarity=(adf);
identify var=u(1) stationarity=(adf);
run;
proc arima data=a;
identify var=w(1) crosscorr=(cpi(1) u(1) mw);
proc autoreg data=a;
model w=lagw cpi lagu mw lagmw;
model w=lagw  lagu mw lagmw;
model w=lagw  cpi  mw lagmw;
model w=lagw cpi lagu;
run;

