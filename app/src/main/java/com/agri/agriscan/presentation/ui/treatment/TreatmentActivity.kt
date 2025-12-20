package com.agri.agriscan.presentation.ui.treatment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.agri.agriscan.R
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
        hideStatusBar()

        setupToolbar()
        setupViewPager()
        setupObservers()

        // Get plant and disease data
        val plant = intent.getParcelableExtraCompat<Plant>(Constants.EXTRA_PLANT_DATA)
        val disease = intent.getParcelableExtraCompat<Disease>(Constants.EXTRA_DISEASE_DATA)

        if (plant != null && disease != null) {
            binding.tvPlantName.text = "${plant.commonNames.firstOrNull() ?: plant.scientificName}"
            binding.tvDiseaseName.text = getString(R.string.disease_name_format, disease.name)

            viewModel.getTreatment(plant, disease)
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
        androidx.appcompat.app.AlertDialog.Builder(this)
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

inline fun <reified T : Parcelable> Intent.getParcelableExtraCompat(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}
