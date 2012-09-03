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
public class OPDynamicSheetTemplateMini {

    // public fields
    public final String id;
    public final String name;
    public final String slug;

    public OPDynamicSheetTemplateMini(JSONObject json) throws JSONException, ParseException {
        super();
//        System.out.println(json);
        id = json.getString("id");
        name = json.getString("name");
        slug = json.getString("slug");
    }

    @Override
    public String toString() {
        return name;
    }

    public String toLongString() {
        return name + " Slug: " + slug + " ID: " + id;
    }

}
