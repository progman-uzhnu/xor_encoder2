package org.example.logical;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

// some shitcode 
public class LMain {

    public static void main(String[] args) throws IOException {
        File file = new File("key.txt");

        if (!file.exists()) {
            try (FileWriter fileWriter = new FileWriter(file)) {
                fileWriter.write(generateKey());
            }
        }

        String keyString;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            keyString = bufferedReader.lines().collect(Collectors.joining());
        }

        String message = new Scanner(System.in).nextLine();

        byte[] byteKey = keyString.getBytes(StandardCharsets.UTF_8);
        byte[] messageArr = message.getBytes(StandardCharsets.UTF_8);

        boolean decodeFunc = new Scanner(System.in).nextLine().equals("y");
        
        for (int i = 0, j = 0; i < messageArr.length && j < byteKey.length; i++) {
            if (byteKey.length - j == 1) j = 0;
            if (decodeFunc && (char) messageArr[i] == '$') {
                messageArr[i] = (byte) '\u007F';
            }

            messageArr[i] = (byte) (messageArr[i] ^ (byteKey[j] / keyString.length()));

            if ((char) messageArr[i] == '\u007F' && !decodeFunc) {
                messageArr[i] = (byte) '$';
            }
        }

        System.out.println(new String(messageArr, StandardCharsets.UTF_8));

    }

    static String generateKey() {
        StringBuilder builder = new StringBuilder();
        String al = "qwertyuiopasdfghjkl;zxcvbnm,<?>@@#$%^&*(1234567890)}{:|'";

        IntStream.range(1, 20)
                .forEach(sym -> builder.append(al.charAt(
                        new Random().nextInt(al.length()))));

        return builder.toString();
    }

}
