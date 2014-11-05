(ns http_server.clack.basic_auth_spec
  (use [speclj.core]
       [http_server.spec_helper]
       [http_server.clack.methods]
       [http_server.clack.basic_auth]))

(defn next-app [env])

(describe "basic auth"
  (it "sends 401 Not Authorized for a simple get"
    (should= 401 (status-of (basic-auth "/logs" "irrelevant" nil {:method GET :path "/logs"}))))

  (it "requires authentication in the body"
    (should= "Authentication required" (body-of (basic-auth "/logs" "irrelevant" nil {:method GET :path "/logs"}))))

  (it "calls next app if path, username and password all match"
    (should-invoke next-app {} (basic-auth "/logs" "password" next-app {:method GET :path "/logs" :headers {"Authorization" "Basic password"}})))

  (it "calls next app if path is not protected"
    (should-invoke next-app {} (basic-auth "/" "irrelevant" next-app {:method GET :path "/logs" :headers {"Authorization" "Basic password"}})))

  (context "initializing"
    (it "returns a partial function that complies the clack interface"
      (let [my-basic-auth (init-basic-auth "/logs" "username" "password")]
        (should= 401 (status-of (my-basic-auth nil {:method GET :path "/logs"})))))

    (it "encodes username and password in base64"
      (let [my-basic-auth (init-basic-auth "/logs" "admin" "hunter2")]
        (should-invoke next-app {} (my-basic-auth next-app {:method GET :path "/logs" :headers {"Authorization" "Basic YWRtaW46aHVudGVyMg=="}})))))) 
