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

public class DebitCardPaymentTest {

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
    void shouldPayByApprovedDebitCard() {
        var paymentPage = dashboardPage.payByDebitCard();
        var approvedCardInformation = DataHelper.getApprovedCardInformation();
        paymentPage.enterCardInfo(approvedCardInformation);
        paymentPage.waitSuccessNotification();
        var paymentStatus = DbHelper.getPaymentEntity();
        assertEquals("APPROVED", paymentStatus);
    }


    @org.junit.jupiter.api.Test
    void shouldPayByDeclinedDebitCard() {
        var paymentPage = dashboardPage.payByDebitCard();
        var declinedCardInformation = DataHelper.getDeclinedCardInformation();
        paymentPage.enterCardInfo(declinedCardInformation);
        paymentPage.waitErrorNotification();
        var paymentStatus = DbHelper.getPaymentEntity();
        assertEquals("DECLINED", paymentStatus);
    }

    @org.junit.jupiter.api.Test
    void shouldPayByDebitCardWithInvalidNumber() {
        var paymentPage = dashboardPage.payByDebitCard();
        var invalidCardInformation = DataHelper.getInvalidCardInformation();
        paymentPage.enterCardInfo(invalidCardInformation);
        paymentPage.checkInvalidCardNumber();
    }

    @org.junit.jupiter.api.Test
    void shouldPayByDebitCardWithExpiredYear() {
        var paymentPage = dashboardPage.payByDebitCard();
        var expiredYearCardInformation = DataHelper.getExpiredYearCardInformation();
        paymentPage.enterCardInfo(expiredYearCardInformation);
        paymentPage.checkExpiredYearMessage();
    }

    @org.junit.jupiter.api.Test
    void shouldPayByDebitCardWithInvalidExpirationDate() {
        var paymentPage = dashboardPage.payByDebitCard();
        var invalidExpirationDate = DataHelper.getInvalidExpirationDateCardInformation();
        paymentPage.enterCardInfo(invalidExpirationDate);
        paymentPage.checkInvalidExpirationDate();
    }

    @org.junit.jupiter.api.Test
    void shouldPayByDebitCardWithExpiredMonth() {
        var paymentPage = dashboardPage.payByDebitCard();
        var expiredMonth = DataHelper.getExpiredMonthCardInformation();
        paymentPage.enterCardInfo(expiredMonth);
        paymentPage.checkExpiredMonthMessage();
    }

    @org.junit.jupiter.api.Test
    void shouldPayByDebitCardWithEmptyCardInformation() {
        var paymentPage = dashboardPage.payByDebitCard();
        var emptyCardInformation = DataHelper.getEmptyCardInformation();
        paymentPage.enterCardInfo(emptyCardInformation);
        paymentPage.checkEmptyCardNumberFieldMessage();
        paymentPage.checkEmptyMonthFieldMessage();
        paymentPage.checkEmptyYearFieldMessage();
        paymentPage.checkEmptyOwnerFieldMessage();
        paymentPage.checkEmptyCvcFieldMessage();
    }

    @org.junit.jupiter.api.Test
    void shouldPayByDebitCardWithInvalidOwner() {
        var paymentPage = dashboardPage.payByDebitCard();
        var invalidOwner = DataHelper.getInvalidOwnerCard();
        paymentPage.enterCardInfo(invalidOwner);
        paymentPage.checkInvalidOwner();
    }

    @org.junit.jupiter.api.Test
    void shouldPayByDebitCardWithValidCardNumberAndInvalidOtherFields() {
        var paymentPage = dashboardPage.payByDebitCard();
        var validCardNumberWithInvalidOtherFields = DataHelper.getValidCardNumberWithInvalidOtherFields();
        paymentPage.enterCardInfo(validCardNumberWithInvalidOtherFields);
        paymentPage.checkInvalidMonth();
        paymentPage.checkInvalidYear();
        paymentPage.checkInvalidOwner();
        paymentPage.checkInvalidCvc();
    }

}