package com.agri.agriscan.presentation.ui.treatment

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.agri.agriscan.databinding.ActivityTreatmentBinding
import com.agri.agriscan.domain.model.Disease
import com.agri.agriscan.domain.model.Plant
import com.agri.agriscan.domain.model.UiState
import com.agri.agriscan.util.Constants
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TreatmentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTreatmentBinding
    private val viewModel: TreatmentViewModel by viewModels()
    private lateinit var treatmentPagerAdapter: TreatmentPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTreatmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupViewPager()
        setupObservers()

        // Get plant and disease data
        val plant = intent.getParcelableExtra<Plant>(Constants.EXTRA_PLANT_DATA)
        val disease = intent.getParcelableExtra<Disease>(Constants.EXTRA_DISEASE_DATA)

        if (plant != null && disease != null) {
            binding.tvPlantName.text = "${plant.commonNames.firstOrNull() ?: plant.scientificName}"
            binding.tvDiseaseName.text = "Bệnh: ${disease.name}"

            viewModel.getTreatment(plant, disease)
        } else {
            finish()
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun setupViewPager() {
        treatmentPagerAdapter = TreatmentPagerAdapter(this)
        binding.viewPager.adapter = treatmentPagerAdapter

        // Link TabLayout with ViewPager2
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Hóa học"
                1 -> "Sinh học"
                2 -> "Phòng ngừa"
                else -> ""
            }
        }.attach()
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    is UiState.Idle -> {
                        binding.loadingOverlay.visibility = View.GONE
                    }
                    is UiState.Loading -> {
                        binding.loadingOverlay.visibility = View.VISIBLE
                    }
                    is UiState.Success -> {
                        binding.loadingOverlay.visibility = View.GONE
                        treatmentPagerAdapter.setTreatment(state.data)
                    }
                    is UiState.Error -> {
                        binding.loadingOverlay.visibility = View.GONE
                        showError(state.message)
                    }
                }
            }
        }
    }

    private fun showError(message: String) {
        com.google.android.material.dialog.MaterialAlertDialogBuilder(this)
            .setTitle("Lỗi")
            .setMessage(message)
            .setPositiveButton("Thử lại") { _, _ ->
                viewModel.retry()
            }
            .setNegativeButton("Đóng") { _, _ ->
                finish()
            }
            .show()
    }
}