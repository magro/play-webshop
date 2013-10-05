import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

import org.fluentlenium.core.filter.FilterConstructor._

// import models.activate.computerPersistenceContext._

class IntegrationSpec extends Specification with ActivateSpecification {

  // reinitializeContext

  "Application" should {

    "work from within a browser" inBrowser { browser =>

      browser.goTo("http://localhost:3333/")

      browser.$("header h1").first.getText must equalTo("Play sample application — Computer database")
      browser.$("section h1").first.getText must equalTo("565 computers found")

      browser.$("#pagination li.current").first.getText must equalTo("Displaying 1 to 10 of 565")

      browser.$("#pagination li.next a").click()

      browser.$("#pagination li.current").first.getText must equalTo("Displaying 11 to 20 of 565")
      browser.$("#searchbox").text("Apple")
      browser.$("#searchsubmit").click()

      browser.$("section h1").first.getText must equalTo("13 computers found")
      browser.$("a", withText("Apple II")).click()

      browser.$("section h1").first.getText must equalTo("Edit computer")

      browser.$("#discontinued").text("xxx")
      browser.$("input.primary").click()

      browser.$("div.error").size must equalTo(1)
      browser.$("div.error label").first.getText must equalTo("Discontinued date")

      browser.$("#discontinued").text("")
      browser.$("input.primary").click()

      browser.$("section h1").first.getText must equalTo("565 computers found")
      browser.$(".alert-message").first.getText must equalTo("Done! Computer Apple II has been updated")

      browser.$("#searchbox").text("Apple")
      browser.$("#searchsubmit").click()

      browser.$("a", withText("Apple II")).click()
      browser.$("input.danger").click()

      browser.$("section h1").first.getText must equalTo("564 computers found")
      browser.$(".alert-message").first.getText must equalTo("Done! Computer has been deleted")

      browser.$("#searchbox").text("Apple")
      browser.$("#searchsubmit").click()

      browser.$("section h1").first.getText must equalTo("12 computers found")

    }

  }

}