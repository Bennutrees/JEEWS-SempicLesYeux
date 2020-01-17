/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.restservice;

import fr.uga.miashs.sempic.ApplicationConfig;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.*;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import org.eclipse.persistence.jaxb.rs.MOXyJsonProvider;

/**
 *
 * @author benjamin
 */
@ApplicationPath(ApplicationConfig.WEB_API)
public class RestApplication extends Application {
    
    public RestApplication() {
        
    }
    
    /**
     * Web services classes and related have to be registered here
     * @return 
     */
    @Override
    public Set<Class<?>> getClasses() {
        Set res = new HashSet();
        String[] features = {
            "org.glassfish.jersey.moxy.json.MoxyJsonFeature"/*,
            "org.apache.cxf.interceptor.secutity.SecureAnnotationsInterceptor"*/
        };
        for (String fName : features) {
            try {
                Class cls = Class.forName(fName);
                res.add(cls);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(RestApplication.class.getName()).log(Level.WARNING, fName+" not available");
            }
        }
        
        res.add(MOXyJsonProvider.class); //Uses moxy a JAXB provider that produce Json from xml annotations
        res.add(SempicUserService.class);
        res.add(AlbumService.class);
        return res;
    }
}
