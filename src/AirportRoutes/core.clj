(ns AirportRoutes.core)

(use 'clojure.java.io)
(use ['clojure.string :only '(split)])

(def n 6); nuber of cities
(def start 0) ; city ​​from which we start
(def end 5); city ​​to which we go
(def max-value 999999 );max value for the distance

(defn to-int [x]
 (java.lang.Integer/parseInt x)) 

;********************** reading from a file and create array of cities
(def file-with-cities (reader (file "cities.txt"))); takes a pointer to a file
(def i (atom 0))
(def cities-number (atom []))
(def distance-between-cities (atom []))
(def cities-​​names (atom []))



(defn read-file [file-with-cities] 
  (line-seq file-with-cities))

(def list-cities (read-file file-with-cities))

(defn get-cities-number [contents-file]
  (reset! cities-number (to-int (first contents-file)))); the first value in file is the number of cities

;and i make array of cities and distance

(defn fill-array-cities [contents-file]  
  (doseq [row (rest contents-file)]
    (let [array (split row #";")
          name  (first array)
          distance (into [](map to-int  (rest array)))]
    (reset! distance-between-cities (assoc @distance-between-cities @i  distance)); vector with distances between cities
    (reset! cities-​​names (assoc @cities-​​names @i  name)); vector with the names of cities
    (reset! i (inc @i))))
  (reset! i 0))

(defn set-value-vector []  
  (get-cities-number list-cities)
  (fill-array-cities list-cities))

;****************************************f-ja koja postavlja vektorsku reprezenataciju *******
(set-value-vector ) ;[ime grada daljina1 daljina2 daljina3] za i-ti grad su daljine od i-tog grada do krajnjeg grada
;**********************************************************************************************
;*************  CREATE MATRIX  !!!!
(defn create-matrix []
  (def matrix-cities (atom (into [] (take @cities-number (repeat [])))))
  (def j (atom 1))
  (def i (atom 0))
  
  (doseq [row @distance-between-cities]
    (reset! matrix-cities (assoc @matrix-cities @i (conj (@matrix-cities @i) 0)))
    (reset! j (inc @i))
    (doseq [distance row]
      (reset! matrix-cities (assoc @matrix-cities @i (conj (@matrix-cities @i) distance)))
      (reset! matrix-cities (assoc @matrix-cities @j (conj (@matrix-cities @j) distance)))
      (reset! j (inc @j)))
    (reset! i (inc @i))))

;***********************************************f-ja koja pravi matricu*************
(create-matrix )
;**********************************************************************************************

(defn create-vector [list-cities] ; create map {:index_city [name-of-city [connection with cities]]}
  (reduce (fn [acc row]
            (let [array (split row #";")
                  name (first array)
                  key (keyword (str "" (count acc)))
                  distance (into [] (map to-int (rest array)))]
              (assoc acc key (conj [name] distance))))
            {} (rest list-cities)))

 
(defn create-vector [list-cities] ;create map {:index_city [:name name-of-city :distance [connection with cities]]}
  (reduce (fn [acc row]
            (let [array (split row #";")
                  city-name (first array)
                  distance-between (into [] (conj (map to-int (rest array)) 0))]
              (assoc acc (count acc)  {:index (count acc) :name city-name :distance distance-between })))
            {} (rest list-cities)))

(def map-data (create-vector list-cities))

(def array (atom []))



(def one-row (atom [0 0 0 0 0 0]))

(defn left-triangle [index map-data]
  (for [i (range 6)] 
    (if (> i index)
      (reset! one-row (assoc @one-row i ((:distance (map-data index)) (- i index)))),
      (reset! one-row (assoc @one-row i ((:distance (map-data i)) (- index i)))))))

(defn row-matrix [index map-data]
(let [array []]
  (for [i (range 6)] 
    (if (> i index)
      (conj array ((:distance (map-data index)) (- i index))),
      (conj array ((:distance (map-data i)) (- index i)))))))

;*********************** dijkstra algorithm   *********************************************

(def mini 0)
(def r (atom (into [] (take @cities-number (repeat max-value)))))
(def s (atom (into [] (take @cities-number (repeat 0)))))


(reset! r (assoc @r strart 0))
(reset! s (assoc @s strart 1))
(def tr (atom start));city in which we are currently in
(def point (atom (into [] (take n (repeat [])))))

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
          (if (and (= (@s j) 0) (< (@r j) minimum))
            (do
              (def minimum (@r j))
              (def mini j)))
         (recur (inc j))))
     (reset! s (assoc @s mini 1))
     (reset! tr mini)
     (recur (inc i))))
    
(def x [11 2 33 1])

(.indexOf x 1)
(rest (sort [11 2 33 1]))





(defn najblizi-grad [])





