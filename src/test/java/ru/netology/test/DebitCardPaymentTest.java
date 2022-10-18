package ru.netology.test;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.val;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.data.SqlHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.PaymentPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class DebitCardPaymentTest {

    @BeforeEach
    void setUp() {
      open("http://localhost:8080");
    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
        SqlHelper.deleteTables();
    }

    // POSITIVE SCENARIOS

    @Test
    void shouldPaymentByDebitCardWithStatusApproved() {
        val dashboardPage = new DashboardPage();
        val paymentPage = new PaymentPage();
        dashboardPage.getDebitCardPayment();
        val cardNumber = DataHelper.getApprovedCardNumber();
        val month = DataHelper.getMonth(0);
        val year = DataHelper.getYear(0);
        val owner = DataHelper.getValidOwner();
        val cvc = DataHelper.getCorrectCVC();
        paymentPage.fillPaymentFormat(cardNumber, month, year, owner, cvc);
        paymentPage.checkSuccessNotification();
        val paymentStatus = SqlHelper.getStatusPaymentEntity();
        assertEquals("APPROVED", paymentStatus);
    }
}
