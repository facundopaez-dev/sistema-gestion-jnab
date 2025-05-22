package com.ebcf.jnab.data.repository

import com.ebcf.jnab.domain.model.SpeakerModel

class SpeakerRepository {

    private val hardcodedSpeakers = listOf(
        SpeakerModel(
            id = 1,
            firstName = "Laura",
            lastName = "González",
            institution = "Universidad Nacional de Córdoba",
            email = "laura.gonzalez@unc.edu.ar",
            talks = emptyList()
        ),
        SpeakerModel(
            id = 2,
            firstName = "Carlos",
            lastName = "Pérez",
            institution = "CONICET",
            email = "carlos.perez@conicet.gov.ar",
            talks = emptyList()
        ),
        SpeakerModel(
            id = 3,
            firstName = "Ana",
            lastName = "Martínez",
            institution = "UBA - Facultad de Ciencias Naturales",
            email = "ana.martinez@fcn.uba.ar",
            talks = emptyList()
        ),
        SpeakerModel(
            id = 4,
            firstName = "Jorge",
            lastName = "Sosa",
            institution = "Universidad Nacional del Sur",
            email = "jorge.sosa@uns.edu.ar",
            talks = emptyList()
        ),
        SpeakerModel(
            id = 5,
            firstName = "Lucía",
            lastName = "Fernández",
            institution = "INTA",
            email = "lucia.fernandez@inta.gob.ar",
            talks = emptyList()
        ),
        SpeakerModel(
            id = 6,
            firstName = "Mariano",
            lastName = "Rodríguez",
            institution = "UNLP - Facultad de Ciencias Exactas",
            email = "mariano.rodriguez@exactas.unlp.edu.ar",
            talks = emptyList()
        ),
        SpeakerModel(
            id = 7,
            firstName = "Soledad",
            lastName = "Vega",
            institution = "Universidad Nacional de Rosario",
            email = "soledad.vega@unr.edu.ar",
            talks = emptyList()
        ),
        SpeakerModel(
            id = 8,
            firstName = "Hernán",
            lastName = "Giménez",
            institution = "Universidad Nacional de Tucumán",
            email = "hernan.gimenez@unt.edu.ar",
            talks = emptyList()
        ),
        SpeakerModel(
            id = 9,
            firstName = "Florencia",
            lastName = "Ruiz",
            institution = "CONICET - CCT Mendoza",
            email = "florencia.ruiz@conicet.gov.ar",
            talks = emptyList()
        ),
        SpeakerModel(
            id = 10,
            firstName = "Diego",
            lastName = "López",
            institution = "Universidad Nacional del Comahue",
            email = "diego.lopez@unco.edu.ar",
            talks = emptyList()
        )
    )

    fun getAllSpeakers(): List<SpeakerModel> = hardcodedSpeakers

    fun getSpeakerById(id: Int): SpeakerModel? =
        hardcodedSpeakers.find { it.id == id }
}
