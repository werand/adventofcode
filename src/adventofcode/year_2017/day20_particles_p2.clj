;; Day 20
;; --- Part Two ---
;;
;; To simplify the problem further, the GPU would like to remove any
;; particles that collide. Particles collide if their positions ever
;; exactly match. Because particles are updated simultaneously, more than
;; two particles can collide at the same time and place. Once particles
;; collide, they are removed and cannot collide with anything else after
;; that tick.
;;
;; For example:
;;
;; p=<-6,0,0>, v=< 3,0,0>, a=< 0,0,0> p=<-4,0,0>, v=< 2,0,0>, a=< 0,0,0> -6
;; -5 -4 -3 -2 -1 0 1 2 3 p=<-2,0,0>, v=< 1,0,0>, a=< 0,0,0> (0) (1) (2)
;; (3) p=< 3,0,0>, v=<-1,0,0>, a=< 0,0,0>
;;
;; p=<-3,0,0>, v=< 3,0,0>, a=< 0,0,0> p=<-2,0,0>, v=< 2,0,0>, a=< 0,0,0> -6
;; -5 -4 -3 -2 -1 0 1 2 3 p=<-1,0,0>, v=< 1,0,0>, a=< 0,0,0> (0)(1)(2) (3)
;; p=< 2,0,0>, v=<-1,0,0>, a=< 0,0,0>
;;
;; p=< 0,0,0>, v=< 3,0,0>, a=< 0,0,0> p=< 0,0,0>, v=< 2,0,0>, a=< 0,0,0> -6
;; -5 -4 -3 -2 -1 0 1 2 3 p=< 0,0,0>, v=< 1,0,0>, a=< 0,0,0> X (3) p=<
;; 1,0,0>, v=<-1,0,0>, a=< 0,0,0>
;;
;; ------destroyed by collision------ destroyed by collision------ -6 -5 -4
;; -------3 -2 -1 0 1 2 3 destroyed by collision------ (3) p=< 0,0,0>,
;; ------v=<-1,0,0>, a=< 0,0,0>
;;
;; In this example, particles 0, 1, and 2 are simultaneously destroyed at
;; the time and place marked X. On the next tick, particle 3 passes through
;; unharmed.
;;
;; How many particles are left after all collisions are resolved?
;;
;; Your puzzle answer was 404.

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
  (when-not (nil? p)
    (let [v (v-add v a)
          p (v-add p v)]
      [p v a])))

(defn manhattan-distance [v]
  (when (seq v)
    (reduce #(+ %1 (Math/abs %2)) 0 v)))

(defn duplicates [p]
  (for [[id freq] (frequencies (map (fn [[_ [p _ _]]] p) p))
        :when (> freq 1)]
    id))

(defn simulate-particles [p]
  (loop [p p
         last-order []
         i 0]
    (let [p (map (fn [[n p]] [n (update-particle p)]) p)
          d (distinct p)
          dups (duplicates p)
          p (remove (fn [[_ [p _ _]]] (some #{p} dups)) p)
          new-order (sort-by (fn [[_ [p _ _]]] (manhattan-distance p)) < p)
          new-order (map (fn [[n _]] n) new-order)]
      (if (= i 2)
        last-order
        (recur p new-order (if (= last-order new-order) (inc i) i))))))

#_(simulate-particles (parse-file "resources/day20_particle_input"))
#_(count (simulate-particles (parse-file "resources/day20_particle_input")))
