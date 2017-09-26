# sInstagram [![Build Status](https://img.shields.io/travis/yukihirai0505/sInstagram.svg)](https://travis-ci.org/yukihirai0505/sInstagram) [![Maven Central](https://img.shields.io/maven-central/v/com.yukihirai0505/sinstagram_2.11.svg)](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.yukihirai0505%22%20AND%20a%3A%22sinstagram_2.11%22) [![Coverage Status](https://img.shields.io/coveralls/yukihirai0505/sInstagram/master.svg)](https://coveralls.io/github/yukihirai0505/sInstagram?branch=master) [![Join the chat at https://gitter.im/yukihirai0505/sInstagram](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/yukihirai0505/sInstagram)

A Scala library for the Instagram API.
An asynchronous non-blocking Scala Instagram API Wrapper,
implemented using play-json.

## Prerequisites

Scala 2.11.+ is supported.

- Go to https://www.instagram.com/developer/clients/manage/, login with your instagram account
  and register your application to get a client id and a client secret.
- Once the app has been created, register callback.

## Setup

### sbt

If you don't have it already, make sure you add the Maven Central as resolver in your SBT settings:

```scala
resolvers += Resolver.sonatypeRepo("releases")
```

Also, you need to include the library as your dependency:

```scala
libraryDependencies += "com.yukihirai0505" % "sinstagram_2.11" % "0.1.8"
```

http://mvnrepository.com/artifact/com.yukihirai0505/sinstagram_2.11/0.1.8

## Usage

### Config

Add your client id and client secret as either environment variables or as part of your configuration.
sInstagram will look for the following environment variables:

```
export INSTAGRAM_CLIENT_ID='my-instagram-client-id'
export INSTAGRAM_SECRET='my-instagram-secret'
export INSTAGRAM_CALLBACK_URL='my-instagram-callback-url'
```

You can also add them to your configuration file,
usually called `application.conf`:

```
instagram {
  client = {
    id = "my-instagram-client-id"
    secret = "my-instagram-secret"
  }
  callbackUrl = "my-instagram-callback-url"
}
```

These configurations will be automatically loaded when creating a instagram client,
so all you have to do is to initialize your clients as following:

```scala
import com.yukihirai0505.sInstagram.InstagramAuth
import com.yukihirai0505.sInstagram.model.{ResponseType, Scope}

val instagramAuth = new InstagramAuth
val scopes: Seq[Scope] = Seq(Scope.BASIC)
val authUrl = instagramAuth.authURL(scopes = scopes)
val accessTokenFuture = instagramAuth.requestToken(code = "the-code-from-callback")
```

### Examples

Alternatively, you can also specify your tokens directly when creating the client:

```scala
  import com.yukihirai0505.sInstagram.model.{ResponseType, Scope}
  import com.yukihirai0505.sInstagram.responses.auth.{AccessToken, Auth}
  import com.yukihirai0505.sInstagram.{Instagram, InstagramAuth}

  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.util.{Failure, Success}

  val clientId = "client-id"
  val clientSecret = "client-secret"
  val callbackUrl = "callback-URI"
  val instagramAuth = new InstagramAuth
  val scopes: Seq[Scope] = Seq(Scope.BASIC) // other: Scope.FOLLOWER_LIST, Scope.PUBLIC_CONTENT, Scope.COMMENTS, Scope.LIKES, Scope.RELATIONSHIPS

  // Server-Side login
  // Step 1: Get a URL to call. This URL will return the CODE to use in step 2
  val authUrl = instagramAuth.authURL(clientId, callbackUrl, ResponseType.CODE, scopes)

  // Step 2: Use the code to get an AccessToken
  val accessTokenFuture = instagramAuth.requestToken(clientId, clientSecret, callbackUrl, "the-code-from-step-1")
  val accessToken = accessTokenFuture onComplete {
    case Success(Some(token: AccessToken)) => token
    case Failure(t) => println("An error has occured: " + t.getMessage)
  }

  // Making an authenticated call
  val auth: Auth = AccessToken("an-access-token")
  // If you want to use signed access token
  // val auth: Auth = SignedAccessToken("an-access-token", clientSecret)
  val instagram: Instagram = new Instagram(auth)
  // The library is asynchronous by default and returns a promise.
  val future = instagram.getRecentMediaFeed()

  import scala.language.postfixOps

  future onComplete {
    case Success(body) =>
      body.fold()(b => b.data.foreach(println))
    case Failure(t) => println("An error has occured: " + t.getMessage)
  }
```

Please look at this file to see all available methods:

https://github.com/yukihirai0505/sInstagram/blob/master/src/main/scala/com/yukihirai0505/sInstagram/Instagram.scala

## References

inspired by following source code

- https://github.com/sachin-handiekar/jInstagram
- https://github.com/Rydgel/scalagram
