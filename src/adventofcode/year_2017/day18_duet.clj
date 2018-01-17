(ns adventofcode.year-2017.day18-duet
  "Day18 solution - part 1")

;; --- Day 18: Duet ---
;;
;; You discover a tablet containing some strange assembly code labeled
;; simply "Duet". Rather than bother the sound card with it, you decide to
;; run the code yourself. Unfortunately, you don't see any documentation,
;; so you're left to figure out what the instructions mean on your own.
;;
;; It seems like the assembly is meant to operate on a set of registers
;; that are each named with a single letter and that can each hold a single
;; integer. You suppose each register should start with a value of 0.
;;
;; There aren't that many instructions, so it shouldn't be hard to figure
;; out what they do. Here's what you determine:
;;
;;     snd X plays a sound with a frequency equal to the value of X.  set X
;;     Y sets register X to the value of Y.  add X Y increases register X
;;     by the value of Y.  mul X Y sets register X to the result of
;;     multiplying the value contained in register X by the value of Y.
;;     mod X Y sets register X to the remainder of dividing the value
;;     contained in register X by the value of Y (that is, it sets X to the
;;     result of X modulo Y).  rcv X recovers the frequency of the last
;;     sound played, but only when the value of X is not zero. (If it is
;;     zero, the command does nothing.)  jgz X Y jumps with an offset of
;;     the value of Y, but only if the value of X is greater than zero. (An
;;     offset of 2 skips the next instruction, an offset of -1 jumps to the
;;     previous instruction, and so on.)
;;
;; Many of the instructions can take either a register (a single letter) or
;; a number. The value of a register is the integer it contains; the value
;; of a number is that number.
;;
;; After each jump instruction, the program continues with the instruction
;; to which the jump jumped. After any other instruction, the program
;; continues with the next instruction. Continuing (or jumping) off either
;; end of the program terminates it.
;;
;; For example:
;;
;; set a 1 add a 2 mul a a mod a 5 snd a set a 0 rcv a jgz a -1 set a 1 jgz
;; a -2
;;
;;     The first four instructions set a to 1, add 2 to it, square it, and
;;     then set it to itself modulo 5, resulting in a value of 4.  Then, a
;;     sound with frequency 4 (the value of a) is played.  After that, a is
;;     set to 0, causing the subsequent rcv and jgz instructions to both be
;;     skipped (rcv because a is 0, and jgz because a is not greater than
;;     0).  Finally, a is set to 1, causing the next jgz instruction to
;;     activate, jumping back two instructions to another jump, which jumps
;;     again to the rcv, which ultimately triggers the recover operation.
;;
;; At the time the recover operation is executed, the frequency of the last
;; sound played is 4.
;;
;; What is the value of the recovered frequency (the value of the most
;; recently played sound) the first time a rcv instruction is executed with
;; a non-zero value?
;;
;; Your puzzle answer was 1187.

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

(def sound (atom 0))

(defmulti command (fn [c _ _] (c :command)))

(defmethod command "set" [c registers pc]
  [(assoc registers (.get-register (:v1 c))
                    (.get-value (:v2 c) registers))
    (inc pc)])

(defmethod command "add" [c registers pc]
  [(assoc registers (.get-register (:v1 c))
                    (+ (.get-value (:v1 c) registers)
                       (.get-value (:v2 c) registers)))
  (inc pc)])

(defmethod command "snd" [c registers pc]
  (reset! sound (.get-value (:v1 c) registers))
  [registers
  (inc pc)])

(defmethod command "mul" [c registers pc]
  [(assoc registers (.get-register (:v1 c))
                    (* (.get-value (:v1 c) registers)
                       (.get-value (:v2 c) registers)))
  (inc pc)])

(defmethod command "mod" [c registers pc]
  [(assoc registers (.get-register (:v1 c))
                    (mod (.get-value (:v1 c) registers)
                         (.get-value (:v2 c) registers)))
  (inc pc)])

(defmethod command "rcv" [c registers pc]
  [registers
   (if (> (.get-value (:v1 c) registers) 0) -1 (inc pc))])

(defmethod command "jgz" [c registers pc]
  [registers
   (if (> (.get-value (:v1 c) registers) 0)
     (+ pc (.get-value (:v2 c) registers))
     (inc pc))])

(defn parse-commands [commands-str]
  (let [commands (re-seq #"([a-z]{3}) ([a-z]{1})( -*[a-z0-9]*){0,1}" commands-str)
        commands (map rest commands)
        commands (vec (map (fn [[command v1 v2]]
                             (assoc {} :command command
                                       :v1 (make-value v1)
                                       :v2 (make-value v2)))
                           commands))]
    commands))

(defn execute-commands [commands]
  (loop [current-command-index 0 registers {}]
    (let [c (nth commands current-command-index :terminate)]
      (if (= :terminate c)
        @sound
        ;registers
        (let [[registers next-command-index] (command c registers current-command-index)]
          (recur next-command-index registers))))))

#_(execute-commands (parse-commands "
set a 1
add a 2
mul a a
mod a 5
snd a
set a 0
rcv a
jgz a -1
set a 1
jgz a -2"))

#_(execute-commands (parse-commands (slurp "resources/year_2017/day18_duet")))
;=> 1187
