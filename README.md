WsClientMockMachine - simple mocking of Play WsClient
=====================================================

[![Build Status](https://travis-ci.org/r10r-org/ws-client-mock-machine.svg?branch=master)](https://travis-ci.org/r10r-org/ws-client-mock-machine)

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

Aren't there other solutions?
-----------------------------
Yes. At least two excellent alternatives exist: [Play-MockWs](https://github.com/leanovate/play-mockws)
and [Wiremock](http://wiremock.org).

There are still reasons why we believe WsClientMockMachine is very different to these two options.

Play-MockWs for instance is very good if you like to setup your tests in a play-like fashion using routers. It also does
not use Mockito at all. This leads to a slightly different style of testing. And if you like that - go for it.
Play-MockWs is very good - we also use it for some of our tests.

Wiremock on the other hand is using a real http server to do stuff. This is very nice, but again - this is different
to WsClientMockMachine. Wiremock testcases are more real integration tests with all pros and cons (like potentially
more flaky and slow tests due to the need for real socket connections). But it allows to simulate the real thing
and not only mocks. Also cool.

And WsClientMockMachine? It's in between. It is 100% based on Mockito
for testing. And there's no http traffic which means rock-solid tests.
So - if you like Mockito, mocks and verifications then WsClientMockMachine is for you.
We like Mockito - and WsClientMockMachine brings both world together with minimal setup.


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

Add it to your project
----------------------

    libraryDependencies += "org.r10r" %% "ws-client-mock-machine" % "1.2.x"


Note: Find the latest version [on maven central](https://search.maven.org/#search%7Cga%7C1%7Ca%3A%22ws-client-mock-machine_2.12%22)

Compatibility
-------------

- 1.2.x => Play 2.6.x / Mockito 2.x


Release process
---------------

Prerequisites:

- gpg installed with a proper public key for signing
- Proper Sonatype credentials in ~/.sbt/0.13/sonatype.sbt

Release:

    > sbt release


