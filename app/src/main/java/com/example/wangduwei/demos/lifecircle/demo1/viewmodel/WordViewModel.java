package com.example.wangduwei.demos.lifecircle.demo1.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.wangduwei.demos.lifecircle.demo1.data.Word;
import com.example.wangduwei.demos.lifecircle.demo1.data.WordRepository;

import java.util.List;

/**
 * @desc:
 * @auther:duwei
 * @date:2018/11/8
 */

public class WordViewModel  extends AndroidViewModel {

    private WordRepository mRepository;

    private LiveData<List<Word>> mAllWords;

    public WordViewModel (Application application) {
        super(application);
        mRepository = new WordRepository(application);
        mAllWords = mRepository.getAllWords();
    }

    public LiveData<List<Word>> getAllWords() { return mAllWords; }

    public void insert(Word word) { mRepository.insert(word); }
}
