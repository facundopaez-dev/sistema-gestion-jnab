package com.ebcf.jnab.data.repository

import com.ebcf.jnab.data.model.SpeakerModel

class SpeakerRepository {

    private val hardcodedSpeakers = listOf(
        SpeakerModel( // Agregamos el id
            id = 1,
            firstName = "Laura",
            lastName = "González",
            institution = "Universidad Nacional de Córdoba",
            email = "laura.gonzalez@unc.edu.ar"
        ),
        SpeakerModel( // Agregamos el id
            id = 2,
            firstName = "Carlos",
            lastName = "Pérez",
            institution = "CONICET",
            email = "carlos.perez@conicet.gov.ar"
        ),
        SpeakerModel( // Agregamos el id
            id = 3,
            firstName = "Ana",
            lastName = "Martínez",
            institution = "UBA - Facultad de Ciencias Naturales",
            email = "ana.martinez@fcn.uba.ar"
        ),
        SpeakerModel( // Agregamos el id
            id = 4,
            firstName = "Jorge",
            lastName = "Sosa",
            institution = "Universidad Nacional del Sur",
            email = "jorge.sosa@uns.edu.ar"
        ),
        SpeakerModel( // Agregamos el id
            id = 5,
            firstName = "Lucía",
            lastName = "Fernández",
            institution = "INTA",
            email = "lucia.fernandez@inta.gob.ar"
        ),
        SpeakerModel( // Agregamos el id
            id = 6,
            firstName = "Mariano",
            lastName = "Rodríguez",
            institution = "UNLP - Facultad de Ciencias Exactas",
            email = "mariano.rodriguez@exactas.unlp.edu.ar"
        ),
        SpeakerModel( // Agregamos el id
            id = 7,
            firstName = "Soledad",
            lastName = "Vega",
            institution = "Universidad Nacional de Rosario",
            email = "soledad.vega@unr.edu.ar"
        ),
        SpeakerModel( // Agregamos el id
            id = 8,
            firstName = "Hernán",
            lastName = "Giménez",
            institution = "Universidad Nacional de Tucumán",
            email = "hernan.gimenez@unt.edu.ar"
        ),
        SpeakerModel( // Agregamos el id
            id = 9,
            firstName = "Florencia",
            lastName = "Ruiz",
            institution = "CONICET - CCT Mendoza",
            email = "florencia.ruiz@conicet.gov.ar"
        ),
        SpeakerModel( // Agregamos el id
            id = 10,
            firstName = "Diego",
            lastName = "López",
            institution = "Universidad Nacional del Comahue",
            email = "diego.lopez@unco.edu.ar"
        )
    )

    fun getAllSpeakers(): List<SpeakerModel> {
        return hardcodedSpeakers
    }

    // Función para obtener un expositor por su ID
    fun getSpeakerById(id: Int): SpeakerModel? {
        return hardcodedSpeakers.find { it.id == id }
    }
}