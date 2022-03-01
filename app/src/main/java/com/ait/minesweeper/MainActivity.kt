package com.ait.minesweeper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.makeText
import com.ait.minesweeper.databinding.ActivityMainBinding
import com.ait.minesweeper.model.MineSweeperModel
import com.ait.minesweeper.ui.MineSweeperView
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MineSweeperModel.setMines()

        binding.btnReset.setOnClickListener {
            binding.mineSweeperView.resetGame()
            resetToggleButton()
        }

        binding.btnToggle.setOnClickListener{
            binding.mineSweeperView.changeMode()
        }
    }

    fun showTextMessage(msg: String){
        binding.tvMode.text = msg
    }

    fun showMessage(msg: String){
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_LONG).show()
    }

    fun resetToggleButton(){
        binding.btnToggle.isChecked = false
    }

}