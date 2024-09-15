package de.dandy.trivyrestapi.trivy.command;

/**
 * Command to Update the trivy DB
 */
public record UpdateJavaDbCommand() implements TrivyCommand {

    @Override
    public String asString() {
        return "image --download-java-db-only";
    }
}
