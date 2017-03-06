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
  - We attach our WebSocket handler class to `/ws`.
  - We assign all clients a UUID in a `before` filter. This will be used later to record separate clients via cookie.
  - We enable a 'route overview' at `/debug/routes` - more on this later.
  - We add our application routes using standard HTTP verbs, by using [Spark's routing methods](http://sparkjava.com/documentation.html#routes).
    - In particular, we stuff this UUID into a cookie so the WebSocket code client-side can identify to the server.
