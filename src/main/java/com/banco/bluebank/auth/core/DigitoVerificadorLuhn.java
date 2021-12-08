package com.banco.bluebank.auth.core;

import org.springframework.stereotype.Component;

@Component
public class DigitoVerificadorLuhn {

    public boolean verificaDigitoVerificador(String card) {
        if (card == null)
            return false;
        char digitoverificador = card.charAt(card.length() - 1);
        String digito = calculaDigitoVerificador(card.substring(0, card.length() - 1));
        return digitoverificador == digito.charAt(0);
    }

    public String calculaDigitoVerificador(String original) {
        if (original == null)
            return null;
        String digit;
        /* se convierte el número en un arreglo de digitos */
        int[] digits = new int[original.length()];
        for (int i = 0; i < original.length(); i++) {
            digits[i] = Character.getNumericValue(original.charAt(i));
        }

        /* se duplica cada dígito desde la derecha saltando de dos en dos*/
        for (int i = digits.length - 1; i >= 0; i -= 2)    {
            digits[i] += digits[i];

            /* si la suma de los digitos es más de 10, se resta 9 */
            if (digits[i] >= 10) {
                digits[i] = digits[i] - 9;
            }
        }
        int sum = 0;
        for (int i = 0; i < digits.length; i++) {
            sum += digits[i];
        }

        /* se multiplica por 9 */
        sum = sum * 9;

        /* se convierte a cadena para obtener facilmente el último dígito */
        digit = sum + "";
        return digit.substring(digit.length() - 1);
    }

}
