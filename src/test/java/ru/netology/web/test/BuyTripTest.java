package ru.netology.web.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.BasePage;

import static com.codeborne.selenide.Selenide.open;

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
    void setup() {
        open("http://localhost:8080");
    }

    @Test
    void checkDefaultPage() {
        // 1. Проверить наличие текстов до клика по кнопкам "Купить" или "Купить в кредит"
        BasePage basePage = new BasePage();
        basePage.validateDefaultPage();
    }

    @Test
    void checkElementsForBuy() {
        // 2. "Купить": Проверка видимости заголовка и полей ввода
        BasePage basePage = new BasePage();
        basePage
                .clickBuyBtn()
                .validateFieldsForBuy();
    }

    @Test
    void checkElementsForBuyWithCredit() {
        // 3. "Купить в кредит": Проверка видимости заголовка и полей ввода
        BasePage basePage = new BasePage();
        basePage
                .clickBuyWithCreditBtn()
                .validateFieldsForBuyWithCredit();
    }

    @Test
    void buyTripByApprovedCardAndValidData() {
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
    }

    @Test
    void buyWithCreditTripByApprovedCardAndValidData() {
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
    }


    @Test
    void buyTripByDeclinedCardAndValidData() {
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
    void buyWithCreditTripByDeclinedCardAndValidData() {
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
    void buyTripByInvalidFormatCardAndValidData() {
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
    void buyWithCreditTripByInvalidFormatCardAndValidData() {
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
    void buyTripByUnfilledCardAndValidData() {
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
    void buyWithCreditTripByUnfilledCardAndValidData() {
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

    /**
     * 12. Проверка ошибки валидации при вводе месяца "00" для типа "Купить"
     * Карта - 4444 4444 4444 4441; Месяц - 00; Год - 24; Владелец - George Byron; CVC - 913
     * ОР: Под полем ввода месяца появилось сообщение о некорректном месяце
     * 13. Проверка ошибки валидации при вводе месяца "00" для типа "Купить в кредит"
     * Карта - 4444 4444 4444 4441; Месяц - 00; Год - 24; Владелец - George Byron; CVC - 913
     * ОР: Под полем ввода месяца появилось сообщение о некорректном месяце
     * 14. Проверка ошибки валидации при вводе месяца "13" для типа "Купить"
     * Карта - 4444 4444 4444 4441; Месяц - 13; Год - 24; Владелец - George Byron; CVC - 913
     * ОР: Под полем ввода месяца появилось сообщение о некорректном месяце
     * 15. Проверка ошибки валидации при вводе месяца "13" для типа "Купить в кредит"
     * Карта - 4444 4444 4444 4441; Месяц - 13; Год - 24; Владелец - George Byron; CVC - 913
     * ОР: Под полем ввода месяца появилось сообщение о некорректном месяце
     * 16. Проверка ошибки валидации при вводе года "текущий + 6 лет" для типа "Купить"
     * Карта - 4444 4444 4444 4441; Месяц - 01; Год - 28; Владелец - George Byron; CVC - 913
     * ОР: Под полем ввода года появилось сообщение о некорректном годе
     * Проверка ошибки валидации при вводе года "текущий + 6 лет" для типа "Купить в кредит"
     * Карта - 4444 4444 4444 4441; Месяц - 01; Год - 28; Владелец - George Byron; CVC - 913
     * ОР: Под полем ввода года появилось сообщение о некорректном годе
     * Проверка ошибки валидации при вводе года "текущий - 1 год" для типа "Купить"
     * Карта - 4444 4444 4444 4441; Месяц - 01; Год - 21; Владелец - George Byron; CVC - 913
     * ОР: Под полем ввода года появилось сообщение о некорректном годе (или "карта просрочена")
     * Проверка ошибки валидации при вводе года "текущий - 1 год" для типа "Купить в кредит"
     * Карта - 4444 4444 4444 4441; Месяц - 01; Год - 21; Владелец - George Byron; CVC - 913
     * ОР: Под полем ввода года появилось сообщение о некорректном годе (или "карта просрочена")
     * Проверка ошибки валидации при вводе прошлого месяца текущего года для типа "Купить"
     * Карта - 4444 4444 4444 4441; Месяц - 08; Год - 21; Владелец - George Byron; CVC - 913
     * ОР: Под полем ввода года появилось сообщение "карта просрочена"
     * Проверка ошибки валидации при вводе прошлого месяца текущего года для типа "Купить в кредит"
     * Карта - 4444 4444 4444 4441; Месяц - 08; Год - 21; Владелец - George Byron; CVC - 913
     * ОР: Под полем ввода года появилось сообщение "карта просрочена"
     * Проверка ошибки валидации при незаполнениии месяца для типа "Купить"
     * Карта - 4444 4444 4444 4441; Месяц - не_заполнять; Год - 24; Владелец - George Byron; CVC - 913
     * ОР: Под полем ввода месяца появилось сообщение о необходимости ввода месяца
     * Проверка ошибки валидации при незаполнениии месяца для типа "Купить в кредит"
     * Карта - 4444 4444 4444 4441; Месяц - не_заполнять; Год - 24; Владелец - George Byron; CVC - 913
     * ОР: Под полем ввода месяца появилось сообщение о необходимости ввода месяца
     * Проверка ошибки валидации при незаполнениии года для типа "Купить"
     * Карта - 4444 4444 4444 4441; Месяц - 01; Год - не_заполнять; Владелец - George Byron; CVC - 913
     * ОР: Под полем ввода месяца появилось сообщение о необходимости ввода года
     * Проверка ошибки валидации при незаполнениии года для типа "Купить в кредит"
     * Карта - 4444 4444 4444 4441; Месяц - 01; Год - не_заполнять; Владелец - George Byron; CVC - 913
     * ОР: Под полем ввода месяца появилось сообщение о необходимости ввода года
     * Проверка ошибки валидации при вводе имени владельца крты на кириллице для типа "Купить"
     * Карта - 4444 4444 4444 4441; Месяц - 01; Год - 25; Владелец - Александр Пушкин; CVC - 913
     * ОР: Под полем ввода месяца появилось сообщение о неверном формате
     * Проверка ошибки валидации при вводе имени владельца крты на кириллице для типа "Купить в кредит"
     * Карта - 4444 4444 4444 4441; Месяц - 01; Год - 25; Владелец - Александр Пушкин; CVC - 913
     * ОР: Под полем ввода месяца появилось сообщение о неверном формате
     * Проверка ошибки валидации при вводе спецсимволов в имени владельца крты для типа "Купить"
     * Карта - 4444 4444 4444 4441; Месяц - 01; Год - 25; Владелец - !@# *&^%; CVC - 913
     * ОР: Под полем ввода месяца появилось сообщение о неверном формате
     * Проверка ошибки валидации при вводе спецсимволов в имени владельца крты для типа "Купить в кредит"
     * Карта - 4444 4444 4444 4441; Месяц - 01; Год - 25; Владелец - !@# *&^%; CVC - 913
     * ОР: Под полем ввода месяца появилось сообщение о неверном формате
     * Проверка ошибки валидации при незаполнениии имени владельца крты для типа "Купить"
     * Карта - 4444 4444 4444 4441; Месяц - 01; Год - 25; Владелец - не_заполнять; CVC - 913
     * ОР: Под полем ввода месяца появилось сообщение о необходимости заполнения имени
     * Проверка ошибки валидации при незаполнениии имени владельца крты для типа "Купить в кредит"
     * Карта - 4444 4444 4444 4441; Месяц - 01; Год - 25; Владелец - не_заполнять; CVC - 913
     * ОР: Под полем ввода месяца появилось сообщение о необходимости заполнения имени
     * Проверка ошибки валидации при вводе 1 цифры в поле CVC/CVV для типа "Купить"
     * Карта - 4444 4444 4444 4441; Месяц - 01; Год - 25; Владелец - Bill Gates; CVC - 1
     * ОР: Под полем ввода месяца появилось сообщение о неверном формате CVC
     * Проверка ошибки валидации при вводе 1 цифры в поле CVC/CVV для типа "Купить в кредит"
     * Карта - 4444 4444 4444 4441; Месяц - 01; Год - 25; Владелец - Bill Gates; CVC - 2
     * ОР: Под полем ввода месяца появилось сообщение о неверном формате CVC
     * Проверка ошибки валидации при незаполнениии поля CVC/CVV для типа "Купить"
     * Карта - 4444 4444 4444 4441; Месяц - 01; Год - 25; Владелец - Bill Gates; CVC - не_заполнять
     * ОР: Под полем ввода месяца появилось сообщение о необходимости заполнения CVC
     * Проверка ошибки валидации при незаполнениии поля CVC/CVV для типа "Купить в кредит"
     * Карта - 4444 4444 4444 4441; Месяц - 01; Год - 25; Владелец - Bill Gates; CVC - не_заполнять
     * ОР: Под полем ввода месяца появилось сообщение о необходимости заполнения CVC
     */
    @Test
    void buyTripByApprovedCardAndInvalidMonthLarge() {
        // "Купить": Проверить текст ошибки при вводе непрпавильного месяца (>12)
        String approvedCardNumber = DataHelper.getApprovedCardNumber();
        String cardMonthInvalid = DataHelper.getCardMonthInvalidLarge();
        String cardYearValid = DataHelper.getCurrYearPlus(3);
        String cardOwner = DataHelper.generateName();
        String cardCvc = DataHelper.getCardCvcValid();

        BasePage basePage = new BasePage();
        basePage
                .clickBuyBtn()
                .fillCardInfo(approvedCardNumber, cardMonthInvalid, cardYearValid, cardOwner, cardCvc)
                .clickContinueBtn()
                .checkWrongMonthNotificationLarge();
    }

    @Test
    void buyTripByApprovedCardAndInvalidMonthSmall() {
        // "Купить": Проверить текст ошибки при вводе непрпавильного месяца (1 цифра)
        String approvedCardNumber = DataHelper.getApprovedCardNumber();
        String cardMonthInvalid = DataHelper.getCardMonthInvalidSmall();
        String cardYearValid = DataHelper.getCurrYearPlus(4);
        String cardOwner = DataHelper.generateName();
        String cardCvc = DataHelper.getCardCvcValid();

        BasePage basePage = new BasePage();
        basePage
                .clickBuyBtn()
                .fillCardInfo(approvedCardNumber, cardMonthInvalid, cardYearValid, cardOwner, cardCvc)
                .clickContinueBtn()
                .checkWrongMonthNotificationSmall();
    }


}