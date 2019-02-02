//import "./Tests/utils/global.tl";
varDecFunction = function(number num, string str){
  return num + str;
};
transfer = @varDecFunction;
assert(varDecFunction(7,"r") == "7.0r");
assert(transfer(3,"f") == "3.0f");
varDecFunction = function(number num, string str){
  return str + num;
};
assert(varDecFunction(7,"r") == "r7.0");

scope();
