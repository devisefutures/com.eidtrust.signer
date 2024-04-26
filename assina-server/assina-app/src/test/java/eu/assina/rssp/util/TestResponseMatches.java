package eu.assina.rssp.util;

import eu.assina.common.ApiError;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Utility methods for matching responses in controller tests
 */
public class TestResponseMatches {

  /**
   * Returns a matcher for asserting a JSON response as valid Assina standard error response.
   *
   * @return a result matcher for error responses
   */
  public static ResultMatcher validErrorResponse() {
    return validateCSCErrorResponse(null);
  }


  /**
   * Returns a matcher for asserting a JSON response as well formed CSC API Error.
   *
   * From section 10.1 of CSC spec 1.0.4.0:
   *   Example error:
   *   {
   *     "error": "invalid_request",
   *     "error_description": "The access token is not valid"
   *   }
   *
   * @param expectedError the expected error enum; if null is passed, we assert that it is NOT null
   *
   * @return a result matcher for error responses
   */
  public static ResultMatcher validateCSCErrorResponse(ApiError expectedError) {
    return ResultMatcher.matchAll(
        jsonPath("$.error", expectedError == null ? notNullValue() : is(expectedError.getCode())),
        jsonPath("$.error_description", notNullValue())
    );
  }
}
