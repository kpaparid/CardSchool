package mainFragments;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.marmi.cardschool.R;
import com.example.marmi.cardschool.data.DatabaseHelper;
import com.example.marmi.cardschool.data.Language;
import com.example.marmi.cardschool.data.Word;
import com.example.marmi.cardschool.data.WordController;
import com.example.marmi.cardschool.data.WordModel;
import com.example.marmi.cardschool.normal.Translator;
import com.transitionseverywhere.ChangeText;
import com.transitionseverywhere.TransitionManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import fragments.EditBtn;
import fragments.FragmentLanguage;
import fragments.WiktionaryBtn;

import static android.graphics.Color.GREEN;

public class quizo extends templateFragment{
    ArrayList<WordController> words;
    ArrayList<Button> buttons;
    private Translator tr0;
    private Translator tr1;
    private Translator tr2;
    private Translator tr3;
    private TextView was;
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private int index;
    private WordController nextWord;
    private WordController currentRandom1;
    private WordController currentRandom2;
    private WordController currentRandom3;
    private WordController nextRandom1;
    private WordController nextRandom2;
    private WordController nextRandom3;
    private DatabaseHelper mDatabaseHelper;

    private View.OnClickListener btnListener = new View.OnClickListener() {
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btn1: {
                    control(btn1);
                    break;
                }

                case R.id.btn2: {
                    control(btn2);
                    break;
                }


                case R.id.btn3: {
                    control(btn3);
                    break;
                }
                case R.id.btn4: {
                    control(btn4);
                    break;
                }

            }

            System.out.println("Post Execute");
            wc = nextWord;
            currentRandom1 = nextRandom1;
            currentRandom2 = nextRandom2;
            currentRandom3 = nextRandom3;

            btn1.setClickable(false);
            btn2.setClickable(false);
            btn3.setClickable(false);
            btn4.setClickable(false);

            final Runnable r = new Runnable() {
                public void run() {
                    if (!dtb.moveToNext()) {
                        dtb.moveToFirst();
                    }
                    resetGUI();
                    update(wc, currentRandom1, currentRandom2, currentRandom3);
                    randomGenerator(nextWord.getType());
                    btn1.setClickable(true);
                    btn2.setClickable(true);
                    btn3.setClickable(true);
                    btn4.setClickable(true);
                }
            };
            handler.postDelayed(r, 1700);


        }
    };

    @Override
    public int getLayoutID(){
        return R.layout.fr_quiz;
    }

    private void initV() {
        if(dtb == null){
            System.out.println("wtf InitV");
        }
        wc.importWord(dtb);
        randomGenerator(wc.getType());
        nextWord = wc;
        currentRandom1 = nextRandom1;
        currentRandom2 = nextRandom2;
        currentRandom3 = nextRandom3;
        update(wc, currentRandom1, currentRandom2, currentRandom3);
        if (!dtb.moveToNext()) {
            dtb.moveToFirst();
        }
        WordModel wm = new WordModel();
        nextWord = new WordController();
        nextWord.importWord(dtb);
        randomGenerator(nextWord.getType());
        System.out.println("Finish InitV");
    }

    private void randomGenerator(String type) {
        String query = " WHERE rate >= " + 0 + " AND rate <= " + 10 + " AND type = '" + type + "' ORDER BY RANDOM() LIMIT 3";
        mDatabaseHelper = new DatabaseHelper(getContext());
        Cursor randomWords = mDatabaseHelper.getRandom(3, query);
        nextRandom1 = new WordController();
        nextRandom1.importWord(randomWords);
        randomWords.moveToNext();
        nextRandom2 = new WordController();
        nextRandom2.importWord(randomWords);
        randomWords.moveToNext();
        nextRandom3 = new WordController();
        nextRandom3.importWord(randomWords);
        randomWords.moveToNext();
        mDatabaseHelper.close();

    }

    private void wrong(Button btn) {
        btn.setBackgroundColor(Color.RED);
        Button rightbtn = buttons.get(index);
        right(rightbtn);
    }

    private void right(Button btn) {
        btn.setBackgroundColor(GREEN);
    }

    private void control(Button btn) {
        String text = btn.getText().toString();
        if (text == wc.getTranslated(target)) {
            right(btn);
        } else {
            wrong(btn);
        }
    }



    private void resetGUI() {
        btn1.setBackgroundColor(Color.parseColor("#AD7F2D"));
        btn2.setBackgroundColor(Color.parseColor("#AD7F2D"));
        btn3.setBackgroundColor(Color.parseColor("#AD7F2D"));
        btn4.setBackgroundColor(Color.parseColor("#AD7F2D"));
    }


    public void update(final WordController word, final WordController random1, final WordController random2, final WordController random3) {

        words = new ArrayList<>();
        words.add(word);
        words.add(random1);
        words.add(random2);
        words.add(random3);

        Collections.shuffle(words);
        index = words.indexOf(word);

        buttons = new ArrayList<Button>();
        buttons.add(btn1);
        buttons.add(btn2);
        buttons.add(btn3);
        buttons.add(btn4);

        anim(word);
    }

    private void anim(final WordController word) {

        ConstraintLayout transitionsContainer = v.findViewById(R.id.contentlayout);
        TransitionManager.beginDelayedTransition(transitionsContainer, new ChangeText().setChangeBehavior(ChangeText.CHANGE_BEHAVIOR_OUT_IN));
        btn1.setText(words.get(0).getTranslated(target));
        btn2.setText(words.get(1).getTranslated(target));
        btn3.setText(words.get(2).getTranslated(target));
        btn4.setText(words.get(3).getTranslated(target));
        wordTxt.setText(word.getWordText());
    }


    @Override
    public void initGUI() {

        addFragment("wiki","b");
        addFragment("edit","c");
        addFragment("language","a");
        wordTxt = v.findViewById(R.id.word);
        wordTxt.setTextColor(Color.parseColor("#C69E4B"));
        was = v.findViewById(R.id.was);
        was.setTextColor(Color.parseColor("#8EAE95"));
        was.setVisibility(View.VISIBLE);

        btn1 = v.findViewById(R.id.btn1);
        btn2 = v.findViewById(R.id.btn2);
        btn3 = v.findViewById(R.id.btn3);
        btn4 = v.findViewById(R.id.btn4);


        btn1.setOnClickListener(btnListener);
        btn2.setOnClickListener(btnListener);
        btn3.setOnClickListener(btnListener);
        btn4.setOnClickListener(btnListener);


        ConstraintLayout backlayout = v.findViewById(R.id.background);
        backlayout.setBackgroundColor(Color.parseColor("#52444C"));

        layout = v.findViewById(R.id.mainlayout);

        layout.setBackgroundColor(Color.parseColor("#1F2018"));

        resetGUI();

    }
    @Override
    public void backGround(){
        System.out.println("init db");
        progressBar = v.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        initDB(" WHERE rate >= " + nfrom + " AND rate <= " + nto +" "+ mode + " ORDER BY RANDOM()");
    }
    @Override
    public void postExecute(){
        System.out.println("init gui");
        initGUI();
        resetGUI();
        initV();
        btn1.setVisibility(View.VISIBLE);
        btn2.setVisibility(View.VISIBLE);
        btn3.setVisibility(View.VISIBLE);
        btn4.setVisibility(View.VISIBLE);
        wordTxt.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    final Handler handler = new Handler();

    public void onInputLanguage(final CharSequence input) {

        if (!input.equals("Error")) {
            target = input.toString();
            resetGUI();
            update(wc, currentRandom1, currentRandom2, currentRandom3);
        }
    }

}
