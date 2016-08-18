(ns example-selenium-project.core
  (:require [example-selenium-project.profile :as profile]
            [utils.webdriver :refer :all]
            [clj-webdriver.taxi :as taxi])
  (:gen-class))

(profile/loading-my-tests "example-selenium-project.tests")

(def test-case-gosuslugi
  ['gosuslugi-reg-form
   'gosuslugi-main-search-form])

(defn run-tests []
  (profile/tests-report-reset)
  (profile/do-list test-case-gosuslugi)
  (profile/tests-report)
  (taxi/quit))

(defn -main [& args]
  (run-tests))
