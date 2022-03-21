package com.example.android.hilt.di

import android.content.Context
import androidx.room.Room
import com.example.android.hilt.data.AppDatabase
import com.example.android.hilt.data.LogDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 *  @InstallIn():
 *  어느 container 에 담기는 component 인지를 명시하는 어노테이션이다.
 *
 *  만약 SingletonComponent를 넣는다면
 *  App 전체 container 에 담겨 App의 생명 주기(App이 끝날때 소멸)를 따를 것이고
 *
 *  만약 FragmentComponent를 넣는다면
 *  Inject를 한 Fragment 의 container 에 담겨 Fragment의 생명 주기를 따라 생성되고 소멸 될 것이다.
 *
 *  @Module:
 *  Hilt에 "결합 방법"을 추가하는 어노테이션이다.
 *  Interface 와 같이 constructor 나 Instance를 생성할 수 없는 특수한 경우에는
 *  Module 어노테이션을 사용하여 방법을 따로 지정할 수 있다.
 *  만약 "Interface에 대고 프로그래밍하기" 나 "class를 추상화" 하여서 Instance를 직접 제공할 수 없다면 사용할 수 있는 방식이다.
 *
 *  @Providers:
 *  Instance를 제공하는 방법을 직접적으로 알려줄 수 있는 어노테이션이다.
 *  providers 어노테이션 밑에 함수를 작성하여, 이 클래스에 Inject가 발생할 때 마다 실행할 함수를 Hilt에 알린다.
 *
 *  참고로 만약 Module의 안에 Providers로 지정된 함수만 존재한 다면 object로 설정할 수 있다.
 *  이렇게 하면 povider는 최적화 되고, 생성된 코드에 Inline 함수로 바뀌어 좋은 퍼포먼스를 낼 수 있게 된다.
 *
 *  @ApplicationContext:
 *  Hilt에서는 기본적인 결합을 제공한다.
 *  Inject와 같이 context들의 인스턴스를 간편하게 주입, 제공해 준다.
 *
 */
@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "logging.db"
        ).build()
    }

    @Provides
    fun provideLogDao(database: AppDatabase): LogDao {
        return database.logDao()
    }
}