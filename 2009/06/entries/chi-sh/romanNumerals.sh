#!/bin/sh
wget -q -U "Mozilla/5.0 (Windows;)" -O - "http://www.google.ca/search?hl=en&q=$1+in+roman+numerals" | awk -F'<img src=/images/calc_img.gif width=40 height=30 alt=""><td>&nbsp;<td nowrap ><h2 class=r style="font-size:138%"><b>' -v RS='</b></h2>' -- 'RT{print $NF}'
