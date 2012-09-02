/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.dertroglodyt.scribeop;

import de.dertroglodyt.scribeop.json.JSONException;
import de.dertroglodyt.scribeop.json.JSONObject;

/**
 *
 * @author dertroglodyt
 */
public class OPGameSystem {

    public final String id;
    public final String name;

    public OPGameSystem(JSONObject json) throws JSONException {
        super();
        id = json.getStringOrNull("id");
        name = json.getStringOrNull("name");
    }

    @Override
    public String toString() {
        return name;
    }

    public String toLongString() {
        return name + " ID: " + id;
    }

}
