package no.sparkfun;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
//import redis.clients.jedis.Jedis;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.google.common.net.HttpHeaders.USER_AGENT;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class SecureRest {

    public static Logger log = LoggerFactory.getLogger(SecureRest.class);

    public static String redishost = "10.36.195.229";

    public static void main(String[] args) throws Exception {

        //secure("/programmering/sparkfun/src/main/resources/key/keystore.jks", "_Salty29", null, null);
        Path file = Paths.get("/tmp/userResponse.txt");

        port(8080);

        get("/hello", (req, res) -> hello());

        get("/error", (req, res) -> error());

        get("/redisread", (req, res) -> redisread());

        get("/rediswrite", (req, res) -> rediswrite());

        get("/slow", (req, res) -> slow());

        get("/callslow", (req, res) -> callslow());


        //post("/userResponse", "application/json", (req, res) -> "200 Ok");
        post("/userResponse", (req, res) -> {
            List<String> lines = new ArrayList<>();
            lines.add(req.body());
            Files.write(file, lines, UTF_8, APPEND, CREATE);
            return "200 Ok";
        });
    }

    private static String slow() {
        try {
            Thread.sleep(310000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "finished";
    }

    private static String callslow() {
        String url = "http://sparkfun-frodetest.test-dc.corp.telenor.no/slow";

        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println(response.toString());
        } catch(Exception e){
            throw new RuntimeException(e);
        }
        return "finished";
    }

    private static String rediswrite() {
        Jedis jedis = new Jedis(redishost);
        jedis.auth("nHBhFnkuPcIn8GAs");
        Random r = new Random();
        jedis.set("foo", "bar "+r.nextInt());
        return jedis.get("foo");
    }

    private static String redisread() {
        Jedis jedis = new Jedis(redishost);
        jedis.auth("nHBhFnkuPcIn8GAs");
        return jedis.get("bar");
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
