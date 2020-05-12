data example1_3;
input price ; 
logprice=log(price);
time=intnx('month','01jan2005'd,_n_-1);
format time monyy.;
cards;
3.41                                                                                                                      
3.45                                                                                                                      
3.42                                                                                                                      
3.53                                                                                                                      
3.45                                                                                                                      
;                                                                                                                                       
proc print data= example1_3 ;                                                                                                                     
run;

data example1_4;                                                                                                             
set example1_3;    
keep time logprice;                                                                                                                              
where time>='01mar2005'd;                                                                                                    
proc print data= example1_4;                                                                                                                      
run;


