// MAP UTILS :: TILE BASE
//
// Support for creating and using maps.
// Refer to the official documentation for more information.
//
// COPYRIGHT 2017 ROUNDPIXEL

function new_map(){
  return [];
}

function map_add(map,name,val){
  for(i=0; i<size(map); i++){
    if(name == map[i][0]){
      map[i] = [name,val];
      return;
    }
  }
  map += [name,val];
}

function map_get(map,name){
  s = size(map);
  for(i : map){
    if(i[0] == name){
      return i[1];
    }
  }
}

function map_toString(map){
  str = "";
  for(i : map){
    str += "[" + i[0] + " -> " + i[1] + "]";
  }
  return str;
}
