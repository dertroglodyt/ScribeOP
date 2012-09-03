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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 *
 * @author dertroglodyt
 */
public class OPDynamicSheetTemplate {

    private static final String URL_INDEX = "http://api.obsidianportal.com/v1/dynamic_sheet_templates.json";
    private static final String URL_DST = "http://api.obsidianportal.com/v1/dynamic_sheet_templates/$id.json";
    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    // public fields
    public final String id;
    public final String name;
    public final String slug;
    public final OPUserMini user;
    public final OPGameSystem gameSystem;
    public final String htmlTemplate;
    public final String css;
    public final String javascript;
    public final String state;
    public final Date createdAt;
    public final Date updatedAt;
    // availability only to author
    public final String htmlTemplateSubmitted;
    public final String cssSubmitted;
    public final String javascriptSubmitted;

    public OPDynamicSheetTemplate(OPService service, String dstID) throws JSONException, ParseException {
        this(new JSONObject(service.get(URL_DST.replace("$id", dstID)).getBody()));
    }

    public OPDynamicSheetTemplate(JSONObject json) throws JSONException, ParseException {
        super();
        System.out.println(json);
        id = json.getString("id");
        name = json.getString("name");
        slug = json.getString("slug");
        user = new OPUserMini(json.getJSONObject("user"));
        gameSystem = new OPGameSystem(json.getJSONObject("game_system"));
        htmlTemplate = json.getString("html_template");
        css = json.getString("css");
        javascript = json.getString("javascript");
        state = json.getString("state");
        createdAt = df.parse(json.getString("created_at"));
        updatedAt = df.parse(json.getString("updated_at"));
        if (json.has("html_template_submitted") && !json.isNull("html_template_submitted")) {
            htmlTemplateSubmitted = json.getString("html_template_submitted");
        } else {
            htmlTemplateSubmitted = null;
        }
        if (json.has("css_submitted") && !json.isNull("css_submitted")) {
            cssSubmitted = json.getString("css_submitted");
        } else {
            cssSubmitted = null;
        }
        if (json.has("javascript_submitted") && !json.isNull("javascript_submitted")) {
            javascriptSubmitted = json.getString("javascript_submitted");
        } else {
            javascriptSubmitted = null;
        }
    }

    public static OPDynamicSheetTemplate[] getIndex(OPService service) throws JSONException, ParseException {
        JSONArray json = new JSONArray(service.get(URL_INDEX).getBody());
        OPDynamicSheetTemplate[] dst = new OPDynamicSheetTemplate[json.length()];
        for (int i=0; i < json.length(); i++) {
            dst[i] = new OPDynamicSheetTemplate(json.getJSONObject(i));
        }
        return dst;
    }

    @Override
    public String toString() {
        return name;
    }

    public String toLongString() {
        StringBuilder sb = new StringBuilder(name
                + "\n ID: " + id
                + "\n Slug: " + slug
                + "\n User: " + user
                + "\n GameSystem: " + gameSystem.toLongString()
                + "\n HtmlTemplate: " + htmlTemplate
                + "\n CSS: " + css
                + "\n JavaScript: " + javascript
                + "\n State: " + state
                + "\n Created: " + createdAt
                + "\n Updated: " + updatedAt
                + "\n HtmlTemplateSub: " + htmlTemplateSubmitted
                + "\n CCSSub: " + cssSubmitted
                + "\n JavaScriptSub: " + javascriptSubmitted);
        return sb.toString();
    }

    public ArrayList<String> getFieldNames() {
        ArrayList<String> list = new ArrayList<String>(0);
        String st = htmlTemplate;
        while (st.indexOf("dsf_") >= 0) {
            st = st.substring(st.indexOf("dsf_")+4);
            String field = st.substring(0, st.indexOf("\""));
            list.add(field);
        }
        return list;
    }

    // ToDo create, update, submit
}
