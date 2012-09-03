/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.dertroglodyt.scribeop;

import de.dertroglodyt.scribeop.json.JSONException;
import de.dertroglodyt.scribeop.json.JSONObject;
import java.text.ParseException;
import java.util.HashMap;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.scribe.model.Token;

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
    public void testAll() throws JSONException, ParseException {
        // API Test key/secret
        String apiKey = "R5d5uehTpOJEBPBIgi5K";
        String apiSecret = "AGtPlGn46OaPrTc8GyYdcHXB5L0LoH3elnv3m2N9";
//        OPService service = OPService.firstConnect(apiKey, apiSecret);
        // API Test access token
        Token accessToken = new Token("YOBBvWwpWievTf0WBxaW", "iWNTYItNR1AQMMhXN9VlyECrVhsiqAhsswYyZeob");
        OPService service = OPService.connect(apiKey, apiSecret, accessToken);
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

        String pageID = "";
        pages = OPWikiPage.getIndex(service, user.campaigns[0].id);
        for (OPWikiPage page : pages) {
            if ("TestSeite".equals(page.name)) {
                pageID = page.id;
            }
        }
        System.out.println("PageID: " + pageID);
        OPWikiPage newPage2 = OPWikiPage.update(service, user.campaigns[0].id, pageID
                , "Nur ein kleiner Test. 2", true, "Geheiminformation! 2");
        System.out.println(newPage2);
        System.out.println(OPWikiPage.delete(service, user.campaigns[0].id, pageID));

        String dstTemplateID = characters[0].dynamicSheetTemplate.id;
        HashMap<String, String> map = new HashMap<String, String>(0);
        map.put("name", "TestCharacter");
        map.put("con", "3");
        OPDynamicSheetData dsData = new OPDynamicSheetData(new JSONObject(map));
        OPCharacter character = OPCharacter.create(service, user.campaigns[0].id, user.id
                , "TestCharacter", "No tagline here :-)"
                , "Description would go here", "My bio is short!", "GM only Info.", true, dstTemplateID, dsData);
        System.out.println(character.toLongString());

        String characterID = "";
        OPCharacter[] chars = OPCharacter.getIndex(service, user.campaigns[0].id);
        for (OPCharacter c : chars) {
            if ("TestCharacter".equals(c.name)) {
                characterID = c.id;
            }
        }
        System.out.println("CharacterID: " + characterID);

        map.put("con", "4");
        map.put("str", "18");
        dsData = new OPDynamicSheetData(new JSONObject(map));
        OPCharacter newCharacter = OPCharacter.update(service, user.campaigns[0].id, characterID
                , "NoBio", true, "NoGMInfo", dsData);
        System.out.println(newCharacter.toLongString());

        System.out.println(OPCharacter.delete(service, user.campaigns[0].id, characterID));
    }

}
