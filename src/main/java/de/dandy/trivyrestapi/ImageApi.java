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

/**
 * Scan docker images with trivy
 */
@RestController
@RequestMapping("/image")
public class ImageApi {


    private final Trivy trivy;

    public ImageApi(Trivy trivy) {
        this.trivy = trivy;
    }

    /**
     * Scan an image with trivy with format option.
     *
     * @param image  image name
     * @param format Supported options <code>json</code> and <code>cyclonedx</code>
     * @return trivy output
     */
    @GetMapping(value = "{*image}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> formatedScan(@PathVariable String image, @RequestParam(required = false) String format) {

        // as {*image} also captures the leading '/' it has to be cut off
        image = image.startsWith("/") ? image.substring(1) : image;

        String saveFormat = "json";
        if (format != null) {
            if (format.equalsIgnoreCase("json") ||
                    format.equalsIgnoreCase("cyclonedx")) {
                saveFormat = format.toLowerCase();
            }
        }

        TrivyResult result = trivy.call(new ImageCommand(image, saveFormat));

        return ResponseEntity.status(result.isSuccess() ? HttpStatus.OK.value() : HttpStatus.CONFLICT.value()).body(result.getResult());
    }

    @GetMapping(value = "has-vulns/{*image}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> hasVulns(@PathVariable String image, @RequestParam(required = false) String severity) {
        // as {*image} also captures the leading '/' it has to be cut off
        image = image.startsWith("/") ? image.substring(1) : image;

        TrivyResult result = trivy.call(new ImageCommand(image, "json"));

        return ResponseEntity.status(result.isSuccess() ? HttpStatus.OK.value() : HttpStatus.CONFLICT.value()).body(result.hasVulns());
    }

}
