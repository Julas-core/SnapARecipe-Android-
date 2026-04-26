class MainViewModel : ViewModel() {

    private val repository = RecipeRepository()

    var uiState by mutableStateOf("Idle")
        private set

    fun analyze(base64: String) {
        uiState = "Loading..."

        viewModelScope.launch {
            try {
                val result = repository.analyzeImage(base64)
                uiState = result
            } catch (e: Exception) {
                uiState = "Error: ${e.message}"
            }
        }
    }
}