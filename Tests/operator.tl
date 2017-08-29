a = 5;
assert(a++ == 6);
assert(a++ == 7);
assert((b=a++) == 8);
assert(b == 8);
assert(b-- == 7);
assert(c = b-- == 6);

c = 7;
b = 2;

assert((c+=b) == 9);
assert((c+=1) == 10);

arr = [1,3,4,5,"test"];
assert(arr[1]++ == 4);
assert(arr[2]++ == arr[3]);
assert(arr[0]-- == 0);
assert((arr[3]--) == 4);

t = [0,1,2,3,4,[5,6]];

//println(t[1] + " -> +2 -> " + (t[1]+=2));

//t[2] += t[2][0];


array = [0,1,2,3,4,5];

println(t);

t += 2;

println(t);

t[5] += ["test1","test2"];

println(t);

text = "this is a";
text2 = " test";
text = text + text2;
println("TEXT: " + text);
text += text2;
println(text);
text += arr[4];
println(text);

stuff = "stuff here: ";
t = t - 0;
println(stuff);
println(t);


//println("First: " + t[2][0]);
//println("Second: " + t[2]);
//t += t[3];

//println("Value: " + t);

println("Assertions Complete");
