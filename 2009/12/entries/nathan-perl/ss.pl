#!/usr/bin/perl
while(<>){/\s(\w+)\s<(\S+)>/;push@p,[$1,$2]}@r=reverse(0..@p-1);
sub f{$a=shift;foreach(@r){return 0 if$$a[$_][0]eq$$a[$_-1][0]}1}
@p=sort{(-1,1)[rand 2]}@p until f(\@p);
foreach(@r){print"$p[$_][1]->$p[$_-1][1]\n"}
