(ns http_server.clack.core
  (use [http_server.clack.request :as request]
       [http_server.clack.response :as response]))

(def middleware (atom []))

(defn use-middleware [app]
   (swap! middleware conj app))

(defn enter-middleware [env]
   ((first @middleware) nil env))

(defn clack [raw-request]
  (->> raw-request
       request/parse
       enter-middleware
       response/raw-response))
