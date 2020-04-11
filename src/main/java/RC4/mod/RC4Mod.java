package RC4.mod;

public class RC4Mod {
    private final int N = 256;
    private final int half = N / 2;
    private final byte[] S1 = new byte[half];
    private final byte[] S2 = new byte[half];
    private final byte[] k1;
    private final byte[] k2;

    private void swap(byte[] arr, int i, int j) {
        byte temp = arr[j];
        arr[j] = arr[i];
        arr[i] = temp;
    }

    private void KSA() {
        // Initializing both states
        for (int i = 0; i < half - 1; i++) {
            S1[i] = i;
        }
        for (int i = half; i < N - 1; i++) {
            S2[i - half] = i;
        }

        int j = 0;
        for (int i = 0; i < half - 1; i++) {
            j = j + S1[i] + k1[i % k1.length] % half;
            swap(S1, i, j);

            j = j + S2[i] + k2[i % k2.length] % half;
            swap(S2, i, j);
        }

    }

    public RC4Mod(final byte[] key1, final byte[] key2) {
        if (key1.length < 1 || key1.length > 128 || key2.length < 1 || key2.length > 128) {
            throw new IllegalArgumentException("keys must be between 1 and 128 bytes");
        } else {
            this.k1 = key1;
            this.k2 = key2;
        }
    }

    public byte[] encrypt(final byte[] plaintext) {
        KSA();
        final byte[] ciphertext = new byte[plaintext.length];
        int i = 0, j1 = 0, j2 = 0;
        byte t1 = 0, t2 = 0;
        for (int counter = 0; counter < plaintext.length; counter += 2) {
            // TO-DO
            ciphertext[counter] = (byte) (plaintext[counter] ^ t1);
            ciphertext[counter] = (byte) (plaintext[counter + 1] ^ t2);

        }
        return ciphertext;
    }

    public byte[] decrypt(final byte[] ciphertext) {
        return encrypt(ciphertext);
    }
}