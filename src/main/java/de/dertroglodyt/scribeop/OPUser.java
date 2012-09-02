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
public class OPUser {

    private static final String URL = "http://api.obsidianportal.com/v1/users/$id.json";
    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    public final String id;
    public final String userName;
    public final String avatarImageUrl;
    public final String profileUrl;
    public final OPCampaignMini[] campaigns;
    public final boolean isAscendant;
    public final Date lastSeenAt;
    public final String utcOffset;
    public final String locale;
    public final Date createdAt;
    public final Date updatedAt;

    public OPUser(OPService service, String userID) throws JSONException, ParseException {
        this(new JSONObject(service.get(URL.replace("$id", (userID == null)?"me":userID)).getBody()));
    }

    public OPUser(JSONObject json) throws JSONException, ParseException {
        super();
        id = json.getString("id");
        userName = json.getString("username");
        avatarImageUrl = json.getString("avatar_image_url");
        profileUrl = json.getString("profile_url");
        JSONArray camp = json.getJSONArray("campaigns");
        campaigns = new OPCampaignMini[camp.length()];
        for (int i = 0; i < camp.length(); i++) {
            JSONObject jsonObj = camp.getJSONObject(i);
            campaigns[i] = new OPCampaignMini(jsonObj);
        }
        isAscendant = json.getBoolean("is_ascendant");
        lastSeenAt = df.parse(json.getString("last_seen_at"));
        utcOffset = json.getString("utc_offset");
        locale = json.getString("locale");
        createdAt = df.parse(json.getString("created_at"));
        updatedAt = df.parse(json.getString("updated_at"));
    }

    @Override
    public String toString() {
        return userName;
    }

    public String toLongString() {
        StringBuilder sb = new StringBuilder("User: (").append('\n');
        sb.append("  ").append(userName).append('\n');
        sb.append(", ID: ").append(id).append('\n');
        sb.append(", ImageURL: ").append(avatarImageUrl).append('\n');
        sb.append(", ProfileURL: ").append(profileUrl).append('\n');
        sb.append(", Campaigns: (").append('\n');
        for (OPCampaignMini camp : campaigns) {
            sb.append("  , ").append(camp.toLongString()).append('\n');
        }
        sb.append("  )").append('\n');
        sb.append(", Ascendant: ").append(isAscendant).append('\n');
        sb.append(", LastSeen: ").append(lastSeenAt).append('\n');
        sb.append(", UTC-Offset: ").append(utcOffset).append('\n');
        sb.append(", Locale: ").append(locale).append('\n');
        sb.append(", Created: ").append(createdAt).append('\n');
        sb.append(", Updated: ").append(updatedAt).append('\n');
        sb.append(")").append('\n');
        return sb.toString();
    }
}
