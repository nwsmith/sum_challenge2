a = ARGV[0].split(",")
d = [a[0]]
t = ["Mon","Tue","Wed","Thu","Fri","Sat","Sun"]
1.upto(a.size-1) do |i| 
  if d.last == "-"
    d.push(a[i]) if a[i].next != a[i.next]
  else
    d.push(a[i-1].next == a[i] && a[i].next == a[i.next] ? "-" : a[i])
  end
end
s = ""
d.each_index {|i| s << "#{d[i]=="-"?d[i]:t[d[i].to_i-1]}#{"," if (d[i] != "-" && d[i+1] != "-" && i < (d.size-1))}"}
puts s
