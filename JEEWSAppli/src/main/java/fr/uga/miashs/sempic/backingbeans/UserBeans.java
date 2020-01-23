/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.backingbeans;

import fr.uga.miashs.sempic.SempicModelException;
import fr.uga.miashs.sempic.SempicModelUniqueException;
import fr.uga.miashs.sempic.dao.SempicUserFacade;
import fr.uga.miashs.sempic.entities.SempicUser;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotBlank;

/**
 *
 * @author Jerome David <jerome.david@univ-grenoble-alpes.fr>
 */
@Named
@ViewScoped
public class UserBeans implements Serializable {
    
    private SempicUser current;
    
    @Inject
    private SempicUserFacade userDao;
    
    public UserBeans() {
    }
    
    @PostConstruct
    public void init() {
        current=new SempicUser();
    }

    public SempicUser getCurrent() {
        return current;
    }

    public void setCurrent(SempicUser current) {
        this.current = current;
    }
    
    public String getPassword() {
        return null;
    }
    
    public void setPassword(@NotBlank(message="Password is required") String password) {
        getCurrent().setPassword(password);
    }
    
    public String create() {
        try {
            userDao.create(current);
        } 
        catch (SempicModelUniqueException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("un utilisateur avec cette adresse mail existe déjà"));
            return "failure";
        }
        catch (SempicModelException e) {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
           return "failure";
        }
        return "success";
    }
    
    public void delete(SempicUser user, SempicUser currentUser) {
        try {
                if(currentUser.getId() != user.getId()){
                userDao.delete(user);
            }
            else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Vous ne pouvez pas vous supprimer vous même"));
            }
        } catch (SempicModelUniqueException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
        } catch (SempicModelException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
        }
    }
}
