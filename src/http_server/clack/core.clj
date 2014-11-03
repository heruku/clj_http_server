(ns http_server.clack.core
  (use [http_server.clack.request :as request]
       [http_server.clack.response :as response]))

(defn four-oh-four-app [env]
  [404 {} ""])

(def middleware (atom []))

(defn use-middleware [& apps]
  (reset! middleware apps))

(defn linked-middleware [middleware]
  (reduce #(partial %2 %1) four-oh-four-app (reverse middleware)))

(defn enter-middleware [env]
  ((linked-middleware @middleware) env))

(defn clack [reader]
  (->> reader
       request/parse
       enter-middleware
       response/raw-response))
