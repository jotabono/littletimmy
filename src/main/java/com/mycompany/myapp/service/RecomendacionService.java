package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Trabajo;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.TrabajoRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by Xavi on 07/10/2016.
 */

@Service
@Transactional
public class RecomendacionService {

    @Autowired
    private TrabajoRepository trabajoRepository;

    @Autowired
    private UserRepository userRepository;

    public Set<Trabajo> getTrabajosUsuarios(String recomendador, String recomendado){

        //OBTENIEN LOS TRABAJOS EN COMUN ENTRE EL USUARIO RECOMENDADOR Y EL USUARIO RECOMENDADO
        return comparadorTrabajos(recomendador, recomendado);
    }

    public Set<Trabajo> getTrabajosComunesUsuarios(String recomendado){

        //OBTENIEN LOS TRABAJOS EN COMUN ENTRE EL USUARIO AUTENTICADO Y EL USUARIO RECOMENDADO
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();

        return comparadorTrabajos(user.getLogin(), recomendado);
    }

    private Set<Trabajo> comparadorTrabajos(String user1, String user2){

        Set<Trabajo> trabajosComunes = new HashSet<>();

        List<Trabajo> trabajosUsuario1 = trabajoRepository.findByTrabajosByTrabajador(user1);
        List<Trabajo> trabajosUsuario2 = trabajoRepository.findByTrabajosByTrabajador(user2);

        for(Trabajo trabajo: trabajosUsuario1){
            for(Trabajo trabajo1: trabajosUsuario2){
                if(trabajo.getEmpresa().getId().equals(trabajo1.getEmpresa().getId())){
                    if((trabajo.getFechaInicio() != null && trabajo.getFechaFin() != null)
                        && (trabajo1.getFechaFin() != null && trabajo1.getFechaInicio() != null)){

                        if(trabajo1.getFechaInicio().isBefore(trabajo.getFechaFin())
                            && trabajo1.getFechaFin().isAfter(trabajo.getFechaInicio())){
                            trabajosComunes.add(trabajo1);
                        }

                    }
                }
            }
        }

        return trabajosComunes;
    }

}
