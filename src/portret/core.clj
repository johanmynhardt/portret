(ns portret.core
  (:use ring.adapter.jetty
        compojure.core)
  (:gen-class))

(defn app-handler [request]
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body (str request)})

(defroutes app-routes
  (GET "/" [] "PONG!")
  (GET "/:name" [name] (str "hello, " name))
  (GET "/resize/:dims/s/:source" [dims source] (str "dims: " dims ", source: "
                                                    source)))

(def app app-routes)

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Starting jetty...")
  (run-jetty app {:port 3000}))
