package de.dandy.trivyrestapi;

import de.dandy.trivyrestapi.trivy.Trivy;
import de.dandy.trivyrestapi.trivy.TrivyResult;
import de.dandy.trivyrestapi.trivy.command.ImageCommand;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Scan docker images with trivy
 */
@RestController
@RequestMapping("/image")
public class ImageApi {


    private final Trivy caller;

    public ImageApi(Trivy caller) {
        this.caller = caller;
    }

    /**
     * Default Scan of an Image
     * <ul>
     *     <li>Format: JSON</li>
     *     <li>Report: All Vulns</li>
     * </ul>
     *
     * @param image Name of the image
     * @return trivy output
     * @throws IOException
     * @throws InterruptedException
     */
    @GetMapping(value = "/{image}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> scan(@PathVariable String image) throws IOException, InterruptedException {

        TrivyResult result = caller.call(new ImageCommand(image, "json"));

        return ResponseEntity.status(result.isSuccess() ? 200 : 409).body(result.getResult());
    }

    /**
     * Scan an image with trivy with format option.
     *
     * @param image  image name
     * @param format Supported options <code>json</code> and <code>cyclonedx</code>
     * @return trivy output
     * @throws InterruptedException
     */
    @GetMapping(value = "/{image}/{format}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> formatedScan(@PathVariable String image, @PathVariable String format) throws InterruptedException {

        String saveFormat = "json";
        if (format != null) {
            if (format.equalsIgnoreCase("json") ||
                    format.equalsIgnoreCase("cyclonedx")) {
                saveFormat = format.toLowerCase();
            }
        }

        TrivyResult result = caller.call(new ImageCommand(image, saveFormat));

        return ResponseEntity.status(result.isSuccess() ? HttpStatus.OK.value() : HttpStatus.CONFLICT.value()).body(result.getResult());
    }

    @GetMapping(value = "has-vulns/{image}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> hasVulns(@PathVariable String image) throws InterruptedException {
        TrivyResult result = caller.call(new ImageCommand(image, "json"));

        return ResponseEntity.status(result.isSuccess() ? HttpStatus.OK.value() : HttpStatus.CONFLICT.value()).body(result.hasVulns());
    }

}
