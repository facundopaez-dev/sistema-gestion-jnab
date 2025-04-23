package com.ebcf.jnab.data.repository

import com.ebcf.jnab.data.model.SpeakerModel

class SpeakerRepository {

    private val hardcodedSpeakers = listOf(
        SpeakerModel(
            firstName = "Laura",
            lastName = "González",
            institution = "Universidad Nacional de Córdoba",
            email = "laura.gonzalez@unc.edu.ar"
        ),
        SpeakerModel(
            firstName = "Carlos",
            lastName = "Pérez",
            institution = "CONICET",
            email = "carlos.perez@conicet.gov.ar"
        ),
        SpeakerModel(
            firstName = "Ana",
            lastName = "Martínez",
            institution = "UBA - Facultad de Ciencias Naturales",
            email = "ana.martinez@fcn.uba.ar"
        ),
        SpeakerModel(
            firstName = "Jorge",
            lastName = "Sosa",
            institution = "Universidad Nacional del Sur",
            email = "jorge.sosa@uns.edu.ar"
        ),
        SpeakerModel(
            firstName = "Lucía",
            lastName = "Fernández",
            institution = "INTA",
            email = "lucia.fernandez@inta.gob.ar"
        ),
        SpeakerModel(
            firstName = "Mariano",
            lastName = "Rodríguez",
            institution = "UNLP - Facultad de Ciencias Exactas",
            email = "mariano.rodriguez@exactas.unlp.edu.ar"
        ),
        SpeakerModel(
            firstName = "Soledad",
            lastName = "Vega",
            institution = "Universidad Nacional de Rosario",
            email = "soledad.vega@unr.edu.ar"
        ),
        SpeakerModel(
            firstName = "Hernán",
            lastName = "Giménez",
            institution = "Universidad Nacional de Tucumán",
            email = "hernan.gimenez@unt.edu.ar"
        ),
        SpeakerModel(
            firstName = "Florencia",
            lastName = "Ruiz",
            institution = "CONICET - CCT Mendoza",
            email = "florencia.ruiz@conicet.gov.ar"
        ),
        SpeakerModel(
            firstName = "Diego",
            lastName = "López",
            institution = "Universidad Nacional del Comahue",
            email = "diego.lopez@unco.edu.ar"
        )
    )

    fun getAllSpeakers(): List<SpeakerModel> {
        return hardcodedSpeakers
    }
}
