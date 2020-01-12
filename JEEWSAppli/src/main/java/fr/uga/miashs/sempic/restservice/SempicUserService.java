/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.restservice;

import fr.uga.miashs.sempic.SempicModelException;
import fr.uga.miashs.sempic.SempicModelUniqueException;
import fr.uga.miashs.sempic.dao.SempicUserFacade;
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
public class SempicUserService {
    
    @Inject
    private SempicUserFacade userDao;
    
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<SempicUser> listAll() {
        return userDao.findAll();
    }
    
    @Path("/users/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public SempicUser get(@PathParam("id") long id) {
        return userDao.readEager(id);
    }
    
    @Path("/users")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_XML)
    @POST
    public Response create(SempicUser user, @Context UriInfo uriInfo) throws SempicModelException {
        try {
            long id = userDao.create(user);
            URI location = uriInfo.getRequestUriBuilder()
                             .path(String.valueOf(id))
                             .build();
            return Response.created(location).entity(user).build();
        } catch (SempicModelUniqueException e){
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        }
    }
}
