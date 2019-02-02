import "./Tests/utils/global.tl";
sys_regex_set("\\G[0-9]+(\\.[0-9]+)?$");

b = sys_regex_match("10.2");
c = sys_regex_match("10.2.");

eval("t = 9;");

d = -5 > -6;
e = -2 == 4;
f = -1+1==0;


//scope();

//eval("scope()");
//x = 1E-5;
//scope();
i = 0;
j = 0;
for(z=1:10){

  while(i < 30){
    j++;
    if(i == 10){
      break;
    }
    if(j == 7){
      continue;
    }
    i++;
  }
  if(z > 5){
    i = i + 4;
    break;
  }
}

scope();
