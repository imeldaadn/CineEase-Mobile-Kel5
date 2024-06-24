package com.example.cineease.pos.film

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.cineease.databinding.ActivityAddFilmBinding
import com.example.cineease.model.Movie
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Locale

@Suppress("DEPRECATION")
class AddFilmActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddFilmBinding
    private var editMovie: Movie? = null
    private var file: File? = null

    @SuppressLint("SetTextI18n")
    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Uri? = result.data?.data
                data?.let { uri ->
                    file = uriToFile(uri, this)
                    binding.etFoto.setText("Gambar dari galeri")
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFilmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cekIzinPenyimpanan()
        cekEditData()
        btnBinding()
    }

    private fun cekIzinPenyimpanan() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU)
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    101
                )
            }
    }

    @SuppressLint("SetTextI18n")
    private fun cekEditData() {
        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("movie", Movie::class.java)
        } else {
            intent.getParcelableExtra("movie")
        }

        data?.let {
            with(binding) {
                editMovie = it
                etRating.setText(it.ratingFilm.toString())
                etJudul.setText(it.judulFilm)
                etStok.setText(it.stokFilm.toString())
                etHarga.setText(it.hargaTiket.toString())
                etFoto.setText("Foto dari database")
                btnTambah.text = "Update"
            }
        }
    }

    private fun btnBinding() {
        with(binding) {
            btnBack.btnBack.setOnClickListener {
                finish()
            }

            etFoto.setOnClickListener {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                getContent.launch(intent)
            }

            btnTambah.setOnClickListener {
                if (!isInputValid()) {
                    Toast.makeText(
                        this@AddFilmActivity,
                        "Harap isi semua bagian",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                if (editMovie != null && file == null) {
                    val m = editMovie!!.copy(
                        judulFilm = etJudul.text.toString(),
                        ratingFilm = etRating.text.toString().toDoubleOrNull(),
                        stokFilm = etStok.text.toString().toLongOrNull(),
                        hargaTiket = etHarga.text.toString().toDoubleOrNull(),
                        deskripsiFilm = etDeskripsi.text.toString(),
                        fotoUrl = editMovie!!.fotoUrl
                    )
                    saveData(m)
                    return@setOnClickListener
                }

                if (editMovie!= null) {
                    val m = editMovie!!.copy(
                        judulFilm = etJudul.text.toString(),
                        ratingFilm = etRating.text.toString().toDoubleOrNull(),
                        stokFilm = etStok.text.toString().toLongOrNull(),
                        hargaTiket = etHarga.text.toString().toDoubleOrNull(),
                        deskripsiFilm = etDeskripsi.text.toString()
                    )
                    uploadFile(m)
                    return@setOnClickListener
                }

                val movie = Movie(
                    judulFilm = etJudul.text.toString(),
                    ratingFilm = etRating.text.toString().toDoubleOrNull(),
                    stokFilm = etStok.text.toString().toLongOrNull(),
                    deskripsiFilm = etDeskripsi.text.toString(),
                    hargaTiket = etHarga.text.toString().toDoubleOrNull()
                )
                uploadFile(movie)
            }
        }
    }

    private fun uploadFile(movie: Movie) {
        binding.progressBar.visibility = View.VISIBLE
        val storage = Firebase.storage
        lifecycleScope.launch {
            try {
                val docByteArray = file!!.readBytes()
                val imageStorage =
                    storage.reference.child("film/${movie.id}")
                val result = imageStorage.putBytes(docByteArray).await()
                val downloadUrl = result.storage.downloadUrl.await().toString()

                saveData(movie.copy(fotoUrl = downloadUrl))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun saveData(movie: Movie) {
        val firestore = Firebase.firestore
        firestore.collection("movie").document(movie.id).set(movie)
            .addOnFailureListener {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show()
            }
            .addOnSuccessListener {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Berhasil menyimpan data", Toast.LENGTH_SHORT).show()
                finish()
            }
    }

    private fun isInputValid(): Boolean {
        with(binding) {
            when {
                etJudul.text.toString().isEmpty() -> return false
                etRating.text.toString().isEmpty() -> return false
                etStok.text.toString().isEmpty() -> return false
                etHarga.text.toString().isEmpty() -> return false
                etDeskripsi.text.toString().isEmpty() -> return false
                file == null && editMovie == null -> return false
                etRating.text.toString().toDouble() > 10.0 || etRating.text.toString()
                    .toDouble() < 0.0 -> {
                    etRating.requestFocus()
                    etRating.error = "Rating tidak boleh dibawah 0 atau diatas 10"
                    return false
                }

                else -> return true
            }
        }
    }

    private fun uriToFile(selectedImg: Uri, context: Context): File {
        val contentResolver: ContentResolver = context.contentResolver
        val myFile = createTemporaryFile(context)

        val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
        val outputStream: OutputStream = FileOutputStream(myFile)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()

        return myFile
    }

    private fun createTemporaryFile(context: Context): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            SimpleDateFormat(
                "dd-MMM-yyyy",
                Locale.US
            ).format(System.currentTimeMillis()), ".jpg", storageDir
        )
    }
}