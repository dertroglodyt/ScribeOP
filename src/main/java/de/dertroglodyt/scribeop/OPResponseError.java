/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.dertroglodyt.scribeop;

import de.dertroglodyt.scribeop.json.JSONArray;
import de.dertroglodyt.scribeop.json.JSONException;
import de.dertroglodyt.scribeop.json.JSONObject;
import org.scribe.model.Response;

/**
 *
 * @author dertroglodyt
 */
public class OPResponseError {

    public static enum HtmlCode {
        UNKNOWN(-1)
        , SUCCESS(200)
        , CREATED(201)
        , NO_CONTENT(204)
        , REQUEST_ERROR(400)
        , UNAUTORIZED(401)
        , FORBIDDEN(403)
        , NOT_FOUND(404)
        , SERVER_ERROR(500)
        , SERVICE_UNAVAILABLE(503)
        ;

        public final int code;

        HtmlCode(int aCode) {
            code = aCode;
        }
        public static HtmlCode get(int code) {
            for (HtmlCode hc : HtmlCode.values()) {
                if (hc.code == code) {
                    return hc;
                }
            }
            return UNKNOWN;
        }

        @Override
        public String toString() {
            return code + " "+ this.name();
        }

    }

    public static enum ErrorType {
        UNKNOWN(-1)
        , SUCCESS(0)
        , INVALID_PARAMETER_VALUE(4010)
        , CAMPAIGN_VISIBILITY_RESTRICTED(4020)
        , GM_ONLY_RESOURCE(4030)
        , CAMPAIGN_MEMBER_REQUIRED(4040)
        , AUTHOR_OR_GM_REQUIRED(4050)
        , ASCENDANT_ONLY(4060)
        ;

        public final int code;
        ErrorType(int aCode) {
            code = aCode;
        }
        public static ErrorType get(int code) {
            for (ErrorType et : ErrorType.values()) {
                if (et.code == code) {
                    return et;
                }
            }
            return UNKNOWN;
        }

        @Override
        public String toString() {
            return code + " "+ this.name();
        }

    }

    public class OPResponseCode {

        public final ErrorType type;
        public final String message;

        public OPResponseCode(ErrorType aType, String msg) {
            super();
            type = aType;
            message = msg;
        }

        public OPResponseCode(JSONObject json) throws JSONException {
            super();
//            System.out.println(json);
            if (! json.isNull("code")) {
                type = ErrorType.get(json.getInt("code"));
            } else {
                type = ErrorType.get(-1);
            }
            message = json.getString("message");
        }

        @Override
        public String toString() {
            return type.toString() + " "+ message;
        }

    }

    public final HtmlCode htmlCode;
    public final OPResponseCode[] errors;

    public OPResponseError(Response response) throws JSONException {
        super();
//        System.out.println(response.getCode() + " Response body: " + response.getBody());
        JSONObject jobj;
        try {
            jobj = new JSONObject(response.getBody());
        } catch(JSONException ex) {
            htmlCode = HtmlCode.get(response.getCode());
            errors = new OPResponseCode[1];
            errors[0] = new OPResponseCode(ErrorType.UNKNOWN, response.getBody());
            return;
        }
        htmlCode = HtmlCode.get(jobj.getInt("http_status"));

        if (response.isSuccessful()) {
            errors = new OPResponseCode[1];
            errors[0] = new OPResponseCode(ErrorType.SUCCESS, response.getBody());
            return;
        }

        JSONArray json = jobj.getJSONArray("errors");
        errors = new OPResponseCode[json.length()];
        for (int i=0; i < json.length(); i++) {
            errors[i] = new OPResponseCode(json.getJSONObject(i));
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(htmlCode.toString());
        if (errors != null) {
            for (OPResponseCode error : errors) {
                sb.append("\n  ").append(error.toString());
            }
        }
        return sb.toString();
    }

}
