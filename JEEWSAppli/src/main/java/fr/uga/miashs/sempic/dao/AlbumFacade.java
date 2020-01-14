/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.dao;

import fr.uga.miashs.sempic.entities.Album;
import java.util.List;
import javax.ejb.Stateful;

/**
 *
 * @author benjamin
 */
@Stateful
public class AlbumFacade extends AbstractJpaFacade<Long,Album> {
    
    public AlbumFacade() {
        super(Album.class);
    }
}
