package com.ait.minesweeper.model

import kotlin.random.Random.Default.nextInt

object MineSweeperModel { //we can only create an instance once

    public val FLAG = 1
    public val MINE = 2
    public val TRY = 3
    public var num = 0
    public var flaggedMines = 0
    public var triedSquares = 0

    //for each square, [0] is storage for flag, [1] is storage for mine, [2] is storage for whether it was tried  or not
    private val model = arrayOf(
        arrayOf(arrayOf(0,MINE,0), arrayOf(0,MINE,0), arrayOf(0,MINE,0), arrayOf(0,0,0), arrayOf(0,0,0)),
        arrayOf(arrayOf(0,0,0), arrayOf(0,0,0), arrayOf(0,0,0), arrayOf(0,0,0), arrayOf(0,0,0)),
        arrayOf(arrayOf(0,0,0), arrayOf(0,0,0), arrayOf(0,0,0), arrayOf(0,0,0), arrayOf(0,0,0)),
        arrayOf(arrayOf(0,0,0), arrayOf(0,0,0), arrayOf(0,0,0), arrayOf(0,0,0), arrayOf(0,0,0)),
        arrayOf(arrayOf(0,0,0), arrayOf(0,0,0), arrayOf(0,0,0), arrayOf(0,0,0), arrayOf(0,0,0))
    )


    private var mode = TRY



    fun resetModel() {
        for (i in 0..4) {
            for (j in 0..4) {
                model[i][j] = arrayOf(0,0,0)
            }
        }
        setMines()
        mode = TRY
        flaggedMines = 0
        triedSquares = 0
    }

    fun setFieldContentToFlag(x: Int, y: Int) {
        val square = model[x][y]
        square[0] = FLAG
        flaggedMines += 1
    }
    fun setFieldContentToTried(x: Int, y: Int) {
        val square = model[x][y]
        square[2] = TRY
        triedSquares += 1
    }

    fun  isEmpty(x: Int, y: Int): Boolean{
        val square = model[x][y]
        return (square[0] == 0 && square[1] == 0 && square[2] == 0)
    }
    fun  isFlagged(x: Int, y: Int): Boolean{
        val square = model[x][y]
        return square[0] == FLAG
    }
    fun  isMine(x: Int, y: Int): Boolean{
        val square = model[x][y]
        return square[1] == MINE
    }
    fun  isTried(x: Int, y: Int): Boolean{
        val square = model[x][y]
        return square[2] == TRY
    }

    fun setMines(){
        for (i in 0..2) {
                val x = nextInt(0,4)
                val y = nextInt(0,4)
            val square = model[x][y]
            square[1] = MINE
        }
    }

    fun checkWin() : Boolean{
        for (i in 0..4) {
            for (j in 0..4) {
                if(isMine(i,  j) && !isFlagged(i, j)){
                    return false
                }
            }
        }
        return true
    }


    fun revealNums(x: Int, y: Int) : Int{
        num = 0
        if(y>0){
            if(isMine(x,y-1))
                num += 1
        }
        if(y<4){
            if(isMine(x,y+1))
                num += 1
        }
        if(x>0){
            if(isMine(x-1,y))
                num += 1
            if(y>0){
               if(isMine(x-1,y-1))
                   num += 1
            }
            if(y<4){
                if(isMine(x-1,y+1))
                    num += 1
            }
        }
        if(x<4){
            if(isMine(x+1,y))
                num += 1
            if(y>0){
                if(isMine(x+1,y-1))
                    num += 1
            }
            if(y<4){
                if(isMine(x+1,y+1))
                    num += 1
            }
        }
        return num
    }

    fun currentMode() = mode

    fun changeMode(){
        mode =  if(mode ==  FLAG) TRY else FLAG
    }


}