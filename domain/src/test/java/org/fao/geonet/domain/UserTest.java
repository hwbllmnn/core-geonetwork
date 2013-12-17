package org.fao.geonet.domain;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import junit.framework.Assert;
import org.fao.geonet.repository.AbstractSpringDataTest;
import org.fao.geonet.repository.UserRepositoryTest;
import org.fao.geonet.utils.Xml;
import org.jdom.Element;
import org.junit.Test;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test user methods.
 * User: Jesse
 * Date: 10/6/13
 * Time: 9:30 PM
 */
public class UserTest extends AbstractSpringDataTest {
    private AtomicInteger _inc = new AtomicInteger();

    @Test
    public void testAsXml() throws Exception {
        final User user = UserRepositoryTest.newUser(_inc);
        user.setId(43234);
        final String add1 = "add1";
        final String city1 = "city1";
        final String country1 = "country1";
        final String state1 = "state1";
        final String zip1 = "zip1";

        final String add2 = "add2";
        final String city2 = "city2";
        final String country2 = "country2";
        final String state2 = "state2";
        final String zip2 = "zip2";

        user.getAddresses().add(
                new Address()
                        .setAddress(add1)
                        .setCity(city1)
                        .setCountry(country1)
                        .setState(state1)
                        .setZip(zip1));
        user.getAddresses().add(
                new Address()
                        .setAddress(add2)
                        .setCity(city2)
                        .setCountry(country2)
                        .setState(state2)
                        .setZip(zip2));
        String email1 = "email1@c2c.com";
        String email2 = "email2@c2c.com";
        user.getEmailAddresses().add("invalidEmail");
        user.getEmailAddresses().add(email1);
        user.getEmailAddresses().add(email2);

        user.getSecurity().setAuthType("authtype");
        user.getSecurity().getSecurityNotifications().add(UserSecurityNotification.HASH_UPDATE_REQUIRED);

        Element xml = user.asXml();

        assertEquals(""+user.getId(), xml.getChildText("id"));

        final Element security = xml.getChild(User_.security.getName());
        assertNull(security.getChild(UserSecurity_.password.getName()));

        final String expectedNotifications = user.getSecurity().getSecurityNotifications().iterator().next().toString();
        final List<Element> securityNotification = security.getChild("securitynotifications").getChildren();
        assertEquals(1, securityNotification.size());
        assertEquals(expectedNotifications, securityNotification.get(0).getText());

        final List<?> emailAddresses = xml.getChild(User_.emailAddresses.getName().toLowerCase()).getChildren();
        List<String> emailAddressesAsStrings = Lists.transform(emailAddresses, new Function<Object, String>() {
            @Nullable
            @Override
            public String apply(@Nullable Object input) {
                return ((Element)input).getTextTrim();
            }
        });
        Assert.assertEquals(3, emailAddresses.size());
        Assert.assertTrue(emailAddressesAsStrings.contains(email1));
        Assert.assertTrue(emailAddressesAsStrings.contains(email2));

        assertEquals(email1, xml.getChildText("email"));

        final Element addressesEl = xml.getChild(User_.addresses.getName());
        List<Element> addresses = addressesEl.getChildren();
        assertEquals(2, addresses.size());

        for (Element address : addresses) {
            if (address.getChild(Address_.address.getName()).getText().equals(add1)) {
                assertEqualAddress(add1, city1, country1, state1, zip1, address);
            } else {
                assertEqualAddress(add2, city2, country2, state2, zip2, address);
            }
        }
    }

    private void assertEqualAddress(String add1, String city1, String country1, String state1, String zip1, Element address) {
        assertEquals(add1, address.getChild(Address_.address.getName()).getText());
        assertEquals(city1, address.getChild(Address_.city.getName()).getText());
        assertEquals(country1, address.getChild(Address_.country.getName()).getText());
        assertEquals(state1, address.getChild(Address_.state.getName()).getText());
        assertEquals(zip1, address.getChild(Address_.zip.getName()).getText());
    }

    @Test
    public void testSetSecurityNotifications() throws Exception {
        final User user = UserRepositoryTest.newUser(_inc);
        user.getSecurity().setSecurityNotificationsString("");

        assertTrue(user.getSecurity().getSecurityNotifications().isEmpty());


    }
}