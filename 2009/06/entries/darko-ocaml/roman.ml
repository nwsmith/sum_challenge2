open List
open Printf;;

let rec roman n xs ys =
  if xs == [] || ys == [] then ""
  else if n < hd xs then roman n (tl xs) (tl ys)
  else hd ys ^ roman (n - hd xs) xs ys ;;

printf "Please enter an integer number between 1 and 3999: %!";;

let n = read_int () in printf "%d => %s\n%!" 
  n (roman n [1000;900;500;400;100;90;50;40;10;9;5;4;1]
 ["M";"CM";"D";"CD";"C";"XC";"L";"XL";"X";"IX";"V";"IV";"I"]);;

