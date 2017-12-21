/**
  * Copyright (C) 2012-2017 the original author or authors.
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  * http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package wsclientmockmachine.api

import org.mockito.{ArgumentMatchers, Mockito}
import play.api.libs.json.JsValue
import play.api.libs.ws.{WSClient, WSRequest, WSResponse}

import scala.concurrent.Future

case class WsClientMockMachine(wsClientMock: WSClient = Mockito.mock(classOf[WSClient]),
                               wsRequestMock: WSRequest = Mockito.mock(classOf[WSRequest]),
                               wsResponseMock: WSResponse = Mockito.mock(classOf[WSResponse])) {

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // Not all methods are mocked yet. If you miss something simply add the interaction and create a Pull Request!
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  // init WsClient
  Mockito.when(wsClientMock.url(ArgumentMatchers.anyString())).thenReturn(wsRequestMock)

  // init WsRequest
  Mockito.when(wsRequestMock.addHttpHeaders(ArgumentMatchers.any())).thenReturn(wsRequestMock)
  Mockito.when(wsRequestMock.withAuth(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(wsRequestMock)
  Mockito.when(wsRequestMock.withBody(ArgumentMatchers.any())).thenReturn(wsRequestMock)
  Mockito.when(wsRequestMock.withHeaders(ArgumentMatchers.any())).thenReturn(wsRequestMock)
  Mockito.when(wsRequestMock.withHttpHeaders(ArgumentMatchers.any())).thenReturn(wsRequestMock)
  Mockito.when(wsRequestMock.withMethod(ArgumentMatchers.any())).thenReturn(wsRequestMock)
  Mockito.when(wsRequestMock.withQueryString(ArgumentMatchers.any())).thenReturn(wsRequestMock)
  Mockito.when(wsRequestMock.withQueryStringParameters(ArgumentMatchers.any())).thenReturn(wsRequestMock)
  Mockito.when(wsRequestMock.withRequestFilter(ArgumentMatchers.any())).thenReturn(wsRequestMock)

  // init WsResponse
  Mockito.when(wsRequestMock.get()).thenReturn(Future.successful(wsResponseMock))
  Mockito.when(wsRequestMock.patch(ArgumentMatchers.any[JsValue]())(ArgumentMatchers.any())).thenReturn(Future.successful(wsResponseMock))
  Mockito.when(wsRequestMock.patch(ArgumentMatchers.anyString())(ArgumentMatchers.any())).thenReturn(Future.successful(wsResponseMock))
  Mockito.when(wsRequestMock.post(ArgumentMatchers.any[JsValue]())(ArgumentMatchers.any())).thenReturn(Future.successful(wsResponseMock))
  Mockito.when(wsRequestMock.post(ArgumentMatchers.anyString())(ArgumentMatchers.any())).thenReturn(Future.successful(wsResponseMock))

}