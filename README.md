WsClientMockMachine - simple mocking of Play WsClient
=====================================================

The problem:
------------
If you are using Play then you'll also likely use Play's nice http library WsClient.
And because you are a professional you do test your code - don't you?

But testing play-ws in a mocked fashion is really cumbersome - mostly because you cannot simply
mock an interaction, but you have to mock a chain - from url() - to request - to response.

Well. You can do that, but that is annoying.

The solution:
-------------
WsClientMockMachine! It is basically a read-to-use mock for wsClient. It has all basic
interactions already mocked and you can use it straight away.

Examples:
---------

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


Other solutions:
----------------

 - Use MockWs (https://github.com/leanovate/play-mockws)
 - Use Wiremock (http://wiremock.org).