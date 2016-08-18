(defproject example-selenium-project "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :resource-paths ["resources"]
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clj-webdriver "0.7.2"]
                 [environ "1.0.3"]
                 [org.clojure/java.classpath "0.2.3"]
                 [org.clojure/tools.namespace "0.2.11"]
                 [org.clojure/tools.logging "0.2.6"]
                 [log4j "1.2.17" :exclusions [javax.mail/mail
                                              javax.jms/jms
                                              com.sun.jdmk/jmxtools
                                              com.sun.jmx/jmxri]]]
  :main ^:skip-aot example-selenium-project.core
  :plugins [[lein-environ "1.0.3"]]
  :uberjar-name "runtests.jar"
  :profiles {:dev         {:dependencies [[org.seleniumhq.selenium/selenium-java "2.47.1"]]
                           :env          {:clj-env "selenium"}}

             :windows     {:env {:clj-env-os "windows"}}

             :uberjar     {:aot          :all
                           :jvm-opts     ["-Dphantomjs.binary.path=phantomjs"
                                          "-Djava.util.logging.config.file=logging.properties"]
                           :dependencies [[com.github.detro.ghostdriver/phantomjsdriver "1.1.0"]]
                           :env          {:clj-env "phantom"}}

             :phantom     {:jvm-opts     ["-Dphantomjs.binary.path=phantomjs"
                                          "-Djava.util.logging.config.file=logging.properties"]
                           :dependencies [[com.github.detro.ghostdriver/phantomjsdriver "1.1.0"]]
                           :env          {:clj-env "phantom"}}

             :selenium    {:dependencies [[org.seleniumhq.selenium/selenium-java "2.47.1"]]
                           :env          {:clj-env "selenium"}}})
