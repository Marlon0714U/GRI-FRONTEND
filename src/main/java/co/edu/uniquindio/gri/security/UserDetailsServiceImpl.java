package co.edu.uniquindio.gri.security;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import co.edu.uniquindio.gri.dao.UserDAO;
import co.edu.uniquindio.gri.model.Rol;

// TODO: Auto-generated Javadoc
/**
 * Class UserDetailsServiceImpl.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    /** The user DAO. */
    @Autowired
    UserDAO userDAO;
	
    /* (non-Javadoc)
     * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
     */
    @Override
     public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
     //Buscar el usuario con el repositorio y si no existe lanzar una exepcion
    	 co.edu.uniquindio.gri.model.User appUser = userDAO.findOne(username);
    
    if(appUser == null){
    	throw new  UsernameNotFoundException("No existe usuario");
    }
 
    //Se carga una lista de 'Autorithies' o Privilegios del usuario. Esta lista se mapea según la especificación de Spring Security.  
    List <GrantedAuthority> grantList = new ArrayList<GrantedAuthority>() ;
    Rol authority = appUser.getRol();
    GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority.getRole());
    grantList.add(grantedAuthority);
    		
    //Finalmente se crea y se retorna el objeto con los detalles del usuario. Esto se incluirá en la sesión. 
    UserDetails user = new User(appUser.getUsername(), appUser.getPassword(), grantList);
         return user;
    }
}