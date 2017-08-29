a = "string here";
b = "another string";

assert(a != b);
assert(a.substring(0,1) == "s");
assert(a.substring(0,5) == "strin");
assert(a.substring(5,7) == "g ");
assert(a.substring(1) == "tring here");
assert(b.substring(4) == "her string");

assert("test".substring(1) == "est");
assert("test".substring(2,2) == "");


assert(a.length() == 11);

assert("test".substring(1).length() == 3);


assert(a.indexOf("t") == 1);
assert(a.indexOf("ing") == 3);
assert(a.indexOf("lol") == -1);
assert(a.indexOf("here") == 7);
assert(a.indexOf("e") == 8);
assert(a.indexOf("e", 9) == 10);
assert(a.indexOf("e", a.indexOf("e") + 1) == 10);
assert("abcfedghie".indexOf("e", "abcfedghie".indexOf("e") + 1) == 9);

assert(b.substring(0,b.indexOf(" ")) == "another");



println("Assertions Complete");
