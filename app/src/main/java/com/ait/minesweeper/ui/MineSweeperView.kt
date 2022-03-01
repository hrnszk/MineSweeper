package com.ait.minesweeper.ui

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.ait.minesweeper.MainActivity
import com.ait.minesweeper.R
import com.ait.minesweeper.model.MineSweeperModel as MineSweeperModel

class MineSweeperView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    lateinit var paintBackGround: Paint
    lateinit var paintLine: Paint
    lateinit var paintText: Paint
    var numOfMines = MineSweeperModel.num
    var numFlaggedMine = 0
    var triedSquare = 0


    init {

        paintBackGround = Paint()
        paintBackGround.color = Color.BLACK
        paintBackGround.style = Paint.Style.FILL

        paintLine = Paint()
        paintLine.color = Color.WHITE
        paintLine.style = Paint.Style.STROKE
        paintLine.strokeWidth = 5f

        paintText = Paint()
        paintText.color = Color.RED
        paintText.textSize = 100f

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintBackGround)

        drawGameArea(canvas!!)

        drawPlayers(canvas!!)
}



    private fun drawPlayers(canvas: Canvas) {
        for (i in 0..4) {
            for (j in 0..4) {
                if(MineSweeperModel.isFlagged(i, j)){
                        val centerX = (i * width / 5 + width / 10).toFloat()
                        val centerY = (j * height / 5 + height / 10).toFloat()
                        val radius = height / 10 - 4
                        canvas.drawCircle(centerX, centerY, radius.toFloat(), paintLine)
                    }
                else if(MineSweeperModel.isTried(i, j)){
                        val centerX = (i * width / 5 + width / 10).toFloat()
                        val centerY = (j * height / 5 + height / 10).toFloat()
                        numOfMines = MineSweeperModel.revealNums(i, j)
                        canvas?.drawText(numOfMines.toString(), centerX, centerY, paintText)
                    }
                else if(MineSweeperModel.isEmpty(i, j)){}
                else if(MineSweeperModel.isMine(i, j)){}
                }
            }
        }



    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {

            val tX = event.x.toInt() / (width / 5)
            val tY = event.y.toInt() / (height / 5)


            //trying
            if (MineSweeperModel.currentMode() == MineSweeperModel.TRY) {
                //empty square
                if (tX < 5 && tY < 5 && MineSweeperModel.isEmpty(tX, tY)) {
                    MineSweeperModel.setFieldContentToTried(tX, tY)
                    triedSquare += 1
                    invalidate()
                }
                //with mine
                else if (tX < 5 && tY < 5 && MineSweeperModel.isMine(tX, tY)) {
                    (context as MainActivity).showMessage(context.getString(R.string.text_gameover)) //this is how you access the MainActivity class from the View class; context is like the root class? for every activity
                    resetGame()
                    invalidate()
                }
            }

            //flagging
            else if (MineSweeperModel.currentMode() == MineSweeperModel.FLAG) {
                //empty square
                if (tX < 5 && tY < 5 && MineSweeperModel.isEmpty(tX, tY)) {
                    (context as MainActivity).showMessage(context.getString(R.string.text_gameover)) //this is how you access the MainActivity class from the View class; context is like the root class? for every activity
                    resetGame()
                    invalidate()
                }
                //with mine
                else if (tX < 5 && tY < 5 && MineSweeperModel.isMine(tX, tY)) {
                    MineSweeperModel.setFieldContentToFlag(tX, tY)
                    numFlaggedMine += 1
                    invalidate()
                }
            }

            if(MineSweeperModel.checkWin()) {
                (context as MainActivity).showMessage(context.getString(R.string.text_win)) //this is how you access the MainActivity class from the View class; context is like the root class? for every activity
                resetGame()
                invalidate()
            }
        }
        return true
    }

    private fun drawGameArea(canvas: Canvas) {
        // border
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintLine)
        // four horizontal lines
        canvas.drawLine(
            0f, (height / 5).toFloat(), width.toFloat(), (height / 5).toFloat(),
            paintLine
        )
        canvas.drawLine(
            0f, (2 * height / 5).toFloat(), width.toFloat(),
            (2 * height / 5).toFloat(), paintLine
        )
        canvas.drawLine(
            0f, (3 * height / 5).toFloat(), width.toFloat(),
            (3 * height / 5).toFloat(), paintLine
        )
        canvas.drawLine(
            0f, (4 * height / 5).toFloat(), width.toFloat(),
            (4 * height / 5).toFloat(), paintLine
        )

        // four vertical lines
        canvas.drawLine(
            (width / 5).toFloat(), 0f, (width / 5).toFloat(), height.toFloat(),
            paintLine
        )
        canvas.drawLine(
            (2 * width / 5).toFloat(), 0f, (2 * width / 5).toFloat(), height.toFloat(),
            paintLine
        )
        canvas.drawLine(
            (3 * width / 5).toFloat(), 0f, (3 * width / 5).toFloat(), height.toFloat(),
            paintLine
        )
        canvas.drawLine(
            (4 * width / 5).toFloat(), 0f, (4 * width / 5).toFloat(), height.toFloat(),
            paintLine
        )

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = View.MeasureSpec.getSize(widthMeasureSpec)
        val h = View.MeasureSpec.getSize(heightMeasureSpec)
        val d = if (w == 0) h else if (h == 0) w else if (w < h) w else h
        setMeasuredDimension(d, d)
    }

    public fun resetGame() {
        MineSweeperModel.resetModel()
        (context as MainActivity).resetToggleButton()
        (context as MainActivity).showTextMessage(context.getString(R.string.text_current_mode, "TRY"))

        invalidate() //when calling invlaidate, it calls the onCreate method (draws the board)
    }

    public fun changeMode(){
        MineSweeperModel.changeMode()
        var modeString =""
        if(MineSweeperModel.currentMode() == MineSweeperModel.FLAG){
            modeString += "FLAG"
        }
        else if(MineSweeperModel.currentMode() == MineSweeperModel.TRY){
            modeString += "TRY"
        }
        (context as  MainActivity).showTextMessage(context.getString(R.string.text_current_mode, modeString))

        invalidate()
    }

}