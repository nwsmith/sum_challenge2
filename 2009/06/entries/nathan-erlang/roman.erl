-module(roman).
-export([convert/1]).

convert(N) -> lists:flatten(convert(N, length(N))).

convert([], 0) -> "";
convert([H|T], P) -> [convert_num(H, P), convert(T, P-1)].

convert_num(N,P) -> 
  case {N, P} of
    {48, _} -> "";

    {49, 1} -> "I";
    {49, 2} -> "X";
    {49, 3} -> "C";
    {49, 4} -> "M";

    {50, 1} -> "II";
    {50, 2} -> "XX";
    {50, 3} -> "CC";
    {50, 4} -> "MM";

    {51, 1} -> "III";
    {51, 2} -> "XXX";
    {51, 3} -> "CCC";
    {51, 4} -> "MMM";

    {52, 1} -> "IV";
    {52, 2} -> "XL";
    {52, 3} -> "CD";

    {53, 1} -> "V";
    {53, 2} -> "L";
    {53, 3} -> "D";

    {54, 1} -> "VI";
    {54, 2} -> "LX";
    {54, 3} -> "DC";

    {55, 1} -> "VII";
    {55, 2} -> "LXX";
    {55, 3} -> "DCC";

    {56, 1} -> "VIII";
    {56, 2} -> "LXXX";
    {56, 3} -> "DCCC";

    {57, 1} -> "IX";
    {57, 2} -> "XC";
    {57, 3} -> "CM"
  end.
