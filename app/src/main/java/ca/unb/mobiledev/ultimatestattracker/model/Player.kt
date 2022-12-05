package ca.unb.mobiledev.ultimatestattracker.model

class Player : java.io.Serializable {
    fun getFormattedName(): String {
        return "[$number] $fName $lName"
    }

    var fName: String
    var lName: String
    var number: Int
    var gender: GENDER

    constructor(fName: String, lName: String, number: Int, gender: GENDER){
        this.fName = fName
        this.lName = lName
        this.number = number
        this.gender = gender
    }
    enum class GENDER {
        Male, Female, Other
    }

    override fun toString(): String {
        return getFormattedName()
    }
}