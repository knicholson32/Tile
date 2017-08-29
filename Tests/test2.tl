import "./Tests/utils/global.tl";

s = "this is a test";
a = strBreak(s);
println(a);
b = strCombine(a);
println(b);

c = "123.45678901E13";

println(toNumber(c));

println(canBeNumber("123.445"));
println(canBeNumber("123"));

if (-5 > -6) {
  println("t");
} else {
  println("a");
}
