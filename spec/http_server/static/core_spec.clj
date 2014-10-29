(ns http_server.static.core_spec
  (use [speclj.core]
       [http_server.static.core]
       [http_server.static.get]
       [http_server.clack.methods]))

(def get-env
  {:method GET})

(describe "static"
  (context "dispatch"
    (it "does get when request is get"
      (should-invoke do-get {:with [get-env files]} (app nil get-env)))))
