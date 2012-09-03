/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.dertroglodyt.scribeop;

import de.dertroglodyt.scribeop.json.JSONArray;
import de.dertroglodyt.scribeop.json.JSONException;
import de.dertroglodyt.scribeop.json.JSONObject;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.scribe.model.Response;

/**
 *
 * @author dertroglodyt
 */
public class OPWikiPage {

    private static final String URL_INDEX = "http://api.obsidianportal.com/v1/campaigns/$id/wikis.json";
    private static final String URL_PAGE = "http://api.obsidianportal.com/v1/campaigns/$id/wikis/$page.json";
    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    // public fields
    public final String id;
    public final String slug;
    public final String name;
    public final String wikiPageUrl;
    public final OPCampaignMini campaign;
    public final Date createdAt;
    public final Date updatedAt;
    public final String type;  // 'WikiPage'|'Post'
    public final boolean isGameMasterOnly;
    public final String[] tags;
    // availibility depends on visibility
    public final String body;
    public final String bodyHtml;
    // For adventure log ('Post') only.
    public final String postTitle;
    public final String postTagline;
    public final Date postTime;
    // GameMaster only
    public final String gameMasterInfo;
    public final String gameMasterInfoMarkup;

    public OPWikiPage(OPService service, String campaignID, String pageID) throws JSONException, ParseException {
        this(new JSONObject(service.get(URL_PAGE.replace("$id", campaignID).replace("$page", pageID)).getBody()));
    }

    public OPWikiPage(JSONObject json) throws JSONException, ParseException {
        super();
//        System.out.println(json);
        id = json.getString("id");
        slug = json.getString("slug");
        name = json.getString("name");
        wikiPageUrl = json.getString("wiki_page_url");
        campaign = new OPCampaignMini(json.getJSONObject("campaign"));
        createdAt = df.parse(json.getString("created_at"));
        updatedAt = df.parse(json.getString("updated_at"));
        type = json.getString("type");
        isGameMasterOnly = json.getBoolean("is_game_master_only");
        JSONArray jtags = json.getJSONArray("tags");
        tags = new String[jtags.length()];
        for (int i=0; i < jtags.length(); i++) {
            tags[i] = jtags.getString(i);
        }
        if (isPost()) {
            body = null;
            bodyHtml = null;
            postTitle = json.getStringOrNull("post_title");
            postTagline = json.getStringOrNull("post_tagline");
            postTime = df.parse(json.getString("post_time"));
        } else {
            // may be empty in listings
            if (json.has("body")) {
                body = json.getStringOrNull("body");
            } else {
                body = null;
            }
            if (json.has("body_html")) {
                bodyHtml = json.getStringOrNull("body_html");
            } else {
                bodyHtml = null;
            }
            postTitle = null;
            postTagline = null;
            postTime = null;
        }
        if (isGameMasterOnly) {
            // may be empty in listings
            if (json.has("game_master_info")) {
                gameMasterInfo = json.getStringOrNull("game_master_info");
            } else {
                gameMasterInfo = null;
            }
            if (json.has("game_master_info_markup")) {
                gameMasterInfoMarkup = json.getStringOrNull("game_master_info_markup");
            } else {
                gameMasterInfoMarkup = null;
            }
        } else {
            gameMasterInfo = null;
            gameMasterInfoMarkup = null;
        }
    }

    public static OPWikiPage[] getIndex(OPService service, String campaignID) throws JSONException, ParseException {
        JSONArray json = new JSONArray(service.get(URL_INDEX.replace("$id", campaignID)).getBody());
        OPWikiPage[] pages = new OPWikiPage[json.length()];
        for (int i=0; i < json.length(); i++) {
            pages[i] = new OPWikiPage(json.getJSONObject(i));
        }
        return pages;
    }

    public static OPWikiPage create(OPService service, String campaignID, String name, String body
            , boolean isGMOnly, String gameMasterInfo) throws JSONException, ParseException {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("body", body);
        json.put("is_game_master_only", isGMOnly);
        json.put("game_master_info", gameMasterInfo);
        JSONObject wp = new JSONObject();
        wp.put("wiki_page", json);
        String payload = wp.toString();
//        System.out.println(payload);
        Response response = service.post(URL_INDEX.replace("$id", campaignID), payload);
        if (!response.isSuccessful()) {
            OPResponseError error = new OPResponseError(response);
//            System.out.println(error.toString());
            throw new UnknownError(error.toString());
        }
        return new OPWikiPage(new JSONObject(response.getBody()));
    }

    public static OPWikiPage update(OPService service, String campaignID, String pageID, String body
            , boolean isGMOnly, String gameMasterInfo) throws JSONException, ParseException {
        JSONObject json = new JSONObject();
        json.put("body", body);
        json.put("is_game_master_only", isGMOnly);
        json.put("game_master_info", gameMasterInfo);
        JSONObject wp = new JSONObject();
        wp.put("wiki_page", json);
        String payload = wp.toString();
//        System.out.println(payload);
        Response response = service.put(URL_PAGE.replace("$id", campaignID).replace("$page", pageID), payload);
        if (!response.isSuccessful()) {
            OPResponseError error = new OPResponseError(response);
//            System.out.println(error.toString());
            throw new UnknownError(error.toString());
        }
        return new OPWikiPage(new JSONObject(response.getBody()));
    }

    public static Response delete(OPService service, String campaignID, String pageID) {
        return service.delete(URL_PAGE.replace("$id", campaignID).replace("$page", pageID), "");
    }

    public final boolean isPost() {
        return ("Post".equals(type));
    }

    @Override
    public String toString() {
        return name;
    }

    public String toLongString() {
        StringBuilder sb = new StringBuilder(name
                + "\n ID: " +id
                + "\n Slug: " + slug
                + "\n Url: " + wikiPageUrl
                + "\n Campaign: " + campaign
                + "\n Created: " + createdAt
                + "\n Updated: " + updatedAt
                + "\n Type: " + type  // 'WikiPage'|'Post'
                + "\n GMOnly: " + isGameMasterOnly
                + "\n Tags:");
        for (String s : tags) {
            sb.append("\n   ").append(s);
        }
        sb.append("\n Body: " + body
                + "\n BodyHtml: " + bodyHtml
                + "\n PostTitle: " + postTitle
                + "\n PostTagline: " + postTagline
                + "\n PostTime: " + postTime
                + "\n GMInfo: " + gameMasterInfo
                + "\n GMInfoMarkup: " + gameMasterInfoMarkup);
        return sb.toString();
    }
}
