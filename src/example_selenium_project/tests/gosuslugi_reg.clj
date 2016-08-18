(ns example-selenium-project.tests.gosuslugi-reg
  (:require [example-selenium-project.profile :as profile]
            [utils.webdriver :refer :all]
            [clj-webdriver.taxi :refer :all]
            [clojure.tools.logging :as log]
            [clojure.test :refer :all]))

;; проверка формы регистрации
(deftest gosuslugi-reg-form
  (profile/open-browser "https://esia.gosuslugi.ru/registration/")
  (try
    (type-text "hickey" ($ "#lastName"))
    (type-text "rich" ($ "#name"))
    (type-text "+13448754747" ($ "#phone"))
    (type-text "richhickey84@gmail.com" ($ "#email"))
    (click ($ "#tryReg"))
    (wait-for-jq-ajax)

    ;; если существует ошибка о числе попыток регистрации
    ;; то считаем тест пройденым
    (when-not ($ "#msg_cttInputs")
      (type-text "111" ($ "#code"))
      (wait-for-jq-ajax)

      ;; если не получили ошибку о неверно верефицированном
      ;; коде из смс, то генерируем ошибку которая провалит тест
      (when-not ($ "#msg_code")
        (throw (Exception. "error msg code, is not found"))))

    (swap! profile/tests-success inc)
    (is true)
    (log/info "gosuslugi-reg-form -> ok")

    (catch Exception e
      (log/info "gosuslugi-reg-form -> fail" (.getMessage e))
      (swap! profile/tests-fail inc)
      (is false))))
