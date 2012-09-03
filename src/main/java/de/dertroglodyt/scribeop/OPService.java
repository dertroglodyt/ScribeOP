/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.dertroglodyt.scribeop;

import java.util.Scanner;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

/**
 *
 * @author dertroglodyt
 */
public class OPService {

    private final OAuthService mService;
    public final Token mAccessToken;

    static public OPService firstConnect(String apiKey, String apiSecret) {
        OAuthService service = new ServiceBuilder()
                .provider(ObsidianPortalApi.class)
                .apiKey(apiKey)
                .apiSecret(apiSecret)
//                .scope(SCOPE)
//                .debug()
                .build();
        Token requestToken = service.getRequestToken();
//        System.out.println("Request token: " + requestToken);
        System.out.println(ObsidianPortalApi.AUTHORIZATION_URL.replaceAll("%s", requestToken.getToken()));
        System.out.print(">>");
        Scanner in = new Scanner(System.in);
        Verifier verifier = new Verifier(in.nextLine());
        System.out.println();
        Token accessToken = service.getAccessToken(requestToken, verifier);
        System.out.println("Access token: " + accessToken);

        return new OPService(service, accessToken);
    }

    static public OPService connect(String apiKey, String apiSecret, Token accessToken) {
        OAuthService service = new ServiceBuilder()
                .provider(ObsidianPortalApi.class)
                .apiKey(apiKey)
                .apiSecret(apiSecret)
//                .scope(SCOPE)
//                .debug()
                .build();
        return new OPService(service, accessToken);
    }

    public OPService(final OAuthService service, final Token accessToken) {
        super();
        mService = service;
        mAccessToken = accessToken;
    }

    public Response get(String url) {
        OAuthRequest request = new OAuthRequest(Verb.GET, url);
        mService.signRequest(mAccessToken, request);
        return request.send();
    }

    public Response post(String url, String payload) {
        OAuthRequest request = new OAuthRequest(Verb.POST, url);
        request.addHeader("Content-Length", Integer.toString(payload.length()));
//        request.addHeader("Content-Type", "application/json");
        request.addHeader("Content-Type", "text/html");
        request.addPayload(payload);
//        System.out.println("POST-request: " + request.getBodyContents());
        mService.signRequest(mAccessToken, request);
//        System.out.println("POST-header: " + request.getHeaders());
        return request.send();
    }

    public Response put(String url, String payload) {
        OAuthRequest request = new OAuthRequest(Verb.PUT, url);
        request.addHeader("Content-Length", Integer.toString(payload.length()));
//        request.addHeader("Content-Type", "application/json");
        request.addHeader("Content-Type", "text/html");
        request.addPayload(payload);
//        System.out.println("PUT-request: " + request.getBodyContents());
        mService.signRequest(mAccessToken, request);
        return request.send();
    }

    public Response delete(String url, String payload) {
        OAuthRequest request = new OAuthRequest(Verb.DELETE, url);
        request.addHeader("Content-Length", Integer.toString(payload.length()));
//        request.addHeader("Content-Type", "application/json");
        request.addHeader("Content-Type", "text/html");
        request.addPayload(payload);
//        System.out.println("DELETE-request: " + request.getBodyContents());
        mService.signRequest(mAccessToken, request);
        return request.send();
    }

}
