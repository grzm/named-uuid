package com.grzm;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.UUID;

/**
 * Helpers to generate Version 3 and Version 5 UUID values for {@link java.util.UUID}
 */
public class NamedUUID {

    /**
     * The nil UUID, {@code 00000000-0000-0000-0000-000000000000}
     */
    public static final UUID NIL = UUID.fromString("00000000-0000-0000-0000-000000000000");
    /**
     * Namespace UUID for fully-qualified domain names
     */
    public static final UUID NS_DNS = UUID.fromString("6ba7b810-9dad-11d1-80b4-00c04fd430c8");
    /**
     * Namespace UUID for URLs
     */
    public static final UUID NS_URL = UUID.fromString("6ba7b811-9dad-11d1-80b4-00c04fd430c8");
    /**
     * Namespace UUID for ITU object identifiers
     */
    public static final UUID NS_OID = UUID.fromString("6ba7b812-9dad-11d1-80b4-00c04fd430c8");
    /**
     * Namespace UUID for X.500 distinguished names
     */
    public static final UUID NS_X500 = UUID.fromString("6ba7b814-9dad-11d1-80b4-00c04fd430c8");

    enum Algorithm {MD5, SHA1}

    private static final String MD5_ENCODING = "MD5";
    private static final String SHA1_ENCODING = "SHA-1";

    private static final int LONG_BYTE_LENGTH = Long.BYTES;
    private static final int UUID_BYTE_LENGTH = 16;

    private static final int V5_VERSION = 0x50;
    private static final int V3_VERSION = 0x30;
    private static final int IETF_VARIANT = 0x80;

    private static byte[] bytes(UUID uuid) {
        byte[] bytes = new byte[UUID_BYTE_LENGTH];
        long msb = uuid.getMostSignificantBits();
        long lsb = uuid.getLeastSignificantBits();

        for (int i = 0; i < LONG_BYTE_LENGTH; i++) {
            bytes[i] = (byte) ((msb >> ((LONG_BYTE_LENGTH - i - 1) * 8)) & 0xff);
        }

        for (int i = LONG_BYTE_LENGTH; i < UUID_BYTE_LENGTH; i++) {
            bytes[i] = (byte) ((lsb >> ((UUID_BYTE_LENGTH - i - 1) * 8)) & 0xff);
        }

        return bytes;
    }

    private static UUID uuid(byte[] bytes) {
        long msb = 0;
        for (int i = 0; i < LONG_BYTE_LENGTH; i++) {
            msb = (msb << 8) | (bytes[i] & 0xff);
        }

        long lsb = 0;
        for (int i = LONG_BYTE_LENGTH; i < UUID_BYTE_LENGTH; i++) {
            lsb = (lsb << 8) | (bytes[i] & 0xff);
        }

        return new UUID(msb, lsb);
    }

    private static UUID namedUUID(Algorithm algorithm, UUID namespace, String name)
            throws IllegalArgumentException {

        if (null == namespace) {
            throw new IllegalArgumentException("namespace is null");
        }

        if (null == name) {
            throw new IllegalArgumentException("name is null");
        }

        MessageDigest digest;
        String encoding;

        if (Algorithm.MD5 == algorithm) {
            encoding = MD5_ENCODING;
        } else {
            encoding = SHA1_ENCODING;
        }

        try {
            digest = MessageDigest.getInstance(encoding);
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new AssertionError(String.format("Unknown encoding passed to MessageDigest: %s", encoding));
        }

        digest.update(bytes(namespace));
        digest.update(name.getBytes(StandardCharsets.UTF_8));

        byte[] bytes;

        if (Algorithm.MD5 == algorithm) {
            bytes = digest.digest();
        } else {
            byte[] shaDigest = digest.digest();
            bytes = new byte[UUID_BYTE_LENGTH];
            System.arraycopy(shaDigest, 0, bytes, 0, UUID_BYTE_LENGTH);
        }

        bytes[6] &= 0x0f; // clear version
        if (Algorithm.MD5 == algorithm) {
            bytes[6] |= V3_VERSION;
        } else {
            bytes[6] |= V5_VERSION;
        }

        bytes[8] &= 0x3f; // clear variant
        bytes[8] |= IETF_VARIANT;

        return uuid(bytes);
    }

    /**
     * Return the a Version 3 UUID for the given namespace UUID and name
     *
     * @param namespace UUID value for the namespace
     * @param name      Namespaced value
     * @return Version 3 UUID corresponding to the given namespace and value
     * @throws NullPointerException when namespace or name are null
     */
    public static UUID v3(UUID namespace, String name)
            throws NullPointerException {
        return namedUUID(Algorithm.MD5, namespace, name);
    }

    /**
     * Return the Version 3 UUID for the given namespace UUID string and name
     *
     * @param namespace String UUID value for the namespace
     * @param name      Namespaced value
     * @return Version 3 UUID corresponding to the given namespace and value
     * @throws NullPointerException     when namespace or name are null
     * @throws IllegalArgumentException when namespace isn't a valid UUID value
     */
    public static UUID v3(String namespace, String name)
            throws NullPointerException, IllegalArgumentException {
        return namedUUID(Algorithm.MD5, UUID.fromString(namespace), name);
    }

    /**
     * Return the Version 5 UUID for the given namespace UUID and name
     *
     * @param namespace UUID value for the namespace
     * @param name      Namespaced value
     * @return Version 5 UUID corresponding to the given namespace and value
     * @throws NullPointerException when namespace or name are null
     */
    public static UUID v5(UUID namespace, String name)
            throws NullPointerException {
        return namedUUID(Algorithm.SHA1, namespace, name);
    }

    /**
     * Return the Version 5 UUID for the given namespace UUID string and name
     *
     * @param namespace String UUID value for the namespace
     * @param name      Namespaced value
     * @return Version 5 UUID corresponding to the given namespace and value
     * @throws NullPointerException     when namespace or name are null
     * @throws IllegalArgumentException when namespace isn't a valid UUID value
     */
    public static UUID v5(String namespace, String name)
            throws NullPointerException, IllegalArgumentException {
        return namedUUID(Algorithm.SHA1, UUID.fromString(namespace), name);
    }
}
