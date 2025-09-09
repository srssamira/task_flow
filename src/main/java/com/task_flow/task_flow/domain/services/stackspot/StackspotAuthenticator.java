package com.task_flow.task_flow.domain.services.stackspot;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class StackspotAuthenticator {

    private static final String CLIENT_ID = "4869b6df-aed6-4f04-a55c-93bc7a8113fb";
    private static final String CLIENT_KEY = "3SEH5Z4HLNqMs3e3pjS5tWxr58jbIBy1lZ30F1X8lnzDY8r7dKS488fi9UsM3IIr";
    private static final String REALM = "zup";

    /**
     * Gera os headers de autenticação para requisições StackSpot.
     */
    public HttpHeaders getAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-stackspot-client-id", CLIENT_ID);
        headers.set("x-stackspot-client-key", CLIENT_KEY);
        headers.set("x-stackspot-realm", REALM);
        headers.set("Content-Type", "application/json");
        return headers;
    }
}