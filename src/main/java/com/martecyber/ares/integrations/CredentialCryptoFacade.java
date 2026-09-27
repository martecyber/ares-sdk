package com.martecyber.ares.integrations;

/** Lets a plugin encrypt/decrypt a secret it stores in its own plugin-owned table (e.g. an API
 *  token), reusing Ares's own credential encryption key rather than rolling its own — without
 *  needing {@code CredentialEncryptionService} (ares-core-internal) on its classpath. Generic:
 *  not tied to the generic {@code Integration} entity's own stored-credentials column, unlike
 *  {@link IntegrationFacade#loadCredentials}. */
public interface CredentialCryptoFacade {

    EncryptedValue encrypt(String plaintext);

    String decrypt(byte[] ciphertext, byte[] iv);

    record EncryptedValue(byte[] ciphertext, byte[] iv) {}
}
