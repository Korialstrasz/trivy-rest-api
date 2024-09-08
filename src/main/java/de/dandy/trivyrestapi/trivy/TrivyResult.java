package de.dandy.trivyrestapi.trivy;

/**
 * Result of a trivy call
 */
public class TrivyResult {

    private final boolean success;
    private final boolean hasVulns;

    private final String result;

    private TrivyResult(boolean success, String result, boolean hasVulns) {
        this.success = success;
        this.result = result;
        this.hasVulns = hasVulns;
    }

    public static TrivyResult of(String trivyoutput, boolean hasVulns) {
        return new TrivyResult(true, trivyoutput, hasVulns);
    }

    public static TrivyResult failed(String message) {
        return new TrivyResult(false, message, false);
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean hasVulns() {
        return hasVulns;
    }

    public String getResult() {
        return result;
    }
}
