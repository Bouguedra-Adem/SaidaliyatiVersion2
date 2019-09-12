package io.pharmacie.models

class Commune(var nomCommune: String?, wilayacode: Int) {
    var wiyalaCode: Int = 0
        private set



    init {
        this.wiyalaCode = wilayacode
    }

    fun getCodeWilaya(): Int {
        return wiyalaCode
    }

    fun setCodeWilaya(codeWilaya: Int) {
        this.wiyalaCode = codeWilaya
    }
}
