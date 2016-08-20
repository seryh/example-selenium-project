(ns example-selenium-project.tests.gosuslugi-main
  (:require [example-selenium-project.profile :as profile]
            [utils.webdriver :refer :all]
            [clj-webdriver.taxi :refer :all]
            [clojure.tools.logging :as log]
            [clojure.test :refer :all])
  (:import [org.openqa.selenium Keys]))

(defn get-search-input [] ($ ".index-slider-search input"))

;; проверка формы поиска https://www.gosuslugi.ru/
(deftest gosuslugi-main-search-form
  (profile/open-browser "https://www.gosuslugi.ru/")
  (try
    (->> (get-search-input)
         (type-text "загранпаспорт")
         (->>keys Keys/ARROW_DOWN)
         (->>keys Keys/ARROW_DOWN)
         (->>keys Keys/ENTER))

    (when-not ($ ".title_search")
      (throw (Exception. "redirect to search result, error")))

    (swap! profile/tests-success inc)
    (is true)
    (log/info "gosuslugi-main-search-form -> ok")

    (catch Exception e
      (log/info "gosuslugi-main-search-form -> fail" (.getMessage e))
      (swap! profile/tests-fail inc)
      (is false))))
