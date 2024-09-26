package com.app.bmicalculator.view.fragmets

import android.annotation.SuppressLint
import android.icu.text.DecimalFormat
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import com.app.bmicalculator.R
import com.app.bmicalculator.base.BaseFragment
import com.app.bmicalculator.databinding.FragmentCalculateBmiBinding
import com.app.bmicalculator.model.pojo.BmiData
import com.app.bmicalculator.utils.HandleClick
import com.app.bmicalculator.utils.extensions.afterTextChanged
import com.app.bmicalculator.utils.extensions.checkString
import com.app.bmicalculator.utils.extensions.getLength
import com.app.bmicalculator.utils.extensions.log
import com.app.bmicalculator.utils.extensions.onTextChanged
import com.app.bmicalculator.utils.extensions.replaceFragment

class CalculateBmiFragment : BaseFragment<FragmentCalculateBmiBinding>(), HandleClick {

    private var height: Float = 0f
    private var weight: Float = 0f


    private var countWeight = 50
    private var countAge = 19
    private var chosen: Boolean = true


    override fun getLayoutRes(): Int = R.layout.fragment_calculate_bmi

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    @SuppressLint("SetTextI18n")
    private fun initUI() {
        binding.handleClick = this
        binding.maleCB.isChecked = true
        binding.femaleCB.isChecked = false
        chosen = false
        binding.heightTxtET.afterTextChanged { it, watcher ->
            if (it.isNotEmpty() && !it.endsWith("cm") && !it.endsWith("c") && !it.endsWith("m")) {
                // Remove the listener before setting the text to avoid infinite loop
                binding.heightTxtET.removeTextChangedListener(watcher)
                binding.heightTxtET.setText("${it}cm")
                // Add the listener back after setting the text
                binding.heightTxtET.addTextChangedListener(watcher)
                if (binding.heightTxtET.getLength() > 2) {
                    binding.heightTxtET.setSelection(binding.heightTxtET.getLength() - 2)
                }
            } else if (it.isEmpty()) {
                binding.heightTxtET.removeTextChangedListener(watcher)
                binding.heightTxtET.setText("")
                binding.heightTxtET.addTextChangedListener(watcher)
                binding.heightTxtET.setSelection(binding.heightTxtET.getLength())
            } else if (binding.heightTxtET.getLength() > 2) {
                binding.heightTxtET.setSelection(binding.heightTxtET.getLength() - 2)
            }
            binding.Seekbar.progress =
                binding.heightTxtET.checkString().replace("c", "").replace("m", "")
                    .replace("cm", "").ifEmpty { "0" }.toInt()
        }


        binding.weightTxtET.afterTextChanged { it, watcher ->
            if (it.isNotEmpty() && !it.endsWith("Kg") && !it.endsWith("K") && !it.endsWith("g")) {
                // Remove the listener before setting the text to avoid infinite loop
                binding.weightTxtET.removeTextChangedListener(watcher)
                binding.weightTxtET.setText("${it}Kg")
                // Add the listener back after setting the text
                binding.weightTxtET.addTextChangedListener(watcher)
                if (binding.weightTxtET.getLength() > 2) {
                    binding.weightTxtET.setSelection(binding.weightTxtET.getLength() - 2)
                }
            } else if (it.isEmpty()) {
                binding.weightTxtET.removeTextChangedListener(watcher)
                binding.weightTxtET.setText("")
                binding.weightTxtET.addTextChangedListener(watcher)
                binding.weightTxtET.setSelection(binding.weightTxtET.getLength())

            } else if (binding.weightTxtET.getLength() > 2) {
                binding.weightTxtET.setSelection(binding.weightTxtET.getLength() - 2)
            }


            countWeight = binding.weightTxtET.checkString().replace("K", "").replace("g", "")
                .replace("Kg", "").ifEmpty { "0" }.toInt()

        }






        binding.ageET.onTextChanged { s, _ ->
            countAge = s.ifEmpty { "0" }.toInt()
        }



        binding.Seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val ht = progress.toString() + resources.getString(R.string.cm)
                binding.heightTxtET.setText(ht)
                height = progress.toFloat() / 100
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    override fun onClickHandle(view: View) {

        when (view.id) {
            R.id.maleCB -> {
                binding.maleCB.isChecked = true
                binding.femaleCB.isChecked = false
                chosen = false
            }

            R.id.femaleCB -> {
                binding.femaleCB.isChecked = true
                binding.maleCB.isChecked = false
                chosen = false
            }

            R.id.weightPlusIV -> {
                Log.e("weightIV plus", "${countWeight++}")
                binding.weightTxtET.setText("${countWeight++}")
                binding.weightTxtET.setSelection(binding.weightTxtET.getLength())
            }

            R.id.weightMinusIV -> {
                Log.e("weightIV Minus", "${countWeight--}")
                binding.weightTxtET.setText("${countWeight--}")
            }

            R.id.agePlusIV -> {
                Log.e("weightIV AgePlus", "${countAge++}")
                binding.ageET.setText(countAge++.toString())
            }

            R.id.ageMinusIV -> {
                Log.e("weightIV AgePlus", "${countAge--}")
                binding.ageET.setText(countAge--.toString())
            }

            R.id.calculateBT -> {
                if (!chosen) {
                    if (height.equals(0f)) {
                        Toast.makeText(
                            requireContext(), "Height cannot be Zero, Try again", Toast.LENGTH_SHORT
                        ).show()
                    } else {

                        calculateBMI()
                    }
                } else {
                    Toast.makeText(requireContext(), "Choose your gender", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }


    private fun calculateBMI() {
        weight = binding.weightTxtET.checkString().replace("Kg", "").toFloat()
        val age = binding.ageET.checkString()
        val bmi = DecimalFormat("#.##").format((weight / (height * height))).toFloat()






        log("BMI Result :-> $bmi \n Age:-> $age")

        val bundle = Bundle()
        bundle.putParcelable("bmiData", BmiData().apply {
            this.age = binding.ageET.checkString()
            this.bmi = bmi
            height = binding.heightTxtET.checkString()
            weight = binding.weightTxtET.checkString()
            male = binding.maleCB.isChecked
            female = binding.femaleCB.isChecked
        })
        baseActivity.replaceFragment(BmiResultFragment(), bundle)
    }

}