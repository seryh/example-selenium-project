(ns example-selenium-project.tests.gosuslugi-main
  (:require [example-selenium-project.profile :refer :all]
            [utils.webdriver :refer :all]
            [clj-webdriver.taxi :refer :all]
            [clojure.tools.logging :as log]
            [clojure.test :refer :all])
  (:import [org.openqa.selenium Keys]))

(defn get-search-input [] ($ ".index-slider-search input"))

;; проверка формы поиска https://www.gosuslugi.ru/
(deftest gosuslugi-main-search-form
  (open-browser "https://www.gosuslugi.ru/")
  (try
    (->> (get-search-input)
         (type-text "загранпаспорт")
         (->>keys Keys/ARROW_DOWN)
         (->>keys Keys/ARROW_DOWN)
         (->>keys Keys/ENTER))

    (when-not ($ ".title_search")
      (throw (Exception. "redirect to search result, error")))

    (swap! tests-success inc)
    (is true)
    (log/info "gosuslugi-main-search-form -> ok")

    (catch Exception e
      (log/info "gosuslugi-main-search-form -> fail" (.getMessage e))
      (swap! tests-fail inc)
      (is false))))

;; тест идентичный тесту gosuslugi-main-search-form,
;; только обернутый для лаконичности в свой макрос profile/defwebtest
(defwebtest gosuslugi-main-search-form-webtest
  "https://www.gosuslugi.ru/"
  (->> (get-search-input)
       (type-text "загранпаспорт")
       (->>keys Keys/ARROW_DOWN)
       (->>keys Keys/ARROW_DOWN)
       (->>keys Keys/ENTER))

  (when-not ($ ".title_search")
    (throw (Exception. "redirect to search result, error"))))