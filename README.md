
<br/>

# DI 라이브러리 Hilt 사용법 및 학습

</br>

이 Repository는 Hilt를 사용하는 방법을 총 정리한 Reopsitory 입니다.

학습 내용과 어노테이션 설명은 모두 코드에 주석으로 달아 놓았습니다. 따라서 코드를 보고 이해해도 무방합니다.

이 Repository는 구글 Android의 공식 페이지 https://developer.android.com/codelabs/ 에서 학습한 내용을 담고 있습니다.

학습 구성과 흐름은 https://developer.android.com/codelabs/android-hilt#0 에서 확인하실 수 있습니다.

이 repository는 https://github.com/googlecodelabs/android-hilt 의 프로젝트에 Hilt를 적용시켜 Refactoring한 repository입니다.

<br/>

## 용어 설명  


<br/>
<br/>

### Container

<br/>

container는 Component가 들어가는 "상자"라고 이해하면 쉽습니다.

이 상자는 생명 주기에 맟춘 상자 입니다.

이 상자에 들어가는 Component들은 상자(Container)로 정의한 Android의 생명주기 클래스들(Activity, Fragment, Application)에 맞춰 인스턴스가 생성(onAttach 때)되고 파괴(onDestroy 때) 됩니다.

Container의 생명 주기를 클래스에 맞춰 Container를 생성하는 방법은

**@AndroidEntryPoint 어노테이션을 생명주기를 가진 클래스 위에 사용합니다.**

<br/>


예시:

```kotlin
//Fragment의 생명주기에 맞는 Container(상자) 생성.
@AndroidEntryPoint
class LogsFragment : Fragment() {

}
```

<br/>
<br/>

### Component

<br/>

Component는 Container에 들어가는 요소입니다. Container에 들어가는, Dependency Injection을 할 Instance라고 봐도 무방합니다.

콘크리트 클래스는 밑에서 설명할 @Indect 어노테이션을 사용하면 자동으로 Container의 생명 주기에 맞춰 들어가고,

콘크리트 클래스가 아닌 Interface나 Instance를 프로젝트 내에 가지고 있지 않고 build() 패턴을 사용하는

Room, Retrofit 같은 경우는 @InstallIn() 어노테이션을 사용하여, 어느 생명주기에 맞춘 Container에 들어갈 component인지 명시합니다.

예시:

```kotlin
//Activity Container에 들어갈 Activity Component 라고 명시하는 방법.
@InstallIn(ActivityComponent::class)
@Module

```

<br/>
<br/>

## 어노테이션 설명

<br/>
<br/>


### @AndroidEntryPoint

<br/>

Container(상자)를 생성합니다.

Container는 이 어노테이션이 사용한 Android class(Activity, Fragment 등)의 생명주기를 따릅니다.

```kotlin
@AndroidEntryPoint
class LogsFragment : Fragment() {
    
}
```

<br/>
<br/>

### @Inject

<br/>

객체를 주입합니다. 

Fragment 나 Activity 등, 생명주기를 가지고 있는 클래스의 Feild에 Instance를 주입합니다.

주입하는 객체가 콘크리트 클래스인 경우, Fragment나 Activity의 필드에 @Inject 어노테이션을 바로 사용하고

```kotlin
@AndroidEntryPoint
class LogsFragment : Fragment() {

    /**
     * 생명 주기에 맞게 생성되고 소멸되는 Instance를 주입해 준다.
     */

    //Instance 주입은 onAttach 에서 하게 된다.
    @Inject lateinit var logger: LoggerLocalDataSource
    @Inject lateinit var dateFormatter: DateFormatter
}
```

<br/>
<br/>

주입 당하는 객체의 Constuctor, 생성자에 @Inject 어노테이션을 추가합니다.

```kotlin
/**DataFormatter는 현재 시간을 반환하는 class이다.
 *
 * 우리는 이 클래스의 Instance를 주입해야 하기 때문에 Hilt 에게 이 사실을 알린다.
 * 알리는 방법은 클래스의 생성자에 @Inject 어노테이션을 사용한다.
*/
class DateFormatter @Inject constructor() {

    @SuppressLint("SimpleDateFormat")
    private val formatter = SimpleDateFormat("d MMM yyyy HH:mm:ss")

    fun formatDate(timestamp: Long): String {
        return formatter.format(Date(timestamp))
    }
}
```

<br/>

이렇게 둘 모두 @Inject 어노테이션을 사용해야 Dependency Injection이 실행 됩니다.

주의할 점은, @Indect를 사용하는 Field는 **무조건 public**이어야 하며,

실제 인스턴스가 생성되어 Injection 하는 시점은 onAttach 입니다.

<br/>
<br/>

### @Module

<br/>

콘크리트 클래스가 아닌 경우, Injection을 하기 위해서 Hilt에게 이 클래스가 모듈임을 알리는 어노테이션 입니다.

Retrofit이나 Room과 같이 프로젝트에 실제 객체를 가지지 못하고 build 패턴을 사용하거나,

"Interface 에 대고 프로그래밍 하기" 같은 규칙이나 추상화를 하여 Interface 타입인 객체를 의존성 주입할 때 사용됩니다.

컴포넌트에 의존성을 알리는 어노테이션 입니다.

<br/>
<br/>

### @InstallIn()

<br/>

어느 container에 담기는 Component인지를 명시하는 어노테이션 입니다.

@Module 어노테이션과 쌍으로 함께 다닙니다.

괄호 안에 Component를 명시합니다.

</br>

만약 SingletonComponent를 넣는다면

App 전체 container 에 담겨 App의 생명 주기(App이 끝날때 소멸)를 따를 것이고

</br>

만약 FragmentComponent를 넣는다면

Inject를 한 Fragment 의 container 에 담겨 Fragment의 생명 주기를 따라 생성되고 소멸 될 것 입니다.

```kotlin
@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

}
```

<br/>
<br/>

### @Providers

<br/>

객체를 생성하지 못하는, @InstallIn 과 @Module을 사용한 클래스 내부에서 사용되는 어노테이션 입니다.

@InstallIn, @Module과 쌍으로 같이 다니는 어노테이션입니다.

@Providers는 함수에게 사용합니다.

@Providers는 @Module을 사용한 클래스의 Instance가 생성될 때 호출되는 함수가 됩니다.

생성자가 된다라고 봐도 무방(?)합니다.

따라서 이 함수의 반환 타입은 반드시 Injection 되는 Field와 같아야 합니다.

build 패턴을 사용하는 Room, Retrofit에 많이 사용합니다.

```kotlin
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
```

<br/>
<br/>


### @Binds

<br/>

객체를 생성하지 못하는, @InstallIn 과 @Module을 사용한 클래스 내부에서 사용되는 어노테이션 입니다.

@InstallIn, @Module과 쌍으로 같이 다니는 어노테이션입니다.

@Binds는 abstract 함수에게 사용합니다.

보통 Interface 타입을 주입해야 할 때, 이 @Binds로 된 함수를 참조하여 Hilt에게 알립니다.

따라서 @Binds의 반환 값은 반드시 Interface 타입이어야 하며, 

매개 변수는 이 Interface를 구현 한 콘크리트 클래스 이어야 합니다.

```kotlin
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
```

<br/>
<br/>
<br/>

앱 실행 화면:  

![hilt_example](https://user-images.githubusercontent.com/53536205/159365038-21089c6e-df93-40c8-9f9d-b28168ffa575.gif)
