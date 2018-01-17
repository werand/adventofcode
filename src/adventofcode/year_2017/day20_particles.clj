(ns adventofcode.year-2017.day20-particles
  "Day 20 solution")

;; --- Day 20: Particle Swarm ---
;;
;; Suddenly, the GPU contacts you, asking for help. Someone has asked it to
;; simulate too many particles, and it won't be able to finish them all in
;; time to render the next frame at this rate.
;;
;; It transmits to you a buffer (your puzzle input) listing each particle
;; in order (starting with particle 0, then particle 1, particle 2, and so
;; on). For each particle, it provides the X, Y, and Z coordinates for the
;; particle's position (p), velocity (v), and acceleration (a), each in the
;; format <X,Y,Z>.
;;
;; Each tick, all particles are updated simultaneously. A particle's
;; properties are updated in the following order:
;;
;;     Increase the X velocity by the X acceleration.  Increase the Y
;;     velocity by the Y acceleration.  Increase the Z velocity by the Z
;;     acceleration.  Increase the X position by the X velocity.  Increase
;;     the Y position by the Y velocity.  Increase the Z position by the Z
;;     velocity.
;;
;; Because of seemingly tenuous rationale involving z-buffering, the GPU
;; would like to know which particle will stay closest to position <0,0,0>
;; in the long term. Measure this using the Manhattan distance, which in
;; this situation is simply the sum of the absolute values of a particle's
;; X, Y, and Z position.
;;
;; For example, suppose you are only given two particles, both of which
;; stay entirely on the X-axis (for simplicity). Drawing the current states
;; of particles 0 and 1 (in that order) with an adjacent a number line and
;; diagram of current X positions (marked in parenthesis), the following
;; would take place:
;;
;; p=< 3,0,0>, v=< 2,0,0>, a=<-1,0,0> -4 -3 -2 -1 0 1 2 3 4 p=< 4,0,0>, v=<
;; 0,0,0>, a=<-2,0,0> (0)(1)
;;
;; p=< 4,0,0>, v=< 1,0,0>, a=<-1,0,0> -4 -3 -2 -1 0 1 2 3 4 p=< 2,0,0>,
;; v=<-2,0,0>, a=<-2,0,0> (1) (0)
;;
;; p=< 4,0,0>, v=< 0,0,0>, a=<-1,0,0> -4 -3 -2 -1 0 1 2 3 4 p=<-2,0,0>,
;; v=<-4,0,0>, a=<-2,0,0> (1) (0)
;;
;; p=< 3,0,0>, v=<-1,0,0>, a=<-1,0,0> -4 -3 -2 -1 0 1 2 3 4 p=<-8,0,0>,
;; v=<-6,0,0>, a=<-2,0,0> (0)
;;
;; At this point, particle 1 will never be closer to <0,0,0> than particle
;; 0, and so, in the long run, particle 0 will stay closest.
;;
;; Which particle will stay closest to position <0,0,0> in the long term?
;;
;; Your puzzle answer was 344.

(defn parse-vector [s]
  (let [v (re-seq #"([ \-\d]+),([ \-\d]+),([ \-\d]+)" s)
        v (map #(Integer/parseInt (.trim %)) (rest (first v)))]
    v))

(defn parse-line [s]
  (map parse-vector (clojure.string/split s #">,")))

(defn parse-file [f]
  (let [lines (clojure.string/split (slurp f) #"\n")]
    (map (fn [a b] [a b]) (range) (map parse-line lines))))

(defn v-add [v a]
  (map + v a))

(defn update-particle [[p v a]]
  (let [v (v-add v a)
        p (v-add p v)]
    #_(println p)
    [p v a]))

(defn manhattan-distance [v]
  (reduce #(+ %1 (Math/abs %2)) 0 v))

(defn simulate-particles [p]
  (loop [p p
         last-order []
         i 0]
    (let [p (map (fn [[n p]] [n (update-particle p)]) p)
          new-order (sort-by (fn [[_ [p _ _]]] (manhattan-distance p)) < p)
          new-order (map (fn [[n _]] n) new-order)]
      (if (= i 2)
        last-order
        (recur p new-order (if (= last-order new-order) (inc i) i))))))

#_(simulate-particles (parse-file "resources/year_2017/day20_particle_input"))
;=>  0. 344
;  1. 336
;  2. 466
;  3. 115
;  4. 222
;  5. 14
;  6. 509
;  7. 386
;  8. 209
;...
