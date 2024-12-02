package ru.netology.data;

import com.github.javafaker.CreditCardType;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.junit.jupiter.api.MethodOrderer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class DataHelper {
    private static final Faker faker = new Faker(new Locale("en"));
    private static final Faker fakerRus = new Faker(new Locale("ru"));

    private DataHelper() {
    }

    @Value
    @RequiredArgsConstructor
    public static class CardInfo {
        String cardNumber;
        String month;
        String year;
        String holder;
        String cvc;
    }

    public static String getApprovedCardNumber() {
        return ("1111 2222 3333 4444");
    }

    public static String getDeclinedCardNumber() {
        return ("5555 6666 7777 8888");
    }

    public static String getRandomCardNumber() {
        return faker.finance().creditCard(CreditCardType.VISA);
    }

    public static String getCardNumberWith13Numbers() {
        return ("1111 2222 3333 4");
    }

    public static String getCardNumberWithCyrillic() {
        return ("карта");
    }

    public static String getCardNumberWithLatin() {
        return ("card");
    }

    public static String getCardNumberWithSymbols() {
        return ("№?*:%;$!?");
    }

    public static String getMonth() {
        String[] month = new String[]{
                "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"
        };
        Random random = new Random();
        return month[random.nextInt(month.length)];
    }

    public static String getMonthWithZero() {
        return ("00");
    }

    public static String getMonthOverTwelve() {
        return ("14");
    }
    public static String getLastMonth() {
        return ("10");
    }

    public static String getMonthWithLatinAndCyrillic() {
        return ("mayмай");
    }

    public static String getMonthWithSymbols() {
        return ("$#");
    }

    public static String getMonthWithOneNumber() {
        return ("4");
    }

    public static String getSpecifiedMonth(int shift) {
        return LocalDate.now().plusMonths(shift).format(DateTimeFormatter.ofPattern("MM"));
    }

    public static String getYear(int shift) {
        return LocalDate.now().plusYears(shift).format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String getLastYear() {
        return ("20");
    }
    public static String getCurrentYear() {
        return ("24");
    }
    public static String getYearWithTwoZero() {
        return ("00");
    }

    public static String getYearWith1Number() {
        return ("3");
    }

    public static String getYearWithCyrillicAndLatin() {
        return ("gп");
    }

    public static String getYearWithSymbols() {
        return ("%*");
    }
    public static String getYearNotExist() {
        return ("30");
    }

    public static String getNameHolder() {
        return faker.name().firstName() + " " + faker.name().lastName();
    }

    public static String getHolderFirstNameWithDash() {
        return faker.name().firstName() + "-" + faker.name().firstName() + " " + faker.name().lastName();
    }

    public static String getHolderNameWithIotatedLetters() {
        return faker.name().firstName() + "Артём" + faker.name().lastName() + "Йосипчук" + faker.name().lastName();
    }
    public static String getHolderWithAnApostropheInLastName() {
        return faker.name().firstName() + "Жанна" + faker.name().lastName() + "д'Арк" + faker.name().lastName();
    }
    public static String getHolderNameWithDash() {
        return faker.name().firstName() + "-" + faker.name().firstName() + " " + faker.name().lastName() + "-" + faker.name().lastName();
    }

    public static String getHolderOnlyFirstName() {
        return faker.name().firstName();
    }

    public static String getHolderNameWithLatinAndCyrillic() {
        return faker.name().firstName() + " " + fakerRus.name().lastName();
    }
    public static String getHolderNameWithLat() {
        return faker.name().firstName() + " " + faker.name().lastName();
    }

    public static String getHolderNameWithSymbols() {
        return ("!@#$%^&*()_+~");
    }

    public static String getHolderNameWithNumbers() {
        return ("1234567890");
    }

    public static String getHolderFirstNameWithSpace() {
        return faker.name().firstName() + " " + faker.name().firstName() + " " + faker.name().lastName();
    }
    public static String getHolderFirstNameWithoutSpace() {
        return faker.name().firstName() + faker.name().lastName();
    }

    public static String getCvc() {
        String[] cvc = new String[]{
                "001", "012","123", "321","456", "654", "678", "876", "890", "098", "987", "876", "765", "654", "543",
                "432", "111", "222", "333", "444", "555", "666", "777", "888", "999", "342", "583", "935", "178", "308",
                "925", "177", "834", "035", "273", "159", "482", "669", "469", "909", "711", "455", "121", "299", "525"
        };
        Random random = new Random();
        return cvc[random.nextInt(cvc.length)];
    }

    public static String getCvcWithOneNumber() {
        return ("1");
    }
    public static String getCvcWithTwoNumbers() {
        return ("12");
    }

    public static String getCvcWithSymbols() {
        return ("№$@");
    }

    public static String getCvcWithCyrillicAndLatin() {
        return ("аfп");
    }
}
