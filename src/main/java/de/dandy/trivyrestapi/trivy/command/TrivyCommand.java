package de.dandy.trivyrestapi.trivy.command;

/**
 * Interface for Commands
 */
public sealed interface TrivyCommand permits UpdateCommand, ImageCommand {

    /**
     * Transforms the Command into a string that can be supplied to the "trivy" cli
     *
     * @return cli string
     */
    String asString();
}
