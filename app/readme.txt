# Cấu trúc dự án AgriScan - Plant Disease Recognition App

## 📂 Cấu trúc thư mục (Clean Architecture + MVVM)

```
app/src/main/
├── java/com/agri/agriscan/
│   │
│   ├── data/                              # DATA LAYER
│   │   ├── remote/
│   │   │   ├── api/
│   │   │   │   ├── PlantNetApi.kt                # API interface cho PlantNet
│   │   │   │   └── OpenAIApi.kt                  # API interface cho OpenAI GPT-4
│   │   │   │
│   │   │   ├── dto/                              # Data Transfer Objects
│   │   │   │   ├── plantnet/
│   │   │   │   │   ├── IdentificationRequest.kt
│   │   │   │   │   ├── IdentificationResponse.kt
│   │   │   │   │   ├── DiseaseRequest.kt
│   │   │   │   │   ├── DiseaseResponse.kt
│   │   │   │   │   └── VarietyDto.kt
│   │   │   │   │
│   │   │   │   └── openai/
│   │   │   │       ├── ChatRequest.kt
│   │   │   │       ├── ChatResponse.kt
│   │   │   │       └── TreatmentResponse.kt
│   │   │   │
│   │   │   └── interceptor/
│   │   │       ├── ApiKeyInterceptor.kt          # Thêm API key vào header
│   │   │       └── LoggingInterceptor.kt         # Log request/response
│   │   │
│   │   ├── local/
│   │   │   ├── database/
│   │   │   │   ├── AppDatabase.kt
│   │   │   │   └── dao/
│   │   │   │       ├── PlantDao.kt
│   │   │   │       ├── DiseaseDao.kt
│   │   │   │       └── HistoryDao.kt
│   │   │   │
│   │   │   └── entity/
│   │   │       ├── PlantEntity.kt
│   │   │       ├── DiseaseEntity.kt
│   │   │       └── HistoryEntity.kt
│   │   │
│   │   ├── repository/
│   │   │   ├── PlantRepositoryImpl.kt            # Implementation của Repository
│   │   │   ├── DiseaseRepositoryImpl.kt
│   │   │   └── TreatmentRepositoryImpl.kt
│   │   │
│   │   └── mapper/
│   │       ├── PlantMapper.kt                    # Map DTO <-> Domain Model
│   │       ├── DiseaseMapper.kt
│   │       └── TreatmentMapper.kt
│   │
│   ├── domain/                            # DOMAIN LAYER (Business Logic)
│   │   ├── model/
│   │   │   ├── Plant.kt                          # Domain model cho cây trồng
│   │   │   ├── PlantIdentification.kt            # Kết quả nhận dạng cây
│   │   │   ├── PlantVariety.kt                   # Giống cây
│   │   │   ├── Disease.kt                        # Domain model bệnh
│   │   │   ├── DiseaseIdentification.kt          # Kết quả nhận dạng bệnh
│   │   │   ├── Treatment.kt                      # Phương pháp điều trị
│   │   │   ├── ChemicalTreatment.kt              # Điều trị hóa học
│   │   │   ├── BiologicalTreatment.kt            # Điều trị sinh học
│   │   │   └── IdentificationHistory.kt          # Lịch sử nhận dạng
│   │   │
│   │   ├── repository/                           # Repository interfaces
│   │   │   ├── PlantRepository.kt
│   │   │   ├── DiseaseRepository.kt
│   │   │   └── TreatmentRepository.kt
│   │   │
│   │   └── usecase/
│   │       ├── plant/
│   │       │   ├── IdentifyPlantUseCase.kt       # Nhận dạng cây trồng
│   │       │   ├── GetPlantVarietiesUseCase.kt   # Lấy danh sách giống cây
│   │       │   └── SelectPlantVarietyUseCase.kt  # Chọn giống cây thủ công
│   │       │
│   │       ├── disease/
│   │       │   ├── IdentifyDiseaseUseCase.kt     # Nhận dạng bệnh
│   │       │   └── ConfirmDiseaseUseCase.kt      # Xác nhận bệnh
│   │       │
│   │       └── treatment/
│   │           ├── GetTreatmentUseCase.kt        # Lấy phương pháp điều trị
│   │           └── GetChemicalListUseCase.kt     # Lấy danh sách thuốc
│   │
│   ├── presentation/                      # PRESENTATION LAYER (UI + ViewModel)
│   │   ├── ui/
│   │   │   ├── main/
│   │   │   │   ├── MainActivity.kt
│   │   │   │   └── MainViewModel.kt
│   │   │   │
│   │   │   ├── camera/
│   │   │   │   ├── CameraActivity.kt             # Màn hình chụp ảnh
│   │   │   │   ├── CameraViewModel.kt
│   │   │   │   └── ImagePickerHelper.kt          # Helper lấy ảnh từ thư viện
│   │   │   │
│   │   │   ├── identification/
│   │   │   │   ├── plant/
│   │   │   │   │   ├── PlantIdentificationFragment.kt    # Màn hình nhận dạng cây
│   │   │   │   │   ├── PlantIdentificationViewModel.kt
│   │   │   │   │   ├── PlantResultAdapter.kt              # Adapter hiển thị kết quả
│   │   │   │   │   └── PlantSelectionDialog.kt            # Dialog chọn cây thủ công
│   │   │   │   │
│   │   │   │   └── disease/
│   │   │   │       ├── DiseaseIdentificationFragment.kt  # Màn hình nhận dạng bệnh
│   │   │   │       ├── DiseaseIdentificationViewModel.kt
│   │   │   │       └── DiseaseResultAdapter.kt           # Adapter hiển thị kết quả bệnh
│   │   │   │
│   │   │   ├── treatment/
│   │   │   │   ├── TreatmentFragment.kt          # Màn hình điều trị
│   │   │   │   ├── TreatmentViewModel.kt
│   │   │   │   ├── ChemicalTreatmentAdapter.kt   # Adapter danh sách thuốc hóa học
│   │   │   │   └── BiologicalTreatmentAdapter.kt # Adapter điều trị sinh học
│   │   │   │
│   │   │   ├── history/
│   │   │   │   ├── HistoryFragment.kt            # Màn hình lịch sử
│   │   │   │   ├── HistoryViewModel.kt
│   │   │   │   └── HistoryAdapter.kt
│   │   │   │
│   │   │   └── common/
│   │   │       ├── LoadingDialog.kt              # Dialog loading
│   │   │       ├── ErrorDialog.kt                # Dialog lỗi
│   │   │       └── ConfirmDialog.kt              # Dialog xác nhận
│   │   │
│   │   ├── mapper/
│   │   │   └── UiModelMapper.kt                  # Map Domain Model -> UI Model
│   │   │
│   │   └── model/
│   │       └── UiState.kt                        # UI State cho các màn hình
│   │
│   ├── di/                                # DEPENDENCY INJECTION
│   │   ├── AppModule.kt                          # Module cho App, Context
│   │   ├── NetworkModule.kt                      # Module cho Retrofit, OkHttp
│   │   ├── DatabaseModule.kt                     # Module cho Room Database
│   │   ├── RepositoryModule.kt                   # Module cho Repository
│   │   └── UseCaseModule.kt                      # Module cho UseCase
│   │
│   └── util/                              # UTILITIES
│       ├── Constants.kt                          # Hằng số
│       ├── ImageUtils.kt                         # Xử lý ảnh (resize, compress, base64)
│       ├── NetworkUtils.kt                       # Kiểm tra kết nối
│       ├── FileUtils.kt                          # Xử lý file
│       ├── DateUtils.kt                          # Format ngày tháng
│       └── Resource.kt                           # Wrapper cho Success/Error/Loading
│
└── res/
    ├── layout/
    │   ├── activity_main.xml
    │   ├── activity_camera.xml
    │   ├── fragment_plant_identification.xml
    │   ├── fragment_disease_identification.xml
    │   ├── fragment_treatment.xml
    │   ├── fragment_history.xml
    │   ├── item_plant_result.xml                 # Item cho danh sách kết quả cây
    │   ├── item_disease_result.xml               # Item cho danh sách kết quả bệnh
    │   ├── item_chemical_treatment.xml           # Item thuốc hóa học
    │   ├── item_biological_treatment.xml         # Item điều trị sinh học
    │   ├── dialog_plant_selection.xml            # Dialog chọn cây
    │   └── dialog_loading.xml                    # Dialog loading
    │
    ├── drawable/
    │   ├── ic_camera.xml
    │   ├── ic_gallery.xml
    │   ├── ic_disease.xml
    │   ├── ic_treatment.xml
    │   ├── ic_history.xml
    │   ├── bg_button.xml
    │   ├── bg_card.xml
    │   └── bg_input.xml
    │
    ├── values/
    │   ├── colors.xml
    │   ├── strings.xml
    │   ├── themes.xml
    │   └── dimens.xml
    │
    └── navigation/
        └── nav_graph.xml                         # Navigation component graph
```

## 🔑 Các file quan trọng cần tạo

### 1. build.gradle.kts (Module: app)

```kotlin
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.agri.agriscan"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.agri.agriscan"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        // API Keys - Nên lưu trong local.properties
        buildConfigField("String", "PLANTNET_API_KEY", "\"${project.findProperty("PLANTNET_API_KEY")}\"")
        buildConfigField("String", "OPENAI_API_KEY", "\"${project.findProperty("OPENAI_API_KEY")}\"")
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
}

dependencies {
    // AndroidX Core
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    
    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    
    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.6")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.6")
    
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    
    // Hilt
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-compiler:2.48")
    
    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    
    // Room Database
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    
    // CameraX
    implementation("androidx.camera:camera-camera2:1.3.1")
    implementation("androidx.camera:camera-lifecycle:1.3.1")
    implementation("androidx.camera:camera-view:1.3.1")
    
    // Image Loading
    implementation("io.coil-kt:coil:2.5.0")
    implementation("io.coil-kt:coil-compose:2.5.0")
    
    // Compose (optional - for modern UI)
    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.activity:activity-compose:1.8.2")
    debugImplementation("androidx.compose.ui:ui-tooling")
    
    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
```

### 2. local.properties (Thêm API keys)

```properties
PLANTNET_API_KEY=your_plantnet_api_key_here
OPENAI_API_KEY=your_openai_api_key_here
```

## 🎯 Luồng hoạt động chi tiết

### Flow 1: Nhận dạng cây trồng
```
MainActivity 
  → Click "Nhận dạng bệnh"
  → CameraActivity (chụp ảnh hoặc chọn từ thư viện)
  → PlantIdentificationFragment
     → ViewModel gọi IdentifyPlantUseCase
     → PlantNetApi /v2/identify endpoint
     → Hiển thị danh sách kết quả
     → Nếu score >= 0.5: Tự động chuyển sang nhận dạng bệnh
     → Nếu score < 0.5: Hiển thị dialog chọn cây từ danh sách hoặc varieties
```

### Flow 2: Nhận dạng bệnh
```
DiseaseIdentificationFragment
  → ViewModel gọi IdentifyDiseaseUseCase
  → PlantNetApi /v2/diseases endpoint (hoặc API tương ứng)
  → Hiển thị danh sách bệnh có thể
  → Người dùng xác nhận bệnh
  → Button "Xem phương pháp điều trị"
```

### Flow 3: Xem điều trị
```
TreatmentFragment
  → ViewModel gọi GetTreatmentUseCase
  → OpenAI GPT-4 API
  → Prompt: "Provide treatment methods for [disease] on [plant], including:
     1. Chemical methods (list of pesticides with active ingredients)
     2. Biological methods (natural solutions)"
  → Parse response và hiển thị
  → Tab 1: Hóa học (danh sách thuốc + thành phần)
  → Tab 2: Sinh học (phương pháp tự nhiên)
```

## 📱 Thiết kế giao diện đơn giản

### Màn hình chính
- Header: Logo + Tên app
- Button lớn: "Nhận dạng bệnh cây trồng" (với icon camera)
- Button: "Lịch sử nhận dạng"
- Bottom navigation: Home | History | Settings

### Màn hình Camera
- Preview camera full screen
- Button chụp ở giữa dưới
- Button chọn từ thư viện ở góc trái dưới
- Button đổi camera ở góc phải dưới

### Màn hình kết quả nhận dạng cây
- Ảnh đã chụp ở trên
- RecyclerView: Danh sách cây có thể (top 5)
  - Tên khoa học
  - Tên thông thường
  - Độ chính xác (%)
  - Ảnh minh họa
- Button: "Tiếp tục nhận dạng bệnh" (nếu score >= 0.5)
- Button: "Chọn cây khác" (nếu score < 0.5)

### Màn hình kết quả nhận dạng bệnh
- Ảnh đã chụp
- Tên cây đã chọn
- RecyclerView: Danh sách bệnh có thể
  - Tên bệnh
  - Độ chính xác
  - Mô tả ngắn
  - Ảnh minh họa
- Checkbox để người dùng xác nhận bệnh
- Button: "Xem phương pháp điều trị"

### Màn hình điều trị
- Header: Tên cây + Tên bệnh
- TabLayout: "Hóa học" | "Sinh học"
- Tab Hóa học:
  - RecyclerView danh sách thuốc
    - Tên thuốc
    - Hoạt chất
    - Liều lượng khuyến nghị
    - Cách sử dụng
- Tab Sinh học:
  - RecyclerView phương pháp tự nhiên
    - Tên phương pháp
    - Mô tả chi tiết
    - Các bước thực hiện
- Button: "Lưu vào lịch sử"

## 🔧 Các constant cần thiết

```kotlin
// Constants.kt
object Constants {
    // API Base URLs
    const val PLANTNET_BASE_URL = "https://my-api.plantnet.org/v2/"
    const val OPENAI_BASE_URL = "https://api.openai.com/v1/"
    
    // Endpoints
    const val ENDPOINT_IDENTIFY = "identify/{project}"
    const val ENDPOINT_DISEASES = "diseases/{project}"  // Cần xác nhận endpoint chính xác
    const val ENDPOINT_VARIETIES = "varieties"          // Cần xác nhận endpoint chính xác
    const val ENDPOINT_CHAT_COMPLETIONS = "chat/completions"
    
    // Thresholds
    const val CONFIDENCE_THRESHOLD = 0.5f
    const val MAX_RESULTS = 10
    
    // Image
    const val MAX_IMAGE_SIZE = 2048
    const val IMAGE_QUALITY = 85
    
    // OpenAI
    const val GPT_MODEL = "gpt-4o"
    const val MAX_TOKENS = 1000
}
```

## 📝 Next Steps

1. **Xác nhận API endpoints**: Bạn cần kiểm tra tài liệu PlantNet chính xác để xác định:
   - Endpoint cho varieties identification
   - Endpoint cho diseases identification
   - Request/Response format

2. **Tạo file code**: Tôi sẽ tạo các file implementation cho:
   - API interfaces
   - DTOs
   - Repository implementations
   - UseCases
   - ViewModels
   - UI layouts

3. **Setup dependencies**: Cài đặt các thư viện cần thiết

Bạn có muốn tôi tiếp tục tạo code implementation cho từng layer không?
# HƯỚNG DẪN IMPLEMENTATION - AGRISCAN APP

## 📋 Danh sách file đã tạo và vị trí

### 1. Constants & Utilities
```
app/src/main/java/com/agri/agriscan/util/
├── Constants.kt                  ✅ ĐÃ TẠO
├── Resource.kt                   (Đã có trong Constants.kt)
├── ImageUtils.kt                 CẦN TẠO
├── NetworkUtils.kt               CẦN TẠO
└── FileUtils.kt                  CẦN TẠO
```

### 2. Data Layer - Remote API
```
app/src/main/java/com/agri/agriscan/data/remote/
├── api/
│   ├── PlantNetApi.kt           ✅ ĐÃ TẠO
│   └── OpenAIApi.kt             ✅ ĐÃ TẠO
│
├── dto/
│   ├── plantnet/
│   │   └── PlantNetDtos.kt      ✅ ĐÃ TẠO (tất cả DTOs)
│   └── openai/
│       └── OpenAIDtos.kt        ✅ ĐÃ TẠO (tất cả DTOs)
│
└── interceptor/
    ├── ApiKeyInterceptor.kt      CẦN TẠO
    └── LoggingInterceptor.kt     CẦN TẠO
```

### 3. Domain Layer
```
app/src/main/java/com/agri/agriscan/domain/
├── model/
│   └── DomainModels.kt          ✅ ĐÃ TẠO (tất cả models)
│
├── repository/
│   ├── PlantRepository.kt        CẦN TẠO (interface)
│   ├── DiseaseRepository.kt      CẦN TẠO (interface)
│   └── TreatmentRepository.kt    CẦN TẠO (interface)
│
└── usecase/
    ├── plant/
    │   ├── IdentifyPlantUseCase.kt
    │   └── GetPlantVarietiesUseCase.kt
    ├── disease/
    │   └── IdentifyDiseaseUseCase.kt
    └── treatment/
        └── GetTreatmentUseCase.kt
```

## 🔧 BƯỚC 1: Copy các file đã tạo vào project

### Bước 1.1: Copy Constants.kt
```bash
# Đường dẫn đích
app/src/main/java/com/agri/agriscan/util/Constants.kt
```
File này chứa:
- ✅ API base URLs
- ✅ Tất cả endpoints
- ✅ Thresholds và settings
- ✅ Prompt template cho GPT-4

### Bước 1.2: Copy API Interfaces
```bash
# PlantNetApi.kt
app/src/main/java/com/agri/agriscan/data/remote/api/PlantNetApi.kt

# OpenAIApi.kt
app/src/main/java/com/agri/agriscan/data/remote/api/OpenAIApi.kt
```

### Bước 1.3: Copy DTOs
```bash
# PlantNet DTOs
app/src/main/java/com/agri/agriscan/data/remote/dto/plantnet/PlantNetDtos.kt

# OpenAI DTOs
app/src/main/java/com/agri/agriscan/data/remote/dto/openai/OpenAIDtos.kt
```

### Bước 1.4: Copy Domain Models
```bash
app/src/main/java/com/agri/agriscan/domain/model/DomainModels.kt
```

## 🔧 BƯỚC 2: Tạo các file tiếp theo

### File cần tạo ngay:

#### 1. NetworkModule.kt (Dependency Injection)
```kotlin
package com.agri.agriscan.di

import com.agri.agriscan.BuildConfig
import com.agri.agriscan.data.remote.api.OpenAIApi
import com.agri.agriscan.data.remote.api.PlantNetApi
import com.agri.agriscan.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PlantNetRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OpenAIRetrofit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }
    
    @Provides
    @Singleton
    fun providePlantNetOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(Constants.NETWORK_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(Constants.NETWORK_READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(Constants.NETWORK_WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }
    
    @Provides
    @Singleton
    fun provideOpenAIOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(Constants.NETWORK_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(Constants.NETWORK_READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(Constants.NETWORK_WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }
    
    @Provides
    @Singleton
    @PlantNetRetrofit
    fun providePlantNetRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.PLANTNET_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    @Provides
    @Singleton
    @OpenAIRetrofit
    fun provideOpenAIRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.OPENAI_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    @Provides
    @Singleton
    fun providePlantNetApi(@PlantNetRetrofit retrofit: Retrofit): PlantNetApi {
        return retrofit.create(PlantNetApi::class.java)
    }
    
    @Provides
    @Singleton
    fun provideOpenAIApi(@OpenAIRetrofit retrofit: Retrofit): OpenAIApi {
        return retrofit.create(OpenAIApi::class.java)
    }
}
```

#### 2. Repository Interfaces
```kotlin
// PlantRepository.kt
package com.agri.agriscan.domain.repository

import com.agri.agriscan.domain.model.*
import kotlinx.coroutines.flow.Flow

interface PlantRepository {
    suspend fun identifyPlant(
        imageUris: List<String>,
        organs: List<String>
    ): Flow<Resource<PlantIdentification>>
    
    suspend fun getVarieties(prefix: String? = null): Flow<Resource<List<PlantVariety>>>
    
    suspend fun identifyVariety(
        imageUris: List<String>,
        organs: List<String>
    ): Flow<Resource<VarietyIdentification>>
}

// DiseaseRepository.kt
package com.agri.agriscan.domain.repository

import com.agri.agriscan.domain.model.*
import kotlinx.coroutines.flow.Flow

interface DiseaseRepository {
    suspend fun getDiseases(prefix: String? = null): Flow<Resource<List<Disease>>>
    
    suspend fun identifyDisease(
        imageUris: List<String>,
        organs: List<String>
    ): Flow<Resource<DiseaseIdentification>>
}

// TreatmentRepository.kt
package com.agri.agriscan.domain.repository

import com.agri.agriscan.domain.model.*
import kotlinx.coroutines.flow.Flow

interface TreatmentRepository {
    suspend fun getTreatment(
        plant: Plant,
        disease: Disease
    ): Flow<Resource<Treatment>>
}
```

#### 3. ImageUtils.kt
```kotlin
package com.agri.agriscan.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import android.util.Base64

object ImageUtils {
    
    /**
     * Resize image to maximum dimensions while maintaining aspect ratio
     */
    fun resizeImage(
        context: Context,
        uri: Uri,
        maxWidth: Int = Constants.MAX_IMAGE_WIDTH,
        maxHeight: Int = Constants.MAX_IMAGE_HEIGHT
    ): Bitmap? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            
            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream?.close()
            
            var width = options.outWidth
            var height = options.outHeight
            var scale = 1
            
            while (width / 2 >= maxWidth && height / 2 >= maxHeight) {
                width /= 2
                height /= 2
                scale *= 2
            }
            
            val finalOptions = BitmapFactory.Options().apply {
                inSampleSize = scale
            }
            
            val finalInputStream = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(finalInputStream, null, finalOptions)
            finalInputStream?.close()
            
            // Fix orientation
            bitmap?.let { fixOrientation(context, uri, it) }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    /**
     * Fix image orientation based on EXIF data
     */
    private fun fixOrientation(context: Context, uri: Uri, bitmap: Bitmap): Bitmap {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val exif = inputStream?.let { ExifInterface(it) }
            inputStream?.close()
            
            val orientation = exif?.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            ) ?: ExifInterface.ORIENTATION_NORMAL
            
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270f)
                else -> bitmap
            }
        } catch (e: Exception) {
            bitmap
        }
    }
    
    /**
     * Rotate bitmap by given degrees
     */
    private fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
    
    /**
     * Compress bitmap to JPEG with quality
     */
    fun compressBitmap(
        bitmap: Bitmap,
        quality: Int = Constants.IMAGE_QUALITY
    ): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        return outputStream.toByteArray()
    }
    
    /**
     * Save bitmap to file
     */
    fun saveBitmapToFile(
        context: Context,
        bitmap: Bitmap,
        filename: String
    ): File? {
        return try {
            val file = File(context.cacheDir, filename)
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, Constants.IMAGE_QUALITY, outputStream)
            outputStream.flush()
            outputStream.close()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    /**
     * Convert bitmap to Base64 string
     */
    fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArray = compressBitmap(bitmap)
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }
    
    /**
     * Get file size in MB
     */
    fun getFileSizeInMB(file: File): Double {
        return file.length().toDouble() / (1024 * 1024)
    }
}
```

## 🎯 BƯỚC 3: Cấu hình build.gradle

### build.gradle.kts (Project level)
```kotlin
buildscript {
    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.48")
    }
}

plugins {
    id("com.android.application") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
    id("com.google.dagger.hilt.android") version "2.48" apply false
}
```

### build.gradle.kts (Module: app)
Đã có trong file project_structure.md

### local.properties
```properties
PLANTNET_API_KEY=your_plantnet_api_key_here
OPENAI_API_KEY=your_openai_api_key_here
```

## 🎯 BƯỚC 4: Tạo Application Class

```kotlin
package com.agri.agriscan

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AgriScanApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        // Initialize any required libraries here
    }
}
```

Và thêm vào AndroidManifest.xml:
```xml
<application
    android:name=".AgriScanApplication"
    ...>
```

## 📝 BƯỚC 5: Quy trình tiếp theo

Sau khi hoàn thành các file trên, bạn có muốn tôi tạo tiếp:

1. **Repository Implementations** (PlantRepositoryImpl, DiseaseRepositoryImpl, TreatmentRepositoryImpl)
2. **Use Cases** (IdentifyPlantUseCase, IdentifyDiseaseUseCase, GetTreatmentUseCase)
3. **ViewModels** (PlantIdentificationViewModel, DiseaseIdentificationViewModel, TreatmentViewModel)
4. **UI Layouts** (XML layouts cho các màn hình)
5. **Activities/Fragments** (Camera, Identification, Treatment screens)

## ✅ Checklist Implementation

- [x] Constants
- [x] API Interfaces (PlantNet + OpenAI)
- [x] DTOs (Request/Response models)
- [x] Domain Models
- [ ] Network Module (DI)
- [ ] Repository Interfaces
- [ ] Repository Implementations
- [ ] Mappers (DTO -> Domain)
- [ ] Use Cases
- [ ] ViewModels
- [ ] UI Layouts
- [ ] Activities/Fragments
- [ ] Image Utils
- [ ] Network Utils
- [ ] Database (Room) - Optional

Bạn muốn tôi tiếp tục tạo file nào tiếp theo?