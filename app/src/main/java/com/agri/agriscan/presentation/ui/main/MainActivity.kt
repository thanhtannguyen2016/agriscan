package com.agri.agriscan.presentation.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.agri.agriscan.databinding.ActivityMainBinding
import com.agri.agriscan.presentation.ui.camera.CameraActivity
import com.agri.agriscan.util.NetworkUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Permission launcher
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.entries.all { it.value }
        if (allGranted) {
            openCamera()
        } else {
            Toast.makeText(
                this,
                "Cần cấp quyền camera để sử dụng chức năng này",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
    }

    private fun setupUI() {
        // Setup toolbar
        setSupportActionBar(binding.toolbar)

        // Identify Disease Button
        binding.btnIdentifyDisease.setOnClickListener {
            checkPermissionsAndOpenCamera()
        }

        // History Button
        binding.btnHistory.setOnClickListener {
            // TODO: Open History Activity
            Toast.makeText(this, "Chức năng đang phát triển", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkPermissionsAndOpenCamera() {
        // Check internet connection
        if (!NetworkUtils.isNetworkAvailable(this)) {
            Toast.makeText(
                this,
                "Không có kết nối internet. Vui lòng kiểm tra lại.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Check camera permission
        when {
            hasRequiredPermissions() -> {
                openCamera()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                // Show explanation
                Toast.makeText(
                    this,
                    "Cần quyền truy cập camera để chụp ảnh cây trồng",
                    Toast.LENGTH_LONG
                ).show()
                requestPermissions()
            }
            else -> {
                requestPermissions()
            }
        }
    }

    private fun hasRequiredPermissions(): Boolean {
        return REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermissions() {
        requestPermissionLauncher.launch(REQUIRED_PERMISSIONS)
    }

    private fun openCamera() {
        val intent = Intent(this, CameraActivity::class.java)
        startActivity(intent)
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA
        )
    }
}