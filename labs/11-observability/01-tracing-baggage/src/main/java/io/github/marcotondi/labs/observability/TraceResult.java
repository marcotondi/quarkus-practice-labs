package io.github.marcotondi.labs.observability;

/**
 * DTO di risposta. NON modificare.
 */
public class TraceResult {

    public String requestId;
    public String traceId;

    public TraceResult() {
    }

    public TraceResult(String requestId, String traceId) {
        this.requestId = requestId;
        this.traceId = traceId;
    }
}