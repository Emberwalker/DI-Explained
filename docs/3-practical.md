# Dependency Injection in Practice
So the stage is set. We have our poorly tested project, and we have the theory of how DI works.

Note: In this section, the amended code is in `samples/2`!

## Google Guice
_It's pronounced 'Juice', the Google devs insist._

Google's Guice library is one of the more common DI implementations in existence, used in Java Virtual Machine projects
everywhere, small to large. We'll be scratching the surface of its functionality here, but it's suggested you keep the
[Guice documentation](https://github.com/google/guice/wiki/GettingStarted) open in the background in case you need it.

## Setting up Guice in the Application
### Mark Injection Points
The first step to enabling Guice in a project is identifying where to actually perform injections. The simplest way to
do this is to use constructors marked with `@Inject` (the one in `com.google.guice`). Let's demonstrate with the
`ClickerWebSocket` class we met earlier.

Here's the original class signature:
```kotlin
@WebSocket
class ClickerWebSocket { ... }
```

And with the injections for the `IClickerHandler` added:
```kotlin
@WebSocket
class ClickerWebSocket @Inject constructor(private val handler: IClickerHandler) { ... }
```

As you can see, it does slightly complicate the class signature, but this is partially because we're defining class
fields in the constructor, as is the Kotlin style. In Java, you would apply `@Inject` to your constructor method
instead:
```java
@Inject
public ClickerWebSocket(IClickerHandler handler) {
  this.handler = handler;
}
```

Suddenly the Kotlin verbosity doesn't seem so bad...

Note that we don't inject Loggers. They already come from a Factory, and Guice developers have recommended against
logger injection in the past (for any logger which isn't the JDK built-in one).

### Define Bindings in Modules
So now we've marked all the injection sites we require. Great! But how does Guice know _what_ to inject into these new
fields? As shown in the [Guice Getting Started](https://github.com/google/guice/wiki/GettingStarted) guide, we need to
create _bindings_ (mappings of type to implementation) inside _modules_ (collections of bindings). Guice makes this very
simple to do, and Kotlin reduces the boilerplate nicely. Let's start by setting up some modules; open
`io.drakon.uod.di_explained.Guice.kt` in an editor and take a look. The comments will explain it all.

### Why DI?
Given we're only injecting a couple components, why bother? In smaller applications, like this, DI is still useful as it
aids testing. In our case, we can now freely detach the database handler and attach a fake one during tests, as well
as mock the `ClickerHandler` in general thanks to our separation of the handler from the WebSocket code.

Look at the new test `ClickerHandler` added in this sample to see it in action! In real projects, injection can allow
_vastly_ more tests to be written in this manner - hopefully this small, slightly contrived example demonstrates the
principle though!
