(ns api-linguagens-formais.service
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [io.pedestal.http.body-params :as body-params]
            [clojure.java.jdbc :as jdbc]
            [ring.util.response :as ring-resp]))

(def db-spec {:subprotocol "mysql"
              :subname "//localhost:3306/linguagens_funcionais"
              :user "root"
              :password "example"})


(defn home-page
  [request]
  (ring-resp/response "Hello World!"))

(defn usuarios
  [request]
  (ring-resp/response {:status "Ok" :usuarios (jdbc/query db-spec ["SELECT * FROM usuarios"])}))

(defn criar-usuario
  [request]
  (jdbc/insert! db-spec "usuarios" {:nome (-> request :json-params :nome)})
  (ring-resp/response {:status "Ok" :usuario_id (first (jdbc/query db-spec ["SELECT max(id) as id FROM usuarios"] {:row-fn :id}))})
 )

(defn usuario
  [request]
  ;(let [id (-> request :path-params :id)])
  (ring-resp/response {:status "Ok" :usuario (jdbc/query db-spec ["SELECT * FROM usuarios WHERE id = ?" (-> request :path-params :id)] {:result-set-fn first})})
 )

(defn atualizar-usuario
  [request]
  ;(let [id (-> request :path-params :id)])
  (jdbc/update! db-spec "usuarios" {:nome (-> request :json-params :nome)} ["id = ?" (-> request :json-params :id)])
  (ring-resp/response {:status "Ok" :usuario_id (-> request :json-params :id)})
)

;; Defines "/" and "/about" routes with their associated :get handlers.
;; The interceptors defined after the verb map (e.g., {:get home-page}
;; apply to / and its children (/about).
(def common-interceptors [(body-params/body-params) http/html-body])

;; Tabular routes
(def routes #{["/" :get (conj common-interceptors `home-page)]
              ["/usuarios" :get (conj common-interceptors `usuarios)]
              ["/criar-usuario" :post (conj common-interceptors `criar-usuario)]
              ["/usuarios/:id" :get (conj common-interceptors `usuario)]
              ["/atualizar-usuario" :post (conj common-interceptors `atualizar-usuario)]})

;; Map-based routes
;(def routes `{"/" {:interceptors [(body-params/body-params) http/html-body]
;                   :get home-page
;                   "/about" {:get about-page}}})

;; Terse/Vector-based routes
;(def routes
;  `[[["/" {:get home-page}
;      ^:interceptors [(body-params/body-params) http/html-body]
;      ["/about" {:get about-page}]]]])


;; Consumed by api-linguagens-formais.server/create-server
;; See http/default-interceptors for additional options you can configure
(def service (-> {:env :prod
              ;; You can bring your own non-default interceptors. Make
              ;; sure you include routing and set it up right for
              ;; dev-mode. If you do, many other keys for configuring
              ;; default interceptors will be ignored.
              ;; ::http/interceptors []
              ::http/routes routes


              ;; Uncomment next line to enable CORS support, add
              ;; string(s) specifying scheme, host and port for
              ;; allowed source(s):
              ;;
              ;; "http://localhost:8080"
              ;;
              ;;::http/allowed-origins ["scheme://host:port"]

              ;; Tune the Secure Headers
              ;; and specifically the Content Security Policy appropriate to your service/application
              ;; For more information, see: https://content-security-policy.com/
              ;;   See also: https://github.com/pedestal/pedestal/issues/499
              ;;::http/secure-headers {:content-security-policy-settings {:object-src "'none'"
              ;;                                                          :script-src "'unsafe-inline' 'unsafe-eval' 'strict-dynamic' https: http:"
              ;;                                                          :frame-ancestors "'none'"}}

              ;; Root for resource interceptor that is available by default.
              ::http/resource-path "/public"

              ;; Either :jetty, :immutant or :tomcat (see comments in project.clj)
              ;;  This can also be your own chain provider/server-fn -- http://pedestal.io/reference/architecture-overview#_chain_provider
              ::http/type :jetty
              ;;::http/host "localhost"
              ::http/port 8000
              ::http/host "0.0.0.0"

              ;; Options to pass to the container (Jetty)
              ::http/container-options {:h2c? true
                                        :h2? false
                                        ;:keystore "test/hp/keystore.jks"
                                        ;:key-password "password"
                                        ;:ssl-port 8443
                                        :ssl? false
                                        ;; Alternatively, You can specify your own Jetty HTTPConfiguration
                                        ;; via the `:io.pedestal.http.jetty/http-configuration` container option.
                                        ;:io.pedestal.http.jetty/http-configuration (org.eclipse.jetty.server.HttpConfiguration.)
                                        }}
  http/default-interceptors
  (update ::http/interceptors into [http/json-body]))
  )
