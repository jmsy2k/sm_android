package com.extacy.ms.scene.game

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
import com.extacy.ms.databinding.CellGameListBinding
import com.extacy.ms.databinding.FragmentGameListBinding
import com.extacy.ms.net.ms.api.APIAddGame
import com.extacy.ms.net.ms.api.APIGameList
import com.extacy.ms.net.ms.api.GameList

class GameListFragment: ViewBindingFragment<FragmentGameListBinding>() {
    var gameList = GameList()
    var isLoading = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonAddGame.setOnClickListener {
            val view = layoutInflater.inflate(R.layout.dialog_editable, null)
            val editText = view.findViewById(R.id.edit) as EditText
            var builder = AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.add_game))
                .setView(view)
                .setPositiveButton(getString(R.string.confirm)) { dialogInterface, i ->
                    if (editText.text.isEmpty()) {
                        showAlert("이름을 입력해주세요.")
                        return@setPositiveButton
                    }

                    val gameName = editText.text.toString()
                    APIAddGame.request(requestable, gameName) { res ->
                        if (res.isSuccess()) {
                            showAlert("추가 되었습니다.", confirmCallback = {
                                binding.editSearchGame.setText(gameName)
                                requestGameList(gameName)
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


        binding.buttonSearch.setOnClickListener {
            requestGameList(binding.editSearchGame.text.toString(), true)
        }

        binding.editSearchGame.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                requestGameList(binding.editSearchGame.text.toString(), true)
                return@setOnKeyListener true
            }
            false
        }

        binding.recyclerGameList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerGameList.adapter = object: RVAdapter<CellGameListBinding>() {
            override fun count(): Int {
                return gameList.games.size
            }

            override fun bind(cell: CellGameListBinding, pos: Int) {
                with(cell) {
                    textGameName.text = gameList.games[pos].name
                    root.setOnClickListener {
                        val fragment = GameInfoFragment()
                        val arg = Bundle()
                        arg.putInt("id", gameList.games[pos].id)
                        arg.putString("name", gameList.games[pos].name)
                        fragment.arguments = arg
                        pushFragment(fragment)
                    }
                    if(pos == gameList.games.size - 1 && gameList.has_next) {
                        requestGameList(gameList.keyword)
                    }
                }
            }
        }

        requestGameList()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun requestGameList(keyword: String = "", force: Boolean = false) {
        if(isLoading) {
            return
        }

        val page = if( force || keyword != gameList.keyword ) 1 else gameList.page + 1
        APIGameList.request(requestable, page, keyword) { res ->
            if (res.isSuccess()) {
                val body = res.body
                if( body != null ) {
                    if (page == 1) {
                        gameList = body
                    } else {
                        gameList.games += body.games
                        gameList.page = body.page
                        gameList.has_next = body.has_next
                    }
                    binding.recyclerGameList.adapter?.notifyDataSetChanged()
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