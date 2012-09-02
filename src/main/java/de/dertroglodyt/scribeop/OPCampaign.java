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

/**
 *
 * @author dertroglodyt
 */
public class OPCampaign {

    private static final String URL = "http://api.obsidianportal.com/v1/campaigns/$id.json";
    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    // public fields
    public final String id;
    public final String name;
    public final String slug;
    public final String campaignUrl;
    public final String visibility;
    public final OPUserMini gameMaster;
    public final Date createdAt;
    public final Date updatedAt;
    // availability depends on visibility
    public final String bannerImageUrl;
    public final String playStatus;
    public final OPUserMini[] players;
    public final OPUserMini[] fans;
    public final boolean lookingForPlayers;
    public final OPLocation location;

    public OPCampaign(OPService service, String campaignID) throws JSONException, ParseException {
        this(new JSONObject(service.get(URL.replace("$id", campaignID)).getBody()));
    }

    public OPCampaign(JSONObject json) throws JSONException, ParseException {
        super();
//        System.out.println(json);
        id = json.getString("id");
        name = json.getString("name");
        slug = json.getString("slug");
        campaignUrl = json.getString("campaign_url");
        visibility = json.getString("visibility");
        bannerImageUrl = json.getStringOrNull("banner_image_url");
        gameMaster = new OPUserMini(json.getJSONObject("game_master"));
        createdAt = df.parse(json.getString("created_at"));
        updatedAt = df.parse(json.getString("updated_at"));
        playStatus = json.getString("play_status");
        JSONArray pl = json.getJSONArray("players");
        players = new OPUserMini[pl.length()];
        for (int i = 0; i < pl.length(); i++) {
            players[i] = new OPUserMini(pl.getJSONObject(i));
        }
        JSONArray fa = json.getJSONArray("fans");
        fans = new OPUserMini[fa.length()];
        for (int i = 0; i < fa.length(); i++) {
            fans[i] = new OPUserMini(fa.getJSONObject(i));
        }
        lookingForPlayers = json.getBoolean("looking_for_players");
        location = new OPLocation(json.getJSONObject("location"));
    }

    @Override
    public String toString() {
        return name;
    }

    public String toLongString() {
        StringBuilder sb = new StringBuilder(name
                + "\n ID: " + id
                + "\n URL: " + campaignUrl
                + "\n Slug: " + slug
                + "\n Visibility: " + visibility
                + "\n BannerURL: " + bannerImageUrl
                + "\n GameMaster: " + gameMaster.toLongString()
                + "\n Created: " + createdAt
                + "\n Updated: " + updatedAt
                + "\n Status: " + playStatus
                + "\n LookingForPlayers: " + lookingForPlayers
                + "\n Location: " + location);
        sb.append("\n Players: ").append(players.length);
        for (OPUserMini p : players) {
            sb.append("\n  ").append(p.toLongString());
        }
        sb.append("\n Fans: ").append(fans.length);
        for (OPUserMini f : fans) {
            sb.append("\n  ").append(f.toLongString());
        }
        return sb.toString();
    }
}
