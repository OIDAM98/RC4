package RC4.mod;

public class RC4Mod {
    private final int N = 256;
    private final int half = N / 2;
    private final int[] S1 = new int[half];
    private final int[] S2 = new int[half];
    private final byte[] k1;
    private final byte[] k2;

    private void swap(int[] arr, int i, int j) {
        int temp = arr[j];
        arr[j] = arr[i];
        arr[i] = temp;
    }

    private void swap(int[] a, int[] b, int i, int j) {
        int temp = b[j];
        b[j] = a[i];
        a[i] = temp;
    }

    private void KSA() {
        // Initializing both states
        for (int i = 0; i < half - 1; i++) {
            S1[i] = i;
            // S2[i] = (byte) i;
        }

        for (int i = half; i < N - 1; i++) {
            S2[i - half] = i;
        }

        int j = 0;
        for (int i = 0; i < half - 1; i++) {
            j = (j + S1[i] + k1[i % k1.length]) % half;
            swap(S1, i, j);
            j = (j + S2[i] + k2[i % k2.length]) % half;
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
        int i = 0, j1 = 0, j2 = 0, t1, t2;
        for (int counter = 0; counter < plaintext.length; counter += 2) {
            i = (i + 1) & 0x7F;
            j1 = (j1 + S1[i]) & 0x7F;

            // Swap S1[i] with S2[j1]
            swap(S1, S2, i, j1);

            t1 = (S1[(S1[i] + S1[j1]) & 0x7F]) & 0x7F;
            j2 = (j2 + S2[i]) & 0x7F;

            // Swap S2[i] with S1[j2]
            swap(S2, S1, i, j2);
            t2 = (S2[(S2[i] + S2[j2]) & 0x7F]) & 0x7F;

            // Swap S1[t1] with S2[t2]
            swap(S1, S2, t1, t2);

            // XOR with plaintext and add to ciphertext
            ciphertext[counter] = (byte) (plaintext[counter] ^ t1);
            if (counter + 1 < plaintext.length) {
                ciphertext[counter + 1] = (byte) (plaintext[counter + 1] ^ t2);
            }
        }
        return ciphertext;
    }

    public byte[] decrypt(final byte[] ciphertext) {
        return encrypt(ciphertext);
    }
}