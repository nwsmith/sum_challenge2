#!/usr/bin/perl

for (reverse split//,shift) {
  push @r, map{$a = $_; for $m (1..$mx++){$a =~ y/IVXLC/XLCDM/} $a} ("",qw(I II III IV V VI VII VIII IX))[$_];
}
print reverse @r;
