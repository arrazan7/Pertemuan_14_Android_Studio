package com.example.pertemuan_14_2

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pertemuan_14_2.databinding.ListAduanBinding

class AduanAdapter (private val context: Context, private val listData: List<Aduan>, private val onClickData: (Aduan) -> Unit) :
    RecyclerView.Adapter<AduanAdapter.ItemDataViewHolder>() {

    inner class ItemDataViewHolder(private val binding: ListAduanBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Aduan) {
            with(binding) {
                // Lakukan binding data ke tampilan
                namaLengkap.text = "Nama Pengadu : " + data.nama
                namaAduan.text = "Judul Aduan : " + data.aduan
                deskripsiAduan.text = "Isi Aduan : " + data.deskripsi
                tanggalAduan.text = "Tanggal Aduan : " + data.tanggal

                // Atur listener untuk onClick
                itemView.setOnClickListener {
                    onClickData(data)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemDataViewHolder {
        val binding = ListAduanBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ItemDataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemDataViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount(): Int = listData.size
}