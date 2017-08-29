println("--Math Tests");

b = 9;
a = [1,4,2,3];

assert((b--) == 8);
assert(b == 8);
assert((b-= a[2]) == 6);
assert(b == 6);
assert((b-= -3) == 9);
b = b * 4;
b *= 2;
println(b/=4);
assert(b==18);

println("--Assertions Complete");
