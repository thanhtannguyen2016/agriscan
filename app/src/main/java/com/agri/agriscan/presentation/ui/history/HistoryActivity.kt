package com.agri.agriscan.presentation.ui.history

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.agri.agriscan.R
import com.agri.agriscan.databinding.ActivityHistoryBinding
import com.agri.agriscan.domain.model.History
import com.agri.agriscan.presentation.ui.treatment.TreatmentActivity
import com.agri.agriscan.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private val viewModel: HistoryViewModel by viewModels()
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideStatusBar()

        setupToolbar()
        setupRecyclerView()
        setupObservers()
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
        historyAdapter = HistoryAdapter { historyItem ->
            navigateToTreatment(historyItem)
        }
        binding.rvHistory.apply {
            adapter = historyAdapter
            layoutManager = LinearLayoutManager(this@HistoryActivity)
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.history.collect { historyList ->
                if (historyList.isEmpty()) {
                    binding.rvHistory.visibility = View.GONE
                    binding.tvNoHistory.visibility = View.VISIBLE
                } else {
                    binding.rvHistory.visibility = View.VISIBLE
                    binding.tvNoHistory.visibility = View.GONE
                    historyAdapter.submitList(historyList)
                }
            }
        }
    }

    private fun navigateToTreatment(history: History) {
        val intent = Intent(this, TreatmentActivity::class.java).apply {
            putExtra(Constants.EXTRA_PLANT_DATA, history.plant)
            putExtra(Constants.EXTRA_DISEASE_DATA, history.disease)
        }
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_history, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_clear_history -> {
                viewModel.clearHistory()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}