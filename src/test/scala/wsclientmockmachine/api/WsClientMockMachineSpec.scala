package wsclientmockmachine.api

import org.mockito.{ArgumentMatchers, Mockito}
import org.scalatest._
import play.api.libs.json.Json
import play.api.libs.ws.WSClient

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global

class WsClientMockMachineSpec extends FlatSpec with Matchers {

  ////////////////////////////////////////////////////////////////////
  // Let's assume we want to test a service that is able to fetch
  // and modify addresses. That service talks to a foreign server
  // via a nice api. It parses Json payloads and sends
  // post and get requests.
  //
  // Let's first define the service and the payload we want to test:
  ///////////////////////////////////////////////////////////////////

  case class Address(id: Int, street: String, town: String)
  object Address {
    implicit val jsonFormat = Json.format[Address]
  }

  // Usually we'd inject the wsClient here
  class MyAddressService(wsClient: WSClient) {

    def fetchAddress(id: Int): Future[Address] = {
      wsClient
        .url("http://my.cool.server.com/api/address")
        .withQueryStringParameters("id" -> id.toString)
        .withHttpHeaders("Content-Type" -> "application/json")
        .withHttpHeaders("Accept" -> "application/json")
        .get()
        .map(wsResponse => {
          val resultAsJsValue = wsResponse.json
          val address = resultAsJsValue.as[Address]

          address
        })
    }

    def modifyAddress(address: Address): Future[Unit] = {
      wsClient
        .url("http://my.cool.server.com/api/address")
        .withHttpHeaders("Content-Type" -> "application/json")
        .withHttpHeaders("Accept" -> "application/json")
        .post(Json.toJson(address))
        .map(wsResponse => {
          if (wsResponse.status == 200) {
            () // just return unit when everything is ok
          } else {
            // explode when something went wrong. A real service'd handle differently likely...
            throw new RuntimeException("Oops. Something went wrong! We got a 404. Please do something...")
          }
        })
    }
  }

  ///////////////////////////////////////////////////////////////////
  // Now - let's verify the behavior of that service:
  ///////////////////////////////////////////////////////////////////
  "WsClientMockMachine" should "properly execute get requests and allow to parse json data" in {
    // GIVEN
    val wsClientMockMachine = WsClientMockMachine()
    val myAddressService = new MyAddressService(wsClientMockMachine.wsClientMock)

    // This makes sure the response object is properly parsed from json into a scala object
    val serverResponse =
      """
        {
          "id": 12345,
          "street": "My Street",
          "town": "My Town"
        }
      """
    Mockito.when(wsClientMockMachine.wsResponseMock.json).thenReturn(Json.parse(serverResponse))

    // WHEN
    val result: Address = await(myAddressService.fetchAddress(12345))

    // THEN
    // verify url
    Mockito.verify(wsClientMockMachine.wsClientMock).url("http://my.cool.server.com/api/address")
    // verify http headers
    Mockito.verify(wsClientMockMachine.wsRequestMock).withQueryStringParameters("id" -> "12345")
    Mockito.verify(wsClientMockMachine.wsRequestMock).withHttpHeaders("Content-Type" -> "application/json")
    Mockito.verify(wsClientMockMachine.wsRequestMock).withHttpHeaders("Accept" -> "application/json")

    // verify http method
    Mockito.verify(wsClientMockMachine.wsRequestMock).get()


    // verify that mapping between json and scala class works
    result.street should be("My Street")
    result.town should be("My Town")
  }

  it should "properly execute post requests" in {
    // GIVEN
    val wsClientMockMachine = WsClientMockMachine()
    val myAddressService = new MyAddressService(wsClientMockMachine.wsClientMock)

    Mockito.when(wsClientMockMachine.wsResponseMock.status).thenReturn(200)

    val address = Address(12345, "My new street", "My new town")

    // WHEN
    val result: Unit = await(myAddressService.modifyAddress(address))

    // THEN
    // verify url
    Mockito.verify(wsClientMockMachine.wsClientMock).url("http://my.cool.server.com/api/address")
    // verify http headers
    Mockito.verify(wsClientMockMachine.wsRequestMock).withHttpHeaders("Content-Type" -> "application/json")
    Mockito.verify(wsClientMockMachine.wsRequestMock).withHttpHeaders("Accept" -> "application/json")

    // verify http method AND payload
    val expectedJsonPayloadAsJson = Json.toJson(address)
    Mockito
      .verify(wsClientMockMachine.wsRequestMock)
      .post(ArgumentMatchers.eq(expectedJsonPayloadAsJson))(ArgumentMatchers.any())
  }

  it should "throw an exception when address that should be found is not available" in {
    // GIVEN
    val wsClientMockMachine = WsClientMockMachine()
    val myAddressService = new MyAddressService(wsClientMockMachine.wsClientMock)

    // => we simulate a 404 response and expect our service to thro a RuntimeException
    Mockito.when(wsClientMockMachine.wsResponseMock.status).thenReturn(404)

    val address = Address(12345, "My new street", "My new town")

    // WHEN
    val thrown = intercept[RuntimeException] {
      await(myAddressService.modifyAddress(address))
    }

    // THEN
    thrown.getMessage() shouldBe "Oops. Something went wrong! We got a 404. Please do something..."

    // verify url
    Mockito.verify(wsClientMockMachine.wsClientMock).url("http://my.cool.server.com/api/address")
    // verify http headers
    Mockito.verify(wsClientMockMachine.wsRequestMock).withHttpHeaders("Content-Type" -> "application/json")
    Mockito.verify(wsClientMockMachine.wsRequestMock).withHttpHeaders("Accept" -> "application/json")

    // verify http method AND payload
    val expectedJsonPayloadAsJson = Json.toJson(address)
    Mockito
      .verify(wsClientMockMachine.wsRequestMock)
      .post(ArgumentMatchers.eq(expectedJsonPayloadAsJson))(ArgumentMatchers.any())
  }

  // just a tiny test utility
  private def await[T](future: Future[T]): T = {
    import scala.concurrent.duration._
    Await.result(future, 1.second)
  }
}