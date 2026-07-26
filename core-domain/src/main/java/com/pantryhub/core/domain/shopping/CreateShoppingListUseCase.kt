package com.pantryhub.core.domain.shopping

import com.pantryhub.core.data.repository.ShoppingListRepository
import com.pantryhub.core.model.shopping.ShoppingList
import com.pantryhub.core.model.shopping.ShoppingListType
import kotlinx.datetime.Clock
import java.util.UUID
import javax.inject.Inject

class CreateShoppingListUseCase @Inject constructor(
    private val shoppingListRepository: ShoppingListRepository
) {
    suspend operator fun invoke(name: String, type: ShoppingListType = ShoppingListType.REGULAR) {
        if (name.isBlank()) return

        val newList = ShoppingList(
            id = UUID.randomUUID().toString(),
            name = name.trim(),
            type = type,
            createdAt = Clock.System.now()
        )
        shoppingListRepository.saveList(newList)
    }
}
