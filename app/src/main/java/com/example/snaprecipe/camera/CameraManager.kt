class CameraManager(private val context: Context) {

    fun createImageFile(): File {
        return File(context.cacheDir, "captured.jpg")
    }
}

