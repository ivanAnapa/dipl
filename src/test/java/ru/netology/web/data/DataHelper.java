package ru.netology.web.data;

import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public class DataHelper {

    private DataHelper() {
    }

    private static final String approvedCardNumber = "4444 4444 4444 4441";
    private static final String declinedCardNumber = "4444 4444 4444 4442";
    private static final String invalidNameWithSpecChar = "!@#$%^&*()";
    private static final String[] tripDetailsDefault =
            {
                    "Сказочный Восток",
                    "33 360 миль на карту",
                    "До 7% на остаток по счёту",
                    "Всего 45 000 руб.!"
            };

    public static String getApprovedCardNumber() {
        return approvedCardNumber;
    }

    public static String getDeclinedCardNumber() {
        return declinedCardNumber;
    }

    public static String[] getTripDetailsDefault() {
        return tripDetailsDefault;
    }

    // ************************************ Получение месяца ************************************
    // Валидный текущий месяц
    public static String getCurrentMonth() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("MM"));
    }

    // Валидный формат: предыдущий месяц
    public static String getCurrentMonthMinusOne() {
        return LocalDate.now().minusMonths(1).format(DateTimeFormatter.ofPattern("MM"));
    }

    // Невалидный месяц из 2 цифр (>12)
    public static String getCardMonthInvalidLarge() {
        int cardMonthInvalid = (int) ThreadLocalRandom.current().nextDouble(13, 99);
        return String.valueOf(cardMonthInvalid);
    }

    // Невалидный месяц из 1 цифры
    public static String getCardMonthInvalidSmall() {
        int cardMonthInvalid = (int) ThreadLocalRandom.current().nextDouble(0, 9);
        return String.valueOf(cardMonthInvalid);
    }

    // ************************************ Получение CVC ************************************
    // Валидный CVC из 3 цифр
    public static String getCardCvcValid() {
        int cvcValid = (int) ThreadLocalRandom.current().nextDouble(100, 999);
        return String.valueOf(cvcValid);
    }

    // Невалидный CVC из 1 или 2 цифр
    public static String getCardCvcInvalid() {
        int cvcInvalid = (int) ThreadLocalRandom.current().nextDouble(0, 99);
        return String.valueOf(cvcInvalid);
    }


    // ************************************ Получение имени ************************************
    // Валидное имя на латинице
    public static String generateName() {
        Faker faker = new Faker();
        return faker.name().firstName() + " " + faker.name().lastName();
    }

    // Невалидное имя на латинице (без пробела)
    public static String generateNameWithoutSpace() {
        Faker faker = new Faker();
        return (faker.name().firstName() + faker.name().lastName())
                .replaceAll(" ", "");
    }

    // Невалидное имя на кириллице
    public static String generateNameRus() {
        Faker faker = new Faker(new Locale("ru"));
        return faker.name().firstName() + " " + faker.name().lastName();
    }

    // Невалидное имя из спецсимволов
    public static String getNameWithSpecChars() {
        return invalidNameWithSpecChar;
    }

    // ************************************ Получение года ************************************
    // Получить текущий год, оставить 2 последние цифры
    public static String getCurrentYear() {
        return LocalDate.now()
                .format(DateTimeFormatter.ofPattern("yyyy"))
                .substring(2, 4);
    }

    // Уменьшить текущий год на "minus" лет
    public static String getCurrYearMinus(int minus) {
        int currYear = Integer.parseInt(getCurrentYear());
        return String.valueOf(currYear - minus);
    }

    // Увеличить текущий год на "plus" лет
    public static String getCurrYearPlus(int plus) {
        int currYear = Integer.parseInt(getCurrentYear());
        return String.valueOf(currYear + plus);
    }


}