(ns AirportRoutes.site)

  (:use (compojure handler 
                   [core :only (GET POST defroutes)]))
  
  (:require compojure.route 
            [net.cgrand.enlive-html :as en]
            [ring.util.response :as response] 
            [ring.adapter.jetty :as jetty]
            [clj-json.core :as json] )
  
  (en/deftemplate homepage
    (en/xml-resource "homepage.html")
    [request])
  
  (def server (jetty/run-jetty #'app {:port 8080 :join false}))
  
 
  
  
  (def app (compojure.handler/site app*))