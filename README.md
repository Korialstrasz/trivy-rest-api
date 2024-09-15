# Trivy-REST-Api

This is a simple Spring Boot Application that enables the `trivy image` command to be call via REST.

# API

| Endpoint                              | Example                             | Description                                                                           |
|---------------------------------------|:------------------------------------|---------------------------------------------------------------------------------------|
| /image/\<image name>                  | /image/alpine:3.10                  | Scans the image with the given name and returns the trivy json response.              |
| /image/\<image name>?format=\<format> | /image/alpine:3.10?foramt=cyclonedx | The with the query param `format` the return can be change from `json` to `cyclonedx` |                                                                   
| /image/has-vulns/\<image name>        | /image/has-vuln/alpine:3.10         | Scans the image and returns `true` or `false` if trivy found vulnerabilities          |

Up next:
All endpoints support the query params `severity` and `ignore-unfixed`, which behave like the trivy cli params of the
same name.

* `severity` only reports vulnerabilities with a severity equal or higher to the supplied value.
* `ingore-unfixed`only reports vulnerabilities that have a known fixed version.
