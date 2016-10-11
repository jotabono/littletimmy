package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Trabajo;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.TrabajoRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.*;

/**
 * Created by Xavi on 07/10/2016.
 */

@Service
@Transactional
public class RecomendacionService {

    @Autowired
    private TrabajoRepository trabajoRepository;

    public Set<Trabajo> getTrabajosUsuarios(String usuario1, String usuario2){

        Set<Trabajo> trabajosComunes = new HashSet<>();

        List<Trabajo> trabajosUsuario1 = trabajoRepository.findByTrabajosByTrabajador(usuario1);
        List<Trabajo> trabajosUsuario2 = trabajoRepository.findByTrabajosByTrabajador(usuario2);

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
