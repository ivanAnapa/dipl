package ru.netology.web.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.web.data.common.DataHelper;
import ru.netology.web.data.db.DbConnectionHelper;
import ru.netology.web.page.BasePage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.netology.web.data.common.DataHelper.getCurrentDateInDbFormat;
import static ru.netology.web.data.db.DbConnectionHelper.*;

class BuyTripTest {


    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setUpSutUrl() {
        open(System.getProperty("sut.url"));
    }

    @AfterEach
    void cleanDataBases() {
        DbConnectionHelper.deletePrevOrders();
    }

    @Test
    void checkDefaultPage() {
        // 1. Проверить наличие текстов до клика по кнопкам "Купить" или "Купить в кредит"
        BasePage basePage = new BasePage();
        basePage.validateDefaultPage();
    }

    @Test
    void buy_checkElements() {
        // 2. "Купить": Проверка видимости заголовка и полей ввода
        BasePage basePage = new BasePage();
        basePage
                .clickBuyBtn()
                .validateFieldsForBuy();
    }

    @Test
    void buyWithCredit_checkElements() {
        // 3. "Купить в кредит": Проверка видимости заголовка и полей ввода
        BasePage basePage = new BasePage();
        basePage
                .clickBuyWithCreditBtn()
                .validateFieldsForBuyWithCredit();
    }

    @Test
    void buy_validData_approvedCard() {
        // 4. "Купить" тур с одобренной картой и валидными данными
        String approvedCardNumber = DataHelper.getApprovedCardNumber();
        String cardMonthValid = DataHelper.getCurrentMonth();
        String cardYearValid = DataHelper.getCurrYearPlus(1);
        String cardOwner = DataHelper.generateName();
        String cardCvc = DataHelper.getCardCvcValid();

        BasePage basePage = new BasePage();
        basePage
                .clickBuyBtn()
                .fillCardInfo(approvedCardNumber, cardMonthValid, cardYearValid, cardOwner, cardCvc)
                .clickContinueBtn()
                .checkSuccessResult();

        // ****************** Проверки в БД - payment_entity ******************
        // Проверить статус в БД
        assertEquals("APPROVED", getStatusFromPaymentEntity());
        // Проверить стоимость в БД (насколько я понял, стоимость в БД указана в копейках?)
        assertEquals("4500000", getAmountFromPaymentEntity());
        // Проверить, что дата покупки совпадает с текущей (гггг-ММ-дд)
        assertEquals(getCurrentDateInDbFormat(), getPurchaseDateFromPaymentEntity());

        // ****************** Проверки в БД - order_entity ******************
        // Проверить, что дата покупки совпадает с текущей (гггг-ММ-дд)
        assertEquals(getCurrentDateInDbFormat(), getPurchaseDateFromOrderEntity());
    }

    @Test
    void buyWithCredit_validData_approvedCard() {
        // 5. "Купить в кредит" тур с одобренной картой и валидными данными
        String approvedCardNumber = DataHelper.getApprovedCardNumber();
        String cardMonthValid = DataHelper.getCurrentMonth();
        String cardYearValid = DataHelper.getCurrYearPlus(1);
        String cardOwner = DataHelper.generateName();
        String cardCvc = DataHelper.getCardCvcValid();

        BasePage basePage = new BasePage();
        basePage
                .clickBuyWithCreditBtn()
                .fillCardInfo(approvedCardNumber, cardMonthValid, cardYearValid, cardOwner, cardCvc)
                .clickContinueBtn()
                .checkSuccessResult();

        // ****************** Проверки в БД - credit_request_entity ******************
        // Проверить статус в БД
        assertEquals("APPROVED", getStatusFromCreditRequestEntity());
        // Проверить, что дата покупки совпадает с текущей (гггг-ММ-дд)
        assertEquals(getCurrentDateInDbFormat(), getPurchaseDateFromCreditRequestEntity());
        // Проверить ИД банка в БД (тут какой-то хеш, придумал проверку только на NotNull)
        assertNotNull(getBankIdFromCreditRequestEntity());

        // ****************** Проверки в БД - order_entity ******************
        // Проверить, что дата покупки совпадает с текущей (гггг-ММ-дд)
        assertEquals(getCurrentDateInDbFormat(), getPurchaseDateFromOrderEntity());

    }

    @Test
    void buy_validData_declinedCard() {
        // 6. "Купить" тур с отклоненной картой и валидными данными
        String declinedCardNumber = DataHelper.getDeclinedCardNumber();
        String cardMonthValid = DataHelper.getCurrentMonth();
        String cardYearValid = DataHelper.getCurrYearPlus(2);
        String cardOwner = DataHelper.generateName();
        String cardCvc = DataHelper.getCardCvcValid();

        BasePage basePage = new BasePage();
        basePage
                .clickBuyBtn()
                .fillCardInfo(declinedCardNumber, cardMonthValid, cardYearValid, cardOwner, cardCvc)
                .clickContinueBtn()
                .checkDeclinedResult();
    }

    @Test
    void buyWithCredit_validData_declinedCard() {
        // 7. "Купить в кредит" тур с отклоненной картой и валидными данными
        String declinedCardNumber = DataHelper.getDeclinedCardNumber();
        String cardMonthValid = DataHelper.getCurrentMonth();
        String cardYearValid = DataHelper.getCurrYearPlus(2);
        String cardOwner = DataHelper.generateName();
        String cardCvc = DataHelper.getCardCvcValid();

        BasePage basePage = new BasePage();
        basePage
                .clickBuyWithCreditBtn()
                .fillCardInfo(declinedCardNumber, cardMonthValid, cardYearValid, cardOwner, cardCvc)
                .clickContinueBtn()
                .checkDeclinedResult();
    }

    @Test
    void buy_checkCardNotification_shortValue() {
        // 8. "Купить": Проверка ошибки валидации при неполном вводе номера карты (15 цифр)
        String shortCardNumber = DataHelper.getApprovedCardNumber().substring(1, 15);
        String cardMonthValid = DataHelper.getCurrentMonth();
        String cardYearValid = DataHelper.getCurrYearPlus(2);
        String cardOwner = DataHelper.generateName();
        String cardCvc = DataHelper.getCardCvcValid();

        BasePage basePage = new BasePage();
        basePage
                .clickBuyBtn()
                .fillCardInfo(shortCardNumber, cardMonthValid, cardYearValid, cardOwner, cardCvc)
                .clickContinueBtn()
                .checkWrongCardNotification();
    }

    @Test
    void buyWithCredit_checkCardNotification_shortValue() {
        // 9. "Купить в кредит": Проверка ошибки валидации при неполном вводе номера карты (15 цифр)
        String shortCardNumber = DataHelper.getDeclinedCardNumber().substring(1, 15);
        String cardMonthValid = DataHelper.getCurrentMonth();
        String cardYearValid = DataHelper.getCurrYearPlus(2);
        String cardOwner = DataHelper.generateName();
        String cardCvc = DataHelper.getCardCvcValid();

        BasePage basePage = new BasePage();
        basePage
                .clickBuyWithCreditBtn()
                .fillCardInfo(shortCardNumber, cardMonthValid, cardYearValid, cardOwner, cardCvc)
                .clickContinueBtn()
                .checkWrongCardNotification();
    }

    @Test
    void buy_checkCardNotification_unfilledValue() {
        // 10. "Купить": Проверка ошибки валидации при незаполнении номера карты
        String cardMonthValid = DataHelper.getCurrentMonth();
        String cardYearValid = DataHelper.getCurrYearPlus(2);
        String cardOwner = DataHelper.generateName();
        String cardCvc = DataHelper.getCardCvcValid();

        BasePage basePage = new BasePage();
        basePage
                .clickBuyBtn()
                .fillCardInfo("", cardMonthValid, cardYearValid, cardOwner, cardCvc)
                .clickContinueBtn()
                .checkUnfilledCardNotification();
    }

    @Test
    void buyWithCredit_checkCardNotification_unfilledValue() {
        // 11. "Купить в кредит": Проверка ошибки валидации при незаполнении номера карты
        String cardMonthValid = DataHelper.getCurrentMonth();
        String cardYearValid = DataHelper.getCurrYearPlus(2);
        String cardOwner = DataHelper.generateName();
        String cardCvc = DataHelper.getCardCvcValid();

        BasePage basePage = new BasePage();
        basePage
                .clickBuyWithCreditBtn()
                .fillCardInfo("", cardMonthValid, cardYearValid, cardOwner, cardCvc)
                .clickContinueBtn()
                .checkUnfilledCardNotification();
    }

    @Test
    void buy_checkInvalidMonthNotification_value00() {
        // 12. "Купить": Проверка ошибки валидации при вводе месяца "00"
        String approvedCardNumber = DataHelper.getApprovedCardNumber();
        String cardYearValid = DataHelper.getCurrYearPlus(2);
        String cardOwner = DataHelper.generateName();
        String cardCvc = DataHelper.getCardCvcValid();

        BasePage basePage = new BasePage();
        basePage
                .clickBuyBtn()
                .fillCardInfo(approvedCardNumber, "00", cardYearValid, cardOwner, cardCvc)
                .clickContinueBtn()
                .checkWrongMonthNotification();
    }

    @Test
    void buyWithCredit_checkInvalidMonthNotification_value00() {
        // 13. "Купить в кредит": Проверка ошибки валидации при вводе месяца "00"
        String approvedCardNumber = DataHelper.getApprovedCardNumber();
        String cardYearValid = DataHelper.getCurrYearPlus(2);
        String cardOwner = DataHelper.generateName();
        String cardCvc = DataHelper.getCardCvcValid();

        BasePage basePage = new BasePage();
        basePage
                .clickBuyWithCreditBtn()
                .fillCardInfo(approvedCardNumber, "00", cardYearValid, cardOwner, cardCvc)
                .clickContinueBtn()
                .checkWrongMonthNotification();
    }

    @Test
    void buy_checkInvalidMonthNotification_valueMoreThen12() {
        // 14. "Купить": Проверка ошибки валидации при вводе месяца ">12"
        String approvedCardNumber = DataHelper.getApprovedCardNumber();
        String cardMonthInvalid = DataHelper.getCardMonthInvalidLarge();
        String cardYearValid = DataHelper.getCurrYearPlus(2);
        String cardOwner = DataHelper.generateName();
        String cardCvc = DataHelper.getCardCvcValid();

        BasePage basePage = new BasePage();
        basePage
                .clickBuyBtn()
                .fillCardInfo(approvedCardNumber, cardMonthInvalid, cardYearValid, cardOwner, cardCvc)
                .clickContinueBtn()
                .checkWrongMonthNotification();
    }

    @Test
    void buyWithCredit_checkInvalidMonthNotification_valueMoreThen12() {
        // 15. "Купить в кредит": Проверка ошибки валидации при вводе месяца ">12"
        String approvedCardNumber = DataHelper.getApprovedCardNumber();
        String cardMonthInvalid = DataHelper.getCardMonthInvalidLarge();
        String cardYearValid = DataHelper.getCurrYearPlus(2);
        String cardOwner = DataHelper.generateName();
        String cardCvc = DataHelper.getCardCvcValid();

        BasePage basePage = new BasePage();
        basePage
                .clickBuyWithCreditBtn()
                .fillCardInfo(approvedCardNumber, cardMonthInvalid, cardYearValid, cardOwner, cardCvc)
                .clickContinueBtn()
                .checkWrongMonthNotification();
    }

    @Test
    void buy_checkInvalidMonthNotification_valueOneChar() {
        // 16. "Купить": Проверка ошибки валидации при вводе месяца "1 цифра"
        String approvedCardNumber = DataHelper.getApprovedCardNumber();
        String cardMonthInvalid = DataHelper.getCardMonthInvalidSmall();
        String cardYearValid = DataHelper.getCurrYearPlus(2);
        String cardOwner = DataHelper.generateName();
        String cardCvc = DataHelper.getCardCvcValid();

        BasePage basePage = new BasePage();
        basePage
                .clickBuyBtn()
                .fillCardInfo(approvedCardNumber, cardMonthInvalid, cardYearValid, cardOwner, cardCvc)
                .clickContinueBtn()
                .checkWrongFormatMonthNotification();
    }

    @Test
    void buyWithCredit_checkInvalidMonthNotification_valueOneChar() {
        // 17. "Купить в кредит": Проверка ошибки валидации при вводе месяца "1 цифра"
        String approvedCardNumber = DataHelper.getApprovedCardNumber();
        String cardMonthInvalid = DataHelper.getCardMonthInvalidSmall();
        String cardYearValid = DataHelper.getCurrYearPlus(2);
        String cardOwner = DataHelper.generateName();
        String cardCvc = DataHelper.getCardCvcValid();

        BasePage basePage = new BasePage();
        basePage
                .clickBuyWithCreditBtn()
                .fillCardInfo(approvedCardNumber, cardMonthInvalid, cardYearValid, cardOwner, cardCvc)
                .clickContinueBtn()
                .checkWrongFormatMonthNotification();
    }

    @Test
    void buy_checkInvalidYearNotification_currentYearPlus6() {
        // 18. "Купить": Проверка ошибки валидации при вводе года "текущий + 6 лет"
        String approvedCardNumber = DataHelper.getApprovedCardNumber();
        String cardMonth = DataHelper.getCurrentMonth();
        String cardYearValid = DataHelper.getCurrYearPlus(6);
        String cardOwner = DataHelper.generateName();
        String cardCvc = DataHelper.getCardCvcValid();

        BasePage basePage = new BasePage();
        basePage
                .clickBuyBtn()
                .fillCardInfo(approvedCardNumber, cardMonth, cardYearValid, cardOwner, cardCvc)
                .clickContinueBtn()
                .checkWrongYearNotification();
    }

    @Test
    void buyWithCredit_checkInvalidYearNotification_currentYearPlus6() {
        // 19. "Купить в кредит": Проверка ошибки валидации при вводе года "текущий + 6 лет"
        String approvedCardNumber = DataHelper.getApprovedCardNumber();
        String cardMonth = DataHelper.getCurrentMonth();
        String cardYearValid = DataHelper.getCurrYearPlus(6);
        String cardOwner = DataHelper.generateName();
        String cardCvc = DataHelper.getCardCvcValid();

        BasePage basePage = new BasePage();
        basePage
                .clickBuyWithCreditBtn()
                .fillCardInfo(approvedCardNumber, cardMonth, cardYearValid, cardOwner, cardCvc)
                .clickContinueBtn()
                .checkWrongYearNotification();
    }

    @Test
    void buy_checkInvalidYearNotification_currentYearMinus1() {
        // 20. "Купить": Проверка ошибки валидации при вводе года "текущий - 1 год"
        String approvedCardNumber = DataHelper.getApprovedCardNumber();
        String cardMonth = DataHelper.getCurrentMonth();
        String cardYear = DataHelper.getCurrYearMinus(1);
        String cardOwner = DataHelper.generateName();
        String cardCvc = DataHelper.getCardCvcValid();

        BasePage basePage = new BasePage();
        basePage
                .clickBuyBtn()
                .fillCardInfo(approvedCardNumber, cardMonth, cardYear, cardOwner, cardCvc)
                .clickContinueBtn()
                .checkWrongYearNotificationOld();
    }

    @Test
    void buyWithCredit_checkInvalidYearNotification_currentYearMinus1() {
        // 21. "Купить в кредит": Проверка ошибки валидации при вводе года "текущий - 1 год"
        String approvedCardNumber = DataHelper.getApprovedCardNumber();
        String cardMonth = DataHelper.getCurrentMonth();
        String cardYear = DataHelper.getCurrYearMinus(1);
        String cardOwner = DataHelper.generateName();
        String cardCvc = DataHelper.getCardCvcValid();

        BasePage basePage = new BasePage();
        basePage
                .clickBuyWithCreditBtn()
                .fillCardInfo(approvedCardNumber, cardMonth, cardYear, cardOwner, cardCvc)
                .clickContinueBtn()
                .checkWrongYearNotificationOld();
    }

    @Test
    void buy_checkInvalidMonthNotification_currentMonthMinus1() {
        // 22. "Купить": Проверка ошибки валидации при вводе прошлого месяца текущего года
        String approvedCardNumber = DataHelper.getApprovedCardNumber();
        String prevMonth = DataHelper.getCurrentMonthMinusOne();
        String cardOwner = DataHelper.generateName();
        String cardCvc = DataHelper.getCardCvcValid();
        String cardYear;
        // Если предыдущий месяц = декабрь, то уменьшить год на 1. В остальных случаях: оставить текущий год
        if (prevMonth.equals("12")) {
            cardYear = DataHelper.getCurrYearMinus(1);
        } else {
            cardYear = DataHelper.getCurrentYear();
        }

        BasePage basePage = new BasePage();
        basePage
                .clickBuyBtn()
                .fillCardInfo(approvedCardNumber, prevMonth, cardYear, cardOwner, cardCvc)
                .clickContinueBtn()
                .checkWrongMonthNotification();
    }

    @Test
    void buyWithCredit_checkInvalidMonthNotification_currentMonthMinus1() {
        // 23. "Купить в кредит": Проверка ошибки валидации при вводе прошлого месяца текущего года
        String approvedCardNumber = DataHelper.getApprovedCardNumber();
        String prevMonth = DataHelper.getCurrentMonthMinusOne();
        String cardOwner = DataHelper.generateName();
        String cardCvc = DataHelper.getCardCvcValid();
        String cardYear;
        // Если предыдущий месяц = декабрь, то уменьшить год на 1. В остальных случаях: оставить текущий год
        if (prevMonth.equals("12")) {
            cardYear = DataHelper.getCurrYearMinus(1);
        } else {
            cardYear = DataHelper.getCurrentYear();
        }

        BasePage basePage = new BasePage();
        basePage
                .clickBuyWithCreditBtn()
                .fillCardInfo(approvedCardNumber, prevMonth, cardYear, cardOwner, cardCvc)
                .clickContinueBtn()
                .checkWrongMonthNotification();
    }

    @Test
    void buy_checkInvalidMonthNotification_unfilledMonth() {
        // 24. "Купить": Проверка ошибки валидации при незаполнениии месяца
        String approvedCardNumber = DataHelper.getApprovedCardNumber();
        String cardYear = DataHelper.getCurrentYear();
        String cardOwner = DataHelper.generateName();
        String cardCvc = DataHelper.getCardCvcValid();

        BasePage basePage = new BasePage();
        basePage
                .clickBuyBtn()
                .fillCardInfo(approvedCardNumber, "", cardYear, cardOwner, cardCvc)
                .clickContinueBtn()
                .checkUnfilledMonthNotification();
    }

    @Test
    void buyWithCredit_checkInvalidMonthNotification_unfilledMonth() {
        // 25. "Купить в кредит": Проверка ошибки валидации при незаполнениии месяца
        String approvedCardNumber = DataHelper.getApprovedCardNumber();
        String cardYear = DataHelper.getCurrentYear();
        String cardOwner = DataHelper.generateName();
        String cardCvc = DataHelper.getCardCvcValid();

        BasePage basePage = new BasePage();
        basePage
                .clickBuyWithCreditBtn()
                .fillCardInfo(approvedCardNumber, "", cardYear, cardOwner, cardCvc)
                .clickContinueBtn()
                .checkUnfilledMonthNotification();
    }

    @Test
    void buy_checkInvalidMonthNotification_unfilledYear() {
        // 26. "Купить": Проверка ошибки валидации при незаполнениии года
        String approvedCardNumber = DataHelper.getApprovedCardNumber();
        String cardMonth = DataHelper.getCurrentMonth();
        String cardOwner = DataHelper.generateName();
        String cardCvc = DataHelper.getCardCvcValid();

        BasePage basePage = new BasePage();
        basePage
                .clickBuyBtn()
                .fillCardInfo(approvedCardNumber, cardMonth, "", cardOwner, cardCvc)
                .clickContinueBtn()
                .checkUnfilledYearNotification();
    }

    @Test
    void buyWithCredit_checkInvalidMonthNotification_unfilledYear() {
        // 27. "Купить в кредит": Проверка ошибки валидации при незаполнениии года
        String approvedCardNumber = DataHelper.getApprovedCardNumber();
        String cardMonth = DataHelper.getCurrentMonth();
        String cardOwner = DataHelper.generateName();
        String cardCvc = DataHelper.getCardCvcValid();

        BasePage basePage = new BasePage();
        basePage
                .clickBuyWithCreditBtn()
                .fillCardInfo(approvedCardNumber, cardMonth, "", cardOwner, cardCvc)
                .clickContinueBtn()
                .checkUnfilledYearNotification();
    }

    @Test
    void buy_checkInvalidNameNotification_rusChars() {
        // 28. "Купить": Проверка ошибки валидации при вводе имени владельца крты на кириллице
        String approvedCardNumber = DataHelper.getApprovedCardNumber();
        String cardMonthValid = DataHelper.getCurrentMonth();
        String cardYearValid = DataHelper.getCurrYearPlus(1);
        String cardOwner = DataHelper.generateNameRus();
        String cardCvc = DataHelper.getCardCvcValid();

        BasePage basePage = new BasePage();
        basePage
                .clickBuyBtn()
                .fillCardInfo(approvedCardNumber, cardMonthValid, cardYearValid, cardOwner, cardCvc)
                .clickContinueBtn()
                .checkWrongFormatOwnerNotification();
    }

    @Test
    void buyWithCredit_checkInvalidNameNotification_rusChars() {
        // 29. "Купить в кредит": Проверка ошибки валидации при вводе имени владельца крты на кириллице
        String approvedCardNumber = DataHelper.getApprovedCardNumber();
        String cardMonthValid = DataHelper.getCurrentMonth();
        String cardYearValid = DataHelper.getCurrYearPlus(1);
        String cardOwner = DataHelper.generateNameRus();
        String cardCvc = DataHelper.getCardCvcValid();

        BasePage basePage = new BasePage();
        basePage
                .clickBuyWithCreditBtn()
                .fillCardInfo(approvedCardNumber, cardMonthValid, cardYearValid, cardOwner, cardCvc)
                .clickContinueBtn()
                .checkWrongFormatOwnerNotification();
    }

    @Test
    void buy_checkInvalidNameNotification_specChars() {
        // 30. "Купить": Проверка ошибки валидации при вводе спецсимволов в имени владельца
        String approvedCardNumber = DataHelper.getApprovedCardNumber();
        String cardMonthValid = DataHelper.getCurrentMonth();
        String cardYearValid = DataHelper.getCurrYearPlus(1);
        String cardOwner = DataHelper.getNameWithSpecChars();
        String cardCvc = DataHelper.getCardCvcValid();

        BasePage basePage = new BasePage();
        basePage
                .clickBuyBtn()
                .fillCardInfo(approvedCardNumber, cardMonthValid, cardYearValid, cardOwner, cardCvc)
                .clickContinueBtn()
                .checkWrongFormatOwnerNotification();
    }

    @Test
    void buyWithCredit_checkInvalidNameNotification_specChars() {
        // 31. "Купить в кредит": Проверка ошибки валидации при вводе спецсимволов в имени владельца
        String approvedCardNumber = DataHelper.getApprovedCardNumber();
        String cardMonthValid = DataHelper.getCurrentMonth();
        String cardYearValid = DataHelper.getCurrYearPlus(1);
        String cardOwner = DataHelper.getNameWithSpecChars();
        String cardCvc = DataHelper.getCardCvcValid();

        BasePage basePage = new BasePage();
        basePage
                .clickBuyWithCreditBtn()
                .fillCardInfo(approvedCardNumber, cardMonthValid, cardYearValid, cardOwner, cardCvc)
                .clickContinueBtn()
                .checkWrongFormatOwnerNotification();
    }

    @Test
    void buy_checkInvalidNameNotification_unfilledOwner() {
        // 32. "Купить": Проверка ошибки валидации при незаполнениии имени владельца крты
        String approvedCardNumber = DataHelper.getApprovedCardNumber();
        String cardMonthValid = DataHelper.getCurrentMonth();
        String cardYearValid = DataHelper.getCurrYearPlus(1);
        String cardCvc = DataHelper.getCardCvcValid();

        BasePage basePage = new BasePage();
        basePage
                .clickBuyBtn()
                .fillCardInfo(approvedCardNumber, cardMonthValid, cardYearValid, "", cardCvc)
                .clickContinueBtn()
                .checkUnfilledOwnerNotification();
    }

    @Test
    void buyWithCredit_checkInvalidNameNotification_unfilledOwner() {
        // 33. "Купить в кредит": Проверка ошибки валидации при незаполнениии имени владельца крты
        String approvedCardNumber = DataHelper.getApprovedCardNumber();
        String cardMonthValid = DataHelper.getCurrentMonth();
        String cardYearValid = DataHelper.getCurrYearPlus(1);
        String cardCvc = DataHelper.getCardCvcValid();

        BasePage basePage = new BasePage();
        basePage
                .clickBuyWithCreditBtn()
                .fillCardInfo(approvedCardNumber, cardMonthValid, cardYearValid, "", cardCvc)
                .clickContinueBtn()
                .checkUnfilledOwnerNotification();
    }

    @Test
    void buy_checkInvalidNameNotification_nameWithoutSpace() {
        // 34. "Купить": Проверка ошибки валидации при вводе имени владельца крты без пробела
        String approvedCardNumber = DataHelper.getApprovedCardNumber();
        String cardMonthValid = DataHelper.getCurrentMonth();
        String cardYearValid = DataHelper.getCurrYearPlus(1);
        String cardOwner = DataHelper.generateNameWithoutSpace();
        String cardCvc = DataHelper.getCardCvcValid();

        BasePage basePage = new BasePage();
        basePage
                .clickBuyBtn()
                .fillCardInfo(approvedCardNumber, cardMonthValid, cardYearValid, cardOwner, cardCvc)
                .clickContinueBtn()
                .checkWrongFormatOwnerNotification();
    }

    @Test
    void buyWithCredit_checkInvalidNameNotification_nameWithoutSpace() {
        // 35. "Купить в кредит": Проверка ошибки валидации при вводе имени владельца крты без пробела
        String approvedCardNumber = DataHelper.getApprovedCardNumber();
        String cardMonthValid = DataHelper.getCurrentMonth();
        String cardYearValid = DataHelper.getCurrYearPlus(1);
        String cardOwner = DataHelper.generateNameWithoutSpace();
        String cardCvc = DataHelper.getCardCvcValid();

        BasePage basePage = new BasePage();
        basePage
                .clickBuyWithCreditBtn()
                .fillCardInfo(approvedCardNumber, cardMonthValid, cardYearValid, cardOwner, cardCvc)
                .clickContinueBtn()
                .checkWrongFormatOwnerNotification();
    }


    @Test
    void buy_checkInvalidCvcNotification_shortCVC() {
        // 36. "Купить": Проверка ошибки валидации при вводе 1-2 цифр в поле CVC/CVV
        String approvedCardNumber = DataHelper.getApprovedCardNumber();
        String cardMonthValid = DataHelper.getCurrentMonth();
        String cardYearValid = DataHelper.getCurrYearPlus(1);
        String cardOwner = DataHelper.generateName();
        String cardCvc = DataHelper.getCardCvcInvalid();

        BasePage basePage = new BasePage();
        basePage
                .clickBuyBtn()
                .fillCardInfo(approvedCardNumber, cardMonthValid, cardYearValid, cardOwner, cardCvc)
                .clickContinueBtn()
                .checkWrongFormatCvcNotification();
    }

    @Test
    void buyWithCredit_checkInvalidCvcNotification_shortCVC() {
        // 37. "Купить в кредит": Проверка ошибки валидации при вводе 1-2 цифр в поле CVC/CVV
        String approvedCardNumber = DataHelper.getApprovedCardNumber();
        String cardMonthValid = DataHelper.getCurrentMonth();
        String cardYearValid = DataHelper.getCurrYearPlus(1);
        String cardOwner = DataHelper.generateName();
        String cardCvc = DataHelper.getCardCvcInvalid();

        BasePage basePage = new BasePage();
        basePage
                .clickBuyWithCreditBtn()
                .fillCardInfo(approvedCardNumber, cardMonthValid, cardYearValid, cardOwner, cardCvc)
                .clickContinueBtn()
                .checkWrongFormatCvcNotification();
    }

    @Test
    void buy_checkInvalidCvcNotification_unfilledCVC() {
        // 38. "Купить": Проверка ошибки валидации при незаполнениии поля CVC/CVV
        String approvedCardNumber = DataHelper.getApprovedCardNumber();
        String cardMonthValid = DataHelper.getCurrentMonth();
        String cardYearValid = DataHelper.getCurrYearPlus(1);
        String cardOwner = DataHelper.generateName();

        BasePage basePage = new BasePage();
        basePage
                .clickBuyBtn()
                .fillCardInfo(approvedCardNumber, cardMonthValid, cardYearValid, cardOwner, "")
                .clickContinueBtn()
                .checkUnfilledCvcNotification();
    }

    @Test
    void buyWithCredit_checkInvalidCvcNotification_unfilledCVC() {
        // 39. "Купить в кредит": Проверка ошибки валидации при незаполнениии поля CVC/CVV
        String approvedCardNumber = DataHelper.getApprovedCardNumber();
        String cardMonthValid = DataHelper.getCurrentMonth();
        String cardYearValid = DataHelper.getCurrYearPlus(1);
        String cardOwner = DataHelper.generateName();

        BasePage basePage = new BasePage();
        basePage
                .clickBuyWithCreditBtn()
                .fillCardInfo(approvedCardNumber, cardMonthValid, cardYearValid, cardOwner, "")
                .clickContinueBtn()
                .checkUnfilledCvcNotification();
    }

    @Test
    void buy_checkInvalidCvcNotification_unfilledAllFields() {
        // 40. "Купить": Проверка ошибки валидации при незаполнениии всех полей
        BasePage basePage = new BasePage();
        basePage
                .clickBuyBtn()
                .fillCardInfo("", "", "", "", "")
                .clickContinueBtn()
                .checkUnfilledCardNotification()
                .checkUnfilledMonthNotification()
                .checkUnfilledYearNotification()
                .checkUnfilledOwnerNotification()
                .checkUnfilledCvcNotification();
    }

    @Test
    void buyWithCredit_checkInvalidCvcNotification_unfilledAllFields() {
        // 41. "Купить в кредит": Проверка ошибки валидации при незаполнениии всех полей
        BasePage basePage = new BasePage();
        basePage
                .clickBuyWithCreditBtn()
                .fillCardInfo("", "", "", "", "")
                .clickContinueBtn()
                .checkUnfilledCardNotification()
                .checkUnfilledMonthNotification()
                .checkUnfilledYearNotification()
                .checkUnfilledOwnerNotification()
                .checkUnfilledCvcNotification();
    }
}