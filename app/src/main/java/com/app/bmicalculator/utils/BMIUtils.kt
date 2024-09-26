package com.app.bmicalculator.utils

import android.content.Context
import com.app.bmicalculator.R

object BMIUtils {

    fun getCategory(result: Float, context: Context): String {
        val category: String = when {
            result < 16 -> {
                "${context.getString(R.string.you_are)} ${context.getString(R.string.bmi_very_severely_underweight)}"
            }

            result in 16.0..16.9 -> {
                "${context.getString(R.string.you_are)} ${context.getString(R.string.bmi_severely_underweight)}"
            }

            result > 17.0 && result <= 18.4 -> {
                "${context.getString(R.string.you_are)} ${context.getString(R.string.bmi_underweight)}"
            }

            result > 18.5 && result <= 24.9 -> {
                "${context.getString(R.string.you_have)} ${context.getString(R.string.bmi_normal)} ${
                    context.getString(
                        R.string.body_weight
                    )
                }"
            }

            result > 25.0 && result <= 29.9 -> {

                "${context.getString(R.string.you_are)} ${context.getString(R.string.bmi_overweight)}"

            }

            result > 30.0 && result <= 34.9 -> {
                "${context.getString(R.string.you_are)} in ${context.getString(R.string.bmi_obese_class_1)}"

            }

            result > 35.0 && result <= 39.9 -> {
                "${context.getString(R.string.you_are)} in ${context.getString(R.string.bmi_obese_class_2)}"

            }

            else -> {
                "${context.getString(R.string.you_are)} in ${context.getString(R.string.bmi_obese_class_3)}"

            }
        }
        return category
    }


    fun checkResult(result: Float): Int {

        val category = when {
            result < 16 -> {
                1
            }

            result in 16.0..16.9 -> {
                2
            }

            result > 17.0 && result <= 18.4 -> {
                3
            }

            result > 18.5 && result <= 24.9 -> {
                4
            }

            result > 25.0 && result <= 29.9 -> {

                5

            }

            result > 30.0 && result <= 34.9 -> {
                6

            }

            result > 35.0 && result <= 39.9 -> {
                7

            }

            else -> {
                8

            }
        }
        return category
    }

    fun getSuggestions(result: Float): String {
        val suggestion: String = when {
            result < 18.5 -> {
                "A BMI of under 18.5 indicates that a person has insufficient weight, so they may need to put on some weight. They should ask a doctor or dietitian for advice."
            }

            result in 18.5..24.9 -> {
                "A BMI of 18.5–24.9 indicates that a person has a healthy weight for their height. By maintaining a healthy weight, they can lower their risk of developing serious health problems."
            }

            result < 25 && result >= 29.9 -> {
                "A BMI of 25–29.9 indicates that a person is slightly overweight. A doctor may advise them to lose some weight for health reasons. They should talk with a doctor or dietitian for advice."
            }

            else -> {
                "A BMI of over 30 indicates that a person has obesity. Their health may be at risk if they do not lose weight. They should talk with a doctor or dietitian for advice."
            }
        }
        return suggestion
    }
}