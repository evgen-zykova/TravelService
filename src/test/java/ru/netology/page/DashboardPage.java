package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private final SelenideElement heading = $("h2.heading");
    private final SelenideElement buyButton = $$(".button").first(); // $(byText("Купить"));
    private final SelenideElement buyButtonOnCredit = $$(".button").last(); // $(byText("Купить в кредит"));
    private final SelenideElement paymentByCard = $$("h3.heading").find(Condition.exactText("Оплата по карте"));
    private final SelenideElement creditCardData = $$("h3.heading").find(Condition.exactText("Кредит по данным карты"));

    public DashboardPage() {
        heading.shouldBe(Condition.visible);
    }

    public void getDebitCardPayment() {
        buyButton.click();
        paymentByCard.shouldBe(Condition.visible);
        new PaymentPage();
    }

    public void getPaymentByCreditCard() {
        buyButtonOnCredit.click();
        creditCardData.shouldBe(Condition.visible);
        new PaymentPage();
    }
}