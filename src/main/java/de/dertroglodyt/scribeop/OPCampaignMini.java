/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.dertroglodyt.scribeop;

import de.dertroglodyt.scribeop.json.JSONException;
import de.dertroglodyt.scribeop.json.JSONObject;

/**
 * Short form of OPCampaign used in index listing of a players campaigns.
 * @author dertroglodyt
 */
public class OPCampaignMini {

    public final String id;
    public final String name;
    public final String slug;
    public final String campaignUrl;
    public final String visibility;
    public final String role;

    public OPCampaignMini(JSONObject json) throws JSONException {
        super();
//        System.out.println(json);
        id = json.getString("id");
        name = json.getString("name");
        slug = json.getString("slug");
        campaignUrl = json.getString("campaign_url");
        visibility = json.getString("visibility");
        if (json.has("role")) {
            role = json.getString("role");
        } else {
            role = null;
        }
    }

    @Override
    public String toString() {
        return name + " (" + role + ")";
    }

    public String toLongString() {
        return name
                + "\n  ID: " + id
                + "\n  URL: " + campaignUrl
                + "\n  Slug: " + slug
                + "\n  Visibility: "  + visibility
                + "\n  Role: " + role;
    }
}
