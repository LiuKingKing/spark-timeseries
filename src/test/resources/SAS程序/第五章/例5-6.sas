goptions vsize=6.8cm hsize=10cm;
data a;
input year	GNP;
dif=dif(GNP);
cards;
1889	25.9
1890	25.4
1891	24.9
1892	24
1893	24.5
1894	23
1895	22.7
1896	22.1
1897	22.2
1898	22.9
1899	23.6
1900	24.7
1901	24.5
1902	25.4
1903	25.7
1904	26
1905	26.5
1906	27.2
1907	28.3
1908	28.1
1909	29.1
1910	29.9
1911	29.7
1912	30.9
1913	31.1
1914	31.4
1915	32.5
1916	36.5
1917	45
1918	52.6
1919	53.8
1920	61.3
1921	52.2
1922	49.5
1923	50.7
1924	50.1
1925	51
1926	51.2
1927	50
1928	50.4
1929	50.6
1930	49.3
1931	44.8
1932	40.2
1933	39.3
1934	42.2
1935	42.6
1936	42.7
1937	44.5
1938	43.9
1939	43.2
1940	43.9
1941	47.2
1942	53
1943	56.8
1944	58.2
1945	59.7
1946	66.7
1947	74.6
1948	79.6
1949	79.1
1950	80.2
1951	85.6
1952	87.5
1953	88.3
1954	89.6
1955	90.9
1956	94
1957	97.5
1958	100
1959	101.6
1960	103.3
1961	104.6
1962	105.8
1963	107.2
1964	108.8
1965	110.9
1966	113.9
1967	117.6
1968	122.3
1969	128.2
1970	135.3
;
proc gplot;
plot GNP*year=1;
plot dif*year=1;
symbol1 c=red v=circle i=join;
proc arima data=a;
identify var=GNP(1) stationarity=(Adf=2) ;
estimate p=1;
forecast id=year lead=10 out=out;
proc gplot data=out;
plot GNP*year=1 forecast*year=2 l95*year=3 u95*year=3/overlay;
symbol1 c=black i=none v=star;
symbol2 c=red i=join v=none;
symbol3 c=green i=join v=none l=3;
run;
