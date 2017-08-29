// Variables

import "./Tests/termUtils.tl";
import "./Tests/utils/global.tl";

// Program
println(getSystem());
tprintln("Please enter a command (? for help):");

runLoop = true;
while(runLoop){
  strIn = input("> ");

  if(strIn == "?"){
    tprintln("--Help--");
    tprintln("?     -  help");
    tprintln("exit  -  exit");
    tprintln("echo  -  echos what you type next");
    tprintln("array -  Create an array and do math on it");
    tprintln("map   -  Run simple map test");
    println();
  }else if(strIn == "exit" || strIn == "q" || strIn == "quit"){
    runLoop = false;
  }else if(strIn == "echo"){
    strIn = input();
    tprintln(strIn);
  }else if(strIn == "test"){
    files = ["./Tests/boolean.tl", "./Tests/substring.tl"];
    run(files);
    run("./Tests/scope.tl");
    tprintln("TESTS DONE");
  }else if(strIn == "master"){

    println(testMaster("test", 123));

  }else if(strIn == "map"){

    map = new_map();
    map_add(map, "name", "bob");
    map_add(map, "age", "25");
    map_add(map, "dog", "Butch");
    map_add(map, "v", 7);
    println(map_toString(map));
    map_add(map, "dog", "fido");
    println(map_toString(map));

  }else if(strIn == "array"){
    inArray = true;
    arr = [];
    println();
    tprintln("Array Math:");
    tprintln("Type numbers to add and then 'done' to exit");
    while(inArray){
      strIn = input();
      if(strIn != "done"){
        arr += toNumber(strIn);
      }else{
        inArray=false;
      }
    }
    tprintln("Array: " + arr);
    inArray = true;
    println();
    tprintln("Type an op (+, -, *, /) and a number to operate on the Array");
    op = input();
    if(op == "+" || op == "-" || op == "*" || op == "/"){
      num = toNumber(input());
      if(op=="+"){
        for(i=0;i<size(arr);i++){
          arr[i] += num;
        }
      }else if(op=="-"){
        for(i=0;i<size(arr);i++){
          arr[i] -= num;
        }
      }else if(op=="*"){
        for(i=0;i<size(arr);i++){
          arr[i] *= num;
        }
      }else if(op=="/"){
        for(i=0;i<size(arr);i++){
          arr[i] /= num;
        }
      }
      tprintln("Array: " + arr);
    }else{
      tprintln("Invalid Op");
    }
  }else{
    eval(strIn);
  }
}
