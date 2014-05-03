(ns AirportRoutes.core)

(use 'clojure.java.io)
(use ['clojure.string :only '(split)])

(def r 6371)
(def max-double (Double/MAX_VALUE))

(defn to-int [x]
 (java.lang.Integer/parseInt x)) 

(defn to-double [x]
 (java.lang.Double/parseDouble x))

(defn remove-last [x]
  (subs x 0 (- (count x) 1)))

(defn sin [n] (Math/sin n))
(defn cos [n] (Math/cos n))
(defn atan2 [x y] (Math/atan2 x y))
(defn sin2 [n] (* (sin n) (sin n)))
(defn sqrt [n] (Math/sqrt n))
(defn to-radians [n] (Math/toRadians n))

;********************** reading from a file and create function  ********************

(def file-with-airports (reader (file "airports.dat"))); takes a pointer to a file

(defn read-file [file-with-airports] ;read the content of file
  (line-seq file-with-airports))

(def read-raw (read-file file-with-airports))

;Airport ID 	Unique OpenFlights identifier for this airport.
;Name 	Name of airport. May or may not contain the City name.
;City 	Main city served by airport. May be spelled differently from Name.
;Country 	Country or territory where airport is located.
;IATA/FAA 	3-letter FAA code, for airports located in Country "United States of America".
;3-letter IATA code, for all other airports.
;Blank if not assigned.
;ICAO 	4-letter ICAO code.
;Blank if not assigned.
;Latitude 	Decimal degrees, usually to six significant digits. Negative is South, positive is North.
;Longitude 	Decimal degrees, usually to six significant digits. Negative is West, positive is East.
;Altitude 	In feet.
;Timezone 	Hours offset from UTC. Fractional hours are expressed as decimals, eg. India is 5.5.
;DST

(defn read-data [contents-file] 
  (reduce (fn [acc row]
            (let [[airport-id name city country f i-la-lo-a-t d] (split row #",\"")
                 [faa iata lantitude logitude altitude timezone dst] (split (str "\"" f ",\"" i-la-lo-a-t ",\"" d) #",")]
                 (assoc acc (to-int airport-id)  
                  {:name (remove-last name) 
                   :city (remove-last city)
                   :country (remove-last country)
                   :faa faa  
                   :iata iata 
                   :lantitude  (to-double lantitude)
                   :logitude   (to-double logitude)
                   :altitude   (to-int altitude)
                   :timezone   (to-double timezone)
                   :dst dst  
                   })))
                 {} contents-file))

(def data (read-data read-raw))

;*******************************FUNCTION FOR CALCULATE DISTANCE ***********************
;dlon = lon2 - lon1
;dlat = lat2 - lat1
;a = (sin(dlat/2))^2 + cos(lat1) * cos(lat2) * (sin(dlon/2))^2
;c = 2 * atan2( sqrt(a), sqrt(1-a) )
;d = R * c (where R is the radius of the Earth)

(defn calculate-distance [airport-1 airport-2]
  (let[f1 (to-radians (:lantitude  airport-1))
       f2 (to-radians (:lantitude  airport-2))
       l1 (to-radians (:logitude   airport-1))
       l2 (to-radians (:logitude   airport-2))
       dlon-2 (/ (- l1 l2) 2)
       dlat-2 (/ (- f1 f2) 2)
       a (+ (sin2 dlat-2) (* (cos f1) (cos f2) (sin2 dlon-2)))
       c (* 2 (atan2 (sqrt a) (sqrt (- 1 a))))]
    (* r c)))


(defn all-connection [airport data] 
  (reduce (fn [acc [k e]]
                 (assoc acc k {:distance (calculate-distance airport e)}))
                 {} data))

(defn all-connection2 [airport] 
  (reduce (fn [acc [k e]]
                 (assoc acc k {:distance (calculate-distance airport e)}))
                 {} data))

;///////////////////////////////////////////////////////////////////////////////////////

(defn create-r-s-point [id-airport] ; index start city
  (reduce (fn [acc  [k e]]
            (if (= k id-airport)
              (assoc acc k  {:r 0 :s 1 :tacke []}),
              (assoc acc k {:r max-double :s 0 :tacke []})))
            {} data))

;////////////////////////////////////////////////////////////////////////////////////

(defn view-solution [r-s-p id-airport-start id-airport-target]
  (reset! array (conj @array id-airport-target))
  (if (not= nil (last (:point (r-s-p id-airport-target))))
    (view-solution r-s-p id-airport-start (last (:point (r-s-p id-airport-target))))),
  (reverse @array))

;******************************* dijkstra algorithm   *********************************

(defn dijks-alg  [id-airport-start id-airport-target] 
  (def r-s-point(atom (create-r-s-point id-airport-start)))
  (def tr (atom id-airport-start))
  
  (doseq [[ki ei] data :while (= (:s (@r-s-point id-airport-target)) 0)]
    (let [row (all-connection2 (data @tr))] ;uzimam jedan aerodrom,i njegove sve veze
      (doseq [[kj ej] row]  
        (if (and 
              (= (:s (@r-s-point kj)) 0)
              (< (+ 
                   (:r (@r-s-point @tr))
                   (:distance ej)) 
                 (:r (@r-s-point kj))))
          (reset! r-s-point (assoc @r-s-point kj 
                                   {:r (+ 
                                         (:r (@r-s-point @tr)) 
                                         (:distance ej)) 
                                    :s (:s (@r-s-point kj)) 
                                    :point (conj (:point (@r-s-point kj)) @tr)})))) 
      
      (def minimum max-double)
      (doseq [[k v] @r-s-point]
        (if (and (= (:s v) 0) (< (:r v) minimum))
          (do 
            (def minimum (:r v))
            (def mini k))))
      
      (reset! r-s-point (assoc @r-s-point mini {:r (:r (@r-s-point mini))
                                                :s 1 
                                                :point (:point (@r-s-point mini))}))
      (reset! tr mini)))
  
  (def array (atom []))
  (view-solution @r-s-point id-airport-start id-airport-target))










