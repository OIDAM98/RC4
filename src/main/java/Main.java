import java.nio.charset.StandardCharsets;
import RC4.mod.RC4Mod;

public class Main {
    public static void main(String... args) {
        if(args.length < 3) {
            System.out.println("Program must be called with 3 arguments:");
            System.out.println("[message] [key 1] [key 2]");
            return;
        }

        String text = args[0];
        String key1 = args[1];
        String key2 = args[2];
        RC4Mod rc = new RC4Mod(key1.getBytes(StandardCharsets.UTF_8), key2.getBytes(StandardCharsets.UTF_8));

        System.out.println("Original text:");
        System.out.println(text);
        System.out.println("Keys:");
        System.out.println("1: " + key1 + "\t" + "2: " + key2);

        System.out.println("Encryption:");
        byte[] enc = rc.encrypt(text.getBytes(StandardCharsets.UTF_8));
        System.out.println(new String(enc));

        System.out.println("Decryption");
        byte[] dec = rc.encrypt(enc);
        System.out.println(new String(dec));
    }
}