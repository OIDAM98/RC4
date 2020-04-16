import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import com.google.common.collect.Streams;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.tuple.Pair;

import RC4.RC4;
import RC4.mod.RC4Mod;

public class Main {

    private static byte[] toBytes(String str) {
        return str.getBytes(StandardCharsets.UTF_8);
    }

    private static String getAvgTime(String plaintext, int epochs, Function<byte[], Long> algorithm) {
        LongSummaryStatistics times = LongStream.range(0, epochs).map(i -> algorithm.apply(toBytes(plaintext)))
                .summaryStatistics();
        return String.format("%.4f", times.getAverage());
    }

    public static void main(String... args) {
        if (args.length > 0 && args.length < 3 && args[0].equals(":test")) {
            int numberOfEpochs = 20;
            if (args.length == 2) {
                numberOfEpochs = NumberUtils.toInt(args[1], numberOfEpochs);
            }
            String str = "Z";
            String key = "Esto es una prueba del algoritmo RC4";
            RC4 original = new RC4(toBytes(key));

            String key1 = "Soy la llave numero 1";
            String key2 = "Soy la llave numero 2";
            RC4Mod mod = new RC4Mod(toBytes(key1), toBytes(key2));

            List<Integer> sizes = Arrays.asList(10, 20, 30, 40, 50, 60, 70, 80, 90, 100);
            final int epochs = numberOfEpochs;
            System.out.println("Epochs: " + epochs);
            List<String> plaintexts = sizes.stream().map(time -> StringUtils.repeat(str, time))
                    .collect(Collectors.toList());
            List<String> encryptionsRC4 = plaintexts.stream().map(pt -> new String(original.encrypt(toBytes(pt))))
                    .collect(Collectors.toList());
            List<String> encryptionsModRC4 = plaintexts.stream().map(pt -> new String(mod.encrypt(toBytes(pt))))
                    .collect(Collectors.toList());

            List<String> timedEncryptionsRC4 = plaintexts.stream()
                    .map(pt -> getAvgTime(pt, epochs, original::timedEncryption)).collect(Collectors.toList());
            List<String> timedDecryptionsRC4 = encryptionsRC4.stream()
                    .map(pt -> getAvgTime(pt, epochs, original::timedDecryption)).collect(Collectors.toList());
            List<String> timedEncryptionsModRC4 = plaintexts.stream()
                    .map(pt -> getAvgTime(pt, epochs, mod::timedEncryption)).collect(Collectors.toList());
            List<String> timedDecryptionsModRC4 = encryptionsModRC4.stream()
                    .map(pt -> getAvgTime(pt, epochs, mod::timedDecryption)).collect(Collectors.toList());

            System.out.println("Average times for RC4 Encryptions:");
            Streams.zip(sizes.stream(), timedEncryptionsRC4.stream(), Pair::of).forEach(System.out::println);
            System.out.println("Average times for RC4 Decryptions:");
            Streams.zip(sizes.stream(), timedDecryptionsRC4.stream(), Pair::of).forEach(System.out::println);
            System.out.println("Average times for RC4 Modification Encryptions:");
            Streams.zip(sizes.stream(), timedEncryptionsModRC4.stream(), Pair::of).forEach(System.out::println);
            System.out.println("Average times for RC4 Modification Decryptions:");
            Streams.zip(sizes.stream(), timedDecryptionsModRC4.stream(), Pair::of).forEach(System.out::println);

        } else if (args.length == 2) {
            String text = args[0];
            String key = args[1];
            RC4 rc = new RC4(toBytes(key));

            System.out.println("Original text:");
            System.out.println(text);
            System.out.println("Key:");
            System.out.println(key);

            System.out.println("Encryption:");
            byte[] enc = rc.encrypt(toBytes(text));
            System.out.println(new String(enc));

            System.out.println("Decryption");
            byte[] dec = rc.encrypt(enc);
            System.out.println(new String(dec));
        } else if (args.length > 3 || args.length == 0) {
            System.out.println("Invalid arguments for the program");
            System.out.println("Program can be called these ways:");
            System.out.println("[message] [key 1] [key 2] \t- To encrypt/decrypt with Modified RC4");
            System.out.println("[message] [key] \t\t- To encrypt/decrypt with Original RC4");
            System.out.println(":test [number of iterations] \t- To test implementations");
            System.exit(0);
        } else {
            String text = args[0];
            String key1 = args[1];
            String key2 = args[2];
            RC4Mod rc = new RC4Mod(toBytes(key1), toBytes(key2));

            System.out.println("Original text:");
            System.out.println(text);
            System.out.println("Keys:");
            System.out.println("1: " + key1 + "\t" + "2: " + key2);

            System.out.println("Encryption:");
            byte[] enc = rc.encrypt(toBytes(text));
            System.out.println(new String(enc));

            System.out.println("Decryption");
            byte[] dec = rc.encrypt(enc);
            System.out.println(new String(dec));
        }
    }
}