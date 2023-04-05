package JustWork.server;


import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.token.Sha512DigestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = ServerApplication.class)
@RunWith(SpringRunner.class)
@DirtiesContext
@Slf4j
public class BaseTest extends PostgreSQLTesting{

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeAll
    static void setup(@LocalServerPort int port) {
        log.info("Configure  rest assured {}", port);
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }


    @Test
    public void cleanTables() {
        List<String> result;
        String flywayTableName = "flyway_schema_history";
        result = jdbcTemplate.query(
                "SELECT table_name from INFORMATION_SCHEMA.tables WHERE table_schema LIKE 'public' AND table_name <> '" + flywayTableName + "'",
                (resultSet, i) -> resultSet.getString("table_name"));
        String truncateSentence = "TRUNCATE " + String.join(", ", result) + " RESTART IDENTITY";
        log.debug("[cleanTables] Borraremos contenido tablas con sentencia sql: " + truncateSentence);
        jdbcTemplate.execute(truncateSentence);
        log.debug("[cleanTables] Contenido tablas borrado");
    }

    private Random rand=new Random();

    public int azarMinMax(int min,int max){
        return min+azarPositivo(max-min);
    }

    public int azarPositivo(int max){
        int resp= rand.nextInt(max);
        if (resp<=0) resp = -resp;
        return resp;
    }

    public int azar(int max){
        return rand.nextInt(max);
    }

    public boolean azarBoolean(){
        return rand.nextBoolean();
    }

    public String apellidoAzar(){
        String[] apellidos = {"Soto","Mendez","Caves","Mambe","Quijada","Perez","Cruz","Rojas"};
        return apellidos[azarMinMax(0,apellidos.length)];
    }

    public String nombreAzar(){
        String[] apellidos = {"Juan","Alan","Christian","Sebastian","Paulina","Johanna","Claudia","Claudio","Sergio","Felipe","Patricio","Leo","Marcelo","Hellen"};
        return apellidos[azarMinMax(0,apellidos.length)];
    }

    public String palabraAzar(){
        String[] textos = {"lorem","ipsum","de","a","la","verdem","comitus","habemus"};
        return textos[azarMinMax(0,textos.length)];
    }

    public double azarDouble() {
        return (double) rand.nextInt()/100;
    }

    public String azarCelular(){
        String respuesta="9";
        while (respuesta.length()<9){
            int numero = rand.nextInt();
            if (numero<0) numero=-numero;
            respuesta+=""+(numero%10);
        }
        return respuesta;
    }

    public String azarNombreApellido(){
        return nombreAzar()+" "+apellidoAzar();
    }

    public String azarTexto(int largoMaximo) {
        String resp ="";
        int maximo = largoMaximo/5;
        for (int i=0;i<maximo;++i){
            resp+=palabraAzar()+" ";
        }
        while (resp.length()>largoMaximo) {
            resp=resp.substring(0,largoMaximo);
        }
        return resp;
    }


}
