package com.example.theawesome

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    enum class PLAYINGPLAYER {
        FIRST_PLAYER,
        SECOND_PLAYER
    }
    enum class WINNER_OF_GAME {
        PLAYER_ONE,
        PLAYER_TWO,
        NO_ONE
    }
    // instance variable
    var playingPlayer: PLAYINGPLAYER? = null
    var winner:WINNER_OF_GAME? = null
    var player1Option:ArrayList<Int> = ArrayList()
    var player2Option:ArrayList<Int> = ArrayList()
    var allDisabledImage:ArrayList<ImageButton?> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        try {
            startService(Intent(this,SoundService::class.java))
        }catch (e:Exception){
            e.printStackTrace()
            Toast.makeText(this,"hello",Toast.LENGTH_SHORT).show()
        }
        playingPlayer = PLAYINGPLAYER.FIRST_PLAYER
        winner = WINNER_OF_GAME.NO_ONE
    }

    private fun changeBackground(randomNumber: Int) {
        when(randomNumber) {
            1 -> tableLayout.setBackgroundColor(Color.LTGRAY)
            2 -> tableLayout.setBackgroundColor(Color.BLUE)
            3 -> tableLayout.setBackgroundColor(Color.BLACK)
            4 -> tableLayout.setBackgroundColor(Color.CYAN)
            5 -> tableLayout.setBackgroundColor(Color.GREEN)
            6 -> tableLayout.setBackgroundColor(Color.MAGENTA)
            7 -> tableLayout.setBackgroundColor(Color.RED)
            8 -> tableLayout.setBackgroundColor(Color.TRANSPARENT)
            9 -> tableLayout.setBackgroundColor(Color.YELLOW)
        }
    }

    fun imageButtonTapped(view: View) {
        val selectedImageButton: ImageButton = view as ImageButton
        var randomNumber = (Math.random() * 9 + 1).toInt()
       changeBackground(randomNumber)
        var optionNumber = 0
        when(selectedImageButton.id) {
            R.id.imageButton1 -> optionNumber = 1
            R.id.imageButton2 -> optionNumber = 2
            R.id.imageButton3 -> optionNumber = 3
            R.id.imageButton4 -> optionNumber = 4
            R.id.imageButton5 -> optionNumber = 5
            R.id.imageButton6 -> optionNumber = 6
            R.id.imageButton7 -> optionNumber = 7
            R.id.imageButton8 -> optionNumber = 8
            R.id.imageButton9 -> optionNumber = 9
        }
        action(optionNumber, selectedImageButton)
    }
    private fun action(optionNumber: Int, _selectedImageButton: ImageButton) {
        var selectedImageButton = _selectedImageButton
        if (playingPlayer == PLAYINGPLAYER.FIRST_PLAYER) {
            selectedImageButton.setImageResource(R.drawable.x_letter)
            player1Option.add(optionNumber)
            selectedImageButton.isEnabled = false
            allDisabledImage.add(selectedImageButton)
            playingPlayer = PLAYINGPLAYER.SECOND_PLAYER
        }
        if (playingPlayer == PLAYINGPLAYER.SECOND_PLAYER) {
//            playing with two people

//            selectedImageButton.setImageResource(R.drawable.o_letter)
//            player2Option.add(optionNumber)
//            selectedImageButton.isEnabled = false
//            allDisabledImage.add(selectedImageButton)
//            playingPlayer = PLAYINGPLAYER.FIRST_PLAYER

            // play with computer

            var notSelectedImageNumber = ArrayList<Int>()
            for(imageNumber in 1..9) {
                if(!player1Option.contains(imageNumber)) {
                    if(!player2Option.contains(imageNumber)){
                        notSelectedImageNumber.add(imageNumber)
                    }
                }
            }
            try {
                var randomNumber = (Math.random() * notSelectedImageNumber.size).toInt()
                var imageNumber = notSelectedImageNumber[randomNumber]
                when(imageNumber) {
                    1 -> selectedImageButton = imageButton1
                    2 -> selectedImageButton = imageButton2
                    3 -> selectedImageButton = imageButton3
                    4 -> selectedImageButton = imageButton4
                    5 -> selectedImageButton = imageButton5
                    6 -> selectedImageButton = imageButton6
                    7 -> selectedImageButton = imageButton7
                    8 -> selectedImageButton = imageButton8
                    9 -> selectedImageButton = imageButton9
                }
                selectedImageButton.setImageResource(R.drawable.o_letter)
                player2Option.add(imageNumber)
                selectedImageButton.isEnabled = false
                allDisabledImage.add(selectedImageButton)
                playingPlayer = PLAYINGPLAYER.FIRST_PLAYER
                println("image no $notSelectedImageNumber")
            } catch (e:Exception) {
                e.printStackTrace()
            }
        }
        specifyTheWinnerOfGame()
    }

    private fun listMatched(list1: List<Int>, list2: List<Int>): Boolean {
        for (l1 in list1) {
            var isTrue = false
            for (l2 in list2) {
                if (l1 == l2) {
                    isTrue = true
                    break
                }
            }
            if (!isTrue) {
                return false
            }
        }
        return true
    }

    private fun isWinner(player: List<Int>): Boolean {
        val list = arrayListOf(
            arrayListOf(1, 2, 3),
            arrayListOf(4, 5, 6),
            arrayListOf(7, 8, 9),
            arrayListOf(1, 4, 7),
            arrayListOf(2, 5, 8),
            arrayListOf(3, 6, 9),
            arrayListOf(1, 5, 9),
            arrayListOf(3, 5, 7)
        )
        for (i in list) {
            if (listMatched(i, player)) {
                return true
            }
        }
        return false
    }

    private fun specifyTheWinnerOfGame() {
        if (isWinner(player1Option)) {
            winner = WINNER_OF_GAME.PLAYER_ONE
            createAlert("player one wins","congratulation to player one",AlertDialog.BUTTON_POSITIVE,"ok")
            return
        } else if (isWinner(player2Option)) {
            winner = WINNER_OF_GAME.PLAYER_TWO
            createAlert("player two wins","congratulation to player two",AlertDialog.BUTTON_POSITIVE,"ok")
            return
        }else if(allDisabledImage.size == 9){
            createAlert("DRAW!!","no one won the game",AlertDialog.BUTTON_POSITIVE,"ok")
        }

    }
    private fun createAlert(title:String,message:String,whichButton:Int,buttonText:String){
        val alertDialog:AlertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle(title)
        alertDialog.setMessage(message)
        alertDialog.setButton(whichButton,buttonText) {_:DialogInterface?, _: Int ->
            resetGame()
        }
        alertDialog.setCancelable(false)
        alertDialog.show()

    }
    private fun resetGame(){
        player1Option.clear()
        player2Option.clear()
        allDisabledImage.clear()
        winner = WINNER_OF_GAME.NO_ONE
        playingPlayer =PLAYINGPLAYER.FIRST_PLAYER

        imageButton1.setImageResource(R.drawable.place_holder)
        imageButton2.setImageResource(R.drawable.place_holder)
        imageButton3.setImageResource(R.drawable.place_holder)
        imageButton4.setImageResource(R.drawable.place_holder)
        imageButton5.setImageResource(R.drawable.place_holder)
        imageButton6.setImageResource(R.drawable.place_holder)
        imageButton7.setImageResource(R.drawable.place_holder)
        imageButton8.setImageResource(R.drawable.place_holder)
        imageButton9.setImageResource(R.drawable.place_holder)

        imageButton1.isEnabled =true
        imageButton2.isEnabled =true
        imageButton3.isEnabled =true
        imageButton4.isEnabled =true
        imageButton5.isEnabled =true
        imageButton6.isEnabled =true
        imageButton7.isEnabled =true
        imageButton8.isEnabled =true
        imageButton9.isEnabled =true

    }
}
