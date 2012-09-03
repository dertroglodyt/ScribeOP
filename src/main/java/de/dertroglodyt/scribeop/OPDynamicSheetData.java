/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.dertroglodyt.scribeop;

import de.dertroglodyt.scribeop.json.JSONException;
import de.dertroglodyt.scribeop.json.JSONObject;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author dertroglodyt
 */
public class OPDynamicSheetData {

    public final HashMap<String, String> data;

    public OPDynamicSheetData(JSONObject json) throws JSONException {
        super();
        data = new HashMap<String, String>(json.length());
        Iterator i = json.keys();
        while (i.hasNext()) {
            String key = (String) i.next();
            data.put(key, json.getString(key));
        }
    }

    @Override
    public String toString() {
        return toLongString();
    }

    public String toLongString() {
        StringBuilder sb = new StringBuilder("DST-Data: {");
        for (String key : data.keySet()) {
            sb.append("\n  ").append(key).append(": ").append(data.get(key));
        }
        sb.append("}");
        return sb.toString();
    }

    public JSONObject asJSONObject() {
        return new JSONObject(data);
    }

}
