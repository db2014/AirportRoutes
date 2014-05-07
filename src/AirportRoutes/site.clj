(ns AirportRoutes.site
(:use (compojure handler 
                 [core :only (GET POST defroutes)]))
(:require compojure.route 
          [AirportRoutes.core :as dajks]
          [net.cgrand.enlive-html :as en]
          [ring.util.response :as response] 
          [ring.adapter.jetty :as jetty]
          [clj-json.core :as json]))

(def airpor-data dajks/data)

(defn data-for-combo [airpor-data]
  (reduce (fn [acc [id m]]
            (assoc acc id (str (:name m)" " (:city m)" " (:country m))))
          {0 "Select airport"} airpor-data))

(def combo-data (data-for-combo airpor-data))

(def server (jetty/run-jetty #'app {:port 8088 :join false}))

(en/deftemplate homepage
  (en/xml-resource "homepage.html")
  [request]
   [:#od :option] (en/clone-for[[v t] combo-data]
                          [:option]
                          (en/content t)
                          [:option]
                          (en/set-attr :value (str v)))
    [:#do :option] (en/clone-for[[v t] combo-data]
                          [:option]
                          (en/content t)
                          [:option]
                          (en/set-attr :value (str v))))


(defroutes app*
  (compojure.route/resources "/")
  (GET "/" request (homepage request)))


(def app (compojure.handler/site app*))