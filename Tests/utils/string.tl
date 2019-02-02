// STRING UTILS :: TILE BASE
//
// Basic string operations
// Refer to the official documentation for more information.
//
// COPYRIGHT 2017 ROUNDPIXEL

// STRBREAK
//  strBreak=[STRING] : funct
// INPUT  : A string
// RETURN : An array containing the given string broken into charactor strings
function strBreak(string str){
  return sys_strbreak(str);
}

// STRCOMBINE
//  strCombine=[STRING] : funct
// INPUT  : An array of charactor strings
// RETURN : A string containing the combined charactors of the input array
function strCombine(list charList){
  str = "";
  for (ch : charList) {
    str += ch;
  }
  return str;
}

// SUBSTRING
//  substring=[STRING] : funct
// INPUT  : A string
// RETURN : An array containing the given string broken into charactor strings
function substring(string str, number first, number last){
  if (first < 0 || last <= first) {
    error("Invalid substring indicies: First:" + first + " Last:" + last + ".");
  }
  first = simpleInt(first);
  last = simpleInt(last);
  charList = sys_strbreak(str);
  strOut = "";
  for (i = first : last) {
    strOut += charList[i];
  }
  return strOut;
}

// INDEXOF
//  indexof=[STRING, STRING], [LIST, STRING] : funct
// INPUT  : A string OR a char list and an char string to find
// OUTPUT : The index the char string is found at, or -1 if not found;
function indexof(string str, string val) {
  return indexof(sys_strbreak(str),val);
}
function indexof(list charList, string val) {
  for (i = 0 : size(charList)-1) {
    if (charList[i] == val) {
      return i;
    }
  }
  return -1;
}
// TODO Maybe find a better way to do this
// TONUMBER
//  toNumber=[STRING] : funct
// INPUT  : A string to convert to a number
// OUTPUT : The converted number, if possible
function toNumber(string str) {
  sys_regex_set("\\G(([0-9]+(\\.[0-9]+)?)|((\\.[0-9]+)))((e|E)-?([0-9]+))?$");
  if (!sys_regex_match(str)) {
    error("The given string cannot be converted to a number: " + str);
  }
  eval("tmp = " + str);
  return tmp;
}

// TOSTRING
//  toString=[NUMBER] : funct
// INPUT  : A number to be converted to a string
// OUTPUT : A string representation of the input number
function toString(number num) {
  return "" + num;
}



// CANBENUMBER
//  canBeNumber=[STRING] : funct
// INPUT  : A string to check
// OUTPUT : a bool stating if the given string can be represented as a number
function canBeNumber(string str) {
  sys_regex_set("\\G(([0-9]+(\\.[0-9]+)?)|((\\.[0-9]+)))((e|E)-?([0-9]+))?$");
  return sys_regex_match(str);
}

function substring(string str, number first){
  return substring(str,first,size(str)-1);
}
