package com.example.rasago.data.repository

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    /**
     * Copies an image from a temporary content URI to the app's internal storage.
     *
     * @param uri The temporary URI of the image selected from the gallery.
     * @return The permanent, absolute file path of the saved image copy, or null if an error occurs.
     */
    fun copyImageToInternalStorage(uri: Uri): String? {
        return try {
            // Get an InputStream to read the data from the selected image.
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null

            // Create a destination file in the app's private 'files' directory.
            // Using a UUID ensures the filename is always unique.
            val file = File(context.filesDir, "${UUID.randomUUID()}.jpg")

            // Create an OutputStream to write the data to the new file.
            val outputStream = FileOutputStream(file)

            // Copy the data from the input stream to the output stream.
            inputStream.copyTo(outputStream)

            // Close the streams to free up resources.
            inputStream.close()
            outputStream.close()

            // Return the absolute path of the newly created permanent file.
            file.absolutePath
        } catch (e: IOException) {
            e.printStackTrace()
            null // Return null if there was an error during the copy process.
        }
    }
}
