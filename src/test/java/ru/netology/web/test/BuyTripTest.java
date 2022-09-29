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
    void buyTripByApprovedCardAndValidData() {
        String approvedCardNumber = DataHelper.getApprovedCardNumber();
        String cardMonthValid = DataHelper.getCardMonthValid();
        String cardYearValid = DataHelper.getCardYearValid();
        String cardOwner = DataHelper.generateName();
        String cardCvc = DataHelper.getCardCVC();

        BasePage basePage = new BasePage();
        basePage
                .clickBuyBtn()
                .fillCardInfo(approvedCardNumber, cardMonthValid, cardYearValid, cardOwner, cardCvc)
                .clickContinueBtn()
                .checkSuccessResult();
    }

    @Test
    void buyTripByDeclinedCardAndValidData() {
        String declinedCardNumber = DataHelper.getDeclinedCardNumber();
        String cardMonthValid = DataHelper.getCardMonthValid();
        String cardYearValid = DataHelper.getCardYearValid();
        String cardOwner = DataHelper.generateName();
        String cardCvc = DataHelper.getCardCVC();

        BasePage basePage = new BasePage();
        basePage
                .clickBuyBtn()
                .fillCardInfo(declinedCardNumber, cardMonthValid, cardYearValid, cardOwner, cardCvc)
                .clickContinueBtn()
                .checkDeclinedResult();
    }

    @Test
    void buyTripByApprovedCardAndInvalidMonthLarge() {
        // Нерпавильный месяц (>12)
        String approvedCardNumber = DataHelper.getApprovedCardNumber();
        String cardMonthInvalid = DataHelper.getCardMonthInvalidLarge();
        String cardYearValid = DataHelper.getCardYearValid();
        String cardOwner = DataHelper.generateName();
        String cardCvc = DataHelper.getCardCVC();

        BasePage basePage = new BasePage();
        basePage
                .clickBuyBtn()
                .fillCardInfo(approvedCardNumber, cardMonthInvalid, cardYearValid, cardOwner, cardCvc)
                .clickContinueBtn()
                .checkWrongMonthNotificationLarge();
    }

    @Test
    void buyTripByApprovedCardAndInvalidMonthSmall() {
        // Нерпавильный месяц (1 цифра)
        String approvedCardNumber = DataHelper.getApprovedCardNumber();
        String cardMonthInvalid = DataHelper.getCardMonthInvalidSmall();
        String cardYearValid = DataHelper.getCardYearValid();
        String cardOwner = DataHelper.generateName();
        String cardCvc = DataHelper.getCardCVC();

        BasePage basePage = new BasePage();
        basePage
                .clickBuyBtn()
                .fillCardInfo(approvedCardNumber, cardMonthInvalid, cardYearValid, cardOwner, cardCvc)
                .clickContinueBtn()
                .checkWrongMonthNotificationSmall();
    }

}