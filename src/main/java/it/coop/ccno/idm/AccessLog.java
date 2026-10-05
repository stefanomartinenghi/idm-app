package it.coop.ccno.idm;

final class AccessLog {

    private AccessLog() {
    }

    static String format(
            String method,
            String path,
            String environment,
            int status,
            long durationMillis,
            String remoteAddress) {

        return "http_request"
                + " method=" + method
                + " path=" + path
                + " environment=" + environment
                + " status=" + status
                + " duration_ms=" + durationMillis
                + " remote=" + remoteAddress;
    }
}
