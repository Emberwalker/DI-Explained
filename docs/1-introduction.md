# Introduction

Let's start simple: what's the project we'll be following to demonstrate the process of adopting Dependency Injection?

## Ye Olde Clicker

Clicker games are notoriously simple: Click button, receive point. Not very interesting, but most of the logic is found
in the backend implementation, which makes it ideal for our purposes. Our application will be HTML5 + JS, and will
communicate with the backend over HTTP (page/resource loading) and WebSocket (game communications).

Since this is an explanation of Dependency Injection, we will _not_ be covering the frontend implementation, which will
remain static throughout the various samples. Only the stuff executed on the JVM server-side is of interest, so focus on
the Kotlin code!

Where possible, tests will be provided for code in the backend. Third-party components will _not_ be tested in
particular.

## Spark 101

Our backend runs on Spark, the website for which is provided in the README of this book.

Let's orient ourselves with the codebase. Once you've opened the project in IDEA or Eclipse (see README), you should be
able to see the `src/main/kotlin` directory - this is where our code lives. For now, just open
`io.drakon.uod.di_explained.Main` in your editor. The structure should look, vaguely, familiar - there's a `public
static main`-type method in the `companion object` block which will be executed when the application starts. It simply
constructs an instance of `Main` and then calls `Main#start()`, which in turn sets up and starts our application.

Useful things to note at this point:
- We have a `LOGGER` object, created via slf4j's `LoggerFactory` specific to this class. This is good practice.
- In `Main#start()`:
  - We elect to load static files from `src/main/resources/public`, if you need to modify frontend code.
  - We attach our WebSocket handler to `/ws`.
  - We assign all clients a UUID in a `before` filter. This will be used later to record separate clients via cookie.
  - We enable a 'route overview' at `/debug/routes` - more on this later.
  - We add our application routes using standard HTTP verbs, by using [Spark's routing methods](http://sparkjava.com/documentation.html#routes).
    - In particular, we stuff this UUID into a cookie so the WebSocket code client-side can identify to the server.

## WebSocket Handling
Next open the classes in `io.drakon.uod.di_explained.handlers` and look at the structure of each.

In some of the classes, we have some basic essentials - a `LOGGER` object, and a bundle of methods. The `WebSocket`
annotations are what Spark uses to identify which methods to call on events in `ClickerWebSocket`. In particular, make
sure you understand the `ClickerWebSocket#message` method.

In the `DataClasses.kt` file you'll see a few Kotlin specific items. Data classes are what Java programmers tend to
refer to as POJOs - simple objects whose sole purpose is to hold data. In `io.drakon.uod.di_explained.Extensions.kt`,
you'll also see that there's a method whose signature looks like `fun Session.send(...)` - this is an _extension
method_, which attaches the defined method to an existing class defined elsewhere, in this case the Jetty session.
Understanding extension methods is not important for this tutorial, but if you want to learn more, check out the
[Kotlin documentation on the topic.](https://kotlinlang.org/docs/reference/extensions.html)

Other than that, you'll see that as much as possible behaviour is defined by _interfaces_ rather than concrete
implementations, but not they are currently untestable as they have hard references to implementations, such as for the
`IClickerStore` implementation being hardcoded to create a SQLite data store. We'll fix this with dependency injection!

## What Next?
Next we'll take a look at the theory behind Dependency Injection, and its parent concept Inversion of Control.
