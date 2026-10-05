package it.coop.ccno.idm;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class IdmApplication {

    private IdmApplication() {
    }

    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
        String environment = System.getenv().getOrDefault("APP_ENV", "local");

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", exchange -> handle(exchange, 200, Responses.home(environment), environment));
        server.createContext("/health", exchange -> handle(exchange, 200, Responses.health(), environment));
        server.setExecutor(Executors.newFixedThreadPool(4));
        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> server.stop(1)));
        System.out.printf("idm-app test started on port %d in environment %s%n", port, environment);
    }

    private static void handle(HttpExchange exchange, int status, String body, String environment) throws IOException {
        long startedAt = System.nanoTime();
        int loggedStatus = status;

        try {
            respond(exchange, status, body);
        } catch (IOException exception) {
            loggedStatus = 500;
            throw exception;
        } finally {
            long durationMillis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startedAt);
            String remoteAddress = exchange.getRemoteAddress().getAddress().getHostAddress();

            System.out.println(AccessLog.format(
                    exchange.getRequestMethod(),
                    exchange.getRequestURI().getPath(),
                    environment,
                    loggedStatus,
                    durationMillis,
                    remoteAddress));
        }
    }

    private static void respond(HttpExchange exchange, int status, String body) throws IOException {
        byte[] payload = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(status, payload.length);
        try (var responseBody = exchange.getResponseBody()) {
            responseBody.write(payload);
        }
    }
}
