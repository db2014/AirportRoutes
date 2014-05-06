(defproject AirportRoutes "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [ring "1.0.1"]
                 [compojure "1.0.1"]
                 [enlive "1.0.0"]
                 [noir "1.3.0-alpha10"]
                 [clj-json "0.5.3"]]
  :cljsbuild {:builds
              [{:builds nil,
                :jar true
                :source-path "src/AirportRoutes/cljs"
                :compiler {:output-dir "resources/public/js/"
                           :output-to "resources/public/js/main.js"
                           :optimization :simple
                           :pretty-print true}}]})
