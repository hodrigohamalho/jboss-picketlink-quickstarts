package org.jboss.as.quickstarts.picketlink.angularjs.security.service;

import org.jboss.as.quickstarts.picketlink.angularjs.model.Person;
import org.jboss.as.quickstarts.picketlink.angularjs.security.model.ApplicationRole;
import org.jboss.as.quickstarts.picketlink.angularjs.security.model.IdentityModelManager;
import org.jboss.as.quickstarts.picketlink.angularjs.security.model.MyUser;
import org.jboss.as.quickstarts.picketlink.angularjs.util.MessageBuilder;
import org.picketlink.authorization.annotations.RolesAllowed;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@javax.ejb.Stateless
@Path("/private/account")
@RolesAllowed(ApplicationRole.ADMINISTRATOR)
public class AccountService {

    @Inject
    private IdentityModelManager identityModelManager;
    
    @POST
    @Path("enableAccount")
    @Produces(MediaType.APPLICATION_JSON)
    public Response enable(Person passedUser) {
        MessageBuilder message;

        try {
            MyUser user = this.identityModelManager.findByLoginName(passedUser.getEmail());

            if (user == null) {
                return MessageBuilder.badRequest().message("Invalid account.").build();
            }
            
            if(user.isEnabled()) {
                return MessageBuilder.badRequest().message("Account is already enabled.").build();
            }

            this.identityModelManager.enableAccount(user);

            message = MessageBuilder.ok().message("Account is now enabled.");
        } catch (Exception e) {
            message = MessageBuilder.badRequest().message(e.getMessage());
        }

        return message.build();
    }

    @POST
    @Path("disableAccount")
    @Produces(MediaType.APPLICATION_JSON)
    public Response disable(Person passedUser) {
        MessageBuilder message;

        try {
            MyUser user = this.identityModelManager.findByLoginName(passedUser.getEmail());

            if (user == null) {
                return MessageBuilder.badRequest().message("Invalid account.").build();
            }

            if(!user.isEnabled()) {
                return MessageBuilder.badRequest().message("Accound is already disabled.").build();
            }

            this.identityModelManager.disableAccount(user);

            message = MessageBuilder.ok().message("Account is now disabled.");
        } catch (Exception e) {
            message = MessageBuilder.badRequest().message(e.getMessage());
        }

        return message.build();
    }
}
