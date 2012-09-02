/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.dertroglodyt.scribeop;

import de.dertroglodyt.scribeop.json.JSONException;
import java.text.ParseException;

/**
 *
 * @author dertroglodyt
 */
public class GesamtTest {

    public void testUser() throws JSONException, ParseException {
        OPService service = OPService.connect();
        OPUser user = new OPUser(service, null);
        System.out.println(user.toLongString());
        for (int i=0; i < user.campaigns.length; i++) {
            OPCampaign camp = new OPCampaign(service, user.campaigns[i].id);
            System.out.println(camp.toLongString());
        }
        OPWikiPage[] pages = OPWikiPage.getIndex(service, user.campaigns[0].id);
        for (OPWikiPage page : pages) {
            System.out.println(page.toLongString());
        }
        OPWikiPage newPage = OPWikiPage.create(service, user.campaigns[0].id
                , "TestSeite", "Nur ein kleiner Test.", true, "Geheiminformation!");
        System.out.println(newPage);
        newPage = OPWikiPage.update(service, user.campaigns[0].id, newPage.id
                , "Nur ein kleiner Test.", true, "Geheiminformation!");
        System.out.println(newPage);
        System.out.println(OPWikiPage.delete(service, user.campaigns[0].id, newPage.id));
    }

}
