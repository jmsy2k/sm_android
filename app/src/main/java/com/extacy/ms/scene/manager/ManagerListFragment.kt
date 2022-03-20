package com.extacy.ms.scene.manager

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.extacy.ms.R
import com.extacy.ms.base.RVAdapter
import com.extacy.ms.base.ViewBindingFragment
import com.extacy.ms.common.Common
import com.extacy.ms.databinding.CellManagerListBinding
import com.extacy.ms.databinding.FragmentManagerListBinding
import com.extacy.ms.net.ms.api.*

class ManagerListFragment: ViewBindingFragment<FragmentManagerListBinding>() {
    var managerList = ManagerList()
    var isLoading = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.run {
            buttonAddManager.setOnClickListener {
                APIManagerType.request(requestable) { typeRes ->
                    if (typeRes.isSuccess()) {

                        val content = layoutInflater.inflate(R.layout.dialog_add_manager, null)
                        val editName = content.findViewById<EditText>(R.id.edit_name)
                        val editId = content.findViewById<EditText>(R.id.edit_id)
                        val editPw = content.findViewById<EditText>(R.id.edit_pw)
                        val editPwConfirm = content.findViewById<EditText>(R.id.edit_pw_confirm)
                        val spinner = content.findViewById<Spinner>(R.id.spinner_type)
                        val managerTypes = typeRes.body?.filter { type -> type.id > Common.userInfo.manager_type.id }
                        spinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, managerTypes?.map { type -> type.name_ko} ?: listOf())


                        val builder = AlertDialog.Builder(requireContext())
                            .setTitle("관리자 추가")
                            .setView(content)
                            .setPositiveButton("확인") { dialog, i ->
                                if (editName.text.length < 2) {
                                    showAlert("이름 입력 2글자이상 필요");
                                    return@setPositiveButton
                                }

                                if (editId.text.length < 5) {
                                    showAlert("아이디 입력 5글자 이상 필요")
                                    return@setPositiveButton
                                }

                                if (editPw.text.length < 3) {
                                    showAlert("비밀번호 입력 3글자 이상 필요")
                                    return@setPositiveButton
                                }

                                if (editPw.text.toString() != editPwConfirm.text.toString()) {
                                    showAlert("비밀번호 확인 안됨 ")
                                    return@setPositiveButton
                                }

                                var type = managerTypes?.get(spinner.selectedItemPosition)?.id ?: 3

                                APIAddManager.request(requestable, editId.text.toString(),editPw.text.toString(), editName.text.toString(), type) { res ->
                                    if( res.isSuccess() ) {
                                        showAlert("추가 되었습니다.", confirmCallback = {
                                            binding?.editSearchManager?.setText(editName.text.toString())
                                            requestManagerList(editName.text.toString())
                                            dialog.dismiss()
                                        })
                                    } else {
                                        showAlert("${res.code}\n${res.msg}")
                                    }
                                }

                            }
                            .setNegativeButton("취소") { dialog, i ->
                                dialog.dismiss()
                            }
                        builder.create().show()
                    } else {
                        showAlert("${typeRes.code}\n${typeRes.msg}")
                    }
                }
            }


            buttonSearch.setOnClickListener {
                requestManagerList(binding?.editSearchManager?.text?.toString() ?: "", true)
            }

            editSearchManager.setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                    requestManagerList(binding?.editSearchManager?.text.toString(), true)
                    return@setOnKeyListener true
                }
                false
            }

            recyclerManagerList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            recyclerManagerList.adapter = object: RVAdapter<CellManagerListBinding>() {
                override fun count(): Int {
                    return managerList.managers.size
                }

                override fun bind(cell: CellManagerListBinding?, pos: Int) {
                    val manager = managerList.managers[pos]
                    cell?.run {
                        textManagerId.text = manager.user_id
                        textManagerName.text = manager.user_name
                        textManagerCreater.text = manager.creater_name
                        textManagerType.text = manager.manager_type.name_ko
                        if( manager.is_block ) {
                            val color = Color.parseColor("#FFDDDDDD")
                            textManagerId.setTextColor(color)
                            textManagerName.setTextColor(color)
                            textManagerCreater.setTextColor(color)
                            textManagerType.setTextColor(color)
                            buttonManagerBlock.setText("해제")
                        } else {
                            val color = Color.parseColor("#FF444444")
                            textManagerId.setTextColor(color)
                            textManagerName.setTextColor(color)
                            textManagerCreater.setTextColor(color)
                            textManagerType.setTextColor(color)
                            buttonManagerBlock.setText("블록")
                        }
                        buttonManagerBlock.setOnClickListener {
                            APIManagerBlock.request(requestable,
                                manager.id,
                                !manager.is_block
                            ) { res ->
                                if( res.isSuccess() ) {
                                    val msg = if( manager.is_block ) "블럭해제 되었습니다." else "블럭 되었습니다."
                                    showAlert(msg, confirmCallback = {
                                        manager.is_block = !manager.is_block
                                        binding?.recyclerManagerList?.adapter?.notifyItemChanged(pos)
                                    })
                                } else {
                                    showAlert("${res.code}\n${res.msg}")
                                }
                            }
                        }

                        if(pos == managerList.managers.size - 1 && managerList.has_next) {
                            requestManagerList(managerList.keyword)
                        }
                    }
                }
            }
        }


        requestManagerList()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun requestManagerList(keyword: String = "", force: Boolean = false) {
        if(isLoading) {
            return
        }

        val page = if( force || keyword != managerList.keyword ) 1 else managerList.page + 1
        APIManagerList.request(requestable, page, keyword) { res ->
            if (res.isSuccess()) {
                val body = res.body
                if( body != null ) {
                    if (page == 1) {
                        managerList = body
                    } else {
                        managerList.managers += body.managers
                        managerList.page = body.page
                        managerList.has_next = body.has_next
                    }
                    binding?.recyclerManagerList?.adapter?.notifyDataSetChanged()
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