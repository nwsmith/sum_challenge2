#!/bin/sh
#-----------------------------------------------
#
# histogram_trever.sh
#
# usage:  histogram_trever.sh < inputfile
#-----------------------------------------------

LANG=C      # We don't want no stinking unicode
LC_CTYPE=C  # ASCII is the one true character set

BARMAX=65  # maximum length of a bar in the histogram

tr -cd '\41-\176' | fold -w1 | sort | uniq -c | awk '

       { freq[$2]=$1; max=$1>max?max=$1:max }

 END   { for ( char in freq )  {
          printf("%c [%6d] : ",char,freq[char])
          bar=""
          for(i=0; i <= int(freq[char]*'$BARMAX'/max); i++)
            printf("*")
          print ""
         }
       }
'


# What it's doing...
#
# tr -cd tr -cd '\41-\176'
# Throw away all characters outside of octal 41-176,
# leaving only the printable ones
#
# fold -w1
# break remaining characters into one per line
#
# sort
# sort the stream
#
# uniq -c
# print character and it's frequency count
#
# awk 
# { freq[$2]=$1; max=$1>max?max=$1:max }
# find the maximum frequency while also building
# an associative array of characters and their frequencies
#
# awk
# END....
# Walk through the associative array
# print the character and it's frequency
# followed by a bar, scaled to $BARMAX maximum bar size

