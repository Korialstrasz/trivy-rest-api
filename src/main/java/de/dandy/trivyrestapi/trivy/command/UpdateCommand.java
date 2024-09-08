package de.dandy.trivyrestapi.trivy.command;

/**
 * Command to Update the trivy DB
 */
public record UpdateCommand() implements TrivyCommand {

    @Override
    public String asString() {
        return "image --download-db-only";
    }
}
