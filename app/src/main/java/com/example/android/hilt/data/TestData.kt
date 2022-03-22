package com.example.android.hilt.data

import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject
import javax.inject.Singleton

@ActivityRetainedScoped
class TestData @Inject constructor(){
    var data=""
}