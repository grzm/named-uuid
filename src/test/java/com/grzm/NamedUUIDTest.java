package com.grzm;

        import org.junit.jupiter.api.Test;
        import org.junit.jupiter.params.ParameterizedTest;
        import org.junit.jupiter.params.provider.Arguments;
        import org.junit.jupiter.params.provider.MethodSource;

        import java.util.UUID;
        import java.util.stream.Stream;

        import static org.junit.jupiter.api.Assertions.*;
        import static org.junit.jupiter.params.provider.Arguments.arguments;

class NamedUUIDTest {

    static Stream<Arguments> v3Provider() {
        final String name = "example.com";
        return Stream.of(
                arguments(UUID.fromString("9073926b-929f-31c2-abc9-fad77ae3e8eb"),
                        NamedUUID.NS_DNS, name),
                arguments(UUID.fromString("a0473a67-27a1-3c05-a2d1-5c134639347f"),
                        NamedUUID.NS_URL, name),
                arguments(UUID.fromString("109f8204-164d-33ef-871d-d92c373e8c66"),
                        NamedUUID.NS_OID, name),
                arguments(UUID.fromString("11c2f001-e3a4-3ad0-90f7-88ac418c36b8"),
                        NamedUUID.NS_X500, name),
                arguments(UUID.fromString("dfff2161-94de-3de8-a5d7-5d517fd724d6"),
                        NamedUUID.NIL, name));
    }

    @ParameterizedTest
    @MethodSource("v3Provider")
    void v3(UUID expected, UUID namespace, String name) {
        assertEquals(expected, NamedUUID.v3(namespace, name));
    }

    @ParameterizedTest
    @MethodSource("v3Provider")
    void v3StringNamespace(UUID expected, UUID namespace, String name) {
        assertEquals(expected, NamedUUID.v3(namespace.toString(), name));
    }

    static Stream<Arguments> v5Provider() {
        final String name = "example.com";
        return Stream.of(arguments(UUID.fromString("cfbff0d1-9375-5685-968c-48ce8b15ae17"),
                NamedUUID.NS_DNS, name),
                arguments(UUID.fromString("a5cf6e8e-4cfa-5f31-a804-6de6d1245e26"),
                        NamedUUID.NS_URL, name),
                arguments(UUID.fromString("eb6106fd-8a37-5395-b3f7-7cb93195fdba"),
                        NamedUUID.NS_OID, name),
                arguments(UUID.fromString("f014ed3c-177a-541e-a840-fc330670f8e8"),
                        NamedUUID.NS_X500, name),
                arguments(UUID.fromString("5e7c9a9e-d31d-5f7a-8599-b72f466a0cae"),
                        NamedUUID.NIL, name));
    }

    @ParameterizedTest
    @MethodSource("v5Provider")
    void v5(UUID expected, UUID namespace, String name) {
        assertEquals(expected, NamedUUID.v5(namespace, name));
    }

    @ParameterizedTest
    @MethodSource("v5Provider")
    void v5StringNamespace(UUID expected, UUID namespace, String name) {
        assertEquals(expected, NamedUUID.v5(namespace.toString(), name));
    }

}
