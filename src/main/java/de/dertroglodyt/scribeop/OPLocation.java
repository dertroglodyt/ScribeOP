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
public class OPLocation {

    public final String longitude;
    public final String lattitude;

    public OPLocation(JSONObject json) throws JSONException {
        super();
        longitude = json.getStringOrNull("lng");
        lattitude = json.getStringOrNull("lat");
    }

    @Override
    public String toString() {
        return "(Long: " + longitude + ", Lat: " + lattitude + ")";
    }

}
