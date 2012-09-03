/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.dertroglodyt.scribeop;

import de.dertroglodyt.scribeop.json.JSONException;
import java.text.ParseException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author dertroglodyt
 */
public class GesamtTest {

    public GesamtTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
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
        OPCharacter[] characters = OPCharacter.getIndex(service, user.campaigns[0].id);
        for (OPCharacter character : characters) {
            System.out.println(character.toLongString());
            OPCharacter c = new OPCharacter(service, user.campaigns[0].id, character.id);
            System.out.println(c.toLongString());
            OPDynamicSheetTemplate dst = new OPDynamicSheetTemplate(service, c.dynamicSheetTemplate.id);
            System.out.println(dst.toLongString());
            System.out.println("Fields: " + dst.getFieldNames());
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
