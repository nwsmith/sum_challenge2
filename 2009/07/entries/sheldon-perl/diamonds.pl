            $s=
           "A" ;
          $w= ord
         (uc(shift
        ))-ord($s);
       $e =$w*2; for
      (0..$w) { $l[$_
     ]=" " x ( $w- $_)
      . $s.(" "x(($_*
       2)-1)).( $_==
        0 ?"" : $s)
         ;$l[$e--]
          =$l[$_]
           ;$s++
            ; } 

    print "$_\n" for @l;
