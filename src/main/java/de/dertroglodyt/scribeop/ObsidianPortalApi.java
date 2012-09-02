/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.dertroglodyt.scribeop;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.Token;
import org.scribe.model.Verb;

/**
 *
 * @author dertroglodyt
 */
public class ObsidianPortalApi extends DefaultApi10a {

    public static final String AUTHORIZATION_URL = "https://www.obsidianportal.com/oauth/authorize?oauth_token=%s";

    @Override
    public String getAccessTokenEndpoint() {
        return "https://www.obsidianportal.com/oauth/access_token";
    }

    @Override
    public String getRequestTokenEndpoint() {
        return "https://www.obsidianportal.com/oauth/request_token";
    }

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.GET;
    }

    @Override
    public Verb getRequestTokenVerb() {
        return Verb.GET;
    }

    @Override
    public String getAuthorizationUrl(Token requestToken) {
        return String.format(AUTHORIZATION_URL, requestToken.getToken());
    }
}
