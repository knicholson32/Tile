//v[0] = [1,2,3]; // Throws exception

d = [1,[1,2,3], "2"];
d[4] = []; // Works correctly
d[4][4] = "test";

scope();
// Prints Index: 0, Size: 0
