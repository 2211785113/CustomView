package com.example.ruru.customview.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.ruru.customview.R;
import com.example.ruru.customview.view.SearchView;

public class SearchActivity extends AppCompatActivity {

  private final String TAG = getClass().getName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);

    SearchView searchView = findViewById(R.id.searchView);
    searchView.setOnSearchTextChangedListener(content ->
        Log.d(TAG, "onCreate: 执行搜索功能")
    );
    // 注意：Edittext设置了inputType或singleLine，imeOptions属性才会生效。
    searchView.setOnSearchEditorActionListener(content ->
        Log.d(TAG, "onCreate: 键盘执行搜索功能")
    );
  }
}
