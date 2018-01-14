(ns adventofcode.year-2017.day01_day02_day03_day04)

;; --- Day 1: Inverse Captcha ---
;;
;; You're standing in a room with "digitization quarantine" written in
;; LEDs along one wall. The only door is locked, but it includes a small
;; interface. "Restricted Area - Strictly No Digitized Users Allowed."
;;
;; It goes on to explain that you may only leave by solving a captcha to
;; prove you're not a human. Apparently, you only get one millisecond to
;; solve the captcha: too fast for a normal human, but it feels like hours
;; to you.
;;
;; The captcha requires you to review a sequence of digits (your puzzle
;; input) and find the sum of all digits that match the next digit in the
;; list. The list is circular, so the digit after the last digit is the
;; first digit in the list.
;;
;; For example:
;;
;; 1122 produces a sum of 3 (1 + 2) because the first digit (1) matches
;; the second digit and the third digit (2) matches the fourth digit.
;; 1111 produces 4 because each digit (all 1) matches the next.
;; 1234 produces 0 because no digit matches the next.
;; 91212129 produces 9 because the only digit that matches the next one is
;; the last digit, 9.
;;
;; What is the solution to your captcha?
;;
;; Your puzzle answer was 1393.
;; --- Part Two ---
;;
;; You notice a progress bar that jumps to 50% completion. Apparently, the
;; door isn't yet satisfied, but it did emit a star as encouragement. The
;; instructions change:
;;
;; Now, instead of considering the next digit, it wants you to consider
;; the digit halfway around the circular list. That is, if your list
;; contains 10 items, only include a digit in your sum if the digit 10/2 =
;; 5 steps forward matches it. Fortunately, your list has an even number
;; of elements.
;;
;; For example:
;;
;; 1212 produces 6: the list contains 4 items, and all four digits match
;; the digit 2 items ahead.
;; 1221 produces 0, because every comparison is between a 1 and a 2.
;; 123425 produces 4, because both 2s match each other, but no other digit
;; has a match.
;; 123123 produces 12.
;; 12131415 produces 4.
;;
;; What is the solution to your new captcha?
;;
;; Your puzzle answer was 1292.

(let [input (seq "5994521226795838486188872189952551475352929145357284983463678944777228139398117649129843853837124228353689551178129353548331779783742915361343229141538334688254819714813664439268791978215553677772838853328835345484711229767477729948473391228776486456686265114875686536926498634495695692252159373971631543594656954494117149294648876661157534851938933954787612146436571183144494679952452325989212481219139686138139314915852774628718443532415524776642877131763359413822986619312862889689472397776968662148753187767793762654133429349515324333877787925465541588584988827136676376128887819161672467142579261995482731878979284573246533688835226352691122169847832943513758924194232345988726741789247379184319782387757613138742817826316376233443521857881678228694863681971445442663251423184177628977899963919997529468354953548612966699526718649132789922584524556697715133163376463256225181833257692821331665532681288216949451276844419154245423434141834913951854551253339785533395949815115622811565999252555234944554473912359674379862182425695187593452363724591541992766651311175217218144998691121856882973825162368564156726989939993412963536831593196997676992942673571336164535927371229823236937293782396318237879715612956317715187757397815346635454412183198642637577528632393813964514681344162814122588795865169788121655353319233798811796765852443424783552419541481132132344487835757888468196543736833342945718867855493422435511348343711311624399744482832385998592864795271972577548584967433917322296752992127719964453376414665576196829945664941856493768794911984537445227285657716317974649417586528395488789946689914972732288276665356179889783557481819454699354317555417691494844812852232551189751386484638428296871436139489616192954267794441256929783839652519285835238736142997245189363849356454645663151314124885661919451447628964996797247781196891787171648169427894282768776275689124191811751135567692313571663637214298625367655969575699851121381872872875774999172839521617845847358966264291175387374464425566514426499166813392768677233356646752273398541814142523651415521363267414564886379863699323887278761615927993953372779567675")
      inputs (map #(- (int %) (int \0)) input)
      followup-digits (map (fn [x y] (if (= x y) x 0)) inputs (rest (cycle inputs)))
      sum (reduce + followup-digits)]
  sum)


(let [input (seq "5994521226795838486188872189952551475352929145357284983463678944777228139398117649129843853837124228353689551178129353548331779783742915361343229141538334688254819714813664439268791978215553677772838853328835345484711229767477729948473391228776486456686265114875686536926498634495695692252159373971631543594656954494117149294648876661157534851938933954787612146436571183144494679952452325989212481219139686138139314915852774628718443532415524776642877131763359413822986619312862889689472397776968662148753187767793762654133429349515324333877787925465541588584988827136676376128887819161672467142579261995482731878979284573246533688835226352691122169847832943513758924194232345988726741789247379184319782387757613138742817826316376233443521857881678228694863681971445442663251423184177628977899963919997529468354953548612966699526718649132789922584524556697715133163376463256225181833257692821331665532681288216949451276844419154245423434141834913951854551253339785533395949815115622811565999252555234944554473912359674379862182425695187593452363724591541992766651311175217218144998691121856882973825162368564156726989939993412963536831593196997676992942673571336164535927371229823236937293782396318237879715612956317715187757397815346635454412183198642637577528632393813964514681344162814122588795865169788121655353319233798811796765852443424783552419541481132132344487835757888468196543736833342945718867855493422435511348343711311624399744482832385998592864795271972577548584967433917322296752992127719964453376414665576196829945664941856493768794911984537445227285657716317974649417586528395488789946689914972732288276665356179889783557481819454699354317555417691494844812852232551189751386484638428296871436139489616192954267794441256929783839652519285835238736142997245189363849356454645663151314124885661919451447628964996797247781196891787171648169427894282768776275689124191811751135567692313571663637214298625367655969575699851121381872872875774999172839521617845847358966264291175387374464425566514426499166813392768677233356646752273398541814142523651415521363267414564886379863699323887278761615927993953372779567675")
      inputs (map #(- (int %) (int \0)) input)
      followup-digits (map (fn [x y] (if (= x y) x 0)) inputs (drop (/ (count inputs) 2) (cycle inputs)))
      sum (reduce + followup-digits)]
  sum)



;; --- Day 2: Corruption Checksum ---
;;
;; As you walk through the door, a glowing humanoid shape yells in your
;; direction.
;;
;; "You there! Your state appears to be idle. Come help us
;; repair the corruption in this spreadsheet - if we take another
;; millisecond, we'll have to display an hourglass cursor!"
;;
;; The spreadsheet consists of rows of apparently-random numbers. To make
;; sure the recovery process is on the right track, they need you to
;; calculate the spreadsheet's checksum. For each row, determine the
;; difference between the largest value and the smallest value; the
;; checksum is the sum of all of these differences.
;;
;; For example, given the following spreadsheet:
;;
;; 5 1 9 5 7 5 3 2 4 6 8
;;
;;     The first row's largest and smallest values are 9 and 1, and their
;;     difference is 8.  The second row's largest and smallest values are 7
;;     and 3, and their difference is 4.  The third row's difference is 6.
;;
;; In this example, the spreadsheet's checksum would be 8 + 4 + 6 = 18.
;;
;; What is the checksum for the spreadsheet in your puzzle input?
;;
;; Your puzzle answer was 58975.

(let [input [[5 1 9 5]
             [7 5 3]
             [2 4 6 8]]
      maxs (map #(apply max %) input)
      mins (map #(apply min %) input)
      diffs (map #(- %1 %2) maxs mins)
      sum (reduce + diffs)]
  sum)


(let [input (partition 16 [5048 177 5280 5058 4504 3805 5735 220 4362 1809 1521 230 772 1088 178 1794
                           6629 3839 258 4473 5961 6539 6870 4140 4638 387 7464 229 4173 5706 185 271
                           5149 2892 5854 2000 256 3995 5250 249 3916 184 2497 210 4601 3955 1110 5340
                           153 468 550 126 495 142 385 144 165 188 609 182 439 545 608 319
                           1123 104 567 1098 286 665 1261 107 227 942 1222 128 1001 122 69 139
                           111 1998 1148 91 1355 90 202 1522 1496 1362 1728 109 2287 918 2217 1138
                           426 372 489 226 344 431 67 124 120 386 348 153 242 133 112 369
                           1574 265 144 2490 163 749 3409 3086 154 151 133 990 1002 3168 588 2998
                           173 192 2269 760 1630 215 966 2692 3855 3550 468 4098 3071 162 329 3648
                           1984 300 163 5616 4862 586 4884 239 1839 169 5514 4226 5551 3700 216 5912
                           1749 2062 194 1045 2685 156 3257 1319 3199 2775 211 213 1221 198 2864 2982
                           273 977 89 198 85 1025 1157 1125 69 94 919 103 1299 998 809 478
                           1965 6989 230 2025 6290 2901 192 215 4782 6041 6672 7070 7104 207 7451 5071
                           1261 77 1417 1053 2072 641 74 86 91 1878 1944 2292 1446 689 2315 1379
                           296 306 1953 3538 248 1579 4326 2178 5021 2529 794 5391 4712 3734 261 4362
                           2426 192 1764 288 4431 2396 2336 854 2157 216 4392 3972 229 244 4289 1902])
      maxs (map #(apply max %) input)
      _ (println maxs)
      mins (map #(apply min %) input)
      diffs (map #(- %1 %2) maxs mins)
      sum (reduce + diffs)]
  sum)

;; -- Part Two ---
;;
;; "Great work; looks like we're on the right track after all. Here's a
;; tar for your effort." However, the program seems a little worried. Can
;; rograms be worried?
;;
;; "Based on what we're seeing, it looks like all the User wanted is some
;; nformation about the evenly divisible values in the
;; preadsheet. Unfortunately, none of us are equipped for that kind of
;; alculation - most of us specialize in bitwise operations."
;;
;; t sounds like the goal is to find the only two numbers in each row
;; here one evenly divides the other - that is, where the result of the
;; ivision operation is a whole number. They would like you to find those
;; umbers on each line, divide them, and add up each line's result.
;;
;; or example, given the following spreadsheet:
;;
;;  9 2 8 9 4 7 3 3 8 6 5
;;
;;    In the first row, the only two numbers that evenly divide are 8 and
;;    2; the result of this division is 4.  In the second row, the two
;;    numbers are 9 and 3; the result is 3.  In the third row, the result
;;    is 2.
;;
;; n this example, the sum of the results would be 4 + 3 + 2 = 9.
;;
;; hat is the sum of each row's result in your puzzle input?
;;
;; our puzzle answer was 308.

(defn evenly-dividable? [x y]
  (= 0 (rem x y)))

(defn evenly-dividables-in-row [row]
  (for [x row
        y row
        :when (and (not= x y) (evenly-dividable? x y))]
    [x y]))

(let [input (partition 16 [5048 177 5280 5058 4504 3805 5735 220 4362 1809 1521 230 772 1088 178 1794
                           6629 3839 258 4473 5961 6539 6870 4140 4638 387 7464 229 4173 5706 185 271
                           5149 2892 5854 2000 256 3995 5250 249 3916 184 2497 210 4601 3955 1110 5340
                           153 468 550 126 495 142 385 144 165 188 609 182 439 545 608 319
                           1123 104 567 1098 286 665 1261 107 227 942 1222 128 1001 122 69 139
                           111 1998 1148 91 1355 90 202 1522 1496 1362 1728 109 2287 918 2217 1138
                           426 372 489 226 344 431 67 124 120 386 348 153 242 133 112 369
                           1574 265 144 2490 163 749 3409 3086 154 151 133 990 1002 3168 588 2998
                           173 192 2269 760 1630 215 966 2692 3855 3550 468 4098 3071 162 329 3648
                           1984 300 163 5616 4862 586 4884 239 1839 169 5514 4226 5551 3700 216 5912
                           1749 2062 194 1045 2685 156 3257 1319 3199 2775 211 213 1221 198 2864 2982
                           273 977 89 198 85 1025 1157 1125 69 94 919 103 1299 998 809 478
                           1965 6989 230 2025 6290 2901 192 215 4782 6041 6672 7070 7104 207 7451 5071
                           1261 77 1417 1053 2072 641 74 86 91 1878 1944 2292 1446 689 2315 1379
                           296 306 1953 3538 248 1579 4326 2178 5021 2529 794 5391 4712 3734 261 4362
                           2426 192 1764 288 4431 2396 2336 854 2157 216 4392 3972 229 244 4289 1902])
      dividables-in-row (map first (map evenly-dividables-in-row input))
      divisions (map #(apply / %) dividables-in-row)
      sum (reduce + divisions)
      ]
  sum)


;;--- Day 3: Spiral Memory ---
;;
;;You come across an experimental new kind of memory stored on an infinite
;;two-dimensional grid.
;;
;;Each square on the grid is allocated in a spiral pattern starting at a
;;location marked 1 and then counting up while spiraling outward. For
;;example, the first few squares are allocated like this:
;;
;;17 16 15 14 13 18 5 4 3 12 19 6 1 2 11 20 7 8 9 10 21 22 23---> ...
;;
;;While this is very space-efficient (no squares are skipped), requested
;;data must be carried back to square 1 (the location of the only access
;;port for this memory system) by programs that can only move up, down,
;;left, or right. They always take the shortest path: the Manhattan
;;Distance between the location of the data and square 1.
;;
;;For example:
;;
;;    Data from square 1 is carried 0 steps, since it's at the access
;;    port.  Data from square 12 is carried 3 steps, such as: down, left,
;;    left.  Data from square 23 is carried only 2 steps: up twice.  Data
;;    from square 1024 must be carried 31 steps.
;;
;;How many steps are required to carry the data from the square identified
;;in your puzzle input all the way to the access port?
;;
;;Your puzzle answer was 480.
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
  (map *
       (filter odd? (range))
       (filter odd? (range))))


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
  (let [next-odd-square (first (filter #(>= % n) odd-squares))
        circular-space (take-while (fn [[_ _ _ lr]] (<= lr next-odd-square)) circular-memory)
        circular-space (reverse circular-space)]
    circular-space))

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

(determine-neighbour-sums 347991)

; 543
; 612
; 789


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

(let [lines (clojure.string/split (slurp "./resources/day04_passphrases") #"\n")]
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

(let [lines (clojure.string/split (slurp "./resources/day04_passphrases") #"\n")]
  (count (filter true? (map #(and (valid-anagram-passphrase? %)
                                  (valid-passphrase? %)) lines))))
