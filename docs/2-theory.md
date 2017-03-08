# Theory - Inversion of Control & DI

## In the Beginning...
When programmers first emerged from the lands of computers the size of rooms and started to have access to compilers,
they used simple imperative programming. In this original methodology, your program code would call out to reusable,
external libraries (which were provided either as 3rd-party libraries or by the OS), forming the original pattern of
control - custom code calls reusable code to get work done.

This worked fine, and in fact still does in many applications, however with the increasing prevalence of large, complex
systems and the need for more testing, the flaws in this approach started to show. Each call out to other code
introduced _hard dependencies_ between classes, files and libraries. In addition to making dependency changes a
nightmare, this also got in the way of testing - how can you mock objects that you can't change in the code under test?

## Inversion of Control
_Or: "Don't call us, we'll call you."_ ([source](https://en.wikipedia.org/wiki/Inversion_of_control#Overview))

There are many approaches to solving this problem, but most of them fall under the umbrella phrase _Inversion of
Control_. The principle behind inversion of control is to make client code not have a hard dependency on another
component directly - instead it asks for an interface (in OOP terms), which is then fulfilled at runtime by some
method. Dependency Injection is one of these methods, along with more infamous patterns like Factories, Service Locators
and Strategies.

So far so simple. But how do you get an implementation from an abstract interface? This is where the individual patterns
come into things. Below we'll look at three of the competing patterns, ending with our target - Dependency Injection.

### `ObjectFactoryFactory`
_Or: The Enterprise engineers best friend. Fizzbuzz!_

Factories are a very common implementation of Inversion of Control, especially common in large, enterprise codebases.
Conceptually they are like, well, factories - if you have a factory that produces bolts, you can ask it for a new bolt
and it will give you a new bolt. In programming, you'll generally have a factory for a certain _type_ in OOP languages.
As an example, say you have a class needing database connections, you might have a `ConnectionFactory` which provides
`Connection` objects on demand for clients. At runtime, the `ConnectionFactory` could be told what database to give
connections out to - this separates the client (which just wants to use a database) from the configuration for that
database. It also makes testing easier; just tell the Factory to produce mock `Connection` objects instead of real ones.

This pattern is not without flaws however - many an engineer has cursed nested factories for making their lives
miserable, hence `ObjectFactoryFactory`, so care has to be taken to not apply the pattern too literally in all cases.
Generally, having nested factories is a nasty code smell, but can be necessary in some (rare) contexts.

### Strategies
_Or: Not your mothers favourite RTS game._

The Strategy pattern is a pattern that has found its calling in the world of internet-connected applications. The basic
premise is that you have a task you wish to complete (for example, downloading a file) but multiple ways you could go
about fulfilling that task. Sticking with the file download example, you might have several strategies - HTTP download,
FTP download, BitTorrent download, or even local file "download" for development. All the strategies would implement a
common interface, say `DownloadStrategy` - when a client is instantiated it would be passed one of the implementations
of that interface. If you wish to see a real world usage of the pattern, take a look at the
[Homebrew project](https://github.com/Homebrew/brew/blob/master/Library/Homebrew/download_strategy.rb) which uses
download strategies in Ruby code.

![Strategy pattern UML](https://upload.wikimedia.org/wikipedia/commons/3/39/Strategy_Pattern_in_UML.png)
(source: [Wikipedia](https://en.wikipedia.org/wiki/Strategy_pattern))

### Service Locator
_Or: "Hello, operator? Yes, I want a pizza with..."_

This pattern is the best one to compare with Dependency Injection, as the two are relative opposites. In Service
Locator-based code, all code has access to a central _locator_ or _directory_, which performs look ups in the manner of
a phone book based on the name/type of the service being requested. For example, an application might want to be given
a logger, a database handler and another component in the application. To get them, it would contact the directory,
generally by a method call on the directory, which would provide a matching implementation for the query if one exists.

This works relatively well, however there's still a central component _all_ code has a hard dependency to - the
directory! If you have to change the directory interface for any reason, you'll need to amend the _entire_ codebase to
apply the change. There is also a risk of failure, where a component requests a name that simply doesn't exist in the
directory, introducing extra error handling needs. There are also other side effects which are less desirable - any code
that can access the directory can also _add_ to the directory, potentially providing an injection point for malicious
code, which other components might then pull and use. While it's an improvement over testing classic applications, the
directory is still a big wad of state a testing framework would have to manage.

## Dependency Injection 101
_Or: "Here's something to work with. Touch. Nothing. Else."_

There's an excellent quote from a contributor on Stack Overflow which explains the rationale for dependency Injection
very nicely;

> When you go and get things out of the refrigerator for yourself, you can cause problems. You might leave the door
> open, you might get something Mommy or Daddy doesn't want you to have. You might even be looking for something we
> don't even have or which has expired.

> What you should be doing is stating a need, "I need something to drink with lunch," and then we will make sure you
> have something when you sit down to eat.

-- "DI for five-year olds", John Munsch on [Stack Overflow](http://stackoverflow.com/a/1638961)

Dependency injection is fundamentally an attempt to improve upon service locator. Instead of clients demanding an item
from a central directory, it is provided with the item during construction, typically as a constructor parameter, but
some implementations support other forms of injection (such as `@Inject` marked fields in Java + Guice). This neatly
divides construction and usage, thus decreasing coupling - the client does not need to know how to construct the
service, just that it receives one pre-configured when constructed.

This provides a massive bonus for testing as well - there is no external state to manage in the form of a directory,
as the testing code can construct an injector and run it on the class under test directly, with the required
dependencies able to be mocked safely and easily. The injector will then just supply the mock stubs, rather than the
real implementation(s) when the code under test is constructed.

On the next page, we'll start going into dependency injection in practice.
