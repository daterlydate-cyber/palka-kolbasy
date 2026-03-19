package com.phantomclone.di

import com.phantomclone.engine.VirtualEngine
import com.phantomclone.engine.VirtualEngineImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module binding interface implementations to their concrete types.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class EngineModule {

    @Binds
    @Singleton
    abstract fun bindVirtualEngine(impl: VirtualEngineImpl): VirtualEngine
}
