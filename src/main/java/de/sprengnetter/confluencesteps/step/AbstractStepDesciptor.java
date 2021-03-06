package de.sprengnetter.confluencesteps.step;

import com.google.common.collect.ImmutableSet;
import hudson.EnvVars;
import hudson.model.Run;
import hudson.model.TaskListener;
import org.jenkinsci.plugins.workflow.steps.StepDescriptor;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * @author Oliver Breitenbach
 * @version 1.0.0
 * Abstract base class which declares the methods that MUST be overridden by the step descriptors.
 * Each step contains an inner class, the descriptor that is used by Jenkins, that should extend this class.
 */
public abstract class AbstractStepDesciptor extends StepDescriptor {

    @Nonnull
    @Override
    public abstract String getDisplayName();

    @Override
    public abstract String getFunctionName();


    @Override
    public Set<? extends Class<?>> getRequiredContext() {
        return ImmutableSet.of(Run.class, TaskListener.class, EnvVars.class);
    }
}
