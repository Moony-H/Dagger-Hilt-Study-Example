/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.hilt.data

import android.os.Handler
import android.os.Looper
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Data manager class that handles data manipulation between the database and the UI.
 */


/**
 *
 * 이 객체의 인스턴스는 앱의 시작부터 종료까지 같은 인스턴스를 유지해야 한다.
 * 따라서 @Singleton 어노테이션을 사용하면, 이 클래스의 인스턴스가 필요한 곳 마다 같은 인스턴스를 주입해 준다.
 *
 * 위와 마찬가지로, 만약 어느 액티비티가 실행 중이고, 그 액티비티가 실행하는 동안 같은 Instance를 유지해야 한다면
 * @ActivityScope 어노테이션을 사용하면 된다.
 *
 * 이렇게 동일한 Instance 가 필요한 범위를 범위 어노테이션으로 설정할 수 있다.
 */


@Singleton
class LoggerLocalDataSource @Inject constructor(private val logDao: LogDao):LoggerDataSource{

    private val executorService: ExecutorService = Executors.newFixedThreadPool(4)
    private val mainThreadHandler by lazy {
        Handler(Looper.getMainLooper())
    }

    override fun addLog(msg: String) {
        executorService.execute {
            logDao.insertAll(
                Log(
                    msg,
                    System.currentTimeMillis()
                )
            )
        }
    }

    override fun getAllLogs(callback: (List<Log>) -> Unit) {
        executorService.execute {
            val logs = logDao.getAll()
            mainThreadHandler.post { callback(logs) }
        }
    }

    override fun removeLogs() {
        executorService.execute {
            logDao.nukeTable()
        }
    }
}
