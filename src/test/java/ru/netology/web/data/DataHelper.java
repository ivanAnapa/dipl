package ru.netology.web.data;

import lombok.Value;
import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class DataHelper {

    private DataHelper() {
    }

    private static final String approvedCardNumber = "4444 4444 4444 4441";
    private static final String declinedCardNumber = "4444 4444 4444 4442";
    private static final String cardYearValid = "25";
    private static final String cardYearInvalid = "21";
    private static final String cardCVC = "999";


    public static String getApprovedCardNumber() {
        return approvedCardNumber;
    }
    public static String getDeclinedCardNumber() {
        return declinedCardNumber;
    }
    public static String getCardMonthValid() {
        // Что лучше оставить? LocalDate или Random?
        return LocalDate.now().format(DateTimeFormatter.ofPattern("MM"));

        /*int cardMonthValid = (int) ThreadLocalRandom.current().nextDouble(1, 12);
        if (cardMonthValid > 9) {
            return String.valueOf(cardMonthValid);
        } else {
            return "0" + cardMonthValid;
        }*/
    }

    public static String getCardMonthInvalidLarge() {
        int cardMonthInvalid = (int) ThreadLocalRandom.current().nextDouble(13, 99);
        return String.valueOf(cardMonthInvalid);
    }
    public static String getCardMonthInvalidSmall() {
        int cardMonthInvalid = (int) ThreadLocalRandom.current().nextDouble(0, 9);
        return String.valueOf(cardMonthInvalid);
    }
    public static String getCardYearValid() {
        return cardYearValid;
    }
    public static String getCardYearInvalid() {
        return cardYearInvalid;
    }
    public static String getCardCVC() {return cardCVC;}


    public static String generateName() {
        Faker faker = new Faker();
        return faker.name().firstName() + " " + faker.name().lastName();
    }
}