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

    @AfterAll static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }


    @BeforeEach
    void setup() {
        open("http://localhost:8080");
    }

    @Test
    void buyTripByApprovedCardAndValidData() {
        String approvedCardNumber = DataHelper.getApprovedCardNumber();

        BasePage basePage = new BasePage();
        basePage.clickBuyBtn();
        System.out.println(approvedCardNumber); // Для дебага

    }

}