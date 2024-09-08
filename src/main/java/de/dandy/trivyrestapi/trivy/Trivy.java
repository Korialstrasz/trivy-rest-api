package de.dandy.trivyrestapi.trivy;

import de.dandy.trivyrestapi.trivy.command.TrivyCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Service to call the trivy cli
 */
@Service
public class Trivy {

    private static final Logger LOGGER = LoggerFactory.getLogger(Trivy.class);

    /**
     * Call the trivy CLI with the supplied command.
     *
     * @param command Command to be executed
     * @return return the trivy console output
     * @throws IOException
     * @throws InterruptedException
     */
    public TrivyResult call(TrivyCommand command) throws InterruptedException {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("sh", "-c", "trivy " + command.asString());
            processBuilder.directory(new File(System.getProperty("user.home")));

            Process process = processBuilder.start();

            int returnCode = process.waitFor();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder result = new StringBuilder();

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line).append("\n");
            }

            return TrivyResult.of(result.toString(), returnCode != 0);
        } catch (IOException e) {
            LOGGER.error("Could not run trivy command. %s".formatted(command.asString()), e);
            return TrivyResult.failed(e.getMessage());
        }
    }

}