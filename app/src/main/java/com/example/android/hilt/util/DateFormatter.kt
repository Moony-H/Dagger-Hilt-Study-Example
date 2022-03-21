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

package com.example.android.hilt.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

/**
 * String formatter for the log dates.
 */

/**DataFormatter는 특수한 방법(현재 시간을 반환하는 SimpleDataFormat)으로 매번 다른 시간을 반환하는 class 이다.
 *우리는 이 클래스의 Instance를 주입해야 하기 때문에 Hilt 에게 이 사실을 알린다.
 * 알리는 방법은 클래스의 생성자에 @Inject 어노테이션을 사용한다.
*/
class DateFormatter @Inject constructor() {

    @SuppressLint("SimpleDateFormat")
    private val formatter = SimpleDateFormat("d MMM yyyy HH:mm:ss")

    fun formatDate(timestamp: Long): String {
        return formatter.format(Date(timestamp))
    }
}
