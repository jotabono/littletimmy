package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.security.SecurityUtils;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.web.rest.vm.ManagedUserVM;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Xavi on 17/10/2016.
 */
@Service
@Transactional
public class UploadService {

    private File theDir = new File("./src/main/webapp/uploads");
    private byte[] bytes;
    private String nameFile;

    @Inject
    private UserService userService;

    @Inject
    private UserRepository userRepository;

    public void handleUpload(MultipartFile file, String name){

        this.nameFile = "";

        try {

            if (!this.theDir.exists()) {
                System.out.println("creating directory: /uploads");
                boolean result = false;

                try{
                    this.theDir.mkdir();
                    result = true;
                }
                catch(SecurityException se){
                    //handle it
                }
                if(result) {
                    System.out.println("DIR created");
                }
            }

            //Get name of file
            this.nameFile = name;

            //Create new file in path
            BufferedOutputStream stream =
                new BufferedOutputStream(new FileOutputStream(new File("./src/main/webapp/uploads/"+nameFile)));

            stream.write(file.getBytes());
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void handleUploadAndroid(MultipartFile file, String name){

        this.nameFile = "";

        try {

            if (!this.theDir.exists()) {
                System.out.println("creating directory: /uploads");
                boolean result = false;

                try{
                    this.theDir.mkdir();
                    result = true;
                }
                catch(SecurityException se){
                    //handle it
                }
                if(result) {
                    System.out.println("DIR created");
                }
            }

            //Get name of file
            this.nameFile = name;

            //Create new file in path
            BufferedOutputStream stream =
                new BufferedOutputStream(new FileOutputStream(new File("./src/main/webapp/uploads/"+nameFile)));

            stream.write(file.getBytes());
            stream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
        user.setImagen("uploads/"+nameFile);

        userService.updateUser(user.getFirstName(), user.getLastName(), user.getEmail(),
            user.getLangKey(), user.getImagen(), user.getFecha_nacimiento(), user.getDni(), user.getTelefono(),
            user.getDomicilio(), user.getWeb_personal(), user.getFacebook(), user.getTwitter(), user.getSkype(),
            user.getCorreo_alternativo(), user.getCarta_presentacion(), user.getCiudad(), user.getGithub());

    }
}
