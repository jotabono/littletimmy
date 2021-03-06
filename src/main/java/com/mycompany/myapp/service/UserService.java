package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Authority;
import com.mycompany.myapp.domain.Friend_user;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.AuthorityRepository;
import com.mycompany.myapp.repository.Friend_userRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.repository.search.UserSearchRepository;
import com.mycompany.myapp.security.AuthoritiesConstants;
import com.mycompany.myapp.security.SecurityUtils;
import com.mycompany.myapp.service.util.RandomUtil;
import com.mycompany.myapp.web.rest.vm.ManagedUserVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import javax.inject.Inject;
import java.util.*;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    public JdbcTokenStore jdbcTokenStore;

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserSearchRepository userSearchRepository;

    @Inject
    private AuthorityRepository authorityRepository;

    @Inject
    private Friend_userRepository friend_userRepository;

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository.findOneByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                userRepository.save(user);
                userSearchRepository.save(user);
                log.debug("Activated user: {}", user);
                return user;
            });
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);

        return userRepository.findOneByResetKey(key)
            .filter(user -> {
                ZonedDateTime oneDayAgo = ZonedDateTime.now().minusHours(24);
                return user.getResetDate().isAfter(oneDayAgo);
            })
            .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetKey(null);
                user.setResetDate(null);
                userRepository.save(user);
                return user;
            });
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository.findOneByEmail(mail)
            .filter(User::getActivated)
            .map(user -> {
                user.setResetKey(RandomUtil.generateResetKey());
                user.setResetDate(ZonedDateTime.now());
                userRepository.save(user);
                return user;
            });
    }

    public User createUser(String login, String password, String firstName, String lastName, String email,
                           String langKey) {

        User newUser = new User();
        Authority authority = authorityRepository.findOne(AuthoritiesConstants.USER);
        Set<Authority> authorities = new HashSet<>();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(login);
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmail(email);
        newUser.setLangKey(langKey);
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        authorities.add(authority);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        userSearchRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public User createUser(ManagedUserVM managedUserVM) {
        User user = new User();
        user.setLogin(managedUserVM.getLogin());
        user.setFirstName(managedUserVM.getFirstName());
        user.setLastName(managedUserVM.getLastName());
        user.setEmail(managedUserVM.getEmail());
        if (managedUserVM.getLangKey() == null) {
            user.setLangKey("es"); // default language
        } else {
            user.setLangKey(managedUserVM.getLangKey());
        }
        if (managedUserVM.getAuthorities() != null) {
            Set<Authority> authorities = new HashSet<>();
            managedUserVM.getAuthorities().stream().forEach(
                authority -> authorities.add(authorityRepository.findOne(authority))
            );
            user.setAuthorities(authorities);
        }
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(ZonedDateTime.now());
        user.setActivated(true);
        userRepository.save(user);
        userSearchRepository.save(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }

    public void updateUser(String firstName, String lastName, String email, String langKey, String imagen, LocalDate fecha_nacimiento, String dni, String telefono, String domicilio,
                           String web_personal, String facebook, String twitter, String skype, String correo_alternativo, String carta_presentacion, String ciudad, String github) {
        userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent(u -> {
            u.setFirstName(firstName);
            u.setLastName(lastName);
            u.setEmail(email);
            u.setLangKey(langKey);
            u.setImagen(imagen);
            u.setFecha_nacimiento(fecha_nacimiento);
            u.setDni(dni);
            u.setTelefono(telefono);
            u.setDomicilio(domicilio);
            u.setWeb_personal(web_personal);
            u.setFacebook(facebook);
            u.setTwitter(twitter);
            u.setSkype(skype);
            u.setCorreo_alternativo(correo_alternativo);
            u.setCarta_presentacion(carta_presentacion);
            u.setCiudad(ciudad);
            u.setGithub(github);
            userRepository.save(u);
            userSearchRepository.save(u);
            log.debug("Changed Information for User: {}", u);
        });
    }

    public void updateUser(Long id, String login, String firstName, String lastName, String email,
                           boolean activated, String langKey, Set<String> authorities) {

        userRepository
            .findOneById(id)
            .ifPresent(u -> {
                u.setLogin(login);
                u.setFirstName(firstName);
                u.setLastName(lastName);
                u.setEmail(email);
                u.setActivated(activated);
                u.setLangKey(langKey);
                Set<Authority> managedAuthorities = u.getAuthorities();
                managedAuthorities.clear();
                authorities.stream().forEach(
                    authority -> managedAuthorities.add(authorityRepository.findOne(authority))
                );
                log.debug("Changed Information for User: {}", u);
            });
    }

    public void deleteUser(String login) {
        jdbcTokenStore.findTokensByUserName(login).stream().forEach(token ->
            jdbcTokenStore.removeAccessToken(token));
        userRepository.findOneByLogin(login).ifPresent(u -> {
            userRepository.delete(u);
            userSearchRepository.delete(u);
            log.debug("Deleted User: {}", u);
        });
    }

    public void changePassword(String password) {
        userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent(u -> {
            String encryptedPassword = passwordEncoder.encode(password);
            u.setPassword(encryptedPassword);
            userRepository.save(u);
            log.debug("Changed password for User: {}", u);
        });
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneByLogin(login).map(u -> {
            u.getAuthorities().size();
            return u;
        });
    }

    @Transactional(readOnly = true)
    public User getUserWithAuthorities(Long id) {
        User user = userRepository.findOne(id);
        user.getAuthorities().size(); // eagerly load the association
        return user;
    }

    @Transactional(readOnly = true)
    public User getUserWithAuthorities() {
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
        user.getAuthorities().size(); // eagerly load the association
        return user;
    }


    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     * </p>
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        ZonedDateTime now = ZonedDateTime.now();
        List<User> users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(now.minusDays(3));
        for (User user : users) {
            log.debug("Deleting not activated user {}", user.getLogin());
            userRepository.delete(user);
            userSearchRepository.delete(user);
        }
    }


    public Set<User> getFriends(String login){
        Set<Friend_user> friends = friend_userRepository.findFriendsOfUser(login);
        Set<User> friendU = new HashSet<>();

        for(Friend_user friend_user: friends){
            if(friend_user.getFriend_to().getLogin().equals(login)) friendU.add(friend_user.getFriend_from());
            else friendU.add(friend_user.getFriend_to());
        }
        return friendU;
    }

    public List<User> getConectionPath (Long idUserSrc, Long idUserDst) {

        User primeroLista=userRepository.findOne(idUserSrc);
        User personaDestino = userRepository.findOne(idUserDst);

        Set<User> visitado = new HashSet<>();
        Map<User, User> predecesor = new HashMap<>();

        List<User> camino = new LinkedList();
        Queue<User> cola = new LinkedList();

        cola.add(primeroLista);
        visitado.add(primeroLista);

        boolean encontrado=false;

        while(!cola.isEmpty()){
            primeroLista = cola.poll();
            System.out.println("Voy a visitar la persona: ");
            System.out.println(primeroLista);
            if (primeroLista.equals(personaDestino)){
                encontrado=true;
                break;
            }else{
                for(User amigo : getFriends(primeroLista.getLogin())){
                    if(!visitado.contains(amigo)){
                        cola.add(amigo);
                        visitado.add(amigo);
                        predecesor.put(amigo, primeroLista);
                    }
                }
            }
        }

        if(encontrado){
            for(User persona = personaDestino;
                persona != null;
                persona = predecesor.get(persona)) {

                camino.add(persona);
            }
            Collections.reverse(camino);
            log.debug("Muestro el camino mínimo entre "+primeroLista.getFirstName()+" "+primeroLista.getLastName()
                +" y "+personaDestino.getFirstName()+" "+personaDestino.getLastName());
        }else
        {
            log.debug("No hay conexión entre "+primeroLista.getFirstName()+" "+primeroLista.getLastName()
                +" y "+personaDestino.getFirstName()+" "+personaDestino.getLastName());
        }
            log.debug("Shortest path between ", camino);
        return camino;
    }
}

