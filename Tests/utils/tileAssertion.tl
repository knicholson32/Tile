/*
    A script for testing Tile Language syntax.
    This is used to ensure the language passes.


    COPYRIGHT 2017 ROUNDPIXEL
*/
import "./Tests/utils/global.tl";
println("> Imports: Passed");
// boolean expressions
assert(true);
assert(true || false);
assert(!false);
assert(true && true);
assert(!true || !false);
assert(true && (true || false));
println("> Boolean Expressions: Passed");
// relational expressions
assert(1 < 2);
assert(666 >= 666);
assert(-5 > -6);
assert(0 >= -1);
assert('a' < 's');
assert('sw' <= 'sw');
println("> Rational Expressions: Passed");

// add
assert(1 + 999 == 1000);
assert([1] + 1 == [1,1]);
assert(2 - -2 == 4);
assert(-1 + 1 == 0);
assert(1 - 50 == -49);
println("> Addition Operations: Passed");

// multiply
assert(3 * 50 == 150);
assert(4 / 2 == 2);
assert(1 / 4 == 0.25);
assert(999999 % 3 == 0);
assert(-5 * -5 == 25);
println("> Multiplication Operations: Passed");

// power
assert(2^10 == 1024);
assert(3^3 == 27);
println("> Power Operations: Passed");

// for- and while statements
a = 0;
for (i=1 : 10) {
  a = a + i;
}
assert(a == (1+2+3+4+5+6+7+8+9+10));

b = -10;
c = 0;
while (b < 0) {
  c = c + b;
  b = b + 1;
}
assert(c == -(1+2+3+4+5+6+7+8+9+10));
println("> Loop Statements: Passed");

// if
a = 123;
if (a > 200) {
  assert(false);
}

if (a < 100) {
  assert(false);
} else if (a > 124) {
  assert(false);
} else if (a < 124) {
  assert(true);
} else {
  assert(false);
}

if (false) {
  assert(false);
} else {
  assert(true);
}
println("> If Statements: Passed");

// functions
function twice(n) {
  temp = n + n;
  return temp;
}

function squared(n) {
  return n*n;
}

function squaredAndTwice(n) {
  return twice(squared(n));
}

function lt() {
  return [7,8,9];
}

function overrd(number val) {
  return 1;
}

function overrd(string val) {
  return 2;
}

function overrd(number val, number val2) {
  return 3;
}

function overrd(string val, number val2) {
  return 4;
}

function overrd(number val, string val2) {
  return 5;
}

assert(overrd(1) == 1);
assert(overrd("test") == 2);
assert(overrd(1,2) == 3);
assert(overrd("test",1) == 4);
assert(overrd(1,"test") == 5);

assert(squared(666) == 666^2);
assert(twice(squared(5)) == 50);
assert(squaredAndTwice(10) == 200);
assert(squared(squared(squared(2))) == 2^2^2^2);
assert(lt() == [7,8,9]);
assert(size(lt()) == 3);
assert(lt()[1] == 8);
println("> Functions: Passed");

// naive bubble sort
function sort(lt) {
  while (!sorted(lt)){}
}
function sorted(lt) {

  n = size(lt);
  for (i=0 to n-2) {
    if (lt[i] > lt[i+1]) {
      temp = lt[i+1];
      lt[i+1] = lt[i];
      lt[i] = temp;
      return false;
    }
  }
  return true;
}
numbers = [3,5,1,4,2];
sort(numbers);
assert(numbers == [1,2,3,4,5]);
println("> Sort: Passed");

// resursive calls
function fib(n) {
  if (n < 2) {
    return n;
  } else {
    return fib(n-2) + fib(n-1);
  }
}
sequence = [];
for (i = 0 to 10) {
  sequence = sequence + fib(i);
}
assert(sequence == [0,1,1,2,3,5,8,13,21,34,55]);
println("> Recursion: Passed");

// lists and lookups, `in` operator
n = [[1,0,0],[0,1,0],[0,0,1]];
p = [-1, 'abc', true];

assert('abc' in p);
assert([0,1,0] in n);
assert(n[0][2] == 0);
assert(n[1][1] == n[2][2]);
assert(p[2]);
assert(p[1][2] == 'c');
println("> List Operations: Passed");

// Math operators
assert(.123 == 0.123);
assert(.123E5 == 0.123E5);
assert(.123E-5 == 0.123E-5);

assert(simpleInt(-4.31) == 4);
assert(simpleInt(1) == 1);

assert(abs(5) == 5);
assert(abs(-4.32) == 4.32);
assert(abs(0) == 0);

assert(factorial(5) == 121);
assert(factorial(5.4) == 120);
assert(factorial(-3) == 6);

assert(floor(4.21) == 4);
assert(floor(5) == 5);
assert(floor(-14.3) == -15);
assert(floor(-16) == -16);

assert(ceil(4.23) == 5);
assert(ceil(3) == 3);
assert(ceil(-3.2) == -3);
assert(ceil(-30) == -30);

assert(round(4.3) == 4);
assert(round(4.54) == 5);
assert(round(4.114,2) == 4.11);
assert(round(4.791,3) == 4.791);
assert(round(4.88,1) == 4.9);

assert(max(4,10) == 10);
assert(max(-100, 2) == 2);
assert(max(3,3) == 3);
assert(max(-4,-4) == -4);

assert(min(4,10) == 4);
assert(min(100,-2) == -2);
assert(min(3,3) == 3);
assert(min(-4,-4) == -4);

assert(pi == 3.1415926536);

assert(toDegrees(pi/3) == 60);
assert(toRadians(60) == 1.047198);
println("> Math Operations: Passed");

// String operators
str = "abc";
lt = ["a","b","c"];
assert(str == "abc");
charList = strBreak(str);
assert(size(charList) == 3);

for (i = 0; i < size(charList) - 1; i++) {
  assert(charList[i] == charList[i]);
  assert(charList[i] == lt[i]);
}
assert(strCombine(charList) == str);

assert(1E5 == 100000);
assert(1E-5 == 0.00001);

assert(toNumber("123") == 123);
assert(toNumber("0") == 0);
assert(toNumber("123.345") == 123.345);
assert(toNumber("1.32E10") == 1.32E10);
assert(toNumber("1.32E-10") == 1.32E-10);

assert(toString(123) == "123.0");
assert(toString(0) == "0.0");
assert(toString(123.345) == "123.345");
assert(toString(1.32E10) == "1.32E10");
assert(toString(1.32E-10) == "1.32E-10");

assert(substring("abc123",2,4) == "c12");
assert(substring("abc123",2) == "c123");

assert(indexof("abc123","1") == 3);
assert(indexof("abc123","3") == 5);
assert(indexof("abc123","q") == -1);

assert(canBeNumber("123"));
assert(canBeNumber(".123"));
assert(canBeNumber("123.0"));
assert(canBeNumber("123E12"));
assert(canBeNumber("123E-12"));
assert(canBeNumber("123.123E12"));
assert(canBeNumber("123.123E-12"));
assert(canBeNumber(".123E12"));
assert(canBeNumber(".123E-12"));

assert(!canBeNumber("123.123E12.5"));
assert(!canBeNumber("123.123E-12.5"));
assert(!canBeNumber("123.0.3"));
assert(!canBeNumber("abc"));
assert(!canBeNumber("1 23"));
assert(!canBeNumber(" 123"));
assert(!canBeNumber("123Ed"));
assert(!canBeNumber("123E"));
println("> String Operations: Passed");

println("> Tile Language: All Passed");
println("0");
