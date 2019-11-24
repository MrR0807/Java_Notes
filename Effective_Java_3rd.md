# Item 1: Consider static factory methods instead of constructors

Note that a static factory method is not the same as the Factory Method pattern from Design Patterns. A class can provide its clients with static factory methods instead of, or in addition to, public constructors. Providing a static factory method instead of a public constructor has both advantages and disadvantages:

#### One advantage of static factory methods is that, unlike constructors, they have names

Programmers have been known to get around this restriction by providing two constructors whose parameter lists differ only in the order of their parameter types. This is a  eally bad idea. The user of such an API will never be able to remember which constructor is which and will end up calling the wrong one by mistake.
**In cases where a class seems to require multiple constructors with the same signature, replace the constructors with static factory methods and carefully chosen names to highlight their differences.**


#### A second advantage of static factory methods is that, unlike constructors, they are not required to create a new object each time they’re invoked.
