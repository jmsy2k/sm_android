package com.extacy.ms.scene.game

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import com.extacy.ms.R
import com.extacy.ms.base.RVAdapter
import com.extacy.ms.base.ViewBindingFragment
import com.extacy.ms.common.VirtualKey
import com.extacy.ms.common.getNames
import com.extacy.ms.databinding.CellGameEventBinding
import com.extacy.ms.databinding.FragmentGameInfoBinding
import com.extacy.ms.net.ms.api.*

class GameInfoFragment: ViewBindingFragment<FragmentGameInfoBinding>() {
    var gameId = 0
    var gameName = ""

    var gameInfo = GameInfo()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arg = arguments
        if( arg != null ) {
            gameId = arg.getInt("id")
            gameName = arg.getString("name").toString()
        }

        binding?.run {
            buttonAddEvent.setOnClickListener {
                showEditDialog()
            }
            textGameName.text = gameName

            recyclerEvents.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            recyclerEvents.adapter = object: RVAdapter<CellGameEventBinding>() {
                override fun count(): Int {
                    return gameInfo.events.size
                }

                override fun bind(cell: CellGameEventBinding?, pos: Int) {
                    cell?.run {
                        val event = gameInfo.events[pos]
                        textEventName.text = event.name
                        textVk.text = VirtualKey.fromInt(event.vk).name
                        buttonEdit.setOnClickListener {
                            showEditDialog(event)
                        }

                        buttonDelete.setOnClickListener {
                            APIDeleteEvent.request(requestable, event.id) { res ->
                                if( res.isSuccess() ) {
                                    requestGameInfo()
                                }
                            }
                        }
                    }
                }
            }
        }

        requestGameInfo()
    }


    @SuppressLint("NotifyDataSetChanged")
    fun requestGameInfo() {
        APIGameInfo.request(requestable, gameId) { res ->
            if( res.isSuccess() ) {
                gameInfo = res.body!!
                binding?.recyclerEvents?.adapter?.notifyDataSetChanged()
            }
        }
    }

    fun showEditDialog(event: GameEvent? = null) {
        val view = layoutInflater.inflate(R.layout.dialog_event, null)

        val editName = view.findViewById<EditText>(R.id.edit_name)
        val spinnerVk = view.findViewById<Spinner>(R.id.spinner_vk)

        val items = getNames<VirtualKey>()
        var selectedVk: VirtualKey = VirtualKey.A
        spinnerVk.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, items)
        spinnerVk.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedVk = VirtualKey.valueOf(items[position])
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
        if( event != null ) {
            editName.setText(event.name)
            selectedVk = VirtualKey.fromInt(event.vk)
            spinnerVk.setSelection(items.indexOf(VirtualKey.fromInt(event.vk).name))
        }

        var builder = AlertDialog.Builder(requireContext())
            .setTitle(if( id == null) "이벤트 추가" else "이벤트 수정")
            .setView(view)
            .setPositiveButton("확인") { dialogInterface, i ->
                if (editName.text.length == 0 ) {
                    showAlert("이름 없음")
                }
                val name = editName.text.toString()
                val vk = selectedVk.vk
                if (event != null) {
                    // 수정
                    APIModifyEvent.request(requestable, event.id, name, vk) { res ->
                        if( res.isSuccess() ) {
                            showAlert("수정 완료", confirmCallback = {
                                requestGameInfo()
                                dialogInterface.dismiss()
                            })
                        }
                    }
                } else {
                    // 생성
                    APIAddEvent.request(requestable, gameId, name, vk) { res ->
                        if( res.isSuccess() ) {
                            showAlert("추가 완료", confirmCallback = {
                                requestGameInfo()
                                dialogInterface.dismiss()
                            })
                        }

                    }
                }
            }
            .setNegativeButton("취소") { dialogInterface, i ->
                dialogInterface.dismiss()
            }
        builder.create().show()
    }


}