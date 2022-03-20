package com.extacy.ms.scene.main

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.extacy.ms.R
import com.extacy.ms.base.RVAdapter
import com.extacy.ms.base.ViewBindingFragment
import com.extacy.ms.common.Common
import com.extacy.ms.databinding.CellDeviceBinding
import com.extacy.ms.databinding.FragmentStoreInfoBinding
import com.extacy.ms.net.ms.api.APIDeviceList
import com.extacy.ms.net.ms.api.DeviceItem
import com.extacy.ms.net.socket.*

class StoreInfoFragment: ViewBindingFragment<FragmentStoreInfoBinding>() {

    var online: MutableList<Int> = mutableListOf()
    var devices: MutableList<DeviceItem> = mutableListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Common.selectedStore?.run {
            binding?.run {
                textStoreName.text = name
                buttonMoney.setOnClickListener {
                    val fragment = StoreMoneyFragment()
                    pushFragment(fragment)
                }

            }
            APIDeviceList.request(requestable, id) { res ->
                if( res.isSuccess() ) {
                    if( res.body != null ) {
                        devices = res.body!!
                        refreshDeviceList()
                    }
                    checkStoreisOnline()
                }
            }
        }
    }

    fun checkStoreisOnline() {
        val id = Common.selectedStore?.id ?: 1
        SocketClient.request(ReqStoreIsOnline(id), ResStoreIsOnline::class.java) { isOnline, e ->
            if( isOnline != null ) {
                if( isOnline.isOnline ) {
                    loadOnlineDevices()
                }
            }
        }
    }

    fun loadOnlineDevices() {
        val id = Common.selectedStore?.id ?: 1
        SocketClient.request(ReqDeviceList(id), ResDeviceList::class.java) { onlineDevices, e ->
            this.online = onlineDevices?.deviceList as MutableList<Int>
            refreshDeviceList()
        }
    }

    fun refreshDeviceList() {
        var onlineDevices: MutableList<DeviceItem> = devices?.filter { online.contains(it.id) == true } as MutableList<DeviceItem>
        var offlineDevices: MutableList<DeviceItem> = devices?.filter { online.contains(it.id) == false } as MutableList<DeviceItem>
        onlineDevices.sortWith( compareBy { it.name })
        offlineDevices.sortWith( compareBy { it.name })
        val filteredDevices = onlineDevices + offlineDevices

        binding?.recyclerDevices?.layoutManager = GridLayoutManager(requireContext(), 5, GridLayoutManager.VERTICAL, false)
        binding?.recyclerDevices?.adapter = object: RVAdapter<CellDeviceBinding>() {
            override fun count(): Int {
                return filteredDevices.size
            }

            override fun bind(cell: CellDeviceBinding?, pos: Int) {
                cell?.also { cell ->
                    val device = filteredDevices.get(pos)

                    cell.textName.text = device.name
                    if(!online.contains(device.id)) {
                        cell.root.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray_61))
                    } else {
                        cell.root.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_blue))
                    }

                    cell.root.setOnClickListener {
                        Common.selectedDevice = device
                        Common.selectedDeviceOnline = online.contains(device.id)
                        val fragment = DeviceFragment()
                        pushFragment(fragment)
                    }
                }
            }
        }
    }
}
