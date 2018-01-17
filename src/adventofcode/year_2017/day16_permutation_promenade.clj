(ns adventofcode.year-2017.day16-permutation-promenade
  "Day16 solution")

;; --- Day 16: Permutation Promenade ---
;;
;; You come upon a very unusual sight; a group of programs here appear to
;; be dancing.
;;
;; There are sixteen programs in total, named a through p. They start by
;; standing in a line: a stands in position 0, b stands in position 1, and
;; so on until p, which stands in position 15.
;;
;; The programs' dance consists of a sequence of dance moves:
;;
;;     Spin, written sX, makes X programs move from the end to the front,
;;     but maintain their order otherwise. (For example, s3 on abcde
;;     produces cdeab).  Exchange, written xA/B, makes the programs at
;;     positions A and B swap places.  Partner, written pA/B, makes the
;;     programs named A and B swap places.
;;
;; For example, with only five programs standing in a line (abcde), they
;; could do the following dance:
;;
;;     s1, a spin of size 1: eabcd.  x3/4, swapping the last two programs:
;;     eabdc.  pe/b, swapping programs e and b: baedc.
;;
;; After finishing their dance, the programs end up in order baedc.
;;
;; You watch the dance for a while and record their dance moves (your
;; puzzle input). In what order are the programs standing after their
;; dance?
;;
;; Your puzzle answer was iabmedjhclofgknp.
;;
;; --- Part Two ---
;;
;; Now that you're starting to get a feel for the dance moves, you turn
;; your attention to the dance as a whole.
;;
;; Keeping the positions they ended up in from their previous dance, the
;; programs perform it again and again: including the first dance, a total
;; of one billion (1000000000) times.
;;
;; In the example above, their second dance would begin with the order
;; baedc, and use the same dance moves:
;;
;;     s1, a spin of size 1: cbaed.  x3/4, swapping the last two programs:
;;     cbade.  pe/b, swapping programs e and b: ceadb.
;;
;; In what order are the programs standing after their billion dances?
;;
;; Your puzzle answer was oildcmfeajhbpngk.

(defn spin [n s]
  (->> (cycle s)
       (drop (- (count s) n))
       (take (count s))))

(defn exchange [i j s]
  (let [v (vec s)
        i-element (nth v i)
        j-element (nth v j)]
    (assoc v i j-element j i-element)))

(defn partner [a b s]
  (let [v (vec s)]
    (exchange (.indexOf v (first a)) (.indexOf v (first b)) v)))

(defn parse-spin [c]
  (Integer/parseInt (subs c 1)))

(defn parse-exchange [c]
  (let [r (rest (first (re-seq #".(\d+)/(\d+)" c)))]
    (map #(Integer/parseInt %) r)))

(defn parse-partner [c]
  (rest (first (re-seq #".(.)/(.)" c))))

(defn parse-commands [command-str]
  (let [commands (clojure.string/split command-str #",")]
    (loop [c commands
           command-fn []]
      (if (seq c)
        (condp = (first (first c))
          \s (recur (rest c) (conj command-fn (partial spin (parse-spin (first c)))))
          \x (let [params (parse-exchange (first c))]
               (recur (rest c) (conj command-fn (partial exchange (first params) (second params)))))
          \p (let [params (parse-partner (first c))]
               (recur (rest c) (conj command-fn (partial partner (first params) (second params))))))
        command-fn))))

(defn execute-commands
  ([command-str programs]
    (execute-commands command-str programs 1))
  ([command-str programs iterations]
   (let [c (parse-commands command-str)
         i (* iterations (count c))]
     (loop [c (cycle c) p programs i i j 0]
       (if (and (> j 0) (= "abcdefghijklmnop" (apply str p)))
         (recur (rest c) ((first c) p) (dec (rem i j)) 0)
         (if (> i 0)
           (recur (rest c) ((first c) p) (dec i) (inc j))
           [j p]))))))

;(execute-commands "s1,x3/4,pe/b" "abcde")

#_(execute-commands (slurp "resources/year_2017/day16_permutation_promenade") "abcdefghijklmnop")
;=> [10000 [\i \a \b \m \e \d \j \h \c \l \o \f \g \k \n \p]]


#_(execute-commands (slurp "resources/year_2017/day16_permutation_promenade") "abcdefghijklmnop" 1000000000)
;=> [279999 [\o \i \l \d \c \m \f \e \a \j \h \b \p \n \g \k]]
