package JustWork.server.login;

import JustWork.server.BaseTest;
import JustWork.server.PostgreSQLTesting;
import JustWork.server.ServerApplication;
import JustWork.server.dto.LoginRequestDTO;
import com.google.gson.Gson;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = ServerApplication.class)
@ContextConfiguration(initializers = PostgreSQLTesting.Initializer.class)
@DirtiesContext
@Slf4j
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
@Testcontainers
public class LoginTest extends BaseTest {

    LoginRequestDTO REQUEST;

    @BeforeEach
    public void beforeEach(){
        log.info("Se limpian tablas");
    }

    @Test
    public void checkTestContainer(){
        assertTrue(postgresqlContainer.isRunning());
    }


    @Test
    public void test01_ttest(){

        String metodo = new Object(){}.getClass().getEnclosingMethod().getName();
        String cabecera = "["+metodo+"] ";
        log.info("----TEST---- " + cabecera);
        setupRequest();
        String body = new Gson().toJson(REQUEST);
        log.debug(cabecera + "Test-Body: " + body);
        given()
                .contentType(ContentType.JSON)
                //.body(body)
                .when()
                .post("/app")
                .prettyPeek()
                .then()
                .statusCode(200)
                /*.body("success", equalTo(true))
                .body("msg", equalTo("Login exitoso"))*/
        ;
    }

    void setupRequest(){
        REQUEST = new LoginRequestDTO().setEmail("email").setPassword("pass");
    }
}
