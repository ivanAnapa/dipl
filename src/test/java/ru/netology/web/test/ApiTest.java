package ru.netology.web.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.val;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.web.data.RequestHelper.buyReq;
import static ru.netology.web.data.RequestHelper.buyWithCreditReq;

public class ApiTest {

    private final String approvedCardNumber = DataHelper.getApprovedCardNumber();
    private final String declinedCardNumber = DataHelper.getDeclinedCardNumber();
    private final String cardYear = DataHelper.getCurrYearPlus(1);
    private final String cardMonth = DataHelper.getCurrentMonth();
    private final String cardOwner = DataHelper.generateName();
    private final String cardCvc = DataHelper.getCardCvcValid();

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    public void buyRequest_approvedCardAndValidData() {
        val response = buyReq(approvedCardNumber, cardYear, cardMonth, cardOwner, cardCvc);
        System.out.println("Ответ на запрос: " + response);
        assertEquals("{\"status\":\"APPROVED\"}", response, "");
    }

    @Test
    void buyWithCreditRequest_approvedCardAndValidData() {
        val response = buyWithCreditReq(approvedCardNumber, cardYear, cardMonth, cardOwner, cardCvc);
        System.out.println("Ответ на запрос: " + response);
        assertEquals("{\"status\":\"APPROVED\"}", response, "");
    }

    @Test
    public void buyRequest_declinedCardAndValidData() {
        val response = buyReq(declinedCardNumber, cardYear, cardMonth, cardOwner, cardCvc);
        System.out.println("Ответ на запрос: " + response);
        assertEquals("{\"status\":\"DECLINED\"}", response, "");
    }

    @Test
    void buyWithCreditRequest_declinedCardAndValidData() {
        val response = buyWithCreditReq(declinedCardNumber, cardYear, cardMonth, cardOwner, cardCvc);
        System.out.println("Ответ на запрос: " + response);
        assertEquals("{\"status\":\"DECLINED\"}", response, "");
    }
}
