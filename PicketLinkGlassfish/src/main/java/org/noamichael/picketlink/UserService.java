/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.noamichael.picketlink;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.picketlink.annotations.PicketLink;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.model.IdentityType;

/**
 * Manual retrieval of entities
 * 
 * @author Michael Kucinski
 */
@Stateless
public class UserService {
    @Inject 
    @PicketLink
    private EntityManager em;
    
    @Inject
    private IdentityManager im;
    
    public boolean usersExist(){
        Query q = em.createQuery("SELECT U FROM AccountTypeEntity U");
        return !q.getResultList().isEmpty();
    }
    public void addUser(IdentityType user){
        im.add(user);
    }
    
}
