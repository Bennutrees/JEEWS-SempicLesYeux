/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.entities;

import java.io.File;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 *
 * @author benjamin
 */

@NamedQueries({
    @NamedQuery(
            name = "findPhotosByAlbum",
            query = "SELECT DISTINCT photo FROM Photo photo WHERE photo.album=:album"
    ),
    @NamedQuery(
            name = "findPhotoById",
            query = "SELECT DISTINCT photo FROM Photo photo WHERE photo.id=:id"
    )       
})
@Entity
@Table(name = "photos")
public class Photo implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "album_id")
    @NotNull
    private Album album;
    
    @NotBlank(message="Vous devez renseigner un titre pour votre photo")
    private String title;
    
    private File picture;
    
    public long getId() {
        return id;
    }

    public Album getAlbum() {
        return album;
    }
    
    public String getTitle() {
        return title;
    }
    
    public File getPicture() {
        return picture;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public void setPicture(File picture) {
        this.picture = picture;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Photo other = (Photo) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
}
