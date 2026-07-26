package com.pantryhub.core.domain.shopping

import com.pantryhub.core.data.repository.ShoppingListRepository
import javax.inject.Inject

class RenameShoppingListUseCase @Inject constructor(
    private val repository: ShoppingListRepository
) {
    suspend operator fun invoke(listId: String, newName: String) {
        if (newName.isBlank()) return
        
        val list = repository.getList(listId) ?: return
        repository.saveList(list.copy(name = newName.trim()))
    }
}
