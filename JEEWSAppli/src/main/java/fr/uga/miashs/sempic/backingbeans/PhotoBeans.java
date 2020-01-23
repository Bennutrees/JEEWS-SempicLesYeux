/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.backingbeans;

import fr.uga.miashs.sempic.ResourceNotFoundException;
import fr.uga.miashs.sempic.SempicException;
import fr.uga.miashs.sempic.SempicModelException;
import fr.uga.miashs.sempic.SempicModelUniqueException;
import fr.uga.miashs.sempic.dao.AlbumFacade;
import fr.uga.miashs.sempic.dao.PhotoFacade;
import fr.uga.miashs.sempic.dao.PhotoStorage;
import fr.uga.miashs.sempic.entities.Album;
import fr.uga.miashs.sempic.entities.Photo;
import fr.uga.miashs.sempic.entities.SempicUser;
import fr.uga.miashs.sempic.rdf.SempicRDFStore;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;

/**
 *
 * @author adem
 */
@Named
@ViewScoped
public class PhotoBeans implements Serializable{
        
    @Inject
    private AlbumFacade albumDAO;
    
    @Inject
    private PhotoFacade photoDao;
    
    @Inject
    private PhotoStorage photoStorage;
    
    @EJB
    private SempicRDFStore rdfStore;
    
    private Photo photo;
    private Album currentAlbum;
    
    private Part image;
    private File myPicture;
    
    private String albumId;
    
    public PhotoBeans(){
    }
    
    @PostConstruct
    public void init() {
        this.photo = new Photo();
        try {
            this.photoStorage = new PhotoStorage(Paths.get(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/")));
        } catch (IOException ex) {
            Logger.getLogger(PhotoBeans.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String getAlbumId() {
        return this.albumId;
    }
    
    public void setAlbumId(String value) {
        this.albumId = value;
    }
    
    public Part getImage() {
        return image;
    }
    
    public void setImage(Part image) {
        this.image = image;
    }
    
    public Photo getPhoto() {
       return photo;
    }
    
    public void setPhoto(Photo photo) {
        this.photo = photo;
    }
   
    public Album getCurrentAlbum() {
        return currentAlbum;
    }

    public void setCurrentAlbum(Album currentAlbum) {
        this.currentAlbum = currentAlbum;
    }
    
    public void setCurrentAlbum(Long albumID) throws ResourceNotFoundException {
        this.currentAlbum = this.albumDAO.readEager(albumID);
    }
    
    public Set<Photo> getPhotoByAlbumId(Long idAlbum) {
        return this.albumDAO.read(idAlbum).getPhotos();
    }
    
    public String create(SempicUser currentUser) throws SempicModelException{
        Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String idAlbum = params.get("albumId");
        Album album = albumDAO.read(Long.parseLong(idAlbum));
        if(album != null) {
            if(photoDao == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Missing photo Dao"));
                return "failure";
            }
            if(rdfStore == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Missing rdf Store"));
                return "failure";
            }
            try{
                
                InputStream in = image.getInputStream();
                Path photoPath = Paths.get(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/")+image.getSubmittedFileName());
                photoStorage.savePicture(photoPath, in);
                in.close();
                photo.setAlbum(album);
                photo.setPhotoName(image.getSubmittedFileName());
                photoDao.create(photo);
                /*BasicSempicRDFStore s = new BasicSempicRDFStore();
                Model m = ModelFactory.createDefaultModel();

                Resource pRest = s.createPhoto(photo.getId(),Long.parseLong(idAlbum),currentUser.getId());
                s.saveModel(m);*/
                return "album.xhtml?faces-redirect=true&albumId="+idAlbum;
            }catch(Exception e){
                e.printStackTrace(System.out);
            }
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Album id required on path"));
        return "failure";
    }
    
    public String delete(Long id) throws SempicModelException, SempicException  {
        try {
            this.photoDao.deleteById(id);
            this.photoStorage.deletePicture(Paths.get(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/")+this.photo.getPhotoName()));
            //rdfStore.deletePhoto(id);
        } catch (SempicModelUniqueException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Data error"));
            return "failure";
        }
        return "album?faces-redirect=true&albumId="+this.albumId;
    }
}
