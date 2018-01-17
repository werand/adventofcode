(ns adventofcode.year-2017.day11-hex-ed
  "Day11 solution")

;; --- Day 11: Hex Ed ---
;;
;; Crossing the bridge, you've barely reached the other side of the stream
;; when a program comes up to you, clearly in distress. "It's my child
;; process," she says, "he's gotten lost in an infinite grid!"
;;
;; Fortunately for her, you have plenty of experience with infinite grids.
;;
;; Unfortunately for you, it's a hex grid.
;;
;; The hexagons ("hexes") in this grid are aligned such that adjacent hexes
;; can be found to the north, northeast, southeast, south, southwest, and
;; northwest:
;;
;;   \ n  /
;; nw +--+ ne
;;   /    \
;; -+      +-
;;   \    /
;; sw +--+ se
;;   / s  \
;;
;; You have the path the child process took. Starting where he started, you
;; need to determine the fewest number of steps required to reach him. (A
;; "step" means to move from the hex you are in to any adjacent hex.)
;;
;; For example:
;;
;;     ne,ne,ne is 3 steps away.  ne,ne,sw,sw is 0 steps away (back where
;;     you started).  ne,ne,s,s is 2 steps away (se,se).  se,sw,se,sw,sw is
;;     3 steps away (s,s,sw).
;;
;; Your puzzle answer was 743.
;;
;;--- Part Two ---
;;
;; How many steps away is the furthest he ever got from his starting
;; position?
;;
;; Your puzzle answer was 1493.

(def directions {:SE [0.5, -0.5]
                 :S [0, -1]
                 :SW [-0.5, -0.5]
                 :NW [-0.5 , 0.5]
                 :N [0, 1]
                 :NE [0.5, 0.5]})

(defn add [[x1 y1] [x2 y2]]
  [(+ x1 x2) (+ y1 y2)])

(defn abs [v]
  (map #(Math/abs %) v))

(defn steps [v]
  (let [[x y] (abs v)]
    (let [x-steps (* 2 x)
          y-steps (- y x)
          result (+ x-steps y-steps)]
      result)))

(defn bigger [v1 v2]
  (if (>= (steps v1) (steps v2))
    v1
    v2))

(defn follow-path [p]
  (let [str-path (clojure.string/split p #",")
        path (map keyword (map clojure.string/upper-case str-path))
        final-field (reduce #(assoc %1 :current (add (:current %1) %2) :max (bigger (:max %1) (:current %1)))
                            { :current [0, 0] :max [0, 0] }
                            (map directions path))
        result (steps (:current final-field))
        result-max (steps (:max final-field))]
  [final-field result result-max]))

;=> (reduce add [0 0] (concat (repeat 355 (:NE directions)) (repeat 388 (:N directions))))
;[177.5 565.5]
;=> (+ 388 355)
;; 743

#_(follow-path (slurp "resources/year_2017/day11_hex_ed"))
