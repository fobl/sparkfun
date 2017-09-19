package no.sparkfun;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class SecureRest {

    public static Logger log = LoggerFactory.getLogger(SecureRest.class);

    public static void main(String[] args) throws Exception {


        //secure("/programmering/sparkfun/src/main/resources/key/keystore.jks", "_Salty29", null, null);
        Path file = Paths.get("/tmp/userResponse.txt");

        port(8080);

        get("/hello", (req, res) -> hello());

        get("/error", (req, res) -> error());


        //post("/userResponse", "application/json", (req, res) -> "200 Ok");
        post("/userResponse", (req, res) -> {
            List<String> lines = new ArrayList<>();
            lines.add(req.body());
            Files.write(file, lines, UTF_8, APPEND, CREATE);
            return "200 Ok";
        });
    }

    private static String hello(){
        log.info("Calling hello");
        return "Hello World";
    }

    private static String error(){
        log.error("Calling error");
        throw new RuntimeException("Calling /error url");
    }
}
