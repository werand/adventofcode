(ns adventofcode.year-2017.day21-fractal-art
  "Day21 solution")

;; --- Day 21: Fractal Art ---
;;
;; You find a program trying to generate some art. It uses a strange
;; process that involves repeatedly enhancing the detail of an image
;; through a set of rules.
;;
;; The image consists of a two-dimensional square grid of pixels that are
;; either on (#) or off (.). The program always begins with this pattern:
;;
;; .#.
;; ..#
;; ###
;;
;; Because the pattern is both 3 pixels wide and 3 pixels tall, it is said
;; to have a size of 3.
;;
;; Then, the program repeats the following process:
;;
;;     If the size is evenly divisible by 2, break the pixels up into 2x2
;;     squares, and convert each 2x2 square into a 3x3 square by following
;;     the corresponding enhancement rule.  Otherwise, the size is evenly
;;     divisible by 3; break the pixels up into 3x3 squares, and convert
;;     each 3x3 square into a 4x4 square by following the corresponding
;;     enhancement rule.
;;
;; Because each square of pixels is replaced by a larger one, the image
;; gains pixels and so its size increases.
;;
;; The artist's book of enhancement rules is nearby (your puzzle input);
;; however, it seems to be missing rules. The artist explains that
;; sometimes, one must rotate or flip the input pattern to find a
;; match. (Never rotate or flip the output pattern, though.) Each pattern
;; is written concisely: rows are listed as single units, ordered top-down,
;; and separated by slashes. For example, the following rules correspond to
;; the adjacent patterns:
;;
;; ../.#  =  ..
;;           .#
;;
;;                 .#.
;; .#./..#/###  =  ..#
;;                 ###
;;
;;                         #..#
;; #..#/..../#..#/.##.  =  ....
;;                         #..#
;;                         .##.
;;
;; When searching for a rule to use, rotate and flip the pattern as
;; necessary. For example, all of the following patterns match the same
;; rule:
;;
;; .#.   .#.   #..   ###
;; ..#   #..   #.#   ..#
;; ###   ###   ##.   .#.
;;
;; Suppose the book contained the following two rules:
;;
;; ../.# => ##./#../...
;; .#./..#/### => #..#/..../..../#..#
;;
;; As before, the program begins with this pattern:
;;
;; .#.
;; ..#
;; ###
;;
;; The size of the grid (3) is not divisible by 2, but it is divisible by 3.
;; It divides evenly into a single square; the square matches the second rule,
;; which produces:
;;
;; #..#
;; ....
;; ....
;; #..#
;;
;; The size of this enhanced grid (4) is evenly divisible by 2, so that rule
;; is used. It divides evenly into four squares:
;;
;; #.|.#
;; ..|..
;; --+--
;; ..|..
;; #.|.#
;;
;; Each of these squares matches the same rule (../.# => ##./#../...), three of
;; which require some flipping and rotation to line up with the rule. The output
;; for the rule is the same in all four cases:
;;
;; ##.|##.
;; #..|#..
;; ...|...
;; ---+---
;; ##.|##.
;; #..|#..
;; ...|...
;;
;; Finally, the squares are joined into a new grid:
;;
;; ##.##.
;; #..#..
;; ......
;; ##.##.
;; #..#..
;; ......
;;
;; Thus, after 2 iterations, the grid contains 12 pixels that are on.
;;
;; How many pixels stay on after 5 iterations?
;;
;; Your puzzle answer was 186.
;;
;; --- Part Two ---
;;
;; How many pixels stay on after 18 iterations?

(def start-pattern [[\. \# \.]
                    [\. \. \#]
                    [\# \# \#]])

(defn load-book-of-enhancement [f]
  (let [lines (clojure.string/split (slurp f) #"\n" )
        p-str->new-p-str (map #(clojure.string/split % #" => ") lines)
        p->new-p (map (fn [[k v]] [k (map seq (clojure.string/split v #"\/"))]) p-str->new-p-str)
        book (reduce (fn [b [k v]] (assoc b k v)) {} p->new-p)]
    book))

(defn str-pattern [p j]
  (clojure.string/join j (map #(apply str %) p)))

(defn rotate-v
  ([x y] (map (fn [a b] [a b]) y x))
  ([x y z] (map (fn [a b c] [a b c]) z y x)))

(defn rotate [p]
  "Rotate the pattern"
  (apply rotate-v p))

(defn flip [p]
  "Flip the pattern"
  (reverse p))

(defn find-pattern [b p]
  "Find the rule for the pattern p"
  (loop [p p
         t [rotate flip flip rotate flip flip rotate flip flip flip]]
    (let [new-p (get b (str-pattern p "/"))]
      (if (or (not (nil? new-p)) (empty? t))
        new-p
        (recur ((first t) p) (rest t))))))

(defn join-patterns [p size]
  "Joins the newly created patterns"
  (let [partitioned-rows (map (partial partition (inc size)) p)
        joined-rows (mapcat (fn [row] (apply (partial map concat) row)) partitioned-rows)]
    joined-rows))

(defn next-pattern [b p]
  "Divides the pattern into squares of 2x2 or 3x3,
  calculates the new pattern, and joins it together"
  (let [dividable-by-two? (= 0 (mod (count (first p)) 2))
        partition-size (if dividable-by-two? 2 3)
        p (map (partial partition partition-size) p)]
    (loop [p p
           new-pattern []]
      (if-not (seq p)
        (join-patterns new-pattern partition-size)
        (let [row (if dividable-by-two?
                    (map (fn [a b] [a b]) (first p) (second p))
                    (map (fn [a b c] [a b c]) (first p) (second p) (second (rest p))))
              new-row (mapcat (partial find-pattern b) row)
              new-pattern (conj new-pattern new-row)]
            (recur (drop partition-size p) new-pattern))))))


#_(def b (load-book-of-enhancement "resources/year_2017/day21_book_of_enhancment_lite"))
#_(next-pattern b (next-pattern b start-pattern))

#_(def b (load-book-of-enhancement "resources/year_2017/day21_book_of_enhancement"))
#_(count
    (filter #(= \# %)
            (flatten
              (next-pattern b
                            (next-pattern b
                                          (next-pattern b
                                                        (next-pattern b
                                                                      (next-pattern b start-pattern))))))))

#_(count (filter #(= \# %)
                 (flatten (first (drop 5 (iterate (partial next-pattern b) start-pattern))))))
;=> 186

;; Part 2

#_(count (filter #(= \# %)
                 (flatten (first (drop 18 (iterate (partial next-pattern b) start-pattern))))))
;=> 3018423
