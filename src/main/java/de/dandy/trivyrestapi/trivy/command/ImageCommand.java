package de.dandy.trivyrestapi.trivy.command;

public record ImageCommand(String target, String format) implements TrivyCommand {

    @Override
    public String asString() {
        return "image --skip-db-update --format %s -q %s".formatted(format, target);
    }
}
