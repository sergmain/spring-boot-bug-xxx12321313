package bug;

import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * @author Sergio Lissner
 * Date: 7/21/2022
 * Time: 8:07 PM
 */
public class SimpleControllerTest {

    public static final String WEB_CONTAINER_SESSIONID_NAME = "JSESSIONID";

    @ExtendWith(SpringExtension.class)
    @SpringBootTest
    @ActiveProfiles("profile26")
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    @AutoConfigureCache
    public static class TestAccessForAllEndPointsProfile26 {

        private MockMvc mockMvc;

        @Autowired
        private WebApplicationContext webApplicationContext;

        @BeforeEach
        public void setup() {
            this.mockMvc = webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
        }

        @Test
        public void testAnonymousAccessRestriction() throws Exception {
            checkAccessRestriction(mockMvc, ACCOUNT_URLS);
            checkRestAccessRestriction(mockMvc, ACCOUNT_REST_URLS);
        }
    }

    private enum AccessMethod {POST, GET}

    @Data
    public static class AccessUrl {
        public final String url;
        public final AccessMethod accessMethod;
    }

    public static final AccessUrl[] ACCOUNT_URLS = new AccessUrl[]{
            new AccessUrl("/content/secured/info", AccessMethod.GET),
    };

    public static final AccessUrl[] ACCOUNT_REST_URLS = new AccessUrl[]{
            new AccessUrl("/rest/v1/secured/info", AccessMethod.GET),
    };

    public static void checkRestAccessRestriction(MockMvc mockMvc, AccessUrl[] accountRestUrls) throws Exception {
        for (AccessUrl accessUrl : accountRestUrls) {
            System.out.println("accessUrl: " + accessUrl);
            ResultActions resultActions;
            if (accessUrl.accessMethod== AccessMethod.GET) {
                resultActions = mockMvc.perform(get(accessUrl.url));
            }
            else if (accessUrl.accessMethod== AccessMethod.POST) {
                resultActions = mockMvc.perform(post(accessUrl.url));
            }
            else {
                throw new IllegalStateException("Unknown http method: " + accessUrl.accessMethod);
            }
            resultActions.andExpect(status().isUnauthorized()).andExpect(cookie().doesNotExist(WEB_CONTAINER_SESSIONID_NAME));
        }
    }

    public static void checkAccessRestriction(MockMvc mockMvc, AccessUrl[] accountUrls) throws Exception {
        for (AccessUrl accessUrl : accountUrls) {
            System.out.println("accessUrl: " + accessUrl);
            if (accessUrl.accessMethod== AccessMethod.GET) {
                mockMvc.perform(get(accessUrl.url))
                        .andExpect(status().isFound())
                        .andExpect(redirectedUrlPattern("http://*/login"))
                        .andExpect(cookie().doesNotExist(WEB_CONTAINER_SESSIONID_NAME));
            }
            else if (accessUrl.accessMethod== AccessMethod.POST) {
                mockMvc.perform(post(accessUrl.url))
                        .andExpect(status().isForbidden())
                        .andExpect(cookie().doesNotExist(WEB_CONTAINER_SESSIONID_NAME));
            }
            else {
                throw new IllegalStateException("Unknown http method: " + accessUrl.accessMethod);
            }
        }
    }

}
