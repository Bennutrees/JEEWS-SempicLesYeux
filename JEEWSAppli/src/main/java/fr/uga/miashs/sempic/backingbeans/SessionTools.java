/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.backingbeans;

import fr.uga.miashs.sempic.SempicException;
import fr.uga.miashs.sempic.SempicModelException;
import fr.uga.miashs.sempic.qualifiers.*;
import fr.uga.miashs.sempic.dao.*;
import fr.uga.miashs.sempic.entities.*;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.security.Principal;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.security.enterprise.SecurityContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Jerome David <jerome.david@univ-grenoble-alpes.fr>
 */
@Named
@SessionScoped
public class SessionTools implements Serializable {
 
    @Inject
    private FacesContext facesContext;
    
    @Inject
    private transient SecurityContext securityContext;

    @Inject
    private SempicUserFacade userDao;
    
    @Inject
    private AlbumFacade albumDao;
    
    @Inject PhotoFacade photoDao;
    

    private SempicUser connectedUser;
    
    private String currentView;
    private String previousView;
    private Map<String,String> currentParams;
    private Map<String,String> previousParams;

    /**
     * Creates a new instance of Login
     */
    public SessionTools() {
    }
    
    /*
     The two following methods are used to implement a CDI Realm.
    This class is declared in META-INF/context.xml with:
    <Valve className="org.apache.catalina.authenticator.FormAuthenticator"/>
    <Realm cdi="true" 
        className="org.apache.tomee.catalina.realm.LazyRealm" 
        realmClass="fr.uga.miashs.sempic.backingbeans.SessionTools"/>

    
    see the doc : http://tomee.apache.org/latest/examples/cdi-realm.html
    */
    public Principal authenticate(final String username, String password) {
        //Logger.getAnonymousLogger().log(Level.SEVERE,"username: "+username+" password: "+password);
        try {
            connectedUser = userDao.login(username, password);
            return new Principal() {
                @Override
                public String getName() {
                    return connectedUser.getEmail();
                }

                @Override
                public String toString() {
                    return connectedUser.getEmail();
                }
            };
        }
        catch (NoResultException e) {
            //e.printStackTrace();
            return null;
        } catch (SempicModelException ex) {
            return null;
        }
    }
    
    public boolean hasRole(final Principal principal, final String role) {
        if ("ADMIN".equals(role)) {
            return connectedUser.getUserType()==SempicUserType.ADMIN;
        }
        else {
            return true;
        }
    }

    @Produces
    @LoggedUser
    @Dependent
    @Named
    public SempicUser getConnectedUser() {
        if (connectedUser == null) {
            try {
                connectedUser = userDao.readByEmail(securityContext.getCallerPrincipal().getName());
            }
            catch (IllegalArgumentException | NullPointerException e) {
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "No connected user: {0}", e.getMessage());
            }
        }
        return connectedUser;
    }
    
    
    /**
     * get the user that correspond to a specific userId in the request
     * parameter only admin can request another user than himself. in other
     * cases, it returns the connected user.
     *
     * @return
     */
    @Produces
    @SelectedUser
    @Dependent
    @Named
    public SempicUser getSelectedUser() throws SempicException {
        String userId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("userId");
        if (userId == null || getConnectedUser().getUserType() != SempicUserType.ADMIN) {
            return getConnectedUser();
        }
        try {
            long id = Long.parseLong(userId);
            return userDao.read(id);
        } catch (NumberFormatException e) {
            throw new SempicException("User id ust be Long type : " + userId,e);
        }
    }
    
    @Produces
    @SelectedAlbum
    @Dependent
    @Named
    public Album getSelectedAlbum() throws SempicException {
        String albumId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("albumId");
        if (albumId != null) {
            try {
                long albumLongId = Long.parseLong(albumId);
                Album selectedAlbum = albumDao.read(albumLongId);
                SempicUser currentUser = getConnectedUser();
                if (currentUser != null && selectedAlbum.getOwner().equals(currentUser)) {
                    return selectedAlbum;
                }
                throw new SempicException(currentUser + " is not allowed to access this album");
            } catch (NumberFormatException e){
                throw new SempicException("Album id must be Long type : " + albumId, e);
            }
        }
        throw new SempicException("Missing Album id");
    }
    
    @Produces
    @SelectedPhoto
    @Dependent
    @Named
    public Photo getSelectedPhoto() throws SempicException {
        String photoId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("photoId");
        if (photoId != null) {
            try {
                long photoLongId = Long.parseLong(photoId);
                Photo selectedPhoto = photoDao.read(photoLongId);
                SempicUser currentUser = getConnectedUser();
                if (currentUser != null) {
                    return selectedPhoto;
                }
                throw new SempicException(currentUser + " is not allowed to access this photo");
            } catch (NumberFormatException e){
                throw new SempicException("Photo id must be Long type : " + photoId, e);
            }
        }
        throw new SempicException("Missing Photo id");
    }
    
    
    public boolean isNotLogged() {
        return (getConnectedUser()==null);
    }
    
    public boolean isAdmin() {
        SempicUser u = getConnectedUser();
        return (u!=null && u.getUserType()==SempicUserType.ADMIN);
    }
    
     public boolean isUser() {
        SempicUser u = getConnectedUser();
        return (u!=null && u.getUserType()==SempicUserType.USER);
    }
     
     public void setCurrentView(String v, Map<String,String> params) {
         if (v!=null && !v.equals(currentView)) {
             previousView=currentView;
             previousParams=currentParams;
             currentView=v;
             currentParams=params;
         }
     }
     
     
     private static String buildViewParams(String viewID, Map<String,String> params) {
         if (params!=null && !params.isEmpty()) {
            StringBuilder sb = new StringBuilder(viewID);
            sb.append('?');
            Iterator<Map.Entry<String,String>> it = params.entrySet().iterator();
            Map.Entry<String,String> ent = it.next();
            sb.append(ent.getKey());
            sb.append('=');
            sb.append(ent.getValue());
            while (it.hasNext()) {
                ent = it.next();
                sb.append('&');
                sb.append(ent.getKey());
                sb.append('=');
                sb.append(ent.getValue());
            }
            viewID = sb.toString();
        }
        return viewID;
     }
     
     public String getPreviousView() {
         return previousView!=null?buildViewParams(previousView,previousParams):buildViewParams(currentView,currentParams);
     }
    
    public String logout() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        session.invalidate();
        // return "/login?faces-redirect=true";
        return "logout";
    }
}
