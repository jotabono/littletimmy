package com.mycompany.myapp.service;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
}
