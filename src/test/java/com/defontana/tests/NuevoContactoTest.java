package com.defontana.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.Random;

import static org.junit.Assert.assertTrue;

public class NuevoContactoTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private String correoGenerado;
    private final String baseUrl = "https://crm-web-cert.defontana.com/login";

    @Before
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();

        // Opciones para ejecución en contenedor
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");

        // Evitar conflicto con perfiles
        String tempDir = System.getProperty("java.io.tmpdir");
        options.addArguments("--user-data-dir=" + tempDir + "/chrome-user-data-" + System.currentTimeMillis());

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        driver.manage().window().maximize();
    }

    @Test
    public void testLogin() {
        driver.get(baseUrl);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("cliente"))).sendKeys("empresas defontana");
        driver.findElement(By.name("usuario")).sendKeys("auditor6");
        driver.findElement(By.name("empresa")).sendKeys("DFCHILE");
        driver.findElement(By.name("password")).sendKeys("2");

        WebElement botonEntrar = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@type='submit' and contains(@class, 'login__submit') and normalize-space()='Entrar']")));
        botonEntrar.click();

        boolean loginExitoso = wait.until(ExpectedConditions.urlContains("pipeline=7"));
        assertTrue("❌ Login falló", loginExitoso);
        System.out.println("✅ Login exitoso");

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".loading-content")));

        WebElement enlaceContactos = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(@href, '/contacts')]")));
        enlaceContactos.click();
        System.out.println("🟢 Se hizo clic en el enlace de Contactos");
        System.out.println("🟢 Se agrega testigo de ejecución automatica 1");

        WebElement btnNuevo = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(., 'Persona')]")));
        btnNuevo.click();

        WebElement campoNombreEmpresa = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("name")));
        campoNombreEmpresa.sendKeys("Prueba automatizada");
        System.out.println("✅ Nombre contacto ingresado correctamente");

        WebElement campoApellido = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("lastName")));
        campoApellido.sendKeys("Prueba automatizada");
        System.out.println("✅ Apellido del contacto ingresado correctamente");

        WebElement Fuente = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("lastName")));
        Fuente.sendKeys("Prueba");
        System.out.println("✅ fuente del contacto ingresado correctamente");

        WebElement Telefono = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("phoneNumber")));
        Telefono.sendKeys("985326620");
        System.out.println("✅ Telefono del contacto ingresado correctamente");

        correoGenerado = generarCorreoAleatorio();
        WebElement emailInput = wait.until(ExpectedConditions.elementToBeClickable(By.name("mail")));
        emailInput.sendKeys(correoGenerado);

        WebElement campoAutocomplete = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//input[contains(@class,'MuiAutocomplete-input') and contains(@class,'MuiOutlinedInput-input')]")
        ));
        campoAutocomplete.clear();
        campoAutocomplete.sendKeys("Archetool");
        System.out.println("✅ Empresa archetool seleccionada correctamente");

        WebElement opcionArchetool = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[normalize-space()='Archetool']")
        ));
        opcionArchetool.click();

        WebElement btnGuardar = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(., 'Guardar')]")));
        btnGuardar.click();

        esperarSpinnerDesaparecer();

        WebElement buscador = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//input[@placeholder='Buscar por nombre, email, teléfono o empresa...']")));
        buscador.sendKeys(correoGenerado);
        buscador.sendKeys(Keys.ENTER);

        boolean contactoCreado = wait.until(driver -> driver.getPageSource().contains(correoGenerado));
        assertTrue("El contacto no se creó correctamente", contactoCreado);

        System.out.println("✅ El contacto se encontró correctamente con el correo: " + correoGenerado);
    }

    public String generarCorreoAleatorio() {
        String dominio = "@qa.defontana.com";
        String base = "usuario" + new Random().nextInt(100000);
        return base + dominio;
    }

    public void esperarSpinnerDesaparecer() {
        try {
            By spinner = By.cssSelector(".MuiCircularProgress-root");
            wait.until(ExpectedConditions.invisibilityOfElementLocated(spinner));
        } catch (TimeoutException e) {
            System.out.println("Spinner no desapareció a tiempo.");
        }
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit(); // Activa el quit para liberar recursos en CodeBuild
        }
    }
}
