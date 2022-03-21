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

package com.example.android.hilt.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.android.hilt.LogApplication
import com.example.android.hilt.R
import com.example.android.hilt.data.Log
import com.example.android.hilt.data.LoggerLocalDataSource
import com.example.android.hilt.util.DateFormatter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Fragment that displays the database logs.
 */

/**
 *
 * @AndroidEntryPoint를 사용하면 사용한 class의 생명 주기를 따르는 Container(종속 항목들이 들어가 있는 공간)을 생성해 준다.
 */

@AndroidEntryPoint
class LogsFragment : Fragment() {

    /**
     * 생명 주기에 맞게 생성되고 소멸되는 Instance를 주입해 준다.
     */

    //Instance 주입은 onAttach 에서 하게 된다.
    @Inject lateinit var logger: LoggerLocalDataSource
    @Inject lateinit var dateFormatter: DateFormatter

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_logs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view).apply {
            setHasFixedSize(true)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //populateFields(context)
        /**더이상 위와 같은 Service Locator에서 Dependency를 주입하는 코드는 필요치 않다.
        *
        실제로 Hilt는 onAttach(부모에 View가 붙었을 때,)에서 의존성을 주입한다.(그래서 lateinit var 이다.)
        */
    }

    /**
     * 밑과 같은 onAttach 에서 instance 를 service locator에서 주압하는 코드는 필요하지 않다.
     */


    //private fun populateFields(context: Context) {
    //   logger = (context.applicationContext as LogApplication).serviceLocator.loggerLocalDataSource
    //    dateFormatter =
    //        (context.applicationContext as LogApplication).serviceLocator.provideDateFormatter()
    //}


    override fun onResume() {
        super.onResume()

        logger.getAllLogs { logs ->
            recyclerView.adapter =
                LogsViewAdapter(
                    logs,
                    dateFormatter
                )
        }
    }
}

/**
 * RecyclerView adapter for the logs list.
 */
private class LogsViewAdapter(
    private val logsDataSet: List<Log>,
    private val daterFormatter: DateFormatter
) : RecyclerView.Adapter<LogsViewAdapter.LogsViewHolder>() {

    class LogsViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogsViewHolder {
        return LogsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.text_row_item, parent, false) as TextView
        )
    }

    override fun getItemCount(): Int {
        return logsDataSet.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: LogsViewHolder, position: Int) {
        val log = logsDataSet[position]
        holder.textView.text = "${log.msg}\n\t${daterFormatter.formatDate(log.timestamp)}"
    }
}
