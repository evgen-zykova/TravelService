package ru.netology.test;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.val;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.sql.DbHelper;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreditCardPaymentTest {

    DashboardPage dashboardPage = new DashboardPage();

    @BeforeEach
    public void openPage() {
        String url = System.getProperty("sut.url");
        open(url);
        Selenide.clearBrowserCookies();
        Selenide.clearBrowserLocalStorage();
    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
        DbHelper.cleanDb();
    }

    @org.junit.jupiter.api.Test
    void shouldPayByApprovedCreditCard() {
        var paymentPage = dashboardPage.payByCreditCard();
        var approvedCardInformation = DataHelper.getApprovedCardInformation();
        paymentPage.enterCardInfo(approvedCardInformation);
        paymentPage.waitSuccessNotification();
        var creditStatus = DbHelper.getCreditEntity();
        assertEquals("APPROVED", creditStatus);
    }

    @org.junit.jupiter.api.Test
    void shouldPayByDeclinedCreditCard() {
        var paymentPage = dashboardPage.payByCreditCard();
        var declinedCardInformation = DataHelper.getDeclinedCardInformation();
        paymentPage.enterCardInfo(declinedCardInformation);
        paymentPage.waitErrorNotification();
        var creditStatus = DbHelper.getCreditEntity();
        assertEquals("DECLINED", creditStatus);
    }

    @org.junit.jupiter.api.Test
    void shouldPayByCreditCardWithInvalidNumber() {
        var paymentPage = dashboardPage.payByCreditCard();
        var invalidCardInformation = DataHelper.getInvalidCardInformation();
        paymentPage.enterCardInfo(invalidCardInformation);
        paymentPage.checkInvalidCardNumber();
    }

    @org.junit.jupiter.api.Test
    void shouldPayByCreditCardWithExpiredYear() {
        var paymentPage = dashboardPage.payByCreditCard();
        var expiredYearCardInformation = DataHelper.getExpiredYearCardInformation();
        paymentPage.enterCardInfo(expiredYearCardInformation);
        paymentPage.checkExpiredYearMessage();
    }

    @org.junit.jupiter.api.Test
    void shouldPayByCreditCardWithInvalidExpirationDate() {
        var paymentPage = dashboardPage.payByCreditCard();
        var invalidExpirationDate = DataHelper.getInvalidExpirationDateCardInformation();
        paymentPage.enterCardInfo(invalidExpirationDate);
        paymentPage.checkInvalidExpirationDate();
    }

    @org.junit.jupiter.api.Test
    void shouldPayByCreditCardWithExpiredMonth() {
        var paymentPage = dashboardPage.payByCreditCard();
        var expiredMonth = DataHelper.getExpiredMonthCardInformation();
        paymentPage.enterCardInfo(expiredMonth);
        paymentPage.checkExpiredMonthMessage();
    }

    @org.junit.jupiter.api.Test
    void shouldPayByCreditCardWithEmptyCardInformation() {
        var paymentPage = dashboardPage.payByCreditCard();
        var emptyCardInformation = DataHelper.getEmptyCardInformation();
        paymentPage.enterCardInfo(emptyCardInformation);
        paymentPage.checkEmptyCardNumberFieldMessage();
        paymentPage.checkEmptyMonthFieldMessage();
        paymentPage.checkEmptyYearFieldMessage();
        paymentPage.checkEmptyOwnerFieldMessage();
        paymentPage.checkEmptyCvcFieldMessage();
    }

    @org.junit.jupiter.api.Test
    void shouldPayByCreditCardWithInvalidOwner() {
        var paymentPage = dashboardPage.payByCreditCard();
        var invalidOwner = DataHelper.getInvalidOwnerCard();
        paymentPage.enterCardInfo(invalidOwner);
        paymentPage.checkInvalidOwner();
    }

    @org.junit.jupiter.api.Test
    void shouldPayByCreditCardWithValidCardNumberAndInvalidOtherFields() {
        var paymentPage = dashboardPage.payByCreditCard();
        var validCardNumberWithInvalidOtherFields = DataHelper.getValidCardNumberWithInvalidOtherFields();
        paymentPage.enterCardInfo(validCardNumberWithInvalidOtherFields);
        paymentPage.checkInvalidMonth();
        paymentPage.checkInvalidYear();
        paymentPage.checkInvalidOwner();
        paymentPage.checkInvalidCvc();
    }

}