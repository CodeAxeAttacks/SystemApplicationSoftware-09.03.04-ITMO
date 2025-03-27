package ru.sayron.common.interaction;

import ru.sayron.common.data.Organization;

import java.io.Serializable;
import java.util.NavigableSet;

/**
 * Class for get response value.
 */
public class Response implements Serializable {
    NavigableSet<Organization> organizationsCollection;
    private ResponseCode responseCode;
    private String responseBody;
    private String[] responseBodyArgs;

    public Response(ResponseCode responseCode, String responseBody, String[] responseBodyArgs,
                    NavigableSet<Organization> marinesCollection) {
        this.responseCode = responseCode;
        this.responseBody = responseBody;
        this.organizationsCollection = marinesCollection;
        this.responseBodyArgs = responseBodyArgs;
    }

    /**
     * @return Response сode.
     */
    public ResponseCode getResponseCode() {
        return responseCode;
    }

    /**
     * @return Response body.
     */
    public String getResponseBody() {
        return responseBody;
    }

    public String[] getResponseBodyArgs() {
        return responseBodyArgs;
    }


    /**
     * @return Marines collection last save.
     */
    public NavigableSet<Organization> getOrganizationsCollection() {
        return organizationsCollection;
    }

    @Override
    public String toString() {
        return "Response[" + responseCode + ", " + responseBody + "]";
    }
}
