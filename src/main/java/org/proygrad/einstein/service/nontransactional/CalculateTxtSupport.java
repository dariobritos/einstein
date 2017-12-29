package org.proygrad.einstein.service.nontransactional;

import org.proygrad.einstein.api.Parameter;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Service
public class CalculateTxtSupport {


    private  void saveLineTxt(String fileName, String line){

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

    public void mostrarCalculateOrig1(Parameter crackDepth, Parameter crackLength, Parameter wall_thickness, Parameter fractureToughness, Parameter inner_radius, Parameter yieldStress, Parameter operatingPressure) {
        String line = "";
        line = line + "crackDepth:" + crackDepth.getValue().toString();
        line = line + ", crackLength:" + crackLength.getValue().toString();
        line = line + ", wall_thickness:" + wall_thickness.getValue().toString();
        line = line + ", fractureToughness:" + fractureToughness.getValue().toString();
        line = line + ", inner_radius:" + inner_radius.getValue().toString();
        line = line + ", yieldStress:" + yieldStress.getValue().toString();
        line = line + ", operatingPressure:" + operatingPressure.getValue().toString();
        line = line + "\n";

        saveLineTxt("testFileOrig1", line);
    }

    public void mostrarCalculateOrig(Double crackDepth, Double crackLength, Double wall_thickness, Double fractureToughness, Double pRi, Double yieldStress, Double operatingPressure) {
        String line = "";
        line = line + "crackDepth:" + crackDepth.toString();
        line = line + ", crackLength:" + crackLength.toString();
        line = line + ", wall_thickness:" + wall_thickness.toString();
        line = line + ", fractureToughness:" + fractureToughness.toString();
        line = line + ", pRi:" + pRi.toString();
        line = line + ", yieldStress:" + yieldStress.toString();
        line = line + ", operatingPressure:" + operatingPressure.toString();
        line = line + "\n";

        saveLineTxt("testFileOrig", line);
    }

    public void mostrarIsSafeZone(Double gx, Double fLr, Double  Kr, Double Lr ) {

        String line = "";
        line = line + "gx:" + gx.toString();
        line = line + ", fLr:" + fLr.toString();
        line = line + ", Kr:" + Kr.toString();
        line = line + ", Lr:" + Lr.toString();
        line = line + "\n";

        saveLineTxt("testFileIsSafeZone", line);
    }

    public void mostrarCalculateKr(Double a, Double c, Double t, Double KIC, Double PRi, Double SigS, Double Kr) {

        String line = "";
        line = line + "a:" + a.toString();
        line = line + ", c:" + c.toString();
        line = line + ", t:" + t.toString();
        line = line + ", KIC:" + KIC.toString();
        line = line + ", PRi:" + PRi.toString();
        line = line + ", SigS:" + SigS.toString();
        line = line + ", Kr:" + Kr.toString();
        line = line + "\n";

        saveLineTxt("testFileKr", line);
    }

    public void mostrarCalculateLr(Double a, Double c, Double t, Double ri, Double pRi, Double p, Double sigS, Double Lr) {

        String line = "";
        line = line + "a:" + a.toString();
        line = line + ", c:" + c.toString();
        line = line + ", t:" + t.toString();
        line = line + ", ri:" + ri.toString();
        line = line + ", pRi:" + pRi.toString();
        line = line + ", p:" + p.toString();
        line = line + ", sigS:" + sigS.toString();
        line = line + ", Lr:" + Lr.toString();
        line = line + "\n";

        saveLineTxt("testFileLr", line);
    }


    public void mostrarCalculateSafeZoneIf(Double Lr2, Double Lr6, Double expLr6, Double fLr){
        Double p1=(1 - (0.14 * Lr2));
        Double p2=(0.3 + (0.7 * expLr6));
        Double p3=p1*p2;

        String line = "";
        line = line + "Lr2:" + Lr2.toString();
        line = line + ", Lr6:" + Lr6.toString();
        line = line + ", expLr6:" + expLr6.toString();
        line = line + ", p1:" + p1.toString();
        line = line + ", p2:" + p2.toString();
        line = line + ", p3:" + p3.toString();
        line = line + ", fLr:" + fLr.toString();
        line = line + "\n";

        saveLineTxt("testFileIsSafeZoneIf", line);
    }



}
