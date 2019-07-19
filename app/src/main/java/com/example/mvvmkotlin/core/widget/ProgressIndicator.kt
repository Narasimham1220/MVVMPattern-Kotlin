package com.example.mvvmkotlin.core.widget

import android.animation.*
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import com.example.mvvmkotlin.R

class ProgressIndicator : View {
    private var startArcAngle = 0f
    private var endArcAngle = 0f
    private var indeterminateSweep: Float = 0.toFloat()
    private var indeterminateRotateOffset: Float = 0.toFloat()
    private var indeterminateAnimationStartAngle = -90f
    private val paintArc = Paint()
    private var animatorSet: AnimatorSet? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context) {
        paintArc.color = ContextCompat.getColor(context, R.color.colorPrimary)
        paintArc.strokeWidth = 25f
        paintArc.style = Paint.Style.STROKE
        paintArc.isAntiAlias = true
        paintArc.strokeCap = Paint.Cap.ROUND
    }

    fun setArcColor(color: Int) {
        paintArc.color = color
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        val rect = RectF(20f, 20f, (width - 20).toFloat(), (height - 20).toFloat())
        drawArc(canvas, rect, startArcAngle, endArcAngle, paintArc)
    }

    private fun drawArc(canvas: Canvas, rect: RectF, startAngle: Float, endAngle: Float, paint: Paint) {
        val path = Path()
        path.addArc(rect, startAngle, endAngle - startAngle)
        canvas.drawPath(path, paint)
    }

    fun animateGrowing(isForward: Boolean, callback: SpinnerCallback) {
        if (animatorSet != null) {
            return
        }

        animatorSet = AnimatorSet()
        animatorSet!!.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                if (animatorSet != null) {
                    animatorSet = null
                    callback.animationEnd(false)
                }
            }

            override fun onAnimationCancel(animation: Animator) {
                callback.animationEnd(true)
            }
        })

        val endObjectAnimator: ObjectAnimator
        if (isForward) {
            endObjectAnimator = ObjectAnimator.ofFloat(this, "endArcAngle", -90f, 270f)
            animatorSet!!.duration = 2000
        } else {
            endObjectAnimator = ObjectAnimator.ofFloat(this, "endArcAngle", 270f, -90f)
            animatorSet!!.duration = 3000
        }

        animatorSet!!.playTogether(ObjectAnimator.ofFloat(this, "startArcAngle", -90f, -90f), endObjectAnimator)
        animatorSet!!.start()
    }

    fun startIndeterminantAnimation() {
        if (animatorSet != null) {
            return
        }

        indeterminateSweep = INDETERMINANT_MIN_SWEEP
        val animSteps = 3
        var prevSet: AnimatorSet? = null
        var nextSet: AnimatorSet
        animatorSet = AnimatorSet()
        for (k in 0 until animSteps) {
            nextSet = createIndeterminateAnimator(k.toFloat(), animSteps)
            val builder = animatorSet!!.play(nextSet)

            if (prevSet != null) {
                builder.after(prevSet)
            }

            prevSet = nextSet
        }

        animatorSet!!.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                if (animatorSet != null) {
                    animatorSet = null

                    startIndeterminantAnimation()
                }
            }

        })

        animatorSet!!.start()
    }

    fun cancelAnimation() {
        if (animatorSet != null) {
            val a = this.animatorSet
            animatorSet = null
            a!!.cancel()
            setDefaultPosition()
            invalidate()
        }
    }

    private fun setDefaultPosition() {
        startArcAngle = -90f
        endArcAngle = -90f
        indeterminateRotateOffset = 0f
        indeterminateSweep = 0f
        indeterminateAnimationStartAngle = -90f
    }

    private fun createIndeterminateAnimator(step: Float, animSteps: Int): AnimatorSet {
        val maxSweep = 360f * (animSteps - 1) / animSteps + INDETERMINANT_MIN_SWEEP
        val start = -90f + step * (maxSweep - INDETERMINANT_MIN_SWEEP)
        val animDuration = 3000

        setAngles(indeterminateAnimationStartAngle + indeterminateRotateOffset, indeterminateSweep)
        // Extending the front of the arc
        val frontEndExtend = ValueAnimator.ofFloat(INDETERMINANT_MIN_SWEEP, maxSweep)
        frontEndExtend.duration = (animDuration / animSteps / 2).toLong()
        frontEndExtend.interpolator = DecelerateInterpolator(1f)
        frontEndExtend.addUpdateListener { animation ->
            indeterminateSweep = animation.animatedValue as Float
            setAngles(indeterminateAnimationStartAngle + indeterminateRotateOffset, indeterminateSweep)
        }

        // Overall rotation
        val rotateAnimator1 = ValueAnimator.ofFloat(step * 720f / animSteps, (step + .5f) * 720f / animSteps)
        rotateAnimator1.duration = (animDuration / animSteps / 2).toLong()
        rotateAnimator1.interpolator = LinearInterpolator()
        rotateAnimator1.addUpdateListener { animation ->
            indeterminateRotateOffset = animation.animatedValue as Float
            setAngles(indeterminateAnimationStartAngle + indeterminateRotateOffset, indeterminateSweep)
            invalidate()
        }

        // Retracting the back end of the arc
        val backEndRetract = ValueAnimator.ofFloat(start, start + maxSweep - INDETERMINANT_MIN_SWEEP)
        backEndRetract.duration = (animDuration / animSteps / 2).toLong()
        backEndRetract.interpolator = DecelerateInterpolator(1f)
        backEndRetract.addUpdateListener { animation ->
            indeterminateAnimationStartAngle = animation.animatedValue as Float
            indeterminateSweep = maxSweep - indeterminateAnimationStartAngle + start
            setAngles(indeterminateAnimationStartAngle + indeterminateRotateOffset, indeterminateSweep)
            invalidate()
        }

        // More overall rotation
        val rotateAnimator2 = ValueAnimator.ofFloat((step + .5f) * 720f / animSteps, (step + 1) * 720f / animSteps)
        rotateAnimator2.duration = (animDuration / animSteps / 2).toLong()
        rotateAnimator2.interpolator = LinearInterpolator()
        rotateAnimator2.addUpdateListener { animation ->
            indeterminateRotateOffset = animation.animatedValue as Float
            setAngles(indeterminateAnimationStartAngle + indeterminateRotateOffset, indeterminateSweep)
            invalidate()
        }

        val set = AnimatorSet()
        set.play(frontEndExtend).with(rotateAnimator1)
        set.play(backEndRetract).with(rotateAnimator2).after(rotateAnimator1)

        return set
    }

    private fun setAngles(start: Float, sweep: Float) {
        startArcAngle = start
        endArcAngle = start + sweep
    }

    fun getStartArcAngle(): Float {
        return startArcAngle
    }

    fun setStartArcAngle(startArcAngle: Float) {
        this.startArcAngle = startArcAngle
        invalidate()
    }

    fun getEndArcAngle(): Float {
        return endArcAngle
    }

    fun setEndArcAngle(endArcAngle: Float) {
        this.endArcAngle = endArcAngle
        invalidate()
    }

    interface SpinnerCallback {
        fun animationEnd(isInterrupted: Boolean)
    }

    companion object {
        private val INDETERMINANT_MIN_SWEEP = 15f
    }
}