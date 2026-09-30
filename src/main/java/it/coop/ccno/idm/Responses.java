package it.coop.ccno.idm;

final class Responses {

    private Responses() {
    }

    static String home(String environment) {
        return "{\"application\":\"idm-app\",\"environment\":\"" + environment + "\"}";
    }

    static String health() {
        return "{\"status\":\"UP\"}";
    }
}
