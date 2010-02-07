(def codes
  {
   ".-" "a",
   "-..." "b",
   "-.-." "c",
   "-.." "d",
   "." "e",
   "..-." "f",
   "--." "g", 
   "...." "h",
   ".." "i",
   ".---" "j",
   "-.-" "k",
   ".-.." "l",
   "--" "m",
   "-." "n",
   "---" "o",
   ".--." "p",
   "--.-" "q",
   ".-." "r",
   "..." "s",
   "-" "t",
   "..-" "u",
   "...-" "v",
   ".--" "w", 
   "-..-" "x", 
   "-.--" "y",
   "--.." "z"
  }
)

(defn morsify 
  ([input]
    (morsify "" (str (first input)) (next input) ())
  )
  ([accum codefragment remainder found]
    (if remainder
        (morsify accum (str codefragment (first remainder)) (next remainder) 
          (morsify (str accum (codes codefragment)) (str (first remainder)) (next remainder) found)
        )
      (conj found (str accum (codes codefragment)))
    )
  )   
)


(doseq [n (sort (morsify (read-line)))] (println n))
