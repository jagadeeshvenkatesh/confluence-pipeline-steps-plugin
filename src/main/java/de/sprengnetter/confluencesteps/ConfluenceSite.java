package de.sprengnetter.confluencesteps;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import javax.annotation.Nonnull;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import de.sprengnetter.confluencesteps.util.HttpUtil;
import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.util.FormValidation;
import net.sf.json.JSONObject;


/**
 * Representation of a configured site for Confluence.
 *
 * @author Oliver Breitenbach
 * @version 1.0.0
 */
public class ConfluenceSite extends AbstractDescribableImpl<ConfluenceSite> implements Serializable {

    private static final long serialVersionUID = -1895419369131803022L;

    private URL url;
    private String userName;
    private String password;
    private AuthenticationType authenticationType;
    private Integer timeout;
    private Integer poolSize;
    private final ConfluenceSiteDescriptor confluenceSiteDescriptor = new ConfluenceSiteDescriptor();

    /**
     * Constructor that takes the values of this instance.
     *
     * @param userName
     *        The user name of the Confluence user.
     * @param password
     *        The password of the Confluence user.
     * @param url
     *        The base URL of Confluence.
     * @param authenticationType
     *        The desired type of authentication.
     * @param timeout
     *        The desired timeout for the connection.
     * @param poolSize
     *        The max connection pool size.
     */
    @DataBoundConstructor
    public ConfluenceSite(final String userName, final String password, final URL url, final AuthenticationType authenticationType,
        final Integer timeout, final Integer poolSize) {
        this.userName = userName;
        this.url = url;
        this.password = password;
        this.authenticationType = authenticationType;
        this.timeout = timeout;
        this.poolSize = poolSize;
    }

    @Override
    public Descriptor<ConfluenceSite> getDescriptor() {
        return confluenceSiteDescriptor;
    }

    /**
     * Returns the user name of the Confluence user.
     *
     * @return The user name as a String.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the user name of the Confluence user.
     *
     * @param userName
     *        The value for user name.
     */
    @DataBoundSetter
    public void setUserName(final String userName) {
        this.userName = userName;
    }

    /**
     * Returns the base URL of Confluence.
     *
     * @return The base URL of Confluence.
     */
    public URL getUrl() {
        return url;
    }

    /**
     * Sets the base URL of Confluence.
     *
     * @param url
     *        The base URL of Confluence.
     */
    @DataBoundSetter
    public void setUrl(final URL url) {
        this.url = url;
    }

    /**
     * Returns the password of the Confluence user.
     *
     * @return The password of the Confluence user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the Confluence user.
     *
     * @param password
     *        The password of the Confluence user.
     */
    @DataBoundSetter
    public void setPassword(final String password) {
        this.password = password;
    }

    /**
     * Returns the type of authentication.
     *
     * @return The type of authentication.
     */
    public AuthenticationType getAuthenticationType() {
        return authenticationType;
    }

    /**
     * Sets the type of authentication.
     *
     * @param authenticationType
     *        The type of authentication.
     */
    @DataBoundSetter
    public void setAuthenticationType(final AuthenticationType authenticationType) {
        this.authenticationType = authenticationType;
    }

    /**
     * Returns the timeout for the connections.
     *
     * @return The timeout for the connections.
     */
    public Integer getTimeout() {
        return timeout;
    }

    /**
     * Sets the timeout for the connections.
     *
     * @param timeout
     *        The timeout for the connections.
     */
    @DataBoundSetter
    public void setTimeout(final Integer timeout) {
        this.timeout = timeout;
    }

    /**
     * Returns the max pool size for the connection pool.
     *
     * @return The max pool size.
     */
    public Integer getPoolSize() {
        return poolSize;
    }

    /**
     * Sets the max pool size for the connection pool.
     *
     * @param poolSize
     *        The max pool size for the connection pool.
     */
    @DataBoundSetter
    public void setPoolSize(Integer poolSize) {
        this.poolSize = poolSize;
    }

    /**
     * Descriptor for {@link ConfluenceSite}.
     */
    @Extension
    public static final class ConfluenceSiteDescriptor extends Descriptor<ConfluenceSite> implements Serializable {

        private static final long serialVersionUID = 7773097811656159514L;

        private String userName;
        private String password;
        private String url;
        private AuthenticationType authenticationType;
        private Integer timeout;
        private Integer poolSize;

        /**
         * Constructor that initializes the view.
         */
        public ConfluenceSiteDescriptor() {
            super(ConfluenceSite.class);
            load();
        }

        @Nonnull
        @Override
        public String getDisplayName() {
            return "Confluence Site";
        }

        @Override
        public boolean configure(final StaplerRequest req, final JSONObject json) throws FormException {
            userName = json.getString("userName");
            password = json.getString("password");
            url = json.getString("url");
            authenticationType = AuthenticationType.fromString(json.getString("authenticationType"));
            if (authenticationType.equals(AuthenticationType.OAUTH)) {
                throw new UnsupportedOperationException("OAuth is not a supported authentication method, yet");
            }
            timeout = json.getInt("timeout");
            poolSize = json.getInt("poolSize");
            validate(userName, password, url, timeout);
            save();
            return super.configure(req, json);
        }

        /**
         * Tests the connection with the data from the view.
         *
         * @param userName
         *        The user name of the Confluence user.
         * @param password
         *        The password of the Confluence user.
         * @param url
         *        The base URL of Confluence.l
         * @param timeout
         *        The timeout for the connections.
         * @return FormValidation to show a success or an error on the view.
         */
        public FormValidation doTestConnection(@QueryParameter("userName") final String userName,
            @QueryParameter("password") final String password,
            @QueryParameter("url") final String url,
            @QueryParameter("timeout") final Integer timeout) {
            try {
                validate(userName, password, url, timeout);
                URL confluenceUrl = new URL(url);
                if (!HttpUtil.isReachable(confluenceUrl, timeout)) {
                    throw new IllegalStateException("Address " + confluenceUrl.toURI().toString() + " is not reachable");
                }
                return FormValidation.okWithMarkup("Success");
            } catch (MalformedURLException e) {
                return FormValidation.errorWithMarkup("The URL " + url + " is malformed");
            } catch (IllegalArgumentException e) {
                return FormValidation.warningWithMarkup(e.getMessage());
            } catch (URISyntaxException e) {
                return FormValidation.errorWithMarkup("URI build from URL " + url + " is malformed");
            } catch (IllegalStateException e) {
                return FormValidation.errorWithMarkup(e.getMessage());
            }
        }

        private void validate(final String userName, final String password, final String url, final Integer timeout) {
            validateCredentials(userName, password);
            HttpUtil.validateUrl(url);
        }

        private void validateCredentials(final String userName, final String password) {
            if (userName == null || userName.isEmpty()) {
                throw new IllegalArgumentException("Please enter the user name of the confluence user!");
            }

            if (password == null || password.isEmpty()) {
                throw new IllegalArgumentException("Please enter the passoword of the confluence user!");
            }
        }


        /**
         * Returns the configured user name of the Confluence user.
         *
         * @return The configured user name of the Confluence user.
         */
        public String getUserName() {
            return userName;
        }

        /**
         * Returns the configured password of the Confluence user.
         *
         * @return The configured password of the Confluence user.
         */
        public String getPassword() {
            return password;
        }

        /**
         * Returns the configured URL of Confluence.
         *
         * @return The configured URL of Confluence.
         */
        public URL getUrl() {
            try {
                return new URL(url);
            } catch (MalformedURLException e) {
                throw new RuntimeException("I am already validated", e);
            }
        }

        /**
         * Returns the type of authentication.
         *
         * @return The type of authentication.
         */
        public AuthenticationType getAuthenticationType() {
            return authenticationType;
        }

        /**
         * Returns the configured timeout for connections.
         *
         * @return The configured timeout.
         */
        public Integer getTimeout() {
            return timeout;
        }

        /**
         * Returns the configured max size of the connection pool.
         *
         * @return The configured max size of the connection pool.
         */
        public Integer getPoolSize() {
            return poolSize;
        }
    }
}
