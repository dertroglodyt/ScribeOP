/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.dertroglodyt.scribeop;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

/**
 *
 * @author dertroglodyt
 */
public class OPService {

    private final OAuthService mService;
    private final Token mAccessToken;

    static public OPService connect() {
        OAuthService service = new ServiceBuilder()
                .provider(ObsidianPortalApi.class)
                .apiKey("iqH1tk58qWVI1eeBLY6m")
                .apiSecret("J8OlNmuezi8SvxJ3W3eyEbT9GeZxmf9B45f98tQI")
//                .scope(SCOPE)
//                .debug()
                .build();
        // TODO: authenticate if connect error
//        Token requestToken = service.getRequestToken();
//        System.out.println("Request token: " + requestToken);
//        System.out.println(ObsidianPortalApi.AUTHORIZATION_URL.replaceAll("%s", requestToken.getToken()));
//        System.out.print(">>");
//        Scanner in = new Scanner(System.in);
//        Verifier verifier = new Verifier(in.nextLine());
//        System.out.println();
//        Token accessToken = service.getAccessToken(requestToken, verifier);
//        System.out.println("Access token: " + accessToken);

//        Token requestToken = new Token("53xLlaBaSmHDhgYtaI6L", "Shr8CzM2B0vJUC1QTb3YncqF16b1QTmz3DU2a9PC");
        Token accessToken = new Token("jlmZyxS6tnzWpCvZxJsa", "KYET24Pm5kaeh6TNHRDH4o2m8duTyjWLXO4PybwY");
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
