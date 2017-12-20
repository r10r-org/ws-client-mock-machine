package wsclientmockmachine.api

import org.mockito.{ArgumentMatchers, Mockito}
import play.api.libs.ws.{WSClient, WSRequest, WSResponse}

import scala.concurrent.Future

case class WsClientMockMachine(wsClientMock: WSClient = Mockito.mock(classOf[WSClient]),
                               wsRequestMock: WSRequest = Mockito.mock(classOf[WSRequest]),
                               wsResponseMock: WSResponse = Mockito.mock(classOf[WSResponse])) {

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // Not all methods are properly mocked. If you miss something simply add them :)
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  // init wsClient
  Mockito.when(wsClientMock.url(ArgumentMatchers.anyString())).thenReturn(wsRequestMock)

  // init wsRequest
  Mockito.when(wsRequestMock.addHttpHeaders(ArgumentMatchers.any())).thenReturn(wsRequestMock)
  Mockito.when(wsRequestMock.withAuth(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(wsRequestMock)
  Mockito.when(wsRequestMock.withBody(ArgumentMatchers.any())).thenReturn(wsRequestMock)
  Mockito.when(wsRequestMock.withHeaders(ArgumentMatchers.any())).thenReturn(wsRequestMock)
  Mockito.when(wsRequestMock.withHttpHeaders(ArgumentMatchers.any())).thenReturn(wsRequestMock)
  Mockito.when(wsRequestMock.withMethod(ArgumentMatchers.any())).thenReturn(wsRequestMock)
  Mockito.when(wsRequestMock.withQueryString(ArgumentMatchers.any())).thenReturn(wsRequestMock)
  Mockito.when(wsRequestMock.withQueryStringParameters(ArgumentMatchers.any())).thenReturn(wsRequestMock)
  Mockito.when(wsRequestMock.withRequestFilter(ArgumentMatchers.any())).thenReturn(wsRequestMock)
  
  // init wsResponse
  Mockito.when(wsRequestMock.get()).thenReturn(Future.successful(wsResponseMock))
  Mockito.when(wsRequestMock.patch(ArgumentMatchers.anyString())(ArgumentMatchers.any())).thenReturn(Future.successful(wsResponseMock))
  Mockito.when(wsRequestMock.post(ArgumentMatchers.anyString())(ArgumentMatchers.any())).thenReturn(Future.successful(wsResponseMock))

}