/*
 * Copyright (c) 2018 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.natallia.radaman.experimentswithcomplexlayout

import android.animation.ValueAnimator
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.animation.LinearInterpolator
import kotlinx.android.synthetic.main.keyframe1.*

class MainActivity : AppCompatActivity() {

    private val constraintSet1 = ConstraintSet()
    private val constraintSet2 = ConstraintSet()

    private var isOffScreen = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.keyframe1)

        switch1.setOnCheckedChangeListener { _, isChecked ->
            switch1.setText(if (isChecked) R.string.round_trip else R.string.one_way)
        }

        constraintSet1.clone(mainConstraintLayout)
        constraintSet2.clone(this, R.layout.activity_main)

        departButton.setOnClickListener{
            val layoutParams = rocketIcon.layoutParams as ConstraintLayout.LayoutParams
            val startAngle = layoutParams.circleAngle
            val endAngle = startAngle + (if (switch1.isChecked) 360 else 180)

            val anim = ValueAnimator.ofFloat(startAngle, endAngle)
            anim.addUpdateListener { valueAnimator ->

                val animatedValue = valueAnimator.animatedValue as Float
                val layoutParams = rocketIcon.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.circleAngle = animatedValue
                rocketIcon.layoutParams = layoutParams

                rocketIcon.rotation = (animatedValue % 360 - 270)
            }

            anim.duration = if (switch1.isChecked) 2000 else 1000

            anim.interpolator = LinearInterpolator()
            anim.start()
        }
    }

    override fun onEnterAnimationComplete() {
        super.onEnterAnimationComplete()

        constraintSet2.clone(this, R.layout.activity_main)

        val transition = AutoTransition()
        transition.duration = 1000
        TransitionManager.beginDelayedTransition(mainConstraintLayout, transition)

        constraintSet2.applyTo(mainConstraintLayout)
    }
}
