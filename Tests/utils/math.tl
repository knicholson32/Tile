// MATH UTILS :: TILE BASE
//
// Basic math functions and operators
// Refer to the official documentation for more information.
//
// COPYRIGHT 2017 ROUNDPIXEL

// ABSOLUTE VALUE
//  abs=[NUMBER] : funct
// INPUT  : A number
// RETURN : The absolute value of the input
function abs(number val){
  if (val < 0) {
    val *= -1;
  }
  return clean(val);
}

// FACTORIAL
//  factorial=[NUMBER] : funct
// INPUT  : A number
// RETURN : The factorial of the input
function factorial(number val) {
  val = simpleInt(val);
  mult = val;
  for (i = val-1; i > 0; i--) {
    mult = mult * i;
  }
  return mult;
}

// FLOOR
//  floor=[NUMBER] : funct
// INPUT  : A number
// RETURN : The integer floor of the input
function floor(number val) {
  if (val < 0) {
    return -ceil(abs(val));
  } else {
    return val - (val % 1);
  }
}

// CEIL
//  ceil=[NUMBER] : funct
// INPUT  : A number
// RETURN : The integer ceiling of the input
function ceil(number val) {
  if (val < 0) {
    return -floor(abs(val));
  } else {
    dec = val % 1;
    if (dec != 0) {
      return val + 1 - dec;
    } else {
      return simpleInt(val);
    }
  }
}

// ROUND
//  round=[NUMBER], [NUMBER, NUMBER] : funct
// INPUT  : A number
// RETURN : The input number rounded to the nearest integer
function round(number v) {
  return round(v,0);
}
function round(number val, number idx) {
  idx = simpleInt(idx);
  m = 1 / (10^idx);
  dec = val % m;
  comp = 0.5 * 1 / (10^idx);
  if (dec < comp) {
    return clean(val - dec);
  } else {
    return clean(val + (m - dec));
  }
}

// CLEAN
//  clean=[NUMBER] : funct
// INPUT  : A number
// OUTPUT : The input number, cleaned by the base language for trailing errors
function clean(number val){
  sys_clean();
  return val + 0;
}

// EXP
//  exp=[NUMBER] : funct
// INPUT  : A power
// OUTPUT : e raised to the input
function exp(number pow) {
  return clean(e ^ pow);
}

// MAX
//  max=[NUMBER, NUMBER] : funct
// INPUT  : Two numbers
// RETURN : The larger of the two inputs (closer to pos inf)
function max(number val1, number val2) {
  if (val1 >= val2) {
    return val1;
  } else {
    return val2;
  }
}

// MIN
//  min=[NUMBER, NUMBER] : funct
// INPUT  : Two numbers
// RETURN : The smaller of the two inputs (closer to neg inf)
function min(number val1, number val2) {
  if (val1 <= val2) {
    return val1;
  } else {
    return val2;
  }
}

// TODEGREES
//  toDegrees=[NUMBER] : funct
// INPUT  : An angle in radians
// OUTPUT : The input angle in degrees
function toDegrees(number angleRad) {
  return clean(angleRad / pi * 180);
}

// TORADIANS
//  toRadians=[NUMBER] : funct
// INPUT  : An angle in degrees
// OUTPUT : The input angle in radians
function toRadians(number angleDeg) {
  return clean(angleDeg / 180 * pi);
}

// SIMPLEINT
//  simpleInt=[NUMBER] : funct
// INPUT  : A number
// RETURN : the positive floor of the input (floored positive integer)
function simpleInt(number val) {
  return floor(abs(val));
}

// Mathematical Constant: Pi
//  Relationship between a circle's circumference to its diameter
pi = 3.1415926536;

// Mathematical Constant: e
//  Base of the natural logarithm
e = 2.7182818285;
