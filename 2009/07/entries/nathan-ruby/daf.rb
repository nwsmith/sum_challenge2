z=ARGV[0][0];a=[];(65..z).each{|b|a<<' '*(z-b)+b.chr+(b>65?(' '*(b*2-131)+b.chr):'')};puts a;a.pop;puts a.reverse
