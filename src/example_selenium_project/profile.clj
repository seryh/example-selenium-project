;; этот ns реализует логику по работе с профайлами
;; позволяющую использовать как chromedriver так и phantomjs драйвер
;; а также использовать проект кроссплатформенно для систем windows и *nix

(ns example-selenium-project.profile
  (:use [clj-webdriver.driver :only [init-driver]])
  (:require [clojure.test :refer :all]
            [utils.webdriver :refer :all]
            [clj-webdriver.taxi :refer :all]
            [environ.core :as environ]
            [clojure.tools.namespace.find :as ns]
            [clojure.java.classpath :as cp])
  (:import (java.util.concurrent TimeUnit)
           (org.openqa.selenium.remote DesiredCapabilities)))

;; используется макрос из за конфликта библиотек PhantomJS и selenium-java
(defmacro webdriver-import []
  (when-not (#{"selenium"} (environ/env :clj-env))
    (import '(org.openqa.selenium.phantomjs PhantomJSDriver)
            '(org.openqa.selenium.phantomjs PhantomJSDriverService)
            '(org.openqa.selenium.remote DesiredCapabilities))))

(webdriver-import)

(def profile-name (environ/env :clj-env))

(defonce driver (atom nil))
(defonce tests-fail (atom 0))
(defonce tests-success (atom 0))

(defn tests-report []
  (println {:tests-fail @tests-fail
            :tests-success @tests-success}))

(defn tests-report-reset []
  (reset! tests-fail 0)
  (reset! tests-success 0))

;; если +windows профайл, переназначаем путь к web-driver,
;; в корне проекта должны быть файлы chromedriver.exe и phantomjs.exe
(case (environ/env :clj-env-os)
  "windows" (if (#{"selenium"} profile-name)
              (System/setProperty "webdriver.chrome.driver" "chromedriver.exe")
              (System/setProperty "phantomjs.binary.path" "phantomjs.exe")) nil)

(defmacro webdriver-select
  "Определяет веб драйвер при компиляции по профайлу,
   инициализирует selenium или Phantom Driver"
  [url]
  (let [profile-name (environ/env :clj-env)]
    (if (#{"selenium"} profile-name)
      `(reset! driver (set-driver! {:browser :chrome} ~url))
      (do
        (webdriver-import)
        `(reset! driver
             (set-driver! (init-driver
                           {:webdriver
                            (PhantomJSDriver.
                             (doto (DesiredCapabilities.)
                               (.setCapability PhantomJSDriverService/PHANTOMJS_CLI_ARGS
                                               (into-array String ["--webdriver-loglevel=NONE"
                                                                   "--ignore-ssl-errors=yes"
                                                                   "--ssl-protocol=any"]))))}) ~url))))))

(defmacro loading-my-tests
  "Подключает все ns из каталога ns-dir с атрибутами :refer :all"
  [ns-dir]
  (let [all-ns (ns/find-namespaces (cp/classpath))
        test-ns-list (filter #(re-matches (re-pattern (str ns-dir "(.*)")) (str %)) all-ns)
        name-to-fn (fn [ns-name] `(require '[~ns-name :refer :all]))
        fn-list (mapv name-to-fn test-ns-list)]
    `(do ~@fn-list)))

(defmacro do-list
  "Выполнит все переданные в векторе list функции без аргументов"
  [list]
  `(do ~@(mapv #(do `(~%)) (eval list))))

(defn open-browser
  "Открывает браузер по переданному url"
  [^String url]
  (when @driver (quit))
  (println (str "Open driver with profile:" profile-name " and url: " url))
  (webdriver-select url)
  (window-resize {:width 1024 :height 768})
  (implicit-wait 6000))




