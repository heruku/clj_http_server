(ns http_server.cob.core_spec
  (use [speclj.core]
       [http_server.spec_helper]
       [http_server.cob.core]
       [http_server.clack.methods]))

(defn next-app [env])

(defn allow-header-of [response]
  (get (headers-of response) "Allow"))

(defn location-header-of [response]
  (get (headers-of response) "Location"))

(defn call-cob 
  ([method path] (cob nil {:path path :method method}))
  ([method path headers] (cob nil (merge {:path path :method method} headers))))

(describe "cob specific parts"
  (it "responds 200 ok to root"
    (should= 200 (status-of (call-cob GET "/"))))

  (context "/method_options"
    (it "responds 200 ok"
      (should= 200 (status-of (call-cob OPTIONS "/method_options"))))

    (it "allow header contains correct methods"
      (should= "GET,HEAD,POST,OPTIONS,PUT" (allow-header-of (call-cob OPTIONS "/method_options")))))

  (context "post /form"
    (it "responds 200 ok"
      (should= 200 (status-of (call-cob POST "/form"))))

    (it "replaces form contents with payload"
      (with-redefs [form-data (atom "")]
        (call-cob POST "/form" {:body "data=new-data"}) 
        (should= "data=new-data" @form-data)))

  (context "put /form"
    (it "responds 200 ok"
      (should= 200 (status-of (call-cob PUT "/form"))))

    (it "replaces form contents with payload"
      (with-redefs [form-data (atom "")]
        (call-cob PUT "/form" {:body "data=new-data"}) 
        (should= "data=new-data" @form-data))))

  (context "get /form"
    (it "responds 200 ok"
      (should= 200 (status-of (call-cob GET "/form"))))

    (it "sends the forms content as body"
      (with-redefs [form-data (atom "data=form-data")]
        (should= "data=form-data" (body-of (call-cob GET "/form")))))) 

  (context "delete /form"
    (it "responds 200 ok"
      (should= 200 (status-of (call-cob DELETE "/form"))))

    (it "sends the forms content as body"
      (with-redefs [form-data (atom "data=form-data")]
        (call-cob DELETE "/form")
        (should= "" @form-data)))) 

  (context "get /redirect"
    (it "responds with 301 Moved Permanently"
      (should= 301 (status-of (call-cob GET "/redirect"))))

    (it "sets location header to host's root"
      (should= "http://www.google.com/" (location-header-of (call-cob GET "/redirect" {:host "www.google.com"})))))) 


  (it "calls through with bad path"
    (should-invoke next-app {} (status-of (cob next-app {:path "/foobar" :method GET}))))) 
