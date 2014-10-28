(ns http_server.get)

(defn- filename [request]
  (subs (:path request) 1))

(defn- success [content]
  {:status 200 
   :content content 
   :headers {"Content-Length" (count content)}})

(defn do-get [request files]
  (if (contains? files (filename request))
    (success (get files (filename request)))
    {:status 404}))
