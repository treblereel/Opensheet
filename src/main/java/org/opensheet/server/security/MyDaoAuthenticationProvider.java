/*******************************************************************************
 * Copyright (c) 2012 Dmitry Tikhomirov.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Dmitry Tikhomirov - initial API and implementation
 ******************************************************************************/
package org.opensheet.server.security;

import org.opensheet.server.utils.Exceptions.BadAuthMethodException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.PlaintextPasswordEncoder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;



@Component("MyDaoAuthenticationProvider")
public class MyDaoAuthenticationProvider  extends AbstractUserDetailsAuthenticationProvider {

	@Autowired	private UserDetailsService userDetailsService;
	@Autowired	private UserPasswdCheck    userPasswdCheck;

	
	
	private boolean includeDetailsObject = true;
	private PasswordEncoder passwordEncoder = new PlaintextPasswordEncoder();
    private SaltSource saltSource;
    
    
    @SuppressWarnings("deprecation")
	@Override
    @Transactional
	protected void additionalAuthenticationChecks(UserDetails userDetails,	UsernamePasswordAuthenticationToken password)			throws AuthenticationException {
    	CustomUser customUser = (CustomUser) userDetails;

    	
    	
    	if (password.getCredentials() == null) {
    	
            logger.debug("Authentication failed: no credentials provided");

            
            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"),
                    includeDetailsObject ? userDetails : null);
                    
        }
    	
   
    	
    	try {
			if(userPasswdCheck.check(customUser,password.getCredentials().toString()) != true){
			        	throw new BadCredentialsException(messages.getMessage(
			                    "AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"),
			                    includeDetailsObject ?  customUser: null);
			        }
		} catch (BadAuthMethodException e) {
				e.printStackTrace();
			}
		
    	}
    	
  
    protected void doAfterPropertiesSet() throws Exception {
        Assert.notNull(this.userDetailsService, "A UserDetailsService must be set");
    }

    protected final UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        UserDetails loadedUser;

        try {
            loadedUser = this.getUserDetailsService().loadUserByUsername(username);
        }
        catch (DataAccessException repositoryProblem) {
            throw new AuthenticationServiceException(repositoryProblem.getMessage(), repositoryProblem);
        }

        if (loadedUser == null) {
            throw new AuthenticationServiceException(
                    "UserDetailsService returned null, which is an interface contract violation");
        }
        return loadedUser;
    }

    
    

    /**
     * Sets the PasswordEncoder instance to be used to encode and validate passwords.
     * If not set, {@link PlaintextPasswordEncoder} will be used by default.
     *
     * @param passwordEncoder The passwordEncoder to use
     */
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    protected PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    /**
     * The source of salts to use when decoding passwords. <code>null</code>
     * is a valid value, meaning the <code>DaoAuthenticationProvider</code>
     * will present <code>null</code> to the relevant <code>PasswordEncoder</code>.
     *
     * @param saltSource to use when attempting to decode passwords via the <code>PasswordEncoder</code>
     */
    public void setSaltSource(SaltSource saltSource) {
        this.saltSource = saltSource;
    }

    protected SaltSource getSaltSource() {
        return saltSource;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    protected UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    protected boolean isIncludeDetailsObject() {
        return includeDetailsObject;
    }

      public void setIncludeDetailsObject(boolean includeDetailsObject) {
        this.includeDetailsObject = includeDetailsObject;
    }

	


}





