package com.extacy.ms.scene.main

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.extacy.ms.R
import com.extacy.ms.base.RVAdapter
import com.extacy.ms.base.ViewBindingFragment
import com.extacy.ms.common.Common
import com.extacy.ms.common.dateFormatDate
import com.extacy.ms.common.dateFormatFull
import com.extacy.ms.common.dateFormatTime
import com.extacy.ms.databinding.CellGameListBinding
import com.extacy.ms.databinding.CellStoreMoneyBinding
import com.extacy.ms.databinding.FragmentStoreMoneyBinding
import com.extacy.ms.net.ms.api.APIGameList
import com.extacy.ms.net.ms.api.APIStoreMoney
import com.extacy.ms.net.ms.api.MoneyList
import com.extacy.ms.scene.game.GameInfoFragment
import java.util.*

class StoreMoneyFragment: ViewBindingFragment<FragmentStoreMoneyBinding>() {
    var moneyList = MoneyList()
    var isLoading = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var now = Date()
        var endDate = now
        var cal = Calendar.getInstance()
        cal.time = now
        cal.add(Calendar.DAY_OF_MONTH, -1)
        var startDate = cal.time

        binding?.run {

            textStoreName.text = Common.selectedStore?.name
            textStartDate.text = dateFormatDate.format(startDate)
            textStartTime.text = dateFormatTime.format(startDate)

            textEndDate.text = dateFormatDate.format(endDate)
            textEndTime.text = dateFormatTime.format(endDate)
            textStartDate.setOnClickListener {
                binding?.run {
                    val date = dateFormatDate.parse(textStartDate.text.toString())
                    val cal = Calendar.getInstance()
                    cal.time = date
                    var picker = DatePickerDialog(requireContext(), { picker, year, month, day ->
                        String()

                        val str = String.format("%04d.%02d.%02d", year, month + 1, day)
                        textStartDate.text = str
                        requestMoneyList(force = true)
                    }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))

                    picker.datePicker.maxDate = endDateTime().time

                    picker.show()
                }
            }

            textStartTime.setOnClickListener {
                binding?.run {
                    val date = dateFormatTime.parse(textStartTime.text.toString())
                    val cal = Calendar.getInstance()
                    cal.time = date
                    TimePickerDialog(requireContext(), { picker, hour, minute ->
                        var str = String.format("%02d:%02d", hour, minute)
                        textStartTime.text = str
                        requestMoneyList(force = true)
                    }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
                }

            }

            textEndDate.setOnClickListener {
                binding?.run {
                    val date = dateFormatDate.parse(textEndDate.text.toString())
                    val cal = Calendar.getInstance()
                    cal.time = date
                    var picker = DatePickerDialog(requireContext(), { picker, year, month, day ->
                        val str = String.format("%04d.%02d.%02d", year, month + 1, day)
                        val resDate = dateCombind(str, textEndTime.text.toString())
                        var now = Date()
                        if( resDate > now)  {
                            showAlert("현재보다 클 수 없습니다.")
                        } else if( startDateTime() > date ) {
                            showAlert("시작 시간보다 작을 수 없습니다.")
                        }
                        textEndDate.text = str
                        requestMoneyList(force = true)
                    }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))

                    picker.datePicker.maxDate = Date().time
                    picker.datePicker.minDate = startDateTime().time
                    picker.show()
                }


            }

            textEndTime.setOnClickListener {
                binding?.run {
                    val date = dateFormatTime.parse(textEndTime.text.toString())
                    val cal = Calendar.getInstance()
                    cal.time = date
                    TimePickerDialog(requireContext(), { picker, hour, minute ->
                        var str = String.format("%02d:%02d", hour, minute)
                        textEndTime.text = str
                        requestMoneyList(force = true)
                    }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
                }
            }



            recyclerMoney.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            recyclerMoney.adapter = object: RVAdapter<CellStoreMoneyBinding>() {
                override fun count(): Int {
                    return moneyList.moneys.size
                }

                override fun bind(cell: CellStoreMoneyBinding?, pos: Int) {
                    cell?.run {
                        val money = moneyList.moneys[pos]

                        var dt = Date()
                        dt.time = money.dt

                        textDt.text = dateFormatFull.format(dt)
                        textMoney.text = "${money.money}"
                        textMoney.setTextColor( if( money.money > 0 ) ContextCompat.getColor(requireContext(), R.color.light_red) else ContextCompat.getColor(requireContext(), R.color.light_blue) )
                        textDeviceName.text = money.device.name
                        textMacAddr.text = money.device.mac_addr

                        if(pos == moneyList.moneys.size - 1 && moneyList.has_next) {
                            requestMoneyList()
                        }
                    }
                }
            }
            requestMoneyList(force = true)
        }
    }

    fun startDateTime(): Date {
        binding?.run {
            return dateCombind(textStartDate.text.toString(), textStartTime.text.toString())
        }
        return Date()
    }

    fun endDateTime(): Date {
        binding?.run {
            return dateCombind(textEndDate.text.toString(), textEndTime.text.toString())
        }
        return Date()
    }

    fun dateCombind(date: String, time: String): Date {
        val str = "$date $time"
        return dateFormatFull.parse(str)
    }


    @SuppressLint("NotifyDataSetChanged")
    fun requestMoneyList(force: Boolean = false) {
        if(isLoading) {
            return
        }

        val page = if( force ) 1 else moneyList.page + 1
        val storeId = Common.selectedStore?.id ?: 1

        APIStoreMoney.request(requestable, storeId, startDateTime().time, endDateTime().time, page) { res ->
            if (res.isSuccess()) {
                val body = res.body
                if( body != null ) {
                    if (page == 1) {
                        moneyList = body
                        binding?.textTotalMoney?.text = "${moneyList.total}"
                        binding?.textTotalMoney?.setTextColor( if( moneyList.total > 0 ) ContextCompat.getColor(requireContext(), R.color.light_red) else ContextCompat.getColor(requireContext(), R.color.light_blue) )
                    } else {
                        moneyList.moneys += body.moneys
                        moneyList.page = body.page
                        moneyList.has_next = body.has_next
                    }
                    binding?.recyclerMoney?.adapter?.notifyDataSetChanged()
                } else {
                    showToast("통신 실패")
                }
            } else {
                showToast("${res.code}\n(${res.msg})")
            }

            isLoading = false
        }
    }

}