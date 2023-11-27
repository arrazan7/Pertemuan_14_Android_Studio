package com.example.pertemuan_14_2

import android.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pertemuan_14_2.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    // firebase
    private val firestore = FirebaseFirestore.getInstance()
    private val aduanCollectionRef = firestore.collection("Aduan")

    private var updateId = ""
    private val aduanListLiveData: MutableLiveData<List<Aduan>> by lazy {
        MutableLiveData<List<Aduan>>()
    }

    private val listViewData = ArrayList<Aduan>()
    private lateinit var manager: RecyclerView.LayoutManager
    private lateinit var itemAdapter: AduanAdapter

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_NAMA = "extra_nama"
        const val EXTRA_ADUAN = "extra_aduan"
        const val EXTRA_DESC = "extra_desc"
        const val EXTRA_DATE = "extra_date"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        manager = LinearLayoutManager(this@MainActivity)

        itemAdapter = AduanAdapter(this, listViewData) { item ->
            // Handle item click event
            // Misalnya, buka detail aduan atau lakukan tindakan lain
            updateId = item.id
            val IntentToThirdActivity = Intent(this, MainActivity3::class.java)
                .apply {
                    putExtra(EXTRA_NAMA, item.nama)
                    putExtra(EXTRA_ADUAN, item.aduan)
                    putExtra(EXTRA_DESC, item.deskripsi)
                    putExtra(EXTRA_DATE, item.tanggal)
                    putExtra(EXTRA_ID, item.id)
                }
            startActivity(IntentToThirdActivity)
        }

        with(binding) {
            btnAdd.setOnClickListener {
                val IntentToSecondActivity = Intent(this@MainActivity, MainActivity2::class.java)
                startActivity(IntentToSecondActivity)
            }

            listView.layoutManager = LinearLayoutManager(this@MainActivity)
            listView.adapter = itemAdapter

            observeAduan()
            getAllAduan()
        }
    }

    private fun getAllAduan() {
        observeAduanChanges()
    }

    private fun observeAduan() {
        aduanListLiveData.observe(this) { aduanAduan ->
            listViewData.clear()
            listViewData.addAll(aduanAduan)
            itemAdapter.notifyDataSetChanged()

            Log.d("MainActivity", "Number of aduan: ${aduanAduan.size}")
        }
    }

    private fun observeAduanChanges() {
        aduanCollectionRef.addSnapshotListener { snapshots, error ->
            if (error != null) {
                Log.d("MainActivity", "Error listening for aduan changes: ", error)
                return@addSnapshotListener
            }
            val budgets = snapshots?.toObjects(Aduan::class.java)
            if (budgets != null) {
                aduanListLiveData.postValue(budgets)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getAllAduan()
    }

}