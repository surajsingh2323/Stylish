package com.example.stylish.di

import android.content.Context
import androidx.room.Room
import com.example.stylish.data.local.UserPreferencesDataStore
import com.example.stylish.data.local.dao.WishlistDao
import com.example.stylish.data.local.database.EcoMartDatabase
import com.example.stylish.data.remote.ProductApiService
import com.example.stylish.data.repository.UserSettingsRepositoryImpl
import com.example.stylish.data.repositoryimpl.AuthRepositoryImpl
import com.example.stylish.data.repositoryimpl.CartRepositoryImpl
import com.example.stylish.data.repositoryimpl.ProductRepositoryImpl
import com.example.stylish.data.repositoryimpl.UserPreferencesRepositoryImpl
import com.example.stylish.data.repositoryimpl.WishlistRepositoryImpl
import com.example.stylish.domain.repository.AuthRepository
import com.example.stylish.domain.repository.CartRepository
import com.example.stylish.domain.repository.ProductRepository
import com.example.stylish.domain.repository.UserPreferenceRepository
import com.example.stylish.domain.repository.UserSettingsRepository
import com.example.stylish.domain.repository.WishListRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton
import com.example.stylish.data.local.CartDataStore

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth{
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseDatabase{
        // Use the default database URL based on project ID
        val databaseUrl = "https://stylish-3f6d4-default-rtdb.firebaseio.com/"
        return FirebaseDatabase.getInstance(databaseUrl)
    }

    @Provides
    @Singleton
    fun provideUserPreferencesDataStore(@ApplicationContext context: Context): UserPreferencesDataStore {
        return UserPreferencesDataStore(context)
    }

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(Android) {
            defaultRequest {
                url("https://dummyjson.com/")
            }

            install(ContentNegotiation){
                json(Json{
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }

            install(Logging){
                level = LogLevel.BODY
            }
        }
    }

    @Provides
    @Singleton
    fun provideProductApiService(httpClient: HttpClient): ProductApiService {
        return ProductApiService(httpClient)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(firebaseAuth: FirebaseAuth): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideProductRepository(productApiService: ProductApiService): ProductRepository{
        return ProductRepositoryImpl(productApiService)
    }

    @Provides
    @Singleton
    fun provideUserPreferencesRepository(userPreferencesDataStore: UserPreferencesDataStore): UserPreferenceRepository{
        return UserPreferencesRepositoryImpl(userPreferencesDataStore)
    }

    @Provides
    @Singleton
    fun provideEcoMartDatabase(@ApplicationContext context: Context): EcoMartDatabase {
        return Room.databaseBuilder(
            context,
            EcoMartDatabase::class.java,
            "ecomart_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideWishlistDao(database: EcoMartDatabase): WishlistDao{
        return database.wishlistDao()
    }

    @Provides
    @Singleton
    fun provideWishlistRepository(wishlistDao: WishlistDao): WishListRepository {
        return WishlistRepositoryImpl(wishlistDao)
    }

    @Provides
    @Singleton
    fun provideCartDataStore(@ApplicationContext context: Context): CartDataStore{
        return CartDataStore(context)
    }

    @Provides
    @Singleton
    fun provideCartRepository(cartDataStore: CartDataStore): CartRepository{
        return CartRepositoryImpl(cartDataStore)
    }

    @Provides
    @Singleton
    fun provideUserSettingsRepository(database: FirebaseDatabase): UserSettingsRepository{
        return UserSettingsRepositoryImpl(database)
    }
}