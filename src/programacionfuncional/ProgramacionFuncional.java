/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package programacionfuncional;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author DELL
 */
public class ProgramacionFuncional {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Map<String, List<String>> mapa = new HashMap<>();
        List<String[]> lista = new ArrayList<>();
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Seleccione el archivo CSV");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("csv", "csv");
        chooser.setFileFilter(filtro);
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

            String s;
            BufferedReader entrada;

            try {
                int cont = 0;
                int tam = 0;
                entrada = new BufferedReader(new FileReader(chooser.getSelectedFile()));
                s = entrada.readLine();
                String[] titulos = s.split(",");
                tam = titulos.length;
                while ((s = entrada.readLine()) != null) {
                    for (int i = 0; i < tam; i++) {
                        String[] fila = {titulos[i], s.split(",")[i]};
                        lista.add(fila);
                    }
                }
                entrada.close();
                for (int i = 0; i < tam; i++) {
                    mapa.put(titulos[i], ProgramacionFuncional.extractor(lista, titulos[i]));
                }
                Map<String, List<String>> enfermedadesmap = mapa.entrySet().stream().filter(x -> x.getKey().equals("causa103")).collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
                List<String> enfermedades = enfermedadesmap.get("causa103");
                Map<String, List<String>> edadesmap = mapa.entrySet().stream().filter(x -> x.getKey().equals("edad")).collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
                List<String> edades = edadesmap.get("edad");
                //edades.forEach(x -> System.out.printf("%s%n", x));
                int suma = 0;
                suma = edades.stream().map((edad) -> Integer.parseInt(edad)).reduce(suma, Integer::sum);
                List<String> apariciones = new ArrayList<>();
                int mayor = 0;
                String enmayor = "";
                for (String enfermedade : enfermedades) {
                    if (!apariciones.contains(enfermedade)) {
                        int frecuencia = 0;
                        frecuencia = enfermedades.stream().filter((enfermedad) -> (enfermedade.equals(enfermedad))).map((_item) -> 1).reduce(frecuencia, Integer::sum);
                        apariciones.add(enfermedade);
                        System.out.println("La enfermedad: " + enfermedade + " aparece " + frecuencia + " veces");
                        if (frecuencia > mayor) {
                            mayor = frecuencia;
                            enmayor = enfermedade;
                        }
                    }
                }
                JOptionPane.showMessageDialog(null, "La enfermedad que mas mortalidades ha causado en el año de estudio es:\n"
                        + enmayor + "\nCon " + mayor + " muertes\nLa edad promedio de muerte en el año de estudio es:\n" + (suma / edades.size()) + " años");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ProgramacionFuncional.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ProgramacionFuncional.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static List<String> extractor(List<String[]> lista, String analisis) {
        List<String> salida = new ArrayList<>();
        lista.stream().filter((estructura) -> (estructura[0].equals(analisis))).forEachOrdered((estructura) -> {
            salida.add(estructura[1]);
        });
        return salida;
    }

}
