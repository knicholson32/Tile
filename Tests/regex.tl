import "./Tests/utils/global.tl";
sys_regex_set("\\G[0-9]+(\\.[0-9]+)?$");

b = sys_regex_match("10.2");
c = sys_regex_match("10.2.");

eval("t = 9;");

d = -5 > -6;
e = -2 == 4;
f = -1+1==0;


scope();

eval("scope()");
x = 1E-5;
scope();
assert(1E-5 == 0.00001);
