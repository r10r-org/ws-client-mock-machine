WsClientMockMachine - simple mocking of Play WsClient
=====================================================

[![Build Status](https://travis-ci.org/r10r-org/wsclientmockmachine.svg?branch=master)](https://travis-ci.org/r10r-org/wsclientmockmachine)

The problem
-----------
If you are using Play then you'll also likely use Play's nice http library [play-ws](https://github.com/playframework/play-ws).
And because you are a professional software developer test your code - don't you?

But writing mocked tests for play-ws is cumbersome. Mainly because
you don't have to mock only one class and one call but an interaction chain -
from WsClient.url() to WsRequest to a WsResponse. In between are a lot if
methods like addHeader, addQueryParameter, withAuth, post and many many more.

Doing that setup for every single of your testcases is annoying and repetitive.

The solution
------------
WsClientMockMachine a read-to-use mock for wsClient. It has all basic
interactions already set up so you can use it straight away. Just drop
WsClientMockMachine instead of wsClient and verify the interactions.

Example
-------

    import wsclientmockmachine.api.WsClientMockMachine

    val wsClientMockMachine = WsClientMockMachine()

    // Your service accepts a WsClient - you simply drop in the wsClientMock configured by wsClientMockMachine
    val myService = new MyService(wsClientMockMachine.wsClientMock)

    val responseFuture: Future[AccessToken] = myService.doSomething(payload)
    val response = Await.result(responseFuture, 10 seconds)

    // And now you can test all interactions.
    // Verify the correct url has been called
    Mockito.verify(wsClientMockMachine.wsClientMock).url("https://myserver.com")
    // Verify some headers...
    Mockito.verify(wsClientMockMachine.wsRequestMock).withAuth("username", "password", WSAuthScheme.BASIC)
    // Verify that the correct http method has been called - and also make sure the body is correct
    Mockito.verify(wsClientMockMachine.wsRequestMock).post(Matchers.eq("""some_content"""))(Matchers.any())


More exhaustive examples can be found here: https://github.com/r10r-org/ws-client-mock-machine/blob/master/src/test/scala/wsclientmockmachine/api/WsClientMockMachineSpec.scala


Compatibility
-------------

- 1.2.x => Play 2.6.x / Mockito 2.x


Other solutions
---------------

 - MockWs (https://github.com/leanovate/play-mockws)
 - Wiremock (http://wiremock.org)


Release process
---------------

Prerequisites:

- gpg installed with a proper public key for signing
- Proper Sonatype credentials in ~/.sbt/0.13/sonatype.sbt

Release:

    > sbt release


