package you.thiago.qualanota.validator

import you.thiago.qualanota.data.model.ItemReview

object ItemReviewValidator {

    private val invalidOwners = listOf(
        "null",
        "selecione",
        "selecione...",
        "selecione ...",
        "...",
        "select",
        "select...",
    )

    fun validate(itemReview: ItemReview) {
        val owner = itemReview.owner.toString().trim().lowercase()
        val rating = itemReview.rating ?: -1

        if (itemReview.title.isNullOrBlank()) {
            throw Exception("O título não pode ficar vazio.")
        }
        if (itemReview.owner.isNullOrBlank()) {
            throw Exception("O lugar não pode ficar vazio.")
        }
        if (owner in invalidOwners) {
            throw Exception("O lugar não pode ficar vazio.")
        }
        if (itemReview.rating == null) {
            throw Exception("A nota não pode ficar vazia.")
        }
        if (rating < 1 || rating > 5) {
            throw Exception("A nota não é válida.")
        }
    }
}
