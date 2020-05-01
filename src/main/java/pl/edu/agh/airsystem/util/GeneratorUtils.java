package pl.edu.agh.airsystem.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneratorUtils {

    public static String generateString() {
        Random r = new Random();
        int len = RandomUtils.randomBetween(4, 12);
        List<Character> consonants = List.of('b', 'c', 'd', 'f', 'g', 'h', 'j',
                'k', 'l', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'z');
        List<Character> vowels = List.of('a', 'e', 'i', 'o', 'u', 'y');
        List<Character> all = new ArrayList<>(consonants);
        all.addAll(vowels);

        StringBuilder stringBuilder = new StringBuilder();

        char lastChar = all.get(r.nextInt(all.size()));
        for (int i = 0; i < len; i++) {
            if(i == 0) {
                stringBuilder.append(("" + all.get(r.nextInt(all.size()))).toUpperCase().charAt(0));
            } else {
                stringBuilder.append(lastChar);
            }
            if (vowels.contains(lastChar)) {
                lastChar = consonants.get(r.nextInt(consonants.size()));
            } else {
                lastChar = vowels.get(r.nextInt(vowels.size()));
            }
        }

        return stringBuilder.toString();
    }

}
