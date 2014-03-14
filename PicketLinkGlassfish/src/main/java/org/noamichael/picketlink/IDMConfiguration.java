package org.noamichael.picketlink;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import org.picketlink.idm.config.IdentityConfiguration;
import org.picketlink.idm.config.IdentityConfigurationBuilder;
import org.picketlink.idm.model.Relationship;
import javax.enterprise.event.Observes;
import org.picketlink.IdentityConfigurationEvent;
import org.picketlink.idm.jpa.model.sample.simple.*;
import org.picketlink.internal.EEJPAContextInitializer;

/**
 * This bean produces the configuration for PicketLink IDM
 *
 * Every entity class which needs to be managed by PicketLink gets mapped
 * here. Custom credential handlers must also be added to this location.
 *
 *
 * @author Michael Kucinski
 *
 */
@ApplicationScoped
public class IDMConfiguration {

    @Inject
    private EEJPAContextInitializer contextInitializer;

    private IdentityConfiguration identityConfig = null;

    @Produces
    IdentityConfiguration createConfig() {
        if (identityConfig == null) {
            IdentityConfigurationBuilder builder = new IdentityConfigurationBuilder();
            initConfig(builder);
        }
        return identityConfig;
    }

    public void observeIdentityEvent(@Observes IdentityConfigurationEvent event) {
        IdentityConfigurationBuilder builder = event.getConfig();
        initConfig(builder);
    }

    /**
     * This method uses the IdentityConfigurationBuilder to create an
     * IdentityConfiguration, which defines how PicketLink stores
     * identity-related data. In this particular example, a JPAIdentityStore is
     * configured to allow the identity data to be stored in a relational
     * database using JPA.
     */
    @SuppressWarnings("unchecked")
    private void initConfig(IdentityConfigurationBuilder builder) {
        builder = new IdentityConfigurationBuilder();

        builder
            .named("default")
                .stores()
                    .jpa()
                        .mappedEntity(
                                AccountTypeEntity.class,
                                RoleTypeEntity.class,
                                GroupTypeEntity.class,
                                IdentityTypeEntity.class,
                                RelationshipTypeEntity.class,
                                RelationshipIdentityTypeEntity.class,
                                PartitionTypeEntity.class,
                                PasswordCredentialTypeEntity.class,
                                AttributeTypeEntity.class)
                        .supportGlobalRelationship(Relationship.class)
                        .addContextInitializer(this.contextInitializer)
                        // Specify that this identity store configuration supports all features
                        .supportAllFeatures();

        identityConfig = builder.build();

    }
}
