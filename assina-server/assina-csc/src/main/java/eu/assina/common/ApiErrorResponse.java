package eu.assina.common;

/**
 * Error response that matches the format defined in the CSC spec 1.0.4.0
 *
 * Example:
 *   {
 *     "error": "invalid_request",
 *     "error_description": "The access token is not valid"
 *  }
 */
public class ApiErrorResponse
{
    private String error;
    private String error_description;

    public ApiErrorResponse(String error)
    {
        this(error, "");
    }

    public ApiErrorResponse(String error, String error_description)
    {
        this.error = error;
        this.error_description = error_description;
    }

    public String getError()
    {
        return error;
    }

    public void setError(String error)
    {
        this.error = error;
    }

    /**
     * Unconventional method name deliberately matches the CSC specification
     * @return
     */
    public String getError_description()
    {
        return error_description;
    }

    public void setError_description(String error_description)
    {
        this.error_description = error_description;
    }
}
