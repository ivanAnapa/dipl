package ru.netology.web.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.common.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class BasePage {

    // ******************************************** Текстовки  ********************************************
    private final SelenideElement pageTitle = $("h2");
    private final SelenideElement tripImage =
            $x("//img[@src='/static/media/marrakech.869b2a02.jpg' and @alt='Марракэш']");
    /*
     xpath для заголовка h3 "Марракэш" сделал немного замудреным, т.к. после клика по "купить" появляется
     еще 1 заголовок h3. Классы мне не понравились (не внушают доверия), поэтому решил отталкиваться от тега img.
     С этим xpath нужный заголовок h3 находится всегда, каждый раз уникален.
     */
    private final SelenideElement tripDescriptionHeader = $x("//img/ancestor::div[2]//h3");
    private final ElementsCollection tripDetails = $$x("//li[@class='list__item']");
    private final SelenideElement buyTripHeader = $x("//h3[text()='Оплата по карте']");
    private final SelenideElement buyWithCreditTripHeader = $x("//h3[text()='Кредит по данным карты']");

    // ******************************************** Кнопки  ********************************************
    private final SelenideElement buyBtn = $x("//button[.//span[text()='Купить']]");
    private final SelenideElement buyWithCreditBtn = $x("//button[.//span[text()='Купить в кредит']]");
    private final SelenideElement continueBtn = $x("//button[.//span[text()='Продолжить']]");

    // ******************************************* Поля ввода *******************************************
    private final SelenideElement cardNumberInput = $x("//span[./span[text()='Номер карты']]//input");
    private final SelenideElement cardMonthInput = $x("//span[./span[text()='Месяц']]//input");
    private final SelenideElement cardYearInput = $x("//span[./span[text()='Год']]//input");
    private final SelenideElement cardOwnerInput = $x("//span[./span[text()='Владелец']]//input");
    private final SelenideElement cardCvcInput = $x("//span[./span[text()='CVC/CVV']]//input");

    // ******************************************* Уведомление о покупке *******************************************
    private final ElementsCollection operationResultTitle = $$x("//div[@class='notification__title']");
    private final ElementsCollection operationResultContent = $$x("//div[@class='notification__content']");

    // ***************************** Уведомления о неправильно заполненных полях *****************************
    private final SelenideElement wrongCardNotification = $x("//span[./span[text()='Номер карты']]//span[last()]");
    private final SelenideElement wrongMonthNotification = $x("//span[./span[text()='Месяц']]//span[last()]");
    private final SelenideElement wrongYearNotification = $x("//span[./span[text()='Год']]//span[last()]");
    private final SelenideElement wrongOwnerNotification = $x("//span[./span[text()='Владелец']]//span[last()]");
    private final SelenideElement wrongCvcNotification = $x("//span[./span[text()='CVC/CVV']]//span[last()]");


    public BasePage() {
        pageTitle.shouldBe(text("Путешествие дня"), visible);
    }

    // ************************************ Действия ************************************
    // Кликнуть "Купить"
    public BasePage clickBuyBtn() {
        buyBtn.shouldBe(visible).click();
        return this;
    }

    // Кликнуть "Купить в кредит"
    public BasePage clickBuyWithCreditBtn() {
        buyWithCreditBtn.shouldBe(visible).click();
        return this;
    }

    // Заполнить все поля ввода
    public BasePage fillCardInfo(String cardNumber, String cardMonth, String cardYear, String cardOwner, String cvc) {
        cardNumberInput.setValue(cardNumber);
        cardMonthInput.setValue(cardMonth);
        cardYearInput.setValue(cardYear);
        cardOwnerInput.setValue(cardOwner);
        cardCvcInput.setValue(cvc);
        return this;
    }

    // Кликнуть "Продолжить"
    public BasePage clickContinueBtn() {
        continueBtn.click();
        return this;
    }

    // ************************************ Проверки ************************************
    // Валидация текстов на основной странице
    public void validateDefaultPage() {
        String[] tripDetailsExp = DataHelper.getTripDetailsDefault();

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

    // Видимость общих полей ввода
    private void validateInputFieldsVisibility() {
        cardNumberInput.shouldBe(visible);
        cardMonthInput.shouldBe(visible);
        cardYearInput.shouldBe(visible);
        cardOwnerInput.shouldBe(visible);
        cardCvcInput.shouldBe(visible);
        continueBtn.shouldBe(visible);
    }

    // "Купить": Проверка видимости заголовка и полей ввода
    public void validateFieldsForBuy() {
        buyTripHeader.shouldBe(visible);
        validateInputFieldsVisibility();
    }

    // "Купить в кредит": Проверка видимости заголовка и полей ввода
    public void validateFieldsForBuyWithCredit() {
        buyWithCreditTripHeader.shouldBe(visible);
        validateInputFieldsVisibility();
    }

    // Сообщение об успешной покупке
    public BasePage checkSuccessResult() {
        operationResultTitle.findBy(text("Успешно")).shouldBe(visible, Duration.ofSeconds(15));
        operationResultContent.findBy(text("Операция одобрена Банком.")).shouldBe(visible);
        return this;
    }

    // Сообщение об отказе в покупке
    public BasePage checkDeclinedResult() {
        operationResultTitle.findBy(text("Ошибка")).shouldBe(visible, Duration.ofSeconds(15));
        operationResultContent.findBy(text("Ошибка! Банк отказал в проведении операции.")).shouldBe(visible);
        return this;
    }

    // Сообщение о неправильном вводе номера карты (<16 цифр)
    public BasePage checkWrongCardNotification() {
        wrongCardNotification.shouldBe(text("Неверный формат"), visible);
        return this;
    }

    // Сообщение о незаполненном номере карты
    public BasePage checkUnfilledCardNotification() {
        wrongCardNotification.shouldBe(text("Поле обязательно для заполнения"), visible);
        return this;
    }


    // Сообщение о неправильном вводе месяца (1 цифра)
    public BasePage checkWrongFormatMonthNotification() {
        wrongMonthNotification.shouldBe(text("Неверный формат"), visible);
        return this;
    }

    // Сообщение о неправильном вводе месяца (месяц >12 или "00")
    public BasePage checkWrongMonthNotification() {
        wrongMonthNotification.shouldBe(text("Неверно указан срок действия карты"), visible);
        return this;
    }

    // Сообщение о незаполненном месяце
    public BasePage checkUnfilledMonthNotification() {
        wrongMonthNotification.shouldBe(text("Поле обязательно для заполнения"), visible);
        return this;
    }


    // Сообщение о неправильном вводе года (карта просрочена)
    public BasePage checkWrongYearNotificationOld() {
        wrongYearNotification.shouldBe(text("Истёк срок действия карты"), visible);
        return this;
    }

    // Сообщение о неправильном вводе года (срок действия карты на 6 лет больше текущего года)
    public BasePage checkWrongYearNotification() {
        wrongYearNotification.shouldBe(text("Неверно указан срок действия карты"), visible);
        return this;
    }

    // Сообщение о незаполненном поле "год"
    public BasePage checkUnfilledYearNotification() {
        wrongYearNotification.shouldBe(text("Поле обязательно для заполнения"), visible);
        return this;
    }


    // Сообщение о незаполненном имени владельца карты
    public BasePage checkUnfilledOwnerNotification() {
        wrongOwnerNotification.shouldBe(text("Поле обязательно для заполнения"), visible);
        return this;
    }

    // Сообщение о неверном формате в поле "Владелец"
    public BasePage checkWrongFormatOwnerNotification() {
        wrongOwnerNotification.shouldBe(text("Неверный формат"), visible);
        return this;
    }

    // Сообщение о неправильном вводе CVC (1-2 цифры вместо 3)
    public BasePage checkWrongFormatCvcNotification() {
        wrongCvcNotification.shouldBe(text("Неверный формат"), visible);
        return this;
    }

    // Сообщение о незаполненном CVC
    public BasePage checkUnfilledCvcNotification() {
        wrongCvcNotification.shouldBe(text("Поле обязательно для заполнения"), visible);
        return this;
    }


}