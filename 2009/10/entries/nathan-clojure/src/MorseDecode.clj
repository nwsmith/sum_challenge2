(def morse-map {
  "a" ".-" "b" "-...", "c" "-.-.", "d" "-..", "e" ".", "f" "..-.", "g" "--.", "h" "....", "i" "..", "j" ".---", "k" "-.-", "l" ".-..", "m" "--",
  "n" "-.", "o" "---", "p" ".--.", "q" "--.-", "r" ".-.", "s" "...", "t" "-", "u" "..-", "v" "...-", "w" ".--", "x" "-..-", "y" "-.--", "z" "--.."})

(defn from-morse [morse match]
  (if (= "" morse) match
    (for [l (keys morse-map) :when (.startsWith morse (morse-map l))]
      (list (from-morse (subs morse (count (morse-map l))) (str match l))))))

(defn dictionary [file] (set (map #(.toLowerCase %) (.split (slurp file) "\n"))))
(defn flatten [l] (filter (complement seq?) (tree-seq seq? seq l)))
(defn print-all [l] (doseq [e (sort l)] (println e)))

(if (not (= 2 (count *command-line-args*)))
  (do
    (println "usage: MorseDecode /usr/share/dict/words ...")
    (System/exit 1)))

(let [morse-combos (set (flatten (from-morse (second *command-line-args*) "")))]
  (println "All Combinations")
  (print-all morse-combos)
  (println "Dictionary Words")
  (print-all (clojure.set/intersection morse-combos (dictionary (first *command-line-args*)))))
