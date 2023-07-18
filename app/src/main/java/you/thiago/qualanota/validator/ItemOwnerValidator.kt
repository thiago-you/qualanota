package you.thiago.qualanota.validator

import you.thiago.qualanota.data.model.ItemOwner

object ItemOwnerValidator {

    fun validate(itemReview: ItemOwner) {
        if (itemReview.name.isNullOrBlank()) {
            throw Exception("O nome não pode ficar vazio.")
        }
    }
}
