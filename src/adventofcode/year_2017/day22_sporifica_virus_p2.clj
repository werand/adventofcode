(ns adventofcode.year-2017.day22-sporifica-virus-p2
  "Day22  solution - part 2")

;; --- Day 22: Sporifica Virus ---
;;
;; Diagnostics indicate that the local grid computing cluster has been
;; contaminated with the Sporifica Virus. The grid computing cluster is
;; a seemingly-infinite two-dimensional grid of compute nodes. Each node
;; is either clean or infected by the virus.
;;
;; To prevent overloading the nodes (which would render them useless to
;; the virus) or detection by system administrators, exactly one virus
;; carrier moves through the network, infecting or cleaning nodes as it
;; moves. The virus carrier is always located on a single node in the
;; network (the current node) and keeps track of the direction it is
;; facing.
;;
;; To avoid detection, the virus carrier works in bursts; in each burst,
;; it wakes up, does some work, and goes back to sleep. The following
;; steps are all executed in order one time each burst:
;;
;;     If the current node is infected, it turns to its
;;     right. Otherwise, it turns to its left. (Turning is done
;;     in-place; the current node does not change.)
;;     If the current node is clean, it becomes infected. Otherwise, it
;;     becomes cleaned.
;;     (This is done after the node is considered for the purposes of changing direction.)
;;     The virus carrier moves forward one node in the direction it is facing.
;;
;; Diagnostics have also provided a map of the node infection
;; status (your puzzle input). Clean nodes are shown as .; infected
;; nodes are shown as #. This map only shows the center of the grid;
;; there are many more nodes beyond those shown, but none of them are
;; currently infected.
;;
;; The virus carrier begins in the middle of the map facing up.
;;
;; For example, suppose you are given a map like this:
;;
;; ..#
;; #..
;; ...
;;
;; Then, the middle of the infinite grid looks like this, with the virus
;; carrier's position marked with [ ]:
;;
;; . . . . . . . . .
;; . . . . . . . . .
;; . . . . . . . . .
;; . . . . . # . . .
;; . . . #[.]. . . .
;; . . . . . . . . .
;; . . . . . . . . .
;; . . . . . . . . .
;;
;; The virus carrier is on a clean node, so it turns left, infects the
;; node, and moves left:
;;
;; . . . . . . . . .
;; . . . . . . . . .
;; . . . . . . . . .
;; . . . . . # . . .
;; . . .[#]# . . . .
;; . . . . . . . . .
;; . . . . . . . . .
;; . . . . . . . . .
;;
;; The virus carrier is on an infected node, so it turns right, cleans
;; the node, and moves up:
;;
;; . . . . . . . . .
;; . . . . . . . . .
;; . . . . . . . . .
;; . . .[.]. # . . .
;; . . . . # . . . .
;; . . . . . . . . .
;; . . . . . . . . .
;; . . . . . . . . .
;;
;; Four times in a row, the virus carrier finds a clean, infects it,
;; turns left, and moves forward, ending in the same place and still
;; facing up:
;;
;; . . . . . . . . .
;; . . . . . . . . .
;; . . . . . . . . .
;; . . #[#]. # . . .
;; . . # # # . . . .
;; . . . . . . . . .
;; . . . . . . . . .
;; . . . . . . . . .
;;
;; Now on the same node as before, it sees an infection, which causes it
;; to turn right, clean the node, and move forward:
;;
;; . . . . . . . . .
;; . . . . . . . . .
;; . . . . . . . . .
;; . . # .[.]# . . .
;; . . # # # . . . .
;; . . . . . . . . .
;; . . . . . . . . .
;; . . . . . . . . .
;;
;; After the above actions, a total of 7 bursts of activity had taken
;; place. Of them, 5 bursts of activity caused an infection.
;;
;; After a total of 70, the grid looks like this, with the virus carrier
;; facing up:
;;
;; . . . . . # # . .
;; . . . . # . . # .
;; . . . # . . . . #
;; . . # . #[.]. . #
;; . . # . # . . # .
;; . . . . . # # . .
;; . . . . . . . . .
;; . . . . . . . . .
;;
;; By this time, 41 bursts of activity caused an infection (though most
;; of those nodes have since been cleaned).
;;
;; After a total of 10000 bursts of activity, 5587 bursts will have
;; caused an infection.
;;
;; Given your actual map, after 10000 bursts of activity, how many
;; bursts cause a node to become infected? (Do not count nodes that
;; begin infected.)
;;
;; Your puzzle answer was 5280.
;; --- Part Two ---
;;
;; As you go to remove the virus from the infected nodes, it evolves to resist your attempt.
;;
;; Now, before it infects a clean node, it will weaken it to disable
;; your defenses. If it encounters an infected node, it will instead
;; flag the node to be cleaned in the future. So:
;;
;;     Clean nodes become weakened.
;;     Weakened nodes become infected.
;;     Infected nodes become flagged.
;;     Flagged nodes become clean.
;;
;; Every node is always in exactly one of the above states.
;;
;; The virus carrier still functions in a similar way, but now uses the
;; following logic during its bursts of action:
;;
;;     Decide which way to turn based on the current node:
;;         If it is clean, it turns left.
;;         If it is weakened, it does not turn, and will continue moving in the same direction.
;;         If it is infected, it turns right.
;;         If it is flagged, it reverses direction, and will go back the way it came.
;;     Modify the state of the current node, as described above.
;;     The virus carrier moves forward one node in the direction it is facing.
;;
;; Start with the same map (still using . for clean and # for infected)
;; and still with the virus carrier starting in the middle and facing
;; up.
;;
;; Using the same initial state as the previous example, and drawing
;; weakened as W and flagged as F, the middle of the infinite grid looks
;; like this, with the virus carrier's position again marked with [ ]:
;;
;; . . . . . . . . .
;; . . . . . . . . .
;; . . . . . . . . .
;; . . . . . # . . .
;; . . . #[.]. . . .
;; . . . . . . . . .
;; . . . . . . . . .
;; . . . . . . . . .
;;
;; This is the same as before, since no initial nodes are weakened or
;; flagged. The virus carrier is on a clean node, so it still turns
;; left, instead weakens the node, and moves left:
;;
;; . . . . . . . . .
;; . . . . . . . . .
;; . . . . . . . . .
;; . . . . . # . . .
;; . . .[#]W . . . .
;; . . . . . . . . .
;; . . . . . . . . .
;; . . . . . . . . .
;;
;; The virus carrier is on an infected node, so it still turns right,
;; instead flags the node, and moves up:
;;
;; . . . . . . . . .
;; . . . . . . . . .
;; . . . . . . . . .
;; . . .[.]. # . . .
;; . . . F W . . . .
;; . . . . . . . . .
;; . . . . . . . . .
;; . . . . . . . . .
;;
;; This process repeats three more times, ending on the
;; previously-flagged node and facing right:
;;
;; . . . . . . . . .
;; . . . . . . . . .
;; . . . . . . . . .
;; . . W W . # . . .
;; . . W[F]W . . . .
;; . . . . . . . . .
;; . . . . . . . . .
;; . . . . . . . . .
;;
;; Finding a flagged node, it reverses direction and cleans the node:
;;
;; . . . . . . . . .
;; . . . . . . . . .
;; . . . . . . . . .
;; . . W W . # . . .
;; . .[W]. W . . . .
;; . . . . . . . . .
;; . . . . . . . . .
;; . . . . . . . . .
;;
;; The weakened node becomes infected, and it continues in the same
;; direction:
;;
;; . . . . . . . . .
;; . . . . . . . . .
;; . . . . . . . . .
;; . . W W . # . . .
;; .[.]# . W . . . .
;; . . . . . . . . .
;; . . . . . . . . .
;; . . . . . . . . .
;;
;; Of the first 100 bursts, 26 will result in infection. Unfortunately,
;; another feature of this evolved virus is speed; of the first 10000000
;; bursts, 2511944 will result in infection.
;;
;; Given your actual map, after 10000000 bursts of activity, how many
;; bursts cause a node to become infected? (Do not count nodes that
;; begin infected.)
;;
;; Your puzzle answer was 2512261.

(def direction->left+right {:up [:left :right :down] :right [:up :down :left] :down [:right :left :up] :left [:down :up :right]})

(defn turn-right [direction]
  (second (get direction->left+right direction)))

(defn turn-left [direction]
  (first (get direction->left+right direction)))

(defn reverse-direction [direction]
  (first (rest (rest (get direction->left+right direction)))))

(defn next-direction [m pos current-direction]
  (condp = (get m pos \.)
    \. (turn-left current-direction)
    \W current-direction
    \# (turn-right current-direction)
    \F (reverse-direction current-direction)))

(def infection-count (atom 0))

(def node->next-node {\. \W
                      \W \#
                      \# \F
                      \F \.})

(defn toggle-node [m pos]
  (let [new-node (get node->next-node (get m pos \.))]
    (when (= new-node \#)
      (swap! infection-count inc))
    (assoc m pos new-node)))

(defn move [[x y] direction]
 (condp = direction
   :up [x (dec y)]
   :left [(dec x) y]
   :right [(inc x) y]
   :down [x (inc y)]))

(defn process-burst [{:keys [m pos direction]}]
  (let [new-direction (next-direction m pos direction)
        new-m (toggle-node m pos)
        next-pos (move pos new-direction)]
    {:m new-m :direction new-direction :pos next-pos}))

(defn parse-map [m-str]
 (loop [s (filter #(not= % \return) (seq m-str)) x 0 y 0 m {}]
   (if (seq s)
    (if (= \newline (first s))
      (recur (rest s) 0 (inc y) m)
      (recur (rest s) (inc x) y (assoc m [x y] (first s))))
    m)))

(defn start-pos [m]
  (let [x (->> (keys m)
            (map first)
            (apply max)
            (#(/ % 2))
            (int))
        y (->> (keys m)
            (map second)
            (apply max)
            (#(/ % 2))
            (int))]
    [x y]))

(defn str-grid [m]
  "Render the grid into a string which can be printed"
  (let [[min-y max-y] (->> (keys m)
                        (map second)
                        ((fn [s] [(apply min s) (apply max s)])))
        y-size (+ (Math/abs min-y) (Math/abs max-y))
        [min-x max-x] (->> (keys m)
                        (map first)
                        ((fn [s] [(apply min s) (apply max s)])))
        x-size (+ (Math/abs min-x) (Math/abs max-x))]
    (clojure.string/join "\n"
                         (map #(apply str %)
                              (for [y (range min-y (inc y-size))]
                                (for [x (range min-x (inc x-size))]
                                  (get m [x y] \.)))))))

(defn simulate [f iterations]
  (let [m (parse-map (slurp f))
        input {:m m
              :pos (start-pos m)
              :direction :up}]
    (reset! infection-count 0)
    (loop [i (dec iterations) input input]
      #_(println (str-grid (:m input)))
      #_(println)
      (if (<= 0 i)
        (recur (dec i) (process-burst input))
        [@infection-count (:direction input) (:pos input)]))))

#_(simulate "resources/year_2017/day22_sporifica_virus_input" 10000000)
;=> 2512261
