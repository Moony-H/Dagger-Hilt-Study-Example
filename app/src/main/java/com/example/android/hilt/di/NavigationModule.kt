package com.example.android.hilt.di

import com.example.android.hilt.navigator.AppNavigator
import com.example.android.hilt.navigator.AppNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

/**
 * @Binds:
 * binds 어노테이션은 타입이 Interface라 객체를 Inject할 수 없을 때 사용한다.
 * binds 어노테이션 밑의 함수는 abstract 함수이어야 하며
 * 반환 값은 Interface 타입이고(당연하다. Interface 타입의 객체를 생성하지 못하니까 이걸 하고있다.)
 * 매개 변수는 Interface를 구현한, 실제 Instance를 줄 콘크리트 클래스 이다.

 */


@InstallIn(ActivityComponent::class)
@Module
abstract class NavigationModule {

    @Binds
    abstract fun bindNavigator(impl: AppNavigatorImpl): AppNavigator
}