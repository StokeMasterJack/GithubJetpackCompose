package com.smartsoft.github




enum class GhAction {
    FetchUsersAsync,
    FetchDummyUsers,
    ClearUsers
}

typealias GhDispatch = (ev: GhAction) -> Unit
