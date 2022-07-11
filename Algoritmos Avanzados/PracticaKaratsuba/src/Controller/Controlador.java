package Controller;

import Model.Data;
import View.Vista;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import practicaKaratsuba.PerEsdeveniments;
import practicaKaratsuba.PracticaKaratsuba;

/**
 *
 * @author Víctor Manuel Blanes Castro
 */
//TODO: Recordar borrar metodo testing() para la entrega
public class Controlador extends Thread implements PerEsdeveniments {

    private static PracticaKaratsuba main;
    private static Vista vista;
    private static Data modelo;
    private final static int UMBRAL_KARATSUBA = 2;
    private static boolean esEstudio = false, calculoEnProgreso = false;

    public Controlador(PracticaKaratsuba main) {
        this.main = main;
        this.vista = new Vista(this);
        this.modelo = new Data(this);
    }

    public Controlador() {
    }

    private void testing() {
        Random rng = new Random();
        int numtest = 50;
        int good = 0, bad = 0, totals = 0;
        System.out.println("--------------------SUMA--------------------");
        for (int i = 0; i < numtest; i++) {
            String result;
            int num1 = rng.nextInt(-1000, 1000);
            int num2 = rng.nextInt(-1000, 1000);
            int res1 = num1 + num2;
            String res2 = suma(String.valueOf(num1), String.valueOf(num2));
            if (res1 == Integer.valueOf(res2)) {
                result = "GOOD";
                good++;
                totals++;
            } else {
                result = "BAD";
                bad++;
                totals++;
                System.out.printf("   Values: %d and %d\tExpected: %d       \tGot: %s\t %s \n", num1, num2, res1, res2, result);
            }
        }
        System.out.println("--------------------------------------------");
        System.out.println("--------------------RESTA--------------------");
        for (int i = 0; i < numtest; i++) {
            String result;
            int num1 = rng.nextInt(-1000, 1000);
            int num2 = rng.nextInt(-1000, 1000);
            int res1 = num1 - num2;
            String res2 = resta(String.valueOf(num1), String.valueOf(num2));
            if (res1 == Integer.valueOf(res2)) {
                result = "GOOD";
                good++;
                totals++;
            } else {
                result = "BAD";
                bad++;
                totals++;
                System.out.printf("   Values: %d and %d\t\tExpected: %d       \t\tGot: %s\t\t %s \n", num1, num2, res1, res2, result);
            }
        }
        System.out.println("--------------------------------------------");
        System.out.println("-----------------TRADICIONAL-----------------");
        for (int i = 0; i < numtest; i++) {
            String result;
            int num1 = rng.nextInt(-1000, 1000);
            int num2 = rng.nextInt(-1000, 1000);
            int res1 = num1 * num2;
            String res2 = tradicional(String.valueOf(num1), String.valueOf(num2));
            if (res1 == Integer.valueOf(res2)) {
                result = "GOOD";
                good++;
                totals++;
            } else {
                result = "BAD";
                bad++;
                totals++;
                System.out.printf("   Values: %d and %d\tExpected: %d       \tGot: %s\t %s \n", num1, num2, res1, res2, result);
            }
        }
        System.out.println("---------------------------------------------");
        System.out.println("----------------KARATSUBA------------------");
        for (int i = 0; i < numtest; i++) {
            String result;
            int num1 = rng.nextInt(-1000, 1000);
            int num2 = rng.nextInt(-1000, 1000);
            String res1 = tradicional(String.valueOf(num1), String.valueOf(num2));
            String res2 = karatsuba(String.valueOf(num1), String.valueOf(num2), UMBRAL_KARATSUBA);
            if (Objects.equals(Integer.valueOf(res1), Integer.valueOf(res2))) {
                result = "GOOD";
                good++;
                totals++;
            } else {
                result = "BAD";
                bad++;
                totals++;
                System.out.printf("   Values: %d and %d\tExpected: %s       \tGot: %s\t %s \n", num1, num2, res1, res2, result);
            }
        }
        System.out.println("---------------------------------------------");
        System.out.printf("RESULTADOS:\n\tTotales: %d\n\tAciertos: %d\n\tFallos: %d\n", totals, good, bad);
        //
    }

    private void calculo() {
        String numero1 = modelo.getNumero1();
        String numero2 = modelo.getNumero2();
        String algoritmo = modelo.getAlgoritmo();
        String res = "";
        double start = System.nanoTime();
        switch (algoritmo) {
            case "Karatsuba":
                res = karatsuba(numero1, numero2, UMBRAL_KARATSUBA);
                break;
            case "Mixto":
                res = karatsuba(numero1, numero2, modelo.getUmbral());
                break;
            case "Tradicional":
                res = tradicional(numero1, numero2);
                break;
            default:
                vista.errorVentana("Error controlador", "El algoritmo elegido no es valido");
                break;
        }
        double end = System.nanoTime();
        modelo.setTiempoEjecucion((end - start) / 1000000000.0);
        modelo.setResultado(res);
        if (calculoEnProgreso) {
            notificar("Calculo Terminado");
        } else {
            notificar("Ejecucion Parada");
        }
    }

    private void calculoEstudio() {
        double start, end, time;
        for (int i = 1; i <= 700 & calculoEnProgreso; i++) {
            time = 0.0;
            modelo.setAlgoritmo("Karatsuba");
            for (int j = 0; j < 5; j++) {
                modelo.setNumero1(randomNDigitos(i));
                modelo.setNumero2(randomNDigitos(i));
                start = System.nanoTime();
                karatsuba(modelo.getNumero1(), modelo.getNumero2(), UMBRAL_KARATSUBA);
                end = System.nanoTime();
                time += (end - start) / 1000000000.0;
            }
            modelo.setTiemposEstudio(0, i - 1, time / 5.0);
            time = 0.0;
            modelo.setAlgoritmo("Tradicional");
            for (int j = 0; j < 5; j++) {
                modelo.setNumero1(randomNDigitos(i));
                modelo.setNumero2(randomNDigitos(i));
                start = System.nanoTime();
                tradicional(modelo.getNumero1(), modelo.getNumero2());
                end = System.nanoTime();
                time += (end - start) / 1000000000.0;
            }
            modelo.setTiemposEstudio(1, i - 1, time / 5.0);
            notificar("Actualizar Progreso" + i);
        }
        if (calculoEnProgreso) {
            double[][] data = modelo.getTiemposEstudio();
            boolean exit = false;
            int umbralCount = 15;
            for (int i = 0; i < data[0].length && !exit; i++) {
                double count = 0.0;
                double mediaKaratsuba = 0.0, mediaTradicional = 0.0;
                for (int j = 0; j < 15 && (i - j) >= 0; j++) {
                    count++;
                    mediaKaratsuba += data[0][i];
                    mediaTradicional += data[1][i];
                }
                mediaKaratsuba = mediaKaratsuba / count;
                mediaTradicional = mediaTradicional / count;
                if (mediaKaratsuba < mediaTradicional) {
                    umbralCount--;
                } else {
                    umbralCount = 15;
                }

                if (umbralCount == 0) {
                    modelo.setUmbral(i);
                    exit = true;
                }
            }
            notificar("Actualizar Grafica");
        } else {
            notificar("Ejecucion Parada");
        }
    }

    private String karatsuba(String numero1, String numero2, int umbral) {
        boolean esNegativo = false;
        if (esNegativo(numero1) ^ esNegativo(numero2)) {
            esNegativo = true;
        }
        if (esNegativo(numero1)) {
            numero1 = invertirSigno(numero1);
        }
        if (esNegativo(numero2)) {
            numero2 = invertirSigno(numero2);
        }

        if (esNegativo) {
            return invertirSigno(trueKaratsuba(numero1, numero2, umbral));
        } else {
            return trueKaratsuba(numero1, numero2, umbral);
        }
    }

    private String trueKaratsuba(String numero1, String numero2, int umbral) {
        String res;
        if (numero1.length() < numero2.length()) {
            numero1 = ponerZerosIzq(numero1, numero2.length());
        } else if (numero2.length() < numero1.length()) {
            numero2 = ponerZerosIzq(numero2, numero1.length());
        }
        char[] num1 = numero1.toCharArray();
        char[] num2 = numero2.toCharArray();

        if (num1.length >= umbral) {
            int half;
            if (num1.length % 2 != 0) {
                half = (num1.length / 2) + 1;
            } else {
                half = num1.length / 2;
            }
            char[] a = Arrays.copyOfRange(num1, 0, half);
            char[] b = Arrays.copyOfRange(num1, half, num1.length);
            char[] c = Arrays.copyOfRange(num2, 0, half);
            char[] d = Arrays.copyOfRange(num2, half, num2.length);

            String ac = trueKaratsuba(String.valueOf(a), String.valueOf(c), umbral);
            String bd = trueKaratsuba(String.valueOf(b), String.valueOf(d), umbral);
            String adbc = resta(trueKaratsuba(suma(String.valueOf(a), String.valueOf(b)), suma(String.valueOf(c), String.valueOf(d)), umbral), suma(ac, bd));

            half = num1.length / 2;
            ac = ponerZerosDer(ac, 2 * half);
            adbc = ponerZerosDer(adbc, half);

            res = suma(ac, adbc);
            res = suma(res, bd);
            return res;
        } else {
            return tradicional(numero1, numero2);
        }
    }

    private String tradicional(String numero1, String numero2) {
        boolean esNegativo = false;
        if (esNegativo(numero1) ^ esNegativo(numero2)) {
            esNegativo = true;
        }
        if (esNegativo(numero1)) {
            numero1 = invertirSigno(numero1);
        }
        if (esNegativo(numero2)) {
            numero2 = invertirSigno(numero2);
        }
        char[] num1_reversed = reverse(numero1.toCharArray());
        char[] num2_reversed = reverse(numero2.toCharArray());
        int num1, num2, carryOverNum;
        int temp;
        String res, finalRes = "0";
        char carryOver;
        for (int i = 0; i < num1_reversed.length; i++) {
            carryOver = '0';
            res = "";
            for (int k = 0; k < i; k++) {
                res = res + '0';
            }
            for (int j = 0; j < num2_reversed.length; j++) {
                num1 = Character.getNumericValue(num1_reversed[i]);
                num2 = Character.getNumericValue(num2_reversed[j]);
                carryOverNum = Character.getNumericValue(carryOver);

                temp = (num1 * num2) + carryOverNum;
                char[] tempChar = String.valueOf(temp).toCharArray();

                if (tempChar.length > 1) {
                    carryOver = tempChar[0];
                    res = tempChar[1] + res;
                } else {
                    carryOver = '0';
                    res = tempChar[0] + res;
                }
            }
            if (carryOver != '0') {
                res = carryOver + res;
            }
            finalRes = suma(finalRes, res);
        }
        if (esNegativo) {
            finalRes = invertirSigno(finalRes);
        }
        return finalRes;
    }

    private String trueSuma(String numero1, String numero2) {
        if (numero1.length() > numero2.length()) {
            String temp = numero1;
            numero1 = numero2;
            numero2 = temp;
        }
        char[] num1_reversed = reverse(numero1.toCharArray());
        char[] num2_reversed = reverse(numero2.toCharArray());
        char[] tempChar;
        int num1, num2, carryOverNum, temp, i;
        String res = "", finalRes = "";
        char carryOver = '0';
        for (i = 0; i < num1_reversed.length; i++) {
            num1 = Character.getNumericValue(num1_reversed[i]);
            num2 = Character.getNumericValue(num2_reversed[i]);
            carryOverNum = Character.getNumericValue(carryOver);

            temp = num1 + num2 + carryOverNum;
            tempChar = String.valueOf(temp).toCharArray();

            if (tempChar.length > 1) {
                carryOver = tempChar[0];
                res = tempChar[1] + res;
            } else {
                carryOver = '0';
                res = tempChar[0] + res;
            }
        }
        while (i < num2_reversed.length) {
            if (carryOver == '0') {
                res = num2_reversed[i] + res;
            } else {
                num2 = Character.getNumericValue(num2_reversed[i]);
                carryOverNum = Character.getNumericValue(carryOver);

                temp = num2 + carryOverNum;
                tempChar = String.valueOf(temp).toCharArray();
                if (tempChar.length > 1) {
                    carryOver = tempChar[0];
                    res = tempChar[1] + res;
                } else {
                    carryOver = '0';
                    res = tempChar[0] + res;
                }
            }
            i++;
        }
        if (carryOver != '0') {
            res = carryOver + res;
        }
        return res;
    }

    private String trueResta(String numero1, String numero2) {
        boolean invertir = false;
        if (esMayor(numero2, numero1)) {
            invertir = true;
            String temp = numero1;
            numero1 = numero2;
            numero2 = temp;
        }
        char[] num1_reversed = reverse(numero1.toCharArray());
        char[] num2_reversed = reverse(numero2.toCharArray());
        char[] tempChar;
        int num1, num2, temp, i;
        String res = "", finalRes = "";
        char carryOver = '0';
        for (i = 0; i < num2_reversed.length; i++) {
            num1 = Character.getNumericValue(num1_reversed[i]);
            num2 = Character.getNumericValue(num2_reversed[i]);

            if (carryOver != '0') {
                num1--;
                carryOver = '0';
            }

            if (num2 > num1) {
                carryOver = '1';
                num1 += 10;
                temp = num1 - num2;
            } else {
                temp = num1 - num2;
            }

            tempChar = String.valueOf(temp).toCharArray();

            res = tempChar[0] + res;
        }
        while (i < num1_reversed.length) {
            if (carryOver == '0') {
                res = num1_reversed[i] + res;
            } else {
                num1 = Character.getNumericValue(num1_reversed[i]);
                num1--;

                if (num1 < 0) {
                    carryOver = '1';
                    num1 += 10;
                } else {
                    carryOver = '0';
                }
                temp = num1;
                tempChar = String.valueOf(temp).toCharArray();
                res = tempChar[0] + res;
            }
            i++;
        }
        if (invertir) {
            res = invertirSigno(res);
        }
        return res;
    }

    private String quitarZerosIzq(String numero) {
        char[] num = numero.toCharArray();
        boolean esNegativo = false;
        String res = "";
        int i;
        if (num[0] == '-') {
            num[0] = '0';
            esNegativo = true;
        }
        for (i = 0; i < num.length && (num[i] == '0' || num[i] == '-'); i++) {
        }
        while (i < num.length) {
            res = res + num[i];
            i++;
        }
        if (res == "") {
            res = "0";
        }
        if (esNegativo) {
            res = invertirSigno(res);
        }
        return res;
    }

    private String ponerZerosIzq(String numero, int longitud) {
        for (int i = numero.length(); i < longitud; i++) {
            numero = "0" + numero;
        }
        return numero;
    }

    private String ponerZerosDer(String numero, int cantidad) {
        for (int i = 0; i < cantidad; i++) {
            numero = numero + 0;
        }
        return numero;
    }

    private char[] reverse(char[] array) {
        int start = 0, end = array.length - 1;
        char temp;
        while (start < end) {
            temp = array[start];
            array[start] = array[end];
            array[end] = temp;
            start++;
            end--;
        }
        return array;
    }

    private boolean esNegativo(String numero) {
        char[] num = numero.toCharArray();
        return num[0] == '-';
    }

    private String resta(String numero1, String numero2) {
        String res;
        if (esNegativo(numero1) && esNegativo(numero2)) {
            res = invertirSigno(trueResta(invertirSigno(numero1), invertirSigno(numero2)));
        } else if (esNegativo(numero1)) {
            res = invertirSigno(trueSuma(invertirSigno(numero1), numero2));
        } else if (esNegativo(numero2)) {
            res = trueSuma(numero1, invertirSigno(numero2));
        } else {
            res = trueResta(numero1, numero2);
        }
        res = quitarZerosIzq(res);
        return res;
    }

    private String suma(String numero1, String numero2) {
        String res;
        if (esNegativo(numero1) && esNegativo(numero2)) {
            res = invertirSigno(trueSuma(invertirSigno(numero1), invertirSigno(numero2)));
        } else if (esNegativo(numero1)) {
            res = invertirSigno(trueResta(invertirSigno(numero1), numero2));
        } else if (esNegativo(numero2)) {
            res = trueResta(numero1, invertirSigno(numero2));
        } else {
            res = trueSuma(numero1, numero2);
        }
        res = quitarZerosIzq(res);
        return res;
    }

    private String invertirSigno(String numero) {
        if (esNegativo(numero)) {
            return numero.substring(1);
        } else {
            return '-' + numero;
        }
    }

    private boolean esMayor(String numero1, String numero2) {
        char[] num1 = numero1.toCharArray();
        char[] num2 = numero2.toCharArray();
        boolean exit = false;
        if (num1.length > num2.length) {
            return true;
        } else if (num2.length > num1.length) {
            return false;
        } else {
            for (int i = 0; i < num1.length; i++) {
                if (Character.getNumericValue(num1[i]) > Character.getNumericValue(num2[i])) {
                    return true;
                } else if (Character.getNumericValue(num2[i]) > Character.getNumericValue(num1[i])) {
                    return false;
                }
            }
        }
        return false;
    }

    private String randomNDigitos(int n) {
        Random rng = new Random();
        String res = "";
        if (rng.nextInt(0, 10) % 2 == 0) {
            res = res + "-";
        }
        res = res + String.valueOf(rng.nextInt(1, 10));
        for (int i = 1; i < n; i++) {
            res = res + String.valueOf(rng.nextInt(0, 10));
        }
        return res;
    }

    public String getNumero1() {
        return modelo.getNumero1();
    }

    public String getNumero2() {
        return modelo.getNumero2();
    }

    public String getAlgoritmo() {
        return modelo.getAlgoritmo();
    }

    public String getResultado() {
        return modelo.getResultado();
    }

    public double getTiempoEjecuccion() {
        return modelo.getTiempoEjecucion();
    }

    public double[][] getTiemposEstudio() {
        return modelo.getTiemposEstudio();
    }

    public int getUmbral() {
        return modelo.getUmbral();
    }

    @Override
    public void run() {
        calculoEnProgreso = true;
        if (esEstudio) {
            calculoEstudio();
        } else {
            calculo();
        }
        calculoEnProgreso = false;
    }

    @Override
    public void notificar(String s) {
        if (s.startsWith("Actualizar Modelo")) {
            modelo.notificar(s);
        } else if (s.startsWith("Calculo Terminado") || s.startsWith("Actualizar Progreso")
                || s.startsWith("Actualizar Grafica") || s.startsWith("Ejecucion Parada")) {
            vista.notificar(s);
        } else if (s.startsWith("Ejecutar Estudio")) {
            if (!calculoEnProgreso) {
                calculoEnProgreso = true;
                esEstudio = true;
                new Thread(this).start();
            }
        } else if (s.startsWith("Ejecutar Calculo")) {
            if (!calculoEnProgreso) {
                calculoEnProgreso = true;
                esEstudio = false;
                new Thread(this).start();
            }
        } else if (s.startsWith("Parar Ejecucion")) {
            calculoEnProgreso = false;
        }
    }

}
