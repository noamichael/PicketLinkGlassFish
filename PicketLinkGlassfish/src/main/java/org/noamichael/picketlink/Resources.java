package org.noamichael.picketlink;

import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.picketlink.annotations.PicketLink;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.permission.acl.spi.PersistentPermissionVoter;
import org.picketlink.idm.permission.spi.PermissionVoter;

/**
 * This class uses CDI to alias Java EE resources, such as the
 * {@link FacesContext}, to CDI beans
 *
 * <p>
 * Example injection on a managed bean field:
 * </p>
 *
 * <pre>
 * &#064;Inject
 * private FacesContext facesContext;
 * </pre>
 */
@Stateless
public class Resources {

    @PersistenceContext(unitName = "picketlink-default")
    @PicketLink
    private EntityManager em;

    /*
     * Since we are using JPAIdentityStore to store identity-related data, we must provide it with an EntityManager via a
     * producer method or field annotated with the @PicketLink qualifier.
     */
    @Produces
    @PicketLink
    public EntityManager getPicketLinkEntityManager() {
        if (em == null) {
            throw new RuntimeException("The entity manager is null.");
        }
        return em;
    }

    @Produces
    @RequestScoped
    public FacesContext produceFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Produces
    @ApplicationScoped
    public PermissionVoter producePermissionVoter(PartitionManager partitionManager) {
        return new PersistentPermissionVoter(partitionManager);

    }

}
