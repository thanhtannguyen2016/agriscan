package com.agri.agriscan.presentation.ui.identification.plant

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.agri.agriscan.databinding.ActivityPlantIdentificationBinding
import com.agri.agriscan.domain.model.Plant
import com.agri.agriscan.domain.model.UiState
import com.agri.agriscan.presentation.ui.identification.disease.DiseaseIdentificationActivity
import com.agri.agriscan.util.Constants
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PlantIdentificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlantIdentificationBinding
    private val viewModel: PlantIdentificationViewModel by viewModels()
    private lateinit var plantAdapter: PlantResultAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlantIdentificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        setupObservers()
        setupClickListeners()

        // Get image URI and start identification
        val imageUri = intent.getStringExtra(Constants.EXTRA_IMAGE_URI)
        if (imageUri != null) {
            binding.ivCapturedImage.load(Uri.parse(imageUri))
            viewModel.identifyPlant(imageUri)
        } else {
            finish()
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun setupRecyclerView() {
        plantAdapter = PlantResultAdapter { plant ->
            viewModel.selectPlant(plant)
            showPlantSelectionDialog(plant)
        }

        binding.rvPlantResults.apply {
            layoutManager = LinearLayoutManager(this@PlantIdentificationActivity)
            adapter = plantAdapter
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
            viewModel.selectedPlant.collect { plant ->
                binding.btnProceed.visibility = if (plant != null) View.VISIBLE else View.GONE
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnProceed.setOnClickListener {
            val plant = viewModel.selectedPlant.value
            val imageUri = viewModel.imageUri.value

            if (plant != null && imageUri != null) {
                navigateToDiseaseIdentification(plant, imageUri)
            }
        }

        binding.btnRetry.setOnClickListener {
            viewModel.imageUri.value?.let { uri ->
                viewModel.identifyPlant(uri)
            }
        }
    }

    private fun showIdleState() {
        binding.statusSection.visibility = View.GONE
        binding.errorView.visibility = View.GONE
        binding.rvPlantResults.visibility = View.GONE
        binding.tvResultsTitle.visibility = View.GONE
        binding.cardLowConfidence.visibility = View.GONE
    }

    private fun showLoadingState() {
        binding.statusSection.visibility = View.VISIBLE
        binding.progressBar.visibility = View.VISIBLE
        binding.tvStatus.text = "Đang nhận dạng cây trồng..."
        binding.errorView.visibility = View.GONE
        binding.rvPlantResults.visibility = View.GONE
        binding.tvResultsTitle.visibility = View.GONE
    }

    private fun showSuccessState(identification: com.agri.agriscan.domain.model.PlantIdentification) {
        binding.statusSection.visibility = View.GONE
        binding.errorView.visibility = View.GONE
        binding.tvResultsTitle.visibility = View.VISIBLE
        binding.rvPlantResults.visibility = View.VISIBLE

        plantAdapter.submitList(identification.results)

        // Show warning if confidence is low
        binding.cardLowConfidence.visibility = if (!identification.hasSufficientConfidence) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun showErrorState(message: String) {
        binding.statusSection.visibility = View.GONE
        binding.rvPlantResults.visibility = View.GONE
        binding.tvResultsTitle.visibility = View.GONE
        binding.errorView.visibility = View.VISIBLE
        binding.tvError.text = message
    }

    private fun showPlantSelectionDialog(plant: Plant) {
        val commonName = plant.commonNames.firstOrNull() ?: plant.scientificName

        MaterialAlertDialogBuilder(this)
            .setTitle("Xác nhận cây trồng")
            .setMessage("Bạn đã chọn: $commonName\n\nTiếp tục nhận dạng bệnh?")
            .setPositiveButton("Tiếp tục") { _, _ ->
                viewModel.imageUri.value?.let { uri ->
                    navigateToDiseaseIdentification(plant, uri)
                }
            }
            .setNegativeButton("Chọn lại", null)
            .show()
    }

    private fun navigateToDiseaseIdentification(plant: Plant, imageUri: String) {
        val intent = Intent(this, DiseaseIdentificationActivity::class.java).apply {
            putExtra(Constants.EXTRA_PLANT_DATA, plant)
            putExtra(Constants.EXTRA_IMAGE_URI, imageUri)
        }
        startActivity(intent)
        finish()
    }
}