package controllers

import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import play.api.libs.json.Json
import play.api.mvc.Results._
import play.api.test._
import play.api.test.Helpers._
import mockws.{Route, MockWS}

import play.api.mvc.{Action}
import support.api.ApiWSClient
import play.api.test.FakeRequest


@RunWith(classOf[JUnitRunner])
class ChartSpec extends PlaySpecification {
  "Chart Controller" should {

    "redirect back to the charting url after calling update technicals" in {
      val url = ChartSpec.baseUrl + "/datapoints/SGP.L"
      val myRoute = Route {
        case (POST, url) => Action { Ok("")}
      }

      val ws = MockWS(myRoute)
      val apiClient = new ApiWSClient(ws, ChartSpec.baseUrl)
      val chartController = new Chart(apiClient)
      val result = await(chartController.updateTechnicalsFromChart("SGP.L")(FakeRequest()))

      result.header.status must be equalTo 302
      result.header.headers("Location") must be equalTo "/charting/SGP.L"
    }

    "show company name on screen when displaying chart if company found" in {
      val url = ChartSpec.baseUrl + "/companies/SGP.L"
      val myRoute = Route {
        case (GET, url) => Action { Ok(Json.parse("{\"companyName\": \"Supergroup\", \"symbol\": \"SGP.L\"}")) }
      }

      val ws = MockWS(myRoute)
      val apiClient = new ApiWSClient(ws, ChartSpec.baseUrl)
      val chartController = new Chart(apiClient)
      val result = chartController.get("SGP.L")(FakeRequest())


      val content = contentAsString(result)
      content must contain("<h1>Supergroup</h1>")
    }

    "inform user that couldnt find company for symbol if API returns 404" in {
      val url = ChartSpec.baseUrl + "/companies/SGP.L"
      val myRoute = Route {
        case (GET, url) => Action { NotFound }
      }

      val ws = MockWS(myRoute)
      val apiClient = new ApiWSClient(ws, ChartSpec.baseUrl)
      val chartController = new Chart(apiClient)
      val result = chartController.get("SGP.L")(FakeRequest())


      val content = contentAsString(result)
      content must contain("<h1>No company found</h1>")
    }
  }
}

object ChartSpec {
  val baseUrl :String = "http://testservice"
}
