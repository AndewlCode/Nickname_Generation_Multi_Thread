import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    // Заведите в статических полях три счётчика — по одному для длин 3, 4 и 5.
    public static AtomicInteger lengthThree = new AtomicInteger(0);
    public static AtomicInteger lengthFour = new AtomicInteger(0);
    public static AtomicInteger lengthFive = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {

        // Создайте генератор текстов и сгенерируйте набор из 100 000 текстов, используя код из описания задачи.
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        // Заведите три потока — по одному на каждый критерий «красоты» слова.
        // Каждый поток проверяет все тексты на «красоту» и увеличивает счётчик нужной длины,
        // если текст соответствует критериям.
        new Thread(() -> {
            // сгенерированное слово является палиндромом,т. е. читается одинаково как слева направо, так и справа налево, например, abba
            for (String text : texts) {
                if (isPalindrome(text)) {
                    checkTextLengthAndIncrementAtomicValue(text);
                }
            }
        }).start();


        new Thread(() -> {
            // сгенерированное слово состоит из одной и той же буквы, например, aaa
            for (String text : texts) {
                if (isSameCharString(text)) {
                    checkTextLengthAndIncrementAtomicValue(text);
                }
            }
        }).start();

        new Thread(() -> {
            // буквы в слове идут по возрастанию: сначала все a (при наличии), затем все b (при наличии), затем все c и т. д.
            for (String text : texts) {
                if (isAscending(text)) {
                    checkTextLengthAndIncrementAtomicValue(text);
                }
            }
        }).start();

        printFinalResult();
    }

    // Предположим, что для реализации сервиса по подбору никнеймов вы разработали генератор случайного текста:
    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    // Функция проверяет, что сгенерированное слово является палиндромом.
    public static boolean isPalindrome(String str) {
        StringBuilder reverseStringBuilder = new StringBuilder();
        reverseStringBuilder.append(str).reverse(); // Инвертируем исходную строку
        return str.equals(reverseStringBuilder.toString());
    }


    // Функция проверяет, что сгенерированное слово состоит из одной и той же буквы.
    public static boolean isSameCharString(String str) {
        char ch = str.charAt(0);
        for (int i = 1; i < str.length(); i++) {
            if (str.charAt(i) != ch) {
                return false;
            }
        }
        return true;
    }

    // Функция проверяет, что буквы в слове идут по возрастанию.
    public static boolean isAscending(String str) {
        char ch = str.charAt(0);
        for (int i = 1; i < str.length(); i++) {
            if (ch > str.charAt(i)) {
                return false;
            } else {
                ch = str.charAt(i);
            }
        }
        return true;
    }

    // После завершения всех трёх потоков выведите сообщение вида:
    // Красивых слов с длиной 3: 100 шт
    // Красивых слов с длиной 4: 104 шт
    // Красивых слов с длиной 5: 90 шт
    public static void printFinalResult() {
        System.out.println("Красивых слов с длиной 3: " + lengthThree + " шт.");
        System.out.println("Красивых слов с длиной 4: " + lengthFour + " шт.");
        System.out.println("Красивых слов с длиной 5: " + lengthFive + " шт.");
    }

    // Функция проверяет длину текста и увеличивает соответствующую переменную.
    private static void checkTextLengthAndIncrementAtomicValue(String text) {
        switch (text.length()) {
            case 3 -> lengthThree.getAndIncrement();
            case 4 -> lengthFour.getAndIncrement();
            case 5 -> lengthFive.getAndIncrement();
        }
    }
}