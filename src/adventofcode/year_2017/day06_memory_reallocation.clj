(ns adventofcode.year-2017.day06-memory-reallocation
  "day06 solution")
;; --- Day 6: Memory Reallocation ---
;;
;; A debugger program here is having an issue: it is trying to repair a
;; memory reallocation routine, but it keeps getting stuck in an infinite
;; loop.
;;
;; In this area, there are sixteen memory banks; each memory bank can hold
;; any number of blocks. The goal of the reallocation routine is to balance
;; the blocks between the memory banks.
;;
;; The reallocation routine operates in cycles. In each cycle, it finds the
;; memory bank with the most blocks (ties won by the lowest-numbered memory
;; bank) and redistributes those blocks among the banks. To do this, it
;; removes all of the blocks from the selected bank, then moves to the next
;; (by index) memory bank and inserts one of the blocks. It continues doing
;; this until it runs out of blocks; if it reaches the last memory bank, it
;; wraps around to the first one.
;;
;; The debugger would like to know how many redistributions can be done
;; before a blocks-in-banks configuration is produced that has been seen
;; before.
;;
;; For example, imagine a scenario with only four memory banks:
;;
;;     The banks start with 0, 2, 7, and 0 blocks. The third bank has the
;;     most blocks, so it is chosen for redistribution.  Starting with the
;;     next bank (the fourth bank) and then continuing to the first bank,
;;     the second bank, and so on, the 7 blocks are spread out over the
;;     memory banks. The fourth, first, and second banks get two blocks
;;     each, and the third bank gets one back. The final result looks like
;;     this: 2 4 1 2.  Next, the second bank is chosen because it contains
;;     the most blocks (four). Because there are four memory banks, each
;;     gets one block. The result is: 3 1 2 3.  Now, there is a tie between
;;     the first and fourth memory banks, both of which have three
;;     blocks. The first bank wins the tie, and its three blocks are
;;     distributed evenly over the other three banks, leaving it with none:
;;     0 2 3 4.  The fourth bank is chosen, and its four blocks are
;;     distributed such that each of the four banks receives one: 1 3 4 1.
;;     The third bank is chosen, and the same thing happens: 2 4 1 2.
;;
;; At this point, we've reached a state we've seen before: 2 4 1 2 was
;; already seen. The infinite loop is detected after the fifth block
;; redistribution cycle, and so the answer in this example is 5.
;;
;; Given the initial block counts in your puzzle input, how many
;; redistribution cycles must be completed before a configuration is
;; produced that has been seen before?
;;
;; Your puzzle answer was 5042.

(defn round [i count]
  (int (Math/ceil (/ i count))))

(defn next-index [index count]
  (if (< (inc index) count)
    (inc index)
    0))

(defn distribute [n max-value index count]
  (loop [result (into [] (repeat count 0))
         sum 0
         current-index (next-index index count)]
    (if (= sum max-value)
      result
      (let [r (- max-value sum)
            next-index (next-index current-index count)]
        (if (< r n)
          (recur (assoc result current-index r)
                 (+ sum r)
                 next-index)
          (recur (assoc result current-index n)
                 (+ sum n)
                 next-index))))))

(defn rebalance [banks]
  (let [c (count banks)]
    (loop [banks banks
           steps #{}
           finished nil
           rebalances 0]
      (if (not (nil? finished))
        [rebalances]
        (let [max-value (apply max banks)
              max-index (loop [i 0] (if (= (get banks i) max-value) i (recur (inc i))))
              distribute-value (round max-value c)
              distribute (distribute distribute-value max-value max-index c)
              banks (assoc banks max-index 0)
              banks (into [] (map + banks distribute))
              finished? (some #{banks} steps)
              _ (when finished? (println banks))]
          (recur banks (conj steps banks) finished? (inc rebalances)))))))

#_(rebalance [0 2 7 0])
#_(rebalance [5	1	10	0	1	7	13	14	3	12	8	10	7	12	0	6])

;; --- Part Two ---
;;
;; Out of curiosity, the debugger would also like to know the size of the
;; loop: starting from a state that has already been seen, how many block
;; redistribution cycles must be performed before that same state is seen
;; again?
;;
;; In the example above, 2 4 1 2 is seen again after four cycles, and so
;; the answer in that example would be 4.
;;
;; How many cycles are in the infinite loop that arises from the
;; configuration in your puzzle input?
;;
;; Your puzzle answer was 1086.

(defn rebalance2 [banks]
  (let [c (count banks)]
    (loop [banks banks
           steps #{}
           finished nil
           rebalances 0
           cycle nil
           cycle-counter 0]
      (if finished
        [rebalances (- rebalances cycle-counter)]
        (let [max-value (apply max banks)
              max-index (loop [i 0] (if (= (get banks i) max-value) i (recur (inc i))))
              distribute-value (round max-value c)
              distribute (distribute distribute-value max-value max-index c)
              banks (assoc banks max-index 0)
              banks (into [] (map + banks distribute))
              finished? (some #{banks} steps)
              finished-after-one-cycle? (and (> cycle-counter 0) (not (nil? finished?)))
              cycle (if finished? banks cycle)
              cycle-counter (if (and finished? (not finished-after-one-cycle?))
                              rebalances
                              cycle-counter)
              steps (if (and finished? (not finished-after-one-cycle?)) #{} steps)
              ;_ (println (str steps " " banks " " cycle))
              ]
          (recur banks (conj steps banks) finished-after-one-cycle? (inc rebalances) cycle cycle-counter))))))

#_(rebalance2 [5	1	10	0	1	7	13	14	3	12	8	10	7	12	0	6])
; Ergebnis -1
