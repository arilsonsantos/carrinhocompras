package br.com.improving.carrinho;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Teste {
    public static void main(String[] args) {
        BigDecimal resutlado;

        System.out.println("HALF_UP --------------------------");
        resutlado = new BigDecimal(1.1151).divide(new BigDecimal(1), 4, RoundingMode.HALF_UP);
        System.out.println(resutlado);
        resutlado = resutlado.setScale(2, RoundingMode.HALF_UP);
        System.out.println(resutlado);

        System.out.println("HALF_EVEN --------------------------");
        resutlado = new BigDecimal(1.0549).divide(new BigDecimal(1), 3, RoundingMode.HALF_EVEN);
        System.out.println(resutlado);
        resutlado = resutlado.setScale(2, RoundingMode.HALF_EVEN);
        System.out.println(resutlado);

        int[] lista = { 4, 5, 3, 2 };

        int menor = lista[0];
        int maior = 0;
        for (int i = 0; i < lista.length; i++) {
            if (lista[i] < menor) {
                menor = lista[i];
            }
            if (lista[i] > maior) {
                maior = lista[i];
            }
        }
        System.out.println(menor);
        System.out.println(maior);

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();

        boolean ordenado = true;

        while (ordenado) {
            ordenado = false;

            for (int i = 0; i < lista.length - 1; i++) {
                if (lista[i] > lista[i + 1]) {
                    int aux = lista[i];
                    lista[i] = lista[i + 1];
                    lista[i + 1] = aux;
                    ordenado = true;
                }
            }

        }

        for (int i = 0; i < lista.length; i++) {
            System.out.println(lista[i]);
        }

        System.out.println(buscaBinaria(lista, 5));

    }

    public static boolean buscaBinaria(int[] lista, int chave) {
        int idxIni = 0;
        int idxEnd = lista.length - 1;

        while (idxIni <= idxEnd) {
            int idxMid = (idxIni + idxEnd) >>> 1;
            int vrMid = lista[idxMid];

            int comp = chave - vrMid;

            if (comp > 0) {
                idxIni = idxMid + 1;
            } else if (comp < 0) {
                idxEnd = idxMid - 1;
            } else {
                return true;
            }
        }

        return false;

    }
}