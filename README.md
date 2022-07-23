<br/>

# DI 라이브러리 Hilt 사용법 및 학습

</br>

이 Repository는 Hilt를 사용하는 방법을 총 정리한 Reopsitory 입니다.

학습 내용과 어노테이션 설명은 모두 코드에 주석으로 달아 놓았습니다. 따라서 코드를 보고 이해해도 무방합니다.

이 Repository는 구글 Android의 [공식 페이지](https://developer.android.com/codelabs/) 에서 학습한 내용을 담고 있습니다.

학습 구성과 흐름은 https://developer.android.com/codelabs/android-hilt#0 에서 확인하실 수 있습니다.

이 repository는 구글의 Service Locator 패턴 예시의 프로젝트에 Hilt를 적용시켜 Refactoring한 repository입니다.

따라서 원본 파일은 이곳에서 볼 수 있습니다. https://github.com/googlecodelabs/android-hilt

<br/>

## 용어 설명  


<br/>
<br/>

### Container

<br/>



container는 Component가 연결해 주는 Instance의 주입 시작점 입니다.

이 상자는 생명 주기에 맟춘 상자 입니다.

각 의존성들을 가지고 생명주기를 고려하여 의존성 주입을 하는 기능을 가집니다.

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

Component는 Container에 들어갈 Module, Instance를 연결해 주는, 주된 역할을 수행합니다.

콘크리트 클래스가 아닌 Interface나

Instance를 프로젝트 내에 가지고 있지 않고 build() 패턴을 사용하는

Room, Retrofit 같은 경우는 @InstallIn() 어노테이션을 사용하여, 표준 컴포넌트 중 어느 Component 인지 명시합니다.

**(! 주의! 어느 컨테이너에 들어갈지를 결정하지, 컨테이너가 지정된 안드로이드 class(Activity, Fragment, View)의 생명 주기를 따르는 것은 아닙니다. 즉, 그저 AndroidEntryPoint에 인스턴스를 생성해서 주입만 합니다.)**

**(생명 주기를 따라 Instance가 유지되는 어노테이션은 밑에서 알아볼 Scope입니다.)**

예시:

```kotlin
//표준 컴포넌트 중, ActivityComponent에 Module을 설치할 것이라고 명시하는 방법.
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

https://github.com/Moony-H/Dagger-Hilt-Study-Example/blob/master/app/src/main/java/com/example/android/hilt/ui/LogsFragment.kt

```kotlin
@AndroidEntryPoint
class LogsFragment : Fragment() {
    
}
```

<br/>
<br/>

### @EntryPoint

<br/>

생명주기가 없는 일반 Container를 생성합니다.

생명 주기가 없는 일반 class에 Inject할 때 사용합니다.

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
     * Instance를 찾아 주입한다.
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

주의할 점은, @Indect를 사용하는 Field는 **무조건 public과 lateinit var**이어야 하며(필드 주입이기 때문 입니다.),

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

App 전체 container 에 담길 것이고

</br>

만약 FragmentComponent를 넣는다면

Inject를 한 Fragment 의 container 에 담길 것 입니다.

```kotlin
@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

}
```

<br/>
<br/>

### @Provides

<br/>

객체를 생성하지 못하는, @InstallIn 과 @Module을 사용한 클래스 내부에서 사용되는 어노테이션 입니다.

@InstallIn, @Module과 쌍으로 같이 다니는 어노테이션입니다.

@Provides는 함수에게 사용합니다.

@Provides는 @Module을 사용한 클래스의 Instance가 생성될 때 호출되는 함수가 됩니다.

생성자가 된다라고 봐도 무방(?)합니다.

따라서 이 함수의 반환 타입은 반드시 Injection 되는 Field와 같아야 합니다.

build 패턴을 사용하는 Room, Retrofit에 많이 사용합니다.

https://github.com/Moony-H/Dagger-Hilt-Study-Example/blob/master/app/src/main/java/com/example/android/hilt/di/DataBaseModule.kt


```kotlin
/**
 *  @InstallIn():
 *  어느 container 에 담기는 component 인지를 명시하는 어노테이션이다.
 *
 *  만약 SingletonComponent를 넣는다면
 *  App 전체 container 에 담길 것 이고
 *
 *  만약 FragmentComponent를 넣는다면
 *  Inject를 한 Fragment 의 container 에 담길 것 이다.
 *
 *  @Module:
 *  Hilt에 "결합 방법"을 추가하는 어노테이션이다.
 *  Interface 와 같이 constructor 나 Instance를 생성할 수 없는 특수한 경우에는
 *  Module 어노테이션을 사용하여 방법을 따로 지정할 수 있다.
 *  만약 "Interface에 대고 프로그래밍하기" 나 "class를 추상화" 하여서 Instance를 직접 제공할 수 없다면 사용할 수 있는 방식이다.
 *
 *  @Provides:
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

### Scoped

Hilt는 생명주기를 맞춰 인스턴스를 관리할 수 있는 여러 어노테이션을 제공합니다.

이 어노테이션들은 Instance가 어떤 생명 주기에 맞춰 생성되고 파괴되는 지를 명시하는 어노테이션 입니다.

또한 상위 어노테이션으로 명시된 class는 그 하위 AndroidEntryPoint 에도 Inject 할 수 있습니다.

![화면 캡처 2022-03-23 003126](https://user-images.githubusercontent.com/53536205/159519198-bc8ea419-7000-482d-bc9a-5ab84e60aef8.png)

<br/>

예시)

@ActivityScoped는 AndroidEntryPoint가 명시된 Activity class의 생명 주기에 맞춰 생성되고 파괴 됩니다.

또한 위의 그림에서와 같이 ActivityScoped의 하위 구성 요소인 Fragment와 View 에도 @Inject를 수행할 수 있으며,

이 때, Inject된 인스턴스는 ActivityScoped를 따르니, Fragment와 View에 들어간 객체는 Activity에 들어간 객체와 동일한 Instance가 됩니다.(Activity가 끝나지 않는 한 Instance는 파괴되지 않는다.)

<br/>

만약 @ActivityScoped로 설정된 객체가 두개의 서로다른 Activity로 @Inject 될 경우, 두 개의 Instance는 다른 Instance가 됩니다.

두 Activity에 같은 인스턴스를 넣고 싶다면 그 상위 구성 요소(Component)인 @ActivityRetainedScoped나 @Singleton으로 명시해야 합니다.

만약 맞는 Scope도, 상위 Scope도 아닌 하위 Scope로 상위 Component에 @Inject를 수행할 경우 아래와 같은 오류가 나타납니다.

![화면 캡처 2022-03-23 004312](https://user-images.githubusercontent.com/53536205/159522040-e390757a-b6e0-4f81-b6f9-bb8b4ec6eb4c.png)

만약 위와 같은 오류가 발생한 다면, 계층 구조를 의심해 봅시다.

<br/>

<br/>

### @Qualifier

@Qualifier는 한정자 주석 입니다. 어노테이션을 만듭니다.

이 어노테이션을 설명하기 위해 하나의 상황을 가정합니다.

### _**"추상화를 통하여 하나의 타입으로 서로 다른 Instance를 주입하고자 한다."**_

만약 data를 메모리, 또는 Database에 저장한다고 합시다.

그렇다면 data를 저장하는 공통된 함수를 interface로 묶을 수 있습니다.

```kotlin
interface LoggerDataSource {
    fun addLog(msg: String)
    fun getAllLogs(callback: (List<Log>) -> Unit)
    fun removeLogs()
}
```

<br/>

<br/>

그리고 메모리에 저장하는 클래스, Database에 저장하는 클래스 모두 이 interface를 implement 합니다.

```kotlin
class LoggerLocalDataSource @Inject constructor(
    private val logDao: LogDao
    override fun addLog(msg: String) {
    }
    
    /**
    중략
    */
) : LoggerDataSource //interface를 상속한다.
```

```kotlin
class LoggerInMemoryDataSource @Inject constructor(): LoggerDataSource {

    private val logs = LinkedList<Log>()

    override fun addLog(msg: String) {
        
    }

    /**
    중략
    */
}:LoggerDataSource //interface를 상속한다.
```

<br/>

<br/>

그리고 위의 Interface를 주입할 Fragment에 Interface 타입으로 Inject를 수행합니다.

```kotlin
@AndroidEntryPoint
class ButtonsFragment : Fragment() {

    @Inject
    lateinit var logger: LoggerDataSource
}
```

<br/>

<br/>

그 다음 두 클래스 모두 Interface 타입으로 사용하기 위해, 위에서 배운 @Binds로 명시해 줍니다.

```kotlin

@InstallIn(ApplicationComponent::class)
@Module
abstract class LoggingDatabaseModule {


    @Singleton
    @Binds
    abstract fun bindDatabaseLogger(impl: LoggerLocalDataSource): LoggerDataSource
}

@InstallIn(ActivityComponent::class)
@Module
abstract class LoggingInMemoryModule {

    @ActivityScoped
    @Binds
    abstract fun bindInMemoryLogger(impl: LoggerInMemoryDataSource): LoggerDataSource
}
```

<br/>

<br/>

문제는 여기서 발생합니다. Fragment에 Inject한 타입은 Interface 타입입니다. 그리고 그 Interface는 Binds로 정의된 @Module이 두 개나 존재합니다.

따라서 Hilt는 두 가지의 Binds 된 abstract 함수 중 어느 것을 실행하여 @Inject를 수행할 것인지 알지 못합니다.

우리는 이 때에 @Qualifier 를 이용하여 새로운 어노테이션을 만든 후, 이 것을 알려 줄 수 있습니다.

```kotlin
@Qualifier
annotation class InMemoryLogger //새로운 어노테이션 생성

@Qualifier
annotation class DatabaseLogger //새로운 어노테이션 생성

@InstallIn(ApplicationComponent::class)
@Module
abstract class LoggingDatabaseModule {

    @DatabaseLogger //만약 @Inject와 이 어노테이션이 함께 사용된다면, 이 Binds를 사용해야 한다.
    			   //라고 Hilt에게 알린다.
    @Singleton
    @Binds
    abstract fun bindDatabaseLogger(impl: LoggerLocalDataSource): LoggerDataSource
}

@InstallIn(ActivityComponent::class)
@Module
abstract class LoggingInMemoryModule {

    @InMemoryLogger //만약 @Inject와 이 어노테이션이 함께 사용된다면, 이 Binds를 사용해야 한다.
    			   //라고 Hilt에게 알린다.
    @ActivityScoped
    @Binds
    abstract fun bindInMemoryLogger(impl: LoggerInMemoryDataSource): LoggerDataSource
}
```

<br/>

이제 Fragment에서 @Inject를 수행할 때에 @Inject와 같이 어느 Instance를 주입할 것인지 명시하면 됩니다.

```kotlin
@AndroidEntryPoint
class ButtonsFragment : Fragment() {

    @DatabaseLogger //이 @Inject의 타입은 Interface이고, 이 Interface를 사용한 Binds는 두 가지이다.
    			   // 따라서 DatabaseLogger 이라고 명시된 Binds의 Module을 사용하라.
    @Inject
    lateinit var logger: LoggerDataSource
    @Inject
    lateinit var navigator: AppNavigator
```

<br/>

<br/>

모든 설명을 마쳤습니다. 감사합니다.



앱 실행 화면:  

![hilt_example](https://user-images.githubusercontent.com/53536205/159365038-21089c6e-df93-40c8-9f9d-b28168ffa575.gif)
