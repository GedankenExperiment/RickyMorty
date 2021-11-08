package com.santi.rickymortyapi.model
data class RickyMortyCharacter (val info : Info, val results : List<FullCharacter>)
data class FullCharacter(
    var id: Int?,
    var image: String?,
    val location: FullLocation?,
    var name: String?,
    var species: String?,
    var status: String?,
)
data class Info(val count : Int?, val pages: String?, val next: String?, val prev : String?)

