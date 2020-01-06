package com.example.ruru.customview.view

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.ruru.customview.R
import com.example.ruru.customview.util.DensityUtil
import kotlinx.android.synthetic.main.activity_fire_work.view.*
import kotlinx.android.synthetic.main.layout_search.view.*

/**
 * Created by lyr on 2020/1/5 & content is 搜索框
 */
class SearchView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int = 0):
  RelativeLayout(context, attributeSet, defStyleAttr), View.OnClickListener, TextWatcher, TextView.OnEditorActionListener {

  private lateinit var listener: OnSearchTextChangedListener
  private lateinit var editorListener: OnSearchEditorActionListener

  init {
    initView()
  }

  private fun initView() {
    LayoutInflater.from(context).inflate(R.layout.layout_search, this, true)

    et_name.setOnClickListener(this)
    btn_clear.setOnClickListener(this)

    et_name.addTextChangedListener(this)
    et_name.setOnEditorActionListener(this)

    val drawable = resources.getDrawable(R.drawable.search, null)
    drawable.setBounds(0, 0, 30, 30)
    et_name.setCompoundDrawables(drawable, null, null, null)
  }

  override fun onClick(v: View?) {
    if (v?.id == R.id.btn_clear) {
      et_name.text.clear()  // et_name.setText("")
    } else {
      et_name.requestFocus()
      et_name.post {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(et_name, 0)
      }
    }
  }

  override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

  }

  override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
  }

  override fun afterTextChanged(s: Editable) {
    btn_clear.visibility = if (s.isNotEmpty()) View.VISIBLE else View.GONE
    if (listener != null) {
      listener.onSearchTextChange(s.toString().trim())
    }
  }

  override fun onEditorAction(textView: TextView, actionId: Int, event: KeyEvent?): Boolean {
    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
      if (editorListener != null) {
        editorListener.onSearchEditorAction(textView.text.toString().trim())
      }
      return true // 返回true，保留软键盘。false，隐藏软键盘。
    }
    return false
  }

  open fun setOnSearchTextChangedListener(listener: OnSearchTextChangedListener) {
    this.listener = listener
  }

  /**
   * 点击软键盘上的键才会触发
   */
  open fun setOnSearchEditorActionListener(editorListener: OnSearchEditorActionListener) {
    this.editorListener = editorListener
  }

  interface OnSearchTextChangedListener {
    fun onSearchTextChange(content: String)
  }

  interface OnSearchEditorActionListener {
    fun onSearchEditorAction(content: String)
  }
}

