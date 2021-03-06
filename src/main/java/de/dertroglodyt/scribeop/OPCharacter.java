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
public class OPCharacter {

    private static final String URL_INDEX = "http://api.obsidianportal.com/v1/campaigns/$id/characters.json";
    private static final String URL_CHARACTER = "http://api.obsidianportal.com/v1/campaigns/$id/characters/$charID.json";
    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    // public fields
    public final String id;
    public final String name;
    public final String slug;
    public final String characterUrl;
    public final String avatarUrl;
    public final OPCampaignMini campaign;
    public final String visibility;
    public final Date createdAt;
    public final Date updatedAt;
    // availability depends on visibility
    public final OPUserMini author;
    public final boolean isPlayerCharacter;
    public final boolean isGameMasterOnly;
    public final OPDynamicSheetTemplateMini dynamicSheetTemplate;
    public final OPDynamicSheetData dynamicSheet;
    public final String description;
    public final String descriptionHtml;
    public final String bio;
    public final String bioHtml;
    public final String[] tags;
    // available only to GM and co-GMs
    public final String gameMasterInfo;
    public final String gameMasterInfoMarkup;

    public OPCharacter(OPService service, String campaignID, String characterID) throws JSONException, ParseException {
        this(new JSONObject(service.get(URL_CHARACTER.replace("$id", campaignID).replace("$charID", characterID)).getBody()));
    }

    public OPCharacter(JSONObject json) throws JSONException, ParseException {
        super();
        System.out.println(json);
        id = json.getString("id");
        name = json.getString("name");
        slug = json.getString("slug");
        characterUrl = json.getString("character_url");
        avatarUrl = json.getStringOrNull("avatar_url");
        if (json.has("campaign") && !json.isNull("campaign")) {
            campaign = new OPCampaignMini(json.getJSONObject("campaign"));
        } else {
            campaign = null;
        }
        if (json.has("visibility")) {
            visibility = json.getString("visibility");
        } else {
            visibility = "public";
        }
        createdAt = df.parse(json.getString("created_at"));
        updatedAt = df.parse(json.getString("updated_at"));
        author = new OPUserMini(json.getJSONObject("author"));
        isPlayerCharacter = json.getBoolean("is_player_character");
        isGameMasterOnly = json.getBoolean("is_game_master_only");
        if (json.has("dynamic_sheet_template") && !json.isNull("dynamic_sheet_template")) {
            dynamicSheetTemplate = new OPDynamicSheetTemplateMini(json.getJSONObject("dynamic_sheet_template"));
        } else {
            dynamicSheetTemplate = null;
        }
        if (json.has("dynamic_sheet") && !json.isNull("dynamic_sheet")) {
            dynamicSheet = new OPDynamicSheetData(json.getJSONObject("dynamic_sheet"));
        } else {
            dynamicSheet = null;
        }
        if (json.has("description") && !json.isNull("description")) {
            description = json.getString("description");
        } else {
            description = null;
        }
        if (json.has("descriptionHtml") && !json.isNull("descriptionHtml")) {
            descriptionHtml = json.getString("descriptionHtml");
        } else {
            descriptionHtml = null;
        }
        if (json.has("bio") && !json.isNull("bio")) {
            bio = json.getString("bio");
        } else {
            bio = null;
        }
        if (json.has("bioHtml") && !json.isNull("bioHtml")) {
            bioHtml = json.getString("bioHtml");
        } else {
            bioHtml = null;
        }
        JSONArray jtags = json.getJSONArray("tags");
        tags = new String[jtags.length()];
        for (int i=0; i < jtags.length(); i++) {
            tags[i] = jtags.getString(i);
        }
        if (json.has("game_master_info") && !json.isNull("game_master_info")) {
            gameMasterInfo = json.getString("game_master_info");
        } else {
            gameMasterInfo = null;
        }
        if (json.has("game_master_info_markup") && !json.isNull("game_master_info_markup")) {
            gameMasterInfoMarkup = json.getString("game_master_info_markup");
        } else {
            gameMasterInfoMarkup = null;
        }
    }

    public static OPCharacter[] getIndex(OPService service, String campaignID) throws JSONException, ParseException {
        JSONArray json = new JSONArray(service.get(URL_INDEX.replace("$id", campaignID)).getBody());
        OPCharacter[] characters = new OPCharacter[json.length()];
        for (int i=0; i < json.length(); i++) {
            characters[i] = new OPCharacter(json.getJSONObject(i));
        }
        return characters;
    }

    public static OPCharacter create(OPService service, String campaignID, String authorID
            , String name, String tagline, String description, String bio, String gameMasterInfo
            , boolean isGMOnly, String dstTemplateID, OPDynamicSheetData dsData
            ) throws JSONException, ParseException {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("author_id", authorID);
        json.put("tagline", tagline);
        json.put("description", description);
        json.put("bio", bio);
        json.put("game_master_info", gameMasterInfo);
        json.put("is_game_master_only", isGMOnly);
        json.put("dynamic_sheet_template_id", dstTemplateID);
        json.put("dynamic_sheet", (dsData != null)?dsData.asJSONObject():null);
        JSONObject wp = new JSONObject();
        wp.put("character", json);
        String payload = wp.toString();
//        System.out.println(payload);
        Response response = service.post(URL_INDEX.replace("$id", campaignID), payload);
        if (!response.isSuccessful()) {
            OPResponseError error = new OPResponseError(response);
//            System.out.println(error.toString());
            throw new UnknownError(error.toString());
        }
        return new OPCharacter(new JSONObject(response.getBody()));
    }

    public static OPCharacter update(OPService service, String campaignID, String characterID
            , String bio, boolean isGMOnly, String gameMasterInfo, OPDynamicSheetData dsData
            ) throws JSONException, ParseException {
        JSONObject json = new JSONObject();
        json.put("bio", bio);
        json.put("game_master_info", gameMasterInfo);
        json.put("is_game_master_only", isGMOnly);
        json.put("dynamic_sheet", (dsData != null)?dsData.asJSONObject():null);
        JSONObject wp = new JSONObject();
        wp.put("character", json);
        String payload = wp.toString();
//        System.out.println(payload);
        Response response = service.put(URL_CHARACTER.replace("$id", campaignID).replace("$charID", characterID), payload);
        if (!response.isSuccessful()) {
            OPResponseError error = new OPResponseError(response);
//            System.out.println(error.toString());
            throw new UnknownError(error.toString());
        }
        return new OPCharacter(new JSONObject(response.getBody()));
    }

    public static Response delete(OPService service, String campaignID, String characterID) {
        return service.delete(URL_CHARACTER.replace("$id", campaignID).replace("$charID", characterID), "");
    }

    @Override
    public String toString() {
        return name;
    }

    public String toLongString() {
        StringBuilder sb = new StringBuilder(name
                + "\n ID: " + id
                + "\n URL: " + characterUrl
                + "\n Slug: " + slug
                + "\n AvatarURL: " + avatarUrl
                + "\n Campaign: " + ((campaign != null)?campaign.toLongString():null)
                + "\n Visibility: " + visibility
                + "\n Created: " + createdAt
                + "\n Updated: " + updatedAt
                + "\n Author: " + author
                + "\n isPLayerCharacter: " + isPlayerCharacter
                + "\n isGMOnly: " + isGameMasterOnly
                + "\n DST: " + ((dynamicSheetTemplate != null)?dynamicSheetTemplate.toLongString():null)
                + "\n DS: " + ((dynamicSheet != null)?dynamicSheet.toLongString():null)
                + "\n Description: " + description
                + "\n DescriptionHtml: " + descriptionHtml
                + "\n Bio: " + bio
                + "\n BioHtml: " + bioHtml
                + "\n GMInfo: " + gameMasterInfo
                + "\n GMInfoMarkup: " + gameMasterInfoMarkup
                );
        sb.append("\n Tags: ").append(tags.length);
        for (String s : tags) {
            sb.append("\n  ").append(s);
        }
        return sb.toString();
    }

}
