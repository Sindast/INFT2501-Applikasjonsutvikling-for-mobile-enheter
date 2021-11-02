package com.example.oving7.managers

import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter

class FileManager(private val activity: AppCompatActivity) {
    private val filename: String = "filename.txt"

    private var dir: File = activity.filesDir
    private var file: File = File(dir, filename)


       fun getFile(): File
       {
           return this.file
       }

}