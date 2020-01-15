/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.restservice;

import fr.uga.miashs.sempic.SempicModelException;
import fr.uga.miashs.sempic.SempicModelUniqueException;
import fr.uga.miashs.sempic.dao.AlbumFacade;
import fr.uga.miashs.sempic.entities.Album;
import fr.uga.miashs.sempic.entities.SempicUser;
import java.net.URI;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author benjamin
 */
public class AlbumService {
    
    @Inject
    private AlbumFacade albumDAO;
   
    @Path("/albums")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<Album> listAll() {
        return albumDAO.findAll();
    }
    
    @Path("/albums/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public Album get(@PathParam("id") long id) {
        return albumDAO.readEager(id);
    }
    
    @Path("/albums")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_XML)
    @POST
    public Response create(Album album, @Context UriInfo uriInfo) throws SempicModelException {
        try {
            long id = albumDAO.create(album);
            URI location = uriInfo.getRequestUriBuilder()
                             .path(String.valueOf(id))
                             .build();
            return Response.created(location).entity(album).build();
        } catch (SempicModelUniqueException e){
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        }
    }
    
}
