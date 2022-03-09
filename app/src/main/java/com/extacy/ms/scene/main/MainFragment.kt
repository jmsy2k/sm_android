package com.extacy.ms.scene.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.extacy.ms.R
import com.extacy.ms.base.RVAdapter
import com.extacy.ms.base.ViewBindingFragment
import com.extacy.ms.databinding.CellStoreListBinding
import com.extacy.ms.databinding.FragmentMainBinding
import com.extacy.ms.net.ms.api.APIAddStore
import com.extacy.ms.net.ms.api.APIStoreList
import com.extacy.ms.net.ms.api.StoreList
import com.extacy.ms.scene.game.GameInfoFragment
import com.extacy.ms.scene.game.GameListFragment


// 네비게이션

//* 매장 목록
//* 매장 추가 버튼
//* -> 매장 추가(추가 되면 해당 매장 검색)
// 매장 정보로 이동
// -> 매장 정보(기기 목록, 매장 입출금 상세 화면으로 이동)
// ->-> 기기 상세( 입출금 목록 표시)
// ->-> 매장 입출금 상세( 시간 선택 조회 )
// 게임 목록으로 이동버튼
// -> 게임 목록(게임 목록 리스팅 선택되면 정보 화면으로 이동)
// ->->게임 정보(이벤트 수정 가능)
// ->->-> 이벤트 추가/수정
// -> 게임 추가
// 관리자 추가 버튼
// -> 관리자 추가 화면
// 메인에 표시 될 기능



class MainFragment: ViewBindingFragment<FragmentMainBinding>() {
    var storeList = StoreList()
    var isLoading = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonAddStore.setOnClickListener {
            val view = layoutInflater.inflate(R.layout.dialog_editable, null)
            val editText = view.findViewById(R.id.edit) as EditText
            var builder = AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.add_store))
                .setView(view)
                .setPositiveButton(getString(R.string.confirm)) { dialogInterface, i ->
                    if (editText.text.isEmpty()) {
                        showAlert("이름을 입력해주세요.")
                        return@setPositiveButton
                    }

                    val storeName = editText.text.toString()
                    APIAddStore.request(requestable, storeName) { res ->
                        if (res.isSuccess()) {
                            showAlert("추가 되었습니다.", confirmCallback = {
                                binding.editSearchStore.setText(storeName)
                                requestStoreList(storeName)

                                dialogInterface.dismiss()
                            })
                        } else {
                            showAlert("${res.code}\n${res.msg}")
                        }
                    }
                }
                .setNegativeButton(getString(R.string.cancel)) { dialogInterface, i ->
                    dialogInterface.dismiss()
                }
            builder.create().show()

        }

        binding.buttonGameList.setOnClickListener {
            val fragment = GameListFragment()
            pushFragment(fragment)
        }

        binding.buttonAddManager.setOnClickListener {


        }

        binding.buttonSearch.setOnClickListener {
            requestStoreList(binding.editSearchStore.text.toString(), true)
        }

        binding.editSearchStore.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                requestStoreList(binding.editSearchStore.text.toString(), true)
                return@setOnKeyListener true
            }
            false
        }


        binding.recyclerStoreList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerStoreList.adapter = object: RVAdapter<CellStoreListBinding>() {
            override fun count(): Int {
                return storeList.stores.size
            }

            override fun bind(cell: CellStoreListBinding, pos: Int) {
                with(cell) {
                    textStoreName.text = storeList.stores[pos].name
                    root.setOnClickListener {
                        val fragment = StoreInfoFragment()
                        val arg = Bundle()
                        arg.putInt("id", storeList.stores[pos].id)
                        fragment.arguments = arg
                        pushFragment(fragment)
                    }

                    if(pos == storeList.stores.size - 1 && storeList.has_next) {
                        requestStoreList(storeList.keyword)
                    }
                }
            }
        }
        requestStoreList()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun requestStoreList(keyword: String = "", force: Boolean = false) {
        if(isLoading) {
            return
        }

        val page = if( force || keyword != storeList.keyword ) 1 else storeList.page + 1
        APIStoreList.request(requestable, page, keyword) { res ->
            if (res.isSuccess()) {
                val body = res.body
                if( body != null ) {
                    if (page == 1) {
                        storeList = body
                    } else {
                        storeList.stores += body.stores
                        storeList.page = body.page
                        storeList.has_next = body.has_next
                    }
                    binding.recyclerStoreList.adapter?.notifyDataSetChanged()
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
