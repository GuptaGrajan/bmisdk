package com.app.bmicalculator.view.fragmets

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.os.BundleCompat
import com.app.bmicalculator.R
import com.app.bmicalculator.base.BaseAdapter
import com.app.bmicalculator.base.BaseFragment
import com.app.bmicalculator.databinding.FragmentResultBinding
import com.app.bmicalculator.model.pojo.BmiData
import com.app.bmicalculator.model.pojo.auth.DashBoardModel
import com.app.bmicalculator.utils.BMIUtils
import com.app.bmicalculator.utils.HandleClick
import com.app.bmicalculator.view.adapter.AdapterBmiResult

class BmiResultFragment : BaseFragment<FragmentResultBinding>(), HandleClick,
    BaseAdapter.OnItemClickListener {

    private var adapterBmiResult: AdapterBmiResult? = null
    private var dashList: ArrayList<DashBoardModel> = ArrayList()
    private var bmiData: BmiData? = null


    override fun getLayoutRes(): Int = R.layout.fragment_result
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            bmiData = BundleCompat.getParcelable(it, "bmiData", BmiData::class.java)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
    }

    @SuppressLint("SetTextI18n")
    private fun initUI() {

        initBMIList()
        binding.apply {
            messageTV.text = BMIUtils.getCategory(bmiData?.bmi ?: 0f, baseActivity)
            ageTV.text = "${bmiData?.age ?: "0"}yr"
            weightTV.text = bmiData?.weight ?: ""
            heightTV.text = bmiData?.height ?: ""
            genderTV.text =
                if (bmiData?.male == true) this@BmiResultFragment.getString(R.string.male) else this@BmiResultFragment.getString(
                    R.string.female
                )
        }
        binding.circularSB.interactive = false

        binding.circularSB.progress = bmiData?.bmi ?: 0f

        binding.circularSB.setOnProgressChangedListener { progress ->
            binding.progressTV.text = "${bmiData?.bmi ?: 0f}"
        }

        binding.circularSB.setOnAnimationEndListener { _ ->
            // listen
        }


    }

    override fun onClickHandle(view: View) {


    }

    @SuppressLint("Recycle")
    private fun initBMIList() {
        dashList.clear()
        val names = resources.getStringArray(R.array.resultPageList)
        val value = resources.getStringArray(R.array.resultPageListValue)
        val resultPageListColor = resources.obtainTypedArray(R.array.resultPageListColor)
        for (i in names.indices) {
            // Use Color.parseColor to parse color strings
            dashList.add(
                DashBoardModel(
                    id = i + 1,
                    color = resultPageListColor.getResourceId(i, 0),
                    isSelected = BMIUtils.checkResult(bmiData?.bmi ?: 0f) == (i + 1),
                    title = names[i],
                    value = value[i]
                )
            )

        }

        adapterBmiResult = AdapterBmiResult(dashList)
        binding.resultRV.adapter = adapterBmiResult
        adapterBmiResult?.setOnItemClickListener(this)


    }


    override fun onItemClick(vararg itemData: Any) {


    }


}