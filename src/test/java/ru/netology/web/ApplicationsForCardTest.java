package ru.netology.web;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApplicationsForCardTest {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");

    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldCheckCardApplicationForm() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Антонов Юрий");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+71234567890");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector(".button__text")).click();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText().trim();
        assertEquals(expected, actual);
    }

    //НЕГАТИВНЫЕ ТЕСТЫ
    @Test
    // Ввод латиницы в фамилии-должно отображаться предупреждение при вводе на латинице
    void shouldDisplayWarningWhenEnteringInLatin1() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Uнтонов Юрий");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+71234567890");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector(".button__text")).click();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actual = driver.findElement(By.cssSelector(".input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
     // Ввод латиницы в имени-должно отображаться предупреждение при вводе на латинице
    void shouldDisplayWarningWhenEnteringInLatin2() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Антонов Yрий");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+71234567890");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector(".button__text")).click();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actual = driver.findElement(By.cssSelector(".input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
     //следует проверить телефон без +
    void shouldCheckPhoneWithoutPlus() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Антонов Юрий");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("71234567890");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector(".button__text")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

}
