package org.proygrad.einstein.service.nontransactional;

import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Service
public class CalculateTxtSupport {


    public void saveLineTxt(String fileName, String line){

        String ruta = "C:\\Users\\usuario\\Desktop\\"+fileName+".txt";
        File archivo = new File(ruta);
        BufferedWriter bw;

        try {
            bw = new BufferedWriter(new FileWriter(archivo, true));
            bw.write(line);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }





}
