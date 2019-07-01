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
