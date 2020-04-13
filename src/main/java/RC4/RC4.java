package RC4;

import org.apache.commons.lang3.time.StopWatch;


public class RC4 {
    private final byte[] S = new byte[256];
    private final byte[] T = new byte[256];
    private final byte[] key;

    private void KSA() {
        int keylen = key.length;
        for (int i = 0; i < 256; i++) {
            S[i] = (byte) i;
            T[i] = key[i % keylen];
        }
        int j = 0;
        byte tmp;
        for (int i = 0; i < 256; i++) {
            j = (j + S[i] + T[i]) & 0xFF;
            tmp = S[j];
            S[j] = S[i];
            S[i] = tmp;
        }
    }

    public RC4(final byte[] key) {
        if (key.length < 1 || key.length > 256) {
            throw new IllegalArgumentException("key must be between 1 and 256 bytes");
        } else {
            this.key = key;
        }
    }

    public byte[] encrypt(final byte[] plaintext) {
        KSA();
        final byte[] ciphertext = new byte[plaintext.length];
        int i = 0, j = 0, k, t;
        byte tmp;
        for (int counter = 0; counter < plaintext.length; counter++) {
            i = (i + 1) & 0xFF;
            j = (j + S[i]) & 0xFF;
            tmp = S[j];
            S[j] = S[i];
            S[i] = tmp;
            t = (S[i] + S[j]) & 0xFF;
            k = S[t];
            ciphertext[counter] = (byte) (plaintext[counter] ^ k);
        }
        return ciphertext;
    }

    public byte[] decrypt(final byte[] ciphertext) {
        return encrypt(ciphertext);
    }

    public long timedEncryption(final byte[] plaintext) {
        StopWatch watch = new StopWatch();
        watch.start();
        encrypt(plaintext);
        watch.stop();
        return watch.getNanoTime();
    }

    public long timedDecryption(final byte[] ciphertext) {
        return timedEncryption(ciphertext);
    }
}