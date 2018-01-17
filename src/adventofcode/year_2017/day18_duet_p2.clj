(ns adventofcode.year-2017.day18-duet-p2
  "Day 18 solution - part 2")

;; Day 18
;; --- Part Two ---
;;
;; As you congratulate yourself for a job well done, you notice that the
;; documentation has been on the back of the tablet this entire time. While
;; you actually got most of the instructions correct, there are a few key
;; differences. This assembly code isn't about sound at all - it's meant to
;; be run twice at the same time.
;;
;; Each running copy of the program has its own set of registers and
;; follows the code independently - in fact, the programs don't even
;; necessarily run at the same speed. To coordinate, they use the send
;; (snd) and receive (rcv) instructions:
;;
;;     snd X sends the value of X to the other program. These values wait
;;     in a queue until that program is ready to receive them. Each program
;;     has its own message queue, so a program can never receive a message
;;     it sent.  rcv X receives the next value and stores it in register
;;     X. If no values are in the queue, the program waits for a value to
;;     be sent to it. Programs do not continue to the next instruction
;;     until they have received a value. Values are received in the order
;;     they are sent.
;;
;; Each program also has its own program ID (one 0 and the other 1); the
;; register p should begin with this value.
;;
;; For example:
;;
;; snd 1 snd 2 snd p rcv a rcv b rcv c rcv d
;;
;; Both programs begin by sending three values to the other. Program 0
;; sends 1, 2, 0; program 1 sends 1, 2, 1. Then, each program receives a
;; value (both 1) and stores it in a, receives another value (both 2) and
;; stores it in b, and then each receives the program ID of the other
;; program (program 0 receives 1; program 1 receives 0) and stores it in
;; c. Each program now sees a different value in its own copy of register
;; c.
;;
;; Finally, both programs try to rcv a fourth time, but no data is waiting
;; for either of them, and they reach a deadlock. When this happens, both
;; programs terminate.
;;
;; It should be noted that it would be equally valid for the programs to
;; run at different speeds; for example, program 0 might have sent all
;; three values and then stopped at the first rcv before program 1 executed
;; even its first instruction.
;;
;; Once both of your programs have terminated (regardless of what caused
;; them to do so), how many times did program 1 send a value?
;;
;; Your puzzle answer was 5969.

(def pgm-send-count (atom [0 0]))
(def pgm-receive-count (atom [0 0]))

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

(defprotocol Queue
  (send [this p v] "")
  (receive [this p] "")
  (deadlock? [this] ""))

(defrecord SendReceiveQueue [q1 q2]

  Queue

  (send [this p v]
    (swap! pgm-send-count #(assoc % p (inc (nth % p))))
    (if (= 0 p)
      (swap! (:q1 this) conj v)
      (swap! (:q2 this) conj v)))

  (receive [this p]
    (swap! pgm-receive-count #(assoc % p (inc (nth % p))))
    (if (= 0 p)
      (let [v (peek (deref (:q2 this)))
            _ (swap! (:q2 this) pop)]
        v)
      (let [v (peek (deref (:q1 this)))
            _ (swap! (:q1 this) pop)]
        v)))

  (deadlock? [this]
    (= (nil? (peek (deref (:q1 this)))))
       (nil? (peek (deref (:q2 this))))))

(defn make-queue []
  (SendReceiveQueue. (atom clojure.lang.PersistentQueue/EMPTY)
                                  (atom clojure.lang.PersistentQueue/EMPTY)))

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
  (.send (:queue registers) (:p registers) (.get-value (:v1 c) registers))
  [registers (inc pc)])

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
  (let [queue-value (.receive (:queue registers) (:p registers))]
    (if (nil? queue-value)
      [registers pc]
      [(assoc registers (.get-register (:v1 c)) queue-value) (inc pc)])))

(defmethod command "jgz" [c registers pc]
  [registers
   (if (> (.get-value (:v1 c) registers) 0)
     (+ pc (.get-value (:v2 c) registers))
     (inc pc))])

(defn parse-commands [commands-str]
  (let [commands (re-seq #"([a-z]{3}) ([0-9a-z]{1})( -*[a-z0-9]*){0,1}" commands-str)
        commands (map rest commands)
        commands (vec (map (fn [[command v1 v2]]
                             (assoc {} :command command
                                       :v1 (make-value v1)
                                       :v2 (make-value v2)))
                           commands))]
    commands))

(defn execute-commands [commands]
  (loop [current-command-index-a 0
         current-command-index-b 0
         queue (make-queue)
         registers-a {:queue queue :p 0 "p" 0}
         registers-b {:queue queue :p 1 "p" 1}
         deadlock? false]
    (let [ca (nth commands current-command-index-a :terminate)
          cb (nth commands current-command-index-b :terminate)]
      (if (or (= :terminate ca) (= :terminate cb) deadlock?)
        [@pgm-send-count @pgm-receive-count (dissoc registers-a :queue) (dissoc registers-b :queue)]
        (let [[registers-a next-command-index-a] (command ca registers-a current-command-index-a)
              [registers-b next-command-index-b] (command cb registers-b current-command-index-b)]
          (recur next-command-index-a
                 next-command-index-b
                 queue
                 registers-a
                 registers-b
                 (and (= current-command-index-a next-command-index-a)
                      (= current-command-index-b next-command-index-b))))))))

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
;=> [[6096 5969] [5970 7280] {:p 0, "p" -76, "i" 0, "a" 35, "b" 35, "f" 0} {:p 1, "p" -76, "i" 0, "a" 35, "f" 0, "b" 35}]
