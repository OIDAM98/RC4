import java.nio.charset.StandardCharsets;
import RC4.RC4;

public class Main {
    public static void main(String... strings) {
        String text = "Oscar";
        String key = "dulio";
        RC4 rc = new RC4(key.getBytes(StandardCharsets.UTF_8));

        System.out.println("Original text:");
        System.out.println(text);

        System.out.println("Encryption:");
        byte[] enc = rc.encrypt(text.getBytes(StandardCharsets.UTF_8));
        System.out.println(new String(enc));

        System.out.println("Decryption");
        byte[] dec = rc.encrypt(enc);
        System.out.println(new String(dec));
    }
}