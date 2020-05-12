goptions vsize=6.8cm hsize=10cm;
data a;
input year temp;
cards;
1949	38.8
1950	35.6
1951	38.3
1952	39.6
1953	37
1954	33.4
1955	39.6
1956	34.6
1957	36.2
1958	37.6
1959	36.8
1960	38.1
1961	40.6
1962	37.1
1963	39
1964	37.5
1965	38.5
1966	37.5
1967	35.8
1968	40.1
1969	35.9
1970	35.3
1971	35.2
1972	39.5
1973	37.5
1974	35.8
1975	38.4
1976	35
1977	34.1
1978	37.5
1979	35.9
1980	35.1
1981	38.1
1982	37.3
1983	37.2
1984	36.1
1985	35.1
1986	38.5
1987	36.1
1988	38.1
1989	35.8
1990	37.5
1991	35.7
1992	37.5
1993	35.8
1994	37.2
1995	35
1996	36
1997	38.2
1998	37.2
;
proc gplot;
plot temp*year=1;
symbol c=red i=join;
run;
proc forecast data=a lead=20 method=expo trend=1  out=out outfull; 
id year;
var temp;
run;
proc gplot data=out ;
plot temp*year=_type_ ;
symbol1 c=black v=star i=join;
symbol2 i=spline v=none c=red w=1;
symbol3 i=spline v=none l=3 r=1 c=blue;
symbol4 i=spline v=none l=3 r=1 c=blue;
run;
