(ns adventofcode.year-2017.day01-high-entropy-password
  "Day04 solution")

;; --- Day 4: High-Entropy Passphrases ---
;;
;; A new system policy has been put in place that requires all accounts to
;; use a passphrase instead of simply a password. A passphrase consists of
;; a series of words (lowercase letters) separated by spaces.
;;
;; To ensure security, a valid passphrase must contain no duplicate words.
;;
;; For example:
;;
;;     aa bb cc dd ee is valid.  aa bb cc dd aa is not valid - the word aa
;;     appears more than once.  aa bb cc dd aaa is valid - aa and aaa count
;;     as different words.
;;
;; The system's full passphrase list is available as your puzzle input. How
;; many day04_passphrases are valid?
;;
;; Your puzzle answer was 477.
;;
;; --- Part Two ---
;;
;; For added security, yet another system policy has been put in
;; place. Now, a valid passphrase must contain no two words that are
;; anagrams of each other - that is, a passphrase is invalid if any word's
;; letters can be rearranged to form any other word in the passphrase.
;;
;; For example:
;;
;;     abcde fghij is a valid passphrase.  abcde xyz ecdab is not valid -
;;     the letters from the third word can be rearranged to form the first
;;     word.  a ab abc abd abf abj is a valid passphrase, because all
;;     letters need to be used when forming another word.  iiii oiii ooii
;;     oooi oooo is valid.  oiii ioii iioi iiio is not valid - any of these
;;     words can be rearranged to form any other word.
;;
;; Under this new system policy, how many day04_passphrases are valid?
;;
;; Your puzzle answer was 167.

(defn valid-passphrase? [p]
  (let [words (clojure.string/split p #" ")]
    (= (count words) (count (set words)))))

#_(let [lines (clojure.string/split (slurp "./resources/year_2017/day04_passphrases") #"\n")]
  (count (filter true? (map valid-passphrase? lines))))

;; Day 4 - Part 2

(defn count-char-occurences [w]
  (reduce #(assoc %1 %2 (inc (%1 %2 0))) {} (seq w)))

(defn anagram? [w1 w2]
  (and (= (count w1) (count w2))
       (= (count-char-occurences w1) (count-char-occurences w2))))

(defn valid-anagram-passphrase? [p]
  (let [words (clojure.string/split p #" ")]
    (if (some true? (for [w1 words w2 words :when (not= w1 w2)]
                      (anagram? w1 w2)))
      false
      true)))

#_(let [lines (clojure.string/split (slurp "./resources/year_2017/day04_passphrases") #"\n")]
  (count (filter true? (map #(and (valid-anagram-passphrase? %)
                                  (valid-passphrase? %)) lines))))
