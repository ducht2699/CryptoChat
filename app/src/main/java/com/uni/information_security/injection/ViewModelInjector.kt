package com.uni.information_security.injection

import com.uni.information_security.ui.chat.ChatViewModel
import com.uni.information_security.ui.chat_info.ChatInfoViewModel
import com.uni.information_security.ui.create_group.CreateGroupViewModel
import com.uni.information_security.ui.login.LoginViewModel
import com.uni.information_security.ui.main.MainViewModel
import com.uni.information_security.ui.personal.PersonalViewModel
import dagger.Component

/**
 * Component providing inject() methods for presenters.
 */
@Component(modules = [(NetworkModule::class)], dependencies = [ApplicationComponent::class])
@ViewModelScope
interface ViewModelInjector {
    /**
     * Injects required dependencies into the specified PostListViewModel.
     * @param postListViewModel PostListViewModel in which to inject the dependencies
     */
    fun inject(loginViewModel: LoginViewModel)
    fun inject(homeViewModel: MainViewModel)
    fun inject(personalViewModel: PersonalViewModel)
    fun inject(createGroupViewModel: CreateGroupViewModel)
    fun inject(chatViewModel: ChatViewModel)
    fun inject(chatInfoViewModel: ChatInfoViewModel)

    @Component.Builder
    interface Builder {
        fun build(): ViewModelInjector
        fun networkModule(networkModule: NetworkModule): Builder
        fun applicationComponent(applicationComponent: ApplicationComponent): Builder
    }
}