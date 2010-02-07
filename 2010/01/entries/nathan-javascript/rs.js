var buffer = [];
var length = 0;

function push() {
  var e = document.getElementById("text");
  var s = e.value.toUpperCase();
  writeString(s);
}

function doClear() {
  clearString();
  var e = document.getElementById("text");
  e.value = ''; 
}

function putBuffer() {
  writeBuffer();
}

function putReverseBuffer() {
  writeReverseBuffer();
}

function putMirrorBuffer() {
  writeMirrorBuffer()
}

function writeReverseBuffer() {
  var x = 1;
  var y = 1;
  for (var i = length -1 ; i >= 0; --i) {
    drawBitfield(x, y, buffer[i]);
    x += (width + 1);
  }
} 

function writeMirrorBuffer() {
  var x = 1;
  var y = 1;
  for (var i = 0; i < length; ++i) {
    drawMirrorBitfield(x, y, buffer[i]);
    x += (width + 1);
  }
} 

function writeBuffer() {
  var x = 1;
  var y = 1;
  for (var i = 0; i < length; ++i) {
    drawBitfield(x, y, buffer[i]);
    x += (width + 1);
  }
} 

function writeString(s) {
  var x = 1;
  var y = 1;
  length = s.length;
  for (var i = 0; i < length; ++i) {
    buffer[i] = letters[s.charAt(i)];
    draw(x, y, s.charAt(i));
    x += (width + 1);
  }  
}

function fetchString() {
  var x = 1;
  var y = 1;
  for (var i = 0; i < length; ++i) {
    fetch(x, y, i);
    x += (width + 1);
  }
}

function clearString() {
  var x = 1;
  var y = 1;
  for (var i = 0; i < length; ++i) {
    clear(x, y);
    x += (width + 1);
  }
}

function drawBitfield(x, y, bitfield) {
  var xx = x;
  var yy = y;
  for (var i = 0; i < width; ++i, ++x) {
    y = yy;
    for (var j = 0; j < height; ++j, ++y) {
      if (bitfield[j][i] == 1) {
        paint(x, y, "#000000");
      } else {
        paint(x, y, "#FFFFFF");
      }
    }
  }
}

function drawMirrorBitfield(x, y, bitfield) {
  var xx = x;
  var yy = y;
  for (var i = width - 1; i >= 0; --i, ++x) {
    y = yy;
    for (var j = 0; j < height; ++j, ++y) {
      if (bitfield[j][i] == 1) {
        paint(x, y, "#000000");
      } else {
        paint(x, y, "#FFFFFF");
      }
    }
  }
}

function draw(x, y, letter) {
  var bitfield = letters[letter];
  var xx = x;
  var yy = y;
  for (var i = 0; i < width; ++i, ++x) {
    y = yy;
    for (var j = 0; j < height; ++j, ++y) {
      if (bitfield[j][i] == 1) {
        paint(x, y, "#000000");
      } else {
        paint(x, y, "#FFFFFF");
      }
    }
  }
}

function clear(x, y) {
  var xx = x;
  var yy = y;
  for (var i = 0; i < width; ++i, ++x) {
    y = yy;
    for (var j = 0; j < height; ++j, ++y) {
      paint(x, y, "#FFFFFF");
    }
  }
}

function fetch(x, y, z) {
  bitfield = [];
  for (var i = 0; i < height; ++i) {
    bitfield[i] = [];
  }
  var xx = x;
  var yy = y;
  for (var i = 0; i < width; ++i, ++x) {
    y = yy;
    for (var j = 0; j < height; ++j, ++y) {
      if (isOn(x, y)) {
    bitfield[j][i] = 1;
      } else {
    bitfield[j][i] = 0;
      }
    }
  }
  buffer[z] = bitfield;
}


function paint(x, y, color) {
  var id = x + "," + y;
  var e = document.getElementById(id);
  e.bgColor = color;
}

function isOn(x, y) {
  var id = x + "," + y;
  var e = document.getElementById(id);
  return e.bgColor == "#000000";
}

var width = 5; 
var height = 5;
var letters = { 
'A' : 
[[0,0,1,0,0],
 [0,1,0,1,0],
 [1,1,1,1,1],
 [1,0,0,0,1],
 [1,0,0,0,1]],
'B' : 
[[1,1,1,1,0],
 [1,0,0,0,1],
 [1,1,1,1,0],
 [1,0,0,0,1],
 [1,1,1,1,0]],
'C' : 
[[0,1,1,1,0],
 [1,0,0,0,1],
 [1,0,0,0,0],
 [1,0,0,0,1],
 [0,1,1,1,0]],
'D' : 
[[1,1,1,1,0],
 [1,0,0,0,1],
 [1,0,0,0,1],
 [1,0,0,0,1],
 [1,1,1,1,0]],
'E' : 
[[1,1,1,1,1],
 [1,0,0,0,0],
 [1,1,1,0,0],
 [1,0,0,0,0],
 [1,1,1,1,1]],
'F' : 
[[1,1,1,1,1],
 [1,0,0,0,0],
 [1,1,1,0,0],
 [1,0,0,0,0],
 [1,0,0,0,0]],
'G' : 
[[0,1,1,1,1],
 [1,0,0,0,0],
 [1,0,0,1,1],
 [1,0,0,0,1],
 [0,1,1,1,0]],
'H' : 
[[1,0,0,0,1],
 [1,0,0,0,1],
 [1,1,1,1,1],
 [1,0,0,0,1],
 [1,0,0,0,1]],
'I' : 
[[0,1,1,1,0],
 [0,0,1,0,0],
 [0,0,1,0,0],
 [0,0,1,0,0],
 [0,1,1,1,0]],
'J' : 
[[1,1,1,1,1],
 [0,0,1,0,0],
 [0,0,1,0,0],
 [1,0,1,0,0],
 [0,1,1,0,0]],
'K' : 
[[0,1,0,1,0],
 [0,1,1,0,0],
 [0,1,0,0,0],
 [0,1,1,0,0],
 [0,1,0,1,0]],
'L' : 
[[0,1,0,0,0],
 [0,1,0,0,0],
 [0,1,0,0,0],
 [0,1,0,0,0],
 [0,1,1,1,1]],
'M' : 
[[1,0,0,0,1],
 [1,1,0,1,1],
 [1,0,1,0,1],
 [1,0,0,0,1],
 [1,0,0,0,1]],
'N' : 
[[1,0,0,0,1],
 [1,1,0,0,1],
 [1,0,1,0,1],
 [1,0,0,1,1],
 [1,0,0,0,1]],
'O' : 
[[0,1,1,1,0],
 [1,0,0,0,1],
 [1,0,0,0,1],
 [1,0,0,0,1],
 [0,1,1,1,0]],
'P' : 
[[1,1,1,1,0],
 [1,0,0,0,1],
 [1,1,1,1,0],
 [1,0,0,0,0],
 [1,0,0,0,0]],
'Q' : 
[[0,1,1,1,0],
 [1,0,0,0,1],
 [1,0,1,0,1],
 [1,0,0,1,0],
 [0,1,1,0,1]],
'R' : 
[[1,1,1,1,0],
 [1,0,0,0,1],
 [1,1,1,1,0],
 [1,0,0,0,1],
 [1,0,0,0,1]],
'S' : 
[[0,1,1,1,1],
 [1,0,0,0,0],
 [0,1,1,1,0],
 [0,0,0,0,1],
 [1,1,1,1,0]],
'T' : 
[[1,1,1,1,1],
 [0,0,1,0,0],
 [0,0,1,0,0],
 [0,0,1,0,0],
 [0,0,1,0,0]],
'U' : 
[[1,0,0,0,1],
 [1,0,0,0,1],
 [1,0,0,0,1],
 [1,0,0,0,1],
 [0,1,1,1,0]],
'V' : 
[[1,0,0,0,1],
 [1,0,0,0,1],
 [0,1,0,1,0],
 [0,1,0,1,0],
 [0,0,1,0,0]],
'W' : 
[[1,0,0,0,1],
 [1,0,0,0,1],
 [1,0,1,0,1],
 [1,1,0,1,1],
 [1,0,0,0,1]],
'X' : 
[[1,0,0,0,1],
 [0,1,0,1,0],
 [0,0,1,0,0],
 [0,1,0,1,0],
 [1,0,0,0,1]],
'Y' : 
[[1,0,0,0,1],
 [0,1,0,1,0],
 [0,0,1,0,0],
 [0,0,1,0,0],
 [0,0,1,0,0]],
'Z' : 
[[1,1,1,1,1],
 [0,0,0,1,0],
 [0,0,1,0,0],
 [0,1,0,0,0],
 [1,1,1,1,1]],
' ' : 
[[0,0,0,0,0],
 [0,0,0,0,0],
 [0,0,0,0,0],
 [0,0,0,0,0],
 [0,0,0,0,0]],
}
