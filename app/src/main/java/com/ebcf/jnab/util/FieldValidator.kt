package com.ebcf.jnab.util

import com.google.android.material.textfield.TextInputLayout

object FieldValidator {

    /**
     * Valida una condicion para un campo y actualiza el estado de error en un TextInputLayout.
     *
     * @param condition Condicion que determina si el campo es valido (true si valido, false si no).
     * @param layout El TextInputLayout asociado al campo que se esta validando.
     * @param errorMsg Mensaje de error que se mostrara si la condicion es falsa.
     *
     * @return `true` si la condicion es verdadera (campo valido), `false` si es falsa (campo invalido).
     *
     * Esta funcion establece el mensaje de error en el TextInputLayout si la validacion falla,
     * o limpia el error si la validacion es exitosa.
     */
    fun validateField(
        condition: Boolean, layout: TextInputLayout, errorMsg: String
    ): Boolean {
        if (!condition) {
            layout.error = errorMsg
            return false
        }

        layout.error = null
        return true
    }

}