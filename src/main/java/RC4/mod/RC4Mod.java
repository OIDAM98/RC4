package RC4.mod;

public class RC4Mod {
    private final int N = 256;
    private final byte[] S1 = new byte[N];
    private final byte[] S2 = new byte[N];
    private final byte[] k1;
    private final byte[] k2;

    private void swap(byte[] arr, int i, int j) {
        byte temp = arr[j];
        arr[j] = arr[i];
        arr[i] = temp;
    }

    private void KSA() {
        // TO-DO
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
        byte temp;
        for (int counter = 0; counter < plaintext.length; counter += 2) {
            i = (i + 1) & 0x7F;
            j1 = (j1 + S1[i]) & 0x7F;

            // Swap S1[i] with S2[j1]
            temp = S2[j1];
            S2[j1] = S1[i];
            S1[i] = temp;

            t1 = S1[(S1[i] + S1[j1]) & 0x7F];
            j2 = (j2 + S2[i]) & 0x7F;

            // Swap S2[i] with S1[j2]
            temp = S1[j2];
            S1[j2] = S2[i];
            S2[i] = temp;

            t2 = S2[(S2[i] + S2[j2]) & 0x7F];

            // Swap S1[t1] with S2[t2]
            temp = S2[t2];
            S2[t2] = S1[t1];
            S1[t1] = temp;

            // XOR with plaintext and add to ciphertext
            ciphertext[counter] = (byte) (plaintext[counter] ^ t1);
            ciphertext[counter + 1] = (byte) (plaintext[counter + 1] ^ t2);

        }
        return ciphertext;
    }

    public byte[] decrypt(final byte[] ciphertext) {
        return encrypt(ciphertext);
    }
}