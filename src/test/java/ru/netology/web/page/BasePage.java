package ru.netology.web.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

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

        // Посчитать кол-во деталей путешествия. Если кол-во совпадает с ожидаемым, то убедиться что все записи показаны
        int numOfTripDetails = tripDetails.size();
        if (numOfTripDetails == tripDetailsExp.length) {
            for (int i = 0; i < numOfTripDetails; i++) {
                tripDetails.findBy(text(tripDetailsExp[i])).shouldBe(visible);
            }
        }
    }

    public BasePage clickBuyBtn() {
        buyBtn.shouldBe(visible);
        // ToDo: Добавить клик по кнопке + проверку, что появляются все нужные поля ввода
        return this;
    }
}