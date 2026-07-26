package com.pantryhub.core.domain.shopping

import com.pantryhub.core.data.repository.ShoppingListRepository
import javax.inject.Inject

class DeleteShoppingListUseCase @Inject constructor(
    private val repository: ShoppingListRepository
) {
    suspend operator fun invoke(listId: String) {
        val list = repository.getList(listId) ?: return
        repository.deleteList(list)
    }
}
