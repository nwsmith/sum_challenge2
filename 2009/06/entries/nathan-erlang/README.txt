First, install erlang: sudo apt-get install erlang-base

Then:
Type "erl" to run erlang from same directory containing roman.erl
Type c(roman). to compile module
Then type roman:convert("1234") to convert numbers.

example session:

70> c(roman).            
{ok,roman}
71> roman:convert("123").
"CXXIII"
72> roman:convert("103").
"CIII"
74> roman:convert("2000").
"MM"
75> roman:convert("1999").
"MCMXCIX"
76> roman:convert("1337").
"MCCCXXXVII"
77> 

