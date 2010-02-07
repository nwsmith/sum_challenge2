#!/bin/bash

get_random_number() {
  from=$1
  to=$2
  let "range=$to-$from-1"
  number=0
  
  if [ "$range" -gt 0 ]; then
    number=$RANDOM
    let "number %= $range"
    let "number=$number+$from+1"
  fi

  return $number
}

EMAIL_DELIMETER=" "

file_name=$1

declare -a names
declare -a pairs

old_IFS=$IFS
IFS=$'\n'
for LINE in $(sort -k 2,2 $file_name)   # read the input file sorted by last name   
do
  names[${#names[@]}]=$LINE
done
IFS=$old_IFS

number_of_elements=${#names[@]}

for (( i = 0 ; i < $number_of_elements ; i++ )) do

  current_last_name=${names[$i]#* }
  current_last_name=${current_last_name% *}
 
  let "j=$i+1"                               # skip duplicate last names
  while [ $j -lt $number_of_elements ]; do
  
    last_name=${names[$j]#* }
    last_name=${last_name% *}

    if [ ${current_last_name} != ${last_name} ]; then
      break
    fi

    let "j+=1"
  done

  let "j-=1"
  get_random_number $j $number_of_elements
  random_number=$?

  if [ $random_number -eq 0 ]; then
    break
  fi

  pairs[${#pairs[@]}]=${names[$i]##* }$EMAIL_DELIMETER${names[$random_number]##* }
  
  let "number_of_elements-=1"

  for (( k = $random_number ; k < $number_of_elements ; k++ )) do   # move elements 
    names[$k]=${names[$k+1]}
  done
  
done

for (( i = 0 ; i < ${#pairs[@]} ; i++ )) do
    echo ${pairs[$i]}
done

exit 0
