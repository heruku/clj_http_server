(ns http_server.cob.core_spec
  (use [speclj.core]
       [http_server.cob.core]
       [http_server.clack.methods]))

(defn status-of [response]
  (nth response 0))

(defn next-app [env])

(describe "cob specific parts"
  (it "responds 200 ok to root"
    (should= 200 (status-of (cob nil {:path "/" :method GET}))))

  (it "calls through with random path"
    (should-invoke next-app {} (status-of (cob next-app {:path "/foobar" :method GET}))))) 
