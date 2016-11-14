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

        //OBTIENEN LOS TRABAJOS EN COMUN ENTRE EL USUARIO RECOMENDADOR Y EL USUARIO RECOMENDADO
        return comparadorTrabajos(recomendador, recomendado);
    }

    public Set<Trabajo> getTrabajosComunesUsuarios(String recomendado){

        //OBTIENEN LOS TRABAJOS EN COMUN ENTRE EL USUARIO AUTENTICADO Y EL USUARIO RECOMENDADO
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();

        return comparadorTrabajos(user.getLogin(), recomendado);
    }

    private Set<Trabajo> comparadorTrabajos(String user1, String user2){

        Set<Trabajo> trabajosComunes = new HashSet<>();

        //USER 1 -> Usuario conectado
        //USER 2 -> Usuario a recomendar

        List<Trabajo> trabajosUsuario1 = trabajoRepository.findByTrabajosByTrabajador(user1);
        List<Trabajo> trabajosUsuario2 = trabajoRepository.findByTrabajosByTrabajador(user2);

        for(Trabajo trabajoRecomendador: trabajosUsuario1){
            for(Trabajo trabajoRecomendado: trabajosUsuario2){
                if(trabajoRecomendador.getEmpresa().getId().equals(trabajoRecomendado.getEmpresa().getId())){
                    if((trabajoRecomendador.getFechaInicio() != null && (trabajoRecomendador.getFechaFin() != null || trabajoRecomendador.isActualmente()))
                        && ((trabajoRecomendado.getFechaFin() != null || trabajoRecomendado.isActualmente()) && trabajoRecomendado.getFechaInicio() != null)){

                        if(trabajoRecomendador.isActualmente() || trabajoRecomendador.isActualmente() == null){
                            if((trabajoRecomendado.getFechaInicio().isAfter(trabajoRecomendador.getFechaInicio()) || trabajoRecomendado.getFechaInicio().isBefore(trabajoRecomendador.getFechaInicio()))
                                || trabajoRecomendado.getFechaInicio() == trabajoRecomendado.getFechaInicio()){
                                trabajosComunes.add(trabajoRecomendado);
                            }
                        }
                        else{
                            if(trabajoRecomendado.getFechaInicio().isBefore(trabajoRecomendador.getFechaFin()) && trabajoRecomendado.getFechaFin().isAfter(trabajoRecomendador.getFechaInicio())){
                                trabajosComunes.add(trabajoRecomendado);
                            }
                        }

                    }
                }
            }
        }

        return trabajosComunes;
    }

}
