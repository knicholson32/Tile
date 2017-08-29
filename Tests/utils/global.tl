// GLOBAL UTILS :: TILE BASE
//
// Base Tile util for interfacing with the parent language.
// Refer to the official documentation for more information.
//
// COPYRIGHT 2017 ROUNDPIXEL

import "./Tests/utils/map.tl";
import "./Tests/utils/math.tl";
import "./Tests/utils/string.tl";

// PRINTLN (Print line)
//  println=[ANY], [] : funct
// INPUT : Any value
function println(val) {
  sys_println("" + val);
}
function println() {
  println("");
}

// PRINT (Print)
//  print=[ANY], [] : funct
// INPUT : Any value
function print(val) {
  sys_print("" + val);
}
function print() {
  print("");
}

// INPUT
//  input=[STRING], [] : funct,
// Input  : Nothing OR a string prompt
// Return : A string from the user
function input (string prompt) {
  print(prompt);
  return sys_input();
}
function input () {
  return sys_input();
}
