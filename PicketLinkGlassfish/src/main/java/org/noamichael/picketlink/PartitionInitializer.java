package org.noamichael.picketlink;

import org.picketlink.PartitionManagerCreateEvent;
import org.picketlink.idm.PartitionManager;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.event.Observes;
import javax.transaction.*;
import org.picketlink.idm.IdentityManagementException;
import org.picketlink.idm.model.basic.Realm;

/**
 * Initializes the partition manager for use under GlassFish. Ensures that there
 * is a transaction before adding the default partition. 
 * @author Michael Kucinski
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)

public class PartitionInitializer {

    @Resource
    private UserTransaction userTransaction;
    private PartitionManager pm;

    public void initPartition(@Observes PartitionManagerCreateEvent event) {
        pm = event.getPartitionManager();
        try {
            if (pm.getPartition(Realm.class, Realm.DEFAULT_REALM) == null) {

                this.userTransaction.begin();
                pm.add(new Realm(Realm.DEFAULT_REALM));
                this.userTransaction.commit();
            }

        } catch (IllegalStateException | SecurityException | HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException | IdentityManagementException e) {//Exception e's message is PLIDM000404
            try {
                this.userTransaction.rollback();
            } catch (IllegalStateException | SecurityException | SystemException x) {//Can't roleback: Transaction in in another thread.
                
            }
            throw new RuntimeException("Could not create default partition.", e);//Closed EMF
        }
    }
}
