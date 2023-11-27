package com.example.pertemuan_14_2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.pertemuan_14_2.databinding.ActivityMain2Binding
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity2 : AppCompatActivity() {
    lateinit var binding: ActivityMain2Binding
    //firebase
    private val firestore = FirebaseFirestore.getInstance()
    private val aduanCollectionRef = firestore.collection("Aduan")

    private var updateId = ""
    private val aduanListLiveData: MutableLiveData<List<Aduan>> by lazy {
        MutableLiveData<List<Aduan>>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            btnAdd.setOnClickListener {
                val name = edtNama.text.toString()
                val title = edtAduan.text.toString()
                val desc = edtDeskripsi.text.toString()
                val date = edtTanggal.text.toString()
                val newAduan = Aduan(
                    nama = name,
                    aduan = title,
                    deskripsi = desc,
                    tanggal = date
                )
                addData(newAduan)
                startActivity(Intent(this@MainActivity2, MainActivity::class.java))
            }
        }
    }

    private fun getAllAduan(){
        observeAduanChanges()
    }

    private fun observeAduanChanges(){
        aduanCollectionRef.addSnapshotListener{ snapshot, error ->
            if (error != null) {
                Log.d("MainActivity", "Error listening for aduan change: ", error)
                return@addSnapshotListener
            }
            val aduan = snapshot?.toObjects(Aduan::class.java)
            if (aduan!= null) {
                aduanListLiveData.postValue(aduan)
            }
        }
    }
    // dihandle untuk kondisi sukses, akan mereturn docRef
    private fun addData(aduan: Aduan) {
        aduanCollectionRef.add(aduan)
            .addOnSuccessListener { docRef ->
                val createSuaraId = docRef.id
                //id nya di update sesuai id yang berhasil
                aduan.id = createSuaraId
                docRef.set(aduan)
                    .addOnFailureListener{
                        Log.d("MainActivity2", "Error update aduan id", it)
                    }
                resetForm()
            }
            .addOnFailureListener{
                Log.d("MainActivity2", "Error add aduan", it)
            }
    }

    private fun updateData(aduan: Aduan){
        aduan.id = updateId
        aduanCollectionRef.document(updateId).set(aduan)
            .addOnFailureListener{
                Log.d("MainActivity2", "Error update data aduan", it)
            }
    }

    private fun deleteData(aduan: Aduan){
        if (aduan.id.isEmpty()) {
            Log.d("MainActivity2", "Error delete data empty ID", return)
        }

        aduanCollectionRef.document(aduan.id).delete()
            .addOnFailureListener{
                Log.d("MainActivity2", "Error delete data aduan")
            }
    }

    private fun resetForm() {
        with(binding){
            edtNama.setText("")
            edtAduan.setText("")
            edtDeskripsi.setText("")
            edtTanggal.setText("")
        }
    }
}