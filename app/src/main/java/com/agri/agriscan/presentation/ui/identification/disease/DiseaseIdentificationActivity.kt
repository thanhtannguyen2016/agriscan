package com.agri.agriscan.presentation.ui.identification.disease

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.agri.agriscan.databinding.ActivityDiseaseIdentificationBinding
import com.agri.agriscan.domain.model.Disease
import com.agri.agriscan.domain.model.Plant
import com.agri.agriscan.domain.model.UiState
import com.agri.agriscan.presentation.ui.treatment.TreatmentActivity
import com.agri.agriscan.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DiseaseIdentificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDiseaseIdentificationBinding
    private val viewModel: DiseaseIdentificationViewModel by viewModels()
    private lateinit var diseaseAdapter: DiseaseResultAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiseaseIdentificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideStatusBar()

        setupToolbar()
        setupRecyclerView()
        setupObservers()
        setupClickListeners()

        // Get plant and image URI
        val plant = intent.getParcelableExtra<Plant>(Constants.EXTRA_PLANT_DATA)
        val imageUri = intent.getStringExtra(Constants.EXTRA_IMAGE_URI)

        if (plant != null && imageUri != null) {
            val uri = Uri.parse(imageUri)

            // Take persistable URI permission để load ảnh từ URI
            try {
                contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (e: SecurityException) {
                // URI không phải từ picker (file:// URI) hoặc đã có permission
                Log.w(TAG, "Could not take persistable permission: ${e.message}")
            }

            viewModel.setPlant(plant)
            binding.ivCapturedImage.load(uri)
            binding.tvPlantInfo.text = plant.commonNames.firstOrNull() ?: plant.scientificName
            viewModel.identifyDisease(imageUri)
        } else {
            finish()
        }
    }

    private fun hideStatusBar(){
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller = window.insetsController
            if (controller != null) {
                controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun setupRecyclerView() {
        diseaseAdapter = DiseaseResultAdapter { disease ->
            showDiseaseConfirmationDialog(disease)
        }

        binding.rvDiseaseResults.apply {
            layoutManager = LinearLayoutManager(this@DiseaseIdentificationActivity)
            adapter = diseaseAdapter
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    is UiState.Idle -> showIdleState()
                    is UiState.Loading -> showLoadingState()
                    is UiState.Success -> showSuccessState(state.data)
                    is UiState.Error -> showErrorState(state.message)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.confirmedDisease.collect { disease ->
                binding.btnViewTreatment.visibility = if (disease != null) View.VISIBLE else View.GONE
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnViewTreatment.setOnClickListener {
            val plant = viewModel.plant.value
            val disease = viewModel.confirmedDisease.value

            if (plant != null && disease != null) {
                navigateToTreatment(plant, disease)
            }
        }

        binding.btnRetry.setOnClickListener {
            viewModel.imageUri.value?.let { uri ->
                viewModel.identifyDisease(uri)
            }
        }
    }

    private fun showIdleState() {
        binding.statusSection.visibility = View.GONE
        binding.errorView.visibility = View.GONE
        binding.rvDiseaseResults.visibility = View.GONE
        binding.tvResultsTitle.visibility = View.GONE
    }

    private fun showLoadingState() {
        binding.statusSection.visibility = View.VISIBLE
        binding.progressBar.visibility = View.VISIBLE
        binding.tvStatus.text = "Đang nhận dạng bệnh..."
        binding.errorView.visibility = View.GONE
        binding.rvDiseaseResults.visibility = View.GONE
        binding.tvResultsTitle.visibility = View.GONE
    }

    private fun showSuccessState(identification: com.agri.agriscan.domain.model.DiseaseIdentification) {
        binding.statusSection.visibility = View.GONE
        binding.errorView.visibility = View.GONE
        binding.tvResultsTitle.visibility = View.VISIBLE
        binding.rvDiseaseResults.visibility = View.VISIBLE

        diseaseAdapter.submitList(identification.results)
    }

    private fun showErrorState(message: String) {
        binding.statusSection.visibility = View.GONE
        binding.rvDiseaseResults.visibility = View.GONE
        binding.tvResultsTitle.visibility = View.GONE
        binding.errorView.visibility = View.VISIBLE
        binding.tvError.text = message
    }

    private fun showDiseaseConfirmationDialog(disease: Disease) {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Xác nhận bệnh")
            .setMessage("Bệnh: ${disease.name}\n\n${disease.description ?: ""}\n\nXem phương pháp điều trị?")
            .setPositiveButton("Xem điều trị") { _, _ ->
                viewModel.confirmDisease(disease)
                viewModel.plant.value?.let { plant ->
                    navigateToTreatment(plant, disease)
                }
            }
            .setNegativeButton("Chọn lại", null)
            .show()
    }

    private fun navigateToTreatment(plant: Plant, disease: Disease) {
        val intent = Intent(this, TreatmentActivity::class.java).apply {
            putExtra(Constants.EXTRA_PLANT_DATA, plant)
            putExtra(Constants.EXTRA_DISEASE_DATA, disease)
            // Grant URI permission to next activity (nếu cần pass URI tiếp)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(intent)
    }

    companion object {
        private const val TAG = "DiseaseIdentificationActivity"
    }
}