goptions vsize=7cm hsize=11cm;

data a;
e_1=0;
e_2=0;
do t=-100 to 1000;
e=rannor(12345);
x1=e-2*e_1;
x2=e-0.5*e_1;
x3=e-4/5*e_1+16/25*e_2;
x4=e-5/4*e_1+25/16*e_2;
e_2=e_1;
e_1=e;
if t>0 then output ;
end;
data a;
set a;
keep t x1 x2 x3 x4;
proc arima data=a;
identify var=x1 nlag=20 outcov=out1;
identify var=x2 nlag=20 outcov=out2;
identify var=x3 nlag=20 outcov=out3;
identify var=x4 nlag=20 outcov=out4;
symbol c=red i=needle v=none;
proc gplot data=out1;
plot corr*lag ;
proc gplot data=out2;
plot corr*lag ;
proc gplot data=out3;
plot corr*lag ;
proc gplot data=out4;
plot corr*lag ;
proc gplot data=out1;
plot partcorr*lag ;
proc gplot data=out2;
plot partcorr*lag ;
proc gplot data=out3;
plot partcorr*lag ;
proc gplot data=out4;
plot partcorr*lag ;
run;
