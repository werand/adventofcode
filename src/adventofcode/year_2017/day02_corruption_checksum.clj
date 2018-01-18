(ns adventofcode.year-2017.day02-corruption-checksum
  "Day02 solution")

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

#_(let [input [[5 1 9 5]
             [7 5 3]
             [2 4 6 8]]
      maxs (map #(apply max %) input)
      mins (map #(apply min %) input)
      diffs (map #(- %1 %2) maxs mins)
      sum (reduce + diffs)]
  sum)


#_(let [input (partition 16 [5048 177 5280 5058 4504 3805 5735 220 4362 1809 1521 230 772 1088 178 1794
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
;=> 58975

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

#_(let [input (partition 16 [5048 177 5280 5058 4504 3805 5735 220 4362 1809 1521 230 772 1088 178 1794
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
;=> 308
