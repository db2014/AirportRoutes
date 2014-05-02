(ns AirportRoutes.core)

(def n 6); nuber of cities
(def start 0) ; city ​​from which we start
(def end 5); city ​​to which we go
(def max-value 999999 );max value for the distance
(def cities
  {
   0 {1 4, ; [city distance]
      2 2}
   1 {3 5,
       2 2}
   2 {3 8,
      4 10}   
   3 {4 2, 
      5 6}
   4 {5 3}
   5 {}
   })


;*************************************** home initialization and  matrix NxN **************************
(def r (atom (into [] (take n (repeat max-value)))))
(def s (atom (into [] (take n (repeat 0)))))
(def matrix (atom (into [] (take n (repeat @r)))))

;set 0 on the main diagonal
(loop [i 0]
  (when (< i n)
    (reset! matrix (assoc @matrix  i (assoc (@matrix i) i 0)))
	    (recur (inc i))))
;**************************************** initialization of start-city***********************************************
(reset! r (assoc @r start 0));
(reset! s (assoc @s start 1));visited city set value of s (0-not visited)

;**************************************fill matrix with distance between cities ******************************************************    
;(doseq [y cities] 
 ; (def i (y 0))
  ;(doseq [y2 (into [] (cities i))]
   ; (def j (y2 0))
    ;(def v (y2 1))
    ;(reset! matrix (assoc @matrix  i (assoc (@matrix i) j v)))
    ;(reset! matrix (assoc @matrix  j (assoc (@matrix j) i v)))
    ;(println "["i"]["j"]""="v)))

(doseq [y cities] 
  (doseq [y2 (into [] (cities (y 0)))]
    (reset! matrix (assoc @matrix  (y 0) (assoc (@matrix (y 0)) (y2 0) (y2 1))))
    (reset! matrix (assoc @matrix  (y2 0) (assoc (@matrix (y2 0)) (y 0) (y2 1))))))


;***************************************** algorithm ***************************************
(def tr (atom start));city in which we are currently in
(def point (atom (into [] (take n (repeat [])))))

(def mini 0)
(loop [i 0]
  (when (and (< i (- n 1))(= (@s end) 0))
     (loop [j 0]
       (when (< j n )
         (if (and (= (@s j) 0) (< (+ (@r @tr)((@matrix @tr) j)) (@r j)))
           (do
             (reset! point (assoc @point j (conj (@point j) @tr)))
             (reset! r (assoc @r j (+ (@r @tr) ((@matrix @tr) j))))))          
         (recur (inc j))))
      (def minimum max-value)
      (loop [j 0]
       (when (< j n )
          (if (and (= (@s j) 0) (<  (@r j) minimum))
            (do 
              (def minimum (@r j))
              (def mini j)))
         (recur (inc j))))
     (reset! s (assoc @s mini 1))
     (reset! tr mini)
     (recur (inc i))))

;array of all cities should visit 
(def array [])
(defn show [city-end]
  (if (= ((@point city-end) 0) start)
     (do 
       (def array (conj array (@point city-end)))
       (reverse array)),
    (do
      (def array (conj array (@point city-end)))
      (recur ((@point city-end) 0)))))

(def z1 (atom []))
(doseq [z  (show 5)]
  (reset! z1 (concat @z1 z)))


