package ru.netology.web.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class BasePage {
    private SelenideElement pageTitle = $("h2");
    private SelenideElement tripImage =
            $x("//img[@src='/static/media/marrakech.869b2a02.jpg' and @alt='Марракэш']");
    /*
     xpath для заголовка h3 "Марракэш" сделал немного замудреным, т.к. после клика по "купить" появляется
     еще 1 заголовок h3. Классы мне не понравились (не внушают доверия), поэтому решил отталкиваться от тега img.
     С этим xpath нужный заголовок h3 находится всегда, каждый раз уникален.
     */
    private SelenideElement tripDescriptionHeader = $x("//img/ancestor::div[2]//h3");
    private ElementsCollection tripDetails = $$x("//li[@class='list__item']");
    private SelenideElement buyBtn = $x("//button[.//span[text()='Купить']]");
    private SelenideElement buyWithCreditBtn = $x("//button[.//span[text()='Купить в кредит']]");

    private SelenideElement cardNumberInput = $x("//span[./span[text()='Номер карты']]//input");
    private SelenideElement cardMonthInput = $x("//span[./span[text()='Месяц']]//input");
    private SelenideElement cardYearInput = $x("//span[./span[text()='Год']]//input");
    private SelenideElement cardOwnerInput = $x("//span[./span[text()='Владелец']]//input");
    private SelenideElement cardCvcInput = $x("//span[./span[text()='CVC/CVV']]//input");

    private SelenideElement continueBtn = $x("//button[.//span[text()='Продолжить']]");

    private ElementsCollection operationResultTitle = $$x("//div[@class='notification__title']");
    private ElementsCollection operationResultContent = $$x("//div[@class='notification__content']");

    // Уведомления о неправильно заполненных полях
    private SelenideElement wrongCardNotification = $x("//span[./span[text()='Номер карты']]//span[last()]");
    private SelenideElement wrongMonthNotification = $x("//span[./span[text()='Месяц']]//span[last()]");
    private SelenideElement wrongYearNotification = $x("//span[./span[text()='Год']]//span[last()]");
    private SelenideElement wrongOwnerNotification = $x("//span[./span[text()='Владелец']]//span[last()]");
    private SelenideElement wrongCvcNotification = $x("//span[./span[text()='CVC/CVV']]//span[last()]");

    private final String[] tripDetailsExp =
            {
                    "Сказочный Восток",
                    "33 360 миль на карту",
                    "До 7% на остаток по счёту",
                    "Всего 45 000 руб.!"
            };

    public BasePage() {
        pageTitle.shouldBe(text("Путешествие дня"), visible);
        tripImage.shouldBe(visible);
        tripDescriptionHeader.shouldBe(text("Марракэш"), visible);

        // Посчитать кол-во деталей тура. Если кол-во совпадает с ожидаемым, то убедиться что все записи показаны
        int numOfTripDetails = tripDetails.size();
        if (numOfTripDetails == tripDetailsExp.length) {
            for (int i = 0; i < numOfTripDetails; i++) {
                tripDetails.findBy(text(tripDetailsExp[i])).shouldBe(visible);
            }
        } else {
            throw new RuntimeException("Неверное количество деталей тура. Должно быть 4 записи, по факту: " + numOfTripDetails);
        }
    }

    public BasePage clickBuyBtn() {
        buyBtn.shouldBe(visible).click();
        return this;
    }

    public BasePage clickBuyWithCreditBtn() {
        buyWithCreditBtn.shouldBe(visible).click();
        return this;
    }

    public BasePage fillCardInfo(String cardNumber, String cardMonth, String cardYear, String cardOwner, String cvc) {
        cardNumberInput.setValue(cardNumber);
        cardMonthInput.setValue(cardMonth);
        cardYearInput.setValue(cardYear);
        cardOwnerInput.setValue(cardOwner);
        cardCvcInput.setValue(cvc);
        return this;
    }

    public BasePage clickContinueBtn() {
        continueBtn.click();
        return this;
    }

    public BasePage checkSuccessResult() {
        operationResultTitle.findBy(text("Успешно")).shouldBe(visible, Duration.ofSeconds(10));
        operationResultContent.findBy(text("Операция одобрена Банком.")).shouldBe(visible);
        return this;
    }

    public BasePage checkDeclinedResult() {
        operationResultTitle.findBy(text("Ошибка")).shouldBe(visible, Duration.ofSeconds(10));
        operationResultContent.findBy(text("Ошибка! Банк отказал в проведении операции.")).shouldBe(visible);
        return this;
    }

    public BasePage checkWrongCardNotification() {
        wrongCardNotification.shouldBe(text("Неверный формат"), visible);
        return this;
    }

    public BasePage checkWrongMonthNotificationSmall() {
        // Если месяц в виде 1 цифры
        wrongMonthNotification.shouldBe(text("Неверный формат"), visible);
        return this;
    }
    public BasePage checkWrongMonthNotificationLarge() {
        // Если месяц > 12
        wrongMonthNotification.shouldBe(text("Неверно указан срок действия карты"), visible);
        return this;
    }


    public BasePage checkWrongYearNotificationOld() {
        wrongYearNotification.shouldBe(text("Истёк срок действия карты"), visible);
        return this;
    }

    public BasePage checkWrongYearNotificationInvalid() {
        wrongYearNotification.shouldBe(text("Неверно указан срок действия карты"), visible);
        return this;
    }

    public BasePage checkWrongOwnerNotification() {
        wrongOwnerNotification.shouldBe(text("Поле обязательно для заполнения"), visible);
        return this;
    }

    public BasePage checkWrongCvcNotification() {
        wrongCvcNotification.shouldBe(text("Неверный формат"), visible);
        return this;
    }


}