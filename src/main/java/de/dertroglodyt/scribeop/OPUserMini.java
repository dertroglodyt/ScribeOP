/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.dertroglodyt.scribeop;

import de.dertroglodyt.scribeop.json.JSONException;
import de.dertroglodyt.scribeop.json.JSONObject;
import java.text.ParseException;

/**
 * Short form of OPUser found in indexes of users.
 * @author dertroglodyt
 */
public class OPUserMini {

    public final String id;
    public final String userName;
    public final String avatarImageUrl;
    public final String profileUrl;

    public OPUserMini(JSONObject json) throws JSONException, ParseException {
        super();
        id = json.getString("id");
        userName = json.getString("username");
        profileUrl = json.getString("profile_url");
        if (json.has("avatar_image_url")) {
            avatarImageUrl = json.getString("avatar_image_url");
        } else {
            avatarImageUrl = null;
        }
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
        sb.append(")").append('\n');
        return sb.toString();
    }
}
