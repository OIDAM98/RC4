import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.collect.Streams;

import RC4.RC4;
import RC4.mod.RC4Mod;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.tuple.Pair;

public class Main {

    private static byte[] toBytes(String str) {
        return str.getBytes(StandardCharsets.UTF_8);
    }

    private static long getAvgTime(String plaintext, int epochs, Function<byte[], Long> algorithm) {
        List<Long> times = new ArrayList<>();
        for (int i = 0; i < epochs; i++) {
            times.add(algorithm.apply(toBytes(plaintext)));
        }
        long sum = times.stream().reduce(0L, Long::sum);
        return sum / times.size();
    }

    public static void main(String... args) {
        if (args.length > 0 && args.length < 3 && args[0].equals("test")) {
            int numberOfEpochs = 20;
            if (args.length == 2) {
                numberOfEpochs = NumberUtils.toInt(args[1], numberOfEpochs);
            }
            String str = "a";
            String key = "Esto es una prueba del algoritmo RC4";
            RC4 original = new RC4(toBytes(key));

            String key1 = "Soy la llave numero 1";
            String key2 = "Soy la llave numero 2";
            RC4Mod mod = new RC4Mod(toBytes(key1), toBytes(key2));

            List<Integer> sizes = Arrays.asList(10, 20, 30, 40, 50, 60, 70, 80, 90, 100);
            final int epochs = numberOfEpochs;
            System.out.println("Epochs: " + epochs);
            List<String> plaintexts = sizes.parallelStream().map(time -> StringUtils.repeat(str, time))
                    .collect(Collectors.toList());
            List<String> encryptionsRC4 = plaintexts.parallelStream()
                    .map(pt -> new String(original.encrypt(toBytes(pt)))).collect(Collectors.toList());
            List<String> encryptionsModRC4 = plaintexts.parallelStream().map(pt -> new String(mod.encrypt(toBytes(pt))))
                    .collect(Collectors.toList());

            List<Long> timedEncryptionsRC4 = plaintexts.parallelStream()
                    .map(pt -> getAvgTime(pt, epochs, original::timedEncryption)).collect(Collectors.toList());
            List<Long> timedDecryptionsRC4 = encryptionsRC4.parallelStream()
                    .map(pt -> getAvgTime(pt, epochs, original::timedDecryption)).collect(Collectors.toList());
            List<Long> timedEncryptionsModRC4 = plaintexts.parallelStream()
                    .map(pt -> getAvgTime(pt, epochs, mod::timedEncryption)).collect(Collectors.toList());
            List<Long> timedDecryptionsModRC4 = encryptionsModRC4.parallelStream()
                    .map(pt -> getAvgTime(pt, epochs, mod::timedDecryption)).collect(Collectors.toList());

            System.out.println("Average times for RC4 Encryptions:");
            Streams.zip(sizes.stream(), timedEncryptionsRC4.stream(), Pair::of).forEach(System.out::println);
            System.out.println("Average times for RC4 Decryptions:");
            Streams.zip(sizes.stream(), timedDecryptionsRC4.stream(), Pair::of).forEach(System.out::println);
            System.out.println("Average times for RC4 Modification Encryptions:");
            Streams.zip(sizes.stream(), timedEncryptionsModRC4.stream(), Pair::of).forEach(System.out::println);
            System.out.println("Average times for RC4 Modification Decryptions:");
            Streams.zip(sizes.stream(), timedDecryptionsModRC4.stream(), Pair::of).forEach(System.out::println);

        } else if (args.length < 3) {
            System.out.println("Program must be called with 3 arguments:");
            System.out.println("[message] [key 1] [key 2]");
            System.exit(1);
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