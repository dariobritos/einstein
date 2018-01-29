package org.proygrad.einstein.util;

import org.proygrad.einstein.api.ParameterTO;
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

    public void mostrarCalculateOrig1(ParameterTO crackDepth, ParameterTO crackLength, ParameterTO wall_thickness, ParameterTO fractureToughness, ParameterTO inner_radius, ParameterTO yieldStress, ParameterTO operatingPressure) {
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


    public void mostrarCalculateSafeZoneIf(Double Lr, Double Lr2, Double Lr6, Double expLr6, Double fLr){
        Double p1=(1 - (0.14 * Lr2));
        Double p2=(0.3 + (0.7 * expLr6));
        Double p3=p1*p2;

        String line = "";
        line = line + "Lr:" + Lr.toString();
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


    public void mostrarCalculateKrDentro(Double adivc, Double adivt, Double powAdivt2, Double sigM, Double fm0, Double fm1, Double fm2, Double fm3, Double fm, Double fb1, Double fb2, Double fb, Double ki) {

        String line = "";
        line = line + "adivc:" + adivc.toString();
        line = line + " adivt:" + adivt.toString();
        line = line + ", powAdivt2:" + powAdivt2.toString();
        line = line + ", sigM:" + sigM.toString();
        line = line + ", fm0:" + fm0.toString();
        line = line + ", fm1:" + fm1.toString();
        line = line + ", fm2:" + fm2.toString();
        line = line + ", fm3:" + fm3.toString();
        line = line + ", fm:" + fm.toString();
        line = line + ", fb1:" + fb1.toString();
        line = line + ", fb2:" + fb2.toString();
        line = line + ", fb:" + fb.toString();
        line = line + ", ki:" + ki.toString();
        line = line + "\n";

        saveLineTxt("testFileKrDentro", line);
    }
}
