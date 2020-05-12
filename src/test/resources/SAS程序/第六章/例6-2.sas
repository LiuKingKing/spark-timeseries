/* x1=m12,x2=m2*12 y=x/x2 I=y/S */
goptions vsize=6.8cm hsize=10cm;
data a;
input x	x1	x2 y S I;
time=intnx("month","01jan1993"d,_n_-1);
format time monyy5.;
S_I=x/x2;
cards;
977.5	.	.	.	.	.
892.5	.	.	.	.	.
942.3	.	.	.	.	.
941.3	.	.	.	.	.
962.2	.	.	.	.	.
1005.7	.	.	.	.	.
963.8	1019.75	1028.695833	0.936914459	0.928660342	1.008888198
959.8	1037.641667	1048.9	0.915053866	0.926080696	0.988093014
1023.3	1060.158333	1069.541667	0.956764969	0.981428984	0.974869282
1051.1	1078.925	1088.470833	0.965666665	1.007497006	0.958480928
1102	1098.016667	1108.495833	0.994139957	1.047240268	0.949295006
1415.5	1118.975	1130.45	1.252156221	1.269449281	0.98637751
1192.2	1141.925	1153.9125	1.033180592	1.043902981	0.989728558
1162.7	1165.9	1179.491667	0.985763641	0.993943916	0.991769882
1167.5	1193.083333	1208.620833	0.965977061	0.95926263	1.006999575
1170.4	1224.158333	1240.533333	0.943465176	0.939764448	1.003937932
1213.7	1256.908333	1275.733333	0.951374373	0.943889676	1.007929631
1281.1	1294.558333	1316.0875	0.973415521	0.958879773	1.015159094
1251.5	1337.616667	1354.7	0.923820772	0.928660342	0.994788655
1286	1371.783333	1385.483333	0.928195936	0.926080696	1.002284078
1396.2	1399.183333	1414.425	0.987114905	0.981428984	1.005793512
1444.1	1429.666667	1445.429167	0.999080435	1.007497006	0.991646058
1553.8	1461.191667	1476.679167	1.052225856	1.047240268	1.004760691
1932.2	1492.166667	1507.108333	1.282057804	1.269449281	1.009932278
1602.2	1522.05	1537.554167	1.042044589	1.043902981	0.998219766
1491.5	1553.058333	1567.6875	0.951401348	0.993943916	0.95719822
1533.3	1582.316667	1597.308333	0.959927378	0.95926263	1.000692978
1548.7	1612.3	1627.879167	0.951360538	0.939764448	1.012339359
1585.4	1643.458333	1659.35	0.955434357	0.943889676	1.012230965
1639.7	1675.241667	1694.295833	0.967776682	0.958879773	1.009278441
1623.6	1713.35	1726.1375	0.940597142	0.928660342	1.012853785
1637.1	1738.925	1756.4125	0.932070342	0.926080696	1.006467737
1756	1773.9	1787.516667	0.982368463	0.981428984	1.000957256
1818	1801.133333	1813.8875	1.00226723	1.007497006	0.99480914
1935.2	1826.641667	1839.679167	1.05192255	1.047240268	1.004471067
2389.5	1852.716667	1866.3125	1.280332206	1.269449281	1.00857295
1909.1	1879.908333	1890.954167	1.009596125	1.043902981	0.967135973
1911.2	1902	1913.6375	0.998726248	0.993943916	1.00481147
1860.1	1925.275	1938.920833	0.959348091	0.95926263	1.000089091
1854.8	1952.566667	1966.329167	0.943280521	0.939764448	1.003741441
1898.3	1980.091667	1994.879167	0.951586458	0.943889676	1.008154324
1966	2009.666667	2028.795833	0.969047732	0.958879773	1.010603998
1888.7	2047.925	2063.733333	0.915186071	0.928660342	0.985490636
1916.4	2079.541667	2092.1375	0.91600098	0.926080696	0.989115726
2083.5	2104.733333	2116.016667	0.984633076	0.981428984	1.003264721
2148.3	2127.3	2137.5375	1.005034999	1.007497006	0.997556314
2290.1	2147.775	2156.520833	1.061941978	1.047240268	1.014038526
2848.6	2165.266667	2173.545833	1.310577378	1.269449281	1.032398378
2288.5	2181.825	2190.733333	1.04462737	1.043902981	1.000693924
2213.5	2199.641667	2207.475	1.002729363	0.993943916	1.008838976
2130.9	2215.308333	2221.8125	0.959081831	0.95926263	0.999811523
2100.5	2228.316667	2236.6375	0.939132962	0.939764448	0.999328038
2108.2	2244.958333	2251.825	0.936218401	0.943889676	0.991872699
2164.7	2258.691667	2260.070833	0.957801839	0.958879773	0.99887584
2102.5	2261.45	2272.325	0.925263772	0.928660342	0.996342505
2104.4	2283.2	2287.070833	0.920128913	0.926080696	0.993573149
2239.6	2290.941667	2297.141667	0.974950754	0.981428984	0.993399186
2348	2303.341667	2309.683333	1.016589576	1.007497006	1.00902491
2454.9	2316.025	2322.566667	1.056977195	1.047240268	1.009297701
2881.7	2329.108333	2335.829167	1.233694673	1.269449281	0.971834552
2549.5	2342.55	2350.2	1.084801294	1.043902981	1.03917827
2306.4	2357.85	2366.608333	0.974559232	0.993943916	0.980497205
2279.7	2375.366667	2383.845833	0.956311842	0.95926263	0.9969239
2252.7	2392.325	2400.158333	0.938563081	0.939764448	0.99872163
2265.2	2407.991667	2416.2125	0.937500323	0.943889676	0.993230827
2326	2424.433333	2434.8375	0.955299892	0.958879773	0.996266601
2286.1	2445.241667	2449.933333	0.933127432	0.928660342	1.004810252
2314.6	2454.625	2464.291667	0.939255702	0.926080696	1.014226629
2443.1	2473.958333	2479.1	0.985478601	0.981428984	1.004126245
2536	2484.241667	2488.579167	1.019055385	1.007497006	1.01147237
2652.2	2492.916667	2497.033333	1.062140407	1.047240268	1.014228004
3131.4	2501.15	2505.433333	1.249843673	1.269449281	0.984555816
2662.1	2509.716667	2513.641667	1.059061057	1.043902981	1.014520579
2538.4	2517.566667	2521.579167	1.006670754	0.993943916	1.012804382
2403.1	2525.591667	2532.308333	0.948976066	0.95926263	0.989276593
2356.8	2539.025	2547.6875	0.925074209	0.939764448	0.98436817
2364	2556.35	2561.7375	0.922811178	0.943889676	0.977668473
2428.8	2567.125	2578.554167	0.941923203	0.958879773	0.982316271
2380.3	2589.983333	2594.675	0.917378862	0.928660342	0.987851878
2410.9	2599.366667	2610.475	0.923548396	0.926080696	0.997265574
2604.3	2621.583333	2630.9125	0.989884688	0.981428984	1.008615706
2743.9	2640.241667	2649.208333	1.035743382	1.007497006	1.028036189
2781.5	2658.175	2669.55	1.041935907	1.047240268	0.994934914
3405.7	2680.925	2689.933333	1.266090857	1.269449281	0.997354425
2774.7	2698.941667	2707.970833	1.02464176	1.043902981	0.98154884
2805	2717	2726.379167	1.028837087	0.993943916	1.035105774
2627	2735.758333	2746.1625	0.95660763	0.95926263	0.997232249
2572	2756.566667	2768.445833	0.929041114	0.939764448	0.988589339
2637	2780.325	2793.929167	0.943832088	0.943889676	0.999938989
2645	2807.533333	2818.9625	0.938288466	0.958879773	0.978525664
2597	2830.391667	.	.	.	.
2636	.	.	.	.	.
2854	.	.	.	.	.
3029	.	.	.	.	.
3108	.	.	.	.	.
3680	.	.	.	.	.
;
run;
proc gplot data=a;
plot x*time=1;
plot x*time=1 x2*time=2/overlay;
plot S_I*time=2;
plot I*time=2;
symbol1  c=black i=join v=star;
symbol2 c=red i=join v=none;
run;
data b;
input month S;
cards;
1 1.043902981
2 0.993943916
3 0.95926263
4 0.939764448
5 0.943889676
6 0.958879773
7 0.928660342
8 0.926080696
9 0.981428984
10 1.007497006
11 1.047240268
12 1.269449281
run;
proc gplot data=b;
plot s*month=1;
run;

proc x11 data=a;
monthly date=time;
var x;
output  out=out  b1=x d10=season d11=adjusted d12=trend d13=irr;
data out;
set out;
estimate=trend*season/100;
proc gplot data=out;
plot season*time=2 adjusted*time=2 trend*time=2 irr*time=2;
plot x*time=1 trend*time=2 /overlay;
plot x*time=1 estimate*time=2 /overlay;
symbol1 c=black i=join v=star;
symbol2 c=red i=join v=none w=2;
run;

proc forecast data=a interval=month lead=12 method=winters trend=2 seasons=12 out=out outfull outest=outest;
id time;
var x;
proc gplot data=out;
plot x*time=_type_;
symbol1 c=black v=star i=none;
symbol2 i=spline v=none c=red w=1.5;
symbol3 i=spline v=none l=3 r=1 c=blue;
symbol4 i=spline v=none l=3 r=1 c=blue;
run;
