package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private final SelenideElement heading = $("h2.heading");
    private final SelenideElement paymentButton = $$(".button").first(); // $(byText("Купить"));
    private final SelenideElement creditButton = $$(".button").last(); // $(byText("Купить в кредит"));
    private final SelenideElement payCard = $$("h3.heading").find(Condition.exactText("Оплата по карте"));
    private final SelenideElement payCredit = $$("h3.heading").find(Condition.exactText("Кредит по данным карты"));

    public PaymentPage payByDebitCard() {
        paymentButton.click();
        payCard.shouldBe(visible, Duration.ofSeconds(150));
        return new PaymentPage();
    }

    public PaymentPage payByCreditCard() {
        creditButton.click();
        payCredit.shouldBe(visible, Duration.ofSeconds(150));
        return new PaymentPage();
    }
}