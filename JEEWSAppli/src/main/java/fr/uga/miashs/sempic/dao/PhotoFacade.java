/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.dao;

import fr.uga.miashs.sempic.SempicException;
import fr.uga.miashs.sempic.SempicModelException;
import fr.uga.miashs.sempic.entities.Photo;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateful;
import javax.inject.Inject;

/**
 *
 * @author benjamin
 */
@Stateful
public class PhotoFacade extends AbstractJpaFacade<Long,Photo>{
    
    @Inject
    private PhotoStorage photoStorage;
    
    public PhotoFacade() {
        super(Photo.class);
    }
    
    public void create(Photo photo, InputStream donnee) throws SempicModelException {
        super.create(photo);
        try {
            photoStorage.savePicture(Paths.get(String.valueOf(photo.getAlbum().getId()), String.valueOf(photo.getId())), donnee);
        } catch (SempicException e) {
            throw new SempicModelException();
        }
    }
    
    @Override
    public void delete(Photo photo) throws SempicModelException {
        super.delete(photo);
        try {
            photoStorage.deletePicture(Paths.get(String.valueOf(photo.getAlbum().getId()), String.valueOf(photo.getId())));
        }catch (SempicException e) {
            Logger.getLogger(PhotoFacade.class.getName()).log(Level.INFO, null, e);
        }
    }
}
