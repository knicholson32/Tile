println("--For Tests");

for(i=1:5){
  println(i);
}

a=0;
for(i=1; i<5; a+=0.5){
  if(a > 2){
    a = 0;
    i++;
  }
  println(i + " - " + a);
}

println();

array = [1,3,4,6,7,"test"];

for(i : array){
  println(i);
}

for(i=0;i<0;i++){
  println("lol");
}

println("--Assertions Complete");
