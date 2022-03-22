package com.example.android.hilt.data

import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject
import javax.inject.Singleton

@FragmentScoped
class TestData @Inject constructor(){
    var data=""
}