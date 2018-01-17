(ns adventofcode.year-2017.day23-coprozessor-conflagration
  "Day23 solution")

;; --- Day 23: Coprocessor Conflagration ---
;;
;; You decide to head directly to the CPU and fix the printer from
;; there. As you get close, you find an experimental coprocessor doing so
;; much work that the local programs are afraid it will halt and catch
;; fire. This would cause serious issues for the rest of the computer, so
;; you head in and see what you can do.
;;
;; The code it's running seems to be a variant of the kind you saw recently
;; on that tablet. The general functionality seems very similar, but some
;; of the instructions are different:
;;
;;     set X Y sets register X to the value of Y.
;;     sub X Y decreases register X by the value of Y.
;;     mul X Y sets register X to the result of multiplying the value contained
;;             in register X by the value of Y.
;;     jnz X Y jumps with an offset of the value of Y, but only if the value
;;             of X is not zero. (An offset of 2 skips the next instruction,
;;             an offset of -1 jumps to the previous instruction, and so on.)
;;
;; Only the instructions listed above are used. The eight registers here,
;; named a through h, all start at 0.
;;
;; The coprocessor is currently set to some kind of debug mode, which
;; allows for testing, but prevents it from doing any meaningful work.
;;
;; If you run the program (your puzzle input), how many times is the mul
;; instruction invoked?
;;
;; Your puzzle answer was 4225.
;;
;; --- Part Two ---
;;
;; Now, it's time to fix the problem.
;;
;; The debug mode switch is wired directly to register a. You flip the
;; switch, which makes register a now start at 1 when the program is
;; executed.
;;
;; Immediately, the coprocessor begins to overheat. Whoever wrote this
;; program obviously didn't choose a very efficient implementation. You'll
;; need to optimize the program if it has any hope of completing before
;; Santa needs that printer working.
;;
;; The coprocessor's ultimate goal is to determine the final value left in
;; register h once the program completes. Technically, if it had that... it
;; wouldn't even need to run the program.
;;
;; After setting register a to 1, if the program were to run to completion,
;; what value would be left in register h?
;;
;; Your puzzle answer was 905.


(defprotocol Value
  (get-value [this _] "the value")
  (get-register [this] "the register"))

(defrecord LiteralValue [v]
  Value
  (get-value [this _] (:v this))
  (get-register [this] (throw (IllegalStateException. (str "No register: " (:v this))))))

(defrecord RegisterValue [r]
  Value
  (get-value [this registers] (get registers (:r this) 0))
  (get-register [this] (:r this)))

(defrecord NullValue [r]
  Value
  (get-value [_ _] (throw (IllegalStateException. "Null value")))
  (get-register [_] (throw (IllegalStateException. "Null value"))))

(defn make-value [v]
  (if (nil? v)
    (NullValue. v)
    (if (java.lang.Character/isLetter (first (seq (.trim v))))
      (RegisterValue. (.trim v))
      (LiteralValue. (Integer/parseInt (.trim v))))))

(def mul-count (atom 0))

(defmulti command (fn [c _ _] (c :command)))

(defmethod command "set" [c registers pc]
  "set X Y sets register X to the value of Y."
  [(assoc registers (.get-register (:v1 c))
                    (.get-value (:v2 c) registers))
   (inc pc)])

(defmethod command "add" [c registers pc]
  "add X Y increases register X by the value of Y."
  [(assoc registers (.get-register (:v1 c))
                    (+ (.get-value (:v1 c) registers)
                       (.get-value (:v2 c) registers)))
   (inc pc)])

(defmethod command "sub" [c registers pc]
  "sub X Y decreases register X by the value of Y."
  [(assoc registers (.get-register (:v1 c))
                    (- (.get-value (:v1 c) registers)
                       (.get-value (:v2 c) registers)))
   (inc pc)])

(defmethod command "mul" [c registers pc]
  "mul X Y sets register X to the result of multiplying
  the value contained in register X by the value of Y."
  (swap! mul-count inc)
  [(assoc registers (.get-register (:v1 c))
                    (* (.get-value (:v1 c) registers)
                       (.get-value (:v2 c) registers)))
   (inc pc)])

(defmethod command "mod" [c registers pc]
  "mod X Y sets register X to the remainder of dividing
   the value contained in register X by the value of Y
   (that is, it sets X to the result of X modulo Y)."
  [(assoc registers (.get-register (:v1 c))
                    (mod (.get-value (:v1 c) registers)
                         (.get-value (:v2 c) registers)))
   (inc pc)])

(defmethod command "jgz" [c registers pc]
  "jgz X Y jumps with an offset of the value of Y,
   but only if the value of X is greater than zero.
   (An offset of 2 skips the next instruction, an
   offset of -1 jumps to the previous instruction,
   and so on.)"
  [registers
   (if (> (.get-value (:v1 c) registers) 0)
     (+ pc (.get-value (:v2 c) registers))
     (inc pc))])

(defmethod command "jnz" [c registers pc]
  "jnz X Y jumps with an offset of the value of Y,
   but only if the value of X is not zero.
   (An offset of 2 skips the next instruction, an
   offset of -1 jumps to the previous instruction,
   and so on.)"
  [registers
   (if (not= (.get-value (:v1 c) registers) 0)
     (+ pc (.get-value (:v2 c) registers))
     (inc pc))])

(defn parse-commands [commands-str]
  (let [commands (re-seq #"([a-z]{3}) (-*[a-z0-9]{1})( -*[a-z0-9]*){0,1}" commands-str)
        commands (map rest commands)
        commands (vec (map (fn [[command v1 v2]]
                             (assoc {} :command command
                                       :v1 (make-value v1)
                                       :v2 (make-value v2)))
                           commands))]
    commands))

(defn execute-commands [commands]
  (reset! mul-count 0)
  (loop [current-command-index 0 registers {"a" 0} i 0]
    (let [c (nth commands current-command-index :terminate)]
      (if (or (= :terminate c))
        @mul-count
        ;registers
        (let [[registers next-command-index] (command c registers current-command-index)]
          #_(when (= 0 (mod i 1000))
            (println (str registers " current-pc " (inc current-command-index) " next-pc " (inc next-command-index))))
          (recur next-command-index registers (inc i)))))))

#_(execute-commands (parse-commands (slurp "resources/year_2017/day23_coprozessor_conflagration")))
;; Part 1
;=> 4225


;; Part 2 - It's a brute force primality check.
;; For every seventeenth number x between 106700 and 123701,
;; is checking whether any number less than x divides x' and if so,
;; adds 1 to a running total, to see how many of those x values are
;; composite.

(defn divisible? [a b]
  (zero? (mod a b)))

(defn prime? [n]
  (and (> n 1) (not-any? (partial divisible? n) (range 2 n))))

#_(- (count (range 106700 123701 17)) (count (filter prime? (range 106700 123701 17))))
;=> 905
