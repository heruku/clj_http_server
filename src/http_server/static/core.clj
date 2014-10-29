(ns http_server.static.core
  (use [http_server.static.get]
       [http_server.clack.methods]))

(defn app [_next env]
  (cond 
    (= (:method env) GET) (do-get env nil)))
