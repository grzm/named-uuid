# NamedUUID - Named UUID helpers for java.util.UUID

`com.grzm.NamedUUID` is a minimal class that makes it easy to create
Version 3 and Version 5 `java.util.UUID` values.

## About

[Version 3 and Version 5 UUIDs][named-uuids] provide a deterministic
method of representing arbitrary strings as UUIDs.

Version 3 UUIDs are based on an MD5 hash, while Version 5 uses
SHA-1. As the values are hashed, as opposed to encoded, the generated
UUIDs are non-reversible, other than through brute force (modulo
weaknesses in the underlying algorithms).

You can use named UUIDs to map external identifiers to UUIDs in a
deterministic manner which provides robustness from collisions.

[named-uuids]: https://en.wikipedia.org/wiki/Universally_unique_identifier#Versions_3_and_5_(namespace_name-based)

## Release and Dependency Information

Latest release: `1.0.0`

[Maven](http://maven.apache.org) dependency coordinates

```xml
<dependency>
  <groupId>com.grzm</groupId>
  <artifactId>named-uuid</artifactId>
  <version>1.0.0</version>
</dependency>
```

## Usage

The `com.grzm.NamedUUID` class provides static functions to create Version 3 and Version 5 UUIDs.

It also provides constants for the namespaces provided in the specification as well as the NIL UUID.

 - `NamedUUID.NS_DNS`: [fully-qualified domain names](https://en.wikipedia.org/wiki/Fully_qualified_domain_name) (`6ba7b810-9dad-11d1-80b4-00c04fd430c8`)
 - `NamedUUID.NS_URL`: [URLs](https://en.wikipedia.org/wiki/URL) (`6ba7b811-9dad-11d1-80b4-00c04fd430c8`)
 - `NamedUUID.NS_OID`: [ITU object identifiers](https://en.wikipedia.org/wiki/Object_identifier) (`6ba7b812-9dad-11d1-80b4-00c04fd430c8`)
 - `NamedUUID.NS_X500`: [X.500](https://en.wikipedia.org/wiki/X.500) [distinguished names](https://en.wikipedia.org/wiki/Lightweight_Directory_Access_Protocol) (`6ba7b814-9dad-11d1-80b4-00c04fd430c8`)
 - `NamedUUID.NIL` `00000000-0000-0000-0000-000000000000`

Some [examples](src/test/java/com/grzm/NamedUUIDExamplesAsTest.java);

```java
package com.grzm;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class NamedUUIDExamplesAsTest {

    @Test
    void examples() {
        UUID exampleDotComUUID = NamedUUID.v5(NamedUUID.NS_DNS, "example.com");
        assertEquals(UUID.fromString("cfbff0d1-9375-5685-968c-48ce8b15ae17"), exampleDotComUUID,
                "create a UUID using the NS_DNS namespace and domain");

        UUID wikipediaUUIDPageUUID = NamedUUID.v5(NamedUUID.NS_URL,
                "https://en.wikipedia.org/wiki/Universally_unique_identifier");
        assertEquals(UUID.fromString("e2d4a904-c7c1-5557-9c4f-7645bacf2cd2"), wikipediaUUIDPageUUID,
                "create a UUID for a URL using the NS_URL namespace");

        UUID yourNamespace = NamedUUID.v5(NamedUUID.NS_DNS, "yourdomain.com");
        assertEquals(UUID.fromString("930bb4d3-2265-5d59-95c1-726a7647a7f0"), yourNamespace,
                "create a UUID for your domain to be used as your own namespace");

        UUID yourNamespaced54 = NamedUUID.v5(yourNamespace, "54");
        assertEquals(UUID.fromString("c2fabdea-4cb8-5491-bcc5-ecac4a019d7b"), yourNamespaced54,
                "create a UUID for a value namespaced by your domain");

        UUID yourNamespaced55 = NamedUUID.v5(yourNamespace, "55");
        assertEquals(UUID.fromString("1b907b1c-6886-594d-83f6-3730b120e912"), yourNamespaced55,
                "create a UUID for a different value namespaced by your domain");

        assertNotEquals(yourNamespaced54, yourNamespaced55,
                "confirm two distinct values in same namespace are distinct");

        UUID yourNamespaceV3 = NamedUUID.v3(NamedUUID.NS_DNS, "yourdomain.com");
        assertEquals(UUID.fromString("3b1a8610-1692-3597-9e60-c5db58592c45"), yourNamespaceV3,
                "create a Version 3 UUID for your domain");
        assertNotEquals(yourNamespace, yourNamespaceV3,
                "confirm Version 3 and Version 5 UUIDs for same value are distinct.");

        UUID otherNamespace = NamedUUID.v5(NamedUUID.NS_DNS, "otherdomain.com");
        assertEquals(UUID.fromString("8590a268-8208-588a-b261-b65a2d7f2564"), otherNamespace,
                "create a UUID for another namespace");

        UUID otherNamespaced54 = NamedUUID.v5(otherNamespace, "54");
        assertEquals(UUID.fromString("e8d57c22-f102-542f-bec1-22887f9d93fe"), otherNamespaced54,
                "create a UUID for 54 in the other namespace");

        assertNotEquals(yourNamespaced54, otherNamespaced54,
                "confirm the same value in two different namespaces don't collide");
    }
}
```

## Copyright and License

© 2019 Michael Glaesemann

Distributed under the MIT License.
