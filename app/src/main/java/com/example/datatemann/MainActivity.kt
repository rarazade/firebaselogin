package com.example.datatemann

import android.content.Intent
import android.media.MediaPlayer.OnCompletionListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.example.datatemann.databinding.ActivityMainBinding
import com.example.datatemann.databinding.ActivityLoginBinding
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var auth:FirebaseAuth? = null
    private val RC_SIGN_IN = 1
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.logout.setOnClickListener(this)
        binding.save.setOnClickListener(this)
        binding.showData.setOnClickListener(this)

        auth = FirebaseAuth.getInstance()
    }

    private fun isEmpty(s: String) : Boolean {
        return TextUtils.isEmpty(s)
    }

    override fun onClick(p0: View?) {
        when (p0?.getId()) {
            R.id.save -> {
                //mendapatkan user id dr pengguna yg teratentikasi
                val getUserID = auth!!.currentUser!!.uid
                
                //mendapatkan instance dr database
                val database = FirebaseDatabase.getInstance()
                
                //menyimpan data yg diimputkan
                val getNama: String = binding.nama.getText().toString()
                val getAlamat: String = binding.alamat.getText().toString()
                val getNoHP: String = binding.noHp.getText().toString()

                //mrndapatkan referensi dr database
                val getRefrence: DatabaseReference
                getRefrence = database.reference

                //mengecek apa ada data yg kosong
                if(isEmpty(getNama) || isEmpty(getAlamat) || isEmpty(getNoHP)){
                    //jika ada, maka menampilkan pesan
                    Toast.makeText(this@MainActivity, "data tidak boleh ada yang kosong",
                        Toast.LENGTH_SHORT).show()
                }else{
                    //jika tidak ada, maka menyimpan ke database sesuai id
                    getRefrence.child("Admin").child(getUserID).child("DataTeman").push()
                        .setValue(data_teman(getNama, getAlamat, getNoHP))
                        .addOnCompleteListener(this){
                            binding.nama.setText("")
                            binding.alamat.setText("")
                            binding.noHp.setText("")
                            Toast.makeText(this@MainActivity, "Data Tersimpan!",
                                Toast.LENGTH_SHORT).show()
                        }
                }
            }
            R.id.logout -> {
                AuthUI.getInstance().signOut(this)
                    .addOnCompleteListener(object : OnCompleteListener<Void> {
                        override fun onComplete(p0: Task<Void>) {
                            Toast.makeText(this@MainActivity, "Logout Berhasil",Toast.LENGTH_SHORT).show()
                            intent = Intent(applicationContext, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    })
            }
            R.id.show_data -> {}
        }
    }
}