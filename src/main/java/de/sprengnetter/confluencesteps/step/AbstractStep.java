package de.sprengnetter.confluencesteps.step;

import jenkins.model.Jenkins;
import org.jenkinsci.plugins.workflow.steps.Step;
import de.sprengnetter.confluencesteps.ConfluenceSite;
import java.io.Serializable;

/**
 * @author Oliver Breitenbach
 * @version 1.0.0
 *          Abstract base class for all the available steps within this plugin.
 */
public abstract class AbstractStep extends Step implements Serializable {

    private static final long serialVersionUID = -2394672691414818804L;

    private ConfluenceSite site;

    /**
     * Constructor which extracts the information of the configured site (global Jenkins config) from it's descriptor
     * and constructs an instance of {@link ConfluenceSite} which can be used by the steps.
     */
    public AbstractStep() {
        ConfluenceSite.ConfluenceSiteDescriptor siteDescriptor = (ConfluenceSite.ConfluenceSiteDescriptor) Jenkins.getInstance()
            .getDescriptor(ConfluenceSite.class);

        if (siteDescriptor != null) {
            this.site = new ConfluenceSite(
                siteDescriptor.getUserName(),
                siteDescriptor.getPassword(),
                siteDescriptor.getUrl(),
                siteDescriptor.getAuthenticationType(),
                siteDescriptor.getTimeout(),
                siteDescriptor.getPoolSize());
        }
    }

    /**
     * Returns the configured {@link ConfluenceSite}.
     *
     * @return The configured {@link ConfluenceSite}.
     */
    public ConfluenceSite getSite() {
        return site;
    }
}
