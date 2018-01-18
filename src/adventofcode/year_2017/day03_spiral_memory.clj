(ns adventofcode.year-2017.day03-spiral-memory
  "Day03 solution")

;; --- Day 3: Spiral Memory ---
;;
;; You come across an experimental new kind of memory stored on an infinite
;; two-dimensional grid.
;;
;; Each square on the grid is allocated in a spiral pattern starting at a
;; location marked 1 and then counting up while spiraling outward. For
;; example, the first few squares are allocated like this:
;;
;; 17 16 15 14 13 18 5 4 3 12 19 6 1 2 11 20 7 8 9 10 21 22 23---> ...
;;
;; While this is very space-efficient (no squares are skipped), requested
;; data must be carried back to square 1 (the location of the only access
;; port for this memory system) by programs that can only move up, down,
;; left, or right. They always take the shortest path: the Manhattan
;; Distance between the location of the data and square 1.
;;
;; For example:
;;
;;     Data from square 1 is carried 0 steps, since it's at the access
;;     port.  Data from square 12 is carried 3 steps, such as: down, left,
;;     left.  Data from square 23 is carried only 2 steps: up twice.  Data
;;     from square 1024 must be carried 31 steps.
;;
;; How many steps are required to carry the data from the square identified
;; in your puzzle input all the way to the access port?
;;
;; Your puzzle answer was 480.
;;

(def circular-memory
      (map #(let [current %]
              (if (= current 1)
                [1 1 1 1]
                (let [s (* (- current 2) (- current 2))
                      n (- current 1)]
                  [(+ s n) (+ s n n) (+ s n n n) (+ s n n n n)])))
           (filter odd? (range))))

(def odd-squares
  (map #(* % %) (iterate #(+ 2 %) 1)))

;; Corners upper-right upper-left lower-left lower-right and new-upper-right ...
(defn next-value-fn [n [ur ul ll lr] [nur nul nll _]]
  (cond
    (= lr n) [(dec n) identity :left]
    (= ur n) [(inc n) identity :left]
    (= ul n) [(dec n) identity :right]
    (= ll n) [(inc n) identity :right]
    (< n ur) [(+ (- ur n 1) nur) rest :left]
    (and (> n ur) (< n ul)) [(+ (- n ur 1) nur) rest :down]
    (and (> n ul) (< n ll)) [(+ (- n ul 1) nul) rest :right]
    (and (> n ll) (< n lr)) [(+ (- n ll 1) nll) rest :up]))

(defn determine-path [n]
  (let [next-odd-square (first (filter #(>= % n) odd-squares))
        circular-space (take-while (fn [[_ _ _ lr]] (<= lr next-odd-square)) circular-memory)
        circular-space (reverse circular-space)]
    (loop [n n
           circular-space circular-space
           path []]
      (if (= n 1)
        path
        (let [[n next-fn direction] (next-value-fn n (first circular-space) (second circular-space))]
          (recur n (next-fn circular-space) (conj path direction)))))))

#_ (count (determine-path 347991))
;=> 480

;; --- Part Two ---
;;
;; As a stress test on the system, the programs here clear the grid and
;; then store the value 1 in square 1. Then, in the same allocation order
;; as shown above, they store the sum of the values in all adjacent
;; squares, including diagonals.
;;
;; So, the first few squares' values are chosen as follows:
;;
;;     Square 1 starts with the value 1.  Square 2 has only one adjacent
;;     filled square (with value 1), so it also stores 1.  Square 3 has
;;     both of the above squares as neighbors and stores the sum of their
;;     values, 2.  Square 4 has all three of the aforementioned squares as
;;     neighbors and stores the sum of their values, 4.  Square 5 only has
;;     the first and fourth squares as neighbors, so it gets the value 5.
;;
;; Once a square is written, its value does not change. Therefore, the
;; first few squares would receive the following values:
;;
;; 147 142 133 122 59 304 5 4 2 57 330 10 1 1 54 351 11 23 25 26 362 747
;; 806---> ...
;;
;; What is the first value written that is larger than your puzzle input?
;;
;; Your puzzle answer was 349975.

(defn odd-squares-for [n]
  (->> circular-memory
       (take-while
        (fn [[_ _ _ lr]] (<= lr (->> odd-squares
                                    (filter #(>= % n))
                                    first))))
       reverse
       ))

(defn left-index [n]
  (let [[[ur ul ll lr] [nur _ _ _]] (take 2 (odd-squares-for n))
        first-n (inc (- lr (* 4 (- lr ll))))]
    (cond
      (<= n 1) 0
      ;; Right border
      (= n first-n) (dec n)
      (< n ur) (- nur (- ur n 1))
      ;; Upper border
      (and (>= n ur) (< n ul)) (inc n)
      ;; Lower border
      (and (> n ll) (<= n lr)) (dec n)
      :else 0)))

(defn right-index [n]
  (let [[[ur ul ll lr] [_ nul _ _]] (take 2 (odd-squares-for n))]
    (cond
      (<= n 1) 0
      ;; Left border
      (and (> n ul) (< n ll)) (+ (- n ul 1) nul)
      ;; Upper border
      (and (<= n ul) (> n ur)) (dec n)
      ;; Lower border
      (and (>= n ll) (< n lr)) (inc n)
      :else 0)))

(defn up-index [n]
  (let [[[ur ul ll lr] [_ _ nll _]] (take 2 (odd-squares-for n))]
    (cond
      (<= n 1) 0
      ;; Right border
      (< n ur) (inc n)
      ;; Left border
      (and (> n ul) (<= n ll)) (dec n)
      ;; Lower border
      (and (< n lr) (> n ll)) (+ (- n ll 1) nll)
      (= n lr) (inc (- n (* 4 (- lr ll))))
      :else 0)))

(defn down-index [n]
  (let [[[ur ul ll lr] [nur _ _ _]] (take 2 (odd-squares-for n))]
    (let [first-n (inc (- lr (* 4 (- lr ll))))]
      (cond
        (<= n 1) 0
        ;; Right border
        (= n first-n) lr
        (and (> first-n) (<= n ur)) (dec n)
        ;; Left border
        (and (>= n ul) (< n ll)) (inc n)
        ;; Upper border
        (and (> n ur) (< n ul)) (+ (- n ur 1) nur)
        :else 0))))

(defn ul-index [n]
  (max
    (left-index (up-index n))
    (up-index (left-index n))))

(defn ur-index [n]
  (max
    (up-index (right-index n))
    (right-index (up-index n))))

(defn ll-index [n]
  (max
    (left-index (down-index n))
    (down-index (left-index n))))

(defn lr-index [n]
  (max
    (down-index (right-index n))
    (right-index (down-index n))))

(defn determine-neighbour-sums [n]
  (loop [i 2
         last-sum 0
         sums [0 1]]
    (if (> last-sum n)
      [last-sum sums i]
      (let [fields ((juxt ul-index up-index ur-index right-index lr-index down-index ll-index left-index) i)
            sum (reduce + (map #(get sums % 0) fields))]
        (recur (inc i) sum (conj sums sum))))))

#_(determine-neighbour-sums 347991)
;=> [349975 ....
