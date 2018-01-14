;; --- Day 8: I Heard You Like Registers ---
;;
;; You receive a signal directly from the CPU. Because of your recent
;; assistance with jump instructions, it would like you to compute the
;; result of a series of unusual register instructions.
;;
;; Each instruction consists of several parts: the register to modify,
;; whether to increase or decrease that register's value, the amount by
;; which to increase or decrease it, and a condition. If the condition
;; fails, skip the instruction without modifying the register. The
;; registers all start at 0. The instructions look like this:
;;
;; b inc 5 if a > 1 a inc 1 if b < 5 c dec -10 if a >= 1 c inc -20 if c ==
;; 10
;;
;; These instructions would be processed as follows:
;;
;;     Because a starts at 0, it is not greater than 1, and so b is not
;;     modified.  a is increased by 1 (to 1) because b is less than 5 (it
;;     is 0).  c is decreased by -10 (to 10) because a is now greater than
;;     or equal to 1 (it is 1).  c is increased by -20 (to -10) because c
;;     is equal to 10.
;;
;; After this process, the largest value in any register is 1.
;;
;; You might also encounter <= (less than or equal to) or != (not equal
;; to). However, the CPU doesn't have the bandwidth to tell you what all
;; the registers are named, and leaves that to you to determine.
;;
;; What is the largest value in any register after completing the
;; instructions in your puzzle input?
;;
;; Your puzzle answer was 4416.
;;  --- Part Two ---
;;
;; To be safe, the CPU also needs to know the highest value held in any
;; register during this process so that it can decide how much memory to
;; allocate to these operations. For example, in the above instructions,
;; the highest value ever held was 10 (in register c after the third
;; instruction was evaluated).
;;
;; Your puzzle answer was 5199.

(defmulti compar (fn [m registers] (m :comparison)))
(defmethod compar "<" [m registers] (< (get registers (m :v1) 0) (m :v2)))
(defmethod compar "<=" [m registers] (<= (get registers (m :v1) 0) (m :v2)))
(defmethod compar "==" [m registers] (== (get registers (m :v1) 0) (m :v2)))
(defmethod compar ">=" [m registers] (>= (get registers (m :v1) 0) (m :v2)))
(defmethod compar ">" [m registers] (> (get registers (m :v1) 0) (m :v2)))
(defmethod compar "!=" [m registers] (not= (get registers (m :v1) 0) (m :v2)))

(defmulti f (fn [m registers] (m :operation)))
(defmethod f "inc" [m registers] (+ (get registers (m :register) 0) (m :amount)))
(defmethod f "dec" [m registers] (- (get registers (m :register) 0) (m :amount)))

(defn to-int [s]
 (java.lang.Integer/parseInt  s))

(defn process-input [input]
  (let [input-parts (re-seq #"([a-z]+) (inc|dec) ([\-0-9]+) if ([a-z]+) ([\<\>\=\!]+) ([\-0-9]+)" input)
        program (map #(let [[_ register operation amount v1 comparison v2] %]
                        (zipmap [:register :operation :amount :v1 :comparison :v2]
                                [register operation (to-int amount) v1 comparison (to-int v2)])) input-parts)]
    (loop [registers {"dummy" 0}
           line program
           max-register-value 0]
      (if (seq line)
        (if (compar (first line) registers)
          (recur (assoc registers (get (first line) :register) (f (first line) registers)) (rest line) (apply max (conj (vals registers) max-register-value)))
          (recur registers (rest line) max-register-value))
        [(apply max (vals registers)) max-register-value]))))

#_(process-input (slurp "resources/year_2017/day08_corruption_checksum"))
;=> [4416 5199]
